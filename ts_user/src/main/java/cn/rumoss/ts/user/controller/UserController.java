package cn.rumoss.ts.user.controller;

import java.util.List;
import java.util.Map;

import cn.rumoss.ts.entity.PageResult;
import cn.rumoss.ts.entity.Result;
import cn.rumoss.ts.entity.StatusCode;
import cn.rumoss.ts.user.pojo.User;
import cn.rumoss.ts.user.service.UserService;
import cn.rumoss.ts.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", userService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findById(id));
    }


    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<User> pageList = userService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<User>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", userService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param user
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(String code , @RequestBody User user) {
        userService.add(code,user);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param user
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody User user, @PathVariable String id) {
        user.setId(id);
        userService.update(user);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        Claims claims = (Claims) request.getAttribute("admin_claims");
        if (null==claims){
            return new Result(false, StatusCode.ACCESSERROR, "权限不足");
        }
        userService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 发送手机验证码
     *
     * @param mobile
     * @return
     */
    @RequestMapping(value = "/sendsms/{mobile}",method = RequestMethod.POST)
    public Result sendsms(@PathVariable String mobile){
       userService.sendsms(mobile);
       return new Result(true,StatusCode.OK,"发送成功");
    }

    /**
     * 用户注册
     * @param user
     * @param code
     * @return
     */
    @RequestMapping(value = "/register/{code}",method = RequestMethod.POST)
    public Result register(@PathVariable String code,@RequestBody User user){
        userService.add(code,user);
        return new Result(true,StatusCode.OK,"注册成功");
    }

    /**
     * 用户登陆
     * @param mobile
     * @param password
     * @return
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Result login(@RequestParam String mobile, @RequestParam String password){
        User user = userService.findByMobileAndPassword(mobile,password);
        if (null!=user){
            return new Result(true,StatusCode.OK,"登陆成功");
        }
        return new Result(false,StatusCode.LOGINERROR,"用户名或密码错误");
    }

}
