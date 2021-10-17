package com.yinuo.demo.server.mgt.controller;


import cn.hutool.core.collection.CollectionUtil;
import com.yinuo.demo.server.mgt.consts.PathConstant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * @author liang
 */
@Controller
public class IndexController {

    @GetMapping(PathConstant.PAGE_INDEX)
    public String index(Model model) throws IOException {
        model.addAttribute("date", new Date());
        return "index";
    }

    @GetMapping(PathConstant.PAGE_INDEX_ORIGIN)
    @ResponseBody
    public void indexOrigin(HttpServletResponse response) throws IOException {
        TemplateEngine templateEngine = getTemplateEngine();
        Context context = getContext(null);
        System.out.println(templateEngine.process("index", context));
        templateEngine.process("index", context, response.getWriter());
    }

    private Context getContext(Map<String, Object> map) {
        Context context = new Context();
        if (CollectionUtil.isNotEmpty(map)) {
            context.setVariables(map);
        }
        return context;
    }

    private TemplateEngine getTemplateEngine() {
        TemplateEngine templateEngine = new TemplateEngine();
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        // 无配置模板解析路径,则代码配置
        // 2. 创建模板解析器 并设置相关属性
        resolver.setPrefix("templates/");
        resolver.setSuffix(".html");
        // 不允许重复设置 否则会报错
        templateEngine.setTemplateResolver(resolver);
        return templateEngine;
    }
}
