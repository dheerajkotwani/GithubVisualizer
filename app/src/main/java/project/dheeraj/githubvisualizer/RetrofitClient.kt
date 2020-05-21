package project.dheeraj.githubvisualizer

import com.google.gson.Gson
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient{

    lateinit var retrofit: Retrofit
    final var BASE_URL = "https://api.github.com"

    fun getClient() : Retrofit
    {

            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()


        return retrofit
    }


}