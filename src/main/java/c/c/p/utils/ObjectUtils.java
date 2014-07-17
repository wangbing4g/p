/**
 * 
 */
package c.c.p.utils;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author zszw.now@qq.com
 *
 */
public class ObjectUtils extends org.springframework.util.ObjectUtils {

    public static Object readProperty(Object obj, String property) throws IntrospectionException, IllegalAccessException, IllegalArgumentException,
            InvocationTargetException {

        Method readMethod = ClassUtils.getAccessiableReadMethod(obj.getClass(), property);
        return readMethod.invoke(obj);

    }

}
