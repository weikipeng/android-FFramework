package com.fan.framework.config;

public class FFConfig {
	public static final class NetConfig {
		public static final int CONNECTION_TIMEOUT = 10000;

		public static final int CONNECTION_TIMEOUT_3G = 10000;

		public static final int CONNECTION_TIMEOUT_2G = 10000;
	}

	public static final boolean DEBUG = false;
	public static final boolean LOG_ENABLE = DEBUG;
	public static final boolean SHOW_DEBUG_TOAST = DEBUG;
	public static final boolean LOG2SD_ENABLE = DEBUG;
	public static final String CACHE_DIR = "maiDouMi1";
	public static final String LOG_DIR = "maiDouMi_log1";
	public static final String LOG_FILE = "appLog1";

	public static int getNetTimeOut() {
		return NetConfig.CONNECTION_TIMEOUT;
	}
}
