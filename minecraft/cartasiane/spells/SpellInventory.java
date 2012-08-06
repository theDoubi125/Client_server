package cartasiane.spells;

import java.util.ArrayList;

import net.minecraft.src.EntityPlayer;

public class SpellInventory
{
	public SpellInventory(EntityPlayer player)
	{
    	unlockedSpells.add(new Spell(0));  
    	unlockedSpells.add(new Spell(1));
    	
    	spells[0] = unlockedSpells.get(0);
    	spells[1] = unlockedSpells.get(1);
	}
	
	public Spell getEquipedSpell(int id)
	{
		return spells[id];
	}
	
	public Spell getCurrentSpell()
	{
		return spells[currentSpell];
	}
	
	public void setEquipedSpell(int id, Spell spell)
	{
		spells[id] = spell;
	}
	
	public Spell getAvailableSpell(int id)
	{
		if(unlockedSpells.size() > id)
			return unlockedSpells.get(id);
		return null;
	}
	
	public void changeCurrentSpell(int par1)
	{
		if (par1 > 0)
        {
            par1 = 1;
        }

        if (par1 < 0)
        {
            par1 = -1;
        }
        
    	for (currentSpell -= par1; currentSpell < 0; currentSpell += 9) { }
    	
    	for (; currentSpell >= 9; currentSpell -= 9) { }
	}
	
	public void reloadAllSpells()
	{
		for(Spell spell : unlockedSpells)
		{
			spell.reload();
		}
	}
	
    public Spell[] spells = new Spell[9];
    public ArrayList<Spell> unlockedSpells = new ArrayList<Spell>();
    public int currentSpell;
}
