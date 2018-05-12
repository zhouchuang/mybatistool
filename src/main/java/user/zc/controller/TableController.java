package user.zc.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import user.zc.service.base.TableService;
import user.zc.utils.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * 表设计控制层
 *
 * @author zhouchuang
 * @create 2018-05-10 23:35
 */
@Controller
@RequestMapping("/TableController")
public class TableController {

    @Autowired
    TableService tableService;

    @RequestMapping(value="/index")
    public String index(Model model , HttpServletRequest request) {
        return "/table/index";
    }

    @RequestMapping(value = "/TableList")
    @ResponseBody
    public Result tableList(String database ){
        Result result = new Result();
        List<String> list = tableService.tableList(database);
        result.setData(list);
        return result;
    }

    @RequestMapping(value = "/FieldList")
    @ResponseBody
    public Result fieldList(String table ){
        Result result = new Result();
        List<Map> list = tableService.fieldList(table);
        result.setData(list);
        return result;
    }
}