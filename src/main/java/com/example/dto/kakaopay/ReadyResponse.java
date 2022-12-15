package com.example.dto.kakaopay;

import lombok.Data;

@Data
public class ReadyResponse {
    private String tid;
	private String next_redirect_pc_url;
	private String partner_order_id;
	private Long no;
}
