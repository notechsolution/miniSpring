package com.minis;

import java.io.File;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.StandardRoot;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args ) throws LifecycleException {
        System.out.println( "Hello World!" );
        Tomcat tomcat = new Tomcat();
        String webappDirLocation = "WebContent";
        StandardContext context = (StandardContext) tomcat.addWebapp("/", new File(webappDirLocation).getAbsolutePath());
        Connector connector = new Connector();
        connector.setPort(8081);
        tomcat.setConnector(connector);
        tomcat.start();
        tomcat.getServer().await();
    }
}


//        File additionWebInfClasses = new File("target/classes");
//        WebResourceRoot resources = new StandardRoot(context);
//        resources.addPreResources(new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClasses.getAbsolutePath(), "/"));
//
//        context.setResources(resources);