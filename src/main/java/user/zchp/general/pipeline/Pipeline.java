package user.zchp.general.pipeline;

/**
 * 输出通道，可以有多个
 *
 * @author zhouchuang
 * @create 2018-08-22 21:48
 */
public interface Pipeline {
    public void process(StringBuffer stringBuffer);
}
