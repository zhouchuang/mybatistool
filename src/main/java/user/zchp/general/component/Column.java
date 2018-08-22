package user.zchp.general.component;

/**
 * 列
 *
 * @author zhouchuang
 * @create 2018-08-22 22:17
 */
public class Column {
    private String columnName;
    private String name;
    private String funName;
    private String columnType;
    private String jdbcColumnType;
    private String columnClassName;
    private String type;
    private String remarks;
    private Object defaultValue;
    private Integer columnSize;
    private Integer decimalNum;
    private DataType dataType;
    public final static Integer String = 1;
    public final static Integer Integer = 2;
    public final static Integer Boolean  = 3;

    public Column(DataType dataType,String name){
        this.dataType = dataType;
        this.name = name;
        this.columnName = name;
    }
}
