package user.zchp.general.component;

import java.math.BigDecimal;

/**
 * 数据类型
 *
 * @author zhouchuang
 * @create 2018-08-22 22:31
 */
public enum DataType {
    Integer("Integer","int",11),
    String("String","varchar",32),
    Boolean("Boolean","tinyint",1),
    Date("Date","datetime",0),
    BigDecimal("BigDecimal","decimal",11,2);

    //防止字段值被修改，增加的字段也统一final表示常量
    public final String javatype;
    public final String dbtype;
    public final Integer len;
    public final Integer accuracy;


    private DataType(String javatype,String dbtype,Integer len){
        this.javatype = javatype;
        this.dbtype = dbtype;
        this.len = len;
        this.accuracy = 0;
    }

    private DataType(String javatype,String dbtype,Integer len,Integer accuracy){
        this.javatype = javatype;
        this.dbtype = dbtype;
        this.len = len;
        this.accuracy = accuracy;
    }
}
