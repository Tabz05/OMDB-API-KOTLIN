package com.tabish.omdbapikotlin

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.tabish.omdbapikotlin.R
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MovieList : AppCompatActivity() {

    lateinit var client : OkHttpClient
    lateinit var getReq : Request
    lateinit var searchBar : EditText
    lateinit var submit : TextView
    lateinit var mainLayout : LinearLayout

    fun searchMovie(view : View){

        if(searchBar.text.toString().isNotEmpty())
        {
            mainLayout.removeAllViews();

            var enteredMovie : String = searchBar.text.toString()
            enteredMovie = enteredMovie.lowercase()

            enteredMovie = enteredMovie.replace('-','+')

            getReq = Request.Builder()
                //.url("https://www.omdbapi.com/?s=3+idiots&apikey=64f184f0")
                .url("https://www.omdbapi.com/?s="+enteredMovie+"&apikey=64f184f0")
                .get()
                .build()

            client.newCall(getReq).enqueue(object : Callback {

                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
                        if (!response.isSuccessful) {
                            throw IOException("Unexpected code $response")
                        }
                        else
                        {
                            val myJsonObject : JSONObject = JSONObject(response.body!!.string())
                            val ja_data : JSONArray = myJsonObject.getJSONArray("Search")
                            val length : Int = ja_data.length()

                            runOnUiThread {
                                try {
                                    var i : Int = 0
                                    while (i < length) {

                                        val jObj : JSONObject = ja_data.getJSONObject(i)
                                        val name : String = jObj.getString("Title")
                                        val year : String = jObj.getString("Year")
                                        val imdbID : String = jObj.getString("imdbID")

                                        val fin : String = "Name: $name\n\nYear: $year\n\nimdbID: $imdbID"

                                        val cardview : CardView = CardView(applicationContext)

                                        val cardlayoutparams = LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                        )

                                        cardlayoutparams.setMargins(40, 20, 40, 20)
                                        cardview.layoutParams = cardlayoutparams
                                        cardview.radius = 35f
                                        cardview.setPadding(25, 25, 25, 25)
                                        cardview.setCardBackgroundColor(Color.WHITE)

                                        val textlayoutparams = LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT
                                        )
                                        textlayoutparams.setMargins(20, 20, 20, 20)

                                        val textfin : TextView = TextView(applicationContext)
                                        textfin.text =fin
                                        textfin.setTextColor(Color.BLACK)
                                        textfin.layoutParams = textlayoutparams

                                        cardview.addView(textfin)

                                        val uri : String = jObj.getString("Poster")

                                        val img : ImageView = ImageView(applicationContext)

                                        val imglayoutparams = LinearLayout.LayoutParams(
                                            800,
                                            800
                                        )

                                        imglayoutparams.setMargins(20, 20, 20, 20)

                                        img.layoutParams = imglayoutparams
                                        img.scaleType = ImageView.ScaleType.FIT_XY

                                        Glide.with(applicationContext)
                                            .load(uri)
                                            .placeholder(R.drawable.noimage)
                                            .into(img)

                                        mainLayout.addView(cardview)
                                        mainLayout.addView(img)
                                        i += 1
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(applicationContext, "Error: $e", Toast.LENGTH_LONG)
                                        .show()
                                }
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        response.body!!.close()
                    }
                }
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_list)

        client  = OkHttpClient()
        searchBar = findViewById(R.id.searchBar)
        submit = findViewById(R.id.submit)
        mainLayout = findViewById(R.id.mainLayout)
    }
}