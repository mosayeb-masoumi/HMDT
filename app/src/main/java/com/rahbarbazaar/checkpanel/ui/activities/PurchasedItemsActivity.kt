package com.rahbarbazaar.checkpanel.ui.activities

import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.*
import com.rahbarbazaar.checkpanel.R
import com.rahbarbazaar.checkpanel.controllers.adapters.AdapterEditPrize
import com.rahbarbazaar.checkpanel.controllers.adapters.AdapterPrize
import com.rahbarbazaar.checkpanel.controllers.adapters.AdapterRegisterMemberDialog
import com.rahbarbazaar.checkpanel.controllers.adapters.AdapterRegisterMemberEdit
import com.rahbarbazaar.checkpanel.controllers.interfaces.PrizeItemInteraction
import com.rahbarbazaar.checkpanel.controllers.interfaces.RegisterItemInteraction
import com.rahbarbazaar.checkpanel.models.api_error.ErrorUtils
import com.rahbarbazaar.checkpanel.models.purchased_item.PurchaseItemResult
import com.rahbarbazaar.checkpanel.models.purchased_item.SendPurchasedItemData
import com.rahbarbazaar.checkpanel.models.register.*
import com.rahbarbazaar.checkpanel.network.ServiceProvider
import com.rahbarbazaar.checkpanel.utilities.*
import com.wang.avi.AVLoadingIndicatorView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_purchased_items.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

//

