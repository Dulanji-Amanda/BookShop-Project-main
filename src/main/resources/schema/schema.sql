drop database bookshop;

create database IF NOT EXISTS bookshop;

use bookshop;

CREATE TABLE user (
                      User_Id VARCHAR(10) PRIMARY KEY,
                      firstName VARCHAR(50) NOT NULL,
                      lastName VARCHAR(50) NOT NULL,
                      username VARCHAR(50) NOT NULL UNIQUE,
                      email VARCHAR(100) NOT NULL UNIQUE,
                      password VARCHAR(100) NOT NULL
);


create table stock(
                      Stock_Id varchar(10) primary key,
                      Name varchar(50),
                      User_Id varchar(10),
                      FOREIGN KEY(User_Id) REFERENCES user(User_Id)
                          ON UPDATE CASCADE ON DELETE CASCADE
);

create table employee(
                         Emp_Id varchar(10) primary key,
                         Name varchar(50),
                         Contact int (10),
                         User_Id varchar(10),
                         FOREIGN KEY(User_Id) REFERENCES user(User_Id)
                             ON UPDATE CASCADE ON DELETE CASCADE
);



create table Salary(
                       Salary_Id varchar (10) primary key,
                       Date date,
                       Amount decimal (10,2),
                       Emp_Id varchar(10),
                       FOREIGN KEY(Emp_Id) REFERENCES employee(Emp_Id)
                           ON UPDATE CASCADE ON DELETE CASCADE
);

create table customer(
                         Cust_Id varchar(10) primary key,
                         Cust_Name varchar(50),
                         Contact int (10),
                         Address varchar(50),
                         User_Id varchar(10),
                         FOREIGN KEY(User_Id) REFERENCES user(User_Id)
                             ON UPDATE CASCADE ON DELETE CASCADE
);

create table supplier(
                         Sup_Id varchar(10) primary key,
                         Name varchar(50),
                         Contact int (10),
                         User_Id varchar(10),
                         FOREIGN KEY(User_Id) REFERENCES user(User_Id)
                             ON UPDATE CASCADE ON DELETE CASCADE
);


create table orders(
                       Order_Id varchar(10) primary key,
                       Description varchar(50),
                       Order_qty int (10),
                       Cust_Id  varchar(10),
                       FOREIGN KEY(Cust_Id) REFERENCES customer(Cust_Id)
                           ON UPDATE CASCADE ON DELETE CASCADE
);


create table payment(
                        Payment_Id varchar(10) primary key,
                        Amount decimal (10,2),
                        Contact int (10),
                        Payment_Date date,
                        Order_Id varchar(10),
                        FOREIGN KEY(Order_Id) REFERENCES orders(Order_Id)
                            ON UPDATE CASCADE ON DELETE CASCADE

);

CREATE TABLE item(
                     Item_Id VARCHAR(10) primary key,
                     Item_Name VARCHAR (50),
                     Qty INT (10),
                     Price DECIMAL (10,2)
);


CREATE TABLE order_item_detail(
                             Order_Id VARCHAR(6),
                             Item_Id VARCHAR(6),
                             Date date,
                             Amount DECIMAL(8,2),
                             CONSTRAINT FOREIGN KEY (Order_Id) REFERENCES orders(Order_Id) on Delete Cascade on Update Cascade,
                             CONSTRAINT FOREIGN KEY (Item_Id) REFERENCES item(Item_Id) on Delete Cascade on Update Cascade
);

CREATE TABLE stock_item_detail(
                                  Stock_Id VARCHAR(6),
                                  Item_Id VARCHAR(6),
                                  CONSTRAINT FOREIGN KEY (Stock_Id) REFERENCES stock (Stock_Id) on Delete Cascade on Update Cascade,
                                  CONSTRAINT FOREIGN KEY (Item_Id) REFERENCES item(Item_Id) on Delete Cascade on Update Cascade
);



CREATE TABLE supplier_stock_detail(
                                      Date date,
                                      Stock_Id VARCHAR(6),
                                      Sup_Id VARCHAR(6),
                                      CONSTRAINT FOREIGN KEY (Stock_Id) REFERENCES stock(Stock_Id) on Delete Cascade on Update Cascade,
                                      CONSTRAINT FOREIGN KEY (Sup_Id) REFERENCES supplier(Sup_Id) on Delete Cascade on Update Cascade
);
