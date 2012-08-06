package cartasiane.spells.spells;

import java.util.Random;

import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MovingObjectPosition;
import cartasiane.spells.SpellEffect;

public class ArrowRainSpell implements SpellEffect
{
	public void cast(EntityPlayer caster, int... vars)
	{
		for(int i=0; i<50; i++)
		{
			Random rand = new Random();
			MovingObjectPosition obj = caster.rayTrace(50, 1.0f);
			if(obj == null)
				return;
			double x = obj.blockX + (rand.nextFloat() - 0.5f)*20;
			double y = obj.blockY + 20;
			double z = obj.blockZ + (rand.nextFloat() - 0.5f)*20;
			EntityArrow arrow = new EntityArrow(caster.worldObj, x, y, z);
			arrow.setVelocity(0, -1, 0);
			caster.worldObj.spawnEntityInWorld(arrow);
		}
	}

	public int varsCount()
	{
		return 0;
	}

	public int icon()
	{
		return 0;
	}

	public int varIcon(int var)
	{
		return 0;
	}
	
	public String name()
	{
		return "Arrow Rain";
	}

	public String varName(int var)
	{
		return "";
	}

	public int manaCost(int... vars)
	{
		return 30;
	}

	public int loadTime(int... vars)
	{
		return 70;
	}

	public int reloadTime(int... vars)
	{
		return 200;
	}
	
	public int id()
	{
		return 1;
	}
	
	public int getVar(int i)
	{
		return 0;
	}
}
