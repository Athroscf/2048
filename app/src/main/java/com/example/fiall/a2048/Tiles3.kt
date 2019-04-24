package com.example.fiall.a2048

import java.io.Serializable
import java.util.ArrayList
import java.util.Random

class Tiles3 internal constructor() : Serializable {

    companion object {
        internal const val TARGET = 2048
        private const val GRID_COUNT = 9
        private const val ROW_COUNT = 3
        private const val COL_COUNT = 3
        private const val ESPACIO = 0
        var movimientosRestantes = 15
    }

    var score = 0
        private set
    private var numEmpty = 9
    var maxTile = 0
        private set

    lateinit var transitions: ArrayList<Transition>
        private set
    private val tiles = IntArray(9)
    private val rand = Random()

    init {
        this.transiciones()
        this.otroCuadrito()
        this.otroCuadrito()
    }

    private fun transiciones() {
        transitions = ArrayList()
    }

    fun rePoblar() {
        this.transiciones()
        for (i in 0 until GRID_COUNT) {
            this.transitions.add(Transition(Acciones.Refresh, tiles[i], i))
        }
    }

    private fun otroCuadrito() {
        if (numEmpty == 0) return

        val value = (rand.nextInt(2) + 1) * 2
        val pos = rand.nextInt(numEmpty)
        var blanks = 0

        for (i in 0 until GRID_COUNT) {
            if (tiles[i] == ESPACIO) {
                if (blanks == pos) {
                    tiles[i] = value
                    if (value > maxTile) maxTile = value
                    numEmpty--
                    transitions.add(Transition(Acciones.Add, value, i))
                    return
                }
                blanks++
            }
        }
    }

    fun getValue(i: Int): Int {
        return tiles[i]
    }

    fun fusionesRestantes(): Boolean {

        if (numEmpty > 0) return true

        // verificar si hay fusiones horizontales
        var limite = (GRID_COUNT - COL_COUNT) - 1
        for (i in 0..limite) {
            if (tiles[i] == tiles[i + COL_COUNT]) return true
        }

        // verificar si hay fusiones verticales
        limite = GRID_COUNT - 2
        for (i in 0..limite) {
            if ((i + 1) % ROW_COUNT > 0) {
                if (tiles[i] == tiles[i + 1]) return true
            }
        }
        return false
    }

    // Funcion que valida el movimiento de un cuadro
    private fun deslizarCuadro(index1: Int, index2: Int, index3: Int): Boolean {

        var moved = false
        val tmpArr = intArrayOf(index1, index2, index3)

        // Deslizar o no?
        var es = 0  // empty spot index
        for (j in 0 until tmpArr.size) {
            if (tiles[tmpArr[es]] != ESPACIO) {
                es++
                continue
            } else if (tiles[tmpArr[j]] == ESPACIO) {
                continue
            } else {
                // Otherwise we have a slide condition
                tiles[tmpArr[es]] = tiles[tmpArr[j]]
                tiles[tmpArr[j]] = ESPACIO
                transitions.add(Transition(Acciones.Slide, tiles[tmpArr[es]], tmpArr[es], tmpArr[j]))
                transitions.add(Transition(Acciones.Blank, ESPACIO, tmpArr[j]))
                moved = true
                es++
            }
        }
        return moved
    }

    // Clase que valida la union de dos cuadros
    private fun unirCuadro(index1: Int, index2: Int, index3: Int): Boolean {

        var fusion = false
        val tmpArr = intArrayOf(index1, index2, index3)

        for (j in 0..(tmpArr.size-2)) {

            if (tiles[tmpArr[j]] != ESPACIO && tiles[tmpArr[j]] == tiles[tmpArr[j+1]]) { // we found a matching pair
                val ctv = tiles[tmpArr[j]] * 2   // = compacted tile value
                tiles[tmpArr[j]] = ctv
                tiles[tmpArr[j+1]] = ESPACIO
                score += ctv
                if (ctv > maxTile) {
                    maxTile = ctv
                }  // is this the biggest tile # so far
                transitions.add(Transition(Acciones.Compact, ctv, tmpArr[j], tmpArr[j+1]))
                transitions.add(Transition(Acciones.Blank, ESPACIO, tmpArr[j+1]))
                fusion = true
                numEmpty++
            }
        }
        return fusion
    }

