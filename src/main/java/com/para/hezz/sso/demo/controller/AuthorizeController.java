package com.para.hezz.sso.demo.controller;

import com.para.hezz.sso.demo.dto.AccessTokenDTO;
import com.para.hezz.sso.demo.dto.ProfileDTO;
import com.para.hezz.sso.demo.dto.UserDTO;
import com.para.hezz.sso.demo.provider.ParaProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthorizeController {

    @Autowired
    private ParaProvider paraProvider;

    @Value("${client.id}")
    private String clientId;
    @Value("${client.secret}")
    private String clientSecret;
    @Value("${redirect.uri}")
    private String redirectUri;
    @Value("${oauth2.uri}")
    private String oauth2Uri;

    /**
     * 回调函数
     * @param code code
     * @param request 操作session
     * @return 页面
     */
    @RequestMapping(value = "/callback",method = RequestMethod.GET)
    public String callback(@RequestParam(name = "code",required = false)String code,
                           HttpServletRequest request){
        // 如果code为空，则跳转到第三方登陆页
        if (code == null) {
            return "redirect:"+oauth2Uri+"authorize?"+"client_id="+clientId+"&response_type=code&redirect_uri="+redirectUri;
        }

        AccessTokenDTO accessTokenDto = new AccessTokenDTO();
        accessTokenDto.setClient_id(clientId);
        accessTokenDto.setClient_secret(clientSecret);
        accessTokenDto.setCode(code);
        accessTokenDto.setGrant_type("authorization_code");
        accessTokenDto.setRedirect_uri(redirectUri);
        // 获取系统时间戳
        accessTokenDto.setOauth_timestamp(String.valueOf(System.currentTimeMillis()));
        // 生成随机数
        accessTokenDto.setNonce_str(String.valueOf(Math.round(Math.random()*1000)));
        // 为获取Access Token生成签名
        String sign = "client_id"+ clientId+"client_secret"+clientSecret+"code"+code+"grant_typeauthorization_codenonce_str"+accessTokenDto.getNonce_str()+"oauth_timestamp"+accessTokenDto.getOauth_timestamp()+"redirect_uri"+redirectUri+"appkey"+"56eea6c8e76fc4262a4a2816dfd79c7fdb4781a9433da5509d3ee649125447d8"+clientSecret;
        String signTemp = DigestUtils.md5DigestAsHex(sign.getBytes()).toUpperCase();
        accessTokenDto.setSign(signTemp);

        // 获取Access Token
        String accessToken = paraProvider.getAccessToken(accessTokenDto);

        ProfileDTO profileDTO = new ProfileDTO();
        profileDTO.setClient_id(clientId);
        profileDTO.setClient_secret(clientSecret);
        // 获取系统时间戳
        profileDTO.setOauth_timestamp(String.valueOf(System.currentTimeMillis()));
        // 生成随机数
        profileDTO.setNonce_str(String.valueOf(Math.round(Math.random()*1000)));
        profileDTO.setAccess_token(accessToken);
        //为请求profile API生成签名
        String sign2 = "access_token"+accessToken+"client_id"+clientId+"client_secret"+clientSecret+"nonce_str"+profileDTO.getNonce_str()+"oauth_timestamp"+profileDTO.getOauth_timestamp()+"appkey"+"56eea6c8e76fc4262a4a2816dfd79c7fdb4781a9433da5509d3ee649125447d8"+clientSecret;
        String signTemp2 = DigestUtils.md5DigestAsHex(sign2.getBytes()).toUpperCase();
        profileDTO.setSign(signTemp2);
        UserDTO userDTO = paraProvider.getUser(profileDTO);
        request.getSession().setAttribute("user",userDTO.getId());


        return "redirect:/";
    }
}
