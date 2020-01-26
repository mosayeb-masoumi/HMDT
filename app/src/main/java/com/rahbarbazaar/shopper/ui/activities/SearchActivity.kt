package com.rahbarbazaar.shopper.ui.activities

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.shopper.models.barcodlist.Barcode
import com.rahbarbazaar.shopper.models.search_goods.GroupsData
import com.rahbarbazaar.shopper.network.ServiceProvider
import com.rahbarbazaar.shopper.utilities.CustomBaseActivity
import com.rahbarbazaar.shopper.utilities.DialogFactory
import com.rahbarbazaar.shopper.utilities.GeneralTools
import com.rahbarbazaar.shopper.utilities.RxBus
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_qrcode.*
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList


class SearchActivity : CustomBaseActivity(), View.OnClickListener {

    private var connectivityReceiver: BroadcastReceiver? = null
    var disposable: Disposable = CompositeDisposable()
    lateinit var groupsData: GroupsData

    var group_id: String = ""
    var category_id: String = ""
    var sub_category_id: String = ""
    var brand_id: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        //check network broadcast reciever
        val tools = GeneralTools.getInstance()
        connectivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                tools.doCheckNetwork(this@SearchActivity, findViewById<View>(R.id.rl_root))
            }
        }

//        // get data from rxbus
//        disposable = CompositeDisposable()
//        disposable = RxBus.GroupsSpnData.subscribeGroupsSpnData { result ->
//            if (result is GroupsData) {
//                groupsData = result
//            }
//        }

        val intent = intent
