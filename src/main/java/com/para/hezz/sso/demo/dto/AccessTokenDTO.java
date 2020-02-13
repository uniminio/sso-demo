package com.para.hezz.sso.demo.dto;

import lombok.Data;

@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String oauth_timestamp;
    private String grant_type;
    private String nonce_str;
    private String sign;
}
