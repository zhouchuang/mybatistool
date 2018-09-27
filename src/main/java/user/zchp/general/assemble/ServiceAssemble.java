package user.zchp.general.assemble;

import user.zchp.general.component.ClassModel;
import user.zchp.general.component.TableConfig;
import user.zchp.general.component.TemplateInfo;

import java.io.File;
import java.lang.reflect.Field;

/**
 * dao层生成器
 *
 * @author:Administrator
 * @create 2018-09-25 14:28
 */
public class ServiceAssemble extends AbstractAssemble {

    public ServiceAssemble(TableConfig tableConfig) {
        super(tableConfig);
    }

    public String getTemplateName(){
        return "ServiceTemplate";
    }

    public String getPackageName(){return "service";}

    @Override
    public TemplateInfo process(ClassModel classModel) {
        try{
            templateInfo.setClassName(classModel.getClassName()+"Service");
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
