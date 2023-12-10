create table IF NOT EXISTS product (
id_product bigserial not null,
description varchar(255),
name varchar(255),
price float(53) not null,
quantity integer not null,
primary key (id_product));