package com.para.hezz.sso.demo.service;

import com.alibaba.fastjson.JSON;
import com.para.hezz.sso.demo.common.utils.HttpRequestUtil;
import com.para.hezz.sso.demo.dto.TokenDTO;
import com.para.hezz.sso.demo.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class AuthorizeService {

    @Value("${client.id}")
    private String clientId;
    @Value("${client.secret}")
    private String clientSecret;
    @Value("${redirect.uri}")
    private String redirectUri;
    @Value("${oauth2.uri}")
    private String oauth2Uri;
    @Value("${appkey}")
    private String appKey;

    /**
     * 根据code得到access token
     * @param code code
     * @return access token
     */
    public String getAccessToken(String code) throws Exception {

        String result = HttpRequestUtil.getResult(oauth2Uri+"accessToken",HttpRequestUtil.getAccessTokenParam(clientId,clientSecret,redirectUri,code));
        TokenDTO token= JSON.parseObject(result,TokenDTO.class);
        return token.getAccess_token();
    }

    /**
     * 根据access token返回用户信息
     * @param accessToken access token
     * @return 用户信息
     */
    public UserDTO getUser(String accessToken)  {
        try {
            String result = HttpRequestUtil.getResult(oauth2Uri+"profile",HttpRequestUtil.getUserParam(clientId,clientSecret,accessToken));
            UserDTO userDTO = JSON.parseObject(result,UserDTO.class);
            return userDTO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
