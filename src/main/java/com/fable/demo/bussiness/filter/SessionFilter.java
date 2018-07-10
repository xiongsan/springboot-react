package com.fable.demo.bussiness.filter;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p>
 * Title :
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Author :Hairui
 * Date :2018/5/24
 * Time :18:01
 * </p>
 * <p>
 * Department :
 * </p>
 * <p> Copyright : 江苏飞博软件股份有限公司 </p>
 */
public class SessionFilter implements Filter {

    private  static final Logger logger = LoggerFactory.getLogger(SessionFilter.class);

    @Value("${loginUrl}")
    private String loginUrl;

    @Override
    public void init(FilterConfig filterConfig) {
        logger.info("sessionFilter init .....");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
// 登陆url
        String uri = httpRequest.getRequestURI();
        //首页和登陆请求无需判断
        boolean isAuthenticated = SecurityUtils.getSubject().isAuthenticated();

        if (!isAuthenticated&&uri.contains("baseController")&&!uri.contains("baseController/noAuth")) {
            if (isAjaxRequest(httpRequest)) {
                httpResponse.addHeader("sessionstatus", "timeOut");
                httpResponse.addHeader("loginPath", loginUrl);
            } else {
                String contextPath = httpRequest.getContextPath();//容器名
                String total = httpRequest.getRequestURL().toString();
                String requestRoot = total.substring(0, total.indexOf(contextPath)) + contextPath;
                String str = "<script language='javascript'>"
                        + "window.top.location.href='"
                        + requestRoot + loginUrl
                        + "';</script>";
                response.setContentType("text/html;charset=UTF-8");
                try {
                    PrintWriter writer = response.getWriter();
                    writer.write(str);
                    writer.flush();
                    writer.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        chain.doFilter(httpRequest, response);
    }

    @Override
    public void destroy() {

    }

    private boolean isAjaxRequest(HttpServletRequest httpRequest) {
        return httpRequest.getHeader("x-requested-with") != null
                && httpRequest.getHeader("x-requested-with")
                .equalsIgnoreCase("XMLHttpRequest");
    }
}
