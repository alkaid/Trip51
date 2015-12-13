package com.alkaid.trip51.base.widget.view;

import android.content.ContentResolver;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alkaid.trip51.R;
import com.alkaid.trip51.base.data.HistorySearchSuggestionHelper;
import com.alkaid.trip51.base.widget.BaseFragment;
import com.alkaid.trip51.base.widget.adapter.BasicAdapter;
import com.alkaid.trip51.util.KeyboardUtils;
import com.alkaid.trip51.util.ViewUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// Referenced classes of package com.dianping.base.basic:
//            HistorySearchSuggestionHelper

public abstract class AbstractSearchFragment extends BaseFragment
        implements android.widget.AdapterView.OnItemClickListener, android.view.View.OnClickListener {
    public static class SuggestionItem implements Serializable {
        public String keyword;
        public String displayInfo;
        public int resultCount;

        public SuggestionItem() {
        }

        public SuggestionItem(String keyword) {
            this.keyword = keyword;
        }
    }

    protected final class BaseSuggestionAdapter extends BasicAdapter {

        private List<SuggestionItem> suggestionList;

        public int getCount() {
            int i;
            if (suggestionList.size() == 0)
                i = 1;
            else
                i = suggestionList.size();
            return i;
        }

        public Object getItem(int i) {
            Object obj;
            if (i < suggestionList.size())
                obj = suggestionList.get(i);
            else
                obj = AbstractSearchFragment.NO_SUGGESTION;
            return obj;
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            Object obj = getItem(i);
            View view1;
            if (obj == AbstractSearchFragment.NO_SUGGESTION)
                view1 = getSuggestionEmptyView(searchEditText.getText().toString(), viewgroup);
            else
                view1 = createSuggestionItem((SuggestionItem) obj, i, view, viewgroup);
            return view1;
        }

        public void setSuggestionList(List<SuggestionItem> suggestionItems) {
            suggestionList.clear();
            suggestionList.addAll(suggestionItems);
            notifyDataSetChanged();
        }

        public BaseSuggestionAdapter(List<SuggestionItem> suggestionItems) {
            super();
            suggestionList = new ArrayList<>();
            suggestionList.addAll(suggestionItems);
        }
    }

    protected class HistoryAdapter extends BasicAdapter {

        private List<String> historylist;

        public int getCount() {
            return historylist.size();
        }

        public Object getItem(int i) {
            return historylist.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            return createHistoryItem(i, view, viewgroup);
        }

        public void setHistoryList(List<String> historyList) {
            historylist.clear();
            if (historyList != null && historyList.size() > 0)
                historylist.addAll(historyList);
            if (historylist.size() > 0)
                historylist.add(AbstractSearchFragment.CLEARHISTORY);
            notifyDataSetChanged();
        }

        public HistoryAdapter() {
            super();
            historylist = new ArrayList();
        }

        public HistoryAdapter(ArrayList arraylist) {
            super();
            historylist = new ArrayList();
            historylist.addAll(arraylist);
            if (historylist.size() > 0)
                historylist.add(AbstractSearchFragment.CLEARHISTORY);
        }
    }

    public static interface OnSearchFragmentListener {
        public abstract void onSearchFragmentDetach();

        public abstract void startSearch(String keyword);
    }


    protected static final String CLEARHISTORY = "清除搜索记录";
    protected static final int HEADER_VIEW_HOT_SEARCH = 0;
    protected static final int HEADER_VIEW_HOT_SEARCH_KEYWORDS = 1;
    protected static final int HEADER_VIEW_SEARCH_HISTORY = 2;
    private static final String HOTWORD_RED_TYPE = "1";
    private static final String HOTWORD_RED_WITH_ICON = "2";
    static final Object NO_SUGGESTION = new Object();
    protected static final int SEARCH_MODE_HISTORY = 1;
    protected static final int SEARCH_MODE_SUGGEST = 2;
    private static final int SEARCH_SIZE_LIMIT = 10;
    protected static final int SEARCH_SUGGEST_MESSAGE = 1;
    protected static final int HISTORY_SUGGEST_BACK = 2;
    LinearLayout containerLayout;
    private String dpobjKeyword;
    protected boolean hasHotwordView;
    protected HistoryAdapter historyListAdapter;
    protected ListView listView;
    protected View mClearButton;
    protected ContentResolver mContentResolver;
    protected final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SEARCH_SUGGEST_MESSAGE:
                    if (msg.obj instanceof String) {
                        String s = (String) msg.obj;
                        if (TextUtils.isEmpty(s))
                            mSearchMode = SEARCH_MODE_HISTORY;
                        else
                            mSearchMode = SEARCH_MODE_SUGGEST;
//                        searchSuggest(s);
                    }
                    break;
                case HISTORY_SUGGEST_BACK:
                    if (!searchHistoryList.isEmpty()) {
                        historyListAdapter.setHistoryList(searchHistoryList);
                    }
                    break;
            }
        }
    };
    protected List<View> mHeaderViews;
    private OnSearchFragmentListener mOnSearchFragmentListener;
    protected int mSearchMode;
    protected String queryid;
    private String referPageName;
    protected EditText searchEditText;
    protected String searchHint;
    private List<String> searchHistoryList;
    protected BaseSuggestionAdapter suggestListAdapter;
    protected TextWatcher textWatcher;

    public AbstractSearchFragment() {
        mSearchMode = 1;
        searchHistoryList = new ArrayList<>();
        mHeaderViews = new ArrayList<>();
        hasHotwordView = true;
        containerLayout = null;
    }

