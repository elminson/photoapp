package com.elminson.photoapp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.*
import com.elminson.photoapp.api.PhotoRetriever
import com.elminson.photoapp.models.Photo
import com.elminson.photoapp.models.PhotoList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.StrictMode



class MainActivity : AppCompatActivity(), View.OnClickListener {
    var photos: List<Photo>? = null
    var mainAdapter: MainAdapter? = null
    lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Set the content view

        if (android.os.Build.VERSION.SDK_INT > 9) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }

        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        var retriever =  PhotoRetriever()

        recyclerView = findViewById(R.id.recyclerView) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)


        val callback = object : Callback<PhotoList> {
            override fun onFailure(call: Call<PhotoList>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<PhotoList>?, response: Response<PhotoList>?) {
                Log.e("MainActivity", "response calling API")
                response?.isSuccessful.let {
                    Log.d("result123", response?.body()?.hits.toString())
                    this@MainActivity.photos = response?.body()?.hits
                    mainAdapter = MainAdapter(this@MainActivity.photos!!, this@MainActivity)
                    recyclerView.adapter = mainAdapter
                    recyclerView.adapter?.notifyDataSetChanged()
                }
            }

        }
//        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                val linearLayoutManager = recyclerView
//                        .layoutManager as LinearLayoutManager?
//
//                if (!loading && linearLayoutManager!!.itemCount <= linearLayoutManager.findLastVisibleItemPosition() + 2) {
//                    loading = true
//                    recyclerView.addData(getServiceData())
//                }
//            }
//
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//            }
//        })

        retriever.getPhotos(callback)
    }

//     fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//
//        val rootView =  inflater?.inflate(R.layout.fragment_layout, container, false)
//        recyclerView = rootView?.findViewById(R.id.recycler_view_id)
//        // rest of my stuff
//        recyclerView?.setHasFixedSize(true)
//        recyclerView?.layoutManager = viewManager
//        recyclerView?.adapter = viewAdapter
//        // return the root view
//        return rootView
//    }

    override fun onClick(view: View?) {
        val intent = Intent(this, DetailActivity::class.java)
        val holder = view?.tag as MainAdapter.PhotoHolder
        intent.putExtra(DetailActivity.PHOTO, mainAdapter?.getPhoto(holder.adapterPosition))
        startActivity(intent)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }
}
