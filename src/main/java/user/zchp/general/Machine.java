package user.zchp.general;

import org.apache.shiro.util.CollectionUtils;
import user.zchp.general.assemble.*;
import user.zchp.general.component.ClassModel;
import user.zchp.general.component.LeftTable;
import user.zchp.general.component.Table;
import user.zchp.general.component.TemplateInfo;
import user.zchp.general.pipeline.Pipeline;
import user.zchp.general.process.TableProcess;
import user.zchp.general.resource.MysqlDataResource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 代码生成机器
 *
 * @author zhouchuang
 * @create 2018-07-17 23:01
 */
public class Machine{
    private List<Pipeline> pipelineList = new ArrayList<Pipeline>();
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
                Table table = tableProcess.getLeftTable().getTable();
                processTable(table);
            }
        });
        thread.start();
    }

    public void batchProcessTable(Table table){

        //处理完后，看看有没有下级内容
        if(!CollectionUtils.isEmpty(table.getLeftTables())){
            for(LeftTable leftTable : table.getLeftTables()){
                Table table1 = leftTable.getTable();
                if(table1!=null){
                    processTable(table1);
                }
            }
        }
    }

    public void processTable(Table table){
        try{
            this.tableProcess.setCurrentTable(table);
            ClassModel classModel  = MysqlDataResource.getInstance().addBaseColumn(this.tableProcess).getClassModel(this.tableProcess);
            TemplateInfo modelClassTemplate = new ModelAssemble(this.tableProcess.getConfig()).process(classModel);
            TemplateInfo daoClassTemplate = new DaoAssemble(this.tableProcess.getConfig()).process(classModel);
            TemplateInfo serviceTemplate = new ServiceAssemble(this.tableProcess.getConfig()).process(classModel);
            TemplateInfo mapperTemplate = new MapperAssemble(this.tableProcess.getConfig()).process(classModel);
            templateInfos.add(modelClassTemplate);
            templateInfos.add(daoClassTemplate);
            templateInfos.add(mapperTemplate);
            templateInfos.add(serviceTemplate);
            for (Pipeline pipeline : pipelineList) {
                pipeline.process(templateInfos);
            }
            batchProcessTable(table);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
