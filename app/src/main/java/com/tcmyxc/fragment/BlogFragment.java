package com.tcmyxc.fragment;

import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.tcmyxc.R;

/**
 * @author : 徐文祥
 * @date : 2021/10/2 15:43
 * @description : BlogFragment
 */
public class BlogFragment extends BaseFragment{

    private static final int MAX_VALUE = 100;
    private static final String BLOG_URL = "http://tcmyxc.cn";
    private static final String CSDN_BLOG_URL = "http://m.blog.csdn.net/tcmyxc";

    private WebView webView;
    private ProgressBar progressBar;

    private WebChromeClient webChromeClient = new WebChromeClient(){
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            // 加载过程中更新进度，如果达到最大值则隐藏
            progressBar.setProgress(newProgress);
            if(newProgress == MAX_VALUE){
                progressBar.setVisibility(View.GONE);
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_blog;
    }

    @Override
    protected void initView() {
        webView = bindViewId(R.id.web_view);
        progressBar = bindViewId(R.id.pb_progress);

        WebSettings settings = webView.getSettings();// 设置属性
        settings.setBuiltInZoomControls(true);// 支持放缩
        settings.setJavaScriptEnabled(true);

        progressBar.setMax(MAX_VALUE);

        // 请求网页数据
        webView.loadUrl(CSDN_BLOG_URL);
        webView.setWebChromeClient(webChromeClient);
    }

    @Override
    protected void initData() {

    }
}
