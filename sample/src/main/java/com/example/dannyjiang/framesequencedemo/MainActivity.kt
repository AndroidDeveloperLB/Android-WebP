package com.example.dannyjiang.framesequencedemo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var player: WebpPlayer = WebpPlayer()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        webpImageView!!.setFinishedListener(object : WebpImageView.OnWebpFinishListener {
//
//            override fun onAnimationFinished() {
//            }
//
//            override fun onAnimationStart() {
//            }
//        })
        webpImageView!!.setAnimatedDrawable(resources.openRawResource(R.raw.ben_happy_talk_right))
//        webpImageView!!.setAnimatedDrawable(resources.openRawResource(R.raw.test))
        webpImageView!!.playAnimation()
//        button.setOnClickListener { webpImageView!!.playAnimation() }
//        val inputStream = resources.openRawResource(R.raw.ben_happy_talk_right)
//        val fs = FrameSequence.decodeStream(inputStream)
//        val state = fs.createState()
//        val bitmap = Bitmap.createBitmap(fs.width, fs.height, Bitmap.Config.ARGB_8888)
//        state!!.getFrame(0, bitmap, -1)
//        Log.d("AppLog", "frameCount:${fs.frameCount}")
//        Log.d("AppLog", "size:${fs.width}*${fs.height}")
//        imageView.setImageBitmap(bitmap)
//        fs.destroy()
//        player.listener = object : WebpPlayer.WebpListener {
//            override fun onGotFrame(bitmap: Bitmap, frame: Int, frameCount: Int) {
//                Log.d("AppLog", "onGotFrame:$frame/$frameCount")
//                imageView.setImageBitmap(bitmap)
//            }
//
//            override fun onError() {
//            }
//
//        }
//        player.start("/storage/emulated/0/Download/test.webp")
    }

    override fun onDestroy() {
        super.onDestroy()
        player.stop()
    }


//    override fun onDestroy() {
//        super.onDestroy()
//        webpImageView!!.destroy()
//    }
}
