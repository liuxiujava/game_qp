package x.utils;

import jsa.ioc.context.ApplicationContext;
import jsa.ioc.context.ClassPathApplicationContext;

/**
 * Desc : Spring自注入类
 *
 * @author achou.lau
 * @version 1.0
 * @create 2012-10-15 下午1:44:33
 */
public class SpringUtil {
    private static ApplicationContext ac = ClassPathApplicationContext.getApplicationContext();

    public static <T> T getBean(Class<T> object) {
        return ac.getBean(object);
    }
}
