package com.example.multiplefilesupload.services;



import com.example.multiplefilesupload.Pojos;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RealStateApiServices {


    @Multipart
    @POST("api/Property/PostPropertyPhotos")
    Call<Pojos> uploadMultipleFiles(
            @Part("propertyId") RequestBody propertyId,
            @Part("details1") RequestBody details1,
            @Part("details2") RequestBody details2,
            @Part("details3") RequestBody details3,
            @Part MultipartBody.Part photo,
            @Part MultipartBody.Part photo1,
            @Part MultipartBody.Part photo2);

}