//    public Uri buildUri(DPObject dpobject)
//    {
//        return null;
//    }

    protected View createHistoryItem(int i, View view, ViewGroup viewgroup) {
        String dpobject = (String) historyListAdapter.getItem(i);
        View novalinearlayout;
        TextView textview;
        if (view instanceof LinearLayout)
            novalinearlayout = (LinearLayout) view;
        else
            novalinearlayout = null;
        if (novalinearlayout == null)
            novalinearlayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.search_list_item, viewgroup, false);
        textview = (TextView) novalinearlayout.findViewById(android.R.id.text1);
        textview.setText(dpobject);
        if (i == -1 + historyListAdapter.getCount()) {
            novalinearlayout.findViewById(R.id.divider).setVisibility(View.GONE);
            novalinearlayout.findViewById(R.id.list_view_end_divider).setVisibility(View.VISIBLE);
        } else {
            novalinearlayout.findViewById(R.id.divider).setVisibility(View.VISIBLE);
            novalinearlayout.findViewById(R.id.list_view_end_divider).setVisibility(View.GONE);
        }
        if (i == 0)
            novalinearlayout.findViewById(R.id.list_view_start_divider).setVisibility(View.VISIBLE);
        else
            novalinearlayout.findViewById(R.id.list_view_start_divider).setVisibility(View.GONE);
        if (dpobject == CLEARHISTORY)
            textview.setGravity(Gravity.CENTER);
        else
            textview.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        return novalinearlayout;
    }


    protected View createSuggestionItem(SuggestionItem suggestionItem, int i, View view, ViewGroup viewgroup) {
        LinearLayout novalinearlayout;
        if (view instanceof LinearLayout)
            novalinearlayout = (LinearLayout) view;
        else
            novalinearlayout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.search_list_item, viewgroup, false);
        ((TextView) novalinearlayout.findViewById(android.R.id.text1)).setText(suggestionItem.keyword);
        if (!TextUtils.isEmpty(suggestionItem.displayInfo))
            ((TextView) novalinearlayout.findViewById(android.R.id.text2)).setText(suggestionItem.displayInfo);
        else
            ((TextView) novalinearlayout.findViewById(android.R.id.text2)).setText((new StringBuilder()).append("共").append(suggestionItem.resultCount).append("个结果").toString());
        return novalinearlayout;
    }

    public abstract String getFileName();

    public View getHeaderView(String s, int i, int j, int k) {
        LinearLayout linearlayout = new LinearLayout(getActivity());
        TextView textview = new TextView(getActivity());
        android.widget.LinearLayout.LayoutParams layoutparams = new android.widget.LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0F);
        layoutparams.leftMargin = ViewUtils.dip2px(getActivity(), 15F);
        layoutparams.topMargin = ViewUtils.dip2px(getActivity(), j);
        layoutparams.bottomMargin = ViewUtils.dip2px(getActivity(), k);
        textview.setTextSize(0, getResources().getDimensionPixelSize(R.dimen.text_size_14));
        textview.setLayoutParams(layoutparams);
        textview.setText(s);
        textview.setTextColor(getResources().getColor(R.color.light_gray));
        textview.setGravity(19);
        linearlayout.addView(textview);
        linearlayout.setTag(Integer.valueOf(i));
        linearlayout.setClickable(true);
        mHeaderViews.add(linearlayout);
        return linearlayout;
    }

    protected int getHistoryCount() {
        return searchHistoryList.size();
    }

    public String getHistoryWord(int i) {
        String dpobject;
        if (searchHistoryList != null && searchHistoryList.size() > i)
            dpobject = searchHistoryList.get(i);
        else
            dpobject = null;
        return dpobject;
    }

