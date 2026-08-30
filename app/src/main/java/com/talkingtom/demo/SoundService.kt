package com.talkingtom.demo

import android.app.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import androidx.core.app.NotificationCompat

class SoundService : Service() {
    private var player: MediaPlayer? = null
    private var handler = Handler(Looper.getMainLooper())
    private var volume = 0.2f
    private val runnable = object: Runnable{
        override fun run(){
            volume = (volume + 0.08f).coerceAtMost(1.0f)
            player?.setVolume(volume, volume)
            handler.postDelayed(this, 1800)
        }
    }
    override fun onCreate(){
        super.onCreate()
        val channel = NotificationChannel("tom", "Talking Tom", NotificationManager.IMPORTANCE_LOW)
        (getSystemService(NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(channel)
        val notif = NotificationCompat.Builder(this,"tom").setContentTitle("Talking Tom").setContentText("Som em segundo plano").setSmallIcon(R.mipmap.ic_launcher).build()
        startForeground(1, notif)
        player = MediaPlayer.create(this, R.raw.background_sound).apply{ isLooping=true; setVolume(volume,volume); start() }
        handler.postDelayed(runnable, 1800)
    }
    override fun onBind(intent: Intent?) = null
    override fun onDestroy(){ handler.removeCallbacks(runnable); player?.stop(); player?.release(); super.onDestroy() }
}
