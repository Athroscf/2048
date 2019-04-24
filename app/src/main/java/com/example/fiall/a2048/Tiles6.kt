package com.example.fiall.a2048

import java.io.Serializable
import java.util.ArrayList
import java.util.Random

class Tiles6 internal constructor() : Serializable {

    companion object {
        internal const val TARGET = 2048
        private const val GRID_COUNT = 36
        private const val ROW_COUNT = 6
        private const val COL_COUNT = 6
        private const val ESPACIO = 0
        var movimientosRestantes = 7
    }

    var score = 0
        private set
    private var numEmpty = 36
    var maxTile = 0
        private set

    lateinit var transitions: ArrayList<Transition>
        private set
    private val tiles = IntArray(36)
    private val rand = Random()

    init {
        this.transiciones()
        this.otroCuadrito()
        this.otroCuadrito()
    }

    inner class Transition : Serializable {

        var type: Acciones
        var value = 0
        var newLocation = -1
        private var oldLocation = -1

        constructor(action: Acciones, value: Int, newLocation: Int, oldLocation: Int) {
            type = action
            this.value = value
            this.newLocation = newLocation
            this.oldLocation = oldLocation
        }

        constructor(action: Acciones, value: Int, newLocation: Int) {
            type = action
            this.value = value
            this.newLocation = newLocation
        }
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

    // fusiones restantes
    fun fusionesRestantes(): Boolean {

        if (numEmpty > 0) return true

        // check left-right for compact moves remaining.
        var limite = (GRID_COUNT - COL_COUNT) - 1
        for (i in 0..limite) {
            if (tiles[i] == tiles[i + COL_COUNT]) return true
        }

        // check up-down for compact moves remaining.
        limite = GRID_COUNT - 2
        for (i in 0..limite) {
            if ((i + 1) % ROW_COUNT > 0) {
                if (tiles[i] == tiles[i + 1]) return true
            }
        }
        return false
    }

    // Funcion que valida si un cuadro se mueve o no
    private fun deslizarCuadro(index1: Int, index2: Int, index3: Int, index4: Int, index5: Int, index6: Int): Boolean {

        var moved = false
        val tmpArr = intArrayOf(index1, index2, index3, index4, index5, index6)

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
    private fun unirCuadro(index1: Int, index2: Int, index3: Int, index4: Int, index5: Int, index6: Int): Boolean {

        var fusion = false
        val tmpArr = intArrayOf(index1, index2, index3, index4, index5, index6)

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
        val a = deslizarCuadro(0, 6, 12, 18, 24, 30)
        val b = deslizarCuadro(1, 7, 13, 19, 25,31)
        val c = deslizarCuadro(2, 8, 14, 20, 26,32)
        val d = deslizarCuadro(3, 9, 15, 21, 27,33)
        val e = deslizarCuadro(4, 10, 16, 22, 28,34)
        val f = deslizarCuadro(5, 11, 17, 23, 29,35)
        return a || b || c || d || e || f
    }

    private fun deslizarAbajo(): Boolean {
        val a = deslizarCuadro(35,29,23,17, 11, 5)
        val b = deslizarCuadro(34,28,22,16, 10, 4)
        val c = deslizarCuadro(33,27,21,15, 9, 3)
        val d = deslizarCuadro(32,26,20,14, 8, 2)
        val e = deslizarCuadro(31,25,19,13, 7, 1)
        val f = deslizarCuadro(30,24,18,12, 6, 0)
        return a || b || c || d || e || f
    }

    private fun deslizarDerecha(): Boolean {
        val a = deslizarCuadro(5,4,3,2, 1, 0)
        val b = deslizarCuadro(11,10,9,8, 7, 6)
        val c = deslizarCuadro(17,16,15,14, 13, 12)
        val d = deslizarCuadro(23,22,21, 20, 19, 18)
        val e = deslizarCuadro(29,28,27, 26, 25, 24)
        val f = deslizarCuadro(35,34,33, 32, 31, 30)
        return a || b || c || d || e || f
    }

    private fun deslizarIzquierda(): Boolean {
        val a = deslizarCuadro(0, 1, 2, 3, 4, 5)
        val b = deslizarCuadro(6, 7, 8, 9,10,11)
        val c = deslizarCuadro(12, 13, 14,15, 16, 17)
        val d = deslizarCuadro(18, 19, 20,21, 22, 23)
        val e = deslizarCuadro(24, 25, 26,27, 28,29)
        val f = deslizarCuadro(30, 31, 32,33, 34,35)
        return a || b || c || d || e || f
    }

    private fun fusionArriba(): Boolean {
        val a = unirCuadro(0, 6, 12, 18, 24, 30)
        val b = unirCuadro(1, 7, 13, 19, 25,31)
        val c = unirCuadro(2, 8, 14, 20, 26,32)
        val d = unirCuadro(3, 9, 15, 21, 27,33)
        val e = unirCuadro(4, 10, 16, 22, 28,34)
        val f = unirCuadro(5, 11, 17, 23, 29,35)
        return a || b || c || d || e || f
    }

    private fun fusionAbajo(): Boolean {
        val a = unirCuadro(35,29,23,17, 11, 5)
        val b = unirCuadro(34,28,22,16, 10, 4)
        val c = unirCuadro(33,27,21,15, 9, 3)
        val d = unirCuadro(32,26,20,14, 8, 2)
        val e = unirCuadro(31,25,19,13, 7, 1)
        val f = unirCuadro(30,24,18,12, 6, 0)
        return a || b || c || d || e || f
    }

    private fun fusionDerecha(): Boolean {
        val a = unirCuadro(5,4,3,2, 1, 0)
        val b = unirCuadro(11,10,9,8, 7, 6)
        val c = unirCuadro(17,16,15,14, 13, 12)
        val d = unirCuadro(23,22,21, 20, 19, 18)
        val e = unirCuadro(29,28,27, 26, 25, 24)
        val f = unirCuadro(35,34,33, 32, 31, 30)
        return a || b || c || d || e || f
    }

    private fun fusionIzquierda(): Boolean {
        val a = unirCuadro(0, 1, 2, 3, 4, 5)
        val b = unirCuadro(6, 7, 8, 9,10,11)
        val c = unirCuadro(12, 13, 14,15, 16, 17)
        val d = unirCuadro(18, 19, 20,21, 22, 23)
        val e = unirCuadro(24, 25, 26,27, 28,29)
        val f = unirCuadro(30, 31, 32,33, 34,35)
        return a || b || c || d || e || f
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
                "|" + tiles[0] + "|" + tiles[1] + "|" + tiles[2] + "|" +  tiles[3] + "|" + tiles[4] + "|" + tiles[5] + "|" + "\n" +
                "|" + tiles[6] + "|" + tiles[7] + "|" + tiles[8] + "|" +  tiles[9] + "|" + tiles[10] + "|" + tiles[11] + "|" + "\n" +
                "|" + tiles[12] + "|" + tiles[13] + "|" + tiles[14] + "|" +  tiles[15] + "|" + tiles[16] + "|" + tiles[17] + "|" + "\n" +
                "|" + tiles[18] + "|" + tiles[19] + "|" + tiles[20] + "|" +  tiles[21] + "|" + tiles[22] + "|" + tiles[23] + "|" + "\n" +
                "|" + tiles[24] + "|" + tiles[25] + "|" + tiles[26] + "|" +  tiles[27] + "|" + tiles[28] + "|" + tiles[29] + "|" + "\n" +
                "|" + tiles[30] + "|" + tiles[31] + "|" + tiles[32] + "|" +  tiles[33] + "|" + tiles[34] + "|" + tiles[35] + "|" + "\n" +
                "--------------------\n"
    }
}