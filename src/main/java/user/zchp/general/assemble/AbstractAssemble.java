package user.zchp.general.assemble;

import lombok.Data;
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

    protected String templatePath;
    public StringBuffer read(String path)throws Exception{
        StringBuffer stringBuffer = TemplateResource.getInstance().read(path);
        return stringBuffer;
    }
}
