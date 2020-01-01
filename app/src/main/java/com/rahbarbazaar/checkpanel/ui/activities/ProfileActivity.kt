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
import android.widget.LinearLayout
import android.widget.Toast
import com.rahbarbazaar.checkpanel.R
import com.rahbarbazaar.checkpanel.controllers.adapters.MessageAdapter
import com.rahbarbazaar.checkpanel.controllers.adapters.ProfileFamilyAdapter
import com.rahbarbazaar.checkpanel.models.profile.Family
import com.rahbarbazaar.checkpanel.models.profile.ProfileData
import com.rahbarbazaar.checkpanel.network.ServiceProvider
import com.rahbarbazaar.checkpanel.utilities.CustomBaseActivity
import com.rahbarbazaar.checkpanel.utilities.GeneralTools
import kotlinx.android.synthetic.main.activity_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileActivity : CustomBaseActivity() ,View.OnClickListener {


    private var connectivityReceiver: BroadcastReceiver? = null

    lateinit var profileData: ProfileData
    private lateinit var adapter_family: ProfileFamilyAdapter
    lateinit var family: ArrayList<Family>
    private var linearLayoutManager: LinearLayoutManager? = null

    var familyListVisibility:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        //check network broadcast reciever
        val tools = GeneralTools.getInstance()
        connectivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                tools.doCheckNetwork(this@ProfileActivity, findViewById<View>(R.id.rl_root))
            }
        }


        getProfileData()

        // message must be initialize
        family = ArrayList<Family>()

        familyListVisibility="unseen"
        if(familyListVisibility.equals("unseen")){
            img_plus_profile_family_info.setImageDrawable(resources.getDrawable(R.drawable.plus_blue_dark))
        }

        rl_family_info.setOnClickListener(this)

    }

    private fun getProfileData() {

        val service = ServiceProvider(this).getmService()
        val call = service.profileList
//        val call = service.getBarcodeList("1398")
        call.enqueue(object : Callback<ProfileData> {
            override fun onResponse(call: Call<ProfileData>, response: Response<ProfileData>) {
                if(response.code()==200){

                  profileData = response.body()!!

                    setPersonalProfile(profileData)
                    setRecyclerviewFamily(profileData)
                    setRecyclerviewMember(profileData)



                }else{
                    Toast.makeText(this@ProfileActivity, resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()

                }
            }

            override fun onFailure(call: Call<ProfileData>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()

            }
        })
    }

    private fun setPersonalProfile(profileData: ProfileData) {
        txt_name_title_profile.text = "نام:"
        txt_name_value_profile.text = profileData.data?.personal?.name
        txt_phone_title_profile.text="موبایل:"
        txt_phone_value_profile.text = profileData.data?.personal?.mobile
    }

    private fun setRecyclerviewFamily(profileData: ProfileData) {

        profileData.data?.family?.let {
            family.addAll(it)
        }


        val rv_family: RecyclerView = findViewById(R.id.rv_family_profile)

        linearLayoutManager = LinearLayoutManager(this@ProfileActivity, LinearLayout.VERTICAL, false)
        rv_family.layoutManager = linearLayoutManager

        adapter_family = ProfileFamilyAdapter(family, this@ProfileActivity)
//        adapter.setListener(this)  // important to set or else the app will crashed (onClick)
        rv_family.adapter = adapter_family
        adapter_family.notifyDataSetChanged()



    }

    private fun setRecyclerviewMember(profileData: ProfileData) {

    }


    override fun onClick(view: View?) {

        when(view?.id) {
            R.id.rl_family_info -> {
                if(familyListVisibility.equals("unseen")){

                    img_plus_profile_family_info.setImageDrawable(resources.getDrawable(R.drawable.minus_blue))
                    rv_family_profile.visibility=View.VISIBLE
                    familyListVisibility="seen"
                }else if(familyListVisibility.equals("seen")){
                    img_plus_profile_family_info.setImageDrawable(resources.getDrawable(R.drawable.plus_blue_dark))
                    rv_family_profile.visibility=View.GONE
                    familyListVisibility="unseen"
                }

              }

//            R.id.btnSubmit -> {
//
//            }
        }

    }

    override fun onResume() {
        super.onResume()
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(connectivityReceiver)
    }
}