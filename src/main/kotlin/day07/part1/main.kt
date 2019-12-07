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
package day07.part1

import utils.*
import kotlin.math.*

fun main() {
    val input = readInputText("/day07/input.txt")
    val program = input.split(',').map(String::toInt)
    val phases = (0..4).toList()

    fun <T, O> List<T>.cartesianProduct(other: List<O>): List<Pair<T, O>> = flatMap { t -> other.map { o -> t to o } }

    val permutations = phases.cartesianProduct(phases).flatMap { (a, b) ->
        phases.cartesianProduct(phases).flatMap { (c, d) ->
            phases.map { e -> listOf(a, b, c, d, e) }
        }
    }.filter { list -> list.groupBy { it }.map { it.value.size }.max() == 1 }

    val signal = permutations.map { phaseSettings ->
        val itr = phaseSettings.iterator()
        var signal = 0

        while (itr.hasNext()) {
            run(program, listOf(itr.next(), signal).iterator()::next) { output ->
                signal = output
            }
        }

        return@map signal
    }.max()

    println(signal)
}

private fun run(program: List<Int>, input: () -> Int, output: (Int) -> Unit) {
    val memory = program.toMutableList()

    var instrPtr = 0
    loop@while (true) {
        val instr = memory[instrPtr]
        fun modeOf(param: Int) = ((instr / (10.0.pow(param + 1))) % 10).toInt()

        fun read(param: Int) = when (val mode = modeOf(param)) {
            0 -> memory[memory[instrPtr + param]]
            1 -> memory[instrPtr + param]
            else -> error("Encountered unexpected mode during read: $mode")
        }

        fun write(param: Int, value: Int): Unit = when (val mode = modeOf(param)) {
            0 -> memory[memory[instrPtr + param]] = value
            else -> error("Encountered unexpected mode during write: $mode")
        }

        when (val opcode = instr % 100) {
            1 -> {
                write(3, read(1) + read(2))
                instrPtr += 4
            }
            2 -> {
                write(3, read(1) * read(2))
                instrPtr += 4
            }
            3 -> {
                write(1, input())
                instrPtr += 2
            }
            4 -> {
                output(read(1))
                instrPtr += 2
            }
            5 -> {
                if (read(1) != 0)
                    instrPtr = read(2)
                else
                    instrPtr += 3
            }
            6 -> {
                if (read(1) == 0)
                    instrPtr = read(2)
                else
                    instrPtr += 3
            }
            7 -> {
                write(3, if (read(1) < read(2)) 1 else 0)
                instrPtr += 4
            }
            8 -> {
                write(3, if (read(1) == read(2)) 1 else 0)
                instrPtr += 4
            }
            99 -> break@loop
            else -> error("Encountered unexpected opcode: $opcode")
        }
    }
}