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
package day04.part2

import utils.*

fun main() {
    val input = readInputText("/day04/input.txt")
    val data = input.split("-").map(String::toInt).let { it[0]..it[1] }

    println(run(data))
}

private fun run(range: IntRange): Int {
    fun Int.isValidPassword(): Boolean {
        data class Entry(val index: Int, val value: Int)

        val numbers = toString().toCharArray().map { it.toString().toInt() }
        val pairs = numbers.mapIndexed(::Entry).zipWithNext()

        return pairs.any { it.first.value == it.second.value
            && (it.first.index - 1).let { i -> if (i >= 0) numbers[i] != it.first.value else true }
            && (it.second.index + 1).let { i -> if (i < numbers.size) numbers[i] != it.first.value else true }
        } && pairs.all { it.first.value <= it.second.value }
    }

    return range.filter(Int::isValidPassword).count()
}