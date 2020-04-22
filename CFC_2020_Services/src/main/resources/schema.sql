DROP TABLE IF EXISTS COMMPOST;
 
CREATE TABLE COMMPOST (
  id INT AUTO_INCREMENT PRIMARY KEY,
  user_name VARCHAR(250) NOT NULL,
  product_Type VARCHAR(250) NOT NULL,
  sub_type VARCHAR(250) NOT NULL,
  latitude VARCHAR(250) NOT NULL,
  longitude VARCHAR(250) NOT NULL,
  post_type VARCHAR(250) NOT NULL,
  status VARCHAR(250) NULL,
  quantity VARCHAR(250) NULL
);


DROP TABLE IF EXISTS COMMUSERS;

CREATE TABLE COMMUSERS (
  id INT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(100) NOT NULL,
  user_name VARCHAR(50) NOT NULL,
  password VARCHAR(50) NOT NULL
);

DROP TABLE IF EXISTS COMMNOTIFY;

CREATE TABLE COMMNOTIFY (
id INT AUTO_INCREMENT PRIMARY KEY,
posted_user_name VARCHAR(100),
receiver_user_name VARCHAR(100),
latitude VARCHAR(250),
longitude VARCHAR(250),
submission_date DATE
);