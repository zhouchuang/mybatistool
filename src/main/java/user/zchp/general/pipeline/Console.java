package user.zchp.general.pipeline;

import user.zchp.general.component.TemplateInfo;

import java.util.List;

/**
 * 控制台
 *
 * @author zhouchuang
 * @create 2018-07-17 23:02
 */
public class Console implements Pipeline{
    @Override
    public void process(List<TemplateInfo> templateInfoList) {
        System.out.println("=========Console start==========");
        for(TemplateInfo templateInfo : templateInfoList){
            System.out.println("Path:"+templateInfo.getPath());
            System.out.println("Name:"+templateInfo.getClassName());
            System.out.println("Text:"+templateInfo.getTemplate());
        }
        System.out.println("=========Console end  ==========");
    }
}
