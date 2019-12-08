package com.example.panelist.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.Toast;

import com.example.panelist.R;
import com.example.panelist.controllers.adapters.AdapterActiveList;
import com.example.panelist.controllers.adapters.AdapterPrize;
import com.example.panelist.models.activelist.ActiveList;
import com.example.panelist.models.activelist.ActiveListData;
import com.example.panelist.models.register.Prize;
import com.example.panelist.models.register.RegisterModel;
import com.example.panelist.network.Service;
import com.example.panelist.network.ServiceProvider;
import com.example.panelist.ui.activities.NewRegisterActivity;
import com.example.panelist.utilities.EndlessRecyclerOnScrollListener;
import com.example.panelist.utilities.RxBus;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {

    Button btn_register;
    AVLoadingIndicatorView avi;
    RecyclerView recyclerView;
    AdapterActiveList adapter;
    ActiveListData activeListData = new ActiveListData();


//    private EndlessRecyclerOnScrollListener scrollListener;
    LinearLayoutManager linearLayoutManager;
    Boolean isScrolling = false;

    int page =0;

    List<ActiveList> activeList = new ArrayList<>();

    private boolean loading = true;
    int currentItems, totalItems, scrollOutItems;

    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        initView(view);





        getActiveList(page);





//        setRecyclerview();

        return view;
    }

    private void initView(View view) {
        btn_register = view.findViewById(R.id.btn_register);
        avi = view.findViewById(R.id.avi);
        recyclerView = view.findViewById(R.id.recyclere_register_fragment);
        btn_register.setOnClickListener(this);
    }

    private void getActiveList(int page) {

        Service service = new ServiceProvider(getContext()).getmService();
        Call<ActiveListData> call = service.getActiveList(page);
        call.enqueue(new Callback<ActiveListData>() {
            @Override
            public void onResponse(Call<ActiveListData> call, Response<ActiveListData> response) {
                if (response.code() == 200) {

                    activeListData = response.body();

                    setRecyclerview(activeListData);
                } else {
                    int d = 5;
                }
            }

            @Override
            public void onFailure(Call<ActiveListData> call, Throwable t) {
                int a = 6;
            }
        });


    }

    private void setRecyclerview(ActiveListData activeListData) {



        linearLayoutManager = new LinearLayoutManager(getContext());
        // to show list of member items
//        List<ActiveList> activeList = new ArrayList<>();
        for (int i = 0; i < activeListData.data.size(); i++) {
            activeList.add(new ActiveList(activeListData.data.get(i).id, activeListData.data.get(i).date
                    , activeListData.data.get(i).title));
        }

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new AdapterActiveList(activeList,getContext());
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                {
                   isScrolling = true;
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if(isScrolling && (currentItems + scrollOutItems == totalItems))
                {

                    isScrolling = false;
                    page++;
                    //data fetch

                    getActiveList(page);

                }
            }
        });







//        linearLayoutManager = new LinearLayoutManager(getContext());
//        recyclerView.setLayoutManager(linearLayoutManager);
//
//        // to show list of member items
//        List<ActiveList> activeList = new ArrayList<>();
//        for (int i = 0; i < activeListData.data.size(); i++) {
//            activeList.add(new ActiveList(activeListData.data.get(i).id, activeListData.data.get(i).date
//                    , activeListData.data.get(i).title));
//        }
//
//        adapter = new AdapterActiveList(activeList, getContext());
//        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

    }


//    private void loadNextData(int page) {
//        Service service = new ServiceProvider(getContext()).getmService();
//        Call<ActiveListData> call = service.getActiveList(page);
//        call.enqueue(new Callback<ActiveListData>() {
//            @Override
//            public void onResponse(Call<ActiveListData> call, Response<ActiveListData> response) {
//                if (response.code() == 200) {
//
//                    activeListData = response.body();
//                    setRecyclerview();
//                } else {
//                    int d = 5;
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ActiveListData> call, Throwable t) {
//                int a = 6;
//            }
//        });
//    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_register:
                getNewRegisterData();
                break;
        }
    }

    private void getNewRegisterData() {

        btn_register.setVisibility(View.GONE);
        avi.setVisibility(View.VISIBLE);

        Service service = new ServiceProvider(getContext()).getmService();
        Call<RegisterModel> call = service.getRegisterData();
        call.enqueue(new Callback<RegisterModel>() {
            @Override
            public void onResponse(Call<RegisterModel> call, Response<RegisterModel> response) {
                if (response.code() == 200) {

                    RegisterModel registerModel;
                    registerModel = response.body();
                    RxBus.RegisterModel.publishRegisterModel(registerModel);
                    Objects.requireNonNull(getContext()).startActivity(new Intent(getContext(), NewRegisterActivity.class));
                    Objects.requireNonNull(getActivity()).overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    hideLoading();

                } else if (response.code() == 204) {
                    Toast.makeText(getContext(), "" + getResources().getString(R.string.error204), Toast.LENGTH_SHORT).show();
                    hideLoading();
                } else {
                    Toast.makeText(getContext(), "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    String a = response.message();
                    hideLoading();
                }
            }

            @Override
            public void onFailure(Call<RegisterModel> call, Throwable t) {
                Toast.makeText(getContext(), "" + getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
                hideLoading();
            }
        });
    }

    private void hideLoading() {
        btn_register.setVisibility(View.VISIBLE);
        avi.setVisibility(View.GONE);
    }
}
