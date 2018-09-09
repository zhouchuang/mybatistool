package user.zchp.general.resource;

/**
 * 文件读取资源
 *
 * @author:Administrator
 * @create 2018-09-09 9:12
 */
public interface FileResource {
    StringBuffer read(String path)throws Exception;
    void write(String path,StringBuffer stringBuffer)throws Exception;
}
