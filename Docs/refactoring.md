
_# how?
- 캐싱 레디스
- 포인트가 차감시에, 차감할 수 있는가만 확인하면 된다.
- squash도 마찬가지로 상관 안해도 된다.
    - delete->insert sum은 그대로, 이므로
    - 업데이트를 없애자

- 포인트 복구 => insert +
- 구매 주문 => insert -
- 포인트 취소 => insert -
- 구매 취소 => insert +

- 20개 단위 squash
  - 1 트랜잭션에서
  - 대상 합쳐놓고
  - 대상 delete
  - 합친거 insert

- 차감 insert 시, 캐싱 레디스로부터, user의 포인트를 가져온다.
- point가 insert될 때 마다, 캐싱 요청하고 잊는다.
  - @CachePut 전략

- 캐싱된 것에 접근시, 동시성을 고려해야한다.
  - 한번에 하나만 캐싱 접근
    - Redisson


## 결론
1. 분산 락 조회 및 획득
2. @Cacheput 포인트 조회
  - 미리 포인트 합산을 가져다 놓는다.
