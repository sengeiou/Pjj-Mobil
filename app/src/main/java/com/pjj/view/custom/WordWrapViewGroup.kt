package com.pjj.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.pjj.R
import com.pjj.utils.Log
import com.pjj.utils.ViewUtils

class WordWrapViewGroup<T : View, Data> @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {
    private var padding_v = ViewUtils.getDp(R.dimen.dp_10)
    private var padding_h = padding_v
    private var selectPosition = -1
    var index = 0
    /**
     * 不可选
     */
    private var unSelectPositionList = ArrayList<Int>()
    /**
     * 临时不可选
     */
    private var temporaryUnSelectPositionList = ArrayList<Int>()
    private lateinit var list: MutableList<Data>
    fun setContext(index: Int, title: String, list: MutableList<Data>) {
        this.index = index
        this.list = list
        unSelectPositionList.clear()
        val titleView = onWordWrapViewGroupListener.getTitleView(title)
        addView(titleView)
        list.forEachIndexed { index, it ->
            val childView = onWordWrapViewGroupListener.getChildView(it)
            if (onWordWrapViewGroupListener.initCheckCanSelect(it)) {
                if (onWordWrapViewGroupListener.checkDefaultSelect(it)) {
                    selectPosition = index
                    onWordWrapViewGroupListener.updateSelectView(childView, it, this.index)
                }
            } else {
                childView.isEnabled = false
                unSelectPositionList.add(index)
                onWordWrapViewGroupListener.setUnSelect(childView)
            }
            addView(childView)
            childView.setTag(R.id.position, index)
            childView.setOnClickListener(onClick)
        }
    }

    fun cancelSelectUpdateStatue() {
        temporaryUnSelectPositionList.forEach {
            onWordWrapViewGroupListener.restoreView(getChildAt(it + 1) as T)
        }
        temporaryUnSelectPositionList.clear()
    }

    fun updateCanSelectStatue(index: Int) {
        cancelSelectUpdateStatue()
        if (childCount > 1) {
            (1 until childCount).forEach {
                if (!unSelectPositionList.contains(it - 1)) {
                    val childView = getChildAt(it)
                    val position = childView.getTag(R.id.position) as Int
                    if (index == this.index) {
                        if (!onWordWrapViewGroupListener.checkCanSelectForSelfArray(list[position], this.index)) {
                            onWordWrapViewGroupListener.setUnSelect(childView as T)
                            temporaryUnSelectPositionList.add(position)
                        }
                    } else {
                        if (!onWordWrapViewGroupListener.checkCanSelect(list[position], this.index)) {
                            onWordWrapViewGroupListener.setUnSelect(childView as T)
                            temporaryUnSelectPositionList.add(position)
                        }
                    }
                }
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        if (childCount > 0) {
            val childAt0 = getChildAt(0)
            var top: Int
            var left = paddingLeft
            var right = 0
            var bottom = paddingTop + childAt0.measuredHeight
            val rightLimit = measuredWidth - paddingRight
            (1 until childCount).forEach {
                val childView = getChildAt(it)
                if (right == 0) {
                    right = left + childView.measuredWidth
                    top = bottom + padding_v
                    bottom = top + childView.measuredHeight
                } else {
                    if (right + padding_h + childView.measuredWidth > rightLimit) {
                        top = bottom + padding_v
                        left = paddingLeft
                        //right = left + childView.measuredWidth
                        bottom = top + childView.measuredHeight
                    } else {
                        left = right + padding_h
                    }
                    right = left + childView.measuredWidth
                }
            }
            setMeasuredDimension(width, bottom + paddingBottom)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount > 0) {
            val childAt0 = getChildAt(0)
            var top = paddingTop
            var left = paddingLeft
            var right = 0
            var bottom = paddingTop + childAt0.measuredHeight
            val rightLimit = measuredWidth - paddingRight
            childAt0.layout(left, top, left + childAt0.measuredWidth, bottom)
            (1 until childCount).forEach {
                val childView = getChildAt(it)
                if (right == 0) {
                    //right = left + childView.measuredWidth
                    top = bottom + padding_v
                    bottom = top + childView.measuredHeight
                } else {
                    if (right + padding_h + childView.measuredWidth > rightLimit) {
                        top = bottom + padding_v
                        left = paddingLeft
                        //right = left + childView.measuredWidth
                        bottom = top + childView.measuredHeight
                    } else {
                        left = right + padding_h
                    }
                }
                right = left + childView.measuredWidth
                childView.layout(left, top, right, bottom)
            }
        }
    }

    private val onClick = OnClickListener {
        val position = it.getTag(R.id.position) as Int
        if (!temporaryUnSelectPositionList.contains(position)) {
            notifyChangeView(position, it as T)
        }
    }

    private fun notifyChangeView(position: Int, newView: T) {
        Log.e("TAG", "position=$position selectPosition=$selectPosition")
        if (position == selectPosition) {
            selectPosition = -1
            onWordWrapViewGroupListener.restoreView(newView)
            onWordWrapViewGroupListener.cancelSelect(index)
            return
        }
        val old = selectPosition
        selectPosition = position
        if (old >= 0)
            onWordWrapViewGroupListener.restoreView(getChildAt(old + 1) as T)
        onWordWrapViewGroupListener.updateSelectView(newView, list[position], index)
    }

    lateinit var onWordWrapViewGroupListener: OnWordWrapViewGroupListener<T, Data>

    interface OnWordWrapViewGroupListener<T, Data> {
        fun getChildView(text: Data): T
        fun getTitleView(text: String): View
        fun restoreView(view: T)
        fun setUnSelect(view: T)
        fun updateSelectView(viewSelect: T, data: Data, index: Int)
        fun cancelSelect(index: Int)
        fun checkDefaultSelect(data: Data): Boolean
        fun initCheckCanSelect(data: Data): Boolean
        fun checkCanSelect(data: Data, index: Int): Boolean
        fun checkCanSelectForSelfArray(data: Data, index: Int): Boolean
    }
}