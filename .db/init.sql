
--
CREATE TABLE tb_user(
  id SERIAL PRIMARY KEY,
  name VARCHAR(20)
);
--
CREATE TABLE tb_order(
    id SERIAL PRIMARY KEY,
    user_id INTEGER,
    state INTEGER
);
CREATE INDEX order_user_id ON tb_order(user_id);


--
CREATE TABLE tb_point(
    id SERIAL PRIMARY KEY,
    count INTEGER,
    user_id INTEGER,
    from_type SMALLINT,
    from_id INTEGER
);
CREATE INDEX point_user_id ON tb_point(user_id);
CREATE INDEX point_from_id_from_type ON tb_point(from_id, from_type);

--
CREATE TABLE tb_payment(
    id SERIAL PRIMARY KEY,
    exchanged INTEGER,
    user_id INTEGER
);
CREATE INDEX payment_user_id ON tb_payment(user_id);

--
CREATE TABLE tb_product(
    id SERIAL PRIMARY KEY,
    name VARCHAR(20),
    price INTEGER,
    stock_cnt INTEGER
);

--
CREATE TABLE tb_purchase(
    id SERIAL PRIMARY KEY,
    paid INTEGER,
    count INTEGER,
    user_id INTEGER,
    product_id INTEGER,
    order_id INTEGER,
    state INTEGER
);
CREATE INDEX purchase_product_id ON tb_purchase(product_id, count);
CREATE INDEX purchase_user_id ON tb_purchase(user_id);

--
CREATE VIEW v_point AS
select
    user_id,
    sum(p.count) as remain
from tb_point p
group by user_id;

--
CREATE VIEW v_top_purchased_product AS
with sum_paid_cnt AS (
    select
        tpc.product_id,
        sum(tpc.count) as paid_cnt
    from tb_purchase tpc
    group by tpc.product_id
)
select
    tp.*,
    spc.paid_cnt,
    row_number() over(order by spc.paid_cnt desc) as order_by_paid_cnt
from sum_paid_cnt spc
inner join tb_product tp
    on tp.id = spc.product_id
order by paid_cnt desc;

/*----------------------------------------*/
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
         SELECT 'Apple' AS name, 10 as price, 10 as stock_cnt UNION
         SELECT 'BravoCone', 8, 100 UNION
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