//    protected View getHotWordView(ArrayList arraylist)
//    {
//        if(containerLayout == null)
//        {
//            containerLayout = new LinearLayout(getActivity());
//            containerLayout.setOrientation(0);
//            int i = ViewUtils.dip2px(getActivity(), 12F);
//            containerLayout.setPadding(i, 0, i, 0);
//            CustomGridView customgridview = new CustomGridView(getActivity());
//            HotWordAdapter hotwordadapter = new HotWordAdapter(arraylist);
//            customgridview.setStretchAllColumns(true);
//            customgridview.setAdapter(hotwordadapter);
//            customgridview.setOnItemClickListener(this);
//            customgridview.setTag(Integer.valueOf(1));
//            containerLayout.addView(customgridview);
//        } else
//        {
//            ((HotWordAdapter)((CustomGridView)containerLayout.getChildAt(0)).getAdapter()).setData(arraylist);
//        }
//        containerLayout.setClickable(true);
//        mHeaderViews.add(containerLayout);
//        return containerLayout;
//    }

    protected BaseSuggestionAdapter getSuggestListAdapter(ArrayList arraylist) {
        if (suggestListAdapter == null)
            suggestListAdapter = new BaseSuggestionAdapter(arraylist);
        else
            suggestListAdapter.setSuggestionList(arraylist);
        return suggestListAdapter;
    }

//    protected ArrayList getSuggestListFromResponse(Object obj)
//    {
//        ArrayList arraylist = new ArrayList();
//        if(obj instanceof DPObject)
//        {
//            DPObject dpobject = (DPObject)obj;
//            queryid = dpobject.getString("QueryID");
//            if(dpobject.getArray("List") != null)
//                arraylist.addAll(Arrays.asList(dpobject.getArray("List")));
//        }
//        return arraylist;
//    }

    public View getSuggestionEmptyView(String s, ViewGroup viewgroup) {
        View listitemView = getActivity().getLayoutInflater().inflate(R.layout.search_list_item, viewgroup, false);
        ((TextView) listitemView.findViewById(android.R.id.text1)).setText((new StringBuilder()).append("查找'").append(s).append("'").toString());
        return listitemView;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        mContentResolver = getActivity().getContentResolver();
        if (hasHotwordView && dpobjKeyword == null) {
            Message message = mHandler.obtainMessage(SEARCH_SUGGEST_MESSAGE, "");
            mHandler.sendMessage(message);
        }
        historyListAdapter = new HistoryAdapter();
        listView.setAdapter(historyListAdapter);
        mSearchMode = 1;
        listView.setOnItemClickListener(this);
        (new Thread(new Runnable() {
            @Override
            public void run() {
                searchHistoryList = HistorySearchSuggestionHelper.queryForChannel(mContentResolver, getFileName());
                mHandler.removeMessages(AbstractSearchFragment.HISTORY_SUGGEST_BACK);
                mHandler.sendEmptyMessage(AbstractSearchFragment.HISTORY_SUGGEST_BACK);
            }


        }
        )).start();
        KeyboardUtils.popupKeyboard(searchEditText);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.back) {
            onBackPressed();
        }
    }

    @Override
    public boolean onBackPressed() {
        KeyboardUtils.hideKeyboard(searchEditText);
        if (getFragmentManager() != null)
            getFragmentManager().popBackStackImmediate();
        return false;
    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        try {
            if (mOnSearchFragmentListener == null)
                mOnSearchFragmentListener = (OnSearchFragmentListener) getActivity();
        } catch (ClassCastException classcastexception) {
        }
        if (getArguments() != null) {
            hasHotwordView = getArguments().getBoolean("hasHotwordView", true);
            searchHint = getArguments().getString("searchHint");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.base_search_layout, container, false);
        mClearButton = view.findViewById(R.id.clearBtn);
        mClearButton.setOnClickListener(
                new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(View view1) {
                        searchEditText.setText("");
                    }
                }
        );
        listView = (ListView) view.findViewById(android.R.id.list);
