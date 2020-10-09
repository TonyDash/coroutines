package com.tiga.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tiga.coroutines.entity.Repo
import com.tiga.coroutines.retrofitApi.GitHubApi
import com.tiga.coroutines.ssl.TrustAllSSLSocketFactory
import kotlinx.android.synthetic.main.activity_practice2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.TrustManagerFactory

class PracticeActivity2 : AppCompatActivity() {

    private lateinit var retrofit: Retrofit
    private lateinit var api: GitHubApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice2)
        initViews()
        initRetrofit()
    }

    private fun initViews() {
        btnRunNormal.setOnClickListener {
            requestByNormal()
        }
        btnRunKt.setOnClickListener {
            requestByKt()
        }
    }

    private fun initRetrofit() {
        val okHttpClient = OkHttpClient.Builder().sslSocketFactory(
            TrustAllSSLSocketFactory.newInstance(),
            TrustAllSSLSocketFactory.TrustAllCertsManager()
        )
        retrofit = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(okHttpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(GitHubApi::class.java)
    }

    private fun requestByKt() {
        if (::retrofit.isInitialized && ::api.isInitialized) {
            GlobalScope.launch(Dispatchers.Main) {
                val repos = api.listReposKt("TonyDash")
                textView.text ="KT${repos[0].name}"
            }
        }
    }

    private fun requestByNormal() {
        if (::retrofit.isInitialized && ::api.isInitialized) {
            api.listRepos("TonyDash")
                .enqueue(object : Callback<List<Repo>> {
                    override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                        textView.text = "requestByNormal onFailure"
                    }

                    override fun onResponse(
                        call: Call<List<Repo>>,
                        response: Response<List<Repo>>
                    ) {
                        textView.text = response.body()?.get(0)?.name
                    }

                })
        }
    }
}