package com.tiga.coroutines

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_practice1.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PracticeActivity1 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_practice1)
        runCoroutines()
    }

    private fun runCoroutines() {
        GlobalScope.launch(Dispatchers.Main) {
            val data = getData()
            val processedData = processData(data)
            textView.text = processedData
        }
    }

    private suspend fun getData(): String {
        return withContext(Dispatchers.IO) {
            "hen_coder"
        }
    }

    private suspend fun processData(data: String): String {
        return withContext(Dispatchers.IO) {
            data.split("_")//把"hen_coder" 拆成 ["hen","coder"]
                .map { it.capitalize() }//把["hen","coder"]改成["Hen","Coder"]
                .reduce { acc, s -> acc + s }//把["Hen","Coder"]改成HenCoder
        }
    }
}