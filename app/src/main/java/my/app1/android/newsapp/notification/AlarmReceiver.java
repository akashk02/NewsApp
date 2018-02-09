package my.app1.android.newsapp.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import java.util.List;

import my.app1.android.newsapp.MainActivity;
import my.app1.android.newsapp.NewsInfo;
import my.app1.android.newsapp.QueryUtils;
import my.app1.android.newsapp.R;
import my.app1.android.newsapp.retrofit.News_Api;
import my.app1.android.newsapp.retrofit.PostList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




/**
 * AlarmReceiver handles the broadcast message and generates Notification
 */
public class AlarmReceiver extends BroadcastReceiver {

    private final static String TEST_URL =
            "https://newsapi.org/v2/top-headlines?sources=fox-news&apiKey=b5612ac2de7f47fc8098085f9020d45e";

    private NotificationManager mNotificationManager;
    private String CHANNEL_ID;

    String url;
    String imageUrl;
    String contentText;
    String author;


    @Override
    public void onReceive(Context context, Intent intent) {

        mNotificationManager = NotificationHelper.getNotificationManager(context);
        CHANNEL_ID = "my_channel_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }

        getData(context);

    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {

// The user-visible name of the channel.
        CharSequence name = "name";
// The user-visible description of the channel.
        String description = "discription";
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
// Configure the notification channel.
        mChannel.setDescription(description);
        mChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
        mChannel.setLightColor(Color.RED);
        mChannel.enableVibration(true);
        mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
        mNotificationManager.createNotificationChannel(mChannel);
    }


    public NotificationCompat.Builder buildLocalNotification(Context context, PendingIntent pendingIntent) {

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, CHANNEL_ID)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                        .setContentTitle("Headlines")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(contentText))
                        .setContentText(contentText)
                        .setLights(Color.RED, 3000, 3000)
                        .setVibrate(new long[]{100, 200, 300, 400, 500})
                        .setAutoCancel(true);

        return builder;
    }


    private void getData(final Context context) {



        Call<PostList> postList = News_Api.getService().getPostList(TEST_URL);

        postList.enqueue(new Callback<PostList>() {
            @Override
            public void onResponse(Call<PostList> call, Response<PostList> response) {

                PostList list = response.body();
                List<NewsInfo> result = QueryUtils.fetchNewsData(list);

                if (result != null && !result.isEmpty()) {

                    NewsInfo newsData = result.get(0);
                    url = newsData.getUrl();
                    imageUrl = newsData.getUrlToImage();
                    contentText = newsData.getTitle();
                    author = newsData.getAuthor();


                    //Intent to invoke app when click on notification.

                    Intent intentToRepeat = new Intent(context, MainActivity.class);

                    Bundle extras = new Bundle();
                    extras.putString("url", url);
                    extras.putString("imageUrl", imageUrl);
                    extras.putBoolean("notification", true);
                    intentToRepeat.putExtras(extras);
                    intentToRepeat.setAction("NOTIFICATION");

                    //set flag to restart/relaunch the app
                    intentToRepeat.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                    //Pending intent to handle launch of Activity in intent above
                    PendingIntent pendingIntent =
                            PendingIntent.getActivity(context, NotificationHelper.ALARM_TYPE_RTC, intentToRepeat, PendingIntent.FLAG_UPDATE_CURRENT);

                    //Build notification
                    Notification repeatedNotification = buildLocalNotification(context, pendingIntent).build();

                    //Send local notification
                    mNotificationManager.notify(NotificationHelper.ALARM_TYPE_RTC, repeatedNotification);

                }

            }


            @Override
            public void onFailure(Call<PostList> call, Throwable t) {

            }
        });

    }

}
