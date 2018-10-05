package cn.rumoss.ts.base.controller;

import cn.rumoss.ts.base.pojo.Label;
import cn.rumoss.ts.base.service.LabelService;
import cn.rumoss.ts.entity.Result;
import cn.rumoss.ts.entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 标签web层
 */
@RestController
@RequestMapping("/label")
@CrossOrigin    //解决跨域请求问题
public class LabelController {

    @Autowired
    private LabelService labelService;

    /**
     * get请求查询所有标签列表
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        List<Label> labelList = labelService.findAll();
        return new Result(true, StatusCode.OK, "查询成功", labelList);
    }

    /**
     * get请求的根据id查询
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        Label label = labelService.findById(id);
        return new Result(true, StatusCode.OK, "查询成功", label);
    }

    /**
     * POST方式的增加
     *
     * @param label
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Label label) {
        labelService.addLabel(label);
        return new Result(true, StatusCode.OK, "添加成功", null);
    }

    /**
     * 根据ID修改的PUT提交方式
     *
     * @param label
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Label label, @PathVariable String id) {
        label.setId(id);
        labelService.updateLabel(label);
        return new Result(true, StatusCode.OK, "修改成功", null);
    }

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result deleteById(@PathVariable String id) {
        labelService.delLabelById(id);
        return new Result(true, StatusCode.OK, "删除成功", null);
    }

}
