package com.kojikoji.gpt.interfaces;

import com.kojikoji.gpt.domain.security.service.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ApiAccessController
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/10/26 11:38
 * @Version
 */

@Slf4j
@RestController
public class ApiAccessController {

    @RequestMapping("/authorize")
    public ResponseEntity<Map<String, String>> authorize(String username, String password) {
        Map<String, String> map = new HashMap<>();
        // 账号密码校验
        if (!"inshion".equals(username) || !"123".equals(password)) {
            map.put("msg", "用户名密码错误");
            return ResponseEntity.ok(map);
        }
        // 校验通过生成token
        JwtUtil jwtUtil = new JwtUtil();
        Map<String, Object> claim = new HashMap<>();
        claim.put("username", username);
        String jwtToken = jwtUtil.encode(username, 5 * 60 * 1000, claim);
        map.put("msg", "授权成功");
        map.put("token", jwtToken);
        return ResponseEntity.ok(map);
    }

    @RequestMapping("/verify")
    public ResponseEntity<String> verify(String token) {
        log.info("验证 token: {}", token);
        return ResponseEntity.status(HttpStatus.OK).body("verify success!");
    }

    @RequestMapping("/success")
    public String success() {
        return "test success by inshion";
    }
}
