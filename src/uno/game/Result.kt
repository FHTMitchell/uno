package uno.game

import uno.player.Move

data class Result(val playerName: String, val score: Int, val moves: List<Move>)