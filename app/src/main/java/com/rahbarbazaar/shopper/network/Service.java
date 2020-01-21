package com.rahbarbazaar.shopper.network;


import com.rahbarbazaar.shopper.models.activelist.ActiveListData;
import com.rahbarbazaar.shopper.models.barcodlist.Barcode;
import com.rahbarbazaar.shopper.models.dashboard.dashboard_create.DashboardCreateData;
import com.rahbarbazaar.shopper.models.dashboard.dashboard_history.DashboardHistory;
import com.rahbarbazaar.shopper.models.dashboard.dashboard_update.DashboardUpdateData;
import com.rahbarbazaar.shopper.models.edit_products.DeleteProduct;
import com.rahbarbazaar.shopper.models.edit_products.EditProductDetailUpdateSendData;
import com.rahbarbazaar.shopper.models.edit_products.TotalEditProductData;
import com.rahbarbazaar.shopper.models.edit_products.UpdateEditProductDetailResult;
import com.rahbarbazaar.shopper.models.history.HistoryData;
import com.rahbarbazaar.shopper.models.issue.ReportIssue;
import com.rahbarbazaar.shopper.models.latlng.LatLng;
import com.rahbarbazaar.shopper.models.login.LoginModel;
import com.rahbarbazaar.shopper.models.message.MessageList;
import com.rahbarbazaar.shopper.models.message.MessageRead;
import com.rahbarbazaar.shopper.models.profile.MemberDetail;
import com.rahbarbazaar.shopper.models.profile.ProfileChange;
import com.rahbarbazaar.shopper.models.profile.ProfileData;
import com.rahbarbazaar.shopper.models.purchased_item.PurchaseItemNoProductResult;
import com.rahbarbazaar.shopper.models.purchased_item.PurchaseItemResult;
import com.rahbarbazaar.shopper.models.purchased_item.SendPurchasedItemData;
import com.rahbarbazaar.shopper.models.purchased_item.SendPurchasedItemNoProductData;
import com.rahbarbazaar.shopper.models.refresh.RefreshTokenModel;
import com.rahbarbazaar.shopper.models.register.GetShopId;
import com.rahbarbazaar.shopper.models.register.RegisterModel;
import com.rahbarbazaar.shopper.models.register.SendRegisterTotalData;
import com.rahbarbazaar.shopper.models.shop.ShopCenter;
import com.rahbarbazaar.shopper.models.shopping_edit.SendUpdateTotalData;
import com.rahbarbazaar.shopper.models.shopping_edit.ShoppingEdit;
import com.rahbarbazaar.shopper.models.shopping_memberprize.MemberPrize;
import com.rahbarbazaar.shopper.models.shopping_product.TotalShoppingProductData;
import com.rahbarbazaar.shopper.models.transaction.TransactionData;
import com.rahbarbazaar.shopper.models.verify.VerifyModel;

import java.util.List;

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
    Call<TotalEditProductData> getEditProductsList(@Query("shopping_id") String shopping_id ,
                                                   @Query("page") Integer page );


    @POST("Bought/Delete")
    Call<DeleteProduct> deleteEditProductItem(@Query("bought_id") String bought_id);


    @POST("Bought/Update")
    Call<UpdateEditProductDetailResult> updateEditProductDetail(@Body EditProductDetailUpdateSendData sendData);

    @POST("Wallet/Index")
    Call<TransactionData> getTransactionList(@Query("page") Integer page ,
                                             @Query("type") String type);

    @GET("Shopcenter/Index")
    Call<ShopCenter> getShopcenter();


    @GET("Profile/Index")
    Call<ProfileData> getProfileList();

    @POST("Profile/Detail")
    Call<List<MemberDetail>> getProfileMemberDetail(@Query("member_id") String member_id);


    @POST("Profile/Change")
    Call<ProfileChange> profileChange(@Query("body") String body);

    @POST("Bought/New")
    Call<PurchaseItemNoProductResult> getPurchaseItemNoProductResult(@Body SendPurchasedItemNoProductData sendPurchasedItemNoProductData);

    @GET("Shopping/MemberPrize")
    Call<MemberPrize> getMemberPrizeLists();

    @POST("Dashboard/History")
    Call<DashboardHistory> sendDeviceInfo(@Query("device_brand") String device_brand,
                                          @Query("device_model") String device_model,
                                          @Query("os_type") String os_type,
                                          @Query("os_version") String os_version,
                                          @Query("version_code") String version_code,
                                          @Query("version_name") String version_name,
                                          @Query("ip") String ip,
                                          @Query("network_type") String network_type);

    @POST("Bought/List")
    Call<TotalShoppingProductData> getShoppingProductList(@Query("shopping_id") String shopping_id,
                                                          @Query("page") Integer page);
}
