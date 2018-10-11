package user.zchp.general.utils;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import user.zchp.general.component.Table;
import user.zchp.general.component.TemplateInfo;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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


    public static ResponseEntity<byte[]> download(List<TemplateInfo> paths)throws Exception{
        File file = new File("C:/javatemp");
        if(file.exists()){
            delFolder(file.getPath());
        }
        file.mkdir();
        for(TemplateInfo templateInfo : paths){
            String dest = templateInfo.getRealPath().replace(templateInfo.getBasePath(),file.getPath());
            FileUtils.copyFile(new File(templateInfo.getRealPath()), new File(dest));
        }
        compressHandler(file,"javatemp/");
        return download(file.getPath()+".zip","java生成包");
    }


    private static ZipOutputStream compressHandler(File file ,String baseDir){
        //压缩文件
        File zipFile = new File(file.getPath()+".zip");
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(new FileOutputStream(zipFile));
            compress(file , baseDir, zos);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(zos!=null)
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        }
        return zos;
    }

    //删除文件夹
    public static void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            java.io.File myFilePath = new java.io.File(filePath);
            myFilePath.delete(); //删除空文件夹
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除指定文件夹下所有文件
    public static boolean delAllFile(String path) {
        boolean flag = false;
        File file = new File(path);
        if (!file.exists()) {
            return flag;
        }
        if (!file.isDirectory()) {
            return flag;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);//再删除空文件夹
                flag = true;
            }
        }
        return flag;
    }

    private static void compress(File f, String baseDir, ZipOutputStream zos){
        if(!f.exists()){
            System.out.println("待压缩的文件目录或文件"+f.getName()+"不存在");
            return;
        }
        File[] fs = f.listFiles();
        BufferedInputStream bis = null;
        byte[] bufs = new byte[1024*10];
        FileInputStream fis = null;
        try{
            for(int i=0; i<fs.length; i++){
                String fName =  fs[i].getName();
                System.out.println("压缩：" + baseDir+fName);
                if(fs[i].isFile()){
                    ZipEntry zipEntry = new ZipEntry(baseDir+fName);//
                    zos.putNextEntry(zipEntry);
                    //读取待压缩的文件并写进压缩包里
                    fis = new FileInputStream(fs[i]);
                    bis = new BufferedInputStream(fis, 1024*10);
                    int read = 0;
                    while((read=bis.read(bufs, 0, 1024*10))!=-1){
                        zos.write(bufs, 0, read);
                    }
                }
                else if(fs[i].isDirectory()){
                    compress(fs[i], baseDir+fName+"/", zos);
                }
            }
        }catch  (IOException e) {
            e.printStackTrace();
        } finally{
            //关闭流
            try {
                if(null!=bis)
                    bis.close();
                if(null!=fis)
                    fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

}
