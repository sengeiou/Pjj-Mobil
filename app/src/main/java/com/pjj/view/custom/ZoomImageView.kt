package com.pjj.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import com.pjj.utils.Log


class ZoomImageView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ImageView(context, attrs, defStyleAttr) {
    companion object {
        private val TAG = "ZoomImageView"
        // We can be in one of these 3 states
        internal val NONE = 0
        internal val DRAG = 1
        internal val ZOOM = 2
        /**
         * X轴不允许缩小
         */
        val NOT_ALLOW_REDUCE_SCALE_X = 10
        /**
         * Y轴不允许缩小
         */
        val NOT_ALLOW_REDUCE_SCALE_Y = 11
    }

    private var outWidth = 0
    private var outHeight = 0
    private val matrixFitCenterSave = Matrix()
    var outRect: Rect = Rect()
        private set(value) = field.set(value)

    fun setOutSlide(width: Int, height: Int) {
        outWidth = width
        outHeight = height
        if (getWidth() > 0) {
            invalidate()
        }
        postDelayed({
            if (outHeight > 0 && outWidth > 0 && null != drawable) {
                scaleType = ScaleType.MATRIX
                //重新设置放大倍数，适应外部框
                val matrix = matrixFitCenterSave
                matrix.set(imageMatrix)
                val values = FloatArray(9)
                matrix.getValues(values)
                val rect = drawable.bounds
                val scale = values[0]
                val realHeight = scale * rect.height()
                if (rect.width() > rect.height()) {
                    val scaleXY = outHeight / realHeight
                    Log.e("TAG", "onDraw scaleXY=$scaleXY")
                    matrix.postScale(scaleXY, scaleXY, measuredWidth / 2f, measuredHeight / 2f)
                    scalxMode = NOT_ALLOW_REDUCE_SCALE_Y
                    imageMatrix = matrix
                } else {
                    val realWidth = scale * rect.width()
                    if (realWidth >= outWidth && realHeight >= outHeight) {
                        scalxMode = NONE
                        //不做处理
                        val scaleXY = Math.min(realWidth / outWidth, realHeight / outHeight)
                        matrix.postScale(scaleXY, scaleXY, measuredWidth / 2f, measuredHeight / 2f)
                    } else {
                        val flWidth = outWidth / realWidth
                        val flHeight = outHeight / realHeight
                        var scaleXY: Float
                        if (flWidth > flHeight) {
                            scalxMode = NOT_ALLOW_REDUCE_SCALE_X
                            scaleXY = flWidth
                        } else {
                            scalxMode = NOT_ALLOW_REDUCE_SCALE_Y
                            scaleXY = flHeight
                        }
                        matrix.postScale(scaleXY, scaleXY, measuredWidth / 2f, measuredHeight / 2f)
                        imageMatrix = matrix
                    }
                }
            } else {
                Log.e("TAG", "adfadf")
            }
        }, 100)
    }

    private fun logImageSize(matrix: Matrix) {
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

    private var matrixChange = Matrix()
    var savedMatrix = Matrix()
    private var mode = NONE
    private var scalxMode = NONE
    /**
     * 中心点，用于缩放
     */
    private var mid = PointF()
    private var oldDist = 1f
    private var start = PointF()
    private var rectFFinal = RectF()
    @SuppressLint("DrawAllocation")

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        rectFFinal.setEmpty()
        drawable?.let {
            val rect = it.bounds
            Log.e("TAG", "rect=$rect")
            setDrawableRectF(it, imageMatrix, rectFFinal)
        }
    }

    private fun setDrawableRectF(drawable: Drawable, matrix: Matrix, rectF: RectF) {
        val width = drawable.bounds.width()
        val height = drawable.bounds.height()
        val values = FloatArray(9)
        matrix.getValues(values)
        val scale = values[Matrix.MSCALE_X]
        val left = values[Matrix.MTRANS_X]
        val top = values[Matrix.MTRANS_Y]
        rectF.set(left, top, scale * width + left, scale * height + top)
    }

    private fun checkBorder(matrix: Matrix) {
        val rectF=RectF()
        setDrawableRectF(drawable,matrix,rectF)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val view = this
        val matrix = matrixChange
        //设置支持缩放
        view.scaleType = ScaleType.MATRIX
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
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                //matrix.set(checkBorder(matrix))
                mode = NONE
            }
            MotionEvent.ACTION_MOVE -> if (mode == DRAG) {
                matrix.set(savedMatrix)
                var dx = event.x - start.x
                var dy = event.y - start.y
                when (scalxMode) {
                    NOT_ALLOW_REDUCE_SCALE_X -> dx = 0f
                    NOT_ALLOW_REDUCE_SCALE_Y -> dy = 0f
                }
                matrix.postTranslate(dx, dy)
            } else if (mode == ZOOM) {
                val newDist = spacing(event)
                if (newDist > 10f) {
                    val scale = newDist / oldDist
                    matrix.set(savedMatrix)
                    if (scale < 1f) {
                        return true
                    }
                    scalxMode = NONE
                    Log.e(TAG, "newDist=" + newDist + ", scale=" + scale + ", " + mid.x + " | " + mid.y);
                    matrix.postScale(scale, scale, mid.x, mid.y)
                }
            }
        }
        view.imageMatrix = matrix
        return true
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
}