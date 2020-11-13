package com.rahbarbazaar.homadit.android.ui.activities

import android.os.Bundle
import com.rahbarbazaar.homadit.android.R
import com.rahbarbazaar.homadit.android.utilities.CustomBaseActivity
import kotlinx.android.synthetic.main.activity_photo_guide.*

class PhotoGuideActivity : CustomBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_guide)

       val type = intent.getStringExtra("type")

        if(type == "factor1"){
            img_photo_guide.setImageResource(R.drawable.factor_img_top)
        }else if(type == "factor3"){
            img_photo_guide.setImageResource(R.drawable.factor_img_bottom)
        }else if(type == "img1"){
            img_photo_guide.setImageResource(R.drawable.img_guid_photo1)
        }else if(type == "img2"){
            img_photo_guide.setImageResource(R.drawable.img_guid_photo2)
        }else if(type == "img3"){
            img_photo_guide.setImageResource(R.drawable.img_guid_photo3)
        }else if(type == "img4"){
            img_photo_guide.setImageResource(R.drawable.img_guid_photo4)
        }

        txt_close_photo.setOnClickListener {
            finish()
        }
    }
}