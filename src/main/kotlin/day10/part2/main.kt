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
package day10.part2

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
        station to data.asSequence()
            .filter { it != station }
            .map { Vec(it.x - station.x, -(it.y - station.y)) }
            .groupBy {
                when {
                    it.x == 0 -> Vec(0, it.y.sign)
                    it.y == 0 -> Vec(it.x.sign, 0)
                    else -> {
                        val gcd = gcd(it.x, it.y)
                        if (gcd >= 0) Vec(it.x / gcd, it.y / gcd) else it
                    }
                }
            }
            .toList()
    }.maxBy { (_, rays) -> rays.size }!!.let { (station, vecs) ->
        fun Vec.angle(): Double {
            val rawInRad = atan2(y.toDouble(), x.toDouble())
            val invInRad = -rawInRad
            val expInRad = (invInRad + 2 * PI) % (2 * PI)
            val offInRad = (expInRad + PI / 2) % (2 * PI)

            return offInRad
        }
        var j = 0

        vecs.sortedBy { (rep, _) -> rep.angle() }
            .let {
                fun Vec.length() = sqrt(x.toDouble().pow(2) + y.toDouble().pow(2))

                val rays = it.map { (_, ray) -> ray.toMutableList().sortedBy(Vec::length).toMutableList() }.toMutableList()

                sequence {
                    var i = 0

                    while (rays.isNotEmpty()) {
                        val ray = rays[i]
                        val vec = ray.first()

                        yield(vec)
                        ray.removeAt(0)

                        if (ray.isEmpty()) {
                            rays.removeAt(i)
                        } else {
                            i++
                        }

                        if (i >= rays.size) i = 0
                    }
                }
            }
            .toList()[199]
            .let { 100 * (station.x + it.x) + (station.y + -it.y) }

    }
}

private data class Vec(val x: Int, val y: Int)