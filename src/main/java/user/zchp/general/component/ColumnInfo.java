package user.zchp.general.component;

import lombok.Data;

import java.util.Map;

/**
 * 列的相关信息
 *
 * @author:Administrator
 * @create 2018-09-09 14:51
 */
@Data
public class ColumnInfo {
    Map<String,String> comments;
    ClassModel classModel;

    public ColumnInfo(Map<String,String> comments,ClassModel classModel){
        this.comments = comments;
        this.classModel = classModel;
    }
}
