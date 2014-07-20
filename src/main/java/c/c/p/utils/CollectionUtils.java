/**
 * 
 */
package c.c.p.utils;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;

import c.c.p.CCPRuntimeException;

/**
 * @author zszw.now@qq.com
 *
 */
public class CollectionUtils {

    /**
     * Join as String with separator
     * 
     * @param objs
     * @param separator
     * @return
     */
    public static String join(Object[] objs, String separator) {

        if (ObjectUtils.isEmpty(objs)) {
            return "";
        }

        StringBuilder sb = new StringBuilder(objs[0].toString());

        for (int i = 1; i < objs.length; i++) {
            sb.append(separator).append(objs[i]);
        }

        return sb.toString();

    }
    
    public static boolean isEmpty(Collection<?> objs) {
        if(objs == null || objs.size() <= 0) {
            return true;
        }
        return false;
    }

    /**
     * 
     * @param objs
     * @param property
     * @param separator
     * @return
     * @throws IntrospectionException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    public static String join(Object[] objs, String property, String separator) throws IntrospectionException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {

        if (ObjectUtils.isEmpty(objs)) {
            return "";
        }

        StringBuilder sb = null;
        try {
            Method readMethod = ClassUtils.getAccessiableReadMethod(objs[0].getClass(), property);
            sb = new StringBuilder(String.valueOf(readMethod.invoke(objs[0])));

            for (int i = 1; i < objs.length; i++) {
                sb.append(separator).append(readMethod.invoke(objs[i]));
            }
        } catch (Exception e) {
            throw new CCPRuntimeException(e);
        }

        return sb.toString();

    }
    
    public static String join(Collection<?> objs, String property, String separator)  {
            
        if (isEmpty(objs)) {
            return "";
        }
        
        Method readMethod = null;
        StringBuilder sb = null;
        
        boolean isFirst = true;
        
        try {
            for(Object obj:objs) {
                if(isFirst) {
                    readMethod = ClassUtils.getAccessiableReadMethod(obj.getClass(), property);
                    sb = new StringBuilder(String.valueOf(readMethod.invoke(obj)));
                    isFirst = false;
                } else {
                    sb.append(",").append(readMethod.invoke(obj));
                }
            }
        } catch  (Exception e) {
            throw new CCPRuntimeException(e);
        }
        
        return sb.toString();
    
    }

    public static void main(String[] args) {

        System.out.println(join(new String[] { "x" }, ","));
        System.out.println(join(new String[] { "x", "y" }, ","));

    }

}
