package net.minecraft.schedule;

import net.minecraft.Environment;

import java.util.Arrays;

public class MinecraftScheduled
{
	private final ScheduledRunnable runnable;
	private final StackTraceElement[] creator;
	private final boolean timer;
	private final long period;
	private long delay;
	private boolean complete = false;

	public MinecraftScheduled(ScheduledRunnable runnable, long delay, long period)
	{
		this.runnable = runnable;
		this.creator = frame();
		this.timer = period != 0;
		this.delay = delay;
		this.period = period;
	}

	public boolean run()
	{
		this.delay--;
		if (this.delay <= 0)
		{
			try
			{
				this.runnable.run();
			}
			catch (Throwable t)
			{
				this.exception(t);
			}
			this.delay = this.period;
			this.complete = !this.timer;
			return true;
		}
		return false;
	}

	public boolean timer()
	{
		return this.timer;
	}

	public void cancel()
	{
		this.runnable.cancel(true);
	}

	public boolean canceled()
	{
		return this.runnable.canceled() || this.complete;
	}

	private static StackTraceElement[] frame()
	{
		StackTraceElement[] frames = new Throwable().getStackTrace();
		return Arrays.copyOfRange(frames, 1, frames.length);
	}

	private void exception(Throwable t)
	{
		Environment.exception(System.out, t);
		System.out.println("Created by:");
		Environment.stack(System.out, this.creator, "");
	}
}
