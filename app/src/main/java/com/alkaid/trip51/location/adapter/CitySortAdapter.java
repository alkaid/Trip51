package com.alkaid.trip51.location.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.model.SimpleCity;

public class CitySortAdapter extends BaseAdapter implements SectionIndexer{
	private List<SimpleCity> cities = null;
	private Context mContext;

	public CitySortAdapter(Context mContext, List<SimpleCity> cities) {
		this.mContext = mContext;
		this.cities = cities;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * @param cities
	 */
	public void updateListView(List<SimpleCity> cities){
		this.cities = cities;
		notifyDataSetChanged();
	}

	public int getCount() {
		if(cities!=null){
			return cities.size();
		}else{
			return 0;
		}
	}

	public Object getItem(int position) {
		if(cities!=null){
			return cities.get(position);
		}else{
			return null;
		}
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final SimpleCity mContent = cities.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = LayoutInflater.from(mContext).inflate(R.layout.sort_item_city, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		//根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);

		//如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if(position == getPositionForSection(section)){
			viewHolder.tvLetter.setVisibility(View.VISIBLE);
			viewHolder.tvLetter.setText(cities.get(position).getFirstLetter());
		}else{
			viewHolder.tvLetter.setVisibility(View.GONE);
		}

		viewHolder.tvTitle.setText(cities.get(position).getCityname());

		return view;

	}



	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
	}


	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return cities.get(position).getFirstLetter().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = cities.get(i).getFirstLetter();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 *
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String  sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	@Override
	public Object[] getSections() {
		return null;
	}
}