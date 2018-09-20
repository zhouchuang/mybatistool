package user.zchp.general.pipeline;

import user.zchp.general.component.TemplateInfo;

import java.util.List;

/**
 * 输出通道，可以有多个
 *
 * @author zhouchuang
 * @create 2018-08-22 21:48
 */
public interface Pipeline {
    void process(List<TemplateInfo> templateInfo);
}
