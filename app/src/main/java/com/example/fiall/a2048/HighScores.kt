package com.example.fiall.a2048

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.example.fiall.a2048.dataBase.BD2048
import kotlinx.android.synthetic.main.activity_high_scores.*

class HighScores : AppCompatActivity() {

    private var db: BD2048? = null
    private var adapter: HighScoresAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_high_scores)

        db = BD2048.getInstance(this)

        db = adapter(db?.getGameDao()?.getScores()!!)

    }

    override fun onResume() {
        super.onResume()
        adapter?.listOfScores = db?.getGameDao()?.getScores()!!
        rvHighScores.adapter= adapter
        rvHighScores.layoutManager = LinearLayoutManager(this)
        rvHighScores.hasFixedSize()
    }
}
