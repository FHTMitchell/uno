package uno.game

import uno.cards.*
import uno.enums.Color
import uno.utils.*
import uno.enums.Direction
import uno.player.Player
import java.util.*

class Game(val players: List<Player>) {

    private val takeDeck: Queue<Card> = LinkedList<Card>(deck.shuffled())

    private val playedCards: MutableList<Card> = mutableListOf()

    var direction: Direction = Direction.FORWARDS
        private set

    private var playerIndex: Int = 0

    val currentPlayer: Player = players[playerIndex]

    private fun getNextPlayer(): Player {
        val nextPlayerIndex = when (direction) {
            Direction.FORWARDS -> playerIndex++
            Direction.BACKWARDS -> playerIndex--
        }
        return players[nextPlayerIndex mod players.size]
    }

    val topCard: Card get() = playedCards.last()

    val deckSize: Int get() = takeDeck.size

    val playedSize: Int get() = playedCards.size

    init {
        players.forEach { it.pickup(7, this::takeCard) }
        playedCards.add(takeCard()!!)
        while (topCard is WildCard) { // can't start with a WildCard
            playedCards.add(takeCard()!!)
        }
    }

    private fun takeCard(): Card? {
        if (deckSize != 0)
            return takeDeck.poll()!! // take a card
        else { // reshuffle deck
            takeDeck.addAll(playedCards.apply { shuffle() }) // return all cards, shuffled, to deck
            if (takeDeck.size <= 1) {
                return null
            }
            playedCards.clear()  // clear the played cards
            playedCards.add(takeDeck.poll()!!)  // form new topCard
            return takeDeck.poll()!! // return the next
        }
    }

    fun move(player: Player, card: Card?): Player? {
        if (card == null) {
            val newCard = takeCard()
            return if (newCard == null) {
                null
            } else if (newCard canPlaceOn topCard) {
                move(player, newCard)
            } else {
                player.pickup(newCard)
                getNextPlayer()
            }
        }
        player.playCard(card, topCard=topCard)
        playedCards.add(card)
        val nextPlayer: Player? = when (card) {
            is ValueCard, is Wild ->
                getNextPlayer()
            is Reverse -> {
                direction = direction.reversed()
                if (players.size == 2) {
                    getNextPlayer()
                } else {
                    player  // in 2 player games, reverse acts like skip
                }
            }
            is Skip -> {
                getNextPlayer() // skip
                getNextPlayer()
            }
            is Draw2 -> {
                // null if run out of cards
                if (getNextPlayer().pickup(2, this::takeCard)) getNextPlayer() else null
            }
            is WildDraw4 -> {
                if (getNextPlayer().pickup(4, this::takeCard)) getNextPlayer() else null
            }
        }
        if (!player.inGame) {
            return null
        }
        return nextPlayer
    }

    fun finish(): Map<Player, Int> = players.map { Pair(it, it.handPoints()) }.toMap()


}