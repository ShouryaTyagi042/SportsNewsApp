package com.example.newsapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request

import com.android.volley.toolbox.JsonObjectRequest

class MainActivity : AppCompatActivity(), NewsItemClicked {
    private lateinit var mAdapter: NewsListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerview : RecyclerView = findViewById(R.id.RecyclerView)
        recyclerview.layoutManager = LinearLayoutManager(this)
        fetchData()
        mAdapter = NewsListAdapter(this)
        recyclerview.adapter = mAdapter



    }
    private fun fetchData()  {
        val url = "https://saurav.tech/NewsAPI/top-headlines/category/sports/in.json"
        val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
            { val newJsonArray = it.getJSONArray("articles")
                val newsArray = ArrayList<News>()
                for(i in 0 until newJsonArray.length()) {
                    val newJsonObject  = newJsonArray.getJSONObject(i)
                    val news = News(
                        newJsonObject.getString("title"),
                        newJsonObject.getString("author"),
                        newJsonObject.getString("url"),
                        newJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)

                }
                mAdapter.updateNews(newsArray)

            },
            {
            }
        )


// Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)



    }


    override fun onItemClicked(item: News) {
        val builder  =  CustomTabsIntent.Builder();
        val customTabsIntent = builder.build();
        customTabsIntent.launchUrl(this, Uri.parse(item.url));

    }

}