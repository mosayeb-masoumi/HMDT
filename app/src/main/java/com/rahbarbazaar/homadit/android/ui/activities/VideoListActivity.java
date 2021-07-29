package com.rahbarbazaar.homadit.android.ui.activities;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.adapters.VideoAdapter;
import com.rahbarbazaar.homadit.android.controllers.interfaces.VideoItemInteraction;
import com.rahbarbazaar.homadit.android.models.video.VideoDetail;
import com.rahbarbazaar.homadit.android.models.video.VideoModel;
import com.rahbarbazaar.homadit.android.network.Service;
import com.rahbarbazaar.homadit.android.network.ServiceProvider;
import com.rahbarbazaar.homadit.android.utilities.CustomBaseActivity;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideoListActivity extends CustomBaseActivity implements VideoItemInteraction, View.OnClickListener {

    RecyclerView rv_video;
    VideoAdapter videoAdapter;
    LinearLayout back;
    AVLoadingIndicatorView avi;
    TextView txt_no_video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_list);

        initView();
        onClickListener();
        getData();
    }


    private void getData() {
        avi.setVisibility(View.VISIBLE);
        Service service = new ServiceProvider(this).getmService();
        Call<VideoModel> call = service.getVideos();
        call.enqueue(new Callback<VideoModel>() {
            @Override
            public void onResponse(@NotNull Call<VideoModel> call, @NotNull Response<VideoModel> response) {
                avi.setVisibility(View.GONE);
                if (response.code() == 200) {

                    VideoModel model = response.body();

                    if (model != null) {
                        setListData(model);
                        if(model.videoList.size() == 0){
                            txt_no_video.setVisibility(View.VISIBLE);
                        }
                    }

                } else if (response.code() == 204) {
                    txt_no_video.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(VideoListActivity.this, "" + R.string.serverFaield, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<VideoModel> call, @NotNull Throwable t) {
                avi.setVisibility(View.GONE);
                Toast.makeText(VideoListActivity.this, "" + R.string.connectionFaield, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setListData(VideoModel model) {

        List<VideoDetail> list = model.videoList;

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(VideoListActivity.this);
        rv_video.setLayoutManager(linearLayoutManager);
        videoAdapter = new VideoAdapter(list, VideoListActivity.this);
        rv_video.setAdapter(videoAdapter);
        videoAdapter.setListener(this);  // important to set or else the app will crashed
    }

    private void onClickListener() {
        back.setOnClickListener(this);
    }

    private void initView() {

        avi = findViewById(R.id.avi_video);
        rv_video = findViewById(R.id.rv_video);
        back = findViewById(R.id.linear_return_video);
        txt_no_video = findViewById(R.id.txt_no_video);
    }

    @Override
    public void videoItemClicked(VideoDetail model) {
        Intent intent = new Intent(VideoListActivity.this, VideoStreamActivity.class);
        intent.putExtra("url", model.videoUrl);
        startActivity(intent);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.linear_return_video) {
            finish();
        }
    }
}