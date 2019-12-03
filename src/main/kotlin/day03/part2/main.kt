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
package day03.part2

import utils.*

fun main() {
    val input = readInputText("/day03/input.txt")
    val data = input.lineSequence().map { it.split(',') }.toList()

    println(run(data))
}

private fun run(data: List<List<String>>): Int {
    data class Point(val x: Int, val y: Int)
    data class TrackedPoint(val point: Point, val steps: Int)

    fun String.toPoints(origin: TrackedPoint): List<TrackedPoint> {
        val op: (Point) -> Point = when (val dir = this[0]) {
            'U' -> { it -> it.copy(y = it.y + 1) }
            'D' -> { it -> it.copy(y = it.y - 1) }
            'R' -> { it -> it.copy(x = it.x + 1) }
            'L' -> { it -> it.copy(x = it.x - 1) }
            else -> error("Encountered unexpected direction: $dir")
        }

        val res = mutableListOf<TrackedPoint>()
        var tail = origin
        for (i in 0 until substring(1).toInt()) {
            tail.copy(point = op(tail.point), steps = tail.steps + 1).also {
                tail = it
                res.add(it)
            }
        }

        return res
    }

    fun <T> Iterable<Iterable<T>>.intersectAll(): Iterable<T> {
        val itr = iterator()
        var res = if (itr.hasNext()) itr.next().toSet() else emptySet()

        while (itr.hasNext()) res = res.intersect(itr.next())

        return res
    }

    fun <T, K> Iterable<Iterable<T>>.intersectAllBy(selector: (T) -> K): Iterable<T> {
        val tmp = map { it.map(selector) }.intersectAll()

        val res = mutableListOf<T>()
        forEach { iterable ->
            iterable.forEach { element ->
                if (selector(element) in tmp) res.add(element)
            }
        }

        return res
    }

    return data.map { path ->
        var tail = TrackedPoint(Point(0, 0), 0)
        path.flatMap { segment -> segment.toPoints(tail).also { tail = it.last() } }
            .groupBy { it.point }
            .map { it.value.minBy(TrackedPoint::steps)!! }
    }.intersectAllBy { it.point }
        .filter { it.point != Point(0, 0) }
        .groupBy { it.point }
        .map { it.value.sumBy(TrackedPoint::steps) }
        .min()!!
}