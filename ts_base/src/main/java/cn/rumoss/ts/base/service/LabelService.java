package cn.rumoss.ts.base.service;

import cn.rumoss.ts.base.dao.LabelDao;
import cn.rumoss.ts.base.pojo.Label;
import cn.rumoss.ts.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    /**
     * 构建查询条件
     * @param searchMap
     * @return
     */
    private Specification<Label> createSpecification(Map searchMap) {
        return new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicateList = new ArrayList<>();
                if(searchMap.get("labelname")!=null && !"".equals(searchMap.get("labelname"))){
                    predicateList.add(criteriaBuilder.like(root.get("labelname").as(String.class),"%"+ (String)searchMap.get("labelname")+"%"));
                }
                if(searchMap.get("state")!=null && !"".equals(searchMap.get("state"))){
                    predicateList.add(criteriaBuilder.equal(root.get("state").as(String.class),(String)searchMap.get("state")));
                }
                if(searchMap.get("recommend")!=null && !"".equals(searchMap.get("recommend"))){
                    predicateList.add(   criteriaBuilder.equal(root.get("recommend").as(String.class),(String)searchMap.get("recommend")));
                }
                return criteriaBuilder.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
    }

    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    public List<Label> findSearch(Map searchMap) {
        Specification specification = createSpecification(searchMap);
        return labelDao.findAll(specification);
    }

    /**
     * 分页条件查询
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    public Page<Label> findSearch(Map searchMap,int page,int size) {
        Specification specification = createSpecification(searchMap);
        PageRequest pageRequest = PageRequest.of(page-1,size);
        return labelDao.findAll(specification,pageRequest);
    }

}

