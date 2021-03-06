package user.zchp.general.assemble;

import lombok.Data;
import user.zchp.general.component.ClassModel;
import user.zchp.general.component.TableConfig;
import user.zchp.general.component.TemplateInfo;
import user.zchp.general.resource.FileResource;
import user.zchp.general.resource.TemplateResource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * 抽象组装类
 *
 * @author:Administrator
 * @create 2018-09-09 9:15
 */
@Data
public abstract class AbstractAssemble implements Assemble {

    protected TemplateInfo templateInfo;
    protected String extName = "java";
    protected String templatePath;
    protected String path;
    protected String className;
    protected String templateName;
    protected String proPath = "src\\main\\java\\";
    protected String packageName="dao";
    public StringBuffer read(String path)throws Exception{
        StringBuffer stringBuffer = TemplateResource.getInstance().read(path);
        return stringBuffer;
    }

    protected static  String toUpperFirstLetterCase(String str){
        return  str.substring(0,1).toUpperCase()+str.substring(1);
    }

    protected String getTemplateName(){
        return this.templateName;
    }
    protected String getExtName(){
        return this.extName;
    }
    protected String getProPath(){return this.proPath;}
    protected String getPackageName(){return this.packageName;}

    public AbstractAssemble(TableConfig tableConfig){
        this.templatePath = ModelAssemble.class.getResource("/").getPath()+"template"+ File.separator+getTemplateName();
        templateInfo = new TemplateInfo();
        templateInfo.setExtName(getExtName());
        templateInfo.setBasePath(tableConfig.getBasePath());
        templateInfo.setPath(tableConfig.getBasePath()+ File.separator+getProPath()+tableConfig.getBasePackage().replace(".",File.separator)+File.separator+getPackageName());
    }
}
