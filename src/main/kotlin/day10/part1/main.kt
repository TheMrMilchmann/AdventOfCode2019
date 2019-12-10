/*
 * Copyright (c) 2019 Leon Linhart
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package day10.part1

import utils.*
import kotlin.math.*

fun main() {
    val input = readInputText("/day10/input.txt")
    val data = input.lineSequence().mapIndexed { y, line ->
        line.mapIndexed { x, c ->
            if (c == '#') Vec(x, y) else null
        }
    }.flatten().filterNotNull().toList()

    println(run(data))
}

private fun run(data: List<Vec>): Int {
    fun gcd(a: Int, b: Int): Int {
        var r = abs(b)
        var oldR = abs(a)

        while (r != 0) {
            val quotient = oldR / r

            val tmpR = r
            r = oldR - quotient * r
            oldR = tmpR
        }

        return oldR
    }

    return data.map { station ->
        data.asSequence()
            .filter { it != station }
            .map { Vec(it.x - station.x, it.y - station.y) }
            .map {
                when {
                    it.x == 0 -> Vec(0, it.y.sign)
                    it.y == 0 -> Vec(it.x.sign, 0)
                    else -> {
                        val gcd = gcd(it.x, it.y)
                        if (gcd >= 0) Vec(it.x / gcd, it.y / gcd) else it
                    }
                }
            }.distinct()
            .count()
    }.max()!!
}

private data class Vec(val x: Int, val y: Int)