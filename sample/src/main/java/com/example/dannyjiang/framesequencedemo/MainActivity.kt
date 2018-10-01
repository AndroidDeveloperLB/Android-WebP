package com.example.dannyjiang.framesequencedemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.danny.framesSquencce.WebpImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        webpImageView!!.setFinishedListener(object : WebpImageView.OnWebpFinishListener {

            override fun onAnimationFinished() {
            }

            override fun onAnimationStart() {
            }
        })
        webpImageView!!.setAnimatedDrawable(resources.openRawResource(R.raw.ben_happy_talk_right))
        button.setOnClickListener { webpImageView!!.playAnimation() }
    }


    override fun onDestroy() {
        super.onDestroy()
        webpImageView!!.destroy()
    }
}
