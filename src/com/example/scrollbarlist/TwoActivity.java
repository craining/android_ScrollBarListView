package com.example.scrollbarlist;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.scrollbar.R;
import com.example.scrollbarlist.view.ScrollBar;
import com.example.scrollbarlist.view.ScrollBarView;
/**
 * 测试activity
 * 
 * @Author ZGY
 * @Date:2014-3-27
 * @version
 * @since 欢迎加群88130145进行开发交流
 */
public class TwoActivity extends Activity {

	private EditText mEditItemCounts;

	private boolean mListViewOnScrollBarCtrled = false;// listview是否是滚动条控制的滚动
	int mFirstVisibleItem;
	int mVisibleItemCount;
	int mTotalItemCount;
	private boolean mHasLeftItems;// 是否有显示不下的item，防止滚动时scrollbar高度被重置

	private ListView mListView;
	private ScrollBarView mScrollBarView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_two);
		setTitle("ScrollBarView");
		
		
		mListView = (ListView) findViewById(R.id.list_second);
		mScrollBarView = (ScrollBarView) findViewById(R.id.scroll_bar);
		mEditItemCounts = (EditText) findViewById(R.id.edit_item_count);

		Button btnItemsSet = (Button) findViewById(R.id.btn_item_set);
		btnItemsSet.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 隐藏输入框
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mEditItemCounts.getWindowToken(), 0);
				// 更新listview
				mHasLeftItems = false;
				String[] data = getData();
				mScrollBarView.setMaxProgress(data.length);// 此步必须在设置listview前执行
				((ListViewAdapter) mListView.getAdapter()).notifyDataSetChanged(data);
			}
		});

		mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				mFirstVisibleItem = firstVisibleItem;
				mVisibleItemCount = visibleItemCount;
				mTotalItemCount = totalItemCount;
				// Log.e("", "mFirstVisibleItem =" + mFirstVisibleItem + " mVisibleItemCount=" + mVisibleItemCount + "  mTotalItemCount" + mTotalItemCount);
				if (!mListViewOnScrollBarCtrled) {
					// Log.i("", "onScroll");
					// 仅当listview触摸滑动时才控制滚动条位置
					if ((totalItemCount - visibleItemCount) > 0) {
						Log.i("", "totalItemCount > visibleItemCount");
						if (!mHasLeftItems) {
							// 之前是不满一屏的，现在超过一屏了，则更新滚动条的长度
							mScrollBarView.resetScrollBar(false);
						}
						mHasLeftItems = true;
						mScrollBarView.setScrollBarProgress(firstVisibleItem * mScrollBarView.getMaxProgress() / (totalItemCount - visibleItemCount));
					} else {
						// 不满1屏，则滚动条长度铺满
						if (visibleItemCount <= totalItemCount && !mHasLeftItems) {
							mScrollBarView.resetScrollBar(true);
						}
					}

				} else {
				}
			}
		});

		mScrollBarView.setOnBarScrollListener(new ScrollBar.OnBarListener() {

			@Override
			public void onBarProgressChanged(ScrollBar verticalScrollBar, int progress, int maxProgress) {
				// TODO 滚动条位置有变化，控制listview的滚动，此处待优化
				int shouldShowPosition = (mTotalItemCount - mVisibleItemCount) * progress / mScrollBarView.getMaxProgress();
				// mListView.smoothScrollToPosition(shouldShowPosition);//平滑滚动到某个位置
				// mListView.smoothScrollToPosition(shouldShowPosition, 1);
				mListView.setSelection(shouldShowPosition);// 直接控制展示到某位置
			}

			@Override
			public void onBarControled(boolean underCtrl) {
				mListViewOnScrollBarCtrled = underCtrl;
			}
		});

		// 设置数据
		String[] data = getData();
		mScrollBarView.setMaxProgress(data.length);// 此步如果设置的话，必须在setAdapter前执行
		mListView.setAdapter(new ListViewAdapter(TwoActivity.this, data));

	}

	/**
	 * 生成测试数据
	 * 
	 * @param @return 
	 * @author zhuanggy
	 * @date 2014-3-28
	 */
	private String[] getData() {
		int counts = Integer.parseInt("" + mEditItemCounts.getText().toString());
		// 测试数据
		String[] numbers = new String[counts];
		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = " NO. " + i;
		}
		return numbers;
	}

	/**
	 * listview adapter
	 * 
	 * @Author zhuanggy
	 * @Date:2014-3-27
	 * @version MainActivity
	 * @since
	 */
	private class ListViewAdapter extends BaseAdapter {
		private Context mContext;
		private String[] texts;

		public ListViewAdapter(Context context, String[] texts) {
			this.mContext = context;
			this.texts = texts;
		}

		public void notifyDataSetChanged(String[] texts) {
			this.texts = texts;
			notifyDataSetChanged();
		}

		public int getCount() {
			return texts.length;
		}

		public Object getItem(int position) {
			return null;
		}

		public long getItemId(int position) {
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item, null);
				ItemViewHolder viewCache = new ItemViewHolder();
				viewCache.mTextView = (TextView) convertView.findViewById(R.id.text);
				convertView.setTag(viewCache);
			}
			ItemViewHolder cache = (ItemViewHolder) convertView.getTag();
			cache.mTextView.setText(texts[position]);
			return convertView;
		}
	}

	private static class ItemViewHolder {
		public TextView mTextView;
	}
}
