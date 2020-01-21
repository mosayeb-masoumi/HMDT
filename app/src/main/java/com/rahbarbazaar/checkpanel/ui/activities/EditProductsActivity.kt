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
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import com.rahbarbazaar.checkpanel.R
import com.rahbarbazaar.checkpanel.controllers.adapters.EditProductsAdapter
import com.rahbarbazaar.checkpanel.controllers.interfaces.EditProductsItemInteraction
import com.rahbarbazaar.checkpanel.models.edit_products.DeleteProduct
import com.rahbarbazaar.checkpanel.models.edit_products.EditProducts
import com.rahbarbazaar.checkpanel.models.edit_products.TotalEditProductData

import com.rahbarbazaar.checkpanel.network.ServiceProvider
import com.rahbarbazaar.checkpanel.utilities.CustomBaseActivity
import com.rahbarbazaar.checkpanel.utilities.GeneralTools
import com.rahbarbazaar.checkpanel.utilities.RxBus
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.android.synthetic.main.activity_edit_product.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EditProductsActivity : CustomBaseActivity(), EditProductsItemInteraction {


    private var connectivityReceiver: BroadcastReceiver? = null

    var shopping_id: String = ""

    //    lateinit var editProductsData: EditProductsData
    lateinit var totalEditProductData: TotalEditProductData
    lateinit var editProducts: ArrayList<EditProducts>
    private lateinit var adapter: EditProductsAdapter

    private var linearLayoutManager: LinearLayoutManager? = null
    private var isScrolling: Boolean = false

    var page: Int = 0
    var totalPages: Int = 0
    var currentItems: Int = 0
    var totalItems: Int = 0
    var scrollOutItems: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)

        //check network broadcast reciever
        val tools = GeneralTools.getInstance()
        connectivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                tools.doCheckNetwork(this@EditProductsActivity, findViewById<View>(R.id.rl_root_barcodelist))
            }
        }

        shopping_id = getIntent().getStringExtra("shopping_id")

        linear_exit_edit_product.setOnClickListener {
            finish()
        }

    }

    private fun getEditList(page: Int) {


        avi_edit_products.visibility = View.VISIBLE
        val service = ServiceProvider(this@EditProductsActivity).getmService()
        val call = service.getEditProductsList(shopping_id, page)
        call.enqueue(object : Callback<TotalEditProductData> {

            override fun onResponse(call: Call<TotalEditProductData>, response: Response<TotalEditProductData>) {

                avi_edit_products.visibility = View.GONE
                if (response.code() == 200) {

                    totalEditProductData = response.body()!!
                    setRecyclerview(totalEditProductData)


                } else if (response.code() == 204) {

                    if (this@EditProductsActivity.page == 0) {
                        txt_no_edit_product.visibility = View.VISIBLE
                    }

                } else {

                    Toast.makeText(this@EditProductsActivity, resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<TotalEditProductData>, t: Throwable) {
                Toast.makeText(this@EditProductsActivity, resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
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

        linearLayoutManager = LinearLayoutManager(this@EditProductsActivity, LinearLayout.VERTICAL, false)
        val rv_edit_products: RecyclerView = findViewById(R.id.rv_edit_products)
        rv_edit_products.layoutManager = linearLayoutManager

        adapter = EditProductsAdapter(editProducts, this@EditProductsActivity)
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

                    if (page <= totalPages-1) {
                        getEditList(page)
                    }

                }
            }
        })
    }


    override fun editProductsListOnClicked(model: EditProducts, position: Int, status: String,
                                           avi: AVLoadingIndicatorView, btn_delete: Button) {
        if (status == "delete") {

            avi.visibility = View.VISIBLE
            btn_delete.visibility = View.GONE
            sendDeleteItemId(model.id, avi, btn_delete)

        } else if (status == "edit") {


            //to send data to EditProductsDetailActivity
            RxBus.TotalEditProductData.publishTotalEditProductData(totalEditProductData)
            val intent = Intent(this@EditProductsActivity, EditProductsDetailActivity::class.java)
            intent.putExtra("position", position)
            intent.putExtra("id_editProductItem",model.id)
            intent.putExtra("edit_product_description",model.description)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }
    }

    private fun sendDeleteItemId(id: String?, avi: AVLoadingIndicatorView, btn_delete: Button) {

//        avi_edit_products.visibility = View.VISIBLE
        val service = ServiceProvider(this@EditProductsActivity).getmService()
        val call = service.deleteEditProductItem(id)
        call.enqueue(object : Callback<DeleteProduct> {

            override fun onResponse(call: Call<DeleteProduct>, response: Response<DeleteProduct>) {

                avi_edit_products.visibility = View.GONE
                if (response.code() == 200) {

                    avi.visibility = View.GONE
                    btn_delete.visibility = View.VISIBLE

                    var result: DeleteProduct? = response.body()
                    Toast.makeText(this@EditProductsActivity, resources.getString(R.string.product_deleted), Toast.LENGTH_SHORT).show()

                    //update list
                    page = 0
                    getEditList(page)

                } else if (response.code() == 204) {
                    txt_no_edit_product.visibility = View.VISIBLE
                    avi.visibility = View.GONE
                    btn_delete.visibility = View.VISIBLE
                } else {
                    Toast.makeText(this@EditProductsActivity, resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                    avi.visibility = View.GONE
                    btn_delete.visibility = View.VISIBLE
                }

            }

            override fun onFailure(call: Call<DeleteProduct>, t: Throwable) {
                Toast.makeText(this@EditProductsActivity, resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
                avi_edit_products.visibility = View.GONE   //general loading
                avi.visibility = View.GONE  // loading on delete button
                btn_delete.visibility = View.VISIBLE
            }

        })
    }


    override fun onResume() {
        super.onResume()

        editProducts = ArrayList<EditProducts>()
        getEditList(page)
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
//        disposable.dispose()
    }
}