CREATE TABLE book (book
  id bigint not NULL AUTO_INCREMENT,
  title  varchar(255) not null,
  description VARCHAR(255),
  primary key (id)
);

CREATE TABLE category (
  id bigint not NULL AUTO_INCREMENT,
  title  varchar(255) not null,
  primary key (id)
);

insert into book (title,description) values ('test1', 'test1');
insert into book (title,description) values ('test2', 'test2');
insert into book (title,description) values ('test3', 'test3');
insert into book (title,description) values ('test4', 'test4');

insert into category (title) values ('test1');
insert into category (title) values ('test2');
insert into category (title) values ('test3');
insert into category (title) values ('test4');