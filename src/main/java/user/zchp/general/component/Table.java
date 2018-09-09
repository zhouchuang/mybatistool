package user.zchp.general.component;

import lombok.Data;
import user.zchp.utils.StringUtils;

import java.io.Serializable;
import java.util.List;

/**
 * 生成表辅助类
 *
 * @author zhouchuang
 * @create 2018-07-14 10:21
 */
@Data
public class Table  implements Serializable {

    private String name;
    private String tableName;
    private List<Field> fields;
    private List<LeftTable> leftTables;
    private Boolean status;

    public String getName(){
        if(StringUtils.isEmpty(this.name)){
            this.name  = StringUtils.getFirstCharToUpper(this.tableName);
        }
        return this.name;
    }
}
