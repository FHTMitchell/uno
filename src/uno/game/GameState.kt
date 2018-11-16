package uno.game

import uno.cards.Card

data class GameState(val hand: List<Card>, val topCard: Card)