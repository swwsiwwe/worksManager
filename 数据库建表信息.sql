CREATE TABLE user (
id int(11) AUTO_INCREMENT,
  name varchar(30) ,
  studentID varchar(30) unique,
  password varchar(30),
  type varchar(10),
  level int,
  primary key (id)
)  DEFAULT CHARSET=utf8;

CREATE TABLE work_user (
id int(11) AUTO_INCREMENT,
  studentID varchar(30),
  date datetime,
  status varchar(10),
  work varchar(30),
  primary key (id),
  foreign key(studentID) references user(studentID)
)  DEFAULT CHARSET=utf8;

CREATE TABLE teacher (
id int(11) AUTO_INCREMENT,
  name varchar(30) ,
  tel varchar(30),
  type varchar(10),
  workID varchar(30) unique,
  password varchar(30),
  primary key(id)
)  DEFAULT CHARSET=utf8;

CREATE TABLE work (
id int(11) AUTO_INCREMENT,
  name varchar(30),
  work varchar(30),
  workID varchar(30),
  start datetime,
  end datetime,
  level int,
  type varchar(10),
  primary key(id),
  foreign key(workID) references teacher(workID)
)  DEFAULT CHARSET=utf8;
