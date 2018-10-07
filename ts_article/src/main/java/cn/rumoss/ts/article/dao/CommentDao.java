package cn.rumoss.ts.article.dao;

import cn.rumoss.ts.article.pojo.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * 评论DAO
 */
public interface CommentDao extends MongoRepository<Comment,String> {

    /**
     * 根据文章ID查询品论列表
     * @param atticleid
     * @return
     */
    public List<Comment> findByArticleid(String atticleid);

}
