package user.zchp.general.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import user.zchp.general.component.Table;

import java.io.File;

/**
 * 文件下载工具
 *
 * @author:Administrator
 * @create 2018-10-10 17:10
 */
public class FileDownloadUtil {
    public static  ResponseEntity<byte[]> download(String filePath,String fileName)throws Exception{
        String path=filePath;
        File file=new File(path);
        HttpHeaders headers = new HttpHeaders();
        fileName=new String(fileName.getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题
        headers.setContentDispositionFormData("attachment", fileName);
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),
                headers, HttpStatus.CREATED);
    }


    public static ResponseEntity<byte[]> download(GeneralMessage generalMessage)throws Exception{
        for(Table table : generalMessage.getTableList()){

        }
        return null;
    }
}
