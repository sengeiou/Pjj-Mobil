package com.pjj.view.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.pjj.module.DiyDataBean

/**
 * Create by xinheng on 2018/11/09。
 * describe：
 */
class CustomShapeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : View(context, attrs, defStyleAttr) {
    private var shape: Shape? = null
    //    private var bitmap: Bitmap? = null
    //    private var imgPath: String? = null
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val color = Color.parseColor("#A63E3E3E")
    //    private val color = ContextCompat.getColor(context, R.color.color_40bbf7)
    private var emptyWith: Float = 0f
    private var emptyHeight: Float = 0f
    private var toLeft: Float = 0f
    private var toTop: Float = 0f
    var rectF: RectF = RectF()
        private set
    private var path = Path()


    init {
        paint.isAntiAlias = true
        paint.isDither = true
        //paint.textAlign
        background = ColorDrawable(Color.TRANSPARENT)
        paint.isAntiAlias = true
        paint.isDither = true
        paint.style = Paint.Style.FILL
    }

    enum class Shape {
        圆, 矩形, 椭圆
    }

    private fun setShape(shape: Shape) {
        if (shape != this.shape) {
            this.shape = shape
        }
        if (measuredWidth > 0)
            invalidate()
    }

    /* fun setImageSrc(path: String) {
         if (path != imgPath) {
             this.imgPath = path
             if (measuredWidth > 0)
                 invalidate()
         }
     }*/

    fun setShape(dataBean: DiyDataBean.DataBean): CustomShapeView {
        emptyWith = dataBean.wide.toFloat()
        emptyHeight = dataBean.high.toFloat()
        setShape(getShape(dataBean.isCircle))
        return this
    }

    private fun getShape(type: String): Shape {
        return when (type) {
            "1" -> Shape.圆
            else -> Shape.矩形
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        path = getClipPath()
    }
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        val saveLayer: Int = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.saveLayer(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), null)
        } else {
            canvas.saveLayer(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        }
        paint.color = color
        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        paint.color = Color.RED
        canvas.drawPath(path, paint)
        paint.xfermode = null
        canvas.restoreToCount(saveLayer)
    }

    private fun getClipPath(): Path {
        if (shape == Shape.圆 && emptyHeight != emptyWith) {
            emptyHeight = Math.min(emptyHeight, emptyWith)
            emptyWith = emptyHeight
        }
        path.reset()
        toLeft = (measuredWidth - emptyWith) / 2f
        toTop = (measuredHeight - emptyHeight) / 2f
        rectF.set(toLeft, toTop, toLeft + emptyWith, toTop + emptyHeight)
        setPathContent(path, rectF)
        path.close()
        return path
    }

    private fun setPathContent(path: Path, rectF: RectF) {
        when (shape) {
            Shape.矩形 -> path.addRect(rectF, Path.Direction.CW)
            else -> path.addOval(rectF, Path.Direction.CW)
        }
    }

    fun cropImage(view: View): Bitmap {
        //未裁剪
        var bitOld = Bitmap.createBitmap(emptyWith.toInt(), emptyHeight.toInt(), Bitmap.Config.ARGB_8888)
        val canvasOld = Canvas(bitOld)
        //canvas.drawFilter = PaintFlagsDrawFilter(0, Paint.FILTER_BITMAP_FLAG or Paint.ANTI_ALIAS_FLAG)
        canvasOld.drawColor(Color.TRANSPARENT)
        canvasOld.translate(-toLeft, -toTop)
        view.draw(canvasOld)
        //裁剪
        var bitNew = Bitmap.createBitmap(emptyWith.toInt(), emptyHeight.toInt(), Bitmap.Config.ARGB_8888)
        val canvasNew = Canvas(bitNew)
        canvasNew.drawColor(Color.TRANSPARENT)
        var saveLayer = canvasOld.saveLayer(0f, 0f, canvasOld.width.toFloat(), canvasOld.height.toFloat(), null, Canvas.ALL_SAVE_FLAG)
        canvasNew.drawBitmap(bitOld, 0f, 0f, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_IN)
        var path = Path()
        setPathContent(path, RectF(0f, 0f, emptyWith, emptyHeight))
        path.close()
        var bitShape = getPathBitmap(path)
        canvasNew.drawBitmap(bitShape, 0f, 0f, paint)
        paint.xfermode = null
        canvasNew.restoreToCount(saveLayer)
        bitOld.recycle()
        bitShape.recycle()
        return bitNew
    }

    private fun getPathBitmap(path: Path): Bitmap {
        var bit = Bitmap.createBitmap(emptyWith.toInt(), emptyHeight.toInt(), Bitmap.Config.ARGB_8888)
        var canvas = Canvas(bit)
        canvas.drawColor(Color.TRANSPARENT)
        var paint = Paint().apply {
            isAntiAlias = true
            isDither = true
            style = Paint.Style.FILL
            color = Color.YELLOW
        }
        canvas.drawPath(path, paint)
        return bit
    }
}