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

}
