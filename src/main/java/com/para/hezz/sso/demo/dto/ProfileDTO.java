package com.para.hezz.sso.demo.dto;

import lombok.Data;

@Data
public class ProfileDTO {
    private String client_id;
    private String client_secret;
    private String access_token;
    private String oauth_timestamp;
    private String nonce_str;
    private String sign;
}
