package com.hmdp.utils;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.hmdp.dto.UserDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginInterceptor implements HandlerInterceptor {

    private StringRedisTemplate stringRedisTemplate;

    public LoginInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//        //TODO 1. 获取请求头中的token
//        String token = request.getHeader("authorization");
//        //TODO 2. 基于token获取redis中的用户
//        if (StrUtil.isBlank(token)){
//            response.setStatus(403);
//            return false;
//        }
//        String key = RedisConstants.LOGIN_USER_KEY + token;
//        Map<Object, Object> userMap = stringRedisTemplate.opsForHash().entries(key);
//        // TODO 3. 判断用户是否存在
//        if (userMap.isEmpty()){
//            // 4. 不存在直接拦截，返回401
//            response.setStatus(403);
//            return false;
//        }
//
//        // TODO 5. 将查询到的Hash数据转为UserDTO
//        UserDTO userDTO = BeanUtil.fillBeanWithMap(userMap, new UserDTO(), false);
//        // TODO 刷新token有效期
//        stringRedisTemplate.expire(key,RedisConstants.LOGIN_USER_TTL, TimeUnit.MINUTES);
//        UserHolder.saveUser(userDTO);
//
//
////        //1. 获取session
////        HttpSession session = request.getSession();
////        //2， 获取session中的用户
////        Object user = session.getAttribute("user");
////        //3. 判断用户是否存在
////        if (user == null){
////            //4， 不存在。拦截
////            response.setStatus(403);
////            return false;
////        }
//
////        //5. 存在，保存用户信息到threadLocal
////        UserHolder.saveUser((UserDTO) user);



        //判断是否需要拦截(ThreadLocal是否有用户)
        if (UserHolder.getUser() == null){
            //没有，需要拦截，设置状态码
            response.setStatus(401);
            //拦截
            return false;
        }
        //6. 放行
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
