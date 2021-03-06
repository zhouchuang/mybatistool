package user.zchp.general.component;

import lombok.Data;

import java.io.File;

/**
 * 模板信息
 *
 * @author:Administrator
 * @create 2018-09-20 17:03
 */
@Data
public class TemplateInfo {
    private String template;
    private String path;
    private String className;
    private String extName;
    private String proPath;
    private String basePath;

    public String  getRealPath(){
        return path+ File.separator+className+"."+extName;
    }
}
