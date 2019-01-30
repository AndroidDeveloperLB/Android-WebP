package com.example.dannyjiang.framesequencedemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var url: String? = null
        when (item.itemId) {
            R.id.menuItem_all_my_apps -> url = "https://play.google.com/store/apps/developer?id=AndroidDeveloperLB"
            R.id.menuItem_all_my_repositories -> url = "https://github.com/AndroidDeveloperLB"
            R.id.menuItem_current_repository_website -> url = "https://github.com/AndroidDeveloperLB/Android-WebP"
        }
        if (url == null)
            return true
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY or Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        startActivity(intent)
        return true
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        webpImageView!!.destroy()
//    }
}
