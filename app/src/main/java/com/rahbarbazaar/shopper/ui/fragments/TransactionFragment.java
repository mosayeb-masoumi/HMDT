package com.rahbarbazaar.shopper.ui.fragments;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.controllers.adapters.TransactionAdapter;
import com.rahbarbazaar.shopper.controllers.interfaces.TransactionItemInteraction;
import com.rahbarbazaar.shopper.models.dashboard.dashboard_create.DashboardCreateData;
import com.rahbarbazaar.shopper.models.dashboard.dashboard_home.HomeData;
import com.rahbarbazaar.shopper.models.history.HistoryData;
import com.rahbarbazaar.shopper.models.transaction.Transaction;
import com.rahbarbazaar.shopper.models.transaction.TransactionData;
import com.rahbarbazaar.shopper.network.Service;
import com.rahbarbazaar.shopper.network.ServiceProvider;
import com.rahbarbazaar.shopper.utilities.DialogFactory;
import com.rahbarbazaar.shopper.utilities.RxBus;
import com.wang.avi.AVLoadingIndicatorView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment implements TransactionItemInteraction, View.OnClickListener {

    DashboardCreateData dashboardCreateData;
    Disposable disposable = new CompositeDisposable();
    RecyclerView rv_transaction;
    TextView txt_no_transaction, txt_btnpapasi, txt_btntoman ,txt_papasi_transaction,txt_toman_transaction;
    AVLoadingIndicatorView avi;
    LinearLayout rl_root ,ll_convert_papasi_to_rial ;
    int page = 0;
    Boolean isScrolling = false;
    int totalPage = 0;
    int currentItems, totalItems, scrollOutItems;
    TransactionAdapter adapter;

    TransactionData transactionData;
    LinearLayoutManager linearLayoutManager;
    List<Transaction> transactions;

    SwipeRefreshLayout swipeRefresh;
    //    boolean swipe = false;
    ImageView img_line;
//    String type = "amount";
    String type = "wallet";


    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        disposable = RxBus.TransactionAmountList0.subscribeTransactionAmountList0(result -> {
            if (result instanceof TransactionData) {
                transactionData = (TransactionData) result;
            }
        });

        disposable = RxBus.DashboardModel.subscribeDashboardModel(result -> {
            if (result instanceof DashboardCreateData) {
                dashboardCreateData = (DashboardCreateData) result;
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);
        initView(view);

        transactions = new ArrayList<>();
        if (transactionData.data==null || transactionData.data.size() == 0) {
            txt_no_transaction.setVisibility(View.VISIBLE);
        } else {
            txt_no_transaction.setVisibility(View.GONE);
//            setRecyclerView(transactionData, "amount", 200);
            setRecyclerView(transactionData, "wallet", 200);
        }

        swipeRefresh.setOnRefreshListener(() -> {
            page = 0;
//            swipe = true;
            getTransactionList(page, type);

        });


        txt_toman_transaction.setText(dashboardCreateData.data.one);
        txt_papasi_transaction.setText(dashboardCreateData.data.two);

        return view;
    }

    private void initView(View view) {
        rv_transaction = view.findViewById(R.id.recyclere_transaction_fragment);
        txt_no_transaction = view.findViewById(R.id.txt_no_transaction);
        txt_btnpapasi = view.findViewById(R.id.txt_btnpapasi);
        txt_btntoman = view.findViewById(R.id.txt_btntoman);
        img_line = view.findViewById(R.id.img_line);
        avi = view.findViewById(R.id.avi_loading_fr_transaction);
        rl_root = view.findViewById(R.id.rl_fr_transaction);
        ll_convert_papasi_to_rial = view.findViewById(R.id.ll_convert_papasi_to_rial);
        swipeRefresh = view.findViewById(R.id.swp_refresh_transaction);
        txt_papasi_transaction = view.findViewById(R.id.txt_papasi_transaction);
        txt_toman_transaction = view.findViewById(R.id.txt_toman_transaction);
        txt_btnpapasi.setOnClickListener(this);
        txt_btntoman.setOnClickListener(this);
        ll_convert_papasi_to_rial.setOnClickListener(this);
    }

    private void getTransactionList(int page, String type) {

        avi.setVisibility(View.VISIBLE);

        Service service = new ServiceProvider(getContext()).getmService();
        Call<TransactionData> call = service.getTransactionList(this.page, type);
        call.enqueue(new Callback<TransactionData>() {
            @Override
            public void onResponse(Call<TransactionData> call, Response<TransactionData> response) {
                if (response.code() == 200) {

                    txt_no_transaction.setVisibility(View.GONE);
                    transactionData = response.body();
                    avi.setVisibility(View.GONE);
                    swipeRefresh.setRefreshing(false);
//                    swipe=false;
                    setRecyclerView(transactionData, type, response.code());

                    if(page==0 && type.equals("wallet")){  // to show the latest amount list0
                        RxBus.TransactionAmountList0.publishTransactionAmountList0(response.body());
                    }

                } else if (response.code() == 204) {
                    avi.setVisibility(View.GONE);
                    swipeRefresh.setRefreshing(false);
//                    swipe=false;
                    if (page == 0) {
                        txt_no_transaction.setVisibility(View.VISIBLE);
//                        setRecyclerView(transactionData, type, response.code());
                    } else {
                        txt_no_transaction.setVisibility(View.GONE);
                    }

                } else {
                    avi.setVisibility(View.GONE);
                    swipeRefresh.setRefreshing(false);
//                    swipe = false;
                    Toast.makeText(getContext(), getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TransactionData> call, Throwable t) {
                avi.setVisibility(View.GONE);
                swipeRefresh.setRefreshing(false);
//                swipe = false;
                Toast.makeText(getContext(), getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecyclerView(TransactionData transactionData, String type, int code) {

        totalPage = transactionData.total;
        if (page == 0) {
            transactions.clear();
        }

        transactions.addAll(transactionData.data);

        linearLayoutManager = new LinearLayoutManager(getContext());
        rv_transaction.setLayoutManager(linearLayoutManager);
        adapter = new TransactionAdapter(transactions, getContext(), type);

        if (code == 204 && page == 0) {
            rv_transaction.setAdapter(null);
        } else {
            rv_transaction.setAdapter(adapter);
        }

        adapter.setListener(this);  // important to set or else the app will crashed
        adapter.notifyDataSetChanged();

        rv_transaction.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {

                    isScrolling = false;
                    page++;
                    if (page <= (totalPage - 1)) {
                        //data fetch
                        getTransactionList(page, type);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_btntoman:
                txt_btntoman.setBackground(getResources().getDrawable(R.drawable.bg_transaction_toman_select));
                txt_btntoman.setTextColor(getResources().getColor(R.color.white));
                txt_btnpapasi.setBackground(getResources().getDrawable(R.drawable.bg_transaction_papasi_unselect));
                txt_btnpapasi.setTextColor(getResources().getColor(R.color.colorText));
                img_line.setBackgroundColor(getResources().getColor(R.color.blue_dark));
                page = 0;
//                type = "amount";
                type = "wallet";
                getTransactionList(page, type);
                break;

            case R.id.txt_btnpapasi:
                txt_btnpapasi.setBackground(getResources().getDrawable(R.drawable.bg_transaction_papasi_select));
                txt_btnpapasi.setTextColor(getResources().getColor(R.color.white));
                txt_btntoman.setBackground(getResources().getDrawable(R.drawable.bg_transaction_toman_unselect));
                txt_btntoman.setTextColor(getResources().getColor(R.color.colorText));
                img_line.setBackgroundColor(getResources().getColor(R.color.pink_dark));
                page = 0;
//                type = "papasi";
                type = "credit";
                getTransactionList(page, type);
                break;

            case R.id.ll_convert_papasi_to_rial:

                convertPapasiTomanDoalog();

                break;
        }
    }

    private void convertPapasiTomanDoalog() {
        DialogFactory dialogFactory = new DialogFactory(getContext());
        dialogFactory.createConvertPapasiTomanDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {

                String strToman = params[0];
                String strPapasi = params[1];

                txt_toman_transaction.setText(strToman);
                txt_papasi_transaction.setText(strPapasi);
            }

            @Override
            public void onDeniedButtonClicked(boolean cancel_dialog) {

            }
        }, rl_root);
    }

//    @Override
//    public void onResume() {
//        super.onResume();
////        transactions = new ArrayList<>();
////        type = "amount";
////        getTransactionList(page, type);
//    }

    @Override
    public void transactionItemOnClicked(Transaction model, int position) {

        DialogFactory dialogFactory = new DialogFactory(getContext());
        dialogFactory.createTransactionItemDialog(new DialogFactory.DialogFactoryInteraction() {
            @Override
            public void onAcceptButtonClicked(String... params) {


            }

            @Override
            public void onDeniedButtonClicked(boolean bool) {

            }
        }, rl_root, model, position);
    }


//    private void getRefreshHomeData() {
//        Service service = new ServiceProvider(getContext()).getmService();
//        Call<HomeData> call = service.getRefreshHomeData();
//        call.enqueue(new Callback<HomeData>() {
//            @Override
//            public void onResponse(Call<HomeData> call, Response<HomeData> response) {
//
//                if (response.code() == 200) {
//
//
//                    txt_toman_transaction.setText(response.body().data.one);
//                    txt_papasi_transaction.setText(response.body().data.two);
//
//                } else {
//                    Toast.makeText(getContext(), "" + getContext().getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<HomeData> call, Throwable t) {
//                Toast.makeText(getContext(), "" + getContext().getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
//        EventBus.getDefault().post(dashboardCreateData);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRefreshAuctions(DashboardCreateData dashboardCreateData){
        Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop() {
        super.onStop();
        disposable.dispose();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
//        getRefreshHomeData();
    }
}
