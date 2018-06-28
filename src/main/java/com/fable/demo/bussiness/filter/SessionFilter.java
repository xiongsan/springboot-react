package com.fable.demo.bussiness.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;
import java.util.ResourceBundle;

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
@Component
@ServletComponentScan
@WebFilter(urlPatterns = "/*",filterName = "sessionFilter")
public class SessionFilter implements Filter {

    @Value("${loginUrl}")
    private String loginUrl;

    @Override
    public void init(FilterConfig filterConfig){
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
// 登陆url
        String uri= httpRequest.getRequestURI();
        String path = uri.substring(uri.lastIndexOf("/"));
        //首页和登陆请求无需判断
        //SecurityUtils.getSubject().getSession().getAttribute("user") == null
        if(path.contains("login.jsp")){//shiro默认登录页走到这session肯定为空了
            if (isAjaxRequest(httpRequest)) {
                httpResponse.addHeader("sessionstatus", "timeOut");
                httpResponse.addHeader("loginPath", loginUrl);
                chain.doFilter(request, response);// 不可少，否则请求会出错
            } else {
                String contextPath = httpRequest.getContextPath();//容器名
                String total = httpRequest.getRequestURL().toString();
                String requestRoot = total.substring(0, total.indexOf(contextPath)) + contextPath;
                String str = "<script language='javascript'>"
                        + "window.top.location.href='"
                        + requestRoot+loginUrl
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

    private boolean isAjaxRequest( HttpServletRequest httpRequest){
        return httpRequest.getHeader("x-requested-with") != null
                && httpRequest.getHeader("x-requested-with")
                .equalsIgnoreCase("XMLHttpRequest");
    }
}
