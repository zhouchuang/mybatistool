package user.zchp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import user.zchp.general.component.LeftTable;
import user.zchp.general.utils.SpringResouceUtil;
import user.zchp.models.TableInfo;
import user.zchp.service.TableInfoService;
import user.zchp.service.BusinessTableService;
import user.zchp.utils.QueryParam;
import user.zchp.utils.Result;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
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
    BusinessTableService businessTableService;
    @Autowired
    TableInfoService tableInfoService;

    @RequestMapping(value="/index")
    public String index(Model model , HttpServletRequest request) {
        return "/table/index";
    }

    @RequestMapping(value = "/TableList")
    @ResponseBody
    public Result tableList(){
        Result result = new Result();
        List<String> list = businessTableService.tableList(SpringResouceUtil.getInstance().getDatabase());
        List<Map<String,Boolean>> newlist = new ArrayList<Map<String,Boolean>>();
        try {
            List<TableInfo> tableInfos  = tableInfoService.findList(new QueryParam());
            for(String name : list){
                Map<String,Boolean> map = new HashMap<>();
                map.put(name,tableInfoService.exist(tableInfos,name));
                newlist.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData(newlist);
        return result;
    }

    @RequestMapping(value = "/FieldList")
    @ResponseBody
    public Result fieldList(String table ){
        Result result = new Result();
        List<Map> list = businessTableService.fieldList(table);
        Map map = new HashMap();
        map.put("list",list);
        try {
            TableInfo tableInfo = tableInfoService.one(new QueryParam("name",table));
            map.put("status",tableInfo!=null&&tableInfo.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.setData(map);
        return result;
    }

    @RequestMapping(value = "/generalDao", method = RequestMethod.POST, produces="application/json")
    @ResponseBody
    public Result generalDao(@RequestBody LeftTable leftTable){
        Result result = new Result();
        tableInfoService.generalHandler(leftTable);
        return result;
    }

}