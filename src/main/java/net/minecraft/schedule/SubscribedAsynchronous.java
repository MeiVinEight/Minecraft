package net.minecraft.schedule;

import net.minecraft.Environment;
import net.minecraft.Minecraft;

public class SubscribedAsynchronous implements Runnable
{
	private final MinecraftScheduled schedule;

	public SubscribedAsynchronous(MinecraftScheduled schedule)
	{
		this.schedule = schedule;
	}

	@Override
	public void run()
	{
		long next = System.nanoTime();
		while (!this.schedule.canceled() && Minecraft.minecraft.running())
		{
			next += 20_000_000;
			if (this.schedule.run() && !this.schedule.timer())
			{
				break;
			}

			long time = next - System.nanoTime();
			if (time > 0)
			{
				Environment.sleep(time / 1_000_000, (int) (time % 1_000_000));
			}
		}
	}
}
