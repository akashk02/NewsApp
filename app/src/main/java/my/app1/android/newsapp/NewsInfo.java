package my.app1.android.newsapp;



public class NewsInfo {


    private String mTitle;
    private String mPublishedAt ;
    private String mSource ;
    private String mUrl;
    private String mUrlToImage ;
    private String mAuthor ;



    public NewsInfo(String title, String publishedAt , String source , String url , String urlToImage , String author){

        mTitle = title ;
        mPublishedAt = publishedAt ;
        mSource = source ;
        mUrl = url ;
        mAuthor = author ;

        if(urlToImage.contains("http")) {
            mUrlToImage = urlToImage;
        }else{
            mUrlToImage = "http:"+urlToImage ;
        }
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getPublishedAt() {
        return mPublishedAt;
    }

    public void setPublishedAt(String mPublishedAt) {
        this.mPublishedAt = mPublishedAt;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String mSource) {
        this.mSource = mSource;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public String getUrlToImage() {
        return mUrlToImage;
    }

    public void setUrlToImage(String mUrlToImage) {
        this.mUrlToImage = mUrlToImage;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }


}
