package net.minecraft.schedule;

import net.minecraft.Minecraft;

public class AsynchronousSchedule extends MinecraftSchedule
{
	@Override
	public void schedule(MinecraftScheduled schedule)
	{
		Minecraft.minecraft.execute(new SubscribedAsynchronous(schedule));
	}
}
