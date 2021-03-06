package user.zchp.aop;

import java.lang.annotation.*;

/**
 * 搜索记录接口
 *
 * @author:Administrator
 * @create 2018-01-30 18:39
 */
@Retention(RetentionPolicy.RUNTIME)//注解会在class中存在，运行时可通过反射获取
@Target(ElementType.FIELD)//目标是方法
@Documented//文档生成时，该注解将被包含在javadoc中，可去掉
public @interface MapIdentification {
    public String column() default  "";
    public String revColumn() default "";
}
