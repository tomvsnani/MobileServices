package com.example.mobileservices

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface RetrofitInterface {

    //    @FormUrlEncoded
    @POST(Constants.REGISTER_URL)
    fun register(
//        @Field("first_name") first_name: String,
//        @Field("last_name") last: String,
//        @Field("mobile_number") mobile: String,
//        @Field("email_id") email: String,
//        @Field("password") password: String
        @Body userModel: UserModel
    ): Call<ResponseModel>

    @POST(Constants.LOGIN_URL)
    fun login(@Body userModel: UserModel): Call<ResponseModel>
@FormUrlEncoded
    @POST(Constants.MOBILE_BRANDS)
    fun getBrands(@Field("userid") id:String): Call<RepairDetailsModel>

    @GET(Constants.MOBILE_MODELS)
    fun getModels(): Call<List<ModelModelClass>>

    @GET(Constants.PROBLEMS_URL)
    fun getProblems(): Call<List<ProblemsModelClass>>

    @Multipart
    @POST(Constants.BOOKING_URL)
    fun bookRepair(
        @Part("data") problemsModelClass: RepairBookingModelClass,
        @Part imageFile: Array<MultipartBody.Part>
    ): Call<RepairBookingModelClass>


}