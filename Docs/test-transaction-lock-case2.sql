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
--isolation level serializable 			-- 상대 트랜잭션의 커밋 전까지는, insert 못 읽는다. 커밋 된 것도 못 읽는다. (공유락) 쓰기를 시도했다가 커밋된 버전이 다르면, 그때부터 트랜잭션이 블락된다. 커밋했다가, insert까지 확인해서 다르면 롤백된다.
--isolation level repeatable read		-- 상대 트랜잭션의 커밋 전까지는, insert 못 읽는다. 커밋 된 것도 못 읽는다. (공유락) 쓰기를 시도했다가 커밋된 버전이 다르면, 그때부터 트랜잭션이 블락된다.
--isolation level read committed 		-- 상대 트랜잭션의 커밋 전까지는, insert 못 읽는다. 커밋 된 것은 읽는다. 쓰기를 시도했다가, 쓰기락이 걸려있다면 커밋이 완료될때까지 기다려서 반영한다.
isolation level read uncommitted 		-- 상대 트랜잭션의 커밋 전까지는, insert 못 읽는다. 커밋 된 것은 읽는다. 쓰기를 시도했다가, 쓰기락이 걸려있다면 커밋이 완료될때까지 기다려서 반영한다.

--read write
--read only
;

select * 
from tb_point
where user_id = 1 for update

update tb_point
set count = 223
where id = 2

insert into tb_point(count, user_id, from_type, from_id)
select * from(
	select -20 as count, 1 as user_id, 2 as from_type, 1 as from_id 
--	union all select -10 as count, 1 as user_id, 2 as from_type, 1 as from_id
)tmp

select * from tb_point
where user_id = 1

--rollback
commit


