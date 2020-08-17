package com.rahbarbazaar.homadit.android.ui.activities

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
import com.rahbarbazaar.homadit.android.R
//import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.homadit.android.controllers.adapters.ShoppingProductsAdapter
import com.rahbarbazaar.homadit.android.controllers.interfaces.ShoppingProductsItemInteraction
import com.rahbarbazaar.homadit.android.models.shopping_product.Detail
import com.rahbarbazaar.homadit.android.models.shopping_product.ShoppingProductList
import com.rahbarbazaar.homadit.android.models.shopping_product.TotalShoppingProductData
import com.rahbarbazaar.homadit.android.network.ServiceProvider
import com.rahbarbazaar.homadit.android.utilities.CustomBaseActivity
import com.rahbarbazaar.homadit.android.utilities.DialogFactory
import com.rahbarbazaar.homadit.android.utilities.GeneralTools

import kotlinx.android.synthetic.main.activity_shopping_products.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class ShoppingProducts : CustomBaseActivity(),ShoppingProductsItemInteraction {
    private lateinit var dialogFactory: DialogFactory
    private var connectivityReceiver: BroadcastReceiver? = null
    lateinit var totalShoppingProductData: TotalShoppingProductData
    lateinit var detail: ArrayList<Detail>
    lateinit var shoppingProductList: ArrayList<ShoppingProductList>
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

        //initial Dialog factory
        dialogFactory = DialogFactory(this@ShoppingProducts)
        shopping_id = intent.getStringExtra("shopping_product_id")
        linear_exit_shopping_product.setOnClickListener {
            finish()
        }
    }

    private fun getShoppiongProductList(page: Int) {

        avi_edit_products.visibility = View.VISIBLE
        val service = ServiceProvider(this).getmService()
        val call = service.getShoppingProductList(shopping_id, page)
        call.enqueue(object : Callback<TotalShoppingProductData> {

            override fun onResponse(call: Call<TotalShoppingProductData>, response: Response<TotalShoppingProductData>) {

                avi_edit_products.visibility = View.GONE
                if (response.code() == 200) {

                    totalShoppingProductData = response.body()!!
                    setRecyclerview(totalShoppingProductData)

                } else if (response.code() == 204) {

                    if (this@ShoppingProducts.page == 0) {
                        txt_no_shopping_product.visibility = View.VISIBLE
                    }

                } else {
                    Toast.makeText(this@ShoppingProducts, resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<TotalShoppingProductData>, t: Throwable) {
                Toast.makeText(this@ShoppingProducts, resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
                avi_edit_products.visibility = View.GONE
            }
        })
    }

    private fun setRecyclerview(totalShoppingProductData: TotalShoppingProductData) {

        totalPages = totalShoppingProductData.data!!.total!!
        if (page == 0) {
            detail.clear()

            if (totalShoppingProductData.data!!.bought!!.data!!.isEmpty()) {
                txt_no_shopping_product.visibility = View.VISIBLE
            }
        }

        shoppingProductList.addAll(totalShoppingProductData.data!!.bought?.data!!)
        linearLayoutManager = LinearLayoutManager(this@ShoppingProducts, LinearLayout.VERTICAL, false)
        val rv_shopping_products: RecyclerView = findViewById(R.id.rv_shopping_products)
        rv_shopping_products.layoutManager = linearLayoutManager

        adapter = ShoppingProductsAdapter(shoppingProductList, this@ShoppingProducts)
        adapter.setListener(this)
        rv_shopping_products.adapter = adapter
        adapter.notifyDataSetChanged()

        rv_shopping_products.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                    getShoppiongProductList(page)
                    }
                }
            }
        })
    }

    override fun shoppingProductsListOnClicked(model: ShoppingProductList, position: Int) {
        dialogFactory.createShoppingProductDetailDialog(object : DialogFactory.DialogFactoryInteraction {
            override fun onAcceptButtonClicked(vararg params: String) {
            }

            override fun onDeniedButtonClicked(bool: Boolean) {
            }
        },rl_root_shopping_product,model)
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
        shoppingProductList = ArrayList<ShoppingProductList>()
        detail = ArrayList<Detail>()
        getShoppiongProductList(page)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivityReceiver)
    }
}