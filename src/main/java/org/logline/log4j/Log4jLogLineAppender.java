package org.logline.log4j;

import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.logline.LogLineAppender;

/**
 * @author Yinon Sharifi
 */

@Plugin(name = "Log4jLogLineAppender",
		category = Core.CATEGORY_NAME,
		elementType = Appender.ELEMENT_TYPE,
		printObject = true)
public class Log4jLogLineAppender extends AbstractAppender {
	private LogLineAppender logLineAppender = new LogLineAppender();

	@PluginFactory
	public static Log4jLogLineAppender createAppender(@PluginAttribute("name") final String name) {
		return new Log4jLogLineAppender(name);
	}

	protected Log4jLogLineAppender(String name) {
		super(name, null, null, false);
	}

	@Override
	public void append(LogEvent event) {
		logLineAppender.doAppend(new Log4jLoggingEvent(event));
	}
}
