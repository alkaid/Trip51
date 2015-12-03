package pw.h57.popupbuttonlibrary.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import pw.h57.popupbuttonlibrary.R;

/**
 * 自定义的弹出框列表适配器,类似于大众点评或美团,如果想要此种效果可以直接使用
 * Created by Chris on 2014/12/8.
 */
public abstract class PopupAdapter<T> extends ArrayAdapter<T> {
    private int resource;
    private int normalBg;
    private int pressBg;
    private int selection;

    public PopupAdapter(Context context, int resource, T[] objects, int normalBg, int pressBg) {
        super(context, resource, objects);
        initParams(resource, normalBg, pressBg);
    }


    public PopupAdapter(Context context, int resource, List<T> objects, int normalBg, int pressBg) {
        super(context, resource, objects);
        initParams(resource, normalBg, pressBg);
    }

    private void initParams(int resource, int normalBg, int pressBg){
        this.resource = resource;
        this.normalBg = normalBg;
        this.pressBg = pressBg;
        this.selection = -1;
    }

    protected abstract String getItemLabel(int position);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String s = getItemLabel(position);
        View view;
        ViewHolder holder;
        if(convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resource,null);
            holder = new ViewHolder();
            holder.tv = (TextView) view.findViewById(R.id.tv);
            view.setTag(holder);
        } else {
            view = convertView;
            holder = (ViewHolder) view.getTag();
        }
        holder.tv.setText(s);
        if(position == selection) {
            holder.tv.setBackgroundResource(pressBg);
        } else {
            holder.tv.setBackgroundResource(normalBg);
        }
        return view;
    }

    public void setPressPostion(int position) {
        this.selection = position;
    }
    class ViewHolder{
        TextView tv;
    }
}
