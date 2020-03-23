package com.Etiflex.Splash.ROC.RetrofitInterfaces;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface askOrdenCompra {
    @FormUrlEncoded
    @POST("/BuscarOrdenCompra.php")
    void setData(
            @Field("OC") String OC,
            Callback<Response> callback
    );
}
