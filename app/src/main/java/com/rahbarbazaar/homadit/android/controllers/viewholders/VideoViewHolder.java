package com.rahbarbazaar.homadit.android.controllers.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.interfaces.VideoItemInteraction;
import com.rahbarbazaar.homadit.android.models.video.VideoDetail;

public class VideoViewHolder extends RecyclerView.ViewHolder {

    RelativeLayout root;
    ImageView img_cover;
    TextView title;

    public VideoViewHolder(@NonNull View itemView) {
        super(itemView);
        root = itemView.findViewById(R.id.rl_video_item);
        img_cover = itemView.findViewById(R.id.img_video_cover);
        title = itemView.findViewById(R.id.video_title_row);
    }

    public void bindData(VideoDetail model) {

        title.setText(model.title);
        Glide.with(itemView.getContext()).load(model.imageUrl)
                .transition(DrawableTransitionOptions.withCrossFade()).into(img_cover);

    }

    public void setOnVideoListHolderListener(VideoItemInteraction listener, VideoDetail model) {

        root.setOnClickListener(v -> listener.videoItemClicked(model));
    }
}
