package com.xingkong1983.star.log;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;

public class StarLogFactory implements ILoggerFactory {
	ConcurrentMap<String, Logger> loggerMap;

	public StarLogFactory() {
		loggerMap = new ConcurrentHashMap<>();
		StarLog.lazyInit();
	}

	/**
	 * Return an appropriate {@link StarLog} instance by name.
	 */
	public Logger getLogger(String name) {
		Logger StarLog = loggerMap.get(name);
		if (StarLog != null) {
			return StarLog;
		} else {
			Logger newInstance = new StarLog(name);
			Logger oldInstance = loggerMap.putIfAbsent(name, newInstance);
			return oldInstance == null ? newInstance : oldInstance;
		}
	}

	/**
	 * Clear the internal logger cache.
	 *
	 * This method is intended to be called by classes (in the same package) for
	 * testing purposes. This method is internal. It can be modified, renamed or
	 * removed at any time without notice.
	 *
	 * You are strongly discouraged from calling this method in production code.
	 */
	void reset() {
		loggerMap.clear();
	}
}
