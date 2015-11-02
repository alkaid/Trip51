package com.alkaid.trip51.base.widget;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alkaid.trip51.R;

public class ButtonSearchBar
  extends LinearLayout
  implements View.OnClickListener
{
  private static final String TAG = ButtonSearchBar.class.getSimpleName();
  private TextView btnSearch;
  private ImageView iconSearch;
  private ButtonSearchBarListener mListener;
  
  public ButtonSearchBar(Context context)
  {
    super(context);
  }
  
  public ButtonSearchBar(Context context, AttributeSet attributeSet)
  {
    super(context, attributeSet);
    LayoutInflater.from(context).inflate(R.layout.button_search_bar_inner, this, true);
    setId(R.id.button_search_bar);
    int i = attributeSet.getAttributeResourceValue("http://schemas.android.com/apk/res/android", "background", -1);
    if (i == -1) {
      i = R.drawable.search_bar_button_background;
    }
    setBackgroundResource(i);
    setGravity(19);
    setOrientation(LinearLayout.HORIZONTAL);
    this.btnSearch = ((TextView)findViewById(R.id.start_search));
    this.iconSearch = ((ImageView)findViewById(R.id.search_icon));
    setOnClickListener(this);
  }
  
  public static Intent getResultIntent(Bundle bundle, String keyword, String paramString2)
  {
    Intent localIntent = new Intent("android.intent.action.VIEW", Uri.parse("dianping://searchshoplist"));
    if (keyword != null)
    {
      localIntent.putExtra("query", keyword);
      localIntent.putExtra("keyword", keyword);
      localIntent.putExtra("suggest_text_1", keyword);
    }
    if (paramString2 != null) {
      localIntent.putExtra("suggest_text_2", paramString2);
    }
    if (bundle != null) {
      localIntent.putExtra("app_data", bundle);
    }
    return localIntent;
  }
  
  public ImageView getSearchIconView()
  {
    return this.iconSearch;
  }
  
  public TextView getSearchTextView()
  {
    return this.btnSearch;
  }
  
  public void onClick(View v)
  {
    if (this.mListener != null) {
      post(new Runnable()
      {
        public void run()
        {
          ButtonSearchBar.this.mListener.onSearchRequested();
        }
      });
    }
  }
  
  public void setButtonSearchBarListener(ButtonSearchBarListener buttonSearchBarListener)
  {
    this.mListener = buttonSearchBarListener;
  }
  
  public void setHint(int stringId)
  {
    if (this.btnSearch == null) {return;}
      if (stringId > 0) {
          this.btnSearch.setHint(stringId);
      } else {
        this.btnSearch.setHint(R.string.search_hint);
      }
  }
  
  public void setHint(String hint)
  {
    if (this.btnSearch != null) {
      this.btnSearch.setHint(hint);
    }
  }
  
  public void setKeyword(String keyword)
  {
    if (this.btnSearch != null) {
      this.btnSearch.setText(keyword);
    }
  }
  
  public static abstract interface ButtonSearchBarListener
  {
    public abstract void onSearchRequested();
  }
}



/* Location:           E:\Software\Coder\Android\crack\dex2jar-0.0.9.8\classes_dex2jar.jar

 * Qualified Name:     com.dianping.base.widget.ButtonSearchBar

 * JD-Core Version:    0.7.0.1

 */