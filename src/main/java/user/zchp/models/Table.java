package user.zchp.models;

import lombok.Data;

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

    private String tableName;
    private List<Field> fields;
    private List<LeftTable> leftTables;
}
