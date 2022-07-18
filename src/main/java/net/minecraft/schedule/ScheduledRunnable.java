package net.minecraft.schedule;

public abstract class ScheduledRunnable implements Runnable
{
	private boolean canceled = false;

	public void cancel(boolean canceled)
	{
		this.canceled = canceled;
	}

	public boolean canceled()
	{
		return this.canceled;
	}
}
