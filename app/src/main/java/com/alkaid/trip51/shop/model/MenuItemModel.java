package com.alkaid.trip51.shop.model;

import android.graphics.Bitmap;


/**菜单右边单个item的数据模型
 * Created by jyz on 2015/12/1.
 */
public class MenuItemModel {


    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSold_num() {
        return sold_num;
    }

    public void setSold_num(int sold_num) {
        this.sold_num = sold_num;
    }

    public int getSelect_buy_num() {
        return select_buy_num;
    }

    public void setSelect_buy_num(int select_buy_num) {
        this.select_buy_num = select_buy_num;
    }

    public Bitmap getIcon() {
        return icon;
    }

    public void setIcon(Bitmap icon) {
        this.icon = icon;
    }
    public int getSectionNum() {
        return sectionNum;
    }
    public void setSectionNum(int sectionNum) {
        this.sectionNum = sectionNum;
    }
    public int getPositionNum() {
        return positionNum;
    }
    public void setPositionNum(int positionNum) {
        this.positionNum = positionNum;
    }

    private String name;
    private String price;
    private int sold_num;
    private int select_buy_num;
    private Bitmap icon;
    private int sectionNum;//所在第几部分
    private int positionNum;//所在第几个位置
}
