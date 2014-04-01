package com.example.scrollbarlist.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.scrollbar.R;
import com.example.scrollbarlist.view.ScrollBar.OnBarListener;

/**
 * 此控件是整个scrollbar，包含上下两个控制按钮
 * 
 * @Author ZGY
 * @Date:2014-3-27
 * @version
 * @since 欢迎加QQ群88130145进行开发交流
 * 
 */
@SuppressLint("HandlerLeak")
public class ScrollBarView extends LinearLayout implements OnTouchListener {

	private Context mContext;
	private ScrollBar mScrollBar;
	private ListView mListView;
	private Handler mHandler;

	// Listener
	private AbsListView.OnScrollListener mOnListViewScrollListener;
	private ScrollBar.OnBarListener mOnBarScrollListener;

	public ScrollBarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ScrollBarView(Context context) {
		super(context);
		init(context);
	}

	private void init(Context context) {

		this.mContext = context;
		mHandler = new LoopHandler();
		LayoutInflater inflater = LayoutInflater.from(mContext);
		ViewGroup main = (ViewGroup) inflater.inflate(R.layout.scroll_bar_view, null);

		mListView = (ListView) main.findViewById(R.id.list_view);
		ImageView imgUp = (ImageView) main.findViewById(R.id.img_scroll_ctrl_up);
		ImageView imgDown = (ImageView) main.findViewById(R.id.img_scroll_ctrl_down);
		mScrollBar = (ScrollBar) main.findViewById(R.id.scrollbar);

		imgDown.setOnTouchListener(this);
		imgUp.setOnTouchListener(this);

		this.addView(main);

		mScrollBar.setListener(new OnBarListener() {

			@Override
			public void onBarProgressChanged(ScrollBar verticalScrollBar, int progress, int maxProgress) {
				if (mOnBarScrollListener != null) {
					mOnBarScrollListener.onBarProgressChanged(verticalScrollBar, progress, maxProgress);
				}
			}

			@Override
			public void onBarControled(boolean underCtrl) {
				if (mOnBarScrollListener != null) {
					mOnBarScrollListener.onBarControled(underCtrl);
				}
			}

		});
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// 如果按住上下按钮不动，则一直控制scrollbar的滚动

		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (mOnBarScrollListener != null) {
				mOnBarScrollListener.onBarControled(true);
			}
			if (v.getId() == R.id.img_scroll_ctrl_up) {
				mHandler.sendEmptyMessage(MSG_UP);
			} else {
				mHandler.sendEmptyMessage(MSG_DOWN);
			}
			break;

		case MotionEvent.ACTION_UP:
			if (mOnBarScrollListener != null) {
				mOnBarScrollListener.onBarControled(false);
			}
			mHandler.removeMessages(MSG_UP);
			mHandler.removeMessages(MSG_DOWN);
			break;
		case MotionEvent.ACTION_CANCEL:
			if (mOnBarScrollListener != null) {
				mOnBarScrollListener.onBarControled(false);
			}
			mHandler.removeMessages(MSG_UP);
			mHandler.removeMessages(MSG_DOWN);
			break;

		default:
			break;
		}

		return false;
	}

	private boolean upClicked() {
		if (mScrollBar.getNowProgress() > 0) {
			mScrollBar.setProgress(mScrollBar.getNowProgress() - 1, true);
			return true;
		}

		return false;
	}

	private boolean downClicked() {
		if (mScrollBar.getNowProgress() < mScrollBar.getMaxProgress()) {
			mScrollBar.setProgress(mScrollBar.getNowProgress() + 1, true);
			return true;
		}
		return false;
	}

	// /////////////////
	/**
	 * 当上下按钮被按下时，循环控制滚动条滚动
	 */
	private static final int MSG_UP = 0x100;
	private static final int MSG_DOWN = 0x101;
	private static final int DELAY = 10;

	private class LoopHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case MSG_UP:
				if (upClicked()) {
					mHandler.sendEmptyMessageDelayed(MSG_UP, DELAY);
				}

				break;
			case MSG_DOWN:
				if (downClicked()) {
					mHandler.sendEmptyMessageDelayed(MSG_DOWN, DELAY);
				}
				break;

			default:
				break;
			}

		}

	}

	// ///////////////////添加公开的接口////////////////////
	/**
	 * 控制滚动条滚动的位置
	 * 
	 * @param progress
	 */
	public void setScrollBarProgress(int progress) {
		mScrollBar.setProgress(progress, false);
	}

	/**
	 * 获得滚动条的位置
	 * 
	 * @param @return 
	 * @author zhuanggy
	 * @date 2014-3-28
	 */
	public int getScrollBarProgress() {
		return mScrollBar.getNowProgress();
	}

	/**
	 * 设置滚动条滚动区域的最大值
	 * 
	 * @param @param maxProgress 
	 * @author zhuanggy
	 * @date 2014-3-28
	 */
	public void setMaxProgress(int maxProgress) {
		mScrollBar.setMaxProgress(maxProgress);
	}

	public int getMaxProgress() {
		return mScrollBar.getMaxProgress();
	}

	/**
	 * 设置滚动Bar的高度至少是其宽度的几倍，默认为1.0f倍，（因为若数据量非常大的话，滚动bar高度会非常细）
	 * 
	 * @param @param multiple 
	 * @author zhuanggy
	 * @date 2014-3-28
	 */
	public void setMinBarHeightMultipleWidth(float multiple) {
		mScrollBar.setMinBarHeightMultipleWidth(multiple);
	}

	/**
	 * 获得ListView
	 * 
	 * (注意：不能通过getListView().OnScrollListener()，只能通过 {@link setOnListViewScrollListener(android.widget.AbsListView.OnScrollListener)方法设置监听} )
	 * 
	 * @param @return 
	 * @author zhuanggy
	 * @date 2014-3-28
	 */
	public ListView getListView() {
		return mListView;
	}

	public void resetScrollBar(boolean full) {
		mScrollBar.resetScrollBar(full);
	}

	/**
	 * 你没必要获得滚动条，不需要管它
	 * 
	 * @param @return 
	 * @author zhuanggy
	 * @date 2014-3-28
	 */
	@Deprecated
	public ScrollBar getScrollBar() {
		return null;
	}

	/**
	 * 设置滚动条监听器
	 * 
	 * @param @param listen 
	 * @author zhuanggy
	 * @date 2014-3-28
	 */
	public void setOnBarScrollListener(ScrollBar.OnBarListener listen) {
		this.mOnBarScrollListener = listen;
	}

}
