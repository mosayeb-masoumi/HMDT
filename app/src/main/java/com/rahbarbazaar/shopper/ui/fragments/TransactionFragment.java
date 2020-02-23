package com.rahbarbazaar.shopper.ui.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.rahbarbazaar.shopper.R;
import com.rahbarbazaar.shopper.controllers.adapters.TransactionAdapter;
import com.rahbarbazaar.shopper.controllers.interfaces.TransactionItemInteraction;
import com.rahbarbazaar.shopper.models.transaction.Transaction;
import com.rahbarbazaar.shopper.models.transaction.TransactionData;
import com.rahbarbazaar.shopper.network.Service;
import com.rahbarbazaar.shopper.network.ServiceProvider;
import com.rahbarbazaar.shopper.utilities.DialogFactory;
import com.wang.avi.AVLoadingIndicatorView;
import java.util.ArrayList;
import java.util.List;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionFragment extends Fragment implements TransactionItemInteraction, View.OnClickListener {

    RecyclerView rv_transaction;
    TextView txt_no_transaction, txt_btnpapasi, txt_btntoman;
    AVLoadingIndicatorView avi;
    RelativeLayout rl_root;
    int page = 0;
    Boolean isScrolling = false;
    int totalPage = 0;
    int currentItems, totalItems, scrollOutItems;
    TransactionAdapter adapter;

    TransactionData transactionData;
    LinearLayoutManager linearLayoutManager;
    List<Transaction> transactions;

    ImageView img_line;
    String type = "";

    public TransactionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction, container, false);

        initView(view);

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

        txt_btnpapasi.setOnClickListener(this);
        txt_btntoman.setOnClickListener(this);
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
                    setRecyclerView(transactionData, type, response.code());

                } else if (response.code() == 204) {
                    avi.setVisibility(View.GONE);
                    if (page == 0) {
                        txt_no_transaction.setVisibility(View.VISIBLE);
                        setRecyclerView(transactionData, type, response.code());
                    } else {
                        txt_no_transaction.setVisibility(View.GONE);
                    }

                } else {
                    avi.setVisibility(View.GONE);
                    Toast.makeText(getContext(), getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TransactionData> call, Throwable t) {
                avi.setVisibility(View.GONE);
                Toast.makeText(getContext(), getResources().getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setRecyclerView(TransactionData transactionData, String type, int code) {

            totalPage = transactionData.total;
            if (page == 0) {
                transactions.clear();
            }

            linearLayoutManager = new LinearLayoutManager(getContext());

            transactions.addAll(transactionData.data);

            rv_transaction.setLayoutManager(linearLayoutManager);
            adapter = new TransactionAdapter(transactions, getContext(), type);

            if(code==204 && page==0){
                rv_transaction.setAdapter(null);
            }else{
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
                        if (page <= totalPage) {
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
                type = "amount";
                getTransactionList(page, type);
                break;

            case R.id.txt_btnpapasi:
                txt_btnpapasi.setBackground(getResources().getDrawable(R.drawable.bg_transaction_papasi_select));
                txt_btnpapasi.setTextColor(getResources().getColor(R.color.white));
                txt_btntoman.setBackground(getResources().getDrawable(R.drawable.bg_transaction_toman_unselect));
                txt_btntoman.setTextColor(getResources().getColor(R.color.colorText));
                img_line.setBackgroundColor(getResources().getColor(R.color.pink_dark));
                page = 0;
                type = "papasi";
                getTransactionList(page, type);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        transactions = new ArrayList<>();
        type = "amount";
        getTransactionList(page, type);
    }

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
            }, rl_root , model,position);
    }
}
