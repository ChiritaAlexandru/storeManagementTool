CREATE TABLE IF NOT EXISTS user_store
 ( id_user BIGSERIAL  PRIMARY KEY,
  email varchar(255) UNIQUE,
  name varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  user_type varchar(255) check (user_type in ('CLIENT','ADMIN')) DEFAULT 'CLIENT'
  );