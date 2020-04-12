package lucky.xjz.rpc.core.util;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflexUtil {


    /**]
     * 根据路径获取对象
     * @param classPath
     * @return
     */
    public static Class getClassObj(String classPath) throws ClassNotFoundException {

            return Class.forName(classPath);

    }

    /**
     * 获取方法对象
     * @param classObj  class对象
     * @param methodName 方法名
     * @param paramsType 参数类型
     * @return
     * @throws NoSuchMethodException
     */
    public static Method getMethodObj(Class classObj,String methodName,Class[] paramsType) throws NoSuchMethodException {

          return classObj.getMethod(methodName,paramsType);

    }

    /**
     *
     * @param method 方法对象
     * @param classObj 类对象
     * @param params 参数列表
     * @return 函数调用结果
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static Object getInvokeResult(Method method,Object classObj,Object[]params) throws InvocationTargetException, IllegalAccessException {

           return method.invoke(classObj,params);
    }


}
