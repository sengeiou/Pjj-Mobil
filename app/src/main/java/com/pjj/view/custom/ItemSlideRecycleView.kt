package com.pjj.view.custom

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.view.*

/**
 * Created by XinHeng on 2019/03/04.
 * describe：子item可滑动的recycleview
 */
@SuppressLint("ObjectAnimatorBinding")
class ItemSlideRecycleView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    private val TAG = javaClass.simpleName
    private var lastX = 0f
    private var lastY = 0f
    /**
     * ACTION_DOWN 时的 坐标记录
     */
    private var downLastX = 0f
    private var downLastY = 0f
    /**
     * 有效滑动距离最小值
     */
    private var mMinSlide: Int
    /**
     * 滑动有效标志
     * 一旦有效，接下来所有的动作都和滑动菜单有关，其他忽略
     */
    private var slideEffiectiveTag = false
    private var mMenuView: ViewGroup? = null
    /**
     * 目标view是否完全展开
     */
    private var mMenuShowAllTag = false
    /**
     * 当前view的第二个子元素宽度
     * 即菜单的宽度
     */
    private var mMenuWidth = 0
    /**
     * 最小有效滑动速度
     * 向左滑，速度为负数
     */
    private val MIN_SPEED = -400
    private val mTouchFrame: Rect by lazy {
        Rect()
    }
    private val mVelocityTracker: VelocityTracker by lazy {
        VelocityTracker.obtain()
    }
    private val objectUpdateListener: ValueAnimator.AnimatorUpdateListener by lazy {
        ValueAnimator.AnimatorUpdateListener {
            if (it is ObjectAnimator) {
                var target = it.target as View
                var slide = it.animatedValue as Int
                target.scrollTo(slide, 0)
            }
        }
    }
    private val closeAnimator: ObjectAnimator by lazy {
        ObjectAnimator.ofInt(this@ItemSlideRecycleView, "slideClose", 0, 0)
                .apply {
                    duration = 200
                    addUpdateListener(objectUpdateListener)
                }
    }
    private val openAnimator: ObjectAnimator by lazy {
        ObjectAnimator.ofInt(this@ItemSlideRecycleView, "slideOpen", 0, 0)
                .apply {
                    duration = 200
                    addUpdateListener(objectUpdateListener)
                }
    }
    /**
     * onTouchEvent Down 执行标志
     */
    private var onTouchEventDownTag = false

    init {
        val configuration = ViewConfiguration.get(context)
        mMinSlide = configuration.scaledTouchSlop
    }

    override fun onInterceptTouchEvent(e: MotionEvent): Boolean {
        mVelocityTracker.addMovement(e)
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                //关闭上一个菜单
                closeNowMenu(e.x.toInt(), e.y.toInt())
                //记录坐标值
                updateDownLastXY(e.x, e.y)
                updateLastXY(e.x, e.y)
            }
            MotionEvent.ACTION_MOVE -> {
                if (checkEffectiveSlide(e.x, e.y)) {
                    //查找当前菜单
                    findMotionView(downLastX.toInt(), downLastY.toInt())
                    slideEffiectiveTag = true
                    return true
                }
            }
            MotionEvent.ACTION_UP -> {
                if (slideEffiectiveTag) {
                    return true
                }
            }
        }
        var onInterceptTouchEvent = super.onInterceptTouchEvent(e)
        Log.e(TAG, "onInterceptTouchEvent: result=$onInterceptTouchEvent")
        return onInterceptTouchEvent
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        mVelocityTracker.addMovement(e)
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                updateLastXY(e.x, e.y)
                updateDownLastXY(e.x, e.y)
                Log.e(TAG, "onTouchEvent: ACTION_DOWN")
                //findMotionView(e.x.toInt(), e.y.toInt())
                onTouchEventDownTag = true
            }
            MotionEvent.ACTION_MOVE -> {
                Log.e(TAG, "onTouchEvent: ACTION_MOVE $onTouchEventDownTag")
                var slide = checkEffectiveSlideLength(e.x, e.y)
                Log.e(TAG, "onTouchEvent: slide=$slide  $slideEffiectiveTag")
                if (slide > 0f) {
                    slideEffiectiveTag = true
                    moveToMenuView(slide.toInt())
                    return true
                }
                if (slideEffiectiveTag || mMenuShowAllTag) {
                    return true
                }
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                Log.e(TAG, "onTouchEvent: ACTION_UP $slideEffiectiveTag")
                //此标志恢复初始值
                onTouchEventDownTag = false
                if (slideEffiectiveTag) {
                    if (!mMenuShowAllTag)
                        upFinalMoveToMenuView()
                    //此标志恢复初始值
                    slideEffiectiveTag = false
                    return true
                } else {
                    //若没有有效滑动，但已展开，则关闭菜单
                    if (mMenuShowAllTag) {
                        closeMenu()
                    }
                }
            }
        }
        var onTouchEvent = super.onTouchEvent(e)
        Log.e(TAG, "onTouchEvent: result=$onTouchEvent  action=${MotionEvent.actionToString(e.action)}")
        return onTouchEvent
    }

    private fun closeNowMenu(x: Int, y: Int) {
        if (null != mMenuView && mMenuShowAllTag) {
            if (!isNowMenu(x, y)) {
                closeMenu(getRealMenuView(mMenuView!!))
                //mMenuShowAllTag = false
                slideEffiectiveTag = false
            }
        }
    }

    private fun findMotionView(x: Int, y: Int) {
        //检查是否为当前菜单
        if (isNowMenu(x, y)) {
            return
        }
        mMenuView = null
        val frame = mTouchFrame
        for (i in childCount - 1 downTo 0) {
            val child = getChildAt(i)
            if (child == mMenuView) {
                continue
            }
            if (child.visibility == View.VISIBLE) {
                child.getHitRect(frame)
                if (frame.contains(x, y)) {
                    //当前触碰的view
                    if (child is ViewGroup) {
                        mMenuView = getItemRealTouchMenuView(child, x, y)
                    }
                }
            }
        }
        //Log.e("TAG", "findMotionView: mMenuView is empty = ${mMenuView == null} ")
    }

    private fun getItemRealTouchMenuView(viewGroup: ViewGroup, x: Int, y: Int): ViewGroup? {
        val itemRealTouchMuneView = onItemSlideRecycleListener?.getItemRealTouchMuneView(viewGroup, x, y)
        return itemRealTouchMuneView
    }

    /**
     * 获取可滑动，且包含菜单的view
     * @param view item
     */
    private fun getRealMenuView(view: View): ViewGroup {
        return onItemSlideRecycleListener?.getRealCanSlideMenuView(view) ?: (view as ViewGroup)

    }

    private fun getRealSlideView(view: ViewGroup): ViewGroup {
        return onItemSlideRecycleListener?.getRealCanSlideMenuView(view) ?: view
    }

    /**
     * 是否为当前菜单
     */
    private fun isNowMenu(x: Int, y: Int): Boolean {
        mMenuView?.let {
            it.getHitRect(mTouchFrame)
            if (mTouchFrame.contains(x, y)) {
                return true
            }
        }
        return false
    }

    /**
     * 移动当前view
     */
    private fun moveToMenuView(slide: Int) {
        mMenuView?.let {
            val view = getRealSlideView(it)
            mMenuWidth = view.getChildAt(1).measuredWidth
            Log.e("TAG", "moveToMenuView: mMenuWidth=$mMenuWidth")
            if (view.scrollX + slide >= mMenuWidth) {
                //showMenu(view)
                view.scrollTo(mMenuWidth, 0)
                mMenuShowAllTag = true
            } else {
                mMenuShowAllTag = false
                view.scrollBy(slide, 0)
            }
        }
    }


    /**
     * 手指抬起，对目标view进行最后的移动
     * 即决定菜单是否展开或关闭
     */
    private fun upFinalMoveToMenuView() {
        mMenuView?.let { view ->
            val it = getRealSlideView(view)
            if (it.scrollX >= mMenuWidth / 2f || checkEffectiveSpeed()) {
                showMenu(it)
            } else {
                closeMenu(it)
            }
        }
    }

    /**
     * 菜单展开
     */
    private fun showMenu(view: View) {
        startOpenAnimator(view, view.scrollX, mMenuWidth)
        mMenuShowAllTag = true
    }

    /**
     * 菜单关闭
     */
    private fun closeMenu(view: View) {
        if (closeAnimator.isRunning) {
            closeAnimator.cancel()
            (closeAnimator.target as View).scrollTo(0, 0)
        }
        startCloseAnimator(view, view.scrollX, 0)
        Log.e(TAG, "closeMenu: ${view.hashCode()}")
        mMenuShowAllTag = false
    }

    fun closeMenu() {
        mMenuView?.let { view ->
            val it = getRealSlideView(view)
            closeMenu(it)
        }
    }

    private fun updateDownLastXY(x: Float, y: Float) {
        downLastX = x
        downLastY = y
    }

    private fun updateLastXY(x: Float, y: Float) {
        lastX = x
        lastY = y
    }

    /**
     * 检测滑动是否符合我们的要求
     */
    private fun checkEffectiveSlide(x: Float, y: Float): Boolean {
        if (checkEffectiveSpeed()) {
            return true
        }
        var changeX = lastX - x
        var changeY = lastY - y
        if (changeX > mMinSlide && changeX > Math.abs(changeY)) {//水平向右滑动
            return true
        }
        return false
    }

    /**
     * 检测滑动速度是否符合我们的要求
     */
    private fun checkEffectiveSpeed(): Boolean {
        mVelocityTracker.computeCurrentVelocity(1000)
        return mVelocityTracker.xVelocity <= MIN_SPEED
    }

    /**
     * 检测有效的滑动距离
     * 即是否符合我们要求的滑动
     */
    private fun checkEffectiveSlideLength(x: Float, y: Float): Float {
        var changeX = lastX - x
        var changeY = lastY - y
        Log.e(TAG, "checkEffectiveSlideLength: changeX=$changeX changeY=$changeY  $mMinSlide  $onTouchEventDownTag")
        if (changeX > 0 && changeX > Math.abs(changeY)) {//水平向右滑动
            if (onTouchEventDownTag) {
                findMotionView(downLastX.toInt(), downLastY.toInt())
                //此标志恢复初始值，已找到当前触摸的view
                onTouchEventDownTag = false
            }
            return changeX
        }
        updateLastXY(x, y)
        return -1f
    }

    private fun startCloseAnimator(view: View, start: Int, end: Int) {
        closeAnimator.target = view
        closeAnimator.setIntValues(start, end)
        closeAnimator.start()
    }

    private fun startOpenAnimator(view: View, start: Int, end: Int) {
        openAnimator.target = view
        openAnimator.setIntValues(start, end)
        openAnimator.start()
    }

    var onItemSlideRecycleListener: OnItemSlideRecycleListener? = null

    interface OnItemSlideRecycleListener {
        /**
         * 获取真正可滑动view
         * @param view recycleview列表的item
         */
        fun getRealCanSlideMenuView(view: View): ViewGroup?

        /**
         * 获取item 真正触碰的可滑动的子类
         */
        fun getItemRealTouchMuneView(viewGroup: ViewGroup, x: Int, y: Int): ViewGroup?
    }
}