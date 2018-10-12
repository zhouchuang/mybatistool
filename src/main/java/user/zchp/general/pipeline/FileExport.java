package user.zchp.general.pipeline;

import user.zchp.general.component.TemplateInfo;
import user.zchp.service.DictCacheService;

import java.io.*;
import java.util.List;

/**
 * 文件输出
 *
 * @author zhouchuang
 * @create 2018-07-17 23:03
 */
public class FileExport implements Pipeline{
    @Override
    public void process(List<TemplateInfo> templateInfoList) {
        for(TemplateInfo templateInfo : templateInfoList){
            try{
                File fgroup  = new File(templateInfo.getPath());
                if(!fgroup.exists()){
                    fgroup.mkdirs();
                }
                File fout = new File(templateInfo.getPath()+File.separator+templateInfo.getClassName()+"."+templateInfo.getExtName());
                if(fout.exists()){
                    fout.delete();
                }
//                BufferedWriter writer = new BufferedWriter(new FileWriter(fout, true));
                FileOutputStream writerStream = new FileOutputStream(fout);
                OutputStreamWriter osw = new OutputStreamWriter(writerStream,"UTF-8");
                BufferedWriter writer = new BufferedWriter(osw);
                writer.write(templateInfo.getTemplate());
                writer.close();
                osw.close();
                writerStream.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
