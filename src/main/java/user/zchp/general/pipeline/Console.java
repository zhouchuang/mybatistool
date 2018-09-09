package user.zchp.general.pipeline;

/**
 * 控制台
 *
 * @author zhouchuang
 * @create 2018-07-17 23:02
 */
public class Console implements Pipeline{
    @Override
    public void process(String text) {
        System.out.println("=========Console start==========");
        System.out.println(text);
        System.out.println("=========Console end  ==========");
    }
}
