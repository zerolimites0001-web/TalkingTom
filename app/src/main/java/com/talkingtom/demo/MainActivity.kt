package com.talkingtom.demo

import android.app.WallpaperManager
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.animation.*
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.btnSim).setOnClickListener { askPermission(true) }
        findViewById<Button>(R.id.btnNao).setOnClickListener { doNao() }
    }
    private fun askPermission(isSim:Boolean){
        AlertDialog.Builder(this).setTitle("Permissão").setMessage("Você aceita que o app modifique seu telefone? (trocar wallpaper e tocar som em segundo plano)")
            .setPositiveButton("ACEITAR"){_,_-> if(isSim) doSim() } .setNegativeButton("NEGAR",null).show()
    }
    private fun doSim(){
        try {
            val wm = WallpaperManager.getInstance(this)
            val bmp = BitmapFactory.decodeResource(resources, R.drawable.wallpaper)
            wm.setBitmap(bmp)
            Toast.makeText(this,"Wallpaper trocado!",Toast.LENGTH_SHORT).show()
        } catch(e:Exception){ Toast.makeText(this,"Erro wallpaper: ${e.message}",Toast.LENGTH_SHORT).show() }
        val intent = Intent(this, SoundService::class.java)
        ContextCompat.startForegroundService(this, intent)
        Toast.makeText(this,"Som em segundo plano ativado 🔊",Toast.LENGTH_SHORT).show()
    }
    private fun doNao(){
        val root = findViewById<android.view.View>(R.id.root)
        val shake = TranslateAnimation(-30f,30f,-15f,15f).apply{ duration=80; repeatCount=20; repeatMode=Animation.REVERSE; interpolator=CycleInterpolator(3f) }
        root.startAnimation(shake)
        try{ val v=getSystemService(VIBRATOR_SERVICE) as android.os.Vibrator; v.vibrate(longArrayOf(0,200,100,300),-1)}catch(e:Exception){}
        Toast.makeText(this,"...",Toast.LENGTH_SHORT).show()
    }
}
