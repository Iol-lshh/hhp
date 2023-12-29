INSERT INTO tb_user(name)
SELECT name
FROM (
         SELECT 'AAA' AS name UNION
         SELECT 'BBB' AS name UNION
         SELECT 'SSS' AS name
     ) tmp;

INSERT INTO tb_product(name, price, stock_cnt)
SELECT *
FROM (
         SELECT 'ApplePhone' AS name, 30 as price, 10 as stock_cnt UNION
         SELECT 'BravoCone', 5, 100 UNION
         SELECT 'Single', 1, 1
     ) tmp;


INSERT INTO tb_payment(exchanged, user_id)
SELECT *
FROM (
         SELECT 100 AS count, 1 as user_id
     ) tmp;


INSERT INTO tb_point(count, user_id, from_type, from_id)
SELECT *
FROM (
         SELECT 100 AS count, 1 as user_id, 1 as from_type, 1 as from_id
     ) tmp;
