package com.tiga.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tiga.coroutines.entity.Repo
import com.tiga.coroutines.retrofitApi.GitHubApi
import com.tiga.coroutines.ssl.TrustAllSSLSocketFactory
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_practice2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

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
        btnRunRx.setOnClickListener {
            requestByRx()
        }
        btnRunRxAsync.setOnClickListener {
            requestByKtAsync()
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
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))//使用rxjava是加上这一句
            .build()
        api = retrofit.create(GitHubApi::class.java)
    }

    private fun requestByKt() {
        if (::retrofit.isInitialized && ::api.isInitialized) {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val repos = api.listReposKt("TonyDash")
                    textView.text = "KT${repos[0].name}"
                } catch (e: Exception) {
                    textView.text = e.message ?: "error"
                }
            }
        }
    }

    private fun requestByKtAsync(){
        if (::retrofit.isInitialized && ::api.isInitialized) {
            GlobalScope.launch(Dispatchers.Main) {
                try {
                    val async1 = async { api.listReposKt("TonyDash") }
                    val async2 = async { api.listReposKt("TonyDash") }
                    textView.text = "${async1.await()[0].name} requestByKtAsync ${async2.await()[0].name}"
                }catch (e:Exception){
                    textView.text = e.message ?: "error"
                }
            }
        }
    }

    private fun requestByRx() {
        if (::retrofit.isInitialized && ::api.isInitialized) {
            api.listReposRx("TonyDash")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : SingleObserver<List<Repo>> {
                    override fun onSuccess(t: List<Repo>) {
                        textView.text = "Rx${t[0].name}"
                    }

                    override fun onSubscribe(d: Disposable) {
                        textView.text = "onSubscribe"
                    }

                    override fun onError(e: Throwable) {
                        textView.text = e.message ?: "onError"
                    }
                })
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