package cartasiane.spells.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;

public class Packet150TargetInfos extends Packet
{
	public int id, level, health, mana;
	public boolean answer;
	
	public Packet150TargetInfos()
	{
		
	}
	
	public Packet150TargetInfos(int id,int level, int health, int mana)
	{
		super();
		this.id = id;
		this.level = level;
		this.health = health;
		this.mana = mana;
		this.answer = true;
	}


	public Packet150TargetInfos(int id)
	{
		this.id = id;
		this.answer = false;
	}
	
	public void readPacketData(DataInputStream stream)
			throws IOException
	{
		id = stream.readInt();
		answer = stream.readBoolean();
		if(answer)
		{
			level = stream.readInt();
			health = stream.readInt();
			mana = stream.readInt();
		}
	}

	public void writePacketData(DataOutputStream stream)
			throws IOException
	{
		stream.writeInt(id);
		stream.writeBoolean(answer);
		if(answer)
		{
			stream.writeInt(level);
			stream.writeInt(health);
			stream.writeInt(mana);
		}
	}

	public void processPacket(NetHandler nethandler)
	{
		nethandler.handleTargetInfos(this);
	}

	public int getPacketSize()
	{
		if(answer)
			return 17;
		return 5;
	}

}
