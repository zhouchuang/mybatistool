package user.zchp.controller;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import user.zchp.general.component.Column;
import user.zchp.general.component.LeftTable;
import user.zchp.general.resource.MysqlDataResource;
import user.zchp.general.utils.FileDownloadUtil;
import user.zchp.general.utils.GeneralMessage;
import user.zchp.general.utils.SpringResouceUtil;
import user.zchp.models.TableInfo;
import user.zchp.service.TableInfoService;
import user.zchp.service.BusinessTableService;
import user.zchp.utils.QueryParam;
import user.zchp.utils.Result;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
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
    TableInfoService tableInfoService;

    @RequestMapping(value="/index")
    public String index(Model model , HttpServletRequest request) {
        return "/table/index";
    }

    @RequestMapping(value = "/TableList")
    @ResponseBody
    public Result tableList(){
        Result result = new Result();
        List<String> list = MysqlDataResource.getInstance().tableList(SpringResouceUtil.getInstance().getDatabase());
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

    @RequestMapping(value = "/TableListDetail")
    @ResponseBody
    public Result TableListDetail(){
        Result result = new Result();
        List<String> list = MysqlDataResource.getInstance().tableList(SpringResouceUtil.getInstance().getDatabase());
        List<Map<String,Object>> newlist = new ArrayList<Map<String,Object>>();
        try {
            List<TableInfo> tableInfos  = tableInfoService.findList(new QueryParam());
            for(String name : list){
                Map<String,Object> map = new HashMap<>();
//                map.put(name,tableInfoService.exist(tableInfos,name));
                map.put("name",name);
                map.put("status",tableInfoService.exist(tableInfos,name));
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
        List<Column> list = MysqlDataResource.getInstance().fieldList(table);
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
        GeneralMessage generalMessage = tableInfoService.generalHandler(leftTable);
        result.setData(generalMessage);
        return result;
    }

    @RequestMapping("download")
    public ResponseEntity<byte[]> download(GeneralMessage generalMessage) throws IOException {
        try {
            return FileDownloadUtil.download(generalMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}