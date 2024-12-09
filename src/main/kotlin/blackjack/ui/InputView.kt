package blackjack.ui

import blackjack.domain.Player

object InputView {
    private const val PLAYER_NAMES_PROMPT = "게임에 참여할 사람의 이름을 입력하세요.(쉼표 기준으로 분리)"
    private const val COMMA_DELIMITER = ","

    fun getPlayerNames(): List<String> {
        println(PLAYER_NAMES_PROMPT)
        return readln().split(COMMA_DELIMITER).map { it.trim() }
    }

    fun getCommand(player: Player): PlayerCommand {
        println("${player.name}는 한장의 카드를 더 받겠습니까?(예는 y, 아니오는 n)")
        return PlayerCommand.of(readln(), player)
    }
}
