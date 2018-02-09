package my.app1.android.newsapp.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by ashu on 25-11-2017.
 */

public class CategoryAdapter extends FragmentPagerAdapter {



    private String[] tab = {"Headlines", "Business", "Sports", "Entertainment","Health", "Technology"};


    public CategoryAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {


        if (position == 0) {
            return new HeadLinesFragment();

        } else if (position == 1) {
            return new BusinessFragment();

        } else if (position == 2) {
            return new SportsFragment();

        } else if (position == 3) {
            return new EntertainmentFragment();

        } else if(position == 4){
            return new HealthFragment();

        }else {
            return new TechnologyFragment();
        }

    }


    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() {
        return 6;
    }

    @Override
    public CharSequence getPageTitle(int position) { return tab[position]; }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

    }


}
