package com.example.orderservice.dto;

/* producer 전달 시 페이로드 정보 */

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payload {
    private String order_id;
    private String user_id;
    private String product_id;
    private int qty;
    private int unit_price;
    private int total_price;
}
