package com.example.fiall.a2048

import android.app.Dialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val alert = AlertDialog.Builder(this)
//
//        alert.setTitle(getString(R.string.en_AlertDialogTitleMain))
//
//        alert.


        ibtnRight.setOnClickListener {
            changeTextRight()
        }

        ibtnLeft.setOnClickListener {
            changeTextLeft()
        }

        btnStartGame.setOnClickListener {
            openActivity()
        }

        btnHighScores.setOnClickListener {
            val highScores = Intent(this, HighScores::class.java)
            startActivity(highScores)
        }
        ibtnSettings.setOnClickListener {
            val settings = Intent(this, Settings::class.java)
            startActivity(settings)
        }
    }

    private fun changeTextRight() {
        val txt = findViewById<TextView>(R.id.tvGameType)
        val img = findViewById<ImageView>(R.id.ivGameType)

        when {
            txt.text == getString(R.string.en_TxT) -> {
                txt.text = getString(R.string.en_FxF)
                img.setImageResource(R.drawable.d4x4)
            }
            txt.text == getString(R.string.en_FxF) -> {
                txt.text = getString(R.string.en_FxF)
                img.setImageResource(R.drawable.d6x6)
            }
            txt.text == getString(R.string.en_SxS) -> {
                txt.text = getString(R.string.en_ExE)
                img.setImageResource(R.drawable.d8x8)
            }
            else -> {
                txt.text = getString(R.string.en_TxT)
                img.setImageResource(R.drawable.d3x3)
            }
        }
    }

    private fun changeTextLeft() {
        val txt = findViewById<TextView>(R.id.tvGameType)
        val img = findViewById<ImageView>(R.id.ivGameType)

        when {
            txt.text == getString(R.string.en_FxF) -> {
                txt.text = getString(R.string.en_TxT)
                img.setImageResource(R.drawable.d3x3)
            }
            txt.text == getString(R.string.en_SxS) -> {
                txt.text =getString(R.string.en_FxF)
                img.setImageResource(R.drawable.d4x4)
            }
            txt.text == getString(R.string.en_ExE) -> {
                txt.text = getString(R.string.en_SxS)
                img.setImageResource(R.drawable.d6x6)
            }
            else -> {
                txt.text = getString(R.string.en_ExE)
                img.setImageResource(R.drawable.d8x8)
            }
        }
    }

    private fun openActivity() {
        val txt = findViewById<TextView>(R.id.tvGameType)
        val three = Intent(this, ThreeTimesThree::class.java)
        val four = Intent(this, FourTimesFour::class.java)
        val six = Intent(this, SixTimesSix::class.java)
        val eight = Intent(this, EightTimesEight::class.java)

        when {
            txt.text == getString(R.string.en_TxT) || txt.text == getString(R.string.es_TxT) -> startActivity(three)
            txt.text == getString(R.string.en_FxF) || txt.text == getString(R.string.es_FxF) -> startActivity(four)
            txt.text == getString(R.string.en_SxS) || txt.text == getString(R.string.es_SxS) -> startActivity(six)
            else -> startActivity(eight)
        }
    }
}
