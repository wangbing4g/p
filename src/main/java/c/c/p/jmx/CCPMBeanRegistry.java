/**
 * 
 */
package c.c.p.jmx;

import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author WangBing
 *
 */
public final class CCPMBeanRegistry {

    private static Logger LOG = LoggerFactory.getLogger(CCPMBeanRegistry.class);

    private MBeanServer beanServer;

    private static CCPMBeanRegistry instance = new CCPMBeanRegistry();

    private Map<CCPMBeanInfo, String> mapBean2Name = new HashMap<CCPMBeanInfo, String>();

    private final Object LOCK = new Object();

    public static CCPMBeanRegistry getInstance() {
        return instance;
    }

    /**
     * 
     */
    private CCPMBeanRegistry() {
        beanServer = ManagementFactory.getPlatformMBeanServer();
        if (beanServer == null) {
            beanServer = MBeanServerFactory.createMBeanServer();
        }
    }

    public MBeanServer getPlatformMbeanServer() {
        return beanServer;
    }

    public Set<CCPMBeanInfo> getRegisteredBeans() {
        return new HashSet<CCPMBeanInfo>(mapBean2Name.keySet());
    }

    public void register(CCPMBeanInfo beanInfo) throws JMException {

        assert beanInfo != null;

        ObjectName oname = getObjectName(beanInfo);
        
        LOG.info("Register MBean [{}]",oname);
        
        try {
            synchronized (LOCK) {
                beanServer.registerMBean(beanInfo, oname);
                mapBean2Name.put(beanInfo, beanInfo.getName());
            }
        } catch (JMException e) {
            LOG.warn("Failed to register MBean [{}]", beanInfo.getName());
            throw e;
        }

    }
    
    public void unregister(CCPMBeanInfo beanInfo) throws JMException {
        
        assert beanInfo != null;
        
        ObjectName oname = getObjectName(beanInfo);
        LOG.info("unregister MBean [{}]",oname);
        synchronized (LOCK) {
            beanServer.unregisterMBean(oname);
            mapBean2Name.remove(beanInfo);
        }
    }

    /**
     * @param beanInfo
     * @return
     * @throws MalformedObjectNameException
     */
    private ObjectName getObjectName(CCPMBeanInfo beanInfo) throws MalformedObjectNameException {
        ObjectName oname = new ObjectName("c.c.p.domain", "name", beanInfo.getName());
        return oname;
    }

}
