package com.pjj.view.custom

import android.graphics.Matrix
import android.graphics.PointF
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import android.widget.ImageView

import com.pjj.utils.Log

class MulitPointTouchListener : OnTouchListener {
    // These matrices will be used to move and zoom image
    private var matrix = Matrix()
    var savedMatrix = Matrix()
    private var mode = NONE

    // Remember some things for zooming
    internal var start = PointF()
    var savedMatrixCenter = Matrix()
        set(value) = field.set(value)
    var savedBount = Rect()
        set(value) = field.set(value)

    /**
     * 中心点，用于缩放
     */
    private var mid = PointF()
    private var oldDist = 1f
    private var imageView: ImageView? = null
    var rectF: RectF? = null
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        val view = v as ImageView
        //设置支持缩放
        view.scaleType = ImageView.ScaleType.MATRIX
        if (v is ImageView) {
            imageView = view
        }
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                matrix.set(view.imageMatrix)
                savedMatrix.set(matrix)
                start.set(event.x, event.y)
                Log.e(TAG, "mode=DRAG")
                mode = DRAG
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDist = spacing(event)
                Log.e(TAG, "oldDist=$oldDist")
                if (oldDist > 10f) {
                    savedMatrix.set(matrix)
                    midPoint(mid, event)
                    mode = ZOOM
                    //Log.e(TAG, "mode=ZOOM");
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                mode = NONE
                matrix.set(checkBorder(matrix))
            }
            MotionEvent.ACTION_MOVE -> if (mode == DRAG) {
                matrix.set(savedMatrix)
                matrix.postTranslate(event.x - start.x, event.y - start.y)
            } else if (mode == ZOOM) {
                val newDist = spacing(event)
                if (newDist > 10f) {
                    matrix.set(savedMatrix)
                    val scale = newDist / oldDist
                    Log.e(TAG, "newDist=" + newDist + ", scale=" + scale + ", " + mid.x + " | " + mid.y);
                    matrix.postScale(scale, scale, mid.x, mid.y)
                }
            }
        }//Log.e(TAG, "mode=NONE");
        //Log.e(TAG, "onTouch: isIdentity=" + matrix.isIdentity() + ", " + view.getMatrix().isIdentity());
        //Log.e(TAG, "onTouch: " + (matrix.equals(view.getImageMatrix())));
        //logImageSize(view.imageMatrix,view.drawable)
        view.imageMatrix = matrix
        return true // indicate event was handled
    }

    private fun spacing(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return Math.sqrt((x * x + y * y).toDouble()).toFloat()
    }

    private fun midPoint(point: PointF, event: MotionEvent) {
        val x = event.getX(0) + event.getX(1)
        val y = event.getY(0) + event.getY(1)
        point.set(x / 2, y / 2)
    }

    companion object {

        private val TAG = "Touch"

        // We can be in one of these 3 states
        internal val NONE = 0
        internal val DRAG = 1
        internal val ZOOM = 2
    }

    private fun checkBorder(matrix: Matrix): Matrix {
        return rectF?.let {
            val values = FloatArray(9)
            matrix.getValues(values)
            var rect = imageView!!.drawable.bounds
            val left = values[Matrix.MTRANS_X]
            val top = values[Matrix.MTRANS_Y]
            val scale = values[Matrix.MSCALE_X]
            val fl_width = rect.width() * scale
            val right = fl_width + left
            val fl_height = rect.height() * scale
            val bottom = fl_height + top
            if (left <= it.left && top <= it.top && right >= it.right && bottom >= it.bottom) {
                return matrix
            } else {
                /*if (it.contains(left, top, right, bottom)) {
                    matrix.set(savedMatrixCenter)
                    var scaleXY = Math.max(it.height() - fl_height, it.width() - fl_width)
                    matrix.postScale(scaleXY, scaleXY, )
                }*/
                return changeMatrix(savedMatrixCenter)
            }
        } ?: matrix
    }

    private fun changeMatrix(matrix: Matrix): Matrix {
        val values = FloatArray(9)
        matrix.getValues(values)
        val rect = savedBount
        val scale = values[0]
        val outHeight = rectF!!.height()
        val outWidth = rectF!!.width()
        val realHeight = scale * rect.height()
        if (rect.width() > rect.height()) {
            val scaleXY = outHeight / realHeight
            Log.e("TAG", "onDraw scaleXY=$scaleXY")
            matrix.postScale(scaleXY, scaleXY, rectF!!.centerX(), rectF!!.centerY())
            return matrix
        } else {
            val realWidth = scale * rect.width()
            if (realWidth >= outWidth && realHeight >= outHeight) {
                //不做处理
                val scaleXY = Math.min(realHeight / outHeight, realWidth / outWidth)
                matrix.postScale(scaleXY, scaleXY, rectF!!.centerX(), rectF!!.centerY())
                return matrix
            } else {
                val scaleXY = Math.max(outHeight / realHeight, outWidth / realWidth)
                matrix.postScale(scaleXY, scaleXY, rectF!!.centerX(), rectF!!.centerY())
                return matrix
            }
        }
    }

    private fun logImageSize(matrix: Matrix, drawable: Drawable) {
        val rect = drawable.bounds
        val values = FloatArray(9)
        matrix.getValues(values)
        val left = values[2]
        val top = values[5]
        val scale = values[0]
        val scaleY = values[4]
        Log.e("TAG", "left=$left top=$top right=${rect.width() * scale + left} bottom=${rect.height() * scale + top}")
        Log.e("TAG", "scale=$scale  scaleY=$scaleY")
    }
}