package user.zchp.general.resource;

import java.io.*;

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
        InputStreamReader isr = new InputStreamReader(new FileInputStream(f), "UTF-8");
        BufferedReader br = new BufferedReader(isr);
        String s;
        while ((s = br.readLine()) != null) {
            sb.append(s + "\n\r");
        }
        br.close();
        isr.close();
        return sb;
    }

    @Override
    public void write(String path, StringBuffer stringBuffer) throws Exception {

    }
}
