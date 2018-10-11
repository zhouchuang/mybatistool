package user.zchp.general.utils;

import lombok.Data;
import user.zchp.general.component.Table;
import user.zchp.general.component.TemplateInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息类，返回处理的信息
 *
 * @author:Administrator
 * @create 2018-09-28 15:44
 */
@Data
public class GeneralMessage {
    List<Table> tableList = new ArrayList<>();
    public void addTableMessage(Table table){
        for(TemplateInfo templateInfo  : table.getTemplateInfos()){
            addPath(templateInfo);
        }
        tableList.add(table);
    }
    List<TemplateInfo> paths ;
    private void addPath(TemplateInfo path){
        if(paths ==null ){
            paths = new ArrayList<>();
        }
        paths.add(path);
    }
}
