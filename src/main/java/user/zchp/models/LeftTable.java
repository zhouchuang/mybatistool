package user.zchp.models;

import lombok.Data;

import java.io.Serializable;

/**
 * 关联表
 *
 * @author zhouchuang
 * @create 2018-07-14 10:41
 */
@Data
public class LeftTable implements Serializable{
    private String refId;
    private String revId;
    private Table table;
}
