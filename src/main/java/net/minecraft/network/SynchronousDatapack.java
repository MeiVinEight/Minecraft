package net.minecraft.network;

import net.minecraft.Environment;
import net.minecraft.network.datapack.Datapack;
import net.minecraft.schedule.ScheduledRunnable;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronousDatapack extends ScheduledRunnable
{
	private final Queue<Datapack> datapack = new LinkedList<>();
	private final ReentrantLock lock = new ReentrantLock();
	private final NetworkManager connection;

	public SynchronousDatapack(NetworkManager connection)
	{
		this.connection = connection;
	}

	public void ensure(Datapack datapack)
	{
		this.lock.lock();
		this.datapack.add(datapack);
		this.lock.unlock();
	}

	@Override
	public void run()
	{
		this.lock.lock();
		Datapack datapack;
		while ((datapack = this.datapack.poll()) != null)
		{
			try
			{
				datapack.consume(this.connection);
			}
			catch (Throwable e)
			{
				Environment.exception(System.out, e);
			}
		}
	}
}
