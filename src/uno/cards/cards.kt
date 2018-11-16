package uno.cards

import uno.enums.Color
import uno.enums.Effect

sealed class Card(open val value: Int?, open val color: Color?, open val effect: Effect?) {

    val points: Int get() = when (this.effect) {
        // probably more efficient to use enum rather than classes
        null -> this.value!!
        Effect.DRAW2, Effect.REVERSE, Effect.SKIP -> 20
        Effect.WILD, Effect.WILD_DRAW4 -> 50
    }

    fun canPlaceOn(other: Card): Boolean {
        return (this is WildCard) ||
                (this.color == other.color) ||
                (other is WildCard && this.color == other.chosenColor) ||
                if (this is ValueCard) {
                    this.value == other.value
                } else {
                    this.effect == other.effect
                }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Card) return false

        if (value != other.value) return false
        if (color != other.color) return false
        if (effect != other.effect) return false

        return true
    }

    override fun hashCode(): Int {
        var result = value ?: 0
        result = 31 * result + color.hashCode()
        result = 31 * result + effect.hashCode()
        return result
    }

    override fun toString(): String {
        return "Card(value=$value, color=$color, effect=$effect)"
    }

}

class ValueCard(override val value: Int, color: Color): Card(value, color, null) {
    override val effect: Nothing? = null
}

sealed class EffectCard(color: Color?, effect: Effect): Card(null, color, effect) {
    override val value: Nothing? = null
}

class Reverse(override val color: Color): EffectCard(color, Effect.REVERSE)

class Skip(override val color: Color): EffectCard(color, Effect.SKIP)

class Draw2(override val color: Color): EffectCard(color, Effect.DRAW2)

sealed class WildCard(effect: Effect): EffectCard(null, effect) {
    override val color: Nothing? = null
    lateinit var chosenColor: Color
}

class Wild: WildCard(Effect.WILD)

class WildDraw4: WildCard(Effect.WILD_DRAW4)