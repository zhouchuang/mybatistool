package user.zchp.general;

import user.zchp.general.assemble.*;
import user.zchp.general.component.ClassModel;
import user.zchp.general.component.TemplateInfo;
import user.zchp.general.pipeline.Pipeline;
import user.zchp.general.process.TableProcess;
import user.zchp.general.resource.MysqlDataResource;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成机器
 *
 * @author zhouchuang
 * @create 2018-07-17 23:01
 */
public class Machine{
    private List<Pipeline> pipelineList = new ArrayList<Pipeline>();
    private List<Assemble> assembleList = new ArrayList<>();
    private List<TemplateInfo> templateInfos = new ArrayList<TemplateInfo>();
    private TableProcess tableProcess;
    public Machine(TableProcess tableProcess){
        this.tableProcess = tableProcess;
    }

    public static Machine create(TableProcess tableProcess){
        return new Machine(tableProcess);
    }

    public  Machine addPiplineList(Pipeline pipeline){
        this.pipelineList.add(pipeline);
        return this;
    }

    public void run() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                processTable();
            }
        });
        thread.start();
    }

    public void processTable(){
        try{
            ClassModel classModel  = MysqlDataResource.getInstance().addBaseColumn(this.tableProcess).getClassModel(this.tableProcess);
            TemplateInfo modelClassTemplate = new ModelAssemble().process(classModel);
            TemplateInfo daoClassTemplate = new DaoAssemble().process(classModel);
            TemplateInfo mapperTemplate = new MapperAssemble().process(classModel);
            TemplateInfo serviceTemplate = new ServiceAssemble().process(classModel);
            templateInfos.add(modelClassTemplate);
            templateInfos.add(daoClassTemplate);
            templateInfos.add(mapperTemplate);
            templateInfos.add(serviceTemplate);
            for (Pipeline pipeline : pipelineList) {
                pipeline.process(templateInfos);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
