package uno.utils


/**
 * Create an infinite sequence of integers, starting at start and increasing by step
 *
 * countFrom() => 0 1 2 3 4 5 ...
 * countFrom(1) => 1 2 3 4 5 6 ...
 * countFrom(6, 3) => 6 9 12 15 ...
 */
fun countFrom(start: Int = 0, step: Int = 1) = generateSequence(start) { it + step }


/**
 * Create a sequence repeating this element n times (or infinite if n is null)
 */
fun <T> T.repeat(times: Int? = null): Sequence<T> = sequence {
    for (i in countFrom()) {
        if (i == times)
            break
        yield(this@repeat)
    }
}


/**
 * Create a repeating sequence that cycles through this iterable n times (or infinitely if n is null)
 *
 * asList(1, 2, 3).cycle() => 1 2 3 1 2 3 1 2 3 ...
 */
fun <T> Iterable<T>.cycle(n: Int? = null): Sequence<T> = this.toList().repeat(n).flatten()


@Suppress("ConvertFlatMapToFlatten")
fun <T> concat(vararg iters: Iterable<T>) = iters.flatMap { it }


infix fun Int.mod(other: Int): Int {
    var rem = this % other
    if (rem < 0) {
        rem += other
    }
    return rem
}