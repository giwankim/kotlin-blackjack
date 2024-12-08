package blackjack.domain

import blackjack.domain.StubDeck.Companion.DUMMY_SUIT
import blackjack.support.Fixtures.createBlackjackDealer
import blackjack.support.Fixtures.createBlackjackPlayer
import blackjack.support.Fixtures.createBustedDealer
import blackjack.support.Fixtures.createBustedPlayer
import blackjack.support.Fixtures.createDealer
import blackjack.support.Fixtures.createPlayer
import blackjack.support.Fixtures.createStandingPlayer
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

@Suppress("NonAsciiCharacters")
class PlayerTest {
    @Test
    fun `이름이 빈 문자열인 경우 예외를 던진다`() {
        assertThrows<IllegalArgumentException> { Player("") }
    }

    @Test
    fun `이름을 지정할 수 있다`() {
        val player = Player("jack")
        player.name shouldBe "jack"
    }

    @Test
    fun `플레이어 생성 시 카드가 없다`() {
        val player = Player("jack")
        player.value shouldBe 0
    }

    @Test
    fun `플레이어 생성 시 턴이 종료되지 않았다`() {
        val player = Player("jack")
        player.isDone shouldBe false
    }

    @Test
    fun `초기화 라운드에 카드를 지급 받는다`() {
        val player = Player("jack")
        val deck = StubDeck.from(Rank.ACE)

        player.initialDrawFrom(deck)

        player.hand[0] shouldBe Card(DUMMY_SUIT, Rank.ACE)
    }

    @Test
    fun `두 장의 카드를 받아서 블랙젝인 경우, 턴이 종료 된다`() {
        val player = Player("jack")
        val deck = StubDeck.from(Rank.ACE, Rank.KING)

        player.initialDrawFrom(deck)
        player.initialDrawFrom(deck)

        player.isDone shouldBe true
        player.reasonDone shouldBe PlayerReasonDone.PLAYER_HAS_BLACKJACK
    }

    @Test
    fun `힛을 하여 덱에서 카드를 뽑을 수 있다`() {
        val player = Player("jack")
        val deck = StubDeck.from(Rank.TWO)

        player.hit(deck)

        player.hand[0] shouldBe Card(DUMMY_SUIT, Rank.TWO)
    }

    @ParameterizedTest(name = "{index} 플레이어 상태 = {1}")
    @MethodSource("donePlayers")
    fun `턴이 종료된 경우, 힛을 할 수 없다`(
        player: Player,
        description: String,
    ) {
        val deck = StubDeck.from(Rank.TWO, Rank.THREE)
        assertThrows<IllegalStateException> { player.hit(deck) }
    }

    @Test
    fun `힛을 하여 21점을 넘으면 버스트된다`() {
        val hand =
            Hand(
                Card(Suit.CLUBS, Rank.KING),
                Card(Suit.CLUBS, Rank.QUEEN),
            )
        val player = Player("jack", hand)
        val deck = StubDeck.from(Rank.JACK)

        player.hit(deck)

        player.isDone shouldBe true
        player.reasonDone shouldBe PlayerReasonDone.PLAYER_BUSTED
    }

    @Test
    fun `스탠드를 하면 턴이 종료된다`() {
        val player = Player("jack")

        player.stand()

        player.isDone shouldBe true
        player.reasonDone shouldBe PlayerReasonDone.PLAYER_STANDS
    }

    @ParameterizedTest(name = "{index} 플레이어 상태 = {1}")
    @MethodSource("donePlayers")
    fun `이미 턴이 끝난 상태에서는 스탠드 할 수 없다`(
        player: Player,
        description: String,
    ) {
        assertThrows<IllegalStateException> { player.stand() }
    }

    @Test
    fun `버스트되었는지 리턴한다`() {
        createBustedPlayer().isBusted shouldBe true
        Player("jack").isBusted shouldBe false
    }

    @Test
    fun `딜러가 블랙잭이라 플레이어 턴이 종료된다`() {
        val player = Player("jack")

        player.dealerDealtBlackjack()

        player.reasonDone shouldBe PlayerReasonDone.DEALER_DEALT_BLACKJACK
    }

    @Test
    fun `플레이어가 버스트한 경우 패배한다`() {
        val bustedPlayer = createBustedPlayer("jack")
        val dealer = createDealer(StubDeck.from(Rank.SIX, Rank.SEVEN))

        val outcome = bustedPlayer.outcome(dealer)

        outcome shouldBe PlayerOutcome.LOSE
    }

    @Test
    fun `딜러가 버스트한 경우 버스트하지 않은 플레이어는 승리한다`() {
        val player = createPlayer(StubDeck.from(Rank.JACK, Rank.TEN))
        val bustedDealer = createBustedDealer()

        val outcome = player.outcome(bustedDealer)

        outcome shouldBe PlayerOutcome.WIN
    }

    @Test
    fun `딜러와 플레이어가 모두 버스트한 경우 플레이어는 패배한다`() {
        val bustedPlayer = createBustedPlayer()
        val bustedDealer = createBustedDealer()

        val outcome = bustedPlayer.outcome(bustedDealer)

        outcome shouldBe PlayerOutcome.LOSE
    }

    @Test
    fun `플레이어가 블랙잭이고 딜러가 블랙잭이 아닌 경우 승리한다`() {
        val blackjackPlayer = createBlackjackPlayer()
        val dealer = createDealer()

        val outcome = blackjackPlayer.outcome(dealer)

        outcome shouldBe PlayerOutcome.WIN
    }

    @Test
    fun `플레이러와 딜러 모두 블랙잭이면 무승부다`() {
        val blackjackPlayer = createBlackjackPlayer()
        val blackjackDealer = createBlackjackDealer()

        val outcome = blackjackPlayer.outcome(blackjackDealer)

        outcome shouldBe PlayerOutcome.DRAW
    }

    @Test
    fun `플레이어의 점수가 딜러의 점수보다 높으면 승리한다`() {
        val player = createPlayer(StubDeck.from(Rank.JACK, Rank.TEN))
        val dealer = createDealer(StubDeck.from(Rank.TWO, Rank.THREE))

        val outcome = player.outcome(dealer)

        outcome shouldBe PlayerOutcome.WIN
    }

    @Test
    fun `플레이어 점수와 딜러의 점수가 같은 경우 무승부다`() {
        val player = createPlayer(StubDeck.from(Rank.FIVE, Rank.TEN))
        val dealer = createDealer(StubDeck.from(Rank.SEVEN, Rank.EIGHT))

        val outcome = player.outcome(dealer)

        outcome shouldBe PlayerOutcome.DRAW
    }

    @Test
    fun `플레이어의 점수가 딜러의 점수보다 낮으면 패배한다`() {
        val player = createPlayer(StubDeck.from(Rank.TWO, Rank.THREE))
        val dealer = createDealer(StubDeck.from(Rank.FOUR, Rank.FIVE))

        val outcome = player.outcome(dealer)

        outcome shouldBe PlayerOutcome.LOSE
    }

    companion object {
        @JvmStatic
        fun donePlayers() =
            listOf(
                Arguments.of(createBlackjackPlayer(), "블랙잭"),
                Arguments.of(createBustedPlayer(), "버스트"),
                Arguments.of(createStandingPlayer(), "스탠드"),
            )
    }
}
