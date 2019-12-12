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
package day12.part2

import utils.*
import kotlin.math.*

fun main() {
    val input = readInputText("/day12/input.txt")
    val positions = input.lineSequence().map { line ->
        line.removePrefix("<")
            .removeSuffix(">")
            .replace(" ", "")
            .split(',')
            .let { kvPairs -> kvPairs.map { kvPair -> kvPair.split("=")[1].toInt() } }.let { coords ->
                Vec(coords[0], coords[1], coords[2])
            }
    }

    println(run(positions.toList()))
}

private fun run(positions: List<Vec>): Long {
    data class Moon(var pos: Vec, var velocity: Vec)

    val moons = positions.map { Moon(it, Vec(0, 0, 0)) }

    fun revisitCoordAlongAxis(selector: (Vec) -> Int): Int {
        val visited = hashMapOf<List<Pair<Int, Int>>, Int>()

        var coordsAlongAxis: List<Pair<Int, Int>>
        var step = 0

        do {
            coordsAlongAxis = moons.map { selector(it.pos) to selector(it.velocity) }
            visited[coordsAlongAxis]?.let { firstVisited -> return step - firstVisited }
            visited[coordsAlongAxis] = step

            moons.flatMap { a -> moons.map { b -> a to b } }
                .filter { it.first !== it.second }
                .forEach { (a, b) ->
                    a.velocity += Vec(
                        x = (b.pos.x - a.pos.x).sign,
                        y = (b.pos.y - a.pos.y).sign,
                        z = (b.pos.z - a.pos.z).sign
                    )
                }

            moons.forEach { moon -> moon.pos += moon.velocity }
            step++
        } while (true)
    }

    val xCycle = revisitCoordAlongAxis(Vec::x)
    val yCycle = revisitCoordAlongAxis(Vec::y)
    val zCycle = revisitCoordAlongAxis(Vec::z)

    fun lcm(a: Long, b: Long): Long {
        var lcm = if (a > b) a else b
        while (true) {
            if (lcm % a == 0L && lcm % b == 0L) return lcm
            lcm++
        }
    }

    fun lcm(a: Int, b: Int) = lcm(a.toLong(), b.toLong())
    fun lcm(a: Int, b: Long) = lcm(a.toLong(), b)

    return lcm(xCycle, lcm(yCycle, zCycle))
}

data class Vec(val x: Int, val y: Int, val z: Int)

operator fun Vec.plus(other: Vec) = Vec(x + other.x, y + other.y, z + other.z)