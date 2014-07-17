/**
 * 
 */
package c.c.p.utils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

/**
 * @author zszw.now@qq.com
 *
 */
public class ClassUtils extends org.springframework.util.ClassUtils {

    public static Method getAccessiableReadMethod(Class<?> clazz,String property) throws IntrospectionException {
        
        assert property != null;
        
        BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
        
        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
        
        for(int i=0;i<pds.length;i++) {
            if(pds[i].getName().equals(property)) {
                Method me = pds[i].getReadMethod();
                me.setAccessible(true);
                return me;
            }
        }
        
        return null;
        
    }
}
