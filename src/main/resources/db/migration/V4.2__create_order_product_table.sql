CREATE TABLE IF NOT EXISTS order_product (
order_id bigint not null,
product_id bigint not null,
primary key (order_id, product_id),
constraint fk_order_product_product foreign key (product_id) references product,
constraint fk_order_product_order_store foreign key (order_id) references order_store)