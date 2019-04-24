package com.example.fiall.a2048

import java.io.Serializable
import java.util.ArrayList
import java.util.Random

class Tiles8 internal constructor() : Serializable {

    companion object {
        internal const val TARGET = 2048
        private const val GRID_COUNT = 64
        private const val ROW_COUNT = 8
        private const val COL_COUNT = 8
        private const val ESPACIO = 0
        var movimientosRestantes = 5
    }

    var score = 0
        private set
    private var numEmpty = 64
    var maxTile = 0
        private set

    lateinit var transitions: ArrayList<Transition>
        private set
    private val tiles = IntArray(64)
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

    // Movimientos restantes
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
    private fun deslizarCuadro(index1: Int, index2: Int, index3: Int, index4: Int, index5: Int, index6: Int, index7: Int,
                               index8: Int): Boolean {
        var moved = false
        val tmpArr = intArrayOf(index1, index2, index3, index4, index5, index6, index7, index8)

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
    private fun unirCuadro(index1: Int, index2: Int, index3: Int, index4: Int, index5: Int, index6: Int, index7: Int,
                           index8: Int): Boolean {
        var fusion = false
        val tmpArr = intArrayOf(index1, index2, index3, index4, index5, index6, index7, index8)

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
        val a = deslizarCuadro(0, 8, 16, 24, 32, 40, 48, 56)
        val b = deslizarCuadro(1, 9, 17, 25, 33,41, 49, 57)
        val c = deslizarCuadro(2, 10, 18, 26, 34,42, 50, 58)
        val d = deslizarCuadro(3, 11, 19, 27, 35,43, 51, 59)
        val e = deslizarCuadro(4, 12, 20, 28, 36,44, 52, 60)
        val f = deslizarCuadro(5, 13, 21, 29, 37,45, 53, 61)
        val g = deslizarCuadro(6, 14, 22, 30, 38,46, 54, 62)
        val h = deslizarCuadro(7, 15, 23, 31, 39,47, 55, 63)
        return a || b || c || d || e || f || g || h
    }

    private fun deslizarAbajo(): Boolean {
        val a = deslizarCuadro(63,55,47,39,31,23, 15, 7)
        val b = deslizarCuadro(62,54,46,38,30,22, 14, 6)
        val c = deslizarCuadro(61, 53,45,37,29,21, 13, 5)
        val d = deslizarCuadro(60, 52,44,36,28,20, 12, 4)
        val e = deslizarCuadro(59, 51,43,35,27,19, 11, 3)
        val f = deslizarCuadro(58, 50,42,34,26,18, 10, 2)
        val g = deslizarCuadro(57, 49,41,33,25,17, 9, 1)
        val h = deslizarCuadro(56,48,40,32,24,16, 8, 0)
        return a || b || c || d || e || f || g || h
    }

    private fun deslizarDerecha(): Boolean {
        val a = deslizarCuadro(7,6,5,4,3,2, 1, 0)
        val b = deslizarCuadro(15,14,13,12,11,10, 9, 8)
        val c = deslizarCuadro(23,22,21,20,19,18, 17, 16)
        val d = deslizarCuadro(31,30,29,28,27, 26, 25, 24)
        val e = deslizarCuadro(39,38,37,36,35, 34, 33, 32)
        val f = deslizarCuadro(47,46,45,44,43, 42, 41, 40)
        val g = deslizarCuadro(55,54,53,52,51, 50, 49, 48)
        val h = deslizarCuadro(63,62,61,60,59, 58, 57, 56)
        return a || b || c || d || e || f || g || h
    }

    private fun deslizarIzquierda(): Boolean {
        val a = deslizarCuadro(0, 1, 2, 3, 4, 5, 6, 7)
        val b = deslizarCuadro(8, 9, 10, 11,12,13, 14, 15)
        val c = deslizarCuadro(16, 17, 18,19, 20, 21, 22, 23)
        val d = deslizarCuadro(24, 25, 26,27, 28, 29, 30, 31)
        val e = deslizarCuadro(32, 33, 34,35, 36,37, 38, 39)
        val f = deslizarCuadro(40, 41, 42,43, 44,45, 46, 47)
        val g = deslizarCuadro(48, 49, 50,51, 52,53, 54, 55)
        val h = deslizarCuadro(56, 57, 58,59, 60,61,62, 63)
        return a || b || c || d || e || f || g || h
    }

    private fun fusionArriba(): Boolean {
        val a = unirCuadro(0, 8, 16, 24, 32, 40, 48, 56)
        val b = unirCuadro(1, 9, 17, 25, 33,41, 49, 57)
        val c = unirCuadro(2, 10, 18, 26, 34,42, 50, 58)
        val d = unirCuadro(3, 11, 19, 27, 35,43, 51, 59)
        val e = unirCuadro(4, 12, 20, 28, 36,44, 52, 60)
        val f = unirCuadro(5, 13, 21, 29, 37,45, 53, 61)
        val g = unirCuadro(6, 14, 22, 30, 38,46, 54, 62)
        val h = unirCuadro(7, 15, 23, 31, 39,47, 55, 63)
        return a || b || c || d || e || f || g || h
    }

    private fun fusionAbajo(): Boolean {
        val a = unirCuadro(63,55,47,39,31,23, 15, 7)
        val b = unirCuadro(62,54,46,38,30,22, 14, 6)
        val c = unirCuadro(61, 53,45,37,29,21, 13, 5)
        val d = unirCuadro(60, 52,44,36,28,20, 12, 4)
        val e = unirCuadro(59, 51,43,35,27,19, 11, 3)
        val f = unirCuadro(58, 50,42,34,26,18, 10, 2)
        val g = unirCuadro(57, 49,41,33,25,17, 9, 1)
        val h = unirCuadro(56,48,40,32,24,16, 8, 0)
        return a || b || c || d || e || f || g || h
    }

    private fun fusionDerecha(): Boolean {
        val a = unirCuadro(7,6,5,4,3,2, 1, 0)
        val b = unirCuadro(15,14,13,12,11,10, 9, 8)
        val c = unirCuadro(23,22,21,20,19,18, 17, 16)
        val d = unirCuadro(31,30,29,28,27, 26, 25, 24)
        val e = unirCuadro(39,38,37,36,35, 34, 33, 32)
        val f = unirCuadro(47,46,45,44,43, 42, 41, 40)
        val g = unirCuadro(55,54,53,52,51, 50, 49, 48)
        val h = unirCuadro(63,62,61,60,59, 58, 57, 56)
        return a || b || c || d || e || f || g || h
    }

    private fun fusionIzquierda(): Boolean {
        val a = deslizarCuadro(0, 1, 2, 3, 4, 5, 6, 7)
        val b = deslizarCuadro(8, 9, 10, 11,12,13, 14, 15)
        val c = deslizarCuadro(16, 17, 18,19, 20, 21, 22, 23)
        val d = deslizarCuadro(24, 25, 26,27, 28, 29, 30, 31)
        val e = deslizarCuadro(32, 33, 34,35, 36,37, 38, 39)
        val f = deslizarCuadro(40, 41, 42,43, 44,45, 46, 47)
        val g = deslizarCuadro(48, 49, 50,51, 52,53, 54, 55)
        val h = deslizarCuadro(56, 57, 58,59, 60,61,62, 63)
        return a || b || c || d || e || f || g || h
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
                "--------------------\n" +
                "|" + tiles[0] + "|" + tiles[1] + "|" + tiles[2] + "|" +  tiles[3] + "|" + tiles[4] + "|" + tiles[5] + "|" + tiles[6] + "|" + tiles[7] + "|" + "\n" +
                "|" + tiles[8] + "|" + tiles[9] + "|" + tiles[10] + "|" +  tiles[11] + "|" + tiles[12] + "|" + tiles[13] + "|" + tiles[14] + "|" +  tiles[15] + "|" + "\n" +
                "|" + tiles[16] + "|" + tiles[17] + "|" + tiles[18] + "|" +  tiles[19] + "|" + tiles[20] + "|" + tiles[21] + "|" + tiles[22] + "|" +  tiles[23] + "|" + "\n" +
                "|" + tiles[24] + "|" + tiles[25] + "|" + tiles[26] + "|" +  tiles[27] + "|" + tiles[28] + "|" + tiles[29] + "|" + tiles[30] + "|" +  tiles[31] + "|" + "\n" +
                "|" + tiles[32] + "|" + tiles[33] + "|" + tiles[34] + "|" +  tiles[35] + "|" + tiles[36] + "|" + tiles[37] + "|" + tiles[38] + "|" +  tiles[38] + "|" + "\n" +
                "|" + tiles[40] + "|" + tiles[41] + "|" + tiles[42] + "|" +  tiles[43] + "|" + tiles[44] + "|" + tiles[45] + "|" + tiles[46] + "|" +  tiles[47] + "|" + "\n" +
                "|" + tiles[48] + "|" + tiles[49] + "|" + tiles[50] + "|" +  tiles[51] + "|" + tiles[52] + "|" + tiles[53] + "|" + tiles[54] + "|" +  tiles[55] + "|" + "\n" +
                "|" + tiles[56] + "|" + tiles[57] + "|" + tiles[58] + "|" +  tiles[59] + "|" + tiles[60] + "|" + tiles[61] + "|" + tiles[62] + "|" +  tiles[63] + "|" + "\n" +
                "--------------------\n"
    }
}