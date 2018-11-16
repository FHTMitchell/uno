package uno.cards

import uno.enums.Color
import uno.utils.concat
import uno.utils.cycle

private val colors = Color.values().toList()

private val valueCards: List<ValueCard> by lazy {
    val cards = colors.map { ValueCard(0, it) }.toMutableList()  // one 0 value card for each color
    for (value in 1..10) {
        for (color in colors) {
            repeat (2) {
                cards.add(ValueCard(value, color))  // 2 cards for each color for each value > 1
            }
        }
    }
    return@lazy cards.toList()
}

private val skipCards = colors.cycle(2).map { Skip(it) }.toList()

private val reverseCards = colors.cycle(2).map { Reverse(it) }.toList()

private val draw2Cards = colors.cycle(2).map { Draw2(it) }.toList()

// wild are mutable and so must be unique objects

private val wildCards = Array(4) { Wild() }.toList()

private val wildDraw4Cards = Array(4) { WildDraw4() }.toList()


val deck: List<Card> by lazy{ concat(valueCards, skipCards, reverseCards, draw2Cards, wildCards, wildDraw4Cards) }