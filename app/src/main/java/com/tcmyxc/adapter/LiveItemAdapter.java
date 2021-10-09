package com.tcmyxc.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcmyxc.R;
import com.tcmyxc.activity.PlayActivity;

/**
 * @author : 徐文祥
 * @date : 2021/10/8 20:49
 * @description : TODO
 */
public class LiveItemAdapter extends RecyclerView.Adapter<LiveItemAdapter.ViewHolder>{

    private Context context;
    // 数据集
    private String[] dataList = new String[] {
            "CCTV-1 综合",
            "CCTV-2 财经",
            "CCTV-3 综艺",
            "CCTV-4 中文国际(亚)",
            "CCTV-5 体育",
            "CCTV-6 电影",
            "CCTV-7 军事农业",
            "CCTV-8 电视剧",
            "CCTV-9 纪录",
            "CCTV-10 科教",
            "CCTV-11 戏曲",
            "CCTV-12 社会与法",
            "CCTV-13 新闻",
            "CCTV-14 少儿",
            "CCTV-15 音乐",
            "湖南卫视",
            "浙江卫视",
    };

    private int[] iconList = new int[] {
            R.drawable.cctv_1,
            R.drawable.cctv_2,
            R.drawable.cctv_3,
            R.drawable.cctv_4,
            R.drawable.cctv_5,
            R.drawable.cctv_6,
            R.drawable.cctv_7,
            R.drawable.cctv_8,
            R.drawable.cctv_9,
            R.drawable.cctv_10,
            R.drawable.cctv_11,
            R.drawable.cctv_12,
            R.drawable.cctv_13,
            R.drawable.cctv_14,
            R.drawable.cctv_15,
            R.drawable.hunan_tv,
            R.drawable.zhejiang_tv,
    };

    // 直播源（2021年10月9号有效）
    // 来源：https://www.right.com.cn/FORUM/forum.php?mod=viewthread&tid=4976393&page=1&simpletype=no
    private String [] urlList = new String[]{
            "http://39.134.66.66/PLTV/88888888/224/3221225816/index.m3u8",// cctv1
            "http://39.134.66.66/PLTV/88888888/224/3221225820/index.m3u8",// cctv2
            "http://39.134.66.66/PLTV/88888888/224/3221225799/index.m3u8",// cctv3
            "http://39.134.66.66/PLTV/88888888/224/3221225797/index.m3u8",// cctv4
            "http://39.134.66.66/PLTV/88888888/224/3221225818/index.m3u8",// cctv5
            "http://39.134.66.66/PLTV/88888888/224/3221225814/index.m3u8",// cctv6
            "http://39.134.66.66/PLTV/88888888/224/3221225671/index.m3u8",// cctv7
            "http://39.134.66.66/PLTV/88888888/224/3221225795/index.m3u8",// cctv8
            "http://39.134.66.66/PLTV/88888888/224/3221225676/index.m3u8",// cctv9
            "http://39.134.66.66/PLTV/88888888/224/3221225677/index.m3u8",// cctv10
            "http://39.134.66.66/PLTV/88888888/224/3221225517/index.m3u8",// cctv11
            "http://39.134.66.66/PLTV/88888888/224/3221225669/index.m3u8",// cctv12
            "http://39.134.66.66/PLTV/88888888/224/3221225812/index.m3u8",// cctv13
            "http://39.134.66.66/PLTV/88888888/224/3221225674/index.m3u8",// cctv14
            "http://39.135.138.60:18890/PLTV/88888910/224/3221225641/index.m3u8",// cctv15
            "http://39.135.138.59:18890/PLTV/88888910/224/3221225704/index.m3u8",// 湖南台
            "http://39.134.66.66/PLTV/88888888/224/3221225514/index.m3u8",// 浙江台
    };

    public LiveItemAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.live_item, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int i) {
        holder.icon.setImageResource(iconList[i]);
        holder.title.setText(dataList[i]);
        holder.itemView.setTag(i);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PlayActivity.launch((Activity) context, urlList[i], dataList[i]);
            }
        });
    }


    @Override
    public int getItemCount() {
        return dataList.length;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.iv_live_icon);
            title = itemView.findViewById(R.id.tv_live_title);
        }
    }
}
