package uno.game

import uno.enums.Color
import uno.player.Player
import uno.utils.repeat

fun randomChooseColor(state: GameState): Color {
    val colorCounts = Color.values().map { color -> color to state.hand.count { it.color == color } }.toMap()
    return colorCounts.maxBy { it.value }?.key ?: Color.randomColor()
}

fun simulate(num_players: Int, verbose: Boolean = true, finalScore: Int = 500): List<Result> {

    val players: List<Player> = (1..num_players).map {
        Player("player $it", true, ::randomChooseColor)
    }

    val scores: MutableMap<Player, Int> = players.map { it to 0 }.toMap().toMutableMap()

    val results: MutableList<Result> = mutableListOf()

    var roundID = 1
    while (scores.values.max()!! < finalScore) {

        val game = Game(players)
        var nextPlayer: Player? = game.currentPlayer

        while (nextPlayer != null) {
            val cardToPlay = nextPlayer.randomPlay(game.topCard)
            nextPlayer = game.move(nextPlayer, cardToPlay)
        }

        val gameResults: Map<Player, Result> = players.map {
            it to Result(it.name, it.handPoints(), it.savedMoves.toList())
        }.toMap()

        results.addAll(gameResults.values)
        scores.forEach { scores[it.key] = it.value + gameResults[it.key]!!.score }

        if (verbose) {

            val prettyScores = gameResults.values
                .sortedBy { it.playerName }
                .joinToString("\n") {
                    "${it.playerName}: ${it.score}"
                }

            val numMoves = gameResults.values.sumBy { it.moves.size }

            println('-'.repeat(80).joinToString(""))
            println("finished round $roundID ($numMoves moves) with results: \n$prettyScores")
            println("final deck size: ${game.deckSize}, final played size: ${game.playedSize}")
            println("final top card: ${game.topCard}")
        }
        players.forEach { it.reset() }
        roundID++
    }

    if (verbose) {
        println("final scores == $scores")
        Thread.sleep(1000)
    }

    return results
}

fun main() {
    simulate(2, verbose = true, finalScore = 500)
}