package user.zchp.general.process;

import com.alibaba.druid.sql.visitor.functions.Left;
import user.zchp.general.Machine;
import user.zchp.general.component.ClassModel;
import user.zchp.general.component.LeftTable;
import user.zchp.general.component.Table;
import user.zchp.general.component.TableConfig;

/**
 * 表处理接口
 *
 * @author zhouchuang
 * @create 2018-08-22 21:34
 */
public interface TableProcess {

    void process();

    Machine getMachine();

    TableConfig getConfig();

    LeftTable getLeftTable();

    Table getCurentTable();

    void setCurrentTable(Table table);
}
