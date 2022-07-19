package net.minecraft.network;

import net.minecraft.Environment;
import net.minecraft.Minecraft;
import net.minecraft.network.datapack.Datapack;
import net.minecraft.schedule.ScheduledRunnable;

import java.io.IOException;

public class SubscribeNetwork extends ScheduledRunnable
{
	private final NetworkManager connection;
	private final SynchronousDatapack synchronous;


	public SubscribeNetwork(NetworkManager connection)
	{
		this.connection = connection;
		Minecraft.minecraft.synchronous.ensure(this.synchronous = new SynchronousDatapack(this.connection));
	}

	@Override
	public void run()
	{
		try
		{
			Datapack datapack = this.connection.receive();
			if (datapack.synchronous)
			{
				this.synchronous.ensure(datapack);
			}
			else
			{
				try
				{
					datapack.consume(this.connection);
				}
				catch (Throwable t)
				{
					Environment.exception(System.out, t);
				}
			}
		}
		catch (IOException e)
		{
			Environment.exception(System.out, e);
		}
	}
}
