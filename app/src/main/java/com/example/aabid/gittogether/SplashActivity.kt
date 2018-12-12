package com.example.aabid.gittogether

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_splash.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        animation()
    }

    private fun animation() {
        val animShop = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.photo_anim)
        val animWord = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.word_anim)

        ivPhoto.startAnimation(animShop)
        tvTitle.startAnimation(animWord)

        animShop.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationRepeat(animation: Animation?) {

            }

            override fun onAnimationEnd(animation: Animation?) {
                var intentStart = Intent()
                intentStart.setClass((this@SplashActivity),
                    MainActivity::class.java)
                startActivity(intentStart)
                finish()
            }

            override fun onAnimationStart(animation: Animation?) {

            }
        })
    }
}
