create table users (
  id bigserial primary key,
  username text not null unique,
  password text not null,
  email text not null unique
);

insert into users(username, password, email) values('admin', '$31$16$9VqHXYGMhdWH2Q9cTxwie_E6NsltXJJLOGNn-tw6jtM', 'admin@example.com');

create table flowers (
  id bigserial primary key,
  name text not null unique,
  description text
);

create table user_flowers (
  id bigserial,
  user_id integer references users(id) on delete cascade,
  flower_id integer references flowers(id) on delete cascade,
  notes text,
  primary key(id, user_id, flower_id)
);