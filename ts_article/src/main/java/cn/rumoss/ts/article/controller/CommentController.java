package cn.rumoss.ts.article.controller;

import cn.rumoss.ts.article.pojo.Comment;
import cn.rumoss.ts.article.service.CommentService;
import cn.rumoss.ts.entity.Result;
import cn.rumoss.ts.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Comment comment){
        commentService.add(comment);
        return new Result(true, StatusCode.OK, "提交成功");
    }

    /**
     * 根据文章ID获取评论列表
     * @param articleid
     * @return
     */
    @RequestMapping(value = "/article/{articleid}", method = RequestMethod.GET)
    public Result findByArticleId(@PathVariable String articleid){
        List<Comment> commentList = commentService.findByArticleId(articleid);
        return new Result(true, StatusCode.OK, "查询成功", commentList);
    }

}
