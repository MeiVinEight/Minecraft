package org.mve.text;

public class Hexadecimal
{
	private static final byte[] MAP = "0123456789ABCDEF".getBytes();

	public static byte[] transform(long num)
	{
		byte[] ret = new byte[16];
		for (int i = 0; i < 16; i++)
		{
			ret[i] = MAP[(int) ((num >>> ((15 - i) * 4)) & 0xF)];
		}
		return ret;
	}
}
