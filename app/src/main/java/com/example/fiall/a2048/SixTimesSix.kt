package com.example.fiall.a2048

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.MotionEvent
import android.widget.Chronometer
import android.widget.TextView
import android.widget.Toast
import com.example.fiall.a2048.Tiles6.Companion.movimientosRestantes
import com.example.fiall.a2048.dataBase.BD2048
import com.example.fiall.a2048.dataBase.tablesDB.Scores
import kotlinx.android.synthetic.main.activity_six_times_six.*

class SixTimesSix : AppCompatActivity() {

    companion object {
        private const val GAME_KEY = "2048_GAME_KEY"
    }

    private lateinit var game: Tiles6
    private var x1 = 0f
    private var y1 = 0f
    private var cells = IntArray(36)
    private lateinit var cronometer: Chronometer
    val gameZ = BD2048.getInstance(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_six_times_six)

        cronometer = findViewById(R.id.chronometer)

        cells = IntArray(36)
        cells[0] = R.id.Pos0
        cells[1] = R.id.Pos1
        cells[2] = R.id.Pos2
        cells[3] = R.id.Pos3
        cells[4] = R.id.Pos4
        cells[5] = R.id.Pos5
        cells[6] = R.id.Pos6
        cells[7] = R.id.Pos7
        cells[8] = R.id.Pos8
        cells[9] = R.id.Pos9
        cells[10] = R.id.Pos10
        cells[11] = R.id.Pos11
        cells[12] = R.id.Pos12
        cells[13] = R.id.Pos13
        cells[14] = R.id.Pos14
        cells[15] = R.id.Pos15
        cells[16] = R.id.Pos16
        cells[17] = R.id.Pos17
        cells[18] = R.id.Pos18
        cells[19] = R.id.Pos19
        cells[20] = R.id.Pos20
        cells[21] = R.id.Pos21
        cells[22] = R.id.Pos22
        cells[23] = R.id.Pos23
        cells[24] = R.id.Pos24
        cells[25] = R.id.Pos25
        cells[26] = R.id.Pos26
        cells[27] = R.id.Pos27
        cells[28] = R.id.Pos28
        cells[29] = R.id.Pos29
        cells[30] = R.id.Pos30
        cells[31] = R.id.Pos31
        cells[32] = R.id.Pos32
        cells[33] = R.id.Pos33
        cells[34] = R.id.Pos34
        cells[35] = R.id.Pos35

        if (savedInstanceState != null) {
            val tmpGame :Tiles6? = savedInstanceState.getSerializable(GAME_KEY) as Tiles6
            if (tmpGame != null) {
                this.game = tmpGame
                game.rePoblar()
            }
        } else this.game = Tiles6()
        dibujarCuadros()

        cronometer.start()
    }

