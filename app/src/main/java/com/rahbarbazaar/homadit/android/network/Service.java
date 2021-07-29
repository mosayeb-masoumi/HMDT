package com.rahbarbazaar.homadit.android.network;


import com.rahbarbazaar.homadit.android.models.Lottary.LottaryModel;
import com.rahbarbazaar.homadit.android.models.Lottary.cancel.LottaryCancelModel;
import com.rahbarbazaar.homadit.android.models.Lottary.create.LottaryCreateModel;
import com.rahbarbazaar.homadit.android.models.Lottary.old_detail.OldDetail;
import com.rahbarbazaar.homadit.android.models.activelist.ActiveListData;
import com.rahbarbazaar.homadit.android.models.barcodlist.Barcode;
import com.rahbarbazaar.homadit.android.models.dashboard.dashboard_create.DashboardCreateData;
import com.rahbarbazaar.homadit.android.models.dashboard.dashboard_history.DashboardHistory;
import com.rahbarbazaar.homadit.android.models.dashboard.dashboard_home.HomeData;
import com.rahbarbazaar.homadit.android.models.dashboard.dashboard_update.DashboardUpdateData;
import com.rahbarbazaar.homadit.android.models.edit_products.DeleteProduct;
import com.rahbarbazaar.homadit.android.models.edit_products.EditProductDetailUpdateSendData;
import com.rahbarbazaar.homadit.android.models.edit_products.TotalEditProductData;
import com.rahbarbazaar.homadit.android.models.edit_products.UpdateEditProductDetailResult;
import com.rahbarbazaar.homadit.android.models.history.HistoryData;
import com.rahbarbazaar.homadit.android.models.issue.ReportIssue;
import com.rahbarbazaar.homadit.android.models.latlng.LatLng;
import com.rahbarbazaar.homadit.android.models.login.LoginModel;
import com.rahbarbazaar.homadit.android.models.message.MessageList;
import com.rahbarbazaar.homadit.android.models.message.MessageRead;
import com.rahbarbazaar.homadit.android.models.profile.ProfileChange;
import com.rahbarbazaar.homadit.android.models.profile.ProfileData;
import com.rahbarbazaar.homadit.android.models.purchased_item.PurchaseItemNewProductResult;
import com.rahbarbazaar.homadit.android.models.purchased_item.PurchaseItemResult;
import com.rahbarbazaar.homadit.android.models.purchased_item.SendPurchasedItemData;
import com.rahbarbazaar.homadit.android.models.purchased_item.SendPurchasedItemNewData;
import com.rahbarbazaar.homadit.android.models.purchased_spinners.SpinnersModel;
import com.rahbarbazaar.homadit.android.models.refresh.RefreshTokenModel;
import com.rahbarbazaar.homadit.android.models.register.GetShopId;
import com.rahbarbazaar.homadit.android.models.register.RegisterModel;
import com.rahbarbazaar.homadit.android.models.register.SendRegisterTotalData;
import com.rahbarbazaar.homadit.android.models.search_goods.GroupsData;
import com.rahbarbazaar.homadit.android.models.shopping_edit.SendUpdateTotalData;
import com.rahbarbazaar.homadit.android.models.shopping_edit.ShoppingEdit;
import com.rahbarbazaar.homadit.android.models.shopping_memberprize.MemberPrize;
import com.rahbarbazaar.homadit.android.models.shopping_product.TotalShoppingProductData;
import com.rahbarbazaar.homadit.android.models.transaction.TransactionData;
import com.rahbarbazaar.homadit.android.models.verify.VerifyModel;
import com.rahbarbazaar.homadit.android.models.video.VideoModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Service {

    @POST("Login/Login")
    Call<LoginModel> login(@Query("mobile") String mobile);

    @POST("Login/Verify")
    Call<VerifyModel> verify(@Query("code") String code,
                             @Query("pid") String pid);

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
    Call<Barcode> getBarcodeList(@Query("barcode") String barcode ,
                                 @Query("shopping_id") String shopping_id);

    @POST("Bought/Create")
    Call<PurchaseItemResult> getPurchaseItemResult(@Body SendPurchasedItemData sendPurchasedItemData);


    @POST("Issue/Create")
    Call<ReportIssue> reportIssue(@Query("body") String body);

    @POST("Message/Index")
    Call<MessageList> getMessageList(@Query("page") Integer page);

    @POST("Message/Read")
    Call<MessageRead> getMessageRead(@Query("message_id") String message_id);

    @GET("Dashboard/Home")
    Call<HomeData> getRefreshHomeData();


    @POST("Shopping/Index")
    Call<HistoryData> getHistoryList(@Query("page") Integer page);


    @POST("Bought/Index")
    Call<TotalEditProductData> getEditProductsList(@Query("shopping_id") String shopping_id ,
                                                   @Query("page") Integer page );


    @POST("Bought/Delete")
    Call<DeleteProduct> deleteEditProductItem(@Query("bought_id") String bought_id);


    @POST("Bought/Update")
    Call<UpdateEditProductDetailResult> updateEditProductDetail(@Body EditProductDetailUpdateSendData sendData);

    // rx request
    @POST("Wallet/Index")
    Call<TransactionData> getTransactionList(@Query("page") Integer page ,
                                               @Query("type") String type);

    @GET("Profile/Index")
    Call<ProfileData> getProfileList();

    @POST("Profile/Change")
    Call<ProfileChange> profileChange(@Query("body") String body);

    @POST("Bought/New")
    Call<PurchaseItemNewProductResult> getPurchaseItemNoProductResult(@Body SendPurchasedItemNewData sendPurchasedItemNewData);

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

    @GET("Search/Category")
    Call<GroupsData> getCategorySpnData();

    @POST("Search/Brand")
    Call<SpinnersModel> getSpinnersData(@Query("category_id") String category_id);

    @POST("Wallet/Convertcredittowallet")
    Call<Void> covertPapasi(@Query("amount") long amount);


    @GET("Lottery/Main")
    Call<LottaryModel> getLottaryMain();

    @POST("Lottery/Create")
    Call<LottaryCreateModel> createLottery(@Query("lottery_id") String lottery_id,
                                           @Query("amount") Integer amount);

    @POST("Lottery/Delete")
    Call<LottaryCancelModel> cancelLottery(@Query("lottery_id") String lottery_id);

    @POST("Lottery/OldDetail")
    Call<OldDetail> getOldDetail(@Query("lottery_id") String lottery_id);


    @GET("Video/Index")
    Call<VideoModel> getVideos();

}
