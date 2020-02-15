package com.para.hezz.sso.demo.controller;


import com.para.hezz.sso.demo.dto.UserDTO;
import com.para.hezz.sso.demo.service.AuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthorizeController {

    @Autowired
    private AuthorizeService authorizeService;

    @Value("${client.id}")
    private String clientId;
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
                           HttpServletRequest request) throws Exception {
        // 如果code为空，则跳转到第三方登陆页
        if (code == null) {
            return "redirect:"+oauth2Uri+"authorize?"+"client_id="+clientId+"&response_type=code&redirect_uri="+redirectUri;
        }
        String accessToken = authorizeService.getAccessToken(code);
        UserDTO userDTO = authorizeService.getUser(accessToken);
        // 将用户名存入session
        request.getSession().setAttribute("user",userDTO.getId());
        return "redirect:/";
    }
}
