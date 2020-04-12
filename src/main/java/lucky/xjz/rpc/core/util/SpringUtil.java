package lucky.xjz.rpc.core.util;
import lucky.xjz.rpc.core.systemconfig.SpringConfig;
import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringUtil {

    private static SpringUtil springUtil = new SpringUtil();
    private AnnotationConfigApplicationContext context = null;

    public static SpringUtil getInstance() {

        return springUtil;
    }

    private SpringUtil() {
        context = new AnnotationConfigApplicationContext(SpringConfig.class);
    }

    /**
     * @param filePath spring @Service("") 中的名字 或者类的全路径
     * @return object
     * @throws Exception
     */
    public Object getBean(String filePath) throws Exception {
        Object obj = null;
        try {
            obj = context.getBean(filePath);

        } catch (BeansException e) {
            try {
                obj = context.getBean(ReflexUtil.getClassObj(filePath));
            } catch (BeansException e1) {
                throw new Exception(e1);
            }
        }
        return obj;

    }

    /**
     * 根据类类型获取构造对象
     * @param requiredType
     * @param <T>
     * @return
     * @throws BeansException 获取bean失败抛出的异常
     */
    public  <T> T getBean(Class<T> requiredType) throws BeansException{
        return context.getBean(requiredType);
    }


}
