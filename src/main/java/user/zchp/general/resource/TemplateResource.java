package user.zchp.general.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

/**
 * template文件读取类
 *
 * @author:Administrator
 * @create 2018-09-09 9:13
 */
public class TemplateResource implements FileResource {
    private static class TemplateResourceHolder {
        private static final TemplateResource INSTANCE = new TemplateResource();
    }

    private TemplateResource (){}

    public static final TemplateResource getInstance() {
        return TemplateResourceHolder.INSTANCE;
    }

    @Override
    public StringBuffer read(String path) throws Exception {
        StringBuffer sb = new StringBuffer();
        File f = new File(path);
        FileReader fr = new FileReader(f);
        BufferedReader br = new BufferedReader(fr);
        String s;
        while ((s = br.readLine()) != null) {
            sb.append(s + "\n\r");
        }
        br.close();
        fr.close();
        return sb;
    }

    @Override
    public void write(String path, StringBuffer stringBuffer) throws Exception {

    }
}
