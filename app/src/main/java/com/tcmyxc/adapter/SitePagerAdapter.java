package com.tcmyxc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import com.tcmyxc.fragment.DetailListFragment;
import com.tcmyxc.model.Site;

import java.util.HashMap;

/**
 * @author : 徐文祥
 * @date : 2021/10/3 14:49
 * @description : PagerAdapter
 */
public class SitePagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private int channelId;
    private HashMap<Integer, DetailListFragment> pagerMap;

    public SitePagerAdapter(FragmentManager fm, Context context, int channelId) {
        super(fm);
        this.context = context;
        this.channelId = channelId;
        pagerMap = new HashMap<>();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        Object obj = super.instantiateItem(container, position);
        if (obj instanceof DetailListFragment) {
            pagerMap.put(position, (DetailListFragment) obj);
        }
        return obj;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
        pagerMap.remove(position);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment = DetailListFragment.newInstance(i + 1, channelId);
        return fragment;
    }

    @Override
    public int getCount() {
        return Site.MAX_SITE;
    }
}
