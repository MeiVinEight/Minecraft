package net.minecraft.schedule;

import org.mve.text.Hexadecimal;

import java.util.Arrays;
import java.util.concurrent.ThreadFactory;

public class AsynchronousFactory implements ThreadFactory
{
	@Override
	public Thread newThread(Runnable run)
	{
		Thread t = new Thread(run);
		t.setName(new String(Arrays.copyOfRange(Hexadecimal.transform(t.getId()), 8, 16)));
		return t;
	}
}
