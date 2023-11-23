package blackjack.model.game

import blackjack.model.card.CardDeck
import blackjack.model.player.Dealer
import blackjack.model.player.Player

class BlackjackGame(
    val dealer: Dealer,
    val players: List<Player>,
    val cards: CardDeck,
) {
    fun initDraw(printInitStatus: (Dealer, List<Player>) -> Unit) {
        repeat(2) {
            dealer.draw(cards.pop())
        }

        players.forEach { player ->
            repeat(2) {
                player.draw(cards.pop())
            }
        }

        printInitStatus(dealer, players)
    }

    fun play(
        inputPlayerChoice: (String) -> Boolean,
        printPlayerCards: (Player) -> Unit
    ) {
        players.forEach { player ->
            while (!player.isFinished()) {
                hitOrStay(player, inputPlayerChoice)
                printPlayerCards(player)
            }
        }
    }

    fun playDealer(printDealerPop: () -> Unit) {
        if (dealer.isOverSixTeen()) {
            dealer.draw(cards.pop())
            printDealerPop()
        }
    }

    fun result(
        printResult: (Dealer, List<Player>) -> Unit,
        printMatchResult: (MatchResult) -> Unit
    ) {
        printResult(dealer, players)
        printMatchResult(MatchResult.toResult(dealer, players))
    }

    private fun hitOrStay(player: Player, inputPlayerChoice: (String) -> Boolean) {
        when (inputPlayerChoice(player.name)) {
            true -> player.draw(cards.pop())
            false -> player.stay()
        }
    }
}
