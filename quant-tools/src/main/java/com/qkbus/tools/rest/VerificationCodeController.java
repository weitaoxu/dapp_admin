
package com.qkbus.tools.rest;

import com.qkbus.enums.CodeEnum;
import com.qkbus.tools.domain.vo.EmailVo;
import com.qkbus.tools.service.EmailConfigService;
import com.qkbus.tools.service.VerificationCodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

/**
 * @author 少林一枝花
 * @date 2021-12-26
 */
@RestController
@RequestMapping("/api/code")
@Api(tags = "工具：验证码管理")
public class VerificationCodeController {

    private final VerificationCodeService verificationCodeService;

    private final EmailConfigService emailService;

    public VerificationCodeController(VerificationCodeService verificationCodeService, EmailConfigService emailService) {
        this.verificationCodeService = verificationCodeService;
        this.emailService = emailService;
    }

    @PostMapping(value = "/resetEmail")
    @ApiOperation("重置邮箱，发送验证码")
    public ResponseEntity<Object> resetEmail(String email) {
        EmailVo emailVo = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey());
        emailService.send(emailVo, emailService.find());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/email/resetPass")
    @ApiOperation("重置密码，发送验证码")
    public ResponseEntity<Object> resetPass(String email) {
        EmailVo emailVo = verificationCodeService.sendEmail(email, CodeEnum.EMAIL_RESET_PWD_CODE.getKey());
        emailService.send(emailVo, emailService.find());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/validated")
    @ApiOperation("验证码验证")
    public ResponseEntity<Object> validated(@RequestParam String email, @RequestParam String code, @RequestParam String codeEnumParam) {
        CodeEnum codeEnum = CodeEnum.find(codeEnumParam);
        switch (Objects.requireNonNull(codeEnum)) {
            case EMAIL_RESET_EMAIL_CODE:
                verificationCodeService.validated(CodeEnum.EMAIL_RESET_EMAIL_CODE.getKey() + email, code);
                break;
            case EMAIL_RESET_PWD_CODE:
                verificationCodeService.validated(CodeEnum.EMAIL_RESET_PWD_CODE.getKey() + email, code);
                break;
            case PHONE_RESET_EMAIL_CODE:
                verificationCodeService.validated(CodeEnum.PHONE_RESET_EMAIL_CODE.getKey() + email, code);
                break;
            case PHONE_RESET_PWD_CODE:
                verificationCodeService.validated(CodeEnum.PHONE_RESET_PWD_CODE.getKey() + email, code);
                break;
            default:
                break;
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
