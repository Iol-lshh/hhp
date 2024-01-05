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
 * 우선적으로 비관 모드의 락을 획득한 트랜잭션이 종료될 때 까지, 
 * 대기하고, 쿼리를 진행
 * 이후의 쿼리 진행은 커밋 시점까지 트랜잭션 격리 레벨을 따른다.
 *
 * 비관적 락을 거는 조회 쿼리는, 락 획득이 목적이 되었을때, 비로소 아름답다..
 * 결론) 비관적 락을 거는 조회 쿼리는, 조회를 목적으로 하면 안될 것 같다...!
 *

 동시성 문제 해결 (2번 시도)
- 비관적 락은 락 얻는데만 사용하고,
- 실제 데이터 가져오는건 그 다음에 비관적 락 없이 조회하자!

 * ## 리드 언커밋
 * 트랜잭션 시작 시점부터 있는 row만 읽을 수 있다.
 */

