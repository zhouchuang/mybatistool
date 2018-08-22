package user.zchp.general;

import user.zchp.general.pipeline.Pipeline;
import user.zchp.general.process.TableProcess;

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
    }

    public void processTable(){
        
    }
}
