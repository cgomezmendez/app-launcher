package me.cristiangomez.launcher.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


interface ContactService {
    @FormUrlEncoded
    @POST("contact_us_action.php")
    fun submitForm(@Field("company") company: String, @Field("contact") contact: String,
                   @Field("telephone") phone: String,
                   @Field("email") email: String,
                   @Field("comment") comment: String,
                   @Field("services[]") services: List<String>,
                   @Field("captcha") captcha: String = "10039"): Call<ResponseBody>
}