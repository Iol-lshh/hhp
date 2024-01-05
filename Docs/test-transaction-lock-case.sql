-- psql
-- 준비
delete from tb_point 
where id > 1;

insert into tb_point
select * from(
	select 2 as id, 200 as count, 1 as user_id, 1 as from_type, 2 as from_id 
	union all select 3 as id, 300 as count, 2 as user_id, 1 as from_type, 3 as from_id
)tmp


/* 실험 과정
 * 
 * 쓰레드 t1, t2
 * 
 * 1. t1, t2 트랜잭션을 시작한다.
 * 2. t1 조회한다.
 * 3. t2 조회를 시도한다.
 * 
*/

begin transaction
--isolation level serializable 
--isolation level repeatable read
--isolation level read committed 
isolation level read uncommitted

--read write
--read only
;

select * 
from tb_point
where user_id = 1 for update

update tb_point
set count = 222
where id = 2

insert into tb_point(count, user_id, from_type, from_id)
values(-10,1,1,1)

select * from tb_point
where user_id = 1

--rollback
commit
/*
 * 결론
 * 
 * # 포스트그레
 * ## 비관적 락 모드
 * 트랜잭션 레벨과 관계없이, 
 * 우선적으로 '비관적 락 모드의 쿼리(select)'를 실행한 트랜잭션이 종료될 때 까지,
 * 대기만 하고, 그대로 그 '비관적 락 모드의 쿼리(select)'를 진행
 * 이후의 쿼리 진행은 커밋 시점까지 트랜잭션 격리 레벨을 따른다.

 - 왜 '비관적' 락일까?
    - 비관적이라서,, 회계사가 비관적으로 생각하듯이,, 안될껄 먼저 생각해서.. 충돌날꺼라고 생각하고 만드는거라
    - 즉, 충돌날껄 기다려서 row를 가져오는 것일뿐이다. (row는 이미 뭐 가져올지 골랐고, 락이 걸려있으니 **건져내는 것**을 **기다리는 것**일 뿐이다!!!!!!!! (update야 건져낼때 문제 없지만, insert시 문제, delete는...? 확인해보자... # todo - delete 확인)
        - todo - 같은 비관락이 아니면, insert update 다 할 수 있는 듯 했는데.. 이 부분도 다시 확인하자..
 *
 * 비관적 락을 거는 조회 쿼리는, 락 획득이 목적이 되었을때, 비로소 생각대로 진행되었다..
 * 결론) 비관적 락을 거는 조회 쿼리는, 조회를 목적으로 하면 안될 것 같다...!
 *

동시성 문제 해결 (해결 방안: 2번 시도)
1. 비관적 락은 락을 얻는데만 사용하고,
2. 실제 데이터 가져오는건 그 다음에 비관적 락 없이 조회하자!
 # todo - 확인 내용을 캡쳐하고 문서화 및 정리할 것! - 트랜잭션 레벨( 1. serializable  2. repeatable read  3. read committed   4. read uncommitted ) * 락( 1. 비관적 락  2. update  3. insert ) * 디비 엔진( 1. H2  2. PostgreSQL  3. MySQL ) = 4 * 3 * 3 종 테스트 결과 작성



 * ## 리드 언커밋
 * 트랜잭션 시작 시점부터 있는 row만 읽을 수 있다.
 */

