package user.zchp.general.assemble;

import user.zchp.general.component.ClassModel;
import user.zchp.general.component.Column;
import user.zchp.general.component.TemplateInfo;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类组装器
 *
 * @author zhouchuang
 * @create 2018-09-08 19:46
 */
public class MapperAssemble extends AbstractAssemble {

    public final static String PackageName="mapper";

    public String getTemplateName(){
        return "MapperTemplate";
    }

    public String getExtName(){
        return "xml";
    }

    public TemplateInfo process(ClassModel classModel) {
        TemplateInfo templateInfo = getTemplateInfo();
        try{
            templateInfo.setPath(classModel.getPath()+File.separator+classModel.getPackageName().replace(".",File.separator)+File.separator+PackageName);
            templateInfo.setClassName(classModel.getClassName()+"Dao");
            StringBuffer  stringBuffer = read(templatePath);
            String classString = stringBuffer.toString();

            Field[] fields = classModel.getClass().getDeclaredFields();
            for(Field field : fields ){
                if(field.getType().getName().equals("java.lang.String"))
                    try{
                        classString = classString.replace("${"+field.getName()+"}",(String)classModel.getClass().getMethod("get"+toUpperFirstLetterCase(field.getName()),null).invoke(classModel,null));
                    }catch (Exception e){

                    }
            }

            Pattern pattern = Pattern.compile("@([a-zA-Z]+)\\{(.+)\\}@",Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(classString);
            while(matcher.find()){
                StringBuffer params  = new StringBuffer();
                List<String> matList = new ArrayList<String>();
                Pattern paramreg = Pattern.compile("@\\{([^\\}]+)\\}");
                Matcher parammat = paramreg.matcher(matcher.group(2));
                while(parammat.find()){
                    for(int i=0;i<classModel.getList().size();i++){
                        Column cl  = classModel.getList().get(i);
                        if(matList.size()<classModel.getList().size()){
                            matList.add(matcher.group(2).replace(parammat.group(0), (String)cl.getClass().getMethod("get"+toUpperFirstLetterCase(parammat.group(1)),null).invoke(cl,null)));
                        }else{
                            matList.set(i, matList.get(i).replace(parammat.group(0), (String)cl.getClass().getMethod("get"+toUpperFirstLetterCase(parammat.group(1)),null).invoke(cl,null)));
                        }
                    }
                }
                boolean flag = matcher.group(2).contains("<");
                for(String str : matList){
                    params.append(str+(flag?"\r":""));
                }
                String paramsresult = params.toString();
                if(!flag&&!"".equals(paramsresult))
                    paramsresult = paramsresult.substring(0, paramsresult.length()-1);
                classString   =  classString.replace(matcher.group(0),paramsresult);
                matcher = pattern.matcher(classString);
            }
            templateInfo.setTemplate(classString);
        }catch (Exception e){
            e.printStackTrace();
        }
        return templateInfo;
    }


}
