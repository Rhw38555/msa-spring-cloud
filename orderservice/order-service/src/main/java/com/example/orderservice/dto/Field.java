package com.example.orderservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/* producer 전달 시 필드 정보 */
@Data
@AllArgsConstructor
public class Field {
    private String type; // 변수 타입
    private boolean optional; //
    private String field; // 필드명
}
