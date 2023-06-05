create table orders (
                       id int auto_increment not null comment '주문 index id',
                       user_id varchar(50) not null comment '주문한 사용자 id',
                       product_id varchar(50) not null comment '상품 아이디',
                       order_id varchar(50) not null comment '주문 아이디',
                       qty int default 0 comment '주문 수량',
                       unit_price int default 0 comment '사용자 아이디',
                       total_price int default 0 comment '사용자 아이디',
                       created_at datetime default now() comment '주문 생성시간',
                       primary key (id)
) comment '주문 정보' collate = utf8mb4_bin;