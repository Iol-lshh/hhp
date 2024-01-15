![monitoring_pipeline](./monitoring_pipeline.png)
1. CloudWatch alarms 에 이벤트 발생 조건
2. SNS 로 이벤트 메세지 전달
3. Labmda function 이 SNS 를 구독
    - Slack WebHook API 호출
4. Slack 의 정해진 채널로 Alert 메세지 전송

