package cn.rumoss.ts.base.service;

import cn.rumoss.ts.base.dao.LabelDao;
import cn.rumoss.ts.base.pojo.Label;
import cn.rumoss.ts.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 标签业务层
 */
@Service
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询所有标签列表
     * @return
     */
    public List<Label> findAll(){
        return labelDao.findAll();
    }

    /**
     * 根据id查询标签对象
     * @param id
     * @return
     */
    public Label findById(String id){
        return labelDao.findById(id).get();
    }

    /**
     * 添加标签
     * @param label
     */
    public void addLabel(Label label){
        label.setId(idWorker.nextId() + "");
        labelDao.save(label);
    }

    /**
     * 更新标签
     * @param label
     */
    public void updateLabel(Label label){
        labelDao.save(label);
    }

    /**
     * 根据id删除标签
     * @param id
     */
    public void delLabelById(String id){
        labelDao.deleteById(id);
    }
}

