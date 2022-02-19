
package com.qkbus.tools.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/*
 *
 * @author ：少林一枝花

 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailVo {

    /**
     * 收件人，支持多个收件人
     */
    @NotEmpty
    private List<String> tos;

    @NotBlank
    private String subject;

    @NotBlank
    private String content;
}
