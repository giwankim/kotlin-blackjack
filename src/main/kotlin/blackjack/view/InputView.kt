package blackjack.view

import blackjack.domain.player.Players

private const val YES_PARAMETER = "y"

private const val NO_PARAMETER = "n"

object InputView {
    fun getPlayerInput(): Players {
        println("게임에 참여할 사람의 이름을 입력하세요.(쉼표 기준으로 분리)")
        val playerNames = readln().split(",")
        println()
        return Players.of(playerNames)
    }

    fun getCardOrNot(name: String): Boolean {
        while (true) {
            println("${name}는 한장의 카드를 더 받겠습니까?(예는 y, 아니오는 n)")
            val yOrN = readln()
            if (yOrN == YES_PARAMETER)
                return true
            if (yOrN == NO_PARAMETER)
                return false
        }
    }
}
