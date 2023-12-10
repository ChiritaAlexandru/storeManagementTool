CREATE TABLE IF NOT EXISTS order_store
 (id_order bigserial not null,
 amount float(53) not null,
 order_time timestamp with time zone DEFAULT CURRENT_TIMESTAMP,
 id_user bigint,
 primary key (id_order));

 ALTER TABLE IF  EXISTS order_store ADD CONSTRAINT fk_order_store_user_store foreign key (id_user) REFERENCES user_store