
CREATE TABLE user(
  id SERIAL PRIMARY KEY,
  name VARCHAR(20)
);



CREATE TABLE order(
    id SERIAL PRIMARY KEY,
    user_id INTEGER
);
CREATE INDEX order_user_id ON order(user_id);



CREATE TABLE point(
    id SERIAL PRIMARY KEY,
    count INTEGER,
    user_id INTEGER,
    from_type SMALLINT,
    from_id INTEGER
);
CREATE INDEX point_user_id ON point(user_id);
CREATE INDEX point_from_id_from_type ON point(from_id, from_type);


CREATE TABLE payment(
    id SERIAL PRIMARY KEY,
    exchanged INTEGER,
    user_id INTEGER
);
CREATE INDEX payment_user_id ON payment(user_id);


CREATE TABLE product(
    id SERIAL PRIMARY KEY,
    name VARCHAR(20),
    price INTEGER
);


CREATE TABLE purchase(
    id SERIAL PRIMARY KEY,
    paid INTEGER,
    user_id INTEGER,
    product_id INTEGER
);
CREATE INDEX purchase_product_id ON purchase(product_id);
CREATE INDEX purchase_user_id ON purchase(user_id);



CREATE TABLE stock(
      id SERIAL PRIMARY KEY,
      product_id INTEGER
      purchase_id INTEGER,
);
CREATE INDEX stock_product_id ON stock(product_id);
CREATE INDEX stock_purchase_id ON stock(purchase_id);



CREATE VIEW point_view(
    select
        user_id,
        sum(p.count) as remain
    from point p
    group by user_id
);





