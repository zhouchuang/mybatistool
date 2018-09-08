package user.zchp.general.process;

import user.zchp.general.Machine;
import user.zchp.general.assemble.ClassAssemble;
import user.zchp.general.component.Column;
import user.zchp.general.component.DataType;
import user.zchp.general.component.LeftTable;
import user.zchp.general.component.TableConfig;

/**
 * 处理模板
 *
 * @author zhouchuang
 * @create 2018-08-22 21:37
 */
public class DemoTableProcess implements TableProcess {

    private TableConfig tableConfig = TableConfig.me().addDefaultColumn(new Column(DataType.String,"id"))
            .addDefaultColumn(new Column(DataType.Integer,"version"))
            .addDefaultColumn(new Column(DataType.String,"createBy"))
            .addDefaultColumn(new Column(DataType.String,"updateBy"))
            .addDefaultColumn(new Column(DataType.Date,"createTime"))
            .addDefaultColumn(new Column(DataType.Date,"updateTime"))
            .addDefaultColumn(new Column(DataType.Boolean,"isDeleted"))
            .setDefaultPath("user.zc");

    @Override
    public void process(LeftTable table) {
        try {
            new ClassAssemble().process();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TableConfig getTableConfig(){
        return this.tableConfig;
    }

    @Override
    public Machine getMachine() {
        return null;
    }
}
