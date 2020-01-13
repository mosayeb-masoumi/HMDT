package com.rahbarbazaar.checkpanel.ui.activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.annotation.MainThread
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import com.rahbarbazaar.checkpanel.R
import com.rahbarbazaar.checkpanel.controllers.adapters.EditPrizeAdapter
import com.rahbarbazaar.checkpanel.controllers.adapters.PrizeAdapter
import com.rahbarbazaar.checkpanel.controllers.adapters.RegisterMemberDialogAdapter
import com.rahbarbazaar.checkpanel.controllers.adapters.RegisterMemberEditAdapter
import com.rahbarbazaar.checkpanel.controllers.interfaces.PrizeItemInteraction
import com.rahbarbazaar.checkpanel.controllers.interfaces.RegisterItemInteraction
import com.rahbarbazaar.checkpanel.models.api_error.ErrorUtils
import com.rahbarbazaar.checkpanel.models.purchased_item.PurchaseItemNoProductResult
import com.rahbarbazaar.checkpanel.models.purchased_item.PurchaseItemResult
import com.rahbarbazaar.checkpanel.models.purchased_item.SendPurchasedItemData
import com.rahbarbazaar.checkpanel.models.purchased_item.SendPurchasedItemNoProductData
import com.rahbarbazaar.checkpanel.models.register.*
import com.rahbarbazaar.checkpanel.models.shopping_memberprize.MemberPrize
import com.rahbarbazaar.checkpanel.network.ServiceProvider
import com.rahbarbazaar.checkpanel.utilities.*
import com.wang.avi.AVLoadingIndicatorView
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_purchased_items.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PurchasedItemsActivity : CustomBaseActivity(), View.OnClickListener,
        RegisterItemInteraction, PrizeItemInteraction, CompoundButton.OnCheckedChangeListener {

    private var connectivityReceiver: BroadcastReceiver? = null

    var disposable: Disposable = CompositeDisposable()
    lateinit var registerModel: RegisterModel
    lateinit var initMemberPrizeLists: MemberPrize

    private lateinit var _memberAdapter: RegisterMemberDialogAdapter
    private lateinit var _editedAdapter: RegisterMemberEditAdapter
    private lateinit var _prizeAdapter: PrizeAdapter
    private lateinit var editPrizeAdapter: EditPrizeAdapter

    private lateinit var sendPrizes: ArrayList<SendPrize>
    private lateinit var editMembers: ArrayList<RegisterMemberEditModel>
    private lateinit var recyclerEditedMember: RecyclerView
    private lateinit var recycler_prize: RecyclerView

    private lateinit var dialogFactory: DialogFactory
    private var checkbox_text = ""
    private lateinit var checkBox_precentage: CheckBox
    private lateinit var checkBox_amount: CheckBox

    private lateinit var btn_register: Button
    private lateinit var avi: AVLoadingIndicatorView

    var shopping_id: String? = ""
    var product_id: String? = ""
    var mygroup: String? = ""
    var type: String? = ""
    var barcode: String? = ""
    var description: String? = ""
    var info_type: String? = ""


    // for handling422
    private var builderPaid: StringBuilder? = null
    private var builderCost: StringBuilder? = null
    private var builderDiscountAmount: StringBuilder? = null
    private var builderMember: StringBuilder? = null
    private var buliderPrize: StringBuilder? = null
    private var buliderAmount: StringBuilder? = null
    private var buliderBarcode: StringBuilder? = null
    private var buliderDescription: StringBuilder? = null
    private var buliderBrand: StringBuilder? = null
    private var buliderSize: StringBuilder? = null
    private var buliderUnit: StringBuilder? = null


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

        registerModel = RegisterModel()

        // get data from rxbus
        disposable = CompositeDisposable()
        disposable = RxBus.RegisterModel.subscribeRegisterModel { result ->
            if (result is RegisterModel) {
                registerModel = result
            }
        }

        initMemberPrizeLists = MemberPrize()
        disposable = RxBus.MemberPrizeLists.subscribeMemberPrizeLists { result ->
            if (result is MemberPrize) {
                initMemberPrizeLists = result
            }
        }


        initView()
        //initial Dialog factory
        dialogFactory = DialogFactory(this@PurchasedItemsActivity)

        // important to initialize lists in oncreate in kotlin
        editMembers = ArrayList<RegisterMemberEditModel>()
        sendPrizes = ArrayList<SendPrize>()


        description = intent.getStringExtra("barcodeListItemDesc")
        barcode = intent.getStringExtra("barcode")
        type = intent.getStringExtra("no_product")
        if (type == "no_product") {
            linear_no_product.visibility = View.VISIBLE
            purchased_item_header.text = "ثبت مشخصات و کالای خریداری شده"
            ll_decs_purchased_item.visibility = View.GONE
        } else {
            purchased_item_header.text = "ثبت کالای خریداری شده"
            linear_no_product.visibility = View.GONE
        }

        edt_barcode_no_product.setText(barcode)
        txt_desc_purchased_item.text = description

        edt_unit_no_product.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                txt_unit.text = s
            }

            override fun afterTextChanged(s: Editable) {

            }
        })


        txt_total_amount_title_purchased_item.text = (resources.getString(R.string.tottal_amount) +" "+"("+"ریال"+")")

        txt_paid_title_purchased_item.text = (resources.getString(R.string.paid_amount) +" "+"("+"ریال"+")")


        txt_discount_title_purchased_item.text = (resources.getString(R.string.discount_amount) +" "+"("+"ریال"+")")


    }

    private fun initView() {
        val unit: String? = intent.getStringExtra("unit")
        product_id = intent.getStringExtra("product_id")
        mygroup = intent.getStringExtra("mygroup")
//        shopping_id = Cache.getString("shopping_id")
        shopping_id = Cache.getString(this@PurchasedItemsActivity, "shopping_id")
        txt_unit.text = unit

        btn_register = findViewById(R.id.btn_register_purchased_items)
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


        linear_return_purchased_item.setOnClickListener(this)
        rl_home_purchased_item.setOnClickListener(this)
        rl_info_member_purchased_item.setOnClickListener(this)
        rl_info_prize_purchased_item.setOnClickListener(this)

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

            R.id.linear_return_purchased_item -> {

                finish()
            }

            R.id.btn_register_purchased_items -> {

                if (type == "no_product") {
                    sendDataNo_product()
                } else {
                    sendData()
                }
            }

            R.id.rl_home_purchased_item -> {
                startActivity(Intent(this@PurchasedItemsActivity,MainActivity::class.java))
                finish()
            }

            R.id.rl_info_member_purchased_item -> {
                info_type = "member_info_purchased_item"
                showInfoDialog(info_type!!)
            }
            R.id.rl_info_prize_purchased_item -> {
                info_type = "prize_info_purchased_item"
                showInfoDialog(info_type!!)
            }

        }
    }

    private fun showInfoDialog(info_type: String) {
        dialogFactory.createInfoMemberPrizeDialog(object : DialogFactory.DialogFactoryInteraction {
            override fun onAcceptButtonClicked(vararg params: String) {


            }

            override fun onDeniedButtonClicked(bool: Boolean) {

            }
        }, layout_purchase_item, info_type)
    }


    private fun sendData() {

        showLoading()

        val sendData = SendPurchasedItemData()
        sendData.shopping_id = shopping_id
        sendData.amount = edt_amount.text.toString()
        sendData.cost = edt_price.text.toString()
        sendData.paid = edt_paid.text.toString()
        sendData.product_id = product_id
        sendData.member = editMembers

        if (sendPrizes.size != 0) {
            sendData.prize = sendPrizes
        }


        sendData.type = mygroup

        if (checkBox_precentage.isChecked) {
            sendData.discount_type = "percent"
            sendData.discount_amount = edt_discount.text.toString()
        } else if (checkBox_amount.isChecked) {
            sendData.discount_type = "amount"
            sendData.discount_amount = edt_discount.text.toString()
        }

        val service = ServiceProvider(this).getmService()
        val call = service.getPurchaseItemResult(sendData)
        call.enqueue(object : Callback<PurchaseItemResult> {

            override fun onResponse(call: Call<PurchaseItemResult>, response: Response<PurchaseItemResult>) {

                hideLoading()
                if (response.code() == 200) {
                    var a: Boolean = response.body()!!.data
//                    showNextScanDialog()
                    startActivity(Intent(this@PurchasedItemsActivity, QRcodeActivity::class.java))
                    Toast.makeText(this@PurchasedItemsActivity,
                            resources.getString(R.string.register_product_successfully), Toast.LENGTH_SHORT).show()
                    Cache.setString(this@PurchasedItemsActivity, "barcode_registered", "barcode_registered")

                    finish()
                } else if (response.code() == 422) {

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

                    if (buliderAmount != null) {
                        Toast.makeText(this@PurchasedItemsActivity, "" + buliderAmount, Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this@PurchasedItemsActivity, resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                    hideLoading()
                }
            }

            override fun onFailure(call: Call<PurchaseItemResult>, t: Throwable) {

                Toast.makeText(this@PurchasedItemsActivity, resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
                hideLoading()
            }

        })

    }

    private fun sendDataNo_product() {
        showLoading()

        val sendData = SendPurchasedItemNoProductData()
        sendData.shopping_id = shopping_id
        sendData.amount = edt_amount.text.toString()
        sendData.cost = edt_price.text.toString()
        sendData.paid = edt_paid.text.toString()
        sendData.member = editMembers
        sendData.prize = sendPrizes
        sendData.description = edt_desc_no_product.text.toString()
        sendData.brand = edt_brand_no_product.text.toString()
        sendData.size = edt_size_no_product.text.toString()
        sendData.unit = edt_unit_no_product.text.toString()
        sendData.barcode = barcode


        if (checkBox_precentage.isChecked) {
            sendData.discount_type = "percent"
            sendData.discount_amount = edt_discount.text.toString()
        } else if (checkBox_amount.isChecked) {
            sendData.discount_type = "amount"
            sendData.discount_amount = edt_discount.text.toString()
        }


        val service = ServiceProvider(this).getmService()
        val call = service.getPurchaseItemNoProductResult(sendData)
        call.enqueue(object : Callback<PurchaseItemNoProductResult> {

            override fun onResponse(call: Call<PurchaseItemNoProductResult>, response: Response<PurchaseItemNoProductResult>) {

                hideLoading()
                if (response.code() == 200) {
                    var a: Boolean = response.body()!!.data
//                    showNextScanDialog()
                    startActivity(Intent(this@PurchasedItemsActivity, QRcodeActivity::class.java))
                    Toast.makeText(this@PurchasedItemsActivity,
                            resources.getString(R.string.register_product_successfully), Toast.LENGTH_SHORT).show()
                    Cache.setString(this@PurchasedItemsActivity, "barcode_registered", "barcode_registered")
                    finish()
                } else if (response.code() == 422) {

                    builderMember = null
                    builderCost = null
                    builderPaid = null
                    builderDiscountAmount = null
                    buliderPrize = null
                    buliderAmount = null
                    buliderBarcode = null
                    buliderDescription = null
                    buliderBrand = null
                    buliderSize = null
                    buliderUnit = null


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


                    if (apiError.errors.barcode != null) {
                        buliderBarcode = StringBuilder()
                        for (b in apiError.errors.barcode) {
                            buliderBarcode!!.append("").append(b).append(" ")
                        }
                    }

                    if (apiError.errors.description != null) {
                        buliderDescription = StringBuilder()
                        for (b in apiError.errors.description) {
                            buliderDescription!!.append("").append(b).append(" ")
                        }
                    }

                    if (apiError.errors.brand != null) {
                        buliderBrand = StringBuilder()
                        for (b in apiError.errors.brand) {
                            buliderBrand!!.append("").append(b).append(" ")
                        }
                    }

                    if (apiError.errors.size != null) {
                        buliderSize = StringBuilder()
                        for (b in apiError.errors.size) {
                            buliderSize!!.append("").append(b).append(" ")
                        }
                    }

                    if (apiError.errors.unit != null) {
                        buliderUnit = StringBuilder()
                        for (b in apiError.errors.unit) {
                            buliderUnit!!.append("").append(b).append(" ")
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

                    if (buliderAmount != null) {
                        Toast.makeText(this@PurchasedItemsActivity, "" + buliderAmount, Toast.LENGTH_SHORT).show()
                    }

                    if (buliderBarcode != null) {
                        Toast.makeText(this@PurchasedItemsActivity, "" + buliderBarcode, Toast.LENGTH_SHORT).show()
                    }

                    if (buliderDescription != null) {
                        Toast.makeText(this@PurchasedItemsActivity, "" + buliderDescription, Toast.LENGTH_SHORT).show()
                    }

                    if (buliderBrand != null) {
                        Toast.makeText(this@PurchasedItemsActivity, "" + buliderBrand, Toast.LENGTH_SHORT).show()
                    }

                    if (buliderSize != null) {
                        Toast.makeText(this@PurchasedItemsActivity, "" + buliderSize, Toast.LENGTH_SHORT).show()
                    }

                    if (buliderUnit != null) {
                        Toast.makeText(this@PurchasedItemsActivity, "" + buliderUnit, Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this@PurchasedItemsActivity, resources.getString(R.string.serverFaield), Toast.LENGTH_SHORT).show()
                    hideLoading()
                }
            }

            override fun onFailure(call: Call<PurchaseItemNoProductResult>, t: Throwable) {

                Toast.makeText(this@PurchasedItemsActivity, resources.getString(R.string.connectionFaield), Toast.LENGTH_SHORT).show()
                hideLoading()
            }
        })
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
        for (i in initMemberPrizeLists.data.member.indices) {
            for (j in 0 until initMemberPrizeLists.data.member[i].size) {
                members.add(Member(initMemberPrizeLists.data.member[i][j].name, initMemberPrizeLists.data.member[i][j].id))
            }
        }

//        for (i in registerModel.data.member.indices) {
//            for (j in 0 until registerModel.data.member[i].size) {
//                members.add(Member(registerModel.data.member[i][j].name, registerModel.data.member[i][j].id))
//            }
//        }

        val checkBoxAll = dialog.findViewById<CheckBox>(R.id.checkbox_all)
        val recyclerview_members = dialog.findViewById<RecyclerView>(R.id.recyclerview_members)
        val btn_exit_dialog = dialog.findViewById<Button>(R.id.btn_exit_dialog)
        val img_close = dialog.findViewById<ImageView>(R.id.img_close)
        recyclerview_members.layoutManager = LinearLayoutManager(this@PurchasedItemsActivity)
        _memberAdapter = RegisterMemberDialogAdapter(members, this@PurchasedItemsActivity)
        _memberAdapter.setListener(this) // important or else the app will crashed
        recyclerview_members.adapter = _memberAdapter


        // to select all members
        checkBoxAll.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                editMembers = ArrayList<RegisterMemberEditModel>()

                for (i in initMemberPrizeLists.data.member.indices) {
                    for (j in 0 until initMemberPrizeLists.data.member[i].size) {
                        editMembers.add(RegisterMemberEditModel(initMemberPrizeLists.data.member[i][j].name,
                                initMemberPrizeLists.data.member[i][j].id))
                    }
                }
//                for (i in registerModel.data.member.indices) {
//                    for (j in 0 until registerModel.data.member[i].size) {
//                        editMembers.add(RegisterMemberEditModel(registerModel.data.member[i][j].name,
//                                registerModel.data.member[i][j].id))
//                    }
//                }

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
        _editedAdapter = RegisterMemberEditAdapter(editMembers, this@PurchasedItemsActivity)
        recyclerEditedMember.adapter = _editedAdapter
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

        for (i in initMemberPrizeLists.data.prize.indices) {
            for (j in 0 until initMemberPrizeLists.data.prize[i].size) {
                prizes.add(Prize(initMemberPrizeLists.data.prize[i][j].title, initMemberPrizeLists.data.prize[i][j].id))
            }
        }
//        for (i in registerModel.data.prize.indices) {
//            for (j in 0 until registerModel.data.prize[i].size) {
//                prizes.add(Prize(registerModel.data.prize[i][j].title, registerModel.data.prize[i][j].id))
//            }
//        }


        val recycler_prize = dialog.findViewById<RecyclerView>(R.id.recycler_prize)
        val btn_exit_dialog = dialog.findViewById<Button>(R.id.btn_exit_dialog)
        val img_close = dialog.findViewById<ImageView>(R.id.img_close)
        recycler_prize.layoutManager = LinearLayoutManager(this@PurchasedItemsActivity)
        _prizeAdapter = PrizeAdapter(prizes, this@PurchasedItemsActivity)
        _prizeAdapter.setListener(this)  // important or else the app will crashed
        recycler_prize.adapter = _prizeAdapter

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
        editPrizeAdapter = EditPrizeAdapter(sendPrizes, this@PurchasedItemsActivity)
        recycler_prize.adapter = editPrizeAdapter
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

        when (view!!.id) {
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