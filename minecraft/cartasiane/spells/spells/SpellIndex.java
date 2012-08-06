package cartasiane.spells.spells;

import java.util.HashMap;
import java.util.Map;

import cartasiane.spells.SpellEffect;

public class SpellIndex
{
	private static SpellEffect[] registeredSpells;
	public static SpellEffect arrow;
	public static SpellEffect arrowRain;
	
	public static SpellEffect getSpell(int id)
	{
		return registeredSpells[id];
	}
	
	static
	{
		arrow = new ArrowSpell();
		arrowRain = new ArrowRainSpell();
		registeredSpells = new SpellEffect[500];
		registeredSpells[0] = arrow;
		registeredSpells[1] = arrowRain;
	}
	
}
