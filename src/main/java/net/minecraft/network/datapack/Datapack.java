package net.minecraft.network.datapack;

import net.minecraft.network.DatapackOutputStream;
import net.minecraft.network.NetworkManager;

import java.io.IOException;

public abstract class Datapack
{
	public final int ID;
	public boolean synchronous = true;

	public Datapack(int ID)
	{
		this.ID = ID;
	}

	public abstract void serialize(DatapackOutputStream out) throws IOException;

	public void consume(NetworkManager connection) throws Throwable
	{
	}
}
