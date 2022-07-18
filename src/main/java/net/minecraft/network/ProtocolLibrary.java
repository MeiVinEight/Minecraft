package net.minecraft.network;

import net.minecraft.network.datapack.Datapack;
import org.mve.invoke.MethodKind;
import org.mve.invoke.PolymorphismFactory;

public class ProtocolLibrary
{
	public static final ConstructDatapack[][][] DATAPACK = {
		{
			new ConstructDatapack[0],
			new ConstructDatapack[1]
		},
		{
			new ConstructDatapack[2],
			new ConstructDatapack[2]
		},
		{
			new ConstructDatapack[5],
			new ConstructDatapack[3]
		},
		{
			new ConstructDatapack[104],
			new ConstructDatapack[48]
		}
	};

	public static int STATE_HANDSHAKING = 0;
	public static int STATE_STATUS      = 1;
	public static int STATE_LOGIN       = 2;
	public static int STATE_PLAY        = 3;

	public static int CLIENTBOUND = 0;
	public static int SERVERBOUND = 1;

	private static ConstructDatapack construct(Class<? extends Datapack> c)
	{
		return new PolymorphismFactory<>(ConstructDatapack.class)
			.construct(
				c,
				new MethodKind("construct", Datapack.class, DatapackInputStream.class),
				new MethodKind("<init>", void.class, DatapackInputStream.class)
			)
			.allocate();
	}

	static
	{
	}
}
