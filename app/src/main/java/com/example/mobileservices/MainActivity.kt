package com.example.mobileservices

import Constants
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL


class MainActivity : AppCompatActivity() {

    var modelClass: ModelClass = ModelClass()
    var problemsModelClass: RepairBookingModelClass = RepairBookingModelClass()

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        if(getPreferences(MODE_PRIVATE).getBoolean(Constants.IS_USER_LOGGED_IN_CONSTANT, false))
            loadFragment(ServiceFragment())
        else
            loadFragment(LoginFragment())
        buildRetrofit()

    }

    public fun showToast(message: String) {

    }

    fun createSession(userModel: UserModel) {
        Log.d("sessioon", userModel.email_id)
        var sharedPrefernces = getPreferences(MODE_PRIVATE).edit()
        sharedPrefernces.apply {
            putString(Constants.USER_NAME_CONSTANT, userModel.first_name + userModel.last_name)
            putString(Constants.MOBILE_NUMBER_CONSTANT, userModel.mobile_number)
            putString(Constants.EMAIL_CONSTANT, userModel.email_id)
            putBoolean(Constants.IS_USER_LOGGED_IN_CONSTANT, true)
            putString(Constants.USER_ID_CONSTANT, userModel.id.toString())
        }.apply()
    }

    public fun buildRetrofit(): RetrofitInterface {


//        var gson =  GsonBuilder()
//            .setLenient()
//            .create()
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(interceptor())
        var retrofit = Retrofit.Builder().apply {
            baseUrl(URL(Constants.BASE_URL))
          client(httpClient.build())
            addConverterFactory(GsonConverterFactory.create())
        }.build()

        return retrofit.create(RetrofitInterface::class.java)

    }

    public fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.container, fragment)
            addToBackStack(null)
            commit()
        }
    }
}