/**
 * 
 */
package c.c.p.web.logger.manage;

import java.util.Enumeration;

import javax.management.JMException;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.jmx.HierarchyDynamicMBean;
import org.apache.log4j.spi.LoggerRepository;

import c.c.p.web.jmx.CCPMBeanRegistry;


/**
 * @author WangBing
 *
 */
public class ManagedLog4j {
    
    public static void registerLog4jMbeans() throws JMException {
        
        if (Boolean.getBoolean("jmx.log4j.disable") == true) {
            return;
        }
        
        MBeanServer beanServer = CCPMBeanRegistry.getInstance().getPlatformMbeanServer();
        
     // Create and Register the top level Log4J MBean
        HierarchyDynamicMBean hdm = new HierarchyDynamicMBean();

        ObjectName mbo = new ObjectName("log4j:hiearchy=default");
        beanServer.registerMBean(hdm, mbo);

        // Add the root logger to the Hierarchy MBean
        Logger rootLogger = Logger.getRootLogger();
        hdm.addLoggerMBean(rootLogger.getName());

        // Get each logger from the Log4J Repository and add it to
        // the Hierarchy MBean created above.
        LoggerRepository r = LogManager.getLoggerRepository();
        Enumeration enumer = r.getCurrentLoggers();
        Logger logger = null;

        while (enumer.hasMoreElements()) {
           logger = (Logger) enumer.nextElement();
           hdm.addLoggerMBean(logger.getName());
        }
    }
    

}