//        BitmapUtils.fixBackgroundRepeat(listView);
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.GINGERBREAD_MR1)
            listView.setScrollingCacheEnabled(false);
        listView.setDivider(null);
        view.findViewById(R.id.back).setOnClickListener(this);
        searchEditText = (EditText) view.findViewById(R.id.search_edit);
        searchEditText.setOnKeyListener(
                new android.view.View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        boolean flag = false;
                        if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP)
                            if (TextUtils.isEmpty(searchEditText.getText().toString().trim())) {
                                flag = true;
                            } else {
                                search(searchEditText.getText().toString().trim());
                                flag = true;
                            }
                        return flag;
                    }
                }
        );
        textWatcher = new TextWatcher() {
            String mLastKeyword;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String value = editable.toString().trim();
                if (TextUtils.isEmpty(value)) {
                    mClearButton.setVisibility(View.INVISIBLE);
                    mHandler.removeMessages(AbstractSearchFragment.SEARCH_SUGGEST_MESSAGE);
                    if (hasHotwordView) {
                        Message message1 = mHandler.obtainMessage(AbstractSearchFragment.SEARCH_SUGGEST_MESSAGE, value);
                        mHandler.sendMessage(message1);
                    }
                    listView.setAdapter(historyListAdapter);
                    mSearchMode = SEARCH_MODE_HISTORY;
                } else {
                    mClearButton.setVisibility(View.VISIBLE);
                    if (!value.equals(mLastKeyword)) {
                        mHandler.removeMessages(AbstractSearchFragment.SEARCH_SUGGEST_MESSAGE);
                        Message message = mHandler.obtainMessage(AbstractSearchFragment.SEARCH_SUGGEST_MESSAGE, value);
                        mHandler.sendMessage(message);
                    }
                    mSearchMode = SEARCH_MODE_SUGGEST;
                }
                mLastKeyword = value;
            }
        };
        searchEditText.addTextChangedListener(textWatcher);
        if (!TextUtils.isEmpty(searchHint))
            searchEditText.setHint(searchHint);
        if (dpobjKeyword != null && !TextUtils.isEmpty(dpobjKeyword)) {
            searchEditText.setText(dpobjKeyword);
            searchEditText.setSelection(dpobjKeyword.length());
        }
        final TextView searchBtn = (TextView) view.findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(
                new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(searchEditText.getText().toString().trim())) {
                            String s;
                            android.content.Context context;
                            String s1;
                            String dpobject;
                            if (TextUtils.isEmpty(searchEditText.getText().toString().trim()))
                                s = "";
                            else
                                s = searchEditText.getText().toString().trim();
                            context = searchBtn.getContext();
                            if (TextUtils.isEmpty(searchEditText.getText().toString().trim()))
                                s1 = "searchBtn";
                            else
                                s1 = "buttonsearch";
                            dpobject = searchEditText.getText().toString().trim();
                            search(dpobject);
                        }
                    }
                }
        );
        view.setClickable(true);
        return view;
    }

    public void onDetach() {
        if (mOnSearchFragmentListener != null) {
            mOnSearchFragmentListener.onSearchFragmentDetach();
            mOnSearchFragmentListener = null;
        }
        super.onDetach();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object obj = listView.getItemAtPosition(position);
        if (mSearchMode == SEARCH_MODE_HISTORY) {
            if (view.getTag() instanceof Integer)
                return;
            if (obj == CLEARHISTORY) {
                (new Thread(new Runnable() {
                    @Override
                    public void run() {
                        HistorySearchSuggestionHelper.deleteChannel(mContentResolver, getFileName());
                    }
                }
                )).start();
                searchHistoryList.clear();
                if (hasHotwordView)
                    removeSearchListHeaderView();
                historyListAdapter.setHistoryList(searchHistoryList);
                mSearchMode = SEARCH_MODE_HISTORY;
            } else if (obj instanceof String) {
                searchEditText.setText((String) obj);
                searchEditText.setSelection(searchEditText.getText().length());
//            String s1 = ((DPObject) obj).getString("Value");
//            if (s1 == null)
//                s1 = "";
//            if (!s1.contains("history"))
//                if (TextUtils.isEmpty(s1))
//                    s1 = (new StringBuilder()).append(s1).append("history%3A1").toString();
//                else
//                    s1 = (new StringBuilder()).append(s1).append("%3Bhistory%3A1").toString();
//            search((DPObject) ((DPObject) obj).edit().putString(getResources().getString(R.string.search_keyword_ga_suffix), "_history").putString("Value", s1).generate());
                search((String) obj);
            }
        }
        if (mSearchMode == SEARCH_MODE_SUGGEST)
            if (obj instanceof SuggestionItem)
//                search((DPObject) ((DPObject) obj).edit().putString(getResources().getString(R.string.search_keyword_ga_suffix), "_suggest").generate());
                search(((SuggestionItem) obj).keyword);
            else if (obj == NO_SUGGESTION) {
                String s = searchEditText.getText().toString().trim();
                if (!TextUtils.isEmpty(s))
//                    search((new DPObject()).edit().putString("Keyword", s).putString(getResources().getString(R.string.search_keyword_ga_suffix), "_suggest").generate());
                search(s);
            }
    }

