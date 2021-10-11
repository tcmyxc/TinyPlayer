package com.tcmyxc.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tcmyxc.R;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener {

    private List<View> viewList = new ArrayList<>();// view容器
    private ViewPager viewPager;
    private ImageView[] dotList;// 小圆点
    private int prePosition;// 上次点亮的小圆点

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
        initViewPager();
        initDots();
    }

    private void initView() {
        LayoutInflater inflater = LayoutInflater.from(this);
        viewList.add(inflater.inflate(R.layout.guide_one_layout, null));
        viewList.add(inflater.inflate(R.layout.guide_two_layout, null));
        viewList.add(inflater.inflate(R.layout.guide_three_layout, null));
    }

    private void initViewPager() {
        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(new MyPageAdapter(viewList, this));
        viewPager.addOnPageChangeListener(this);
    }

    private void initDots() {
        LinearLayout dotsLayout = findViewById(R.id.ll_dots_layout);
        dotList = new ImageView[viewList.size()];
        for(int i=0; i<viewList.size(); i++){
            dotList[i] = (ImageView) dotsLayout.getChildAt(i);
            dotList[i].setEnabled(false);
        }
        dotList[0].setEnabled(true);// 默认第一个被选中
        prePosition = 0;
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        setCurrentDotPosition(i);
    }

    private void setCurrentDotPosition(int position) {
        dotList[position].setEnabled(true);
        dotList[prePosition].setEnabled(false);
        prePosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    class MyPageAdapter extends PagerAdapter{

        private List<View> viewList;
        private Context context;

        public MyPageAdapter() {
        }

        public MyPageAdapter(List<View> viewList, Context context) {
            this.viewList = viewList;
            this.context = context;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            if(viewList != null && viewList.size() >= 0){
                container.addView(viewList.get(position));
                if(position == viewList.size() - 1){
                    ImageView ivStart = viewList.get(position).findViewById(R.id.iv_start);
                    ivStart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startHomeActivity();
                            setGuided();
                        }
                    });
                }
                return viewList.get(position);
            }
            return null;
        }

        private void startHomeActivity() {
            startActivity(new Intent(GuideActivity.this, HomeActivity.class));
            finish();
        }

        private void setGuided() {
            getSharedPreferences("config", MODE_PRIVATE).edit().putBoolean("isFirstIn", false).commit();
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            if(viewList != null && viewList.size() >= 0){
                container.removeView(viewList.get(position));
            }
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }
    }
}