package org.logline.logback;

import java.util.List;

import org.logline.LoggingEventProcessor;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.FilterAttachableImpl;
import ch.qos.logback.core.spi.FilterReply;
import ch.qos.logback.core.status.WarnStatus;

/**
 * @author Yinon Sharifi
 */

public class LogbackLogLineAppender extends ContextAwareBase implements Appender<ILoggingEvent> {

	private String name;

	private Context context;

	private boolean started;

	private FilterAttachableImpl<ILoggingEvent> fai = new FilterAttachableImpl<>();

	public void process(ILoggingEvent event) {
		LoggingEventProcessor.process(new LogbackLoggingEvent(event));
	}

	/**
	 * The guard prevents an appender from repeatedly calling its own doAppend
	 * method.
	 */
	private boolean guard = false;

	@Override
	public synchronized void doAppend(ILoggingEvent eventObject) {
		// prevent re-entry.
		if (guard) {
			return;
		}

		try {
			guard = true;

			if (!started) {
				addStatus(new WarnStatus("Attempted to append to non started appender [" + name + "].", this));
				return;
			}

			process(eventObject);
		} finally {
			guard = false;
		}
	}

	@Override
	public void start() {
		started = true;
	}

	@Override
	public void stop() {
		started = false;
	}

	@Override
	public boolean isStarted() {
		return started;
	}

	@Override
	public void addFilter(Filter<ILoggingEvent> newFilter) {
		fai.addFilter(newFilter);
	}

	@Override
	public void clearAllFilters() {
		fai.clearAllFilters();
	}

	@Override
	public List<Filter<ILoggingEvent>> getCopyOfAttachedFiltersList() {
		return fai.getCopyOfAttachedFiltersList();
	}

	@Override
	public FilterReply getFilterChainDecision(ILoggingEvent event) {
		return fai.getFilterChainDecision(event);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public Context getContext() {
		return context;
	}

	@Override
	public void setContext(Context context) {
		this.context = context;
	}
}