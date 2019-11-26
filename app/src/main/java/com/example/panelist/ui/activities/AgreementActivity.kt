package com.example.panelist.ui.activities

import android.content.Intent
import android.os.Bundle
import com.example.panelist.R
import com.example.panelist.utilities.CustomBaseActivity
import kotlinx.android.synthetic.main.activity_agreement.*

class AgreementActivity : CustomBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement)

        txt.setOnClickListener {
            startActivity(Intent(this@AgreementActivity,MainActivity::class.java))
            finish()
        }

    }
}
