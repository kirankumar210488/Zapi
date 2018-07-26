package com.Zapi.Utilities;

import java.io.InputStream;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.ConfigurationFactory;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class BaseStep {

	public boolean initilizestatus = false;
	public InputStream isConfig;
	public static String curdir;
	public static Configuration config = null;
	
	Logger Log = Logger.getLogger(this.getClass().getSimpleName());

	public Configuration initialize() {
		curdir = System.getProperty("user.dir");
		System.setProperty("currentDir", curdir);
		PropertyConfigurator.configure("log4j.properties");
		if (!initilizestatus) {
			Log.info("------------------initilizing----------------");
			try {
				ConfigurationFactory factory = new ConfigurationFactory(
						"config.xml");
				config = factory.getConfiguration();
			} catch (ConfigurationException e) {
				e.printStackTrace();
			}
			initilizestatus = true;
		} else {
			Log.info("Initilization is Already Done");
		}
		return config;
	}
	
	public enum Status{
		passed,failed;
	}
}
