package com.whos.swiperefreshandload;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import com.whos.swiperefreshandload.view.SwipeRefreshLayout;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout.OnLoadListener;
import com.whos.swiperefreshandload.view.SwipeRefreshLayout.OnRefreshListener;


@SuppressLint("ResourceAsColor")
public class MainActivity extends Activity implements OnRefreshListener, OnLoadListener{

    protected ListView mListView;
    private ArrayAdapter<String> mListAdapter;
    SwipeRefreshLayout mSwipeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mListView = (ListView) findViewById(R.id.list);
        mListAdapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_list_item_1, values);
		mListView.setAdapter(mListAdapter);


        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeLayout.setOnRefreshListener(this);
        mSwipeLayout.setOnLoadListener(this);
        mSwipeLayout.setColor(android.R.color.holo_blue_bright,
	                            android.R.color.holo_green_light,
	                            android.R.color.holo_orange_light,
	                            android.R.color.holo_red_light);
        mSwipeLayout.setMode(SwipeRefreshLayout.Mode.BOTH);
        mSwipeLayout.setLoadNoFull(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    ArrayList<String> values = new ArrayList<String>() {{
        add("value 0");
        add("value 1");
        add("value 2");
        add("value 3");
        add("value 4");
        add("value 5");
        add("value 6");
    }};

    @Override
    public void onRefresh() {
        values.add(0, "Add " + values.size());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(false);
                mListAdapter.notifyDataSetChanged();
            }
        }, 2000);
    }

    @Override
    public void onLoad() {
        values.add("Add " + values.size());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setLoading(false);
                mListAdapter.notifyDataSetChanged();
            }
        }, 1000);
    }
}
