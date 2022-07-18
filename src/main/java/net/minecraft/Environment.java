package net.minecraft;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;

public class Environment
{
	private static final String CAUSE_CAPTION = "Caused by: ";
	private static final String SUPPRESSED_CAPTION = "Suppressed: ";

	public static void sleep(int millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public static void sleep(long millis, int nanos)
	{
		try
		{
			Thread.sleep(millis, nanos);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	public static void stack(OutputStream out, StackTraceElement[] frames, String prefix)
	{
		PrintStream print = new PrintStream(out);
		for (StackTraceElement element : frames)
		{
			print.print(prefix + "\tat ");
			print.print(element.getClassName());
			print.print('.');
			print.print(element.getMethodName());
			print.print('(');
			print.print(
				element.isNativeMethod() ?
					"Native Method" :
					element.getFileName() != null && element.getLineNumber() >= 0 ?
						element.getFileName() + ":" + element.getLineNumber() :
						element.getFileName() != null ?
							element.getFileName() :
							"Unknown Source");
			print.print(')');
			print.println();
		}
	}

	public static void exception(OutputStream out, Throwable t)
	{
		exception(out, t, new StackTraceElement[0], "", "", Collections.newSetFromMap(new IdentityHashMap<>()));
	}

	public static void exception(OutputStream out, Throwable t, StackTraceElement[] enclosingTrace, String caption, String prefix, Set<Throwable> dejaVe)
	{
		PrintStream print = new PrintStream(out);
		if (dejaVe.contains(t))
		{
			print.print(prefix + caption + "[CIRCULAR REFERENCE: ");
			print.print(t.getClass().getTypeName());
			if (t.getLocalizedMessage() != null)
			{
				print.print(": ");
				print.print(t.getLocalizedMessage());
			}
			print.print(']');
			print.println();
		}
		else
		{
			dejaVe.add(t);
			StackTraceElement[] trace = t.getStackTrace();
			int m = trace.length - 1;
			int n = enclosingTrace.length - 1;
			while (m >= 0 && n >= 0 && trace[m].equals(enclosingTrace[n]))
			{
				m--;
				n--;
			}
			int frameInCommon = trace.length - 1 - m;

			print.print(prefix);
			print.print(t.getClass().getTypeName());
			if (t.getLocalizedMessage() != null)
			{
				print.print(": ");
				print.print(t.getLocalizedMessage());
			}
			print.println();
			stack(out, Arrays.copyOf(trace, m + 1), prefix);
			if (frameInCommon != 0)
			{
				print.print(prefix + "\t... ");
				print.print(frameInCommon);
				print.print(" more");
				print.println();
			}

			for (Throwable se : t.getSuppressed())
			{
				exception(out, se, trace, SUPPRESSED_CAPTION, prefix + "\t", dejaVe);
			}

			Throwable cause = t.getCause();
			if (cause != null)
			{
				exception(out, cause, trace, CAUSE_CAPTION, prefix, dejaVe);
			}
		}
	}
}
