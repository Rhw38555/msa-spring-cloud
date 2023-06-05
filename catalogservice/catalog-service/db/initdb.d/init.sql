create table catalog (
                       id int auto_increment not null comment '카탈로그 index id',
                       product_id varchar(50) not null comment '카탈로그 상품 id',
                       product_name varchar(50) not null comment '카탈로그 상품 이름',
                       stock int default 0 comment '카탈로그 남은 수량',
                       unit_price int default 0 comment '카탈로그 가격',
                       created_at datetime default now() comment '카탈로그 생성시간',
                       primary key (id)
) comment '카탈로그 정보' collate = utf8mb4_bin;


insert into catalog(product_id, product_name, stock, unit_price)
values('CATALOG-001', 'Berlin', 100, 1500);
insert into catalog(product_id, product_name, stock, unit_price)
values('CATALOG-002', 'Tokyo', 110, 1000);
insert into catalog(product_id, product_name, stock, unit_price)
values('CATALOG-003', 'Stockholm', 120, 2000);
