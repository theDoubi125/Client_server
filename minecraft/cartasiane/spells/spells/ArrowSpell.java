package cartasiane.spells.spells;

import java.util.Random;

import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityPlayer;
import cartasiane.spells.SpellEffect;

public class ArrowSpell implements SpellEffect
{
	public void cast(EntityPlayer caster, int... vars)
	{
		Random rand = new Random();
		for(int i=0; i<=vars[0]; i++)
		{
			EntityArrow arrow = new EntityArrow(caster.worldObj, caster, 2.0f, (float)(15f-Math.atan(vars[1]/5)/Math.PI * 30));
			arrow.setFire(10);
			caster.worldObj.spawnEntityInWorld(arrow);
		}
	}
	
	public int icon()
	{
		return 3;
	}

	public int varsCount()
	{
		return 2;
	}

	public int varIcon(int var)
	{
		return 0;
	}
	
	public String name()
	{
		return "Arrow";
	}

	public String varName(int var)
	{
		return "";
	}
	
	public int manaCost(int... vars)
	{
		return 10;
	}

	public int loadTime(int... vars)
	{
		return 0;
	}

	public int reloadTime(int... vars)
	{
		return 5;
	}
	
	public int id()
	{
		return 0;
	}
	
	public int getVar(int var)
	{
		return 0;
	}
}
