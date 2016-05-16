# Scroller平滑滚动

标签： Android

---

### 
在我们做Android应用时，特别是自定义View时，经常要做一些动画。我们主要是有View动画和属性动画以及借助Scoller类来实现View的平滑移动。现在主要来学习Scroller类的应用。
   
---

预备知识：
关于Scroller，我们首先要了解，我们滑动的是这个View的内容，而View本身位置是不能改变的。我们需要知道View的mScrollX和mScrollY这两个属性，这两个属性我们可以通过getScrollX()和getScrollY()分别得到。在滑动过程中，mScrollX表示View的左边缘和View内容左边缘在水平方向的距离。我们都知道View的位置，是由View的四个顶点的位置决定的。当View从左向右滑动时，mScrollX为负值，反之为正。当View从上向下滑动时，mScrollY为负值，反之为正。

### 
现在我们来看Scroller的最基本的用法:
1.实现构造器
```
  public Scroller(Context context) {
        this(context, null);
  }
    
  public Scroller(Context context, Interpolator interpolator) {
        this(context, interpolator,
   }
```
 2.然后在需要滚动的时候，调用Scroller的startScroll()方法：
 sample：在1000ms内水平滑至destX处。
```
  private void smoothScoller(int destX,int destY){
      int scrollX = getScrollX();
      int dx = destX - scrollX;
      mScoller.startScroll(scrollX,0,dx,0,1000);
      invalidate();
  }
```
3.实现View的computeScroll()方法:
```
   @Override
    public void computeScroll() {
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            postInvalidate();
        }
    } 
```
这三步是Scroller的最基本的使用方法。

我们首先来看看Scroller的startScroll()方法:
```
public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        mMode = SCROLL_MODE;
        mFinished = false;
        mDuration = duration;
        mStartTime = AnimationUtils.currentAnimationTimeMillis();
        mStartX = startX;
        mStartY = startY;
        mFinalX = startX + dx;
        mFinalY = startY + dy;
        mDeltaX = dx;
        mDeltaY = dy;
        mDurationReciprocal = 1.0f / (float) mDuration;
    }
```
很简单的一个方法，这个方法其实并没有做些关于动画实现的方法，仅仅只是保存了我们传入的参数：
> startX、startY： 滑动的起始点位置，
> dx、dy: 将要滑动的偏移量
> duration: 动画将要持续的时间

 既然startScroll()不是实现动画的具体实现，那么答案肯定在computeScroll()！我们继续来看：
 
 在startScroll()方法后面，调用了invalidate()方法，会强制View重新绘制，绘制过程中会调用View的draw()方法,在draw()方法里，又回调用computeScroll()。在View的源码里，computeScroll()是个空方法，因此我们必须自己实现这个方法，上面即是我们自己实现的方法。在这个方法里，Scroller会获取View的当前位置getCurrX()和getCurrY()，然后通过scrollTo()来实现滑动。最后又调用postInvalidate()方法实现View的二次重绘，然后Scroller继续获取当前的位置，并通过scrollTo()来滑动到新位置，如此反复，直到整个滑动过程结束。
  在此过程中，还调用了computeScrollOffset()方法。这个方法是返回一个boolean型，主要变现为：如果当前动画已完成，则返回false；如果没有完成，则会一直返回true。
```
public boolean computeScrollOffset() {
        if (mFinished) {
            return false;
        }

        int timePassed = (int)(AnimationUtils.currentAnimationTimeMillis() - mStartTime);
    
        if (timePassed < mDuration) {
            switch (mMode) {
            case SCROLL_MODE:
                ...
                ...
                ...
                
                break;
            case FLING_MODE:
               
                ...
                ...
                
                if (mCurrX == mFinalX && mCurrY == mFinalY) {
                    mFinished = true;
                }

                break;
            }
        }
        else {
            mCurrX = mFinalX;
            mCurrY = mFinalY;
            mFinished = true;
        }
        return true;
    }
```

总结：
 Scroller其实并不会实现View的滑动，它只是保存View的位置信息，然后配合View的computeScroll()方法，不断的重绘View，并修改View的滑动位置，并通过scrollTo()来滑动到新位置，这样不断的重复的小幅度滑动，由此组成了View的平滑滚动。
 
  








