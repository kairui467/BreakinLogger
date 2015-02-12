package com.example.BreakinLogger.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.*;
import android.widget.AbsListView.OnScrollListener;
import com.example.BreakinLogger.R;
import com.example.BreakinLogger.service.DBOpenHelper;
import com.example.BreakinLogger.service.PersonService;

import java.util.ArrayList;
import java.util.HashMap;

public class MoreDateListActivity extends Activity implements OnScrollListener
{
    // ListView的Adapter
    private SimpleAdapter mSimpleAdapter;

    private SimpleCursorAdapter mSimpleCursorAdapter;

    private PersonService mPersonService;

    private ListView mlistView;
    private ProgressBar pg;
    private ArrayList<HashMap<String, String>> list;
    // ListView 底部 View
    private View moreView;
    private Handler handler;
    // 设置一个最大的数据条数，超过即不再加载
    private int MaxDateNum;
    // 最后可见条目的索引
    private int lastVisibleIndex;

    String TAG = "kerray";

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mPersonService = new PersonService(getApplicationContext());

        init2();
    }

    public void init()
    {
        MaxDateNum = 100; // 设置最大数据条数

        mlistView = (ListView) findViewById(R.id.listView);

        // 实例化底部布局
        moreView = getLayoutInflater().inflate(R.layout.moredata, null);

        pg = (ProgressBar) moreView.findViewById(R.id.pg);
        handler = new Handler();

        // 用map来装载数据，初始化10条数据
        list = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < 10; i++)
        {
            HashMap<String, String> map = new HashMap<String, String>();
            map.put("ItemTitle", "第" + i + "行标题");
            map.put("ItemText", "第" + i + "行内容");
            list.add(map);
        }
        // 实例化SimpleAdapter
        mSimpleAdapter = new SimpleAdapter(this, list, R.layout.layout_item_two,
          new String[] { "ItemTitle", "ItemText" },
          new int[] { R.id.text_two_time, R.id.text_two_PMName });

        // 加上底部View，注意要放在setAdapter方法前
        mlistView.addFooterView(moreView);
        mlistView.setAdapter(mSimpleAdapter);
        // 绑定监听器
        mlistView.setOnScrollListener(this);
    }

    public void init2()
    {
        MaxDateNum = (int) mPersonService.getNameCount("Airflare(大回环)"); // 设置最大数据条数

        //Log.i(TAG,mPersonService.getNameCount("Airflare(大回环)") + "");

        mlistView = (ListView) findViewById(R.id.listView);

        // 实例化底部布局
        moreView = getLayoutInflater().inflate(R.layout.moredata, null);

        pg = (ProgressBar) moreView.findViewById(R.id.pg);
        handler = new Handler();

        Cursor cursor = mPersonService.getCursorScrollData(0, 10);
        mSimpleCursorAdapter = new SimpleCursorAdapter(MoreDateListActivity.this, R.layout.item, cursor,
          new String[] { "_id", DBOpenHelper.PM_TIME, DBOpenHelper.PM_NAME, DBOpenHelper.PM_NUMBER },
          new int[] { R.id.text_item_two_id, R.id.text_item_two_time, R.id.text_item_two_PMName, R.id.text_item_two_number }, 0);

        // 加上底部View，注意要放在setAdapter方法前
        mlistView.addFooterView(moreView);
        mlistView.setAdapter(mSimpleCursorAdapter);
        // 绑定监听器
        mlistView.setOnScrollListener(this);
    }

    private void loadMoreDate()
    {
        int count = mSimpleCursorAdapter.getCount();
        if (count + 5 < MaxDateNum)
        {
            Cursor cursor = mPersonService.getCursorScrollData(0, 20);
            /*mSimpleCursorAdapter = new SimpleCursorAdapter(MoreDateListActivity.this, R.layout.item, cursor,
              new String[] { "_id", DBOpenHelper.PM_TIME, DBOpenHelper.PM_NAME, DBOpenHelper.PM_NUMBER },
              new int[] { R.id.text_item_two_id, R.id.text_item_two_time, R.id.text_item_two_PMName, R.id.text_item_two_number }, 0);*/
            // 每次加载5条
            /*for (int i = count; i < count + 5; i++)
            {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemTitle", "新增第" + i + "行标题");
                map.put("ItemText", "新增第" + i + "行内容");
                list.add(map);
            }*/
        } else
        {
            // 数据已经不足5条
            for (int i = count; i < MaxDateNum; i++)
            {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("ItemTitle", "新增第" + i + "行标题");
                map.put("ItemText", "新增第" + i + "行内容");
                list.add(map);
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
      int visibleItemCount, int totalItemCount)
    {
        String str = "firstVisibleItem = " + firstVisibleItem + ",visibleItemCount = " + visibleItemCount + ",totalItemCount = " + totalItemCount;
        //Log.i(TAG, str);
        // 计算最后可见条目的索引
        lastVisibleIndex = firstVisibleItem + visibleItemCount - 1;

        // 所有的条目已经和最大条数相等，则移除底部的View
        if (totalItemCount == MaxDateNum + 1)
        {
            //mlistView.removeFooterView(moreView);
            Toast.makeText(this, "数据全部加载完成，没有更多数据！", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState)
    {
        // 滑到底部后自动加载，判断listview已经停止滚动并且最后可视的条目等于adapter的条目
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && lastVisibleIndex == mSimpleCursorAdapter.getCount())
        {
            // 当滑到底部时自动加载
            pg.setVisibility(View.VISIBLE);
            handler.postDelayed(new Runnable()
            {
                public void run()
                {
                    loadMoreDate();
                    pg.setVisibility(View.GONE);
                    //mSimpleCursorAdapter.notifyDataSetChanged();
                    mlistView.setAdapter(mSimpleCursorAdapter);
                }

            }, 1000);

        }

    }

}