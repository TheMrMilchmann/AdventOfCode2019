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
package day02.part1

import utils.*

fun main() {
    val input = readInputText("/day02/input.txt")
    val program = input.split(',').map(String::toInt).toMutableList()

    program[1] = 12
    program[2] = 2

    println(run(program))
}

private fun run(program: List<Int>): Int {
    val memory = program.toMutableList()
    var instrPtr = 0

    loop@while (true) {
        when (val opcode = memory[instrPtr]) {
            1 -> memory[memory[instrPtr + 3]] = memory[memory[instrPtr + 1]] + memory[memory[instrPtr + 2]]
            2 -> memory[memory[instrPtr + 3]] = memory[memory[instrPtr + 1]] * memory[memory[instrPtr + 2]]
            99 -> break@loop
            else -> error("Encountered unexpected opcode: $opcode")
        }

        instrPtr += 4
    }

    return memory[0]
}