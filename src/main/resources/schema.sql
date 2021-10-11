create table CUSTOMER
(
    ID   VARCHAR(255) not null primary key,
    NAME VARCHAR(255)
);

create table CUSTOMER_CONTACT
(
    ID          VARCHAR(255) not null primary key,
    CONTACT     VARCHAR(255),
    TYPE        VARCHAR(255),
    CUSTOMER_ID VARCHAR(255),
    constraint FK_C_ID foreign key (CUSTOMER_ID) references CUSTOMER (ID)
);

