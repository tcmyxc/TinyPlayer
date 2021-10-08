package com.tcmyxc.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tcmyxc.R;

/**
 * @author : 徐文祥
 * @date : 2021/10/8 20:49
 * @description : TODO
 */
public class LiveItemAdapter extends RecyclerView.Adapter<LiveItemAdapter.ViewHolder>{

    private Context context;

    public LiveItemAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public LiveItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull LiveItemAdapter.ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView icon;
        public TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
//            icon = itemView.findViewById(R.id.iv_live_icon);
//            title = itemView.findViewById(R.id.tv_live_title);
        }
    }
}
