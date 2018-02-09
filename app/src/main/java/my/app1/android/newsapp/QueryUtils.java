package my.app1.android.newsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

import my.app1.android.newsapp.retrofit.Article;
import my.app1.android.newsapp.retrofit.PostList;


public class QueryUtils {

    public static List<NewsInfo> fetchNewsData(PostList postList) {

        ArrayList<NewsInfo> news = new ArrayList<>();
        List<Article> articles = postList.getArticles();

        for (int i = 0; i < articles.size(); i++) {

            String checkingImageString = articles.get(i).getUrlToImage();

            if (checkingImageString != null && !checkingImageString.isEmpty() && !checkingImageString.equals("null")) {

                String source = articles.get(i).getSource().getName();
                String author = articles.get(i).getAuthor();
                String title = articles.get(i).getTitle();
                String publishedAt = articles.get(i).getPublishedAt();
                String urlToImage = articles.get(i).getUrlToImage();
                String url = articles.get(i).getUrl();

                news.add(new NewsInfo(title, publishedAt, source, url, urlToImage, author));
            }
        }

        return news;

    }

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }
}
