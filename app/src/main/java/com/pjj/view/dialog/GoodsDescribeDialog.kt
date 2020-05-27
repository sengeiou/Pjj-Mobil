package com.pjj.view.dialog

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.Gravity
import android.widget.*
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.pjj.R
import com.pjj.utils.Log
import com.pjj.utils.ViewUtils

class GoodsDescribeDialog(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    private var keyLL: LinearLayout
    private var valueLL: LinearLayout

    init {
        val relativeLayout = RelativeLayout(context).apply {
            addView(TextView(context).apply {
                text = "商品参数"
                setTextColor(ViewUtils.getColor(R.color.color_999999))
                setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_15))
                gravity = Gravity.CENTER
                layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_49))
                id = R.id.position
            })
            addView(ImageView(context).apply {
                layoutParams = RelativeLayout.LayoutParams(ViewUtils.getDp(R.dimen.dp_37), ViewUtils.getDp(R.dimen.dp_37)).apply {
                    addRule(RelativeLayout.ALIGN_PARENT_END)
                }
                setImageResource(R.mipmap.close)
                val dp13 = ViewUtils.getDp(R.dimen.dp_13)
                setPadding(dp13, dp13, dp13, dp13)
                setOnClickListener { dismiss() }
            })
            val linearLayout = LinearLayout(context).apply {
                orientation = LinearLayout.HORIZONTAL
                layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                addView(LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    keyLL = this
                    setPadding(ViewUtils.getDp(R.dimen.dp_12), 0, 0, 0)
                })
                addView(LinearLayout(context).apply {
                    orientation = LinearLayout.VERTICAL
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                    valueLL = this
                })
            }
            addView(ScrollView(context).apply {
                layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT).apply {
                    addRule(RelativeLayout.BELOW, R.id.position)
                }
                addView(linearLayout)
            })
        }
        setContentView(relativeLayout)
    }

    override fun getHeightRate(): Float {
        return 0.6f
    }

    override fun getWindowBgDrawable(): Drawable {
        return ViewUtils.getDrawable(R.drawable.shape_white_bg_8_lr)
    }

    fun setJsonArray(jsonArray: JsonArray?) {
        keyLL.removeAllViews()
        valueLL.removeAllViews()
        jsonArray?.forEach {
            val jsonObject = it.asJsonObject
            addTextView(getStringFromJsonObject(jsonObject, "title"), getStringFromJsonObject(jsonObject, "content"))
        }
    }

    private fun getStringFromJsonObject(jsonObject: JsonObject, key: String): String {
        val jsonElement = jsonObject[key]
        Log.e("TAG", "jsonElement: ${jsonElement?.asString}")
        return jsonElement?.asString ?: "未知$key"
    }

    private fun addTextView(key: String, value: String) {
        keyLL.addView(createTextView("$key："))
        valueLL.addView(createTextView(value))
    }

    private fun createTextView(text: String, keyTag: Boolean = false): TextView {
        return TextView(context).apply {
            this.text = text
            maxLines = 1
            val paddingRight = if (keyTag) ViewUtils.getDp(R.dimen.dp_23) else 0
            setPadding(0, 0, paddingRight, 0)
            setTextColor(if (keyTag) ViewUtils.getColor(R.color.color_999999) else ViewUtils.getColor(R.color.color_333333))
            gravity = Gravity.CENTER_VERTICAL
            setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_12))
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_43))
            background = object : Drawable() {
                val paint = Paint().apply {
                    isDither = true
                    isAntiAlias = true
                }

                override fun draw(canvas: Canvas) {
                    val dp1 = ViewUtils.getFDp(R.dimen.dp_1)
                    val rect = RectF(0f, bounds.height() - dp1, bounds.width().toFloat(), bounds.height().toFloat())
                    paint.color = ViewUtils.getColor(R.color.color_eeeeee)
                    canvas.drawRect(rect, paint)
                }

                override fun setAlpha(alpha: Int) {
                    paint.alpha = alpha
                }

                override fun getOpacity(): Int {
                    return PixelFormat.TRANSLUCENT
                }

                override fun setColorFilter(colorFilter: ColorFilter?) {
                    paint.colorFilter = colorFilter
                }
            }
        }
    }
}