/*
fun onClick() {
val intent = Intent(this, MainActivity::class.java)
startActivity(intent)
}
*/

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putSerializable(GAME_KEY, game)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        if (savedInstanceState != null) {
            val tmpGame :Tiles6? = savedInstanceState.getSerializable(GAME_KEY) as Tiles6
            if (tmpGame != null) {
                game = tmpGame
                game.rePoblar()
            }
        }
        dibujarCuadros()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            // obtener coordenadas
            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
                y1 = event.y
            }
            MotionEvent.ACTION_UP -> {
                val x2 = event.x
                val y2 = event.y

                val minDistance = 200
                var moved = false

                //Swipe izquierda a derecha
                if (x1 < x2 && x2 - x1 > minDistance) {
                    moved = game.accion(Movimientos.Right)
                }

                // Swipe derecha a izquierda
                if (x1 > x2 && x1 - x2 > minDistance) {
                    moved = game.accion(Movimientos.Left)
                }

                // Swipe arriba hacia abajo
                if (y1 < y2 && y2 - y1 > minDistance) {
                    moved = game.accion(Movimientos.Down)
                }

                //Swipe abajo hacia arriba
                if (y1 > y2 && y1 - y2 > minDistance) {
                    moved = game.accion(Movimientos.Up)
                }

                if (moved) dibujarCuadros() // dibujar el cuadro

            }
        }
        return super.onTouchEvent(event)
    }

    private fun dibujarCuadros() {

        movimientosRestantes -= 1
        tvRemainingMovesNumber.text = movimientosRestantes.toString()

        val moves = tvRemainingMovesNumber.text
        val tiempo = chronometer.text
        val puntos = tvScoreNumber.text
        val nivel = "3x3"
        val user = LoginActivity.actualUser

        if (game.maxTile >= Tiles6.TARGET) {
            Toast.makeText(this, resources.getString(R.string.en_Winner) +
                    game.maxTile, Toast.LENGTH_LONG).show()

        } else if (!game.fusionesRestantes() || movimientosRestantes.toString().toInt() == 0) {
            // aqui se tiene que guardar los movimientos restantes y el tiempo

            cronometer.stop()

            val score = Scores(user.toString(), nivel, puntos.toString().toInt(), moves.toString().toInt(), tiempo.toString())
            gameZ?.getGameDao()?.saveScore(score)

            val alert = AlertDialog.Builder(this)

            alert.setTitle(getString(R.string.en_AlertDialogTilesTitle))
            alert.setMessage(getString(R.string.en_AlertDialogMessage))

            alert.setPositiveButton(getString(R.string.en_Yes)) { _, _ ->

                val intent = intent
                finish()
                startActivity(intent)
            }

            alert.setNegativeButton(getString(R.string.en_No)) { _, _ ->

                finish()
            }

            val dialog: AlertDialog = alert.create()

            dialog.show()

            movimientosRestantes = 7
        }

        Log.i("Logm", game.toString())
        Log.i("Logi", R.id.tvScoreNumber.toString())
        val tv: TextView = this.findViewById(R.id.tvScoreNumber)
        tv.text = StringBuffer(resources.getString(R.string.zero)).append(game.score)

        val transitions = game.transitions
        if (transitions.size == 0) return

        for (trans in transitions) {
            laTransicion(trans)
        }
    }

    @SuppressLint("NewApi")
    private fun elCuadro(obj: Any, value: Int) {

        val tv = obj as TextView

        if (value == 0) {
            tv.text = ""
        } else {
            tv.text = StringBuffer("").append(value)
        }

        var txCol = resources.getColor(R.color.colorTileTextDark, null)
        var bgCol = resources.getColor(R.color.color0, null)

        when (value) {
            2 -> bgCol = resources.getColor(R.color.color2, null)
            4 -> bgCol = resources.getColor(R.color.color4, null)
            8 -> {
                txCol = resources.getColor(R.color.colorTileTextLight, null)
                bgCol = resources.getColor(R.color.color8, null)
            }
            16 -> {
                txCol = resources.getColor(R.color.colorTileTextLight, null)
                bgCol = resources.getColor(R.color.color16, null)
            }
            32 -> {
                txCol = resources.getColor(R.color.colorTileTextLight, null)
                bgCol = resources.getColor(R.color.color32, null)
            }
            64 -> {
                txCol = resources.getColor(R.color.colorTileTextLight, null)
                bgCol = resources.getColor(R.color.color64, null)
            }
            128 -> {
                txCol = resources.getColor(R.color.colorTileTextLight, null)
                bgCol = resources.getColor(R.color.color128, null)
            }
            256 -> bgCol = resources.getColor(R.color.color256, null)
            512 -> bgCol = resources.getColor(R.color.color512, null)
            1024 -> bgCol = resources.getColor(R.color.color1024, null)
            2048 -> bgCol = resources.getColor(R.color.color2048, null)
            else -> {
            }
        }
        tv.setBackgroundColor(bgCol)
        tv.setTextColor(txCol)
    }

    private fun laTransicion(trans: Tiles6.Transition) {

        val tv = findViewById<TextView>(cells[trans.newLocation])

        if (trans.type == Acciones.Compact) {
            //Log.i("Logm", "About to do paint compact")
            //Log.i("Logm", "About to do paint compact w=$w h=$h t=$t")
            try {
                elCuadro(tv, trans.value)
                //Thread.sleep(100)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                elCuadro(tv, trans.value)
            }
        } else {
            elCuadro(tv, trans.value)
        }
    }
}
