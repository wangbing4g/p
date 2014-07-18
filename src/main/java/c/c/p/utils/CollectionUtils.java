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

        Method readMethod = ClassUtils.getAccessiableReadMethod(objs[0].getClass(), property);
        StringBuilder sb = new StringBuilder(String.valueOf(readMethod.invoke(objs[0])));

        for (int i = 1; i < objs.length; i++) {
            sb.append(separator).append(readMethod.invoke(objs[i]));
        }

        return sb.toString();

    }

    public static void main(String[] args) {

        System.out.println(join(new String[] { "x" }, ","));
        System.out.println(join(new String[] { "x", "y" }, ","));

    }

}
