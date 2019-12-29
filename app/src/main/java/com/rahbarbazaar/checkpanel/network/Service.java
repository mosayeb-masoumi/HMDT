package com.rahbarbazaar.checkpanel.network;


import android.provider.ContactsContract;

import com.rahbarbazaar.checkpanel.models.activelist.ActiveListData;
import com.rahbarbazaar.checkpanel.models.barcodlist.Barcode;
import com.rahbarbazaar.checkpanel.models.dashboard.dashboard_create.DashboardCreateData;
import com.rahbarbazaar.checkpanel.models.dashboard.dashboard_home.HomeData;
import com.rahbarbazaar.checkpanel.models.dashboard.dashboard_update.DashboardUpdateData;
import com.rahbarbazaar.checkpanel.models.edit_products.DeleteProduct;
import com.rahbarbazaar.checkpanel.models.edit_products.EditProductsData;
import com.rahbarbazaar.checkpanel.models.history.HistoryData;
import com.rahbarbazaar.checkpanel.models.issue.ReportIssue;
import com.rahbarbazaar.checkpanel.models.latlng.LatLng;
import com.rahbarbazaar.checkpanel.models.login.LoginModel;
import com.rahbarbazaar.checkpanel.models.message.MessageList;
import com.rahbarbazaar.checkpanel.models.message.MessageRead;
import com.rahbarbazaar.checkpanel.models.message.MessageUnread;
import com.rahbarbazaar.checkpanel.models.purchased_item.PurchaseItemResult;
import com.rahbarbazaar.checkpanel.models.purchased_item.SendPurchasedItemData;
import com.rahbarbazaar.checkpanel.models.refresh.RefreshTokenModel;
import com.rahbarbazaar.checkpanel.models.register.GetShopId;
import com.rahbarbazaar.checkpanel.models.register.RegisterModel;
import com.rahbarbazaar.checkpanel.models.register.SendRegisterTotalData;
import com.rahbarbazaar.checkpanel.models.shopping_edit.SendUpdateTotalData;
import com.rahbarbazaar.checkpanel.models.shopping_edit.ShoppingEdit;
import com.rahbarbazaar.checkpanel.models.verify.VerifyModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {

    //    @POST("Login/Login")
//    Single<LoginModel> login(@Query("mobile") String mobile);
    @POST("Login/Login")
    Call<LoginModel> login(@Query("mobile") String mobile);


    @POST("Login/Verify")
    Call<VerifyModel> verify(@Query("code") String code);


    @GET("Dashboard/Create")
    Call<DashboardCreateData> getDashboardData();

    @GET("Dashboard/Update")
    Call<DashboardUpdateData> dashboardUpdateData();

    @POST("Login/Refresh")
    Call<RefreshTokenModel> refreshToken(@Query("access_token") String access_token,
                                         @Query("refresh_token") String refresh_token);

    @GET("Shopping/Form")
    Call<RegisterModel> getRegisterData();

    @POST("Shopping/Create")
    Call<GetShopId> registerNewShop(@Body SendRegisterTotalData sendRegisterTotalData);

    @POST("Shopping/ActiveIndex")
    Call<ActiveListData> getActiveList(@Query("page") Integer page);


    @POST("Shopping/Latlng")
    Call<LatLng> latLng(@Query("lat") String lat,
                        @Query("lng") String lng);

    @POST("Shopping/Edit")
    Call<ShoppingEdit> getShoppingEdit(@Query("shopping_id") String shopping_id);


    @POST("Shopping/Update")
    Call<GetShopId> update(@Body SendUpdateTotalData sendUpdateTotalData);


    @POST("Product/Barcode")
    Call<Barcode> getBarcodeList(@Query("barcode") String barcode);

    @POST("Bought/Create")
    Call<PurchaseItemResult> getPurchaseItemResult(@Body SendPurchasedItemData sendPurchasedItemData);


    @POST("Issue/Create")
    Call<ReportIssue> reportIssue(@Query("body") String body);

    @POST("Message/Index")
    Call<MessageList> getMessageList(@Query("page") Integer page);

    @POST("Message/Read")
    Call<MessageRead> getMessageRead(@Query("message_id") String message_id);

//    @GET("Dashboard/Home")
//    Call<HomeData> getHomeData();


    @POST("Shopping/Index")
    Call<HistoryData> getHistoryList(@Query("page") Integer page);


    @POST("Bought/Index")
    Call<EditProductsData> getEditProductsList(@Query("shopping_id") String shopping_id ,
                                               @Query("page") Integer page );


    @POST("Bought/Delete")
    Call<DeleteProduct> deleteEditProductItem(@Query("bought_id") String bought_id);
}
