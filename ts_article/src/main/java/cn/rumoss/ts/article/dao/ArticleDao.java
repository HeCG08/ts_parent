package cn.rumoss.ts.article.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.rumoss.ts.article.pojo.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ArticleDao extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{

    /**
     * 文章审核功能
     * @param id
     */
    @Modifying
    @Query("update Article set state='1' where id=?1")
    public void examine(String id);

    /**
     * 文章点赞功能
     * @param id
     */
    @Modifying
    @Query("update Article set thumbup=thumbup+1 where id=?1")
    public void updateThumbup(String id);

}
