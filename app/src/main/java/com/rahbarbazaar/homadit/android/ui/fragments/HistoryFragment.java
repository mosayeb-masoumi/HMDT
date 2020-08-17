package com.rahbarbazaar.homadit.android.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.homadit.android.R;
import com.rahbarbazaar.homadit.android.controllers.adapters.HistoryAdapter;
import com.rahbarbazaar.homadit.android.controllers.interfaces.HistoryItemInteraction;
import com.rahbarbazaar.homadit.android.models.history.History;
import com.rahbarbazaar.homadit.android.models.history.HistoryData;
import com.rahbarbazaar.homadit.android.network.Service;
import com.rahbarbazaar.homadit.android.network.ServiceProvider;
import com.rahbarbazaar.homadit.android.ui.activities.ShoppingProducts;
import com.rahbarbazaar.homadit.android.utilities.DialogFactory;
import com.rahbarbazaar.homadit.android.utilities.RxBus;
import com.wang.avi.AVLoadingIndicatorView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
//public class RegisterFragment extends Fragment implements View.OnClickListener , ActiveListItemInteraction {
public class HistoryFragment extends Fragment implements HistoryItemInteraction {


    Disposable disposable = new CompositeDisposable();
    HistoryData historyData;
    ArrayList<History> history ;
    HistoryAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    RecyclerView rv_history;
    TextView txt_no_message_history;
    AVLoadingIndicatorView avi_history;
    RelativeLayout history_root;
    SwipeRefreshLayout swipeRefresh;

    Boolean isScrolling = false;
    int page = 0;
    int totalPages = 0;
    int currentItems= 0;
    int totalItems= 0;
    int scrollOutItems = 0;


    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disposable = RxBus.HistoryList0.subscribeHistoryList0(result -> {
            if (result instanceof HistoryData) {
                historyData = (HistoryData) result;
            }
        });

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        initView(view);

        history = new ArrayList<>();
        if(historyData.getData() == null || historyData.getData().size() == 0){
            txt_no_message_history.setVisibility(View.VISIBLE);
            avi_history.setVisibility(View.GONE);
        }else{
            txt_no_message_history.setVisibility(View.GONE);
            setRecyclerView(historyData);
        }

        swipeRefresh.setOnRefreshListener(() -> {
            page=0;
            getHistoryList(page);
        });

        return view;
    }

    private void initView(View view) {
        rv_history = view.findViewById(R.id.rv_history);
        avi_history =view.findViewById(R.id.avi_history);
        txt_no_message_history = view.findViewById(R.id.txt_no_message_history);
        history_root = view.findViewById(R.id.history_root);
        swipeRefresh = view.findViewById(R.id.history_swipe_refresh);

//        history = new ArrayList<>();
//        if(historyData.getData().size() == 0){
//            txt_no_message_history.setVisibility(View.VISIBLE);
//        }else{
//            txt_no_message_history.setVisibility(View.GONE);
//            setRecyclerView(historyData);
//        }

    }


    private void getHistoryList(int page) {
        if(page>0){    // to prevent showing swipe loading and avi loading in the same time
            avi_history.setVisibility(View.VISIBLE);
        }

        Service service = new ServiceProvider(getContext()).getmService();
        Call<HistoryData> call = service.getHistoryList(page);
        call.enqueue(new Callback<HistoryData>() {
            @Override
            public void onResponse(Call<HistoryData> call, Response<HistoryData> response) {
                avi_history.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
                if(response.code()==200){

                    txt_no_message_history.setVisibility(View.GONE);
                    historyData = response.body();
                    setRecyclerView(historyData);
                    if(page==0){  // to show the latest list0
                        RxBus.HistoryList0.publishHistoryList0(historyData);
                    }

                }else if(response.code()==204){
                    if(page==0){
                        txt_no_message_history.setVisibility(View.VISIBLE);
                    }else{
                        txt_no_message_history.setVisibility(View.GONE);
                    }

                }else{
                    Toast.makeText(getContext(), ""+getContext().getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HistoryData> call, Throwable t) {
                avi_history.setVisibility(View.GONE);
                Toast.makeText(getContext(), ""+getContext().getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecyclerView(HistoryData historyData) {

        avi_history.setVisibility(View.GONE);

        totalPages = historyData.getTotal();
        if (page == 0) {
            history.clear();
        }

            history.addAll(historyData.getData());



        linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayout.VERTICAL,false);
        rv_history.setLayoutManager(linearLayoutManager);

        adapter = new HistoryAdapter(history,getContext());
        adapter.setListener(this);
        rv_history.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        rv_history.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = linearLayoutManager.getChildCount();
                totalItems = linearLayoutManager.getItemCount();
                scrollOutItems = linearLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling &&(currentItems + scrollOutItems == totalItems) ) {

                    isScrolling = false;
                    page++;
                    if(page<=(totalPages-1)){
                        getHistoryList(page);
                    }
                }
            }
        });
    }

    @Override
    public void historyListOnClicked(@NotNull History model, @NotNull String btn_title) {

        if (btn_title == "item_detail"){
            DialogFactory dialogFactory = new DialogFactory(getContext());
            dialogFactory.createHistoryDetailDialog(new DialogFactory.DialogFactoryInteraction() {
                @Override
                public void onAcceptButtonClicked(String... strings) {
                }

                @Override
                public void onDeniedButtonClicked(boolean cancel_dialog) {
                }
            },history_root,model);



        }else if(btn_title == "btn_shop_item"){

//            val intent = Intent(this@HistoryActivity,ShoppingProducts::class.java)
            Intent intent = new Intent(getContext(), ShoppingProducts.class);
//            intent.putExtra("shopping_product_id", model.id);
            intent.putExtra("shopping_product_id", model.getId());
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }


//    @Override
//    public void onResume() {
//        super.onResume();
////        history = new ArrayList<>();
////        page=0;
////        getHistoryList(page);
//    }



    @Override
    public void onStop() {
        super.onStop();
        disposable.dispose();
    }


}
