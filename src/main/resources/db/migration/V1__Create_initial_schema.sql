create table users (
  id serial,
  username text,
  password text,
  email text
);

insert into users(username, password, email) values('admin', '$31$16$9VqHXYGMhdWH2Q9cTxwie_E6NsltXJJLOGNn-tw6jtM', 'admin@example.com');