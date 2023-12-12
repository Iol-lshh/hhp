


CREATE TABLE tb_user(
  id SERIAL PRIMARY KEY,
  name VARCHAR(20)
);

CREATE TABLE tb_order(
    id SERIAL PRIMARY KEY,
    user_id INTEGER,
    state INTEGER
);
CREATE INDEX order_user_id ON tb_order(user_id);



CREATE TABLE tb_point(
    id SERIAL PRIMARY KEY,
    count INTEGER,
    user_id INTEGER,
    from_type SMALLINT,
    from_id INTEGER
);
CREATE INDEX point_user_id ON tb_point(user_id);
CREATE INDEX point_from_id_from_type ON tb_point(from_id, from_type);


CREATE TABLE tb_payment(
    id SERIAL PRIMARY KEY,
    exchanged INTEGER,
    user_id INTEGER
);
CREATE INDEX payment_user_id ON tb_payment(user_id);


CREATE TABLE tb_product(
    id SERIAL PRIMARY KEY,
    name VARCHAR(20),
    price INTEGER
);


CREATE TABLE tb_purchase(
    id SERIAL PRIMARY KEY,
    paid INTEGER,
    user_id INTEGER,
    product_id INTEGER,
    order_id INTEGER
);
CREATE INDEX purchase_product_id ON tb_purchase(product_id);
CREATE INDEX purchase_user_id ON tb_purchase(user_id);



CREATE TABLE tb_stock(
      id SERIAL PRIMARY KEY,
      product_id INTEGER,
      purchase_id INTEGER
);
CREATE INDEX stock_product_id ON tb_stock(product_id);
CREATE INDEX stock_purchase_id ON tb_stock(purchase_id);


CREATE VIEW v_point AS
select
    user_id,
    sum(p.count) as remain
from tb_point p
group by user_id;


/*----------------------------------------*/
INSERT INTO tb_user(name)
SELECT name
FROM (
    SELECT 'AAA' AS name UNION
    SELECT 'BBB' AS name UNION
    SELECT 'SSS' AS name
) tmp;

INSERT INTO tb_product(name, price)
SELECT name, price
FROM (
     SELECT 'Apple' AS name, 10 as price UNION
     SELECT 'BravoCone', 8 UNION
     SELECT 'Single', 1
 ) tmp;




