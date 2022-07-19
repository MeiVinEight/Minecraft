package net.minecraft;

import net.minecraft.schedule.AsynchronousFactory;
import net.minecraft.schedule.AsynchronousSchedule;
import net.minecraft.schedule.MinecraftSchedule;
import net.minecraft.schedule.MinecraftScheduled;
import net.minecraft.schedule.SynchronousSchedule;
import org.mve.invoke.ReflectionFactory;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class Minecraft implements Runnable
{
	public static final Minecraft minecraft = (Minecraft) ReflectionFactory.UNSAFE.allocateInstance(Minecraft.class);
	private final Thread primitive;
	private final ExecutorService service = Executors.newCachedThreadPool(new AsynchronousFactory());
	private final Queue<MinecraftScheduled> schedule = new LinkedList<>();
	public final MinecraftSchedule synchronous = new SynchronousSchedule();
	public final MinecraftSchedule asynchronous = new AsynchronousSchedule();
	private final ReentrantLock lock = new ReentrantLock();
	private boolean running = false;

	public Minecraft()
	{
		ReflectionFactory.access(Minecraft.class, "minecraft").set(this);
		this.primitive = Thread.currentThread();
	}

	@Override
	public void run()
	{
		ReflectionFactory.access(Minecraft.class, "primitive").set(this, Thread.currentThread());
		this.running = true;

		long next = System.nanoTime();
		while (this.running)
		{
			next += 20_000_000;

			try
			{
				this.lock.lock();
				LinkedList<MinecraftScheduled> inheritedTask = new LinkedList<>();
				MinecraftScheduled task;
				while ((task = this.schedule.poll()) != null)
				{
					if (!task.canceled())
					{
						task.run();
						if (!task.canceled())
						{
							inheritedTask.add(task);
						}
					}
				}
				this.schedule.addAll(inheritedTask);
				this.lock.unlock();
			}
			catch (Throwable t)
			{
				Environment.exception(System.out, t);
			}

			long time = System.nanoTime() - next;
			if (time > 0)
			{
				Environment.sleep(time / 1_000_000, (int) (time % 1_000_000));
			}
		}
	}

	public void schedule(MinecraftScheduled scheduled)
	{
		this.lock.lock();
		this.schedule.add(scheduled);
		this.lock.unlock();
	}

	public void execute(Runnable r)
	{
		this.service.execute(r);
	}

	public boolean primitive()
	{
		return this.primitive == Thread.currentThread();
	}

	public boolean running()
	{
		return this.running;
	}

	public void stop()
	{
		this.service.shutdown();
		this.running = false;
	}
}
