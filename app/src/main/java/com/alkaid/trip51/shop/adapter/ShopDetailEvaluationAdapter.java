package com.alkaid.trip51.shop.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.model.shop.Comment;
import com.alkaid.trip51.util.BitmapUtil;

import java.util.List;

/**
 * Created by jyz on 2015/12/14.
 */
public class ShopDetailEvaluationAdapter extends BaseAdapter {
    private LayoutInflater mInflater;

    private List<Comment> commentList;
    private Context mContext;

    private static final int TOTAL_LEVEL = 5;

    public ShopDetailEvaluationAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
    }

    public void setData(List<Comment> commentList) {
        this.commentList = commentList;
    }

    @Override
    public int getCount() {
        if (commentList != null) {
            return commentList.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (commentList != null) {
            return commentList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_shop_detail_evaluation, null);
            holder = new ViewHolder();
            /**得到各个控件的对象*/
            holder.ivUserThumb = (ImageView) convertView.findViewById(R.id.ivUserThumb);
            holder.tvNickName = (TextView) convertView.findViewById(R.id.tv_nick_name);
            holder.llStarFav = (LinearLayout) convertView.findViewById(R.id.ll_star_fav);
            holder.tvAvgFee = (TextView) convertView.findViewById(R.id.tv_evaluation_avg_price);
            holder.tvEvaluationContent = (TextView) convertView.findViewById(R.id.tv_evaluation_content);
            holder.llEvaluationImgs = (LinearLayout) convertView.findViewById(R.id.ll_evaluation_imgs);
            convertView.setTag(holder);//绑定ViewHolder对象
        } else {
            holder = (ViewHolder) convertView.getTag();//取出ViewHolder对象
            Comment comment = commentList.get(position);
            setUserThumb(comment.getImgurl(),holder.ivUserThumb);
            holder.tvNickName.setText(comment.getNickname());
            int starleve = (int) comment.getCommentlevel();
            setStarView(starleve, holder.llStarFav);
            holder.tvAvgFee.setText("$"+comment.getAvgFee() + "/人");
            holder.tvEvaluationContent.setText(comment.getContent());

        }
        return convertView;
    }

    /**
     * 设置评价的星星View
     *
     * @param starlevel 评为多少颗星
     * @param llStarFav 5星所在的布局里
     */
    private void setStarView(int starlevel, LinearLayout llStarFav) {
        llStarFav.removeAllViews();
        for (int i = 0; i < starlevel; i++) {
            ImageView ivStar = new ImageView(mContext);
            ivStar.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.star_thumb_fav));
            llStarFav.addView(ivStar);
        }
        for (int i = 0; i < TOTAL_LEVEL - starlevel; i++) {
            ImageView ivNoStar = new ImageView(mContext);
            ivNoStar.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(), R.drawable.star_thumb_unfav));
            llStarFav.addView(ivNoStar);
        }
    }

    /**
     * @param urlImgs          图片链接
     * @param llEvaluationImgs 放置评价图片的layout
     */
    private void setCommentImgs(List<String> urlImgs, LinearLayout llEvaluationImgs) {
        if (urlImgs != null && llEvaluationImgs != null) {
            llEvaluationImgs.removeAllViews();
            for (String urlImg : urlImgs) {
                if (urlImg != null) {
                    ImageView v = new ImageView(mContext);
                    v.setImageBitmap(BitmapUtil.getHttpBitmap(urlImg));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    params.setMargins(10,5,5,0);
                    llEvaluationImgs.addView(v, params);
                }
            }
        }
    }

    /**
     * 设置头像
     * @param urlImg
     * @param ivUserThumb
     */
    private void setUserThumb(String urlImg,ImageView ivUserThumb){
        if(urlImg!=null){
            ivUserThumb.setImageBitmap(BitmapUtil.getHttpBitmap(urlImg));
        }else{
            ivUserThumb.setImageBitmap(BitmapFactory.decodeResource(mContext.getResources(),R.drawable.temp_user_info_ipc));
        }
    }

    private class ViewHolder {
        public ImageView ivUserThumb;
        public TextView tvNickName;//用户名
        public LinearLayout llStarFav;//5星
        public TextView tvAvgFee;//每人的平均消费
        public TextView tvEvaluationContent;//评价内容
        public LinearLayout llEvaluationImgs;//评价图片组
    }
}
