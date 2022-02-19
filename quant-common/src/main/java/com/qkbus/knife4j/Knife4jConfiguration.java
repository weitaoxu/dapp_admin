package com.qkbus.knife4j;

import com.fasterxml.classmate.TypeResolver;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.data.domain.Pageable;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.AlternateTypeRule;
import springfox.documentation.schema.AlternateTypeRuleConvention;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static springfox.documentation.schema.AlternateTypeRules.newRule;

@Configuration
@EnableSwagger2WebMvc
public class Knife4jConfiguration {


    @Value("${knife4j.enabled}")
    private Boolean enabled;

    @Value("${knife4j.title}")
    private String title;

    @Value("${knife4j.version}")
    private String version;

    @Value("${knife4j.serverUrl}")
    private String serverUrl;

    @Bean
    public Docket defaultApi2() {
        Docket docket = new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(enabled)
                //分组名称
                .groupName("2.X版本")
                //是否显示
                .select()
                //这里指定Controller扫描包路径
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build();
        return docket;
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //页面标题
                .title(title)
                //创建人
                .contact(new Contact("x'x'x", "xxx", "xxx"))
                //版本号
                .version(version)
                .termsOfServiceUrl(serverUrl)
                //描述
                .description(title)
                .build();
    }
}

/**
 * 将Pageable转换展示在swagger中
 */
@Configuration
class SwaggerDataConfig {
    @Bean
    public AlternateTypeRuleConvention pageableConvention(final TypeResolver resolver) {
        return new AlternateTypeRuleConvention() {
            @Override
            public int getOrder() {
                return Ordered.HIGHEST_PRECEDENCE;
            }

            @Override
            public List<AlternateTypeRule> rules() {
                return newArrayList(newRule(resolver.resolve(Pageable.class), resolver.resolve(Page.class)));
            }
        };
    }

    @ApiModel
    @Data
    private static class Page {
        @ApiModelProperty("页码 (0..N)")
        private Integer page;

        @ApiModelProperty("每页显示的数目")
        private Integer size;

        @ApiModelProperty("以下列格式排序标准：property[,asc | desc]。 默认排序顺序为升序。 支持多种排序条件：如：id,asc")
        private List<String> sort;
    }
}
