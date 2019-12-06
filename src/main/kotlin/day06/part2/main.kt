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
package day06.part2

import utils.*

fun main() {
    val input = readInputText("/day06/input.txt").lines()
    println(run(input.map { it.split(')').let { s -> s[0] to s[1] } }))
}

private fun run(data: List<Pair<String, String>>): Int {
    val you = data.find { it.second == "YOU" }!!
    val santa = data.find { it.second == "SAN" }!!

    fun Pair<String, String>.trace(): List<String> = sequence {
        var prev: Pair<String, String>? = this@trace
        while (prev != null) {
            yield(prev.first)

            val it = data.find { it.second == prev!!.first }
            prev = it
        }
    }.toList()

    val pathToYou = you.trace()
    val pathToSanta = santa.trace()
    val firstCommonOrbit = pathToYou.find { it in pathToSanta }!!

    return pathToYou.indexOf(firstCommonOrbit) + pathToSanta.indexOf(firstCommonOrbit)
}