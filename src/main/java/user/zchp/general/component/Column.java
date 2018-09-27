package user.zchp.general.component;

import lombok.Data;

/**
 * 列
 *
 * @author zhouchuang
 * @create 2018-08-22 22:17
 */
@Data
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
    private Boolean isBase;
    public Column(DataType dataType,String name){
        this.dataType = dataType;
        this.name = name;
        this.columnName = name;
    }

    public Column(){

    }

    public Object getDefaultValueToString(){
        if(this.defaultValue!=null){
            if(this.type.equals("Long")){
                return this.defaultValue +"L";
            }else if(this.type.equals("Double")){
                return this.defaultValue +"D";
            }else if(this.type.equals("Boolean")){
                return this.defaultValue.equals("0")?false:true;
            }else{
                return this.defaultValue.toString();
            }
        }else{
            return this.defaultValue.toString();
        }
    }

    public void setColumnType(String columnType) {
        try {
            this.type = getMapperdType(columnType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.columnType = columnType;
    }

    private String getMapperdType(String str)throws Exception{
        this.jdbcColumnType = str;
        if(str.equals("BIGINT")){
            return "Long";
        }else if(str.equals("VARCHAR")  || str.equals("TEXT") || str.equals("MEDIUMTEXT")){
            return "String";
        }else if(str.equals("VARCHAR2")){
            this.jdbcColumnType = "VARCHAR";

            return "String";
        }else if(str.equals("DECIMAL")){
            return "BigDecimal";
        }else if(str.equals("BIT")){
            return "Boolean";
        }else if(str.equals("DATETIME")){
            this.jdbcColumnType = "TIMESTAMP";
            return "Date";
        }else if(str.equals("DATE")){
            return "Date";
        }else if(str.equals("LONGTEXT")){
            return "String";
        }else if(str.startsWith("TIMESTAMP")){
            this.jdbcColumnType = "TIMESTAMP";
            return "Date";
        }else if(str.startsWith("NUMBER")){
            this.jdbcColumnType = "DECIMAL";
            if(this.columnSize>0){
                return "BigDecimal";
            }else{
                if(this.decimalNum>11)
                    return "Long";
                else
                    return "Integer";
            }
        }else if(str.equals("CHAR")){
            if(this.columnSize==2){
                return "Char";
            }else{
                return "String";
            }
        }else if(str.equals("CLOB")){
            return "String";
        }else if(str.equals("NVARCHAR2")){
            return "String";
        }else if(str.equals("INT")){
            return "Integer";
        }else if(str.equals("DATETIME")){
            return "Date";
        }else if(str.equals("TINYINT")){
            if(this.columnSize==1){
                return "Boolean";
            }else{
                return "Integer";
            }
        }
        throw new  Exception("没有找到对应类型："+str);
    }


    public void setColumnName(String columnName) {
        this.name = this.columnName = columnName;
        this.funName =  this.name.substring(0,1).toUpperCase()+this.name.substring(1);

    }
}
