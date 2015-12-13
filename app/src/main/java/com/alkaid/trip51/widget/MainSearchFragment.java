package com.alkaid.trip51.widget;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.alkaid.trip51.base.widget.view.AbstractSearchFragment;


public class MainSearchFragment extends AbstractSearchFragment
{

    protected static final int ADVANCED_SUGGEST = 1;
    protected static final int HOT_WORD = 2;
    protected static final int NORMAL_SUGGEST = 0;
    protected int mCategoryId;

    public MainSearchFragment()
    {
        mCategoryId = 0;
    }

//    public static MainSearchFragment newInstance(FragmentActivity fragmentactivity)
//    {
//        return newInstance(fragmentactivity, 0);
//    }

    public static MainSearchFragment newInstance(FragmentActivity fragmentactivity)
    {
        MainSearchFragment mainsearchfragment = new MainSearchFragment();
//        mainsearchfragment.mCategoryId = i;
        FragmentTransaction fragmenttransaction = fragmentactivity.getSupportFragmentManager().beginTransaction();
        fragmenttransaction.add( android.R.id.content, mainsearchfragment);
        fragmenttransaction.addToBackStack(null);
        fragmenttransaction.commitAllowingStateLoss();
        return mainsearchfragment;
    }

//    public MApiRequest createRequest(String s)
//    {
//        StringBuilder stringbuilder = new StringBuilder((new StringBuilder()).append("http://m.api.dianping.com/").append(endPoint).append("?").toString());
//        stringbuilder.append("cityid=").append(cityId());
//        DPObject dpobject;
//        MApiRequest mapirequest;
//        if(!TextUtils.isEmpty(s))
//            try
//            {
//                stringbuilder.append("&keyword=").append(URLEncoder.encode(s, "utf-8"));
//            }
//            catch(UnsupportedEncodingException unsupportedencodingexception)
//            {
//                unsupportedencodingexception.printStackTrace();
//            }
//        if(mCategoryId != 0)
//            stringbuilder.append("&categoryid=").append(String.valueOf(mCategoryId));
//        dpobject = locationService().location();
//        if(dpobject != null && dpobject.getDouble("Lat") != 0.0D && dpobject.getDouble("Lng") != 0.0D)
//        {
//            stringbuilder.append("&").append("mylat=").append(Location.FMT.format(dpobject.getDouble("Lat")));
//            stringbuilder.append("&").append("mylng=").append(Location.FMT.format(dpobject.getDouble("Lng")));
//        }
//        if(dpobject != null && dpobject.getInt("Accuracy") > 0)
//            stringbuilder.append("&").append("myacc=").append(dpobject.getInt("Accuracy"));
//        if(TextUtils.isEmpty(s))
//            mapirequest = BasicMApiRequest.mapiGet(stringbuilder.toString(), CacheType.CRITICAL);
//        else
//            mapirequest = BasicMApiRequest.mapiGet(stringbuilder.toString(), CacheType.NORMAL);
//        return mapirequest;
//    }
//
//    protected View createSuggestionItem(final DPObject suggestion, int i, View view, ViewGroup viewgroup)
//    {
//        Object obj;
//        if(!TextUtils.isEmpty(suggestion.getString("Keyword")))
//        {
//            obj = (NovaLinearLayout)createSuggestionItem(suggestion, i, view, viewgroup);
//            if(suggestion.getInt("SuggestType") == 1)
//            {
//                ((TextView)((NovaLinearLayout) (obj)).findViewById(0x1020014)).setTextColor(getResources().getColor(com.dianping.v1.R.color.advance_suggest_blue));
//                ((NovaLinearLayout) (obj)).setGAString("suggest_otherplace", suggestion.getString("Keyword"));
//                ((NovaActivity)getActivity()).addGAView(((View) (obj)), i);
//            } else
//            {
//                ((TextView)((NovaLinearLayout) (obj)).findViewById(0x1020014)).setTextColor(getResources().getColor(com.dianping.v1.R.color.deep_gray));
//                ((NovaLinearLayout) (obj)).setGAString("suggest", suggestion.getString("Keyword"));
//            }
//        } else
//        {
//            NovaRelativeLayout novarelativelayout;
//            if(view instanceof NovaRelativeLayout)
//                novarelativelayout = (NovaRelativeLayout)view;
//            else
//                novarelativelayout = (NovaRelativeLayout)getActivity().getLayoutInflater().inflate(com.dianping.v1.R.layout.suggest_list_direct_zone_item, viewgroup, false);
//            ((NetworkThumbView)novarelativelayout.findViewById(com.dianping.v1.R.id.thumb)).setImage(suggestion.getString("PicUrl"));
//            ((TextView)novarelativelayout.findViewById(com.dianping.v1.R.id.title)).setText(suggestion.getString("Title"));
//            ((TextView)novarelativelayout.findViewById(com.dianping.v1.R.id.abstract_text)).setText(com.dianping.util.TextUtils.highLightShow(getActivity(), suggestion.getString("Abstract"), com.dianping.v1.R.color.tuan_common_orange));
//            ((TextView)novarelativelayout.findViewById(com.dianping.v1.R.id.sub_title)).setText(suggestion.getString("Subtitle"));
//            ((TextView)novarelativelayout.findViewById(com.dianping.v1.R.id.high_light)).setText(com.dianping.util.TextUtils.highLightShow(getActivity(), suggestion.getString("ClickTips"), com.dianping.v1.R.color.tuan_common_orange));
//            novarelativelayout.setOnClickListener(new android.view.View.OnClickListener() {
//
//                final MainSearchFragment this$0;
//                final DPObject val$suggestion;
//
//                public void onClick(View view1)
//                {
//                    startActivity(suggestion.getString("ClickUrl"));
//                    getFragmentManager().popBackStackImmediate();
//                }
//
//
//            {
//                this$0 = MainSearchFragment.this;
//                suggestion = dpobject;
//                Object();
//            }
//            }
//);
//            novarelativelayout.setGAString("suggest_direct");
//            novarelativelayout.gaUserInfo.keyword = searchEditText.getText().toString().trim();
//            novarelativelayout.gaUserInfo.index = Integer.valueOf(i);
//            novarelativelayout.gaUserInfo.query_id = queryid;
//            GAHelper.instance().contextStatisticsEvent(getActivity(), "suggest_direct", novarelativelayout.gaUserInfo, "view");
//            obj = novarelativelayout;
//        }
//        return ((View) (obj));
//    }

    @Override
    public String getFileName()
    {
        return "find_main_search_fragment";
    }

//    protected ArrayList getSuggestListFromResponse(Object obj)
//    {
//        ArrayList arraylist = new ArrayList();
//        if(obj instanceof DPObject)
//        {
//            DPObject dpobject = ((DPObject)obj).getObject("DirectZoneResult");
//            if(dpobject != null)
//            {
//                DPObject adpobject[] = dpobject.getArray("List");
//                if(adpobject != null)
//                    arraylist.addAll(Arrays.asList(adpobject));
//            }
//        }
//        arraylist.addAll(getSuggestListFromResponse(obj));
//        return arraylist;
//    }
}
