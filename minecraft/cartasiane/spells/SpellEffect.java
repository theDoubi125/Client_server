package cartasiane.spells;

import java.util.Random;

import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityLightningBolt;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.MovingObjectPosition;

public interface SpellEffect
{
	public void cast(EntityPlayer caster, int... vars);
	public int varsCount();
	public int icon();
	public int id();
	public int varIcon(int var);
	public String name();
	public String varName(int var);
	public int manaCost(int... vars);
	public int loadTime(int... vars);
	public int reloadTime(int... vars);
	public int getVar(int i);
}