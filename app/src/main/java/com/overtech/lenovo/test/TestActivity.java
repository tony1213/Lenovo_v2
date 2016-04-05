package com.overtech.lenovo.test;
import java.util.Arrays;
import java.util.LinkedList;
import com.overtech.lenovo.R;
import com.overtech.lenovo.widget.pulltorefresh.PullToRefreshBase;
import com.overtech.lenovo.widget.pulltorefresh.PullToRefreshBase.Mode;
import com.overtech.lenovo.widget.pulltorefresh.PullToRefreshListView;
import com.overtech.lenovo.widget.pulltorefresh.RefreshTime;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class TestActivity extends Activity {
    private PullToRefreshListView mPullToRefreshListView;
    private LinkedList<String> mListItems;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Set a listener to be invoked when the list should be refreshed.  
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.listview);
        mPullToRefreshListView.setMode(Mode.BOTH);
        mPullToRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

            // 下拉Pulling Down
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel("上次刷新："+RefreshTime.getRefreshTime(getApplicationContext()));
                new GetDataTask().execute();
            }

            // 上拉Pulling Up
            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                new GetDataTask().execute();
            }

        });

        //设置PullRefreshListView上提加载时的加载提示
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载...");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载...");
        mPullToRefreshListView.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多...");

        // 设置PullRefreshListView下拉加载时的加载提示
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新...");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新...");
        mPullToRefreshListView.getLoadingLayoutProxy(true, false).setReleaseLabel("松开刷新...");
        ListView actualListView = mPullToRefreshListView.getRefreshableView();
        mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(mStrings));
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListItems);
        actualListView.setAdapter(mAdapter);
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.  
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
            }
            return mStrings;
        }
        @Override
        protected void onPostExecute(String[] result) {
            mListItems.addFirst("Added after refresh...");
            mAdapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.  
            mPullToRefreshListView.onRefreshComplete();
            super.onPostExecute(result);
        }
    }

    private String[] mStrings = { "John", "Michelle", "Amy", "Kim", "Mary",
            "David", "Sunny", "James", "Maria", "Michael", "Sarah", "Robert",
            "Lily", "William", "Jessica", "Paul", "Crystal", "Peter",
            "Jennifer", "George", "Rachel", "Thomas", "Lisa", "Daniel", "Elizabeth",
            "Kevin" };
}  