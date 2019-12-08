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
package day08.part2

import utils.*

fun main() {
    val input = readInputText("/day08/input.txt")
    println(run(input.map { "$it".toInt() }, width = 25, height = 6))
}

private fun run(data: List<Int>, width: Int, height: Int): String {
    val layerSize = width * height

    val layers = List(data.size / layerSize) { layerIndex ->
        List(layerSize) { pixelIndex ->
            data[layerIndex * layerSize + pixelIndex]
        }
    }

    val image = List(layerSize) { pixelIndex ->
        layers.map { it[pixelIndex] }.find { it != 2 }!!
    }

    return StringBuilder().apply {
        for (y in 0 until height) {
            for (x in 0 until width) {
                append(if (image[y * width + x] == 1) "█" else " ")
            }

            append('\n')
        }
    }.toString()
}