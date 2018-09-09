package user.zchp.general.assemble;

import user.zchp.general.component.ClassModel;

/**
 * 组装接口
 *
 * @author:Administrator
 * @create 2018-09-08 18:30
 */
public interface Assemble {
    String space = "    ";
    String newLine = "\n\r";
    String  process(ClassModel classModel);
}
