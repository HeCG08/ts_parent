package cn.rumoss.ts.spit.service;

import cn.rumoss.ts.spit.dao.SpitDao;
import cn.rumoss.ts.spit.pojo.Spit;
import cn.rumoss.ts.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 吐槽服务的业务服务层
 */
@Service
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    /***
     * 查询全部数据
     * @return
     */
    public List<Spit> findAll(){
        return spitDao.findAll();
    }

    /**
     * 根据ID查询数据
     * @param id
     * @return
     */
    public Spit findById(String id){
        return spitDao.findById(id).get();
    }

    /**
     * 增加吐槽
     * @param spit
     */
    public void add(Spit spit){
        spit.set_id(idWorker.nextId()+"");// 主键
        spit.setPublishtime(new Date());// 发布日期
        spit.setVisits(0);// 浏览量
        spit.setShare(0);// 分享数
        spit.setThumbup(0);// 点赞数
        spit.setComment(0);// 回复数
        spit.setState("1");// 状态

        String parentid = spit.getParentid();
        //判断哪些是吐槽的评论
        if (parentid != null && !"".equals(parentid)) {
            //更新该评论对应的吐槽的回复数+1
            //1.创建查询对象
            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(parentid));
            //2.创建修改对象
            Update update = new Update();
            update.inc("comment", 1);
            mongoTemplate.updateFirst(query, update, "spit");
        }

        spitDao.save(spit);
    }

    /**
     * 修改吐槽
     * @param spit
     */
    public void update(Spit spit){
        spitDao.save(spit);
    }

    /**
     *  删除吐槽
     * @param id
     */
    public void deleteById(String id){
        spitDao.deleteById(id);
    }

    /**
     * 根据上级ID查询吐槽列表
     *
     * @param parentId
     * @param page
     * @param size
     * @return
     */
    public Page<Spit> findByParentid(String parentId, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return spitDao.findByParentid(parentId, pageRequest);
    }

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 吐槽点赞功能
     *
     * @param id
     */
    public void updateThumbup(String id) {
        //创建查询对象
        Query query = new Query();
        //封装查询条件
        query.addCriteria(Criteria.where("_id").is(id));
        //创建修改对象
        Update update = new Update();
        //设置修改内容
        update.inc("thumbup", 1);
        //执行修改操作
        mongoTemplate.updateFirst(query, update, "spit");
    }

    /**
     * 增加访问量
     * @param id
     */
    public void visit(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.inc("visits", 1);
        mongoTemplate.updateFirst(query, update, "spit");
    }

    /**
     * 增加分享
     * @param id
     */
    public void share(String id) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));
        Update update = new Update();
        update.inc("share", 1);
        mongoTemplate.updateFirst(query, update, "spit");
    }

}
