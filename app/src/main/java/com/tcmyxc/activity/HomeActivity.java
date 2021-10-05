package com.tcmyxc.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Gravity;
import android.view.MenuItem;

import com.tcmyxc.R;
import com.tcmyxc.fragment.AboutFragment;
import com.tcmyxc.fragment.BlogFragment;
import com.tcmyxc.FragmentManagerWrapper;
import com.tcmyxc.fragment.HomeFragment;

public class HomeActivity extends BaseActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private MenuItem preMenuItem;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_home;
    }

    @Override
    protected void initView() {
        setSupportActionBar();
        setActionBarIcon(R.drawable.ic_drawer_home);// 设置icon
        setTitle("首页");// 设置标题

        drawerLayout = bindViewId(R.id.drawer_layout);
        navigationView = bindViewId(R.id.navigation_view);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.syncState();
        drawerLayout.addDrawerListener(actionBarDrawerToggle);

        preMenuItem = navigationView.getMenu().getItem(0);
        preMenuItem.setChecked(true);

        // 初始化fragment
        initFragment();
        // 处理侧拉栏切换
        handleNavigationViewItem();
    }

    private void initFragment() {
        currentFragment = FragmentManagerWrapper.getInstance().createFragment(HomeFragment.class, true);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fl_main_content, currentFragment).commit();
    }

    private void handleNavigationViewItem() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(preMenuItem != null){
                    preMenuItem.setCheckable(false);
                }
                switch (menuItem.getItemId()){
                    case R.id.navigation_item_video:
                        switchFragment(HomeFragment.class);
                        toolbar.setTitle(R.string.home_title);
                        break;
                    case R.id.navigation_item_blog:
                        switchFragment(BlogFragment.class);
                        toolbar.setTitle(R.string.blog_title);
                        break;
                    case R.id.navigation_item_about:
                        switchFragment(AboutFragment.class);
                        toolbar.setTitle(R.string.about_title);
                        break;
                    default:
                        break;
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                preMenuItem = menuItem;
                preMenuItem.setChecked(true);
                return false;
            }
        });
    }

    // 通用Fragment切换
    private void switchFragment(Class<?> clazz) {
        Fragment fragment = FragmentManagerWrapper.getInstance().createFragment(clazz, true);
        if(fragment.isAdded()){
            // 如果添加成了，隐藏当前的fragment
            fragmentManager.beginTransaction()
                    .hide(currentFragment)// 隐藏当前的fragment
                    .show(fragment)// 显示添加上来的fragment
                    .commitAllowingStateLoss();
        }
        else{
            fragmentManager.beginTransaction()
                    .hide(currentFragment)
                    .add(R.id.fl_main_content, fragment)// 添加对应的fragment
                    .commitAllowingStateLoss();
        }
        currentFragment = fragment;// 重置当前fragment
    }

    @Override
    protected void initData() {

    }
}