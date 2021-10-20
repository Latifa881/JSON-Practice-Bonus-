package com.example.jsonpracticebonus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class MainActivity : AppCompatActivity() {
    lateinit var etNumber: EditText
    lateinit var btGet: Button
    lateinit var tvName: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        etNumber = findViewById(R.id.etNumber)
        btGet = findViewById(R.id.btGet)
        tvName = findViewById(R.id.tvName)
        btGet.setOnClickListener {
            val number = etNumber.text.toString()
            if (number.isEmpty() || number.toInt() < 1 || number.toInt() > 14) {
                Toast.makeText(this@MainActivity, "Enter a number between 1-13", Toast.LENGTH_SHORT)
                    .show()

            } else {

                requestApi(number.toInt()-1)
            }
            etNumber.text.clear()
        }

    }

    fun requestApi( number:Int) {
        CoroutineScope(Dispatchers.IO).launch {

            val data = async {
                fetchData()
            }.await()

            if (data.isNotEmpty()) {
                getName(data,number)
            } else {
                Toast.makeText(this@MainActivity, "Something Went wrong", Toast.LENGTH_SHORT)
                    .show()

            }

        }

    }

    private fun fetchData(): String {

        var response = ""
        try {

            var URL = "https://dojo-recipes.herokuapp.com/people/"
            response = URL(URL).readText(Charsets.UTF_8)

        } catch (e: Exception) {
            println("Error $e")

        }
        return response

    }

    private suspend fun getName(data: String,number:Int) {
        withContext(Dispatchers.Main)
        {
           val array =JSONArray(data)
            tvName.setText(array.getJSONObject(number).getString("name"))
            for (i in 0 until array.length()) {
               Log.d("MY TAG", array.getJSONObject(i).getString("name"))


            }


        }

    }

}