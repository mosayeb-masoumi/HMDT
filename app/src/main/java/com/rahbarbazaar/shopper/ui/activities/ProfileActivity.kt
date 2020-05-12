package com.rahbarbazaar.shopper.ui.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.Toast
import com.rahbarbazaar.shopper.R
import com.rahbarbazaar.shopper.controllers.adapters.ProfileFamilyAdapter
import com.rahbarbazaar.shopper.controllers.adapters.ProfileMemberAdapter
import com.rahbarbazaar.shopper.controllers.interfaces.ProfileMemberItemInteraction
import com.rahbarbazaar.shopper.models.profile.*
import com.rahbarbazaar.shopper.network.ServiceProvider
import com.rahbarbazaar.shopper.utilities.CustomBaseActivity
import com.rahbarbazaar.shopper.utilities.DialogFactory
import com.rahbarbazaar.shopper.utilities.GeneralTools
import com.rahbarbazaar.shopper.utilities.RxBus
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileActivity : CustomBaseActivity(), View.OnClickListener, ProfileMemberItemInteraction {

    private var connectivityReceiver: BroadcastReceiver? = null
    var disposable: Disposable = CompositeDisposable()

    lateinit var profileData: ProfileData
    private lateinit var adapter_family: ProfileFamilyAdapter
    private lateinit var adapter_member: ProfileMemberAdapter
    lateinit var family: ArrayList<Family>
    lateinit var member: ArrayList<Member>
    lateinit var member_detail: ArrayList<MemberDetail>

    //    lateinit var memberDetail: MemberDetail
    private var linearLayoutManager: LinearLayoutManager? = null

    var familyListVisibility: String = ""

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

        // get data from rxbus
        disposable = CompositeDisposable()
        disposable = RxBus.ProfileInfo.subscribeProfileInfo{ result ->
            if (result is ProfileData) {
                profileData = result
            }
        }

        // message must be initialize
        family = ArrayList<Family>()
        member = ArrayList<Member>()

        familyListVisibility = "unseen"
        if (familyListVisibility.equals("unseen")) {
            img_plus_profile_family_info.setImageDrawable(resources.getDrawable(R.drawable.plus_blue_dark))
        }
        rl_family_info.setOnClickListener(this)
        btn_profile_change.setOnClickListener(this)

        linear_exit_profile.setOnClickListener {
            finish()
        }

        setPersonalProfile(profileData)
        setRecyclerviewFamily(profileData)
        setRecyclerviewMember(profileData)
    }


    private fun setPersonalProfile(profileData: ProfileData) {
        txt_name_title_profile.text = "نام:"
        txt_name_value_profile.text = profileData.data?.personal?.name
        txt_phone_title_profile.text = "موبایل:"
        txt_phone_value_profile.text = profileData.data?.personal?.mobile
        txt_code_value_profile.text = profileData.data?.personal?.code
    }

    private fun setRecyclerviewFamily(profileData: ProfileData) {

        profileData.data?.family?.let {
            family.addAll(it)
        }

        member_detail = ArrayList<MemberDetail>()
        val rv_family: RecyclerView = findViewById(R.id.rv_family_profile)
        linearLayoutManager = LinearLayoutManager(this@ProfileActivity, LinearLayout.VERTICAL, false)
        rv_family.layoutManager = linearLayoutManager
        adapter_family = ProfileFamilyAdapter(family, this@ProfileActivity)
//        adapter.setListener(this)  // important to set or else the app will crashed (onClick)
        rv_family.adapter = adapter_family
        adapter_family.notifyDataSetChanged()
    }

    private fun setRecyclerviewMember(profileData: ProfileData) {

        member_detail.addAll(profileData.data?.member?.data!!)
        val rv_member: RecyclerView = findViewById(R.id.rv_member_profile)
        linearLayoutManager = LinearLayoutManager(this@ProfileActivity, LinearLayout.VERTICAL, false)
        rv_member.layoutManager = linearLayoutManager
        adapter_member = ProfileMemberAdapter(member_detail, this@ProfileActivity)
        adapter_member.setListener(this)  // important to set (onClick)
        rv_member.adapter = adapter_member
        adapter_member.notifyDataSetChanged()
    }


    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.rl_family_info -> {
                if (familyListVisibility.equals("unseen")) {

                    img_plus_profile_family_info.setImageDrawable(resources.getDrawable(R.drawable.minus_blue))
                    rv_family_profile.visibility = View.VISIBLE
                    familyListVisibility = "seen"
                } else if (familyListVisibility.equals("seen")) {
                    img_plus_profile_family_info.setImageDrawable(resources.getDrawable(R.drawable.plus_blue_dark))
                    rv_family_profile.visibility = View.GONE
                    familyListVisibility = "unseen"
                }
            }

            R.id.btn_profile_change-> {
                showChangeProfileDialog()
            }
        }
    }

    private fun showChangeProfileDialog() {
        val dialogFactory = DialogFactory(this)
        dialogFactory.createChangeProfileDialog(object : DialogFactory.DialogFactoryInteraction {
            override fun onAcceptButtonClicked(vararg strings: String?) {
                val body:String? = strings[0]
                requestProfileChanges(body)
            }

            override fun onDeniedButtonClicked(cancel_dialog: Boolean) {

            }

        }, profile_root)
    }

    private fun requestProfileChanges(body: String?) {
        val service = ServiceProvider(this).getmService()
        val call = service.profileChange(body)
        call.enqueue(object : Callback<ProfileChange> {
            override fun onResponse(call: Call<ProfileChange>, response: Response<ProfileChange>) {
                if (response.code() == 200) {

//                    var profileChange = ProfileChange()
//                    profileChange = response.body()!!

                    Toast.makeText(this@ProfileActivity, resources.getString(R.string.request_successfully), Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(this@ProfileActivity, resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ProfileChange>, t: Throwable) {
                Toast.makeText(this@ProfileActivity, resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()

            }
        })

    }

    override fun profileMemberitemOnClicked(member_detail: MemberDetail, position: Int) {

        val dialogFactory = DialogFactory(this)
        dialogFactory.createProfileMemberDetailDialog(object : DialogFactory.DialogFactoryInteraction {
            override fun onAcceptButtonClicked(vararg strings: String?) {
            }

            override fun onDeniedButtonClicked(cancel_dialog: Boolean) {

            }

        }, profile_root ,member_detail)

    }

    override fun onResume() {
        super.onResume()
        registerReceiver(connectivityReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivityReceiver)
        disposable.dispose()
    }

//    override fun onDestroy() {
//        super.onDestroy()
//
//    }
}