package user.zchp.general.process;

import lombok.Data;
import user.zchp.general.Machine;
import user.zchp.general.assemble.AbstractAssemble;
import user.zchp.general.assemble.ClassAssemble;
import user.zchp.general.component.*;
import user.zchp.general.pipeline.Console;

import java.io.File;

/**
 * 处理模板
 *
 * @author zhouchuang
 * @create 2018-08-22 21:37
 */
@Data
public class DemoTableProcess implements TableProcess {

    private LeftTable leftTable;
    public DemoTableProcess(LeftTable leftTable){
        this.leftTable = leftTable;
    }
    private TableConfig tableConfig = TableConfig.me()
            .addDefaultColumn(new Column(DataType.String,"createBy"))
            .addDefaultColumn(new Column(DataType.String,"updateBy"))
            .addDefaultColumn(new Column(DataType.Date,"createTime"))
            .addDefaultColumn(new Column(DataType.Date,"updateTime"))
            .addDefaultColumn(new Column(DataType.Boolean,"isDeleted"))
            .setPk(new Column(DataType.String,"id"))
            .setVersion(new Column(DataType.Integer,"version"))
            .setDefaultPath("user.zc")
            .setDatabase("tool");

    @Override
    public void process() {

    }

    @Override
    public Machine getMachine() {
        return null;
    }

    @Override
    public TableConfig getConfig() {
        return tableConfig;
    }
    @Override
    public LeftTable getLeftTable(){
        return leftTable;
    }


    public static void main(String[] args) {
        LeftTable leftTable  = new LeftTable() ;
        Table table = new Table();
        table.setTableName("test");
        leftTable.setTable(table);
        Machine.create(new DemoTableProcess(leftTable))
                .addPiplineList(new Console())
                .run();
    }
}
