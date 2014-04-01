package com.example.scrollbarlist.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.scrollbar.R;

/**
 * 此控件是中间带滚动条的布局，不包含上下两个按钮
 * 
 * @Author ZGY
 * @Date:2014-3-27
 * @version
 * @since 欢迎加群88130145进行开发交流
 * 
 */
public class ScrollBar extends View {

	// 要画的图片
	private Drawable mBackGroud;

	private Drawable mBarNormal;
	private Drawable mBarPressed;
	private boolean mIsBarPressed;
	// 背景
	private int mBackGroudWidth;
	private int mBackGroudHeight;
	// 滑块
	private int mBarHeight;
	private int mBarWidth;
	private int mSeekDistenceHeight;
	private int mBarLeftRightMargin; // 滑块里父元素左边的距
	private int mBarTop; // 滑块离顶部距
	private int mMaxProgress = 100; // 最大值
	private OnBarListener mListener;

	private int mNowProgress;// 当前滚动的位置

	private float mMinBarHeightMultipleWidth = 1.0f;// 滚动条高度至少是它宽度的多少倍

	/**
	 * 回调接口
	 * 
	 * @version ScrollBarView
	 * @since
	 */
	public interface OnBarListener {

		void onBarProgressChanged(ScrollBar verticalScrollBar, int progress, int maxProgress);

		void onBarControled(boolean underCtrl);

	}

	public ScrollBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ScrollBar(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {
		mBackGroud = context.getResources().getDrawable(R.drawable.seekbar_bg);// 滚动条背景
		mBarNormal = context.getResources().getDrawable(R.drawable.seekbar_n);// 滚动条
		mBarPressed = context.getResources().getDrawable(R.drawable.seekbar_p);// 滚动条
		
		Log.e("", "mBarNormal.w=" +mBarNormal.getBounds().width());
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		mBackGroudWidth = w;
		mBackGroudHeight = h;
		setWidthHeight(false);
	}

	/**
	 * 控制滚动条、滚动条背景的宽高
	 * 
	 * @param @param full  是否是重置高度调用的，若重置滚动条高度，为true的情况是listview里的item一屏可以展示下，所以此时将滚动条高度置为背景条全高
	 * @author zhuanggy
	 * @date 2014-3-27
	 */
	private void setWidthHeight(boolean full) {
		mBarWidth = mBackGroudWidth * 5 / 7;// 滚动条的宽度，暂时设置为滚动条背景的5/7

		if (full) {
			mBarHeight = mBackGroudHeight;
		} else {
			mBarHeight = mBackGroudHeight * 10 / mMaxProgress;
			// 在此控制一个最短高度，不然listview数据非常庞大时，此高度将非常小
			if (mBarHeight - mBarWidth * mMinBarHeightMultipleWidth < 0) {
				mBarHeight = (int) (mBarWidth * mMinBarHeightMultipleWidth);
			}
		}

		mSeekDistenceHeight = mBackGroudHeight - mBarHeight;
		mBarLeftRightMargin = (mBackGroudWidth - mBarWidth) / 2;
		mBackGroud.setBounds(0, 0, mBackGroudWidth, mBackGroudHeight);
		setProgress(0, false);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// getLeft相对于父元素左边距离,getRight=getLeft+getWidth
		mBackGroud.draw(canvas);
		if (mIsBarPressed) {
			mBarPressed.draw(canvas);
		} else {
			mBarNormal.draw(canvas);
		}

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int Y = (int) event.getY();
		int progress = 0;

		// 这是滑块已经到达顶部
		if (Y <= mBarHeight / 2) {
			progress = 0;
		}
		// 滑块到达最底端
		else if (Y >= (mSeekDistenceHeight + mBarHeight / 2)) {
			progress = mMaxProgress;
		} else {
			progress = (Y - mBarHeight / 2) * mMaxProgress / mSeekDistenceHeight;
		}
		mIsBarPressed = true;
		setProgress(progress, true);

		switch (event.getAction()) {
		case MotionEvent.ACTION_CANCEL:
			mIsBarPressed = false;
			invalidate();
			if (mListener != null) {
				mListener.onBarControled(false);
			}
			break;
		case MotionEvent.ACTION_UP:
			mIsBarPressed = false;
			invalidate();
			if (mListener != null) {
				mListener.onBarControled(false);
			}
			break;

		default:
			break;
		}
		return true;

	}

	// //////////对ListWithScrollBarView公开的接口///////////////

	/**
	 * 设置滚动区域最大值
	 * 
	 * @param @param max 
	 * @date 2014-3-26
	 */
	public void setMaxProgress(int max) {
		this.mMaxProgress = max;
		// Log.e("", "setMax");
	}

	/**
	 * 获得滚动区域最大值
	 * 
	 * @param @return 
	 * @author zhuanggy
	 * @date 2014-3-28
	 */
	public int getMaxProgress() {
		return this.mMaxProgress;
	}

	/**
	 * 获得当前滚动的位置
	 * 
	 * @param @return 
	 * @author zhuanggy
	 * @date 2014-3-28
	 */
	public int getNowProgress() {
		return this.mNowProgress;
	}

	/**
	 * 设置滚动Bar的高度至少是其宽度的几倍，默认为1.0f倍
	 * 
	 * @param @param multiple 
	 * @author zhuanggy
	 * @date 2014-3-28
	 */
	public void setMinBarHeightMultipleWidth(float multiple) {
		this.mMinBarHeightMultipleWidth = multiple;
	}

	/**
	 * 重置scrollBar，高宽。一般当listview里item数量变化是会执行
	 * 
	 * @param  
	 * @author zhuanggy
	 * @date 2014-3-27
	 */
	public void resetScrollBar(boolean full) {
		setWidthHeight(full);
	}

	/**
	 * 设置滚动条滚动位置
	 * 
	 * @param @param progress
	 * @param @param callback  是否需要回调通知progress的变化，如果是listview滚动触发的，则不需要回调；如果是控制scrollbar触发的，则需要回调更新ListView的滚动位置
	 * @date 2014-3-26
	 */
	public void setProgress(int progress, boolean callbackProgressChanged) {
		// Log.e("", "progress=" +progress);
		mNowProgress = progress;
		if (mMaxProgress < 1) {
			mBarTop = 0; // 把m_nSeekBarHeight分成m_nMax份，progress是当前的份额
		} else {
			mBarTop = progress * mSeekDistenceHeight / mMaxProgress; // 把m_nSeekBarHeight分成m_nMax份，progress是当前的份额
		}

		if (mIsBarPressed) {
			mBarPressed.setBounds(mBarLeftRightMargin, mBarTop, mBarLeftRightMargin + mBarWidth, mBarTop + mBarHeight);
		} else {
			mBarNormal.setBounds(mBarLeftRightMargin, mBarTop, mBarLeftRightMargin + mBarWidth, mBarTop + mBarHeight);
		}
		invalidate();
		if (mListener != null) {
			mListener.onBarControled(callbackProgressChanged);
			if (callbackProgressChanged) {
				mListener.onBarProgressChanged(this, progress, mMaxProgress);
			}
		}

	}

	/**
	 * 设置滚动条监听
	 * 
	 * @param @param a_Listen 
	 * @date 2014-3-26
	 */
	public void setListener(OnBarListener a_Listen) {
		mListener = a_Listen;
	}

}
