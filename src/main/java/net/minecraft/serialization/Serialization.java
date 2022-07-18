package net.minecraft.serialization;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Serialization
{

	public static void read(InputStream in, byte[] b, int offset, int len) throws IOException
	{
		while (len > 0)
		{
			int i = in.read(b, offset, len);
			if (i < 0)
			{
				throw new EOFException();
			}
			offset += i;
			len -= i;
		}
	}

	public static void read(InputStream in, byte[] buf) throws IOException
	{
		read(in, buf, 0, buf.length);
	}

	public static int read(InputStream in) throws IOException
	{
		byte[] buf = new byte[1];
		read(in, buf, 0, 1);
		return buf[0] & 0xFF;
	}

	public static int readVarInt(InputStream in) throws IOException
	{
		int ret = 0;
		int i = 0;
		int current;
		do
		{
			if (i >= 5)
			{
				throw new IOException("VarInt is too big");
			}

			current = Serialization.read(in);
			ret |= (current & 0x7F) << (7 * i++);
		}
		while (current >>> 7 != 0);
		return ret;
	}

	public static long readVarLong(InputStream in) throws IOException
	{
		long ret = 0;
		int i = 0;
		long current;
		do
		{
			if (i >= 10)
			{
				throw new IOException("VarInt is too big");
			}

			current = Serialization.read(in);
			ret |= (current & 0x7F) << (7 * i++);
		}
		while (current >>> 7 != 0);
		return ret;
	}

	public static String readString(InputStream in) throws IOException
	{
		int len = Serialization.readVarInt(in);
		byte[] b = new byte[len];
		Serialization.read(in, b);
		return new String(b, StandardCharsets.UTF_8);
	}

	public static void writeVarInt(OutputStream out, int v) throws IOException
	{
		do
		{
			int b = v & 0x7F;
			v >>>= 7;
			b |= v == 0 ? 0 : 0x80;
			out.write(b);
		}
		while (v != 0);
	}

	public static void writeVarLong(OutputStream out, long v) throws IOException
	{
		do
		{
			int b = (int) (v & 0x7F);
			v >>>= 7;
			b |= v == 0 ? 0 : 0x80;
			out.write(b);
		}
		while (v != 0);
	}

	public static void writeString(OutputStream out, String s) throws IOException
	{
		byte[] b = s.getBytes(StandardCharsets.UTF_8);
		Serialization.writeVarInt(out, b.length);
		out.write(b);
	}
}
