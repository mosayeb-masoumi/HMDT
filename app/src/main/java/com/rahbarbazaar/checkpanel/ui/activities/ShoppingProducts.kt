package com.rahbarbazaar.checkpanel.ui.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AbsListView
import android.widget.LinearLayout
import android.widget.Toast
import com.rahbarbazaar.checkpanel.R
import com.rahbarbazaar.checkpanel.controllers.adapters.ShoppingProductsAdapter
import com.rahbarbazaar.checkpanel.controllers.interfaces.ShoppingProductsItemInteraction
import com.rahbarbazaar.checkpanel.models.edit_products.EditProducts
import com.rahbarbazaar.checkpanel.models.edit_products.TotalEditProductData
import com.rahbarbazaar.checkpanel.network.ServiceProvider
import com.rahbarbazaar.checkpanel.utilities.CustomBaseActivity
import com.rahbarbazaar.checkpanel.utilities.DialogFactory
import com.rahbarbazaar.checkpanel.utilities.GeneralTools
import kotlinx.android.synthetic.main.activity_edit_product.avi_edit_products
import kotlinx.android.synthetic.main.activity_edit_product.txt_no_edit_product
import kotlinx.android.synthetic.main.activity_shopping_products.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShoppingProducts : CustomBaseActivity(), ShoppingProductsItemInteraction {



    private var connectivityReceiver: BroadcastReceiver? = null
    //    lateinit var editProductsData: EditProductsData
    lateinit var totalEditProductData: TotalEditProductData
    lateinit var editProducts: ArrayList<EditProducts>
    private lateinit var adapter: ShoppingProductsAdapter

    var shopping_id:String? =""

    private var linearLayoutManager: LinearLayoutManager? = null
    private var isScrolling: Boolean = false

    var page: Int = 0
    var totalPages: Int = 0
    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shopping_products)

        //check network broadcast reciever
        val tools = GeneralTools.getInstance()
        connectivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                tools.doCheckNetwork(this@ShoppingProducts, findViewById<View>(R.id.rl_root))
            }
        }


        shopping_id = intent.getStringExtra("shopping_product_id")


        linear_exit_shopping_product.setOnClickListener {

            finish()
        }

    }

    private fun getEditList(page: Int) {

        avi_edit_products.visibility = View.VISIBLE
        val service = ServiceProvider(this@ShoppingProducts).getmService()
        val call = service.getEditProductsList(shopping_id, page)
        call.enqueue(object : Callback<TotalEditProductData> {

            override fun onResponse(call: Call<TotalEditProductData>, response: Response<TotalEditProductData>) {

                avi_edit_products.visibility = View.GONE
                if (response.code() == 200) {

                    totalEditProductData = response.body()!!
                    setRecyclerview(totalEditProductData)

                    var a =5

                } else if (response.code() == 204) {

                    if (this@ShoppingProducts.page == 0) {
                        txt_no_edit_product.visibility = View.VISIBLE
                    }

                } else {

                    Toast.makeText(this@ShoppingProducts, resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<TotalEditProductData>, t: Throwable) {
                Toast.makeText(this@ShoppingProducts, resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
                avi_edit_products.visibility = View.GONE
            }

        })
    }

    private fun setRecyclerview(totalEditProductData: TotalEditProductData) {

        totalPages = totalEditProductData.data.total
        if (page == 0) {
            editProducts.clear()

            // repeat here because of local delete
            if (totalEditProductData.data.bought.data.size == 0) {
                txt_no_edit_product.visibility = View.VISIBLE
            }
        }

        editProducts.addAll(totalEditProductData.data.bought.data)

        linearLayoutManager = LinearLayoutManager(this@ShoppingProducts, LinearLayout.VERTICAL, false)
        val rv_edit_products: RecyclerView = findViewById(R.id.rv_edit_products)
        rv_edit_products.layoutManager = linearLayoutManager

        adapter = ShoppingProductsAdapter(editProducts, this@ShoppingProducts)
        adapter.setListener(this)  // important to set or else the app will crashed (onClick)
        rv_edit_products.adapter = adapter
        adapter.notifyDataSetChanged()

        rv_edit_products.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                currentItems = linearLayoutManager!!.childCount
                totalItems = linearLayoutManager!!.itemCount
                scrollOutItems = linearLayoutManager!!.findFirstVisibleItemPosition()

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {

                    isScrolling = false
                    page++

                    if (page <= totalPages) {
                        getEditList(page)
                    }

                }
            }
        })
    }

    override fun shoppingProductsListOnClicked(model: EditProducts, position: Int) {

        val dialogFactory = DialogFactory(this)
        dialogFactory.createShoppingProductDetailDialog(object : DialogFactory.DialogFactoryInteraction {
            override fun onAcceptButtonClicked(vararg params: String) {

            }

            override fun onDeniedButtonClicked(bool: Boolean) {

            }
        }, rl_root_shopping_product,model)

    }

    override fun onResume() {
        super.onResume()
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        editProducts = ArrayList<EditProducts>()
        getEditList(page)


    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)

    }
}