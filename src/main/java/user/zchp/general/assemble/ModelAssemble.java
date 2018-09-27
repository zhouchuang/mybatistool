package user.zchp.general.assemble;

import user.zchp.general.component.ClassModel;
import user.zchp.general.component.Column;
import user.zchp.general.component.TableConfig;
import user.zchp.general.component.TemplateInfo;

import java.io.File;

/**
 * 类组装器
 *
 * @author zhouchuang
 * @create 2018-09-08 19:46
 */
public class ModelAssemble extends AbstractAssemble {

    public ModelAssemble(TableConfig tableConfig) {
        super(tableConfig);
    }

    public String getTemplateName(){
        return "ModelTemplate";
    }

    public String getPackageName(){return "models";}

    public TemplateInfo process(ClassModel classModel) {
        try{
            templateInfo.setClassName(classModel.getClassName());
            StringBuffer  stringBuffer = read(templatePath);
            StringBuffer function= new StringBuffer();
            for(Column cl : classModel.getList()){
                if(!cl.getIsBase()){ //不是基本列，才可以拼接
                    String str = (cl.getRemarks()!=null?("    //"+cl.getRemarks()+newLine):"") +space+"private "+ cl.getType() +" "+cl.getName()+(cl.getDefaultValue()!=null?(" = "+cl.getDefaultValueToString()):"")+";"+newLine;
                    function.append( str);
                }

            }
            String classString = stringBuffer.toString();
            classString  =  classString.replace("${packageName}", classModel.getPackageName());
            classString  =  classString.replace("${tableName}", classModel.getTableName());
            classString  =  classString.replace("${className}", classModel.getClassName());
            classString  =  classString.replace("${function}", function.toString());
            templateInfo.setTemplate(classString);
        }catch (Exception e){
            e.printStackTrace();
        }
        return templateInfo;
    }
}
