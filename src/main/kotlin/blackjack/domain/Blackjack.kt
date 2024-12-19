package blackjack.domain

import java.math.BigDecimal

class Blackjack(
    override val hand: Hand,
) : State {
    override val isDone: Boolean get() = true

    init {
        require(hand.size == Hand.INITIAL_HAND_SIZE) { "블랙잭은 두 장의 카드로 이루어집니다." }
        require(hand.value() == Hand.BLACKJACK_VALUE) { "블랙잭은 21점이어야 합니다." }
    }

    override fun drawFrom(deck: Deck): State = throw IllegalStateException("현재 상태에서는 카드를 뽑을 수 없습니다.")

    override fun stand(): State = throw IllegalStateException("현재 상태에서는 스탠드를 할 수 없습니다.")

    override fun profit(bet: Bet): BigDecimal = bet.value.times(BigDecimal(1.5))
}
