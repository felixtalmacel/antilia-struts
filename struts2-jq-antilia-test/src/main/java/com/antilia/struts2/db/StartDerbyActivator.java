/**
 * 
 */
package com.antilia.struts2.db;

import java.io.PrintWriter;

import org.apache.derby.drda.NetworkServerControl;

/**
 * Iservcie Activator used to start derby.
 * 
 * @author Ernesto Reinaldo Barreiro (reiern70@gmail.com)
 *
 */
public class StartDerbyActivator {

	NetworkServerControl derby = null;
	
	public StartDerbyActivator() {
		try {
			derby = new NetworkServerControl("reiern","reiern");
		} catch (Exception e) {			
			e.printStackTrace();
		}
	}
	/* (non-Javadoc)
	 * @see com.antilia.common.osgi.IServiceActivator#isMandatory()
	 */
	public boolean isMandatory() throws Exception {
		return true;
	}

	/* (non-Javadoc)
	 * @see com.antilia.common.osgi.IServiceActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start() throws Exception {
		derby.start(new PrintWriter(System.out));
	}

	/* (non-Javadoc)
	 * @see com.antilia.common.osgi.IServiceActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop() throws Exception {
		derby.shutdown();
	}

}
