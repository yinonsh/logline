package org.logline.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Yinon Sharifi
 */

public class ThreadDumper {

	public static void dumpAllThreadStackTraces() {
		try {
			long start = System.currentTimeMillis();
			Map<Thread, StackTraceElement[]> stackTraces = Thread.getAllStackTraces();

			List<String> threadDumps = new ArrayList<String>();

			for (Map.Entry<Thread, StackTraceElement[]> threadStackTrace : stackTraces.entrySet()) {
				Thread t = threadStackTrace.getKey();
				StackTraceElement[] stackTraceElements = threadStackTrace.getValue();

				StringBuilder threadDump = new StringBuilder();
				threadDump.append("\"").append(t.getName()).append("\"\n     java.lang.Thread.State:")
						.append(t.getState());

				for (StackTraceElement stackTraceElement : stackTraceElements) {
					threadDump.append("\n                 at ").append(stackTraceElement);
				}

				threadDump.append("\n\n");

				threadDumps.add(threadDump.toString());
			}

			StringBuilder fullDump = new StringBuilder();

			fullDump.append(
					String.format("Full thread dump, taken on %s, includes %d threads", new Date(), stackTraces.size()))
					.append("\n");

			for (String td : threadDumps) {
				fullDump.append(td);
			}

			System.out.println(fullDump.toString());
			long duration = System.currentTimeMillis() - start;
			System.out.println(String.format("Took %d ms to dump all threads stack traces to log", duration));
		} catch (Exception e) {
			System.out.println("Failed to generate full thread dump");
			e.printStackTrace();
		}
	}
}
