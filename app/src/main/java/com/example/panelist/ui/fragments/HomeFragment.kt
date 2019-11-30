package com.example.panelist.ui.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.panelist.network.ServiceProvider
import com.example.panelist.utilities.ClientConfig
import com.facebook.drawee.backends.pipeline.Fresco
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_home.view.*
import io.reactivex.disposables.Disposable
import com.example.panelist.models.dashboard.DashboardModel
import com.example.panelist.utilities.RxBus

import com.squareup.picasso.Picasso



class HomeFragment : Fragment(), View.OnClickListener {


    lateinit var provider: ServiceProvider
    var disposable: Disposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        Fresco.initialize(context)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(com.example.panelist.R.layout.fragment_home, container, false)




        setContent(view)

        return view
    }

    private fun setContent(view: View) {


        Picasso.get().load(ClientConfig.Html_imgURL+"files/news.png").fit().centerCrop().into(view.img_news)
        Picasso.get().load(ClientConfig.Html_imgURL+"files/video.png").fit().centerCrop().into(view.img_video)
        Picasso.get().load(ClientConfig.Html_imgURL+"files/myshop.png").fit().centerCrop().into(view.img_myshop)


//        context?.let {
//            Glide.with(it)
//                    .load(ClientConfig.Html_imgURL + "files/news.png")
//                    .centerCrop()
//                    .into(view.img_news)
//
//        }


//        view.img_video.setImageURI(ClientConfig.Html_imgURL + "files/video.png")
//        view.img_myshop.setImageURI(ClientConfig.Html_imgURL + "files/myshop.png")


        disposable = RxBus.subscribe { result ->
            if (result is DashboardModel) {
                view.txt_balance_digit.text = result.data.one.toString()
                view.txt_incomplete_purchase_digit.text = result.data.two.toString()
                view.txt_total_purchase_digit.text = result.data.three.toString()
                view.txt_registered_products_digit.text = result.data.four.toString()
            }
        }


    }


    override fun onClick(v: View) {

    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose();
    }


}

