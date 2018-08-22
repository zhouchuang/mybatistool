package user.zchp.general.component;

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
    Date("Date","datetime",0);

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String javatype;
    private final String dbtype;
    private final Integer len;

    private DataType(String javatype,String dbtype,Integer len){
        this.javatype = javatype;
        this.dbtype = dbtype;
        this.len = len;
    }
}
