package net.minecraft.schedule;

import net.minecraft.Minecraft;

public class SynchronousSchedule extends MinecraftSchedule
{
	@Override
	public void schedule(MinecraftScheduled schedule)
	{
		Minecraft.minecraft.schedule(schedule);
	}
}
