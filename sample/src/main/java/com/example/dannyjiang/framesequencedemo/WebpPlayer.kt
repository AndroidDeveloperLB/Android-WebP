package com.example.dannyjiang.framesequencedemo

import android.graphics.Bitmap
import android.os.Handler
import android.os.HandlerThread
import android.os.Looper
import android.support.annotation.UiThread
import com.danny.framesSquencce.FrameSequence
import java.io.File
import java.io.FileInputStream

open class WebpPlayer {
    private val uiHandler = Handler(Looper.getMainLooper())
    private var playerHandlerThread: HandlerThread? = null
    private var playerHandler: Handler? = null
    private val webpDecoder: WebpDecoder = WebpDecoder()

    private var currentFrame: Int = -1
    var listener: WebpListener? = null
    var state: State = State.IDLE
        private set
    private val playRunnable: Runnable

    enum class State {
        IDLE, PAUSED, PLAYING, RECYCLED, ERROR
    }

    interface WebpListener {
        fun onGotFrame(bitmap: Bitmap, frame: Int, frameCount: Int)

        fun onError()
    }

    init {
        playRunnable = object : Runnable {
            override fun run() {
                val frameCount = webpDecoder.frameCount
                currentFrame = (currentFrame + 1) % (frameCount)
                webpDecoder.currentFrame = currentFrame
                val bitmap = webpDecoder.bitmap!!
                val delay = webpDecoder.decodeNextFrame()
                uiHandler.post {
                    listener?.onGotFrame(bitmap, currentFrame, frameCount)
                    if (state == State.PLAYING)
                        playerHandler!!.postDelayed(this, delay)
                }
            }
        }
    }

    @Suppress("unused")
    protected fun finalize() {
        stop()
    }

    @UiThread
    fun start(filePath: String): Boolean {
        if (state != State.IDLE && state != State.ERROR)
            return false
        currentFrame = -1
        state = State.PLAYING
        playerHandlerThread = HandlerThread("WebPPlayer")
        playerHandlerThread!!.start()
        val looper = playerHandlerThread!!.looper
        playerHandler = Handler(looper)
        playerHandler!!.post {
            try {
                webpDecoder.load(filePath)
            } catch (e: Exception) {
                uiHandler.post {
                    state = State.ERROR
                    listener?.onError()
                }
                return@post
            }

            val bitmap = webpDecoder.bitmap
            if (bitmap != null) {
                playRunnable.run()
            } else {
                webpDecoder.recycle()
                uiHandler.post {
                    state = State.ERROR
                    listener?.onError()
                }
                return@post
            }
        }
        return true
    }

    @UiThread
    fun stop(): Boolean {
        if (state == State.IDLE)
            return false
        state = State.IDLE
        playerHandler!!.removeCallbacks(playRunnable)
        playerHandlerThread!!.quit()
        playerHandlerThread = null
        playerHandler = null
        return true
    }

    @UiThread
    fun pause(): Boolean {
        if (state != State.PLAYING)
            return false
        state = State.PAUSED
        playerHandler?.removeCallbacks(playRunnable)
        return true
    }

    @UiThread
    fun resume(): Boolean {
        if (state != State.PAUSED)
            return false
        state = State.PLAYING
        playerHandler?.removeCallbacks(playRunnable)
        playRunnable.run()
        return true
    }

    @UiThread
    fun toggle(): Boolean {
        when (state) {
            State.PLAYING -> pause()
            State.PAUSED -> resume()
            else -> return false
        }
        return true
    }

    class WebpDecoder {
        val minDelayBetweenFrames=1000L/40L
//        val minDelayBetweenFrames=100L

        var frameSequence: FrameSequence? = null
        val frameCount: Int
            get() = frameSequence?.frameCount ?: 0
        var bitmap: Bitmap? = null
        var currentFrame = 0

        private var state: FrameSequence.State? = null

        fun load(filePath: String) {
            frameSequence = FrameSequence.decodeStream(FileInputStream(File(filePath)))
            bitmap = Bitmap.createBitmap(frameSequence!!.width, frameSequence!!.height, Bitmap.Config.ARGB_8888)
            state = frameSequence!!.createState()
        }

        fun recycle() {
            frameSequence?.destroy()
            frameSequence = null
        }

        fun decodeNextFrame(): Long {
            val delay = state!!.getFrame(currentFrame , bitmap, currentFrame-1)
            return Math.max(delay, minDelayBetweenFrames)
//            return if (delay > 0) delay else 100L
        }

    }
}
