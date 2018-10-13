package cn.rumoss.ts.user.filter;

import cn.rumoss.ts.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * JWT校验token拦截器类
 */
@Component
public class JwtFilter extends HandlerInterceptorAdapter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("经过了JWT拦截器...");

        final String authHeader = request.getHeader("Authorization");// final 防止被恶意修改
        if(null!=authHeader &&  authHeader.startsWith("Bearer ")){
            final String token = authHeader.substring(7);// 提取Token
            Claims claims = jwtUtil.parseJWT(token);
            if(null!=claims){
                if("admin".equals(claims.get("roles"))){// 如果是管理员
                    request.setAttribute("admin_claims",claims);
                }
                if("user".equals(claims.get("roles"))){// 如果是用户
                    request.setAttribute("user_claims",claims);
                }
            }
        }

        System.out.println("出去了JWT拦截器...");
        return true;
    }
}
