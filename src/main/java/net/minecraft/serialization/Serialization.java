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

	public static short readShort(InputStream in) throws IOException
	{
		byte[] buf = new byte[2];
		read(in, buf);
		short ret = 0;
		ret |= (buf[0] & 0xFF) << 8;
		ret |= (buf[1] & 0xFF) << 0;
		return ret;
	}

	public static int readUnsignedShort(InputStream in) throws IOException
	{
		return readShort(in) & 0xFFFF;
	}

	public static int readInt(InputStream in) throws IOException
	{
		byte[] buf = new byte[4];
		read(in, buf);
		int ret = 0;
		ret |= (buf[0] & 0xFF) << 24;
		ret |= (buf[1] & 0XFF) << 16;
		ret |= (buf[2] & 0xFF) << 8;
		ret |= (buf[3] & 0xFF) << 0;
		return ret;
	}

	public static long readUnsignedInt(InputStream in) throws IOException
	{
		return ((long)readInt(in)) & 0xFFFFFFFFL;
	}

	public static long readLong(InputStream in) throws IOException
	{
		byte[] buf = new byte[8];
		read(in, buf);
		long ret = 0;
		ret |= (buf[0] & 0xFFL) << 56;
		ret |= (buf[1] & 0xFFL) << 48;
		ret |= (buf[2] & 0xFFL) << 40;
		ret |= (buf[3] & 0xFFL) << 32;
		ret |= (buf[4] & 0xFFL) << 24;
		ret |= (buf[5] & 0xFFL) << 16;
		ret |= (buf[6] & 0xFFL) << 8;
		ret |= (buf[7] & 0xFFL) << 0;
		return ret;
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

			current = in.read();
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

			current = in.read();
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

	public static void write(OutputStream out, byte[] buf, int offset, int length) throws IOException
	{
		out.write(buf, offset, length);
	}

	public static void write(OutputStream out, byte[] buf) throws IOException
	{
		write(out, buf, 0, buf.length);
	}

	public static void writeShort(OutputStream out, int v) throws IOException
	{
		byte[] buf= new byte[2];
		buf[0] = (byte) (v >> 8);
		buf[1] = (byte) (v >> 0);
		write(out, buf);
	}

	public static void writeInt(OutputStream out, int v) throws IOException
	{
		byte[] buf = new byte[4];
		buf[0] = (byte) (v >> 24);
		buf[1] = (byte) (v >> 16);
		buf[2] = (byte) (v >> 8);
		buf[3] = (byte) (v >> 0);
		write(out, buf);
	}

	public static void writeLong(OutputStream out, long v) throws IOException
	{
		byte[] buf = new byte[8];
		buf[0] = (byte) (v >> 56);
		buf[1] = (byte) (v >> 48);
		buf[2] = (byte) (v >> 40);
		buf[3] = (byte) (v >> 32);
		buf[4] = (byte) (v >> 24);
		buf[5] = (byte) (v >> 16);
		buf[6] = (byte) (v >> 8);
		buf[7] = (byte) (v >> 0);
		write(out, buf);
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
