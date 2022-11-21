package com.xingkong1983.star.log;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Marker;
import org.slf4j.event.Level;
import org.slf4j.event.LoggingEvent;
import org.slf4j.helpers.LegacyAbstractLogger;
import org.slf4j.helpers.MessageFormatter;
import org.slf4j.helpers.NormalizedParameters;
import org.slf4j.spi.LocationAwareLogger;

public class StarLog extends LegacyAbstractLogger {

	private static final long serialVersionUID = 1L;

	private static final long START_TIME = System.currentTimeMillis();

	protected static final int LOG_LEVEL_TRACE = LocationAwareLogger.TRACE_INT;
	protected static final int LOG_LEVEL_DEBUG = LocationAwareLogger.DEBUG_INT;
	protected static final int LOG_LEVEL_INFO = LocationAwareLogger.INFO_INT;
	protected static final int LOG_LEVEL_WARN = LocationAwareLogger.WARN_INT;
	protected static final int LOG_LEVEL_ERROR = LocationAwareLogger.ERROR_INT;

	static char SP = ' ';
	static final String TID_PREFIX = "tid=";

	// The OFF level can only be used in configuration files to disable logging.
	// It has
	// no printing method associated with it in o.s.Logger interface.
	protected static final int LOG_LEVEL_OFF = LOG_LEVEL_ERROR + 10;

	private static boolean INITIALIZED = false;

	static void lazyInit() {
		if (INITIALIZED) {
			return;
		}
		INITIALIZED = true;
		init();
	}

	// external software might be invoking this method directly. Do not rename
	// or change its semantics.
	static void init() {
		System.out.println("========================================");
		System.out.println("StarLog init.");
		System.out.println("========================================");
		StarLogThread.begin();

	}

	/** The current log level */
	protected int currentLogLevel = LOG_LEVEL_DEBUG;
	/** The short name of this simple log instance */
	private transient String shortLogName = null;

	/**
	 * Package access allows only {@link StarLogFactory} to instantiate StarLog
	 * instances.
	 */
	StarLog(String name) {
		this.name = name;
	}

	private String computeShortName() {
		return name.substring(name.lastIndexOf(".") + 1);
	}

	/**
	 * Is the given log level currently enabled?
	 *
	 * @param logLevel is this level enabled?
	 * @return whether the logger is enabled for the given level
	 */
	protected boolean isLevelEnabled(int logLevel) {
		// log level are numerically ordered so can use simple numeric
		// comparison
		return (logLevel >= currentLogLevel);
	}

	/** Are {@code trace} messages currently enabled? */
	public boolean isTraceEnabled() {
		return isLevelEnabled(LOG_LEVEL_TRACE);
	}

	/** Are {@code debug} messages currently enabled? */
	public boolean isDebugEnabled() {
		return isLevelEnabled(LOG_LEVEL_DEBUG);
	}

	/** Are {@code info} messages currently enabled? */
	public boolean isInfoEnabled() {
		return isLevelEnabled(LOG_LEVEL_INFO);
	}

	/** Are {@code warn} messages currently enabled? */
	public boolean isWarnEnabled() {
		return isLevelEnabled(LOG_LEVEL_WARN);
	}

	/** Are {@code error} messages currently enabled? */
	public boolean isErrorEnabled() {
		return isLevelEnabled(LOG_LEVEL_ERROR);
	}



	/**
	 * StarLog's implementation of
	 * {@link org.slf4j.helpers.AbstractLogger#handleNormalizedLoggingCall(Level, Marker, String, Object[], Throwable)
	 * AbstractLogger#handleNormalizedLoggingCall} }
	 *
	 * @param level          the SLF4J level for this event
	 * @param marker         The marker to be used for this event, may be null.
	 * @param messagePattern The message pattern which will be parsed and formatted
	 * @param arguments      the array of arguments to be formatted, may be null
	 * @param throwable      The exception whose stack trace should be logged, may
	 *                       be null
	 */
	@Override
	protected void handleNormalizedLoggingCall(Level level, Marker marker, String messagePattern, Object[] arguments,
			Throwable throwable) {
		ArrayList<Marker> markerList = new ArrayList();
		markerList.add(marker);
		StackTraceElement[] stactTraceList = Thread.currentThread().getStackTrace();
		StarLogMo starLogMo = new StarLogMo(stactTraceList[4], level.name());
		innerHandleNormalizedLoggingCall(level, starLogMo, markerList, messagePattern, arguments, throwable);

	}

	public void log(LoggingEvent event) {
		int levelInt = event.getLevel().toInt();

		if (!isLevelEnabled(levelInt)) {
			return;
		}

		NormalizedParameters np = NormalizedParameters.normalize(event);

		StackTraceElement[] stactTraceList = Thread.currentThread().getStackTrace();
		StarLogMo starLogMo = new StarLogMo(stactTraceList[4], event.getLevel().name());
		innerHandleNormalizedLoggingCall(event.getLevel(), starLogMo, event.getMarkers(), np.getMessage(),
				np.getArguments(), event.getThrowable());
	}

	private void innerHandleNormalizedLoggingCall(Level level, StarLogMo starLogMo, List<Marker> markers,
			String messagePattern, Object[] arguments, Throwable throwable) {
		StringBuilder buf = new StringBuilder(32);

		// Append the name of the log instance if so configured

//		if (shortLogName == null)
//			shortLogName = computeShortName();
//		buf.append(String.valueOf(shortLogName)).append(" - ");
//
//		buf.append(String.valueOf(name)).append(" - ");

		buf.append(starLogMo.getHeadStr());

		String formattedMessage = MessageFormatter.basicArrayFormat(messagePattern, arguments);

		// Append the message
		buf.append(formattedMessage);

		StarLogThread.push(buf.toString());

	}

	@Override
	protected String getFullyQualifiedCallerName() {
		return null;
	}
}
