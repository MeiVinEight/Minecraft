package net.minecraft.network;

import net.minecraft.network.datapack.Datapack;
import net.minecraft.serialization.Serialization;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class NetworkManager
{
	private final Socket connection;
	private final InputStream I;
	private final OutputStream O;
	private int state;
	private boolean compress;
	private int threshold;

	public NetworkManager(Socket connection) throws IOException
	{
		this.connection = connection;
		this.I = connection.getInputStream();
		this.O = connection.getOutputStream();
	}

	public Datapack receive() throws IOException
	{
		byte[] data;
		synchronized (this.I)
		{
			data = new byte[Serialization.readVarInt(this.I)];
			Serialization.read(this.I, data);
		}
		DatapackInputStream in = new DatapackInputStream(data);
		if (this.compress)
		{
			in.uncompress();
		}

		int ID = in.readVarInt();
		return ProtocolLibrary.DATAPACK[this.state][ProtocolLibrary.CLIENTBOUND][ID].construct(in);
	}

	public void send(Datapack datapack) throws IOException
	{
		DatapackOutputStream out = new DatapackOutputStream();
		out.writeVarInt(datapack.ID);
		datapack.serialize(out);
		if (this.compress)
		{
			out.compress(this.threshold);
		}
		byte[] data = out.toByteArray();
		synchronized (this.O)
		{
			Serialization.writeVarInt(this.O, data.length);
			this.O.write(data);
			this.O.flush();
		}
	}

	public void disconnect() throws IOException
	{
		this.connection.close();
	}
}
