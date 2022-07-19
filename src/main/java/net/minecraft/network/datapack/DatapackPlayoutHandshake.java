package net.minecraft.network.datapack;

import net.minecraft.network.DatapackInputStream;
import net.minecraft.network.DatapackOutputStream;
import net.minecraft.network.NetworkManager;

import java.io.IOException;

public class DatapackPlayoutHandshake extends Datapack
{
	public int version;
	public String address;
	public int port;
	public int next;

	public DatapackPlayoutHandshake()
	{
		super(0x00);
	}

	public DatapackPlayoutHandshake(int version, String address, int port, int next)
	{
		this();
		this.version = version;
		this.address = address;
		this.port = port;
		this.next = next;
	}

	public DatapackPlayoutHandshake(DatapackInputStream in) throws IOException
	{
		this();
		this.version = in.readVarInt();
		this.address = in.readString();
		this.port = in.readUnsignedShort();
		this.next = in.readVarInt();
	}

	@Override
	public void serialize(DatapackOutputStream out) throws IOException
	{
		out.writeVarInt(this.version);
		out.writeString(this.address);
		out.writeShort(this.port);
		out.writeVarInt(this.next);
	}

	@Override
	public void consume(NetworkManager connection) throws Throwable
	{
		connection.state = this.next;
	}
}
