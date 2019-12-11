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
package day11.part2

import utils.*
import java.lang.StringBuilder
import java.math.*
import kotlin.math.*

fun main() {
    val input = readInputText("/day11/input.txt")
    val program = input.split(',').map(::IntValue)
    println(run(program))
}

private fun run(program: List<IntValue>): String {
    data class Vec(val x: Int, val y: Int)
    val visited = HashMap<Vec, Boolean>()

    var pos = Vec(0, 0).also { visited[it] = true }
    var facing = Facing.NORTH

    val inputFun = fun IntComputer.(): IntValue {
        return IntValue(if (visited[pos]!!) 1 else 0)
    }

    var outputTurn = 0
    val outputFun = fun (value: IntValue) {
        when (outputTurn) {
            0 -> visited[pos] = when (value) {
                IntValue(0) -> false
                IntValue(1) -> true
                else -> error("Unexpected color: $value")
            }
            else -> {
                facing = when (value) {
                    IntValue(0) -> facing.turnLeft
                    IntValue(1) -> facing.turnRight
                    else -> error("Unexpected input value: $value")
                }

                pos = when (facing) {
                    Facing.NORTH -> pos.copy(y = pos.y + 1)
                    Facing.EAST -> pos.copy(x = pos.x + 1)
                    Facing.SOUTH -> pos.copy(y = pos.y - 1)
                    Facing.WEST -> pos.copy(x = pos.x - 1)
                }.also { visited.putIfAbsent(it, false) }
            }
        }

        outputTurn = (outputTurn + 1) % 2
    }

    IntComputer(program, inputFun, outputFun).run()

    val xShift = 0 - visited.keys.map(Vec::x).min()!!
    val yShift = 0 - visited.keys.map(Vec::y).min()!!
    val positions = visited.filter { (_, value) -> value }
        .map { (key, _) -> key.copy(x = key.x + xShift, y = key.y + yShift) }

    val maxX = positions.map(Vec::x).max()!!
    val maxY = positions.map(Vec::y).max()!!

    return StringBuilder().run {
        for (y in maxY downTo 0) {
            for (x in 0..maxX) {
                append(if (Vec(x, y) in positions) '█' else ' ')
            }

            append('\n')
        }

        toString()
    }
}

enum class Facing {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    val turnLeft get() = values()[(ordinal + 3) % 4]
    val turnRight get() = values()[(ordinal + 1) % 4]
}

inline class IntValue(private val value: BigInteger) {

    constructor(value: Int): this(value.toBigInteger())
    constructor(value: String): this(value.toBigInteger())

    operator fun compareTo(other: IntValue) = value.compareTo(other.value)

    operator fun plus(other: Int): IntValue = IntValue(value + other.toBigInteger())
    operator fun plus(other: IntValue): IntValue = IntValue(value + other.value)
    operator fun times(other: Int): IntValue = IntValue(value * other.toBigInteger())
    operator fun times(other: IntValue): IntValue = IntValue(value * other.value)

    fun toInt(): Int = value.toInt()
    override fun toString() = value.toString()

}

private operator fun Int.plus(other: IntValue) = other.plus(this)

private class IntComputer(
    program: List<IntValue>,
    private val input: IntComputer.() -> IntValue,
    private val output: (IntValue) -> Unit
) {

    private val memory = object {

        val _impl = program.toMutableList()

        operator fun get(addr: Int): IntValue {
            if (addr >= _impl.size) _impl.addAll(List(addr - _impl.size + 1) { IntValue(0) })
            return _impl[addr]
        }

        operator fun set(addr: Int, value: IntValue) {
            if (addr >= _impl.size) _impl.addAll(List(addr - _impl.size + 1) { IntValue(0) })
            _impl[addr] = value
        }

        operator fun get(addr: IntValue): IntValue = this[addr.toInt()]
        operator fun set(addr: IntValue, value: IntValue) = set(addr.toInt(), value)

    }

    var instrPtr = 0
    var relativeBase = 0

    fun run(): Boolean {
        while (true) {
            val instr = memory[instrPtr].toInt()
            fun modeOf(param: Int) = ((instr / (10.0.pow(param + 1))) % 10).toInt()

            fun read(param: Int) = when (val mode = modeOf(param)) {
                0 -> memory[memory[instrPtr + param]]
                1 -> memory[instrPtr + param]
                2 -> memory[relativeBase + memory[instrPtr + param]]
                else -> error("Encountered unexpected mode during read: $mode")
            }

            fun write(param: Int, value: IntValue): Unit = when (val mode = modeOf(param)) {
                0 -> memory[memory[instrPtr + param]] = value
                2 -> memory[relativeBase + memory[instrPtr + param]] = value
                else -> error("Encountered unexpected mode during write: $mode")
            }
            fun write(param: Int, value: Int) = write(param, IntValue(value))

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
                    try {
                        write(1, input())
                        instrPtr += 2
                    } catch (e: SuspendException) {
                        return false
                    }
                }
                4 -> {
                    output(read(1))
                    instrPtr += 2
                }
                5 -> {
                    if (read(1) != IntValue(0))
                        instrPtr = read(2).toInt()
                    else
                        instrPtr += 3
                }
                6 -> {
                    if (read(1) == IntValue(0))
                        instrPtr = read(2).toInt()
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
                9 -> {
                    relativeBase += read(1).toInt()
                    instrPtr += 2
                }
                99 -> return true
                else -> error("Encountered unexpected opcode: $opcode")
            }
        }
    }

    fun suspend(): Nothing { throw SuspendException() }

    private class SuspendException : Exception()

}