package uno.player

import uno.cards.Card
import uno.cards.WildCard
import uno.enums.Color
import uno.exceptions.CardNotPlayable
import uno.exceptions.NoCardInHandException
import uno.game.GameState
import java.lang.RuntimeException
import java.time.Instant
import kotlin.random.Random

class Player(val name: String, val saveMoves: Boolean = false, val chooseWildColor: (GameState) -> Color) {

    companion object {
        private val random = Random(Instant.now().toEpochMilli())
    }

    val savedMoves: MutableList<Move> = mutableListOf()

    private val hand: MutableList<Card> = mutableListOf()

    // dynamic properties

    val handSize: Int get() = hand.size


    val hasUno get() = this.handSize == 1

    val inGame get() = this.handSize != 0

    // methods

    private fun validPlays(topCard: Card): List<Card> =
        this.hand.filter { it.canPlaceOn(topCard) }

    fun canPlay(topCard: Card): Boolean =
        this.validPlays(topCard).isNotEmpty()

    fun playCard(card: Card, topCard: Card) {
        if (card.canPlaceOn(topCard)) {
            if (saveMoves) {
                this.savedMoves.add(Move(hand.toList(), card, topCard))
                this.hand.remove(card)
                if (card is WildCard) {
                    card.chosenColor = chooseWildColor(GameState(this.hand, topCard))
                }
                return
            }
            if (!this.hand.remove(card)) {
                throw NoCardInHandException(card)
            }
        }
        throw CardNotPlayable(card, topCard)
    }

    fun pickup(card: Card) {
        this.hand.add(card)
    }

    fun pickup(number: Int, generator: () -> Card?): Boolean {
        repeat (number) {
            val card = generator()
            this.pickup(card ?: return false)
        }
        return true
    }

    fun handPoints(): Int = this.hand.sumBy { it.points }

    override fun toString(): String {
        return "Player(name='$name')"
    }


    // random

    fun shoutUno(prob: Double = 1.0) = random.nextDouble() <= prob

    fun randomPlay(topCard: Card): Card? {
        val validPlays = this.validPlays(topCard)
        return if (validPlays.isNotEmpty()) validPlays.random(random) else null
    }

    // reset

    fun reset() {
        savedMoves.clear()
        hand.clear()
    }

}