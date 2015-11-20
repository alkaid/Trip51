package com.alkaid.trip51.shop.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alkaid.trip51.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by alkaid on 2015/11/7.
 */
public class MyClientQuestionAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private ArrayList<String> alQuestions;

    public MyClientQuestionAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        alQuestions = new ArrayList<>();
        alQuestions.add("支付问题");
        alQuestions.add("余额问题");
        alQuestions.add("优惠券问题");
        alQuestions.add("退款问题");
        alQuestions.add("红包问题");
        alQuestions.add("积分问题问题");
        alQuestions.add("其他问题");

    }

    @Override
    public int getCount() {
        return alQuestions.size();
    }

    @Override
    public Object getItem(int position) {
        return alQuestions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_user_list_client_question, null);
               holder = new ViewHolder();
            holder.tvQuestionName =(TextView) convertView.findViewById(R.id.tv_question_name);
            holder.tvQuestionName.setText(alQuestions.get(position));
//                *得到各个控件的对象
//                holder.title = (TextView) convertView.findViewById(R.id.ItemTitle);
//                holder.text = (TextView) convertView.findViewById(R.id.ItemText);
//                holder.bt = (Button) convertView.findViewById(R.id.ItemButton);
                convertView.setTag(holder);//绑定ViewHolder对象
        } else {
                holder = (ViewHolder)convertView.getTag();//取出ViewHolder对象
        }
        return convertView;
    }

    private class ViewHolder{
        TextView tvQuestionName;
    }
}

