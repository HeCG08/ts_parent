package cn.rumoss.ts.article.service;

import cn.rumoss.ts.article.dao.ArticleDao;
import cn.rumoss.ts.article.dao.CommentDao;
import cn.rumoss.ts.article.pojo.Article;
import cn.rumoss.ts.article.pojo.Comment;
import cn.rumoss.ts.util.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 服务层
 * 
 * @author Administrator
 *
 */
@Service
public class CommentService {

	@Autowired
	private CommentDao commentDao;

	@Autowired
	private IdWorker idWorker;

	/**
	 * 新增评论
	 * @param comment
	 */
	public void add(Comment comment) {
		comment.set_id(idWorker.nextId() + "");
		commentDao.save(comment);
	}

    /**
     * 根据文章ID获取评论列表
     * @param articleid
     * @return
     */
    public List<Comment> findByArticleId(String articleid){
        return commentDao.findByArticleid(articleid);
    }

}
