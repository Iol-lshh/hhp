### 테스트 케이스 종류
1. 원하는 동작
2. 잘못된 입력에 대한 처리
3. api가 기대한 응답이 아닐 때 어떤 에러를 내는지


# 결제 Payment::exchange
- 결제 생성 실패
    - paymentRepository.save가 no id
- 포인트 생성 실패
    - pointService.payment가 no id

# 주문 Order::order 실패
- 확인되지 않는 유저
    - userService.find(userId)
- 상품 확인 실패
    - productService.find
- 상품 재고 갯수 부족
    - stockService.isAllInStock(purchaseRequestList)
- 포인트 부족
    - purchaseService.isPayable(userId, purchaseRequestList)
- 정상 주문 완료

# 구매 Purchase
## ::isPayable
- 구매 가능 확인
- 구매 불가능 확인

## ::purchase
- 상품 확인 실패
- 포인트 차감 실패

# 포인트 Point
## ::payment
- 결제 성공 아이디 존재

## ::purchase
- 구매 성공 아이디 존재. 갯수 동일

## ::squash
- 처리 확인

# 재고 Stock
## ::isAllInStock
- 부족할 경우
- 충분할 경우

## ::input
- 정상 처리 갯수 확인

## ::output
- 정상 처리 갯수 확인

