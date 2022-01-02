package com.example.events.ui.HomeActivity;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.example.events.R;
import com.example.events.ui.HomeActivity.fragments.InvitationsFragment;
import com.example.events.ui.HomeActivity.fragments.MyEventsFragment;
import com.example.events.ui.HomeActivity.fragments.OrganisedEvents;
import com.example.events.ui.HomeActivity.fragments.ProfileFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[] {R.string.tab_text_1, R.string.tab_text_2, R.string.tab_text_3,R.string.tab_text_4};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new MyEventsFragment();
            case 1: return new OrganisedEvents();
            case 2: return new InvitationsFragment();
            default: return new ProfileFragment();
        }
    }

   @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }


    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }
}