package cn.rumoss.ts.spit.controller;

import cn.rumoss.ts.entity.PageResult;
import cn.rumoss.ts.entity.Result;
import cn.rumoss.ts.entity.StatusCode;
import cn.rumoss.ts.spit.pojo.Spit;
import cn.rumoss.ts.spit.service.SpitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin //跨域注解
@RequestMapping("/spit")
public class SpitController {

    @Autowired
    private SpitService spitService;

    /**
     * 查询全部记录
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        List<Spit> list = spitService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",list);
    }

    /**
     *  根据ID查询吐槽
     * @param id
     * @return
     */
    @RequestMapping( value="/{id}" ,method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
        Spit spit = spitService.findById(id);
        return new Result(true, StatusCode.OK,"查询成功",spit);
    }

    /**
     * 增加吐槽
     * @param spit
     * @return
     */
    @RequestMapping( method = RequestMethod.POST)
    public  Result add( @RequestBody Spit spit ){
        spitService.add(spit);
        return new Result(true,StatusCode.OK,"增加成功");
    }

    /**
     * 修改吐槽
     * @param spit
     * @param id
     * @return
     */
    @RequestMapping( value ="/{id}" ,method = RequestMethod.PUT)
    public  Result update( @RequestBody Spit spit ,@PathVariable String id){
        spit.set_id(id);
        spitService.update(spit);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     *  删除吐槽
     * @param id
     * @return
     */
    @RequestMapping( value ="/{id}" ,method = RequestMethod.DELETE)
    public  Result deleteById(@PathVariable  String id){
        spitService.deleteById(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 根据上级ID查询吐槽列表
     * @param parentId
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/comment/{parentId}/{page}/{size}", method = RequestMethod.GET)
    public Result findByParentId(@PathVariable String parentId, @PathVariable int page, @PathVariable int size) {
        Page<Spit> pageList = spitService.findByParentid(parentId, page, size);
        PageResult<Spit> pageResult = new PageResult<>(pageList.getTotalElements(), pageList.getContent());
        return new Result(true, StatusCode.OK, "查询成功", pageResult);
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 吐槽点赞
     * @param id
     * @return
     */
    @RequestMapping(value = "/thumbup/{id}", method = RequestMethod.PUT)
    public Result updateThumbup(@PathVariable String id){
        // 判断用户是否点过赞
        String userid = "001";
        //1. 从redis 查询该用户是否已经点赞过
        String flag = (String) redisTemplate.opsForValue().get("thumbup_"+userid+"_"+id);
        if (null != flag ){
            //点赞过
            return new Result(false,StatusCode.REPERROR,"已经点赞过");
        }
        spitService.updateThumbup(id);
        //2.把数据存入redis
        redisTemplate.opsForValue().set("thumbup_"+userid+"_"+id,"1");
        return new Result(true, StatusCode.OK, "点赞成功");
    }

    /**
     * 浏览量
     * @param id
     * @return
     */
    @RequestMapping(value = "/visited/{id}",method = RequestMethod.PUT)
    public Result visit(@PathVariable String id){
        spitService.visit(id);
        return new Result(true,StatusCode.OK,"访问成功");
    }

    /**
     * 转发分享
     * @param id
     * @return
     */
    @RequestMapping(value = "/shared/{id}",method = RequestMethod.PUT)
    public Result share(@PathVariable String id){
        spitService.share(id);
        return new Result(true,StatusCode.OK,"分享成功");
    }

}
