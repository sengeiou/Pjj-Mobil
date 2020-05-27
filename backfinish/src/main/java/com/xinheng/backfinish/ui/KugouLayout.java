package com.xinheng.backfinish.ui;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Property;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.xinheng.backfinish.R;
import com.xinheng.backfinish.Spring;
import com.xinheng.backfinish.SpringConfig;
import com.xinheng.backfinish.SpringListener;
import com.xinheng.backfinish.SpringSystem;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by zzt on 2015/2/11.
 * 别人2015年写出的，现在是2019年2月22日11:19:39，隔了4年，我竟然在参考别人的
 * 丢人啊
 * 忘了，我是15年11月才工作
 */
public class KugouLayout extends ViewGroup {

    private static final String TAG = "KugouLayout";

    private LayoutCloseListener mLayoutCloseListener;
    private KugouLayout mKugouLayout;
    /**
     * 滑动的view
     */
    private UnClickableFrameLayout mContentContainer;
    private AnimatorSet mAnimatorSet;
    private ObjectAnimator mOffsetAnimator;
    private Interpolator mInterpolator = new DecelerateInterpolator();
    private SpringSystem mSpringSystem;
    private Spring mSpring;
    private ArrayList<View> scrollChildList = new ArrayList<View>();
    /**
     * 追踪手指滑动的速度
     */
    private VelocityTracker mVelocityTracker;
    private Activity mParentActivity;
    /**
     * 屏幕的宽度 viewgroup的宽度
     */
    private int mDragRange;
    /**
     * mDragRange的一般，关闭的临界点
     */
    private int mCloseDistance;
    private int mWidth;
    protected float mLastMotionX = -1;
    protected float mLastMotionY = -1;
    protected float mInitialMotionX;
    protected float mInitialMotionY;
    /**
     * mContentContainer的移动距离
     */
    protected float mOffsetPixels;
    /**
     * 未知
     */
    private boolean mBackgroundVisible;
    /**
     * 是否正在滑动
     */
    private boolean mIsDragging = false;
    private boolean doAnim = false;
    private boolean closingChangeAlpha = false;
    /**
     * 透明变化开关
     */
    private boolean showingChangeAlpha = false;
    protected static final int INVALID_POINTER = -1;
    protected static final int STATE_CLOSED = 0;
    protected static final int STATE_CLOSING = 1;
    protected static final int STATE_DRAGGING = 2;
    protected static final int STATE_OPENING = 4;
    protected static final int STATE_OPEN = 8;
    protected static final int LEFT = 0;
    protected static final int RIGHT = 1;
    public static final int NORMAL_ANIM = 0;
    public static final int REBOUND_ANIM = 1;
    public static final int ALWAYS_REBOUND = 2;
    private int mAnimType = 0;
    /**
     * 当前view的滑动状态 默认已关闭状态
     */
    protected int mLayoutState = STATE_CLOSED;
    protected int mActivePointerId = INVALID_POINTER;
    /**
     * 最小滑动距离ֵ
     */
    protected int mTouchSlop;
    /**
     * 最大滑动速度
     */
    protected int mMaxVelocity;
    private float mBeginOffsetX;
    /**
     * 动画执行时间
     */
    private int ANIM_DURATION = 300;
    /**
     * 是否大于Android4.0
     */
    public static final boolean USE_TRANSLATIONS = Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;

    public KugouLayout(Context context) {
        this(context, null);
    }

