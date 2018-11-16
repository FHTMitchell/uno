package uno.player

import uno.cards.Card

data class Move(val hand: List<Card>, val playedCard: Card, val topCard: Card)