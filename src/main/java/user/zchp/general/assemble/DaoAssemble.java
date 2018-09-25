package user.zchp.general.assemble;

import user.zchp.general.component.ClassModel;
import user.zchp.general.component.Column;
import user.zchp.general.component.TemplateInfo;

import java.io.File;
import java.lang.reflect.Field;

/**
 * dao层生成器
 *
 * @author:Administrator
 * @create 2018-09-25 14:28
 */
public class DaoAssemble  extends AbstractAssemble {
    public final static String PackageName="dao";

    public String getTemplateName(){
        return "DaoTemplate";
    }

    @Override
    public TemplateInfo process(ClassModel classModel) {
        TemplateInfo templateInfo = getTemplateInfo();
        try{
            templateInfo.setPath(classModel.getPath()+ File.separator+classModel.getPackageName().replace(".",File.separator)+File.separator+PackageName);
            templateInfo.setClassName(classModel.getClassName()+"Dao");
            StringBuffer  stringBuffer = read(templatePath);
            String classString = stringBuffer.toString();
            Field[] fields = classModel.getClass().getDeclaredFields();
            for(Field field : fields ){
                if(field.getType().getName().equals("java.lang.String"))
                    try{
                        classString = classString.replace("${"+field.getName()+"}",(String)classModel.getClass().getMethod("get"+toUpperFirstLetterCase(field.getName()),null).invoke(classModel,null));
                    }catch (NullPointerException e){

                    }
            }
            templateInfo.setTemplate(classString);
        }catch (Exception e){
            e.printStackTrace();
        }
        return templateInfo;
    }
}
