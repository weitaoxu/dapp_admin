
package com.qkbus.modules.security.rest;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.qkbus.annotation.AnonymousAccess;
import com.qkbus.constant.RedisConstant;
import com.qkbus.exception.BadRequestException;
import com.qkbus.google.GoogleCheck;
import com.qkbus.initYml.CacheYml;
import com.qkbus.logging.aop.log.Log;
import com.qkbus.modules.security.config.LoginProperties;
import com.qkbus.modules.security.config.SecurityProperties;
import com.qkbus.modules.security.security.TokenProvider;
import com.qkbus.modules.security.security.vo.AuthUser;
import com.qkbus.modules.security.security.vo.JwtUser;
import com.qkbus.modules.security.service.OnlineUserService;
import com.qkbus.tools.domain.vo.EmailVo;
import com.qkbus.tools.service.EmailConfigService;
import com.qkbus.utils.*;
import com.wf.captcha.base.Captcha;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author 少林一枝花
 * @date 2021-11-23
 * 授权、根据token获取用户详细信息
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Api(tags = "系统：系统授权接口")
public class AuthController {

    @Value("${loginCode.expiration}")
    private Long expiration;
    @Value("${rsa.private_key}")
    private String privateKey;
    @Value("${single.login}")
    private Boolean singleLogin;
    private final SecurityProperties properties;
    private final RedisUtils redisUtils;
    private final UserDetailsService userDetailsService;
    private final OnlineUserService onlineUserService;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final EmailConfigService emailService;

    @Autowired
    private LoginProperties loginProperties;

    public AuthController(SecurityProperties properties, RedisUtils redisUtils, UserDetailsService userDetailsService,
                          OnlineUserService onlineUserService, TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder,
                          EmailConfigService emailService) {
        this.properties = properties;
        this.redisUtils = redisUtils;
        this.userDetailsService = userDetailsService;
        this.onlineUserService = onlineUserService;
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.emailService = emailService;
    }

    @Log("用户登录")
    @ApiOperation("登录授权")
    @AnonymousAccess
    @PostMapping(value = "/login")
    public ResponseEntity<Object> login(@Validated @RequestBody AuthUser authUser, HttpServletRequest request) {
        // 密码解密
        String password = RsaUtils.decryptByPrivateKey(privateKey, authUser.getPassword());
        // 查询验证码
        String code = (String) redisUtils.get(authUser.getUuid());
        // 清除验证码
        redisUtils.del(authUser.getUuid());
        if (StringUtils.isBlank(code)) {
            throw new BadRequestException("验证码不存在或已过期");
        }
        if (StringUtils.isBlank(authUser.getCode()) || !authUser.getCode().equalsIgnoreCase(code)) {
            throw new BadRequestException("验证码错误");
        }

        Authentication authentication = null;
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(authUser.getUsername(), password);
            authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } finally {
            if (authentication == null) {
                if (!CacheYml.getProfiles()) {
                    judgeLoginNum(authUser.getUsername());
                }
            }
        }
        // 生成令牌
        String token = tokenProvider.createToken(authentication);
        final JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
        //验证谷歌验证码  账号密码
        if (!CacheYml.getProfiles()) {
            if (!GoogleCheck.checkGoogleAuthKey(jwtUser.getGoogleKey(), authUser.getGoogleCode())) {
                judgeLoginNum(authUser.getUsername());
            }
        }
        // 保存在线信息
        onlineUserService.save(jwtUser, token, request);
        // 返回 token 与 用户信息
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", properties.getTokenStartWith() + token);
            put("user", jwtUser);
        }};
        if (singleLogin) {
            //踢掉之前已经登录的token
            onlineUserService.checkLoginOnUser(authUser.getUsername(), token);
        }
        //获取之前的IP地址
        Object o = redisUtils.get(RedisConstant.ADMIN_LOGIN_IP + authUser.getUsername());
        String ip = StringUtils.getIp(RequestHolder.getHttpServletRequest());
        if (Objects.isNull(o)) {
            //发送邮箱不同地址登录
            redisUtils.set(RedisConstant.ADMIN_LOGIN_IP + authUser.getUsername(), ip);
        } else {
            if (!ip.equals(o.toString()) && !CacheYml.getProfiles()) {
                //如果IP地址不一致 发送短新
                EmailVo emailVo = new EmailVo();
                String cityInfo = StringUtils.getCityInfo(ip);
                String content = "尊敬的用户，您的账号：" + jwtUser.getUsername() + ",于:" + DateUtil.date() + "异地登录。IP:" + ip + "(" + cityInfo + "),如非本人操作请及时修改密码!";
                emailVo.setContent(content);
                emailVo.setSubject("异地登录提醒");
                List<String> listTo = new ArrayList<String>();
                listTo.add(jwtUser.getEmail());
                emailVo.setTos(listTo);
                try {
                    emailService.send(emailVo, emailService.find());
                } catch (Exception e) {
                    log.error("发送邮箱验证码异常！");
                    e.printStackTrace();
                }
            }
        }
        return ResponseEntity.ok(authInfo);
    }

    public void judgeLoginNum(String userName) {
        userName = userName + ":" + DateUtil.date().toDateStr();
        //判断是否超过次数
        Long expire = redisUtils.getExpire(RedisConstant.FREEZE_ADMIN + userName);
        if (expire > 0) {
            Object o = redisUtils.get(RedisConstant.FREEZE_ADMIN_NUM + userName);
            throw new BadRequestException("您好!输入错误次数太多,请" + RedisConstant.FREEZE_ADMIN_LOCK * Integer.parseInt(o.toString()) / 60 + "分钟后再试!");
        }
        //输错次数 如果是 5的整倍数就进行冻结
        long remainder = redisUtils.setAdminTokenNum(userName, 1);
        if (remainder == 0) {
            Object o = redisUtils.get(RedisConstant.FREEZE_ADMIN_NUM + userName);
            throw new BadRequestException("您好!输入错误次数太多,请" + RedisConstant.FREEZE_ADMIN_LOCK * Integer.parseInt(o.toString()) / 60 + "分钟后再试!");
        } else {
            throw new BadRequestException("谷歌验证码或者密码错误,您还剩余" + (5 - remainder) + "次机会");
        }
    }

    @ApiOperation("获取用户信息")
    @GetMapping(value = "/info")
    public ResponseEntity<Object> getUserInfo() {
        JwtUser jwtUser = (JwtUser) userDetailsService.loadUserByUsername(SecurityUtils.getUsername());
        return ResponseEntity.ok(jwtUser);
    }

    @AnonymousAccess
    @ApiOperation("获取验证码")
    @GetMapping(value = "/code")
    public ResponseEntity<Object> getCode() {
        // 算术类型 https://gitee.com/whvse/EasyCaptcha
        Captcha captcha = loginProperties.getCaptcha();
        // 获取运算的结果
        String result = "";
        try {
            result = new Double(Double.parseDouble(captcha.text())).intValue() + "";
        } catch (Exception e) {
            result = captcha.text();
        }
        String uuid = properties.getCodeKey() + IdUtil.simpleUUID();
        // 保存
        redisUtils.set(uuid, result, expiration, TimeUnit.MINUTES);
        // 验证码信息
        Map<String, Object> imgResult = new HashMap<String, Object>(2) {{
            put("img", captcha.toBase64());
            put("uuid", uuid);
        }};
        return ResponseEntity.ok(imgResult);
    }


    @ApiOperation("退出登录")
    @AnonymousAccess
    @DeleteMapping(value = "/logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) {
        onlineUserService.logout(tokenProvider.getToken(request));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
