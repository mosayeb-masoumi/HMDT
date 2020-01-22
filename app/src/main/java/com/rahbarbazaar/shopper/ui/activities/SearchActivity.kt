package com.rahbarbazaar.shopper.ui.activities

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
import com.rahbarbazaar.shopper.models.search_goods.GroupsData
import com.rahbarbazaar.shopper.network.ServiceProvider
import com.rahbarbazaar.shopper.utilities.CustomBaseActivity
import com.rahbarbazaar.shopper.utilities.GeneralTools
import com.rahbarbazaar.shopper.utilities.RxBus
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.ArrayList



class SearchActivity : CustomBaseActivity(), View.OnClickListener {

    private var connectivityReceiver: BroadcastReceiver? = null
    var disposable: Disposable = CompositeDisposable()
    lateinit var groupsData: GroupsData

    var spnGroupID:String = ""
    var spnCategoryID:String = ""
    var spnSub_categoryID:String = ""
    var spnBrandID:String = ""

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


        // get data from rxbus
        disposable = CompositeDisposable()
        disposable = RxBus.GroupsSpnData.subscribeGroupsSpnData { result ->
            if (result is GroupsData) {
                groupsData = result
            }
        }

        linear_return_search.setOnClickListener(this)
        rl_home_search.setOnClickListener(this)

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
                    position > 0  -> {

                        for (i in this@SearchActivity.groupsData.data!!.indices) {
                            if (this@SearchActivity.groupsData.data!![i].title == title) {
                                spnGroupID = this@SearchActivity.groupsData.data!![i].id!!

                                getCategoryList(spnGroupID)
                            }
                        }

                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


    }

    private fun getCategoryList(spnGroupID: String) {
        val service = ServiceProvider(this).getmService()
        val call = service.getCategorySpnData(spnGroupID)
        call.enqueue(object : Callback<GroupsData> {
            override fun onResponse(call: Call<GroupsData>, response: Response<GroupsData>) {
                if (response.code() == 200) {


                    var groupsData = GroupsData()
                    groupsData = response.body()!!

                    setSpnCategory(groupsData)

                } else {
                    Toast.makeText(this@SearchActivity, resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<GroupsData>, t: Throwable) {
                Toast.makeText(this@SearchActivity, resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun setSpnCategory(groupsData: GroupsData) {

        txt_spn_category.setTextColor(resources.getColor(R.color.blue_dark))
        rl_spn_category.setBackgroundResource(R.drawable.bg_prize_item)


        val categoryTitleList: MutableList<String> = ArrayList()

//        groupsTitleList.add("انتخاب کنید")

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
                    position > 0  -> {

                        for (i in groupsData.data!!.indices) {
                            if (groupsData.data!![i].title == title) {
                                spnCategoryID = groupsData.data!![i].id!!
                                getSubCategoryList(spnCategoryID)
                            }
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
    }

    private fun getSubCategoryList(spnCategoryID: String) {

    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.linear_return_search -> {

                val intent = Intent(this@SearchActivity,QRcodeActivity::class.java)
                intent.putExtra("static_barcode","static_barcode")
                startActivity(intent)
                finish()
            }

            R.id.rl_home_search -> {
                startActivity(Intent(this@SearchActivity, MainActivity::class.java))
                finish()
            }

            R.id.btn_register_search -> {
                startActivity(Intent(this@SearchActivity, MainActivity::class.java))
                finish()
            }

        }
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