package net.minecraft.network;

import net.minecraft.serialization.Compression;
import net.minecraft.serialization.Serialization;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

public class DatapackInputStream extends ByteArrayInputStream
{
	public DatapackInputStream(byte[] data)
	{
		super(data);
	}

	public short readShort() throws IOException
	{
		return Serialization.readShort(this);
	}

	public int readUnsignedShort() throws IOException
	{
		return Serialization.readUnsignedShort(this);
	}

	public int readInt() throws IOException
	{
		return Serialization.readInt(this);
	}

	public long readUnsignedInt() throws IOException
	{
		return Serialization.readUnsignedInt(this);
	}

	public long readLong() throws IOException
	{
		return Serialization.readLong(this);
	}

	public int readVarInt() throws IOException
	{
		return Serialization.readVarInt(this);
	}

	public long readVarLong() throws IOException
	{
		return Serialization.readVarLong(this);
	}

	public String readString() throws IOException
	{
		return Serialization.readString(this);
	}

	public void uncompress() throws IOException
	{
		int ulen = this.readVarInt();
		if (ulen != 0)
		{
			this.buf = Compression.uncompress(Arrays.copyOfRange(this.buf, this.pos, this.count));
			this.mark = this.pos = 0;
			this.count = buf.length;
			if (this.buf.length != ulen)
			{
				throw new IOException("Unknown zlib compressed data");
			}
		}
	}
}
