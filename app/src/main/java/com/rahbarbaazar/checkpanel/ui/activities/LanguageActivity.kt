package com.rahbarbaazar.checkpanel.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.rahbarbaazar.checkpanel.R
import com.rahbarbaazar.checkpanel.utilities.CustomBaseActivity
import com.rahbarbaazar.checkpanel.utilities.LocaleManager
import kotlinx.android.synthetic.main.activity_language.*

class LanguageActivity : CustomBaseActivity(), View.OnClickListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_language)

        btn_persian.setOnClickListener(this)
        btn_english.setOnClickListener(this)

    }

    override fun onClick(v: View?) {

        val id = v!!.id
        when (id) {
            R.id.btn_persian -> {
                LocaleManager.setNewLocale(this@LanguageActivity, "fa")
                startActivity(Intent(this@LanguageActivity,LoginActivity::class.java))
            }
            R.id.btn_english -> {
                LocaleManager.setNewLocale(this@LanguageActivity, "en")
                startActivity(Intent(this@LanguageActivity,LoginActivity::class.java))
            }
        }
    }
}
