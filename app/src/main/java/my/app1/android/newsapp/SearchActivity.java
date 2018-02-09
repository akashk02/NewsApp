package my.app1.android.newsapp;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import my.app1.android.newsapp.RecyclerView.NewsApapter;
import my.app1.android.newsapp.retrofit.News_Api;
import my.app1.android.newsapp.retrofit.PostList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public class SearchActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {


    private List<NewsInfo> newsList ;
    private RecyclerView recyclerView;
    private NewsApapter mAdapter;
    private ProgressBar progressBar ;
    private SwipeRefreshLayout swipeView ;
    private String SEARCH_URL ;
    private Button button;

    public void onRefresh(){
        mAdapter.clear();
        getData(SEARCH_URL);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        button = findViewById(R.id.try_again);
        button.setVisibility(View.GONE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        SpannableString s = new SpannableString("US");
        s.setSpan(new TypefaceSpan(this, "Lobster.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(s);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.indeterminateBar);


        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        newsList = new ArrayList<>();
        mAdapter = new NewsApapter(newsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        swipeView = findViewById(R.id.swiperefresh);
        swipeView.setOnRefreshListener(this);

        handleIntent(getIntent());

    }


    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            SEARCH_URL = "https://newsapi.org/v2/everything?q="+query+"&sortBy=publishedAt&apiKey=11bce9f0b7594fedb7b9f711c8122993";
            getData(SEARCH_URL);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }




    public void getData(String url1){

        Call<PostList> postList = News_Api.getService().getPostList(url1);

        postList.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {

                PostList list = response.body();
                List<NewsInfo> result = QueryUtils.fetchNewsData(list);
                progressBar.setVisibility(View.GONE);

                if (swipeView.isRefreshing()) {
                    swipeView.setRefreshing(false);
                }


                if (result != null && !result.isEmpty()) {
                    newsList.addAll(result);
                    mAdapter.notifyItemInserted(newsList.size() - 1);
                }
            }


            @Override
            public void onFailure(Call<PostList> call, Throwable t) {

                if (   !(QueryUtils.checkInternetConnection(SearchActivity.this)) ) {
                    button.setVisibility(View.VISIBLE);
                    Toast.makeText(SearchActivity.this, R.string.no_network, Toast.LENGTH_LONG).show();
                } else {
                    getData(URL);
                }
            }
        });
    }
}
