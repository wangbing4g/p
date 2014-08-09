/**
 * 
 */
package c.c.p.utils;

import java.lang.reflect.Method;

import c.c.p.CCPRuntimeException;

/**
 * @author zszw.now@qq.com
 *
 */
public class ObjectUtils extends org.springframework.util.ObjectUtils {

    public static Object readProperty(Object obj, String property) throws CCPRuntimeException {

        try {
            Method readMethod = ClassUtils.getAccessiableReadMethod(obj.getClass(), property);
            return readMethod.invoke(obj);
        } catch (Exception e) {
           throw new CCPRuntimeException(e);
        }

    }

}
