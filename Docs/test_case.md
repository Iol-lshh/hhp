### 테스트 케이스 종류
1. 원하는 동작
2. 잘못된 입력에 대한 처리
3. api가 기대한 응답이 아닐 때 어떤 에러를 내는지
### 방법
1. 모종의 결과값을 내는 stub을 만들어두고, 주입한다.



# todo
# 결제 Payment::exchange
- 결제 성공
- 결제 생성 실패
    - paymentRepository.save가 no id

# 주문 Order::order 실패
- 주문 성공
- 확인되지 않는 유저 => 테스트하지 않고 유저로 넘긴다.
    - userService.find(userId)
- 상품 확인 실패 => 테스트하지 않고 상품으로 넘긴다.
    - productService.find
- 상품 재고 갯수 부족 => 상품으로 넘긴다.
    - stockService.isAllInStock(purchaseRequestList)
    - 동시성 확인
- 포인트 부족
    - purchaseService.isPayable(userId, purchaseRequestList)
    - 동시성 확인

# 구매 Purchase
## ::isPayable
- 구매 가능 확인
- 구매 불가능 확인

## ::purchase
- 상품 확인 실패(부족)
- 포인트 차감 실패(부족)

# 포인트 Point
## ::payment
- 정상 확인

## ::purchase
- 구매 성공 아이디 존재. 갯수 동일
  - 동시성 확인
    - 포인트 1번만 차감 확인

## :: favorite
- 정상 확인

# 상품 Product
## ::isInStock
- 부족할 경우
- 충분할 경우

## ::store
- 정상 확인

## ::unstore
- 정상 처리 갯수 확인
  - 비즈니스 확인
  - 동시성 확인
.