    fun accion(move: Movimientos): Boolean {
        this.transiciones()

        if (!fusionesRestantes()) return false

        val result = when (move) {
            Movimientos.Up -> moverArriba()
            Movimientos.Down -> moverAbajo()
            Movimientos.Right -> moverDerecha()
            Movimientos.Left -> moverIzquierda()
        }

        if (result) {
            otroCuadrito()
        }
        return result
    }


    //Definicion de los posibles movimientos
    private fun deslizarArriba(): Boolean {
        val a = deslizarCuadro(0, 3, 6)
        val b = deslizarCuadro(1, 4, 7)
        val c = deslizarCuadro(2, 5, 8)
        return a || b || c
    }

    private fun deslizarAbajo(): Boolean {
        val a = deslizarCuadro(8, 5, 2)
        val b = deslizarCuadro(7, 4, 1)
        val c = deslizarCuadro(6, 3, 0)
        return a || b || c
    }

    private fun deslizarDerecha(): Boolean {
        val a = deslizarCuadro(2, 1, 0)
        val b = deslizarCuadro(5, 4, 3)
        val c = deslizarCuadro(8, 7, 6)
        return a || b || c
    }

    private fun deslizarIzquierda(): Boolean {
        val a = deslizarCuadro(0, 1, 2)
        val b = deslizarCuadro(3, 4, 5)
        val c = deslizarCuadro(6, 7, 8)
        return a || b || c
    }

    private fun fusionArriba(): Boolean {
        val a = unirCuadro(0, 3, 6)
        val b = unirCuadro(1, 4, 7)
        val c = unirCuadro(2, 5, 8)
        return a || b || c
    }

    private fun fusionAbajo(): Boolean {
        val a = unirCuadro(8, 5, 2)
        val b = unirCuadro(7, 4, 1)
        val c = unirCuadro(6, 3, 0)
        return a || b || c
    }

    private fun fusionDerecha(): Boolean {
        val a = unirCuadro(0, 1, 2)
        val b = unirCuadro(3, 4, 5)
        val c = unirCuadro(6, 7, 8)
        return a || b || c
    }

    private fun fusionIzquierda(): Boolean {
        val a = unirCuadro(0, 1, 2)
        val b = unirCuadro(3, 4, 5)
        val c = unirCuadro(6, 7, 8)
        return a || b || c
    }

    private fun moverArriba(): Boolean {
        val a = deslizarArriba()
        val b = fusionArriba()
        val c = deslizarArriba()
        return a || b || c
    }

    private fun moverAbajo(): Boolean {
        val a = deslizarAbajo()
        val b = fusionAbajo()
        val c = deslizarAbajo()
        return a || b || c
    }

    private fun moverDerecha(): Boolean {
        val a = deslizarDerecha()
        val b = fusionDerecha()
        val c = deslizarDerecha()
        return a || b || c
    }

    private fun moverIzquierda(): Boolean {
        val a = deslizarIzquierda()
        val b = fusionIzquierda()
        val c = deslizarIzquierda()
        return a || b || c
    }

    override fun toString(): String {
        return "NewTiles:class\n" +
                "--------------------\n" +
                "|" + tiles[0] + "|" + tiles[1] + "|" + tiles[2] + "|" + "\n" +
                "|" + tiles[3] + "|" + tiles[4] + "|" + tiles[5] + "|" + "\n" +
                "|" + tiles[6] + "|" + tiles[7] + "|" + tiles[8] + "|" + "\n" +
                "--------------------\n"
    }
}
