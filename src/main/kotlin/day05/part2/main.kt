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
package day05.part2

import utils.*
import java.util.*
import kotlin.math.*

fun main() {
    val input = readInputText("/day05/input.txt")
    val program = input.split(',').map(String::toInt)

    println(run(program, inputs = ArrayDeque(listOf(5))))
}

private fun run(program: List<Int>, inputs: ArrayDeque<Int>): List<Int> {
    val memory = program.toMutableList()
    val outputs = mutableListOf<Int>()

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
                write(1, inputs.pop())
                instrPtr += 2
            }
            4 -> {
                outputs.add(read(1))
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

    return outputs
}