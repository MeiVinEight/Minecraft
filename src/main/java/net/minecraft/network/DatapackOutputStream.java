package net.minecraft.network;

import net.minecraft.serialization.Compression;
import net.minecraft.serialization.Serialization;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class DatapackOutputStream extends ByteArrayOutputStream
{
	public void writeVarInt(int v) throws IOException
	{
		Serialization.writeVarInt(this, v);
	}

	public void writeVarLong(long v) throws IOException
	{
		Serialization.writeVarLong(this, v);
	}

	public void writeString(String s) throws IOException
	{
		Serialization.writeString(this, s);
	}

	public void compress(int threshold) throws IOException
	{
		byte[] buf = Arrays.copyOf(this.buf, this.count);
		this.count = 0;
		int ulen = 0;
		if (buf.length > threshold)
		{
			ulen = buf.length;
			buf = Compression.compress(buf);
		}
		this.writeVarInt(ulen);
		this.write(buf);
	}
}