class PurchasedItemsActivity : CustomBaseActivity(), View.OnClickListener,
        RegisterItemInteraction, PrizeItemInteraction, CompoundButton.OnCheckedChangeListener {


    private var connectivityReceiver: BroadcastReceiver? = null

    var disposable: Disposable = CompositeDisposable()
    lateinit var registerModel: RegisterModel

    private lateinit var adapter_member: AdapterRegisterMemberDialog
    private lateinit var adapter_edited: AdapterRegisterMemberEdit
    private lateinit var adapter_prize: AdapterPrize
    private lateinit var adapterEditPrize: AdapterEditPrize

//    private lateinit var sendPrizes: ArrayList<SendPrize>
    private lateinit var sendPrizes: ArrayList<SendPrize>
    private lateinit var editMembers: ArrayList<RegisterMemberEditModel>
//    private lateinit var sendPrizes: ArrayList<SendPrize>
    private lateinit var recyclerEditedMember: RecyclerView
    private lateinit var recycler_prize: RecyclerView

    private lateinit var dialogFactory: DialogFactory
    private var checkbox_text = ""
    private lateinit var checkBox_precentage: CheckBox
    private lateinit var checkBox_amount: CheckBox

    private lateinit var btn_register: Button
    private lateinit var btn_cancel: Button
    private lateinit var avi: AVLoadingIndicatorView

    var shopping_id: String = ""
    var product_id: String = ""
    var mygroup: String = ""

    // for handling422
    private var builderPaid: StringBuilder? = null
    private var builderCost:StringBuilder? = null
    private var builderDiscountAmount:StringBuilder? = null
    private var builderMember:StringBuilder? = null
    private var buliderPrize:StringBuilder? = null
    private var buliderAmount:StringBuilder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchased_items)


        //check network broadcast reciever
        val tools = GeneralTools.getInstance()
        connectivityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                tools.doCheckNetwork(this@PurchasedItemsActivity, findViewById<View>(R.id.rl_root_barcodelist))
            }
        }

        // get data from rxbus
        disposable = CompositeDisposable()
        disposable = RxBus.RegisterModel.subscribeRegisterModel { result ->
            if (result is RegisterModel) {
                registerModel = result
            }
        }

        initView()
        //initial Dialog factory
        dialogFactory = DialogFactory(this@PurchasedItemsActivity)



        // important to initialize lists in oncreate in kotlin
        editMembers = ArrayList<RegisterMemberEditModel>()
        sendPrizes = ArrayList<SendPrize>()

    }

    private fun initView() {
        val unit: String = intent.getStringExtra("unit")
        product_id = intent.getStringExtra("product_id")
        mygroup = intent.getStringExtra("mygroup")
        shopping_id = Cache.getString("shopping_id")
        txt_unit.text = unit

        btn_register = findViewById(R.id.btn_register_purchased_items)
        btn_cancel = findViewById(R.id.btn_cancel_purchased_items)
        recyclerEditedMember = findViewById(R.id.recycler_edited_members)
        avi = findViewById(R.id.avi_register_purchased_items)
        recycler_prize = findViewById(R.id.recycler_prize)
        checkBox_precentage = findViewById(R.id.checkBox_precentage)
        checkBox_amount = findViewById(R.id.checkBox_amount)
        rl_addmember.setOnClickListener(this)
        rl_prize.setOnClickListener(this)
        checkBox_precentage.setOnCheckedChangeListener(this)
        checkBox_amount.setOnCheckedChangeListener(this)

        btn_register.setOnClickListener(this)
        btn_cancel.setOnClickListener(this)



        edt_discount.isEnabled = false
        edt_discount.setText("")
    }


    override fun onClick(view: View) {

        when (view.id) {

            R.id.rl_addmember -> {

                showAddMemberDialog()
            }

            R.id.rl_prize -> {
                showPrizeDialog()
            }

            R.id.btn_cancel_purchased_items -> {

            }

            R.id.btn_register_purchased_items -> {

                sendData()
            }

        }
    }

    private fun sendData() {

        showLoading()

        var sendData = SendPurchasedItemData()

        sendData.shopping_id = shopping_id
        sendData.amount = edt_amount.text.toString()
        sendData.cost = edt_price.text.toString()
        sendData.paid = edt_paid.text.toString()
        sendData.product_id = product_id
        sendData.member = editMembers
        sendData.prize = sendPrizes
        sendData.mygroup = mygroup

        if (checkBox_precentage.isChecked) {
            sendData.discount_type = "percent"
            sendData.discount_amount = edt_discount.text.toString()
        } else if (checkBox_amount.isChecked) {
            sendData.discount_type = "amount"
            sendData.discount_amount =edt_discount.text.toString()
        }


        val service = ServiceProvider(this).getmService()
        val call = service.getPurchaseItemResult(sendData)
        call.enqueue(object :Callback<PurchaseItemResult>{

            override fun onResponse(call: Call<PurchaseItemResult>, response: Response<PurchaseItemResult>) {

                hideLoading()
                if (response.code()==200){
                   var a :Boolean = response.body()!!.data
                    showNextScanDialog()
                }else if(response.code()==422){

                    builderMember = null
                    builderCost = null
                    builderPaid = null
                    builderDiscountAmount = null
                    buliderPrize = null
                    buliderAmount = null

                    val apiError = ErrorUtils.parseError422(response)

                    if (apiError.errors.member != null) {
                        builderMember = StringBuilder()
                        for (b in apiError.errors.member) {
                            builderMember!!.append("").append(b).append(" ")
                        }
                    }


                    if (apiError.errors.cost != null) {
                        builderCost = StringBuilder()
                        for (b in apiError.errors.cost) {
                            builderCost!!.append("").append(b).append(" ")
                        }
                    }

                    if (apiError.errors.paid != null) {
                        builderPaid = StringBuilder()
                        for (a in apiError.errors.paid) {
                            builderPaid!!.append("").append(a).append(" ")
                        }
                    }

                    if (apiError.errors.discountAmount != null) {
                        builderDiscountAmount = StringBuilder()
                        for (c in apiError.errors.discountAmount) {
                            builderDiscountAmount!!.append("").append(c).append(" ")
                        }
                    }

                    if (apiError.errors.prize != null) {
                        buliderPrize = StringBuilder()
                        for (b in apiError.errors.prize) {
                            buliderPrize!!.append("").append(b).append(" ")
                        }
                    }

                    if (apiError.errors.amount != null) {
                        buliderAmount = StringBuilder()
                        for (b in apiError.errors.amount) {
                            buliderAmount!!.append("").append(b).append(" ")
                        }
                    }


                    if (builderMember != null) {
                        Toast.makeText(this@PurchasedItemsActivity, "" + builderMember, Toast.LENGTH_SHORT).show()
                    }

                    if (builderCost != null) {
                        Toast.makeText(this@PurchasedItemsActivity, "" + builderCost, Toast.LENGTH_SHORT).show()
                    }
                    if (builderPaid != null) {
                        Toast.makeText(this@PurchasedItemsActivity, "" + builderPaid, Toast.LENGTH_SHORT).show()
                    }
                    if (builderDiscountAmount != null) {
                        Toast.makeText(this@PurchasedItemsActivity, "" + builderDiscountAmount, Toast.LENGTH_SHORT).show()
                    }
                    if (buliderPrize != null) {
                        Toast.makeText(this@PurchasedItemsActivity, "" + buliderPrize, Toast.LENGTH_SHORT).show()
                    }

                    if (  buliderAmount != null) {
                        Toast.makeText(this@PurchasedItemsActivity, "" +   buliderAmount, Toast.LENGTH_SHORT).show()
                    }


                }else{
                    Toast.makeText(this@PurchasedItemsActivity,resources.getString(R.string.serverFaield),Toast.LENGTH_SHORT).show()
                }

            }

            override fun onFailure(call: Call<PurchaseItemResult>, t: Throwable) {

                Toast.makeText(this@PurchasedItemsActivity,resources.getString(R.string.connectionFaield),Toast.LENGTH_SHORT).show()
                hideLoading()
            }

        })

    }

    private fun showNextScanDialog() {

        val dialogFactory = DialogFactory(this)
        dialogFactory.createRescanDialog(object : DialogFactory.DialogFactoryInteraction {
            override fun onAcceptButtonClicked(vararg strings: String?) {
            }

            override fun onDeniedButtonClicked(cancel_dialog: Boolean) {

            }

        },layout_purchase_item)

    }

    private fun hideLoading() {
        avi.hide()
        avi.visibility = View.GONE
        btn_register.visibility = View.VISIBLE
    }

    private fun showLoading() {
        avi.show()
        avi.visibility = View.VISIBLE
        btn_register.visibility = View.GONE
    }


    private fun showAddMemberDialog() {
        editMembers = ArrayList<RegisterMemberEditModel>()

        val dialog = Dialog(this@PurchasedItemsActivity)
        dialog.setContentView(R.layout.register_members_dialog)

        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        // to show list of member items
        val members = ArrayList<Member>()

        for (i in registerModel.data.member.indices) {
            for (j in 0 until registerModel.data.member[i].size) {
                members.add(Member(registerModel.data.member[i][j].name, registerModel.data.member[i][j].id))
            }
        }

        val checkBoxAll = dialog.findViewById<CheckBox>(R.id.checkbox_all)
        val recyclerview_members = dialog.findViewById<RecyclerView>(R.id.recyclerview_members)
        val btn_exit_dialog = dialog.findViewById<Button>(R.id.btn_exit_dialog)
        val img_close = dialog.findViewById<ImageView>(R.id.img_close)
        recyclerview_members.layoutManager = LinearLayoutManager(this@PurchasedItemsActivity)
        adapter_member = AdapterRegisterMemberDialog(members, this@PurchasedItemsActivity)
        adapter_member.setListener(this) // important or else the app will crashed
        recyclerview_members.adapter = adapter_member


        // to select all members
        checkBoxAll.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editMembers = ArrayList<RegisterMemberEditModel>()

                for (i in registerModel.data.member.indices) {
                    for (j in 0 until registerModel.data.member[i].size) {
                        editMembers.add(RegisterMemberEditModel(registerModel.data.member[i][j].name,
                                registerModel.data.member[i][j].id))
                    }
                }

                updateEditMemberList(editMembers)
                dialog.dismiss()
            }
        }



        img_close.setOnClickListener { v -> dialog.dismiss() }
        btn_exit_dialog.setOnClickListener { v -> dialog.dismiss() }

        dialog.show()

    }

    private fun updateEditMemberList(editMembers: ArrayList<RegisterMemberEditModel>) {

        recyclerEditedMember.layoutManager = GridLayoutManager(this@PurchasedItemsActivity, 3)
        adapter_edited = AdapterRegisterMemberEdit(editMembers, this@PurchasedItemsActivity)
        recyclerEditedMember.adapter = adapter_edited
    }


    override fun onClicked(name: String?, id: String?, chkbox: Boolean?) {
        if (chkbox!!) {
            editMembers.add(RegisterMemberEditModel(name, id))

        } else {

            if (editMembers.size > 0) {
                for (i in editMembers.indices) {
                    if (editMembers[i].txt_name == name) {
                        editMembers.removeAt(i)
                    }
                }
            }
        }
        updateEditMemberList(editMembers)
    }


    private fun showPrizeDialog() {
        sendPrizes = ArrayList<SendPrize>()
        val dialog = Dialog(this@PurchasedItemsActivity)
        dialog.setContentView(R.layout.prize_dialog)

        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }

        // to show list of member items
        val prizes = ArrayList<Prize>()


        for (i in registerModel.data.prize.indices) {
            for (j in 0 until registerModel.data.prize[i].size) {
                prizes.add(Prize(registerModel.data.prize[i][j].title, registerModel.data.prize[i][j].id))
            }
        }

        val recycler_prize = dialog.findViewById<RecyclerView>(R.id.recycler_prize)
        val btn_exit_dialog = dialog.findViewById<Button>(R.id.btn_exit_dialog)
        val img_close = dialog.findViewById<ImageView>(R.id.img_close)
        recycler_prize.layoutManager = LinearLayoutManager(this@PurchasedItemsActivity)
        adapter_prize = AdapterPrize(prizes, this@PurchasedItemsActivity)
        adapter_prize.setListener(this)  // important or else the app will crashed
        recycler_prize.adapter = adapter_prize

        img_close.setOnClickListener { v -> dialog.dismiss() }
        btn_exit_dialog.setOnClickListener { v -> dialog.dismiss() }

        dialog.show()
    }


    override fun prizeOnClicked(title: String?, id: String?, chkbox: Boolean?) {
        if (chkbox!!) {
            createPrizeDetailDialog(title, id)
        } else {
            if (sendPrizes.size > 0) {
                for (i in sendPrizes.indices) {
                    if (sendPrizes[i].id == id) {
                        sendPrizes.removeAt(i)
                    }
                }
                updateEditPrizeList(sendPrizes)
            }
        }
    }



    private fun updateEditPrizeList(sendPrizes: ArrayList<SendPrize>) {

        recycler_prize.layoutManager = GridLayoutManager(this@PurchasedItemsActivity, 3)
        adapterEditPrize = AdapterEditPrize(sendPrizes, this@PurchasedItemsActivity)
        recycler_prize.adapter = adapterEditPrize
    }

    private fun createPrizeDetailDialog(title: String?, id: String?) {
        dialogFactory.createPrizeDetailDialog(object : DialogFactory.DialogFactoryInteraction {
            override fun onAcceptButtonClicked(vararg params: String) {
                val desc = params[0]
//                val title = params[1]
                val id = params[2]
                sendPrizes.add(SendPrize(desc, id))

                updateEditPrizeList(sendPrizes)
            }

            override fun onDeniedButtonClicked(bool: Boolean) {

            }
        }, title, id, layout_purchase_item)
    }


    override fun onCheckedChanged(view: CompoundButton?, isChecked: Boolean) {

        if (!checkBox_precentage.isChecked && !checkBox_amount.isChecked) {
            edt_discount.hint = resources.getString(R.string.percent_amount)
            edt_discount.isEnabled = false
            edt_discount.setText("")
        }

        when (view!!.getId()) {
            R.id.checkBox_amount -> if (isChecked) {
                checkBox_precentage.isChecked = false
                edt_discount.hint = resources.getString(R.string.amount2_)
                checkbox_text = resources.getString(R.string.amount2_)
                edt_discount.isEnabled = true
            }

            R.id.checkBox_precentage -> if (isChecked) {
                checkBox_amount.isChecked = false
                edt_discount.hint = resources.getString(R.string.percent)
                checkbox_text = resources.getString(R.string.percent)
                edt_discount.isEnabled = true
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