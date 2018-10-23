package general;

import java.util.HashSet;
import java.util.Set;

/**
 * @author zhouchuang
 * @create 2018-10-23 22:01
 */
public class Test {
    @org.junit.Test
    public  void test(){
        Set<String> a = new HashSet<String>();
        a.add("123");
        a.add("abc");
        System.out.println(String.join(";/n/r",a.toArray(new String[]{})));
    }
}
