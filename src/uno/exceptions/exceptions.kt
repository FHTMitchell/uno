package uno.exceptions

import uno.cards.Card
import uno.enums.Color
import java.lang.RuntimeException

open class UnoException(msg: String = ""): RuntimeException(msg)

class NoCardInHandException(card: Card): UnoException(card.toString())

class CardNotPlayable(card: Card, topCard: Card): UnoException("Can't play $card on $topCard")

class ColorChosenForColoredCard(card: Card, color: Color):
    UnoException("$card cannot have chosen color $color (must be Color.NONE)")

class NoColorChosenForWildCard(card: Card):
        UnoException("$card must have a chosen color (cannot be Color.NONE)")