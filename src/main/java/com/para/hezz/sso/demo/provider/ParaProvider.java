package com.para.hezz.sso.demo.provider;

import com.alibaba.fastjson.JSON;
import com.para.hezz.sso.demo.dto.AccessTokenDTO;
import com.para.hezz.sso.demo.dto.ProfileDTO;
import com.para.hezz.sso.demo.dto.TokenDTO;
import com.para.hezz.sso.demo.dto.UserDTO;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ParaProvider {
    @Value("${oauth2.uri}")
    private String oauth2Uri;

    public String getAccessToken(AccessTokenDTO accessTokenDTO) {
        OkHttpClient client = new OkHttpClient();

        // 构建form表单
        RequestBody requestBody = new FormBody
                .Builder()
                .add("client_id",accessTokenDTO.getClient_id())
                .add("oauth_timestamp",accessTokenDTO.getOauth_timestamp())
                .add("client_secret",accessTokenDTO.getClient_secret())
                .add("code",accessTokenDTO.getCode())
                .add("grant_type",accessTokenDTO.getGrant_type())
                .add("redirect_uri",accessTokenDTO.getRedirect_uri())
                .add("nonce_str",accessTokenDTO.getNonce_str())
                .add("sign",accessTokenDTO.getSign())
                .build();

        // 构建传输表单的POST请求
        Request request = new Request.Builder()
                .url(oauth2Uri+"accessToken")
                .post(requestBody)
                .build();

        // 利用okHttp3模拟请求
        try (Response response = client.newCall(request).execute()) {
            TokenDTO token=JSON.parseObject(response.body().string(),TokenDTO.class);
            return token.getAccess_token();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


    public UserDTO getUser(ProfileDTO profileDTO) {
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody
                .Builder()
                .add("client_id",profileDTO.getClient_id())
                .add("oauth_timestamp",profileDTO.getOauth_timestamp())
                .add("client_secret",profileDTO.getClient_secret())
                .add("access_token",profileDTO.getAccess_token())
                .add("nonce_str",profileDTO.getNonce_str())
                .add("sign",profileDTO.getSign())
                .build();
        Request request = new Request.Builder()
                .url(oauth2Uri+"profile")
                .post(requestBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            UserDTO userDTO = JSON.parseObject(response.body().string(), UserDTO.class);
            return userDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

}
