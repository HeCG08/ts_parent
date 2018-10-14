package cn.rumoss.ts.manager.config;

import cn.rumoss.ts.util.JwtUtil;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Zuul过滤器
 */
@Component
public class ManagerFilter extends ZuulFilter {

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 设置过滤器类型
     *  pre ： 可以在请求路由之前被调用
     *  route ： 在路由请求时候被调用
     *  post ： 在 route 和 error 过滤器之后被调用
     *  error ： 处理请求时发生错误时被调用

     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 设置过滤器的执行顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;//数值越大,执行的优先级越低
    }

    /**
     * 是否执行过滤器
     * true:执行该过滤器
     * false:不执行过滤器
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器的逻辑
     * 如果return null代表放行
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("执行了Manager Zuul的过滤器...");

        // 获取当前请求的头信息,向header中添加鉴权信息
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        if("OPTIONS".equals(request.getMethod())){
            return null;
        }
        String url = request.getRequestURL().toString();
        if (url.indexOf("/admin/login") > 0){
            System.out.println("登录页面： " + url);
            return null;
        }

        String authHeader = request.getHeader("Authorization");// 获取头信息
        if (authHeader!=null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);// 截取Token
            Claims claims = jwtUtil.parseJWT(token);
            if(null!=claims){
                if("admin".equals(claims.get("roles"))){
                    //把头信息带回到网关后续的请求里面
                    currentContext.addZuulRequestHeader("Authorization",authHeader);
                    System.out.println("Token验证通过，添加了头信息 " + authHeader);
                    return null;
                }
            }
        }

        currentContext.setSendZuulResponse(false);// 终止运行
        currentContext.setResponseStatusCode(401);// Http状态码 需要登录
        currentContext.setResponseBody("无权访问");
        currentContext.getResponse().setContentType("text/html;charset=UTF-8");
        return null;
    }
}
