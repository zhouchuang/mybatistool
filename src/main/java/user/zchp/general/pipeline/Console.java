package user.zchp.general.pipeline;

/**
 * 控制台
 *
 * @author zhouchuang
 * @create 2018-07-17 23:02
 */
public class Console implements Pipeline{
    @Override
    public void process(StringBuffer stringBuffer) {
        System.out.println("=========sql start==========");
        System.out.println(stringBuffer.toString());
        System.out.println("=========sql ends==========");
    }
}
