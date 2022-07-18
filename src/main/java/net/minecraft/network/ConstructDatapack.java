package net.minecraft.network;

import net.minecraft.network.datapack.Datapack;

public interface ConstructDatapack
{
	public abstract Datapack construct(DatapackInputStream in);
}
