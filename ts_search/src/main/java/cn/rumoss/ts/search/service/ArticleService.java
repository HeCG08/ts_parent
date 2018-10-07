package cn.rumoss.ts.search.service;

import cn.rumoss.ts.entity.PageResult;
import cn.rumoss.ts.search.dao.ArticleDao;
import cn.rumoss.ts.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    /**
     * 增加文章
     * @param article
     */
    public void add(Article article){
        articleDao.save(article);
    }

    /**
     * 根据关键字查询文章列表
     * @param keywords
     * @param page
     * @param size
     * @return
     */
    public PageResult findByKeywords(String keywords, int page, int size){
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        Page<Article> pageList = articleDao.findByTitleOrContentLike(keywords, keywords, pageRequest);
        return new PageResult(pageList.getTotalElements(), pageList.getContent());
    }

}
