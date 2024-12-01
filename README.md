# kotlin-blackjack

## 블랙잭 규칙

블랙잭은 카지노 게임의 한 종류로, 플레이어와 딜러가 참여하는 게임이다. 각 참가자는 카드의 합이 21 또는 21에 가장 가까운 수가 되도록 카드를 뽑아야 한다.
플레이어의 점수가 21을 초과하면 패배한다 ("bust").

블랙잭은 52장의 표준 카드 덱을 사용합니다. 각 카드는 에이스(A), 2, 3, 4, 5, 6, 7, 8, 9, 10, 잭(J), 퀸(Q), 또는 킹(K)의 랭크을 가집니다.
카드의 랭크가 카드의 점수를 결정합니다. 숫자 카드 2에서 10은 해당 숫자만큼의 가치가 있고, 페이스 카드(잭, 퀸, 킹)는 10점으로 계산됩니다.
에이스는 1점 또는 11점으로 계산되며, 손의 합이 21에 더 가깝지만 넘지 않도록 하는 값을 선택합니다.

각 라운드는 다음과 같이 진행됩니다:

- 딜러는 덱을 섞고 각 플레이어에게 카드 두 장을 앞면이 보이도록 (랭크가 보이게) 나눠줍니다.
- 딜러는 첫 번째 플레이어에게 "히트" (덱에서 추가 카드를 받는 것) 또는 "스탠드" (더 이상 카드를 받지 않는 것)를 원하는지 묻습니다.
- 만약 그들이 "스탠드"를 선택하면, 그들의 턴은 끝나고 게임은 다음 플레이어로 넘어갑니다.
- 플레이어가 "히트"를 선택하면, 딜러는 덱에서 카드를 한 장 더 앞면이 보이도록 나눠주며, 추가된 카드에 따라 그들의 손패 점수가 변경됩니다.
- 이로 인해 점수가 21을 초과하면, 그들은 "버스트"되어 즉시 그 라운드에서 패배하고 게임은 다음 플레이어로 계속됩니다.
- 버스트되지 않았다면, 그들은 다시 "히트"나 "스탠드"를 선택할 수 있는 기회를 갖습니다.

## 2단계 - 블랙잭

### 기능 요구사항

- [x] 게임에 참여할 사람들의 이름을 입력받는다.
- 게임 운영
  - [x] 플레이어의 카드의 합을 구할 수 있다.
  - [x] Ace 가 포함된 경우 1점 또는 11점으로 21에 더 가까운 값을 선택한다.
  - [x] 게임 시작 시 플레이어들에게 2장의 카드를 나눠준다.
  - [ ] 카드를 더 받을지 여부를 입력받는다.
  - [ ] 21을 초과하지 않은 경우 카드를 계속 뽑을 수 있다.
  - [ ] 21을 초과하거나 원할 경우 카드를 더 받지 않을 수 있다.
  - [ ] 모든 플레이어들이 턴 종료 시 게임이 종료된다.
- [x] 플레이어들의 초기 상태를 화면에 출력한다.
- [x] 각 플레이어의 카드들을 회면에 출력할 수 있다.
- [ ] 게임이 종료된 후 각 플레이어들의 점수를 출력한다.

게임 예시:

```text
게임에 참여할 사람의 이름을 입력하세요.(쉼표 기준으로 분리)
pobi,jason

pobi, jason에게 2장의 나누었습니다.
pobi카드: 2하트, 8스페이드
jason카드: 7클로버, K스페이드

pobi는 한장의 카드를 더 받겠습니까?(예는 y, 아니오는 n)
y
pobi카드: 2하트, 8스페이드, A클로버
pobi는 한장의 카드를 더 받겠습니까?(예는 y, 아니오는 n)
n
jason은 한장의 카드를 더 받겠습니까?(예는 y, 아니오는 n)
n
jason카드: 7클로버, K스페이드

pobi카드: 2하트, 8스페이드, A클로버 - 결과: 21
jason카드: 7클로버, K스페이드 - 결과: 17
```