    public KugouLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public KugouLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
        mKugouLayout = this;
        if (context instanceof Activity)
            mParentActivity = (Activity) context;
    }

    private void init(Context context) {
        //背景透明
        setBackgroundColor(0x0);
        //initPaint();
        /**
         * Distance in pixels a touch can wander before we think the user is scrolling
         * */
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mTouchSlop = configuration.getScaledTouchSlop();
        mMaxVelocity = configuration.getScaledMaximumFlingVelocity();

        mContentContainer = new UnClickableFrameLayout(context);
        mContentContainer.setId(R.id.md__content);
        /**
         * init Property Animation
         * */
        mAnimatorSet = new AnimatorSet();
        mOffsetAnimator = ObjectAnimator.ofFloat(this, aOffset, 0, 0);
        mOffsetAnimator.setDuration(ANIM_DURATION);
        mAnimatorSet.playTogether(mOffsetAnimator);
        mAnimatorSet.setInterpolator(mInterpolator);
        /*mAnimatorSet.setInterpolator(new Interpolator() {
            @Override
            public float getInterpolation(float x) {
//                pow(2, -10 * x) * sin((x - factor / 4) * (2 * PI) / factor) + 1
                float factor = 0.3f;
                return (float) (Math.pow(2, -10 * x) * Math.sin((x - factor / 4) * (2 * Math.PI) / factor) + 1f);
                //return 0;
            }
        });*/

        mOffsetAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                float endValue = (Float) mOffsetAnimator.getAnimatedValue();
                //Log.e(TAG, "onAnimationEnd: " + endValue);
                //Log.i("TAG", "endValue="+endValue+", with="+getWidth());
                if ((endValue == getWidth() || endValue == -getWidth())) {
                    setVisibility(INVISIBLE);
                    if (null != mLayoutCloseListener) {
                        mLayoutCloseListener.onLayoutClose();
                    }
                } else if (endValue == 0 && showingChangeAlpha) {
                    showingChangeAlpha = false;
                }
                if (endValue <= 0) {//mOffsetPixels / 2
                    //mContentContainer.setTranslationX(0);//�ı��view��λ��
                    //scrollBackAnim();
                    //mContentContainer.setX(0);
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        /*
         * create rebound animator
         * 动画反弹
         */
        mSpringSystem = SpringSystem.create();
        mSpring = mSpringSystem.createSpring();
        SpringConfig config = new SpringConfig(70, 9);
        mSpring.setSpringConfig(config);
        mSpring.setCurrentValue(0);
        mSpring.addListener(new SpringListener() {
            @Override
            public void onSpringUpdate(Spring spring) {
                if (doAnim) {
                    double newValue = 1 - spring.getCurrentValue();
                    mOffsetPixels = (float) newValue * mBeginOffsetX;
                    moveContent();
                    if (showingChangeAlpha) {
                        changeAlpha();
                    }
                }
            }

            @Override
            public void onSpringAtRest(Spring spring) {

            }

            @Override
            public void onSpringActivate(Spring spring) {

            }

            @Override
            public void onSpringEndStateChange(Spring spring) {
            }
        });
        /**
         * add frame_layout mContentContainer as the parent of the activity's content view
         * */
        super.addView(mContentContainer, 0, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mContentContainer.setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    private void attachToContent(Activity activity, KugouLayout kugouLayout) {
        Window window = activity.getWindow();
        ViewGroup decorView = (ViewGroup) window.getDecorView();
        ViewGroup decorChild = (ViewGroup) decorView.getChildAt(0);

        decorView.removeAllViews();
        mKugouLayout.mContentContainer.addView(decorChild, decorChild.getLayoutParams());
        decorView.addView(mKugouLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        window.setBackgroundDrawable(new ColorDrawable(0x0));
    }

    public KugouLayout attach(Activity activity) {
        mKugouLayout.setId(R.id.md__drawer);
        attachToContent(activity, mKugouLayout);
        mKugouLayout.mContentContainer.setBackgroundColor(0x0);
        return mKugouLayout;
    }

    public void setContentView(View view) {
        if (mKugouLayout.mContentContainer.getChildCount() != 0) {
            throw new RuntimeException("kugou layout can only have one direct child view ");
        }
        mKugouLayout.mContentContainer.addView(view, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public void setContentView(int ResId) {
        setContentView(mParentActivity.getLayoutInflater().inflate(ResId, null));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * get Screen Size
         * */
        final int width = MeasureSpec.getSize(widthMeasureSpec);
        final int height = MeasureSpec.getSize(heightMeasureSpec);
        mWidth = width;
        /**
         * set the Size of mContentContainer
         * */
        final int contentWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, 0, width);
        final int contentHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, 0, height);
        mContentContainer.measure(contentWidthMeasureSpec, contentHeightMeasureSpec);
        /**
         * set mDragRange FULLSCREEN
         * */
        //mDragRange = getChildMeasureSpec(widthMeasureSpec, 0, width);
        //mCloseDistance = mDragRange / 2;
        //Log.e(TAG, "onMeasure: mDragRange="+mDragRange+", mCloseDistance="+mCloseDistance);
        /**
         * onMeasure() must set the measured dimension by calling setMeasuredDimension()
         * */
        setMeasuredDimension(width, height);
        updateDragRange();
        updatePivot();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int width = r - l;
        final int height = b - t;
        if (USE_TRANSLATIONS) {
            mContentContainer.layout(0, 0, width, height);
        } else {
            final int offsetPixels = (int) mOffsetPixels;
            mContentContainer.layout(offsetPixels, 0, width + offsetPixels, height);
        }
    }

    private void updateDragRange() {
        mDragRange = getMeasuredWidth();
        mCloseDistance = mDragRange / 5;
    }

    /**
     * 设置mContentContainer旋转中心
     */
    private void updatePivot() {
        mContentContainer.setPivotX(getWidth() / 2);
        mContentContainer.setPivotY((int) (getHeight() * 1.5));
    }

    /**
     * 按下位置是否符合拖动条件
     */
    private boolean onDownAllowDrag(int x, int y) {
        return (!mBackgroundVisible && mInitialMotionX <= mDragRange / 10)
                || (mBackgroundVisible && mInitialMotionX >= mOffsetPixels);
    }

    /**
     * 滑动过程是否符合拖动条件
     */
    protected boolean onMoveAllowDrag(int x, int y, float dx, float dy) {
        return (!mBackgroundVisible && mInitialMotionX <= mDragRange / 5)
                || (mBackgroundVisible && x >= mOffsetPixels);
    }

    protected void onMoveEvent(float dx, float dy) {
        //Log.i("TAG", "dx=" + dx + ", mOffsetPixels=" + mOffsetPixels);
        mOffsetPixels += dx;
        moveContent();
    }

    /**
     * 移动父类ui 第二层，本身viewgroup没动
     */
    private void moveContent() {
        if (USE_TRANSLATIONS) {
            float translationX = mOffsetPixels / 2;
            //Log.e(TAG, "moveContent: translationX=" + translationX);
            mContentContainer.setTranslationX(translationX);//平移
            //mContentContainer.setRotation(mOffsetPixels / 60);//旋转
        } else {
            mContentContainer.offsetLeftAndRight(((int) mOffsetPixels - getLeft()) / 2);
            //mContentContainer.setRotation(((int) mOffsetPixels - getLeft())/60);
        }
        invalidate();
    }

    private void normalCloseAnimStart(int closeDirection) {
        closingChangeAlpha = true;
        showingChangeAlpha = false;
        if (closeDirection == RIGHT)
            mOffsetAnimator.setFloatValues(mOffsetPixels, getWidth());
        else
            mOffsetAnimator.setFloatValues(mOffsetPixels, -getWidth());
        mAnimatorSet.start();
    }

    private void normalAnimStart() {
        mOffsetAnimator.setFloatValues(mOffsetPixels, 0);
        mAnimatorSet.start();
    }

    private void normalAnimStop() {
        if (mAnimatorSet.isRunning())
            mAnimatorSet.cancel();
    }

    private void reboundAnimStart() {
        showingChangeAlpha = false;
        mBeginOffsetX = mOffsetPixels;
        stopAnim();
        mOffsetPixels = mBeginOffsetX;
        mSpring.setCurrentValue(0);
        mSpring.setEndValue(1);
        doAnim = true;
    }

    private void reboundAnimStop() {
        doAnim = false;
        double stopValue = mSpring.getCurrentValue();
        mSpring.setCurrentValue(stopValue);
    }

    public void setAnimType(int animType) {
        switch (animType) {
            case REBOUND_ANIM:
                mAnimType = REBOUND_ANIM;
                break;
            case NORMAL_ANIM:
                mAnimType = NORMAL_ANIM;
                break;
            case ALWAYS_REBOUND:
                mAnimType = ALWAYS_REBOUND;
                break;
            default:
                throw new IllegalArgumentException("animType should be NORMAL_ANIM or NORMAL_ANIM or ALWAYS_REBOUND");
        }
    }

    public void hide() {
        setVisibility(INVISIBLE);
    }

    /**
     * 透明度改变
     */
    private void changeAlpha() {//setAlpha((mWidth - tempValue) / (mWidth * 2 / 5));
        //setAlpha(1 - Math.abs(mOffsetPixels) / getWidth());
    }

    private void normalShowAnim() {
        mOffsetAnimator.setFloatValues(getWidth(), 0);
        mAnimatorSet.start();
    }

    private void reboundShowAnim() {
        doAnim = true;
        mOffsetPixels = mBeginOffsetX = getWidth();
        mSpring.setCurrentValue(0);
        mSpring.setEndValue(1);
    }

    public void show() {
        showingChangeAlpha = true;
        setAlpha(0);
        setVisibility(VISIBLE);
        if (mAnimType == NORMAL_ANIM) {
            normalShowAnim();
        } else {
            reboundShowAnim();
        }
    }

    private void closeAnim(int closeDirection) {
        normalCloseAnimStart(closeDirection);
    }

    private void scrollBackAnim() {
        if (REBOUND_ANIM == mAnimType || ALWAYS_REBOUND == mAnimType)
            reboundAnimStart();
        else
            normalAnimStart();
    }

    protected void onActionUp(int x, int y) {
        if (!mIsDragging) {
            scrollBackAnim();
            return;
        }
        mVelocityTracker.computeCurrentVelocity(1000, mMaxVelocity);
        final int initialVelocity = (int) getXVelocity(mVelocityTracker);
        closingChangeAlpha = false;
        if (/*initialVelocity == 0 ||*/ ALWAYS_REBOUND == mAnimType) {
            scrollBackAnim();
            return;
        }

//        if (Math.abs(initialVelocity) < 100 && Math.abs(mOffsetPixels) < mCloseDistance) {
        if (Math.abs(initialVelocity) > 100 || Math.abs(mOffsetPixels) > mCloseDistance) {
            if (mOffsetPixels > 0)
                closeAnim(RIGHT);
            else
                //closeAnim(LEFT);
                return;
        } else {
            scrollBackAnim();
        }

        /*if (initialVelocity > 0) {
            if (mOffsetPixels > mCloseDistance / 2) {
                closeAnim(RIGHT);
            } else if (mOffsetPixels < 0) {
                scrollBackAnim();
            }
        } else if (initialVelocity < 0) {
            if (mOffsetPixels > 0) {
                scrollBackAnim();
            } else if (mOffsetPixels < 0) {
                //closeAnim(LEFT);
            }
        }*/

    }

    protected void stopAnim() {
        setAlpha(1);
        if (REBOUND_ANIM == mAnimType || ALWAYS_REBOUND == mAnimType)
            reboundAnimStop();
        else
            normalAnimStop();
    }

    /**
     * 最后计算X速度
     *
     * @param velocityTracker
     * @return
     */
    protected float getXVelocity(VelocityTracker velocityTracker) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
            return velocityTracker.getXVelocity(mActivePointerId);
        }
        return velocityTracker.getXVelocity();
    }

    /**
     * 检测是否为有效滑动
     *
     * @param dx
     * @param dy
     * @return
     */
    protected boolean checkTouchSlop(float dx, float dy) {
        //Log.i("TAG", "checkTouchSlop dx="+dx);
        if (dx < 0) {//屏蔽向左滑动，仅能向右滑动删除
            //Log.e(TAG, "checkTouchSlop: dx=" + dx);
            //return false;
        }
        return Math.abs(dx) > mTouchSlop && Math.abs(dx) > Math.abs(dy);
    }

    protected boolean checkHorizonSlop(float dx, float dy) {
        //Log.i("TAG", "dx="+dx);
        if (dx < 0) {
            return false;
        }
        return Math.abs(dy) > mTouchSlop && Math.abs(dy) > Math.abs(dx);
    }

    protected void setLayoutState(int newState) {
        mLayoutState = newState;
    }

    Rect rect = new Rect();
    Iterator<View> scrollChildListIterator;

    private boolean canChildScroll(float rawX, float rawY) {
        int[] location = new int[2];
        View childView;

        scrollChildListIterator = scrollChildList.iterator();
        while (scrollChildListIterator.hasNext()) {
            childView = scrollChildListIterator.next();
            childView.getLocationInWindow(location);
            rect.set(childView.getLeft(), location[1], childView.getRight(), location[1] + childView.getHeight());
            if (rect.contains((int) rawX, (int) rawY)) {
                return true;
            }
        }
        return false;
    }

    private float start_down_x;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                //Log.e(TAG, "onInterceptTouchEvent: ACTION_DOWN");
                mLastMotionX = mInitialMotionX = ev.getX();
                mLastMotionY = mInitialMotionY = ev.getY();
                start_down_x = mLastMotionX;
                final boolean allowDrag = onDownAllowDrag((int) mInitialMotionX, (int) mInitialMotionY);
                mActivePointerId = ev.getPointerId(0);
                //Log.i("TAG","DOWN allowDrag="+allowDrag );
                if (allowDrag) {
                    setLayoutState(mBackgroundVisible ? STATE_OPEN : STATE_CLOSED);
                    mIsDragging = false;
                    stopAnim();
                }
                break;
            }
            case MotionEvent.ACTION_MOVE:
                //Log.e(TAG, "onInterceptTouchEvent: ACTION_MOVE");
                final int activePointerId = mActivePointerId;
                if (activePointerId == INVALID_POINTER) {
                    break;
                }
                final int pointerIndex = ev.findPointerIndex(activePointerId);
                if (pointerIndex == -1) {
                    mIsDragging = false;
                    mActivePointerId = INVALID_POINTER;
                    return false;
                }
                final float x = ev.getX(pointerIndex);
                final float y = ev.getY(pointerIndex);
                final float dx = x - mLastMotionX;
                final float dy = y - mLastMotionY;
                // //Log.i("TAG", "MOVE dx="+dx+", dy="+dy+", checkTouchSlop="+checkTouchSlop(dx, dy));
                if (checkTouchSlop(dx, dy)) {
                    if (mOffsetPixels == 0 && canChildScroll(ev.getRawX(), ev.getRawY())) {//先忽略
                        onActionUp((int) ev.getX(), (int) ev.getY());
                        return false;
                    }
                    final boolean allowDrag = onMoveAllowDrag((int) x, (int) y, dx, dy);
                    if (allowDrag) {
                        setLayoutState(STATE_DRAGGING);
                        mIsDragging = true;
                        mLastMotionX = x;
                        mLastMotionY = y;
                    }
                } else if (checkHorizonSlop(dx, dy)) {
                    /**
                     * continue the anim
                     * */
                    onActionUp((int) ev.getX(), (int) ev.getY());
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mActivePointerId = INVALID_POINTER;
                mIsDragging = false;
                onActionUp((int) ev.getX(), (int) ev.getY());
                return false;
        }
        //Log.e(TAG, "onInterceptTouchEvent: mIsDragging=" + mIsDragging);
        return mIsDragging;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        /**
         * multi touch support
         * */
        final int action = ev.getAction() & MotionEvent.ACTION_MASK;
        if (mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain();
        mVelocityTracker.addMovement(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //Log.e(TAG, "onTouchEvent: ACTION_DOWN");
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.e(TAG, "onTouchEvent: ACTION_MOVE");
                final int pointerIndex = ev.findPointerIndex(mActivePointerId);
                if (pointerIndex == -1) {
                    mIsDragging = false;
                    mActivePointerId = INVALID_POINTER;
                    //Log.i(TAG, "onTouchEvent MotionEvent.ACTION_MOVE return false");
                    return false;
                }
                if (!mIsDragging) {
                    //Log.e(TAG, "onTouchEvent: mIsDragging=" + mIsDragging);
                    final float x = ev.getX(pointerIndex);
                    final float y = ev.getY(pointerIndex);
                    final float dx = x - mLastMotionX;
                    final float dy = y - mLastMotionY;
                    if (checkTouchSlop(dx, dy)) {
                        if (onMoveAllowDrag((int) x, (int) y, dx, dy)) {
                            setLayoutState(STATE_DRAGGING);
                            mIsDragging = true;
                            mLastMotionX = x;
                            mLastMotionY = y;
                        } else {
                            mInitialMotionX = x;
                            mInitialMotionY = y;
                        }
                    }
                }
                if (mIsDragging) {
                    //startLayoutAnimation();
                    final float x = ev.getX(pointerIndex);
                    final float y = ev.getY(pointerIndex);
                    //onMoveEvent(x - mLastMotionX, y - mLastMotionY);
                    //Log.i("TAG", "x="+x+", mX="+mLastMotionX+", dx="+(x - mLastMotionX)+", mOffsetPixels="+mOffsetPixels+", start_down_x="+start_down_x);
                    //Log.e(TAG, "onTouchEvent: " + (x < start_down_x));
                    onMoveEvent(x < start_down_x ? (-1 * mOffsetPixels) : x - mLastMotionX, y - mLastMotionY);
                    mLastMotionX = x;
                    mLastMotionY = y;
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                //Log.e(TAG, "onTouchEvent: ACTION_UP");

                int index = ev.findPointerIndex(mActivePointerId);
                index = index == -1 ? 0 : index;
                final int x = (int) ev.getX(index);
                final int y = (int) ev.getY(index);
                onActionUp(x, y);
                mActivePointerId = INVALID_POINTER;
                mIsDragging = false;
                break;
        }
        return true;
    }

    Property<KugouLayout, Float> aOffset = new Property<KugouLayout, Float>(Float.class, "mOffsetPixels") {
        @Override
        public Float get(KugouLayout object) {
            return object.mOffsetPixels;
        }

        @Override
        public void set(KugouLayout object, Float value) {
            float tempValue = value;
            object.mOffsetPixels = tempValue;
            moveContent();
            if (showingChangeAlpha) {
                changeAlpha();
            } else if (closingChangeAlpha && tempValue >= mWidth * 3f / 5) {
                setAlpha((mWidth - tempValue) / (mWidth * 2f / 5));
            } else if (closingChangeAlpha && -tempValue >= mWidth * 3f / 5) {
                setAlpha((mWidth + tempValue) / (mWidth * 2f / 5));
            }
        }
    };

    public void addHorizontalScrollableView(View horizontalScrollableView) {
        scrollChildList.add(horizontalScrollableView);
    }

    public void setLayoutCloseListener(LayoutCloseListener layoutCloseListener) {
        mLayoutCloseListener = layoutCloseListener;
    }

    public interface LayoutCloseListener {
        /**
         * 第二层ui动画结束，回调
         */
        void onLayoutClose();
    }
    //******2017��3��10��17:34**********//
    //mRatio 0.0f~1.0f
    /*private boolean mShouldDraw=true;
    private Paint mShadowPaint;
    private void initPaint(){
    	mShadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
        mShadowPaint.setColor(0xff000000); 
    }
    @Override  
    protected void dispatchDraw(Canvas canvas) {  
        super.dispatchDraw(canvas);  
        if (mShouldDraw){  
            ����ƫ�Ƶı�Ӱ��ɫ  
            drawBackground(canvas);  
            ���Ʊ�Ե����Ӱ  
            drawShadow(canvas) ;  
        }  
    }  
    *//**
     * ���Ʊ�����ɫ�����ž���ĸı���ı�  
     * @param canvas
     *//*  
    private void drawBackground(Canvas canvas) {  //setAlpha(1 - Math.abs(mOffsetPixels)/getWidth());
    	float mRatio=Math.abs(mOffsetPixels)/getWidth();
        mShadowPaint.setAlpha((int) ((1 - mRatio) * 180));  
        canvas.drawRect(-mPix, 0, 0, getHeight(), mShadowPaint);  
    }  
    *//**
     * ����shadow��Ӱ  
     * @param canvas
     *//*  
    private void drawShadow(Canvas canvas){  
        ���滭����ǰ��״̬������÷�����ǰ�潫�Զ���View ��ʱ�򽫵ĺ���ϸ  
        canvas.save() ;  
        ���� drawable �Ĵ�С��Χ  
        mLeftShadow.setBounds(0, 0, mShadowWidth, getHeight());  
        �û���ƽ��һ������  
        canvas.translate(-mShadowWidth,0);  
        ����Drawable  
        mLeftShadow.draw(canvas);  
        �ָ�������״̬  
        canvas.restore();  
    }  */
}
