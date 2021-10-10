package com.yinuo.fiss.server.mgt.configuration;

import com.yinuo.base.aop.CatchLogAspect;
import com.yinuo.fiss.server.mgt.aop.RestCatchLogAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author liang
 */
@Configuration
public class AspectAutoConfiguration {
    @Bean
    @ConditionalOnMissingBean(CatchLogAspect.class)
    public RestCatchLogAspect getRestCatchLogAspect() {
        return new RestCatchLogAspect();
    }
}
