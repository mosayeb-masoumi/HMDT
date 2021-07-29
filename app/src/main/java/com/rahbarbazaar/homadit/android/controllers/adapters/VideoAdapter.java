package com.rahbarbazaar.homadit.android.controllers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.interfaces.VideoItemInteraction;
import com.rahbarbazaar.homadit.android.controllers.viewholders.VideoViewHolder;
import com.rahbarbazaar.homadit.android.models.video.VideoDetail;

import java.util.List;

public class VideoAdapter extends RecyclerView.Adapter<VideoViewHolder> {

    List<VideoDetail> list;
    Context context;

    public VideoAdapter(List<VideoDetail> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_video, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder holder, int position) {
        VideoDetail model = list.get(position);
        holder.bindData(model);
        holder.setOnVideoListHolderListener(listener, model);
    }

    private VideoItemInteraction listener = null;

    public void setListener(VideoItemInteraction listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
