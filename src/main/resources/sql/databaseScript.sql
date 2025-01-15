DROP DATABASE IF EXISTS PSRex;
CREATE DATABASE PSRex;
use PSRex;


DROP USER IF EXISTS 'pizzauser'@'localhost';
CREATE USER 'pizzauser'@'localhost' IDENTIFIED by 'pizzatime';
GRANT SELECT,INSERT,UPDATE,DELETE ON PSRex.* TO 'pizzauser'@'localhost';
GRANT CREATE, DROP ON PSRex.* TO 'pizzauser'@'localhost';

