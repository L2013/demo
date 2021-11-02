package com.yinuo.demo.server.mgt.controller;


import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.http.HttpUtil;
import com.yinuo.base.aop.CatchAndLog;
import com.yinuo.demo.server.mgt.consts.PathConstant;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.Date;
import java.util.Map;

/**
 * @author liang
 */
@Controller
public class IndexController {

    @GetMapping(PathConstant.DOWNLOAD)
    @CatchAndLog
    public void download(HttpServletResponse response) throws IOException {
        setHeader(response);
        extracted(response, new FileInputStream("D://绘图1.vsdx"));
    }

    private void setHeader(HttpServletResponse response) throws UnsupportedEncodingException {
        response.setHeader("content-type", "application/octet-stream");
        response.setContentType("application/octet-stream");
        // 下载文件能正常显示中文
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("绘图1.vsdx", "UTF-8"));
    }

    @GetMapping(PathConstant.DOWNLOAD_FROM_REMOTE)
    @CatchAndLog
    public void downloadFromRemote(HttpServletResponse response) throws IOException {
        setHeader(response);
        extracted(response, HttpUtil.createGet("http://localhost:8080/download").execute().bodyStream());
    }

    private void extracted(HttpServletResponse response, InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len;
        OutputStream out = response.getOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, len);
            out.flush();
        }
    }

    @GetMapping(PathConstant.PAGE_INDEX)
    public String index(Model model) {
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