//    public void onItemClick(CustomGridView customgridview, View view, int i, long l) {
//        DPObject dpobject = (DPObject) view.getTag();
//        if (getActivity() instanceof DPActivity)
//            search(dpobject.edit().putString(getResources().getString(R.string.search_keyword_ga_suffix), "_hot").putInt(getResources().getString(R.string.search_keyword_ga_position), i + 1).generate());
//    }

    @Override
    public void onPause() {
        super.onPause();
    }

    //TODO suggestion
//    public void onRequestFinish(MApiRequest mapirequest, MApiResponse mapiresponse) {
//        if (request != mapirequest)goto _L2;else goto _L1
//        _L1:
//        if (mapiresponse.result() == null)goto _L4;else goto _L3
//        _L3:
//        if (getActivity() != null)goto _L6;else goto _L5
//        _L5:
//        request = null;
//        _L2:
//        return;
//        _L6:
//        ArrayList arraylist;
//        arraylist = getSuggestListFromResponse(mapiresponse.result());
//        resetListView();
//        listView.setHeaderDividersEnabled(false);
//        if (mSearchMode != 2)goto _L8;else goto _L7
//        _L7:
//        mHeaderViews.clear();
//        listView.setAdapter(getSuggestListAdapter(arraylist));
//        _L4:
//        request = null;
//        if (getActivity() != null) {
//            GAUserInfo gauserinfo = new GAUserInfo();
//            gauserinfo.query_id = queryid;
//            String s = Uri.parse(mapirequest.url()).getQueryParameter("keyword");
//            if (s == null)
//                s = "";
//            gauserinfo.keyword = s;
//            GAHelper.instance().setRequestId(getActivity(), UUID.randomUUID().toString(), gauserinfo, false);
//        }
//        if (true)goto _L2;else goto _L8
//        _L8:
//        if (mSearchMode == 1) {
//            if (hasHotwordView) {
//                if (arraylist != null && arraylist.size() > 0)
//                    listView.addHeaderView(getHeaderView("\u70ED\u95E8\u641C\u7D22", 0, 10, 10));
//                listView.addHeaderView(getHotWordView(arraylist));
//            }
//            if (getHistoryCount() > 0)
//                listView.addHeaderView(getHeaderView("\u641C\u7D22\u5386\u53F2", 2, 5, 10));
//            listView.setAdapter(historyListAdapter);
//        }
//        goto _L4
//    }

    public void removeSearchListHeaderView() {
        Iterator iterator = mHeaderViews.iterator();
        do {
            if (!iterator.hasNext())
                break;
            View view = (View) iterator.next();
            if (view.getTag() == null || !(view.getTag() instanceof Integer) || Integer.parseInt(view.getTag().toString()) != 2)
                continue;
            listView.removeHeaderView(view);
            break;
        } while (true);
    }

    protected void resetListView() {
        View view;
        for (Iterator iterator = mHeaderViews.iterator(); iterator.hasNext(); listView.removeHeaderView(view))
            view = (View) iterator.next();

        listView.setAdapter(null);
    }

    protected void search(String dpobject) {
        if (dpobject == null)
            return;
        final String keyword = dpobject;
        if (!TextUtils.isEmpty(keyword)) {
            dpobjKeyword = dpobject;
            if (mOnSearchFragmentListener != null)
                mOnSearchFragmentListener.startSearch(dpobjKeyword);
            (new Thread(new Runnable() {
                public void run() {
                    String s = getFileName();
                    if (!TextUtils.isEmpty(s)) {
                        int i = s.indexOf(",");
                        String s1;
                        if (i == -1)
                            s1 = s;
                        else
                            s1 = s.substring(0, i);
                        HistorySearchSuggestionHelper.insert(mContentResolver, keyword, null, s1);
                    }
                }
            }
            )).start();
            KeyboardUtils.hideKeyboard(searchEditText);
            listView.setVisibility(View.GONE);
            getFragmentManager().popBackStackImmediate();
        }
    }

    //TODO suggesiont
//    protected void searchSuggest(String s) {
//        if (request != null) {
//            mapiService().abort(request, null, true);
//            request = null;
//        }
//        request = createRequest(s);
//        if (request != null)
//            mapiService().exec(request, this);
//    }

    public void setKeyword(String keyword) {
        if (!TextUtils.isEmpty(keyword))
            dpobjKeyword = keyword;
    }

    public void setOnSearchFragmentListener(OnSearchFragmentListener onsearchfragmentlistener) {
        mOnSearchFragmentListener = onsearchfragmentlistener;
    }
}
