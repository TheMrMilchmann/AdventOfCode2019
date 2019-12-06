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
package day06.part1

import utils.*

fun main() {
    val input = readInputText("/day06/input.txt").lines()
    println(run(input.map { it.split(')').let { s -> s[0] to s[1] } }))
}


private fun run(data: List<Pair<String, String>>): Int {
    fun String.countTransitiveOrbits(depth: Int = 0): Int {
        val immediateOrbits = data.filter { it.first == this }
        return immediateOrbits.count() + depth + immediateOrbits.map { it.second.countTransitiveOrbits(depth + 1) }.sum()
    }

    return data.filter { it.first == "COM" }.map { it.second.countTransitiveOrbits() + 1 }.sum()
}