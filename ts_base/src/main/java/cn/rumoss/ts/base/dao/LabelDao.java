package cn.rumoss.ts.base.dao;

import cn.rumoss.ts.base.pojo.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 标签的数据访问层接口
 */
public interface LabelDao extends JpaRepository<Label, String>, JpaSpecificationExecutor<Label> {
    // JpaRepository提供了基本的增删改查,JpaSpecificationExecutor用于做复杂的条件查询
}
