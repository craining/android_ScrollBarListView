package com.example.scrollbarlist;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.scrollbar.R;
import com.example.scrollbarlist.view.ScrollBarListView;
import com.example.scrollbarlist.view.ScrollBar;

/**
 * 测试activity
 * 
 * @Author ZGY
 * @Date:2014-3-27
 * @version
 * @since 欢迎加群88130145进行开发交流
 */
public class OneActivity extends Activity {
	private EditText mEditItemCounts;
	private ScrollBarListView mListWithScrollBarView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one);
		setTitle("ScrollBarListView");

		mEditItemCounts = (EditText) findViewById(R.id.edit_item_count);

		Button btnItemsSet = (Button) findViewById(R.id.btn_item_set);
		btnItemsSet.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// 隐藏输入框
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(mEditItemCounts.getWindowToken(), 0);
				// 更新listview
				String[] data = getData();
				mListWithScrollBarView.setMaxProgress(data.length);// 此步必须在设置listview前执行
				((ListViewAdapter) mListWithScrollBarView.getListView().getAdapter()).notifyDataSetChanged(data);
			}
		});

		// 以下是自定义Listview相关控制
		mListWithScrollBarView = (ScrollBarListView) findViewById(R.id.scrollbar);
		mListWithScrollBarView.setMinBarHeightMultipleWidth(0.5f);

		// 滚动条的监听
		mListWithScrollBarView.setOnBarScrollListener(new ScrollBar.OnBarListener() {

			@Override
			public void onBarProgressChanged(ScrollBar verticalScrollBar, int progress, int maxProgress) {

			}

			@Override
			public void onBarControled(boolean underCtrl) {

			}
		});

		// ListView的滚动监听
		mListWithScrollBarView.setOnListViewScrollListener(new AbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

			}
		});

		// 设置数据
		String[] data = getData();
		mListWithScrollBarView.setMaxProgress(data.length);// 此步如果设置的话，必须在setAdapter前执行
		mListWithScrollBarView.getListView().setAdapter(new ListViewAdapter(OneActivity.this, data));

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