//get data from QrCodeActivity
        groupsData = intent.getParcelableExtra("groupsData")


        linear_return_search.setOnClickListener(this)
        rl_home_search.setOnClickListener(this)
        btn_register_search.setOnClickListener(this)


        setGroupsSpn(groupsData)

    }

    private fun setGroupsSpn(groupsData: GroupsData) {


        val groupsTitleList: MutableList<String> = ArrayList()


        for (i in groupsData.data!!.indices) {
            groupsTitleList.add(groupsData.data!![i].title!!)
        }

        val adapter = ArrayAdapter(this, R.layout.custom_spinner, groupsTitleList)
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        spn_group.adapter = adapter


        spn_group.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val title = spn_group.selectedItem.toString()

                when {
                    position > 0 -> {

                        for (i in this@SearchActivity.groupsData.data!!.indices) {
                            if (this@SearchActivity.groupsData.data!![i].title == title) {
                                group_id = this@SearchActivity.groupsData.data!![i].id!!

                                category_id = ""
                                sub_category_id = ""
                                brand_id = ""
                                getCategoryList(group_id)

                                emptySpnSubCategory_spnBrand()
                            }
                        }

                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

    }

    @SuppressLint("ResourceAsColor")
    private fun emptySpnSubCategory_spnBrand() {
        val emptyList: MutableList<String> = ArrayList()
        val adapter = ArrayAdapter(this, R.layout.custom_spinner, emptyList)
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        spn_sub_category.adapter = adapter
        spn_brand.adapter = adapter

        txt_spn_subCategory.setTextColor(R.color.grey)
        rl_spn_sub_category.setBackgroundResource(R.drawable.bg_inactive_spn)

        txt_spn_brand.setTextColor(R.color.grey)
        rl_spn_brand.setBackgroundResource(R.drawable.bg_inactive_spn)

    }


    private fun getCategoryList(spnGroupID: String) {

        avi_category_spn.visibility = View.VISIBLE

        val service = ServiceProvider(this).getmService()
        val call = service.getCategorySpnData(spnGroupID)
        call.enqueue(object : Callback<GroupsData> {
            override fun onResponse(call: Call<GroupsData>, response: Response<GroupsData>) {

                if (response.code() == 200) {
                    var groupsData = GroupsData()
                    groupsData = response.body()!!
                    setSpnCategory(groupsData)
                    avi_category_spn.visibility = View.GONE

                } else if (response.code() == 204) {
//                    val intent = Intent(this@SearchActivity, PurchasedItemsActivity::class.java)
//                    intent.putExtra("no_searchedList", "no_searchedList")
//                    startActivity(intent)
                    showNoSearchResultDialog()
                    btn_register_search.visibility = View.VISIBLE
                    avi_register_search.visibility = View.GONE
                    avi_category_spn.visibility = View.GONE
                } else {
                    Toast.makeText(this@SearchActivity, resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                    avi_category_spn.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<GroupsData>, t: Throwable) {
                Toast.makeText(this@SearchActivity, resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
                avi_category_spn.visibility = View.GONE
            }
        })

    }

    @SuppressLint("ResourceAsColor")
    private fun setSpnCategory(groupsData: GroupsData) {

        txt_spn_category.setTextColor(R.color.blue_dark)
        rl_spn_category.setBackgroundResource(R.drawable.bg_prize_item)

        val categoryTitleList: MutableList<String> = ArrayList()

        for (i in groupsData.data!!.indices) {
            categoryTitleList.add(groupsData.data!![i].title!!)
        }

        val adapter = ArrayAdapter(this, R.layout.custom_spinner, categoryTitleList)
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        spn_category.adapter = adapter

        spn_category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val title = spn_category.selectedItem.toString()

                when {
                    position > 0 -> {

                        for (i in groupsData.data!!.indices) {
                            if (groupsData.data!![i].title == title) {
                                category_id = groupsData.data!![i].id!!
                                getSubCategoryList(category_id)

                                sub_category_id = ""
                                brand_id = ""
                                emptyBrandSpn()

                            }
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    @SuppressLint("ResourceAsColor")
    private fun emptyBrandSpn() {
        val emptyList: MutableList<String> = ArrayList()
        val adapter = ArrayAdapter(this, R.layout.custom_spinner, emptyList)
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        spn_brand.adapter = adapter

        txt_spn_brand.setTextColor(R.color.grey)
        rl_spn_brand.setBackgroundResource(R.drawable.bg_inactive_spn)
    }

    private fun getSubCategoryList(spnCategoryID: String) {
        avi_subCategory_spn.visibility = View.VISIBLE
        val service = ServiceProvider(this).getmService()
        val call = service.getSubCategorySpnData(spnCategoryID)
        call.enqueue(object : Callback<GroupsData> {
            override fun onResponse(call: Call<GroupsData>, response: Response<GroupsData>) {
                if (response.code() == 200) {
                    var groupsData = GroupsData()

                    groupsData = response.body()!!
                    setSpnSubCategory(groupsData)
                    avi_subCategory_spn.visibility = View.GONE

                } else if (response.code() == 204) {
//                    val intent = Intent(this@SearchActivity, PurchasedItemsActivity::class.java)
//                    intent.putExtra("no_searchedList", "no_searchedList")
//                    startActivity(intent)
                    showNoSearchResultDialog()
                    btn_register_search.visibility = View.VISIBLE
                    avi_register_search.visibility = View.GONE
                    avi_subCategory_spn.visibility = View.GONE
                } else {
                    Toast.makeText(this@SearchActivity, resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                    avi_subCategory_spn.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<GroupsData>, t: Throwable) {
                Toast.makeText(this@SearchActivity, resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
                avi_subCategory_spn.visibility = View.GONE
            }
        })
    }

    private fun setSpnSubCategory(groupsData: GroupsData) {
        txt_spn_subCategory.setTextColor(resources.getColor(R.color.blue_dark))
        rl_spn_sub_category.setBackgroundResource(R.drawable.bg_prize_item)

        val subCategoryTitleList: MutableList<String> = ArrayList()

        for (i in groupsData.data!!.indices) {
            subCategoryTitleList.add(groupsData.data!![i].title!!)
        }

        val adapter = ArrayAdapter(this, R.layout.custom_spinner, subCategoryTitleList)
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        spn_sub_category.adapter = adapter

        spn_sub_category.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val title = spn_sub_category.selectedItem.toString()

                when {
                    position > 0 -> {

                        for (i in groupsData.data!!.indices) {
                            if (groupsData.data!![i].title == title) {
                                sub_category_id = groupsData.data!![i].id!!
                                getBrandList(sub_category_id)
                            }
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

    }

    private fun getBrandList(spnSub_categoryID: String) {
        avi_brand_spn.visibility = View.VISIBLE
        val service = ServiceProvider(this).getmService()
        val call = service.getBrandSpnData(spnSub_categoryID)
        call.enqueue(object : Callback<GroupsData> {
            override fun onResponse(call: Call<GroupsData>, response: Response<GroupsData>) {
                if (response.code() == 200) {

                    var groupsData = GroupsData()
                    groupsData = response.body()!!
                    setSpnBrand(groupsData)
                    avi_brand_spn.visibility = View.GONE

                } else if (response.code() == 204) {
//                    val intent = Intent(this@SearchActivity, PurchasedItemsActivity::class.java)
//                    intent.putExtra("no_searchedList", "no_searchedList")
//                    startActivity(intent)
//                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
                    showNoSearchResultDialog()
                    btn_register_search.visibility = View.VISIBLE
                    avi_register_search.visibility = View.GONE
                    avi_brand_spn.visibility = View.GONE
                } else {
                    Toast.makeText(this@SearchActivity, resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                    avi_brand_spn.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<GroupsData>, t: Throwable) {
                Toast.makeText(this@SearchActivity, resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
                avi_brand_spn.visibility = View.GONE
            }
        })
    }

    private fun showNoSearchResultDialog() {

        val dialogFactory = DialogFactory(this)
        dialogFactory.createNoSearchSpnResultDialog(object : DialogFactory.DialogFactoryInteraction {
            override fun onAcceptButtonClicked(vararg strings: String?) {

                val intent = Intent(this@SearchActivity, PurchasedItemsActivity::class.java)
                intent.putExtra("no_searchedList", "no_searchedList")
                startActivity(intent)
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out)

            }

            override fun onDeniedButtonClicked(cancel_dialog: Boolean) {

            }

        }, rl_root_search)
    }

    private fun setSpnBrand(groupsData: GroupsData) {
        txt_spn_brand.setTextColor(resources.getColor(R.color.blue_dark))
        rl_spn_brand.setBackgroundResource(R.drawable.bg_prize_item)

        val brandTitleList: MutableList<String> = ArrayList()

        for (i in groupsData.data!!.indices) {
            brandTitleList.add(groupsData.data!![i].title!!)
        }

        val adapter = ArrayAdapter(this, R.layout.custom_spinner, brandTitleList)
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        spn_brand.adapter = adapter

        spn_brand.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                val title = spn_brand.selectedItem.toString()

                when {
                    position > 0 -> {

                        for (i in groupsData.data!!.indices) {
                            if (groupsData.data!![i].title == title) {
                                brand_id = groupsData.data!![i].id!!
                            }
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.linear_return_search -> {

                val intent = Intent(this@SearchActivity, QRcodeActivity::class.java)
                intent.putExtra("static_barcode", "static_barcode")
                startActivity(intent)
                finish()
            }

            R.id.rl_home_search -> {
                startActivity(Intent(this@SearchActivity, MainActivity::class.java))
                finish()
            }

            R.id.btn_register_search -> {

                getSearchedList()
            }

        }
    }

    private fun getSearchedList() {

        btn_register_search.visibility = View.GONE
        avi_register_search.visibility = View.VISIBLE

        val service = ServiceProvider(this).getmService()
        val call = service.getSearchedList(category_id, sub_category_id, brand_id, 0)
        call.enqueue(object : Callback<Barcode> {
            override fun onResponse(call: Call<Barcode>, response: Response<Barcode>) {
                if (response.code() == 200) {

                    var barcode = Barcode()
                    barcode = response.body()!!
                    RxBus.BarcodeList.publishBarcodeList(barcode)
                    startActivity(Intent(this@SearchActivity, BarcodeListActivity::class.java))
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
                    Toast.makeText(this@SearchActivity, "" + resources.getString(R.string.search_result_completed), Toast.LENGTH_LONG).show()
                    btn_register_search.visibility = View.VISIBLE
                    avi_register_search.visibility = View.GONE

                } else {
                    Toast.makeText(this@SearchActivity, resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                    btn_register_search.visibility = View.VISIBLE
                    avi_register_search.visibility = View.GONE
                }
            }

            override fun onFailure(call: Call<Barcode>, t: Throwable) {
                Toast.makeText(this@SearchActivity, resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
                btn_register_search.visibility = View.VISIBLE
                avi_register_search.visibility = View.GONE
            }
        })

    }


    override fun onResume() {
        super.onResume()
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
        disposable.dispose()
    }
}