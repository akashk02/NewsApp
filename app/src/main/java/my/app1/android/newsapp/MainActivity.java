package my.app1.android.newsapp;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import my.app1.android.newsapp.Fragments.BusinessFragment;
import my.app1.android.newsapp.Fragments.CategoryAdapter;
import my.app1.android.newsapp.Fragments.EntertainmentFragment;
import my.app1.android.newsapp.Fragments.HeadLinesFragment;
import my.app1.android.newsapp.Fragments.HealthFragment;
import my.app1.android.newsapp.Fragments.SportsFragment;
import my.app1.android.newsapp.Fragments.TechnologyFragment;
import my.app1.android.newsapp.notification.NotificationHelper;

import static my.app1.android.newsapp.R.id.viewpager;


public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private CategoryAdapter adapter;
    private ShareActionProvider mShareActionProvider;
    private Toolbar toolbar;
    private AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SpannableString s = new SpannableString("US");
        s.setSpan(new TypefaceSpan(this, "Lobster.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(s);

        viewPager = findViewById(viewpager);
        adapter = new CategoryAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);


        MobileAds.initialize(this, "ca-app-pub-3940256099942544/6300978111");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);

        NotificationHelper.scheduleRepeatingElapsedNotification(this);
        NotificationHelper.enableBootReceiver(this);
        handleIntent(getIntent());

        if (!QueryUtils.checkInternetConnection(this)) {
            setContentView(R.layout.no_network);
            toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);


            Button button = findViewById(R.id.try_again);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();


        ComponentName componentName = new ComponentName(MainActivity.this, SearchActivity.class);


        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(componentName));


        // Locate MenuItem with ShareActionProvider
        MenuItem item = menu.findItem(R.id.menu_item_share);

        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.refresh:

                int position;

                Fragment page = getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.viewpager + ":" + viewPager.getCurrentItem());

                if (page != null) {

                    position = viewPager.getCurrentItem();

                    switch (position) {

                        case 0:
                            HeadLinesFragment fragment0 = (HeadLinesFragment) page;
                            fragment0.refresh();
                            return true;

                        case 1:
                            BusinessFragment fragment1 = (BusinessFragment) page;
                            fragment1.refresh();
                            return true;


                        case 2:
                            SportsFragment fragment2 = (SportsFragment) page;
                            fragment2.refresh();
                            return true;


                        case 3:
                            EntertainmentFragment fragment3 = (EntertainmentFragment) page;
                            fragment3.refresh();
                            return true;


                        case 4:
                            HealthFragment fragment4 = (HealthFragment) page;
                            fragment4.refresh();
                            return true;

                        case 5:
                            TechnologyFragment fragment5 = (TechnologyFragment) page;
                            fragment5.refresh();
                            return true;
                    }
                }
                return true;


            case R.id.menu_item_share:

                shareIntent();
                return true;
        }

        return super.onOptionsItemSelected(item);

    }


    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);

    }

    private void handleIntent(Intent intent) {

        Bundle extras = intent.getExtras();

        if (extras != null && extras.getBoolean("notification")) {
            String url = extras.getString("url");
            String imageUrl = extras.getString("imageUrl");
            launchChrome(extras, url, imageUrl);
        }
    }


    public void launchChrome(Bundle extras, String url, String imageUrl) {

        Intent webIntent = new Intent(this, Web.class);
        extras.putString("url", url);
        extras.putString("imageUrl", imageUrl);
        webIntent.putExtras(extras);
        startActivity(webIntent);
    }


    private void shareIntent() {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_TEXT, R.string.play_store_link);
        shareIntent.setType("text/plain");
        startActivity(shareIntent);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

}
