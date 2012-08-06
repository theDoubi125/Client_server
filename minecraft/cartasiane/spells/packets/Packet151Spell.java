package cartasiane.spells.packets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetHandler;
import net.minecraft.src.Packet;
import cartasiane.spells.Spell;

public class Packet151Spell extends Packet
{
	public int spellID, playerID, varCount, vars[];

	public Packet151Spell()
	{
		
	}

	public Packet151Spell(EntityPlayer player, Spell spell)
	{
		spellID = spell.getID();
		playerID = player.entityId;
		varCount = spell.varsCount();
		vars = spell.getVars();
	}

	public void readPacketData(DataInputStream stream)
			throws IOException
	{
		spellID = stream.readInt();
		playerID = stream.readInt();
		varCount = stream.readInt();
		vars = new int[varCount];
		for(int i=0; i<varCount; i++)
		{
			vars[i] = stream.readInt();
		}
	}

	public void writePacketData(DataOutputStream stream)
			throws IOException
	{
		stream.writeInt(spellID);
		stream.writeInt(playerID);
		stream.writeInt(varCount);
		stream.writeInt(spellID);
		for(int i=0; i<varCount; i++)
		{
			stream.writeInt(vars[i]);
		}
	}

	public void processPacket(NetHandler nethandler)
	{
		nethandler.handleSpell(this);
	}

	public int getPacketSize()
	{
		return 16 + varCount*4;
	}
	
}
