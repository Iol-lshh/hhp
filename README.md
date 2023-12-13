# phase
- 프로파일 버저닝
    - 로컬: `local`
    - 개발: `dev`
    - 스테이징: `stg`
    - 실: `prod`

- 스웨거: `/swagger-ui/index.html`
- 헬스체크: `/actuator/health`



# 비즈니스
- 이커머스

## 데이터 구조
![data_architecture](./Docs/da.png)

### 사용자 User
- 결제, 구매 주체
- 객체 데이터

### 주문 Order
- 구매 묶음
- 로그성 데이터

### 구매 Purchase, 결제 Payment
- 포인트 일대일 선택 매핑
  - 구매는 음수 포인트 paid
  - 결제는 양수 포인트
- 로그성 데이터

### 포인트 Point
- 구매 재화
- 누적성 데이터 (증감 요소)

### 상품 Product
- 상품 정보 
- 마스터 데이터

### 재고 Stock
- 단일 상품 정보
- 구매와 일대일 매핑 가능
- 객체 데이터

# API
### 주문 내역
```shell
curl -X 'GET' \
  'http://localhost:8078/order/all/1' \
  -H 'accept: application/json'
```
```json
{
  "result": {
    "result": "Ok",
    "value": [
      {
        "id": 0,
        "userId": 0,
        "state": "Start"
      }
    ]
  }
}
```
### 주문
```shell
curl -X 'POST' \
  'http://localhost:8078/order/purchase' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "userId": 1,
  "requestPurchaseList": [
    {
      "productId": 1,
      "count": 10
    },
{
      "productId": 2,
      "count": 2
    }
  ]
}'
```
```json
{
  "result": {
    "result": "OK",
    "value": {
      "id": 1,
      "userId": 1,
      "state": "OK"
    }
  }
}
```

### 포인트 생성
```shell
curl -X 'POST' \
  'http://localhost:8078/pay/exchange' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "userId": 1,
  "toNeed": 1000
}'
```
```json
{
  "result": {
    "result": "OK",
    "value": {
      "id": 1,
      "into": 1000,
      "userId": 1
    }
  }
}
```
### 포인트 압축
```shell
curl -X 'GET' \
  'http://localhost:8078/point/squash' \
  -H 'accept: application/json'
```
```json
{
  "result": {
    "result": "Ok",
    "value": [
      {
        "id": 0,
        "count": 0,
        "userId": 0,
        "fromType": 0,
        "fromId": 0
      }
    ]
  }
}
```

### 유저 포인트 압축
```shell
curl -X 'GET' \
  'http://localhost:8078/point/squash/{userId}' \
  -H 'accept: application/json'
```
```json
{
  "result": {
    "result": "Ok",
    "value": {
      "id": 0,
      "count": 0,
      "userId": 0,
      "fromType": 0,
      "fromId": 0
    }
  }
}
```

### 포인트 잔액 확인
```shell
curl -X 'GET' \
  'http://localhost:8078/point/remain/{userId}' \
  -H 'accept: application/json'
```
```json
{
  "result": {
    "result": "Ok",
    "value": 0
  }
}
```

### 포인트 사용 내역
```shell
curl -X 'GET' \
  'http://localhost:8078/point/history/{userId}' \
  -H 'accept: application/json'
```
```json
{
  "result": {
    "result": "Ok",
    "value": [
      {
        "id": 0,
        "count": 0,
        "userId": 0,
        "fromType": 0,
        "fromId": 0
      }
    ]
  }
}
```

### 상품 전체
```shell
curl -X 'GET' \
  'http://localhost:8078/product/all' \
  -H 'accept: application/json'
```
```json
{
  "result": {
    "result": "Ok",
    "value": [
      {
        "id": 0,
        "name": "string",
        "price": 0
      }
    ]
  }
}
```

### 상품 생성, 갱신
```shell
curl -X 'POST' \
'http://localhost:8078/product/' \
-H 'accept: application/json' \
-H 'Content-Type: application/json' \
-d '{
"id": 0,
"name": "string",
"price": 0
}'
```
```json
{
  "result": {
    "result": "Start",
    "value": {
      "id": 0,
      "name": "string",
      "price": 0
    }
  }
}
```

### 전체 기간 인기 판매 상품
```shell
curl -X 'GET' \
  'http://localhost:8078/purchase/favorite/11' \
  -H 'accept: application/json'
```
```json
{
  "timestamp": "2023-12-13T06:19:42.376+00:00",
  "status": 500,
  "error": "Internal Server Error",
  "path": "/purchase/favorite/11"
}
```

### 재고 확인
```shell
curl -X 'GET' \
  'http://localhost:8078/stock/all' \
  -H 'accept: application/json'
```
```json
{
  "result": {
    "result": "Start",
    "value": [
      {
        "id": 0,
        "productId": 0,
        "purchaseId": 0
      }
    ]
  }
}
```

### 재고 추가
```shell
curl -X 'POST' \
  'http://localhost:8078/stock/input' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "productId": 2,
  "cnt": 100
}'
```
```json
{
  "result": {
    "result": "Start",
    "value": [
      {
        "id": 0,
        "productId": 0,
        "purchaseId": 0
      }
    ]
  }
}
```

### user 리스트
```shell
curl -X 'GET' \
  'http://localhost:8078/user/all' \
  -H 'accept: application/json'
```
```json
{
  "result": {
    "result": "Start",
    "value": [
      {
        "id": 0,
        "name": "string"
      }
    ]
  }
}
```

### user 생성, 갱신
```shell
curl -X 'POST' \
  'http://localhost:8078/stock/input' \
  -H 'accept: application/json' \
  -H 'Content-Type: application/json' \
  -d '{
  "id": 0,
  "name": "string"
}'
```
```json
{
  "result": {
    "result": "Start",
    "value": {
      "id": 0,
      "name": "string"
    }
  }
}
```