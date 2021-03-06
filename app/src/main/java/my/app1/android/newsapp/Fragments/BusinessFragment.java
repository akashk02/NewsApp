package my.app1.android.newsapp.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import my.app1.android.newsapp.NewsInfo;
import my.app1.android.newsapp.QueryUtils;
import my.app1.android.newsapp.RecyclerView.EndlessRecyclerViewScrollListener;
import my.app1.android.newsapp.RecyclerView.NewsApapter;
import my.app1.android.newsapp.retrofit.News_Api;
import my.app1.android.newsapp.retrofit.PostList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class BusinessFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private final static String url1 = "https://newsapi.org/v2/top-headlines?country=us&category=business&apiKey=7e9add1df51e4f86b6bf207349693ab7";
    private final static String url2 = "https://newsapi.org/v2/everything?sources=cnbc&apiKey=dd1a5452fe6542ada57674f83ea365a5";
    private List<NewsInfo> newsList;
    private RecyclerView recyclerView;
    private NewsApapter mAdapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeView;
    private Button button;
    private EndlessRecyclerViewScrollListener scrollListener;
    private int loadCounter;

    public BusinessFragment() {
    }

    public void onRefresh() {
        mAdapter.clear();
        loadCounter = 0;
        button.setVisibility(View.GONE);
        getData(url1);
    }

    public void refresh() {
        mAdapter.clear();
        progressBar.setVisibility(View.VISIBLE);
        loadCounter = 0;
        button.setVisibility(View.GONE);
        getData(url1);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(my.app1.android.newsapp.R.layout.news_list, container, false);

        button = (Button) rootView.findViewById(my.app1.android.newsapp.R.id.try_again);
        button.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRefresh();
            }
        });

        loadCounter = 0;

        progressBar = rootView.findViewById(my.app1.android.newsapp.R.id.indeterminateBar);

        recyclerView = rootView.findViewById(my.app1.android.newsapp.R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        newsList = new ArrayList<>();
        mAdapter = new NewsApapter(newsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);


        scrollListener = new EndlessRecyclerViewScrollListener((LinearLayoutManager) mLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                loadNextDataFromApi(page);
            }
        };
        // Adds the scroll listener to RecyclerView
        recyclerView.addOnScrollListener(scrollListener);


        swipeView =  rootView.findViewById(my.app1.android.newsapp.R.id.swiperefresh);
        swipeView.setOnRefreshListener(this);

        getData(url1);

        return rootView;


    }


    public void getData(final String URL) {

        Call<PostList> postList = News_Api.getService().getPostList(URL);

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

                if (   !(QueryUtils.checkInternetConnection(getContext())) ) {
                    button.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), my.app1.android.newsapp.R.string.no_network, Toast.LENGTH_LONG).show();
                } else {
                    getData(URL);
                }
            }
        });
    }

    public void loadNextDataFromApi(int offset) {

        loadCounter++;
        if (loadCounter == 1) {
            getData(url2);
        }
    }

}
