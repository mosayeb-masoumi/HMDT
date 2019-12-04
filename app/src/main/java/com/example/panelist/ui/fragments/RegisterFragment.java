package com.example.panelist.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.example.panelist.R;
import com.example.panelist.models.register.RegisterModel;
import com.example.panelist.network.Service;
import com.example.panelist.network.ServiceProvider;
import com.example.panelist.ui.activities.NewRegisterActivity;
import com.example.panelist.utilities.RxBus;
import com.wang.avi.AVLoadingIndicatorView;
import java.util.Objects;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener{

    Button btn_register;
    AVLoadingIndicatorView avi;

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

        return view;
    }


    private void initView(View view) {
        btn_register=view.findViewById(R.id.btn_register);
        avi=view.findViewById(R.id.avi);
        btn_register.setOnClickListener(this);
    }


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
                if(response.code()==200){

                    RegisterModel registerModel;
                    registerModel = response.body();
                    RxBus.RegisterModel.publishRegisterModel(registerModel);
                    Objects.requireNonNull(getContext()).startActivity(new Intent(getContext(),NewRegisterActivity.class));
                    Objects.requireNonNull(getActivity()).overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    hideLoading();

                }else if(response.code()==204){
                    Toast.makeText(getContext(), "" + getResources().getString(R.string.error204), Toast.LENGTH_SHORT).show();
                    hideLoading();
                }else{
                    Toast.makeText(getContext(), "" + getResources().getString(R.string.serverFaield), Toast.LENGTH_SHORT).show();
                    String a =response.message();
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
