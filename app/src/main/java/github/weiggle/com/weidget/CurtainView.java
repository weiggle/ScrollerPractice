package github.weiggle.com.weidget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import github.weiggle.com.scrollerpractice.R;

/**
 * Created by wei.li on 2016/5/15.
 */
public class CurtainView extends RelativeLayout implements View.OnTouchListener{

    private Context mContext;
    private Scroller mScroller;

    /**
     * 按下时的坐标
     */
    private int downY = 0;

    /**
     * 滑动时的坐标
     */
    private int moveY = 0;

    /**
     * 抬起时的坐标
     */
    private int upY = 0;

    /**
     * 滑动的像素
     */
    private int scrollY = 0;

    /**
     * 滑动图片的高度
     */
    private int curtainHeight;

    /**
     * 图片是否打开
     */
    private boolean isOpen = false;

    /**
     * 图片是否滑动
     */
    private boolean isMove = false;

    private int upDuration = 1000;
    private int downDuration = 5000;
    private Interpolator mInterpolator = new BounceInterpolator();
    private ImageView adver;

    public CurtainView(Context context) {
        this(context,null);
    }

    public CurtainView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CurtainView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mContext = context;
        init();
    }

    private void init(){
        mScroller = new Scroller(mContext,mInterpolator);
        this.setBackgroundColor(Color.TRANSPARENT);

        final View view = LayoutInflater.from(mContext).inflate(R.layout.curtain_view,null);
        adver = (ImageView) view.findViewById(R.id.adver);
        ImageView rope = (ImageView) view.findViewById(R.id.rope);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(view,params);

        adver.post(new Runnable() {
            @Override
            public void run() {
                curtainHeight = adver.getHeight();
                CurtainView.this.scrollTo(0,curtainHeight);
            }
        });

        rope.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(!isMove){
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    downY = (int) event.getRawY();
                    return true;
                case MotionEvent.ACTION_MOVE:
                    moveY = (int) event.getRawY();
                    scrollY = moveY - downY;
                    if(scrollY < 0){///向上滑动
                        if(isOpen){
                            if(Math.abs(scrollY) <= adver.getBottom()){
                                scrollTo(0,-scrollY);
                            }
                        }
                    }else{///向下滑动
                        if(!isOpen){
                            if(scrollY <= curtainHeight){
                                scrollTo(0, curtainHeight-scrollY);
                            }
                        }

                    }
                    break;
                case MotionEvent.ACTION_UP:
                    upY = (int) event.getRawY();
                    if(Math.abs(upY - downY) < 10){//单机
                        onRopeClick();
                        break;
                    }
                    if(downY > upY){//向上滑动
                        if(isOpen){
                            if(Math.abs(scrollY) > curtainHeight/2){
                                this.startMoveAnim(getScrollY(),curtainHeight - getScrollY(),upDuration);
                                isOpen = false;
                            }else{
                                this.startMoveAnim(getScrollY(),-this.getScrollY(),upDuration);
                                isOpen = true;
                            }
                        }

                    }else{//向下滑动
                            if(scrollY > curtainHeight/2){
                                this.startMoveAnim(getScrollY(),-getScrollY(),downDuration);
                                isOpen = true;
                            }else{
                                this.startMoveAnim(getScrollY(),curtainHeight-getScrollY(),downDuration);
                                isOpen = false;
                            }
                    }

                    break;
                default:
                    break;
            }

            return false;

        }

        return false;
    }

    //单机 关闭 或 打开
    private void onRopeClick(){
        if(isOpen){
            this.startMoveAnim(0,curtainHeight,upDuration);
        }else{
            this.startMoveAnim(curtainHeight,-curtainHeight,downDuration);
        }

        isOpen = !isOpen;
    }

    private void startMoveAnim(int startY,int dy,int duration){
        isMove = true;
        mScroller.startScroll(0,startY,0,dy,duration);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
            isMove = true;
        }else{
            isMove = false;
        }
        super.computeScroll();
    }

}
