package user.zchp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import user.zchp.general.Machine;
import user.zchp.general.pipeline.Console;
import user.zchp.general.process.DemoTableProcess;
import user.zchp.general.component.LeftTable;
import user.zchp.service.base.TableService;
import user.zchp.utils.Result;

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

    @RequestMapping(value = "/generalDao", method = RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Result generalDao(@RequestBody LeftTable leftTable){
        Result result = new Result();
        Machine.create(new DemoTableProcess())
        .addPiplineList(new Console())
        .run();
        return result;
    }

}