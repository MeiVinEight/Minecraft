package net.minecraft.schedule;

public abstract class MinecraftSchedule
{
	public void ensure(ScheduledRunnable runnable)
	{
		this.schedule(new MinecraftScheduled(runnable, 0, 0));
	}

	public void ensureLater(ScheduledRunnable runnable, long delay)
	{
		this.schedule(new MinecraftScheduled(runnable, delay, 0));
	}

	public void ensureTimer(ScheduledRunnable runnable, long delay, long period)
	{
		this.schedule(new MinecraftScheduled(runnable, delay, period));
	}

	public abstract void schedule(MinecraftScheduled schedule);
}
