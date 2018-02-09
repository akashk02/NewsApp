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

import my.app1.android.newsapp.MainActivity;
import my.app1.android.newsapp.NewsInfo;
import my.app1.android.newsapp.QueryUtils;
import my.app1.android.newsapp.R;
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
public class TechnologyFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {


    public TechnologyFragment() {
    }

    private List<NewsInfo> newsList ;
    private RecyclerView recyclerView;
    private NewsApapter mAdapter;
    private ProgressBar progressBar ;
    private SwipeRefreshLayout swipeView ;
    private EndlessRecyclerViewScrollListener scrollListener;
    private int loadCounter ;
    private Button button ;

    private final static String url1 = "https://newsapi.org/v2/top-headlines?country=us&category=technology&apiKey=8afa3cabace04ca68affe22f84262845";
    private final static String url2 = "https://newsapi.org/v2/everything?sources=techcrunch&apiKey=6f516c7ca38545bead5c93bd6e797939";



    public void onRefresh(){
        mAdapter.clear();
        loadCounter = 0 ;
        button.setVisibility(View.GONE);
        getData(url1);
    }

    public void refresh() {
        mAdapter.clear();
        progressBar.setVisibility(View.VISIBLE);
        loadCounter = 0 ;
        button.setVisibility(View.GONE);
        getData(url1);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.news_list, container, false);

        button = rootView.findViewById(R.id.try_again);
        button.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRefresh();
            }
        });

        loadCounter = 0 ;

        progressBar = rootView.findViewById(R.id.indeterminateBar);

        recyclerView = rootView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        newsList = new ArrayList<>();
        mAdapter = new NewsApapter(newsList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager((MainActivity)getActivity());
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


        swipeView = rootView.findViewById(R.id.swiperefresh);
        swipeView.setOnRefreshListener(this);

        getData(url1);

        return rootView;

    }


    public void getData(final String URL){

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

                if( !(QueryUtils.checkInternetConnection(getContext())) ){
                    button.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(),R.string.no_network,Toast.LENGTH_LONG).show();
                } else {
                    getData(URL);
                }
            }
        });
    }

    public void loadNextDataFromApi(int offset) {

        loadCounter++;
        if(loadCounter==1){
            getData(url2);
        }
    }


}
