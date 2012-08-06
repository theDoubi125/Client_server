package cartasiane.spells;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.NetClientHandler;
import net.minecraft.src.NetServerHandler;
import cartasiane.spells.spells.SpellIndex;


public class Spell
{
	public Spell(int spellID)
	{
		effect = SpellIndex.getSpell(spellID);
		vars = new int[effect.varsCount()];
		reloadTime = 0;
	}
	
	public void incrVar(int id)
	{
		vars[id]++;
	}
	
	public void decrVar(int id)
	{
		vars[id]--;
	}
	
	public boolean isTargetValid(EntityPlayer caster)
	{
		return true;
	}
	
	public String getName()
	{
		return effect.name();
	}
	
	public int getIcon()
	{
		return effect.icon();
	}
	
	public int varsCount()
	{
		return effect.varsCount();
	}
	
	public void reload()
	{
		reloadTime--;
	}
	
	public int getIconForVar(int var)
	{
		return effect.varIcon(var);
	}
	
	public int getVar(int i)
	{
		return effect.getVar(i);
	}
	
	public boolean reloaded()
	{
		return reloadTime <= 0;
	}

	public float reloadRatio()
	{
		return 1.f - (float)reloadTime/(float)effect.reloadTime(vars);
	}
	
	public boolean enoughMana(EntityPlayer player)
	{
		return player.mana >= effect.manaCost(vars);
	}
		
	public int getLoadTime()
	{
		return effect.loadTime(vars);
	}
	
	public int getManaCost()
	{
		return effect.manaCost(vars);
	}
	
	public void cast(EntityPlayer player, NetServerHandler netHandler)
	{
		effect.cast(player, vars);
	}
	
	public int getID()
	{
		return effect.id();
	}
	
	public int[] getVars()
	{
		return vars;
	}
	
	public void startReloadSpell()
	{
		reloadTime = effect.reloadTime(vars);
	}
	
	private SpellEffect effect;
	private int vars[];
	private int reloadTime;
}
