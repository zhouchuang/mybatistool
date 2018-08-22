package user.zchp.general.process;

import user.zchp.general.Machine;
import user.zchp.general.component.LeftTable;

/**
 * 表处理接口
 *
 * @author zhouchuang
 * @create 2018-08-22 21:34
 */
public interface TableProcess {

    public void process(LeftTable table);

    public Machine getMachine();
}
