package cartasiane.spells;


import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.RenderHelper;
import net.minecraft.src.ScaledResolution;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiSpell extends GuiScreen
{
	public GuiSpell(EntityPlayer player)
	{
		this.player = player;
		spellPoints = 20;
	}
	
	public void initGui()
    {
		
    }
	
	public void drawScreen(int xMouse, int yMouse, float f)
	{
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		drawDefaultBackground();
        RenderHelper.enableGUIStandardItemLighting();
		ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
    	int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        
		mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/spellmenu.png"));
		drawTexturedModalRect(i / 2 - 120, j/2 - 80, 0, 0, 241, 128);
		drawTexturedModalRect(i / 2 - 88, j/2 + 49, 0, 128, 176, 32);
		drawUnlockedSpellsGui(i/2-112, j/2-62);
		drawSelectedSpellsGui(i/2-88, j/2+49, xMouse-i/2+88, yMouse-j/2-49);
		
		handleMouse(xMouse-i/2+112, yMouse-j/2+62);
		super.drawScreen(xMouse, yMouse, f);
	}
	
	public void drawUnlockedSpellsGui(int x, int y)
	{
		drawTexturedModalRect(x + 103, y - 1 + spellSliderPos, 0, 160, 12, 15);
		drawTexturedModalRect(x + 214, y - 1 + varSliderPos, 0, 160, 12, 15);
		int startPos = Math.max((int) (((float)spellSliderPos/90f)*(player.spells.unlockedSpells.size()-5)), 0);
		int textureID = mc.renderEngine.getTexture("/gui/spells.png");
		for(int i=0; i<5; i++)
		{
			Spell spell = player.spells.getAvailableSpell(i+startPos);
			if(spell != null)
			{
				drawCenteredString(fontRenderer, spell.getName(), x + 59, y + 8 + i*21, -1);
				mc.renderEngine.bindTexture(textureID);
				drawTexturedModalRect(x + 1, y + 1 + i*21, (spell.getIcon()%16)*16, (spell.getIcon()/16)*16, 16, 16);
				if(i + startPos == spellSelected)
					drawRect(x, y + 21*i, x+100, y + 21*i+19, 0x5500b4c4);
			}
		}
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		
		
		mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/spellmenu.png"));

		Spell spell = mc.thePlayer.spells.getAvailableSpell(spellSelected);
		drawString(fontRenderer, "points : " + spellPoints, x + 130, y + 97, -1);
		if(spell != null)
		{
			startPos = Math.max(Math.round((((float)varSliderPos/90f)*(spell.varsCount()-3))), 0);
			int guiTextureID = mc.renderEngine.getTexture("/gui/spellmenu.png");
			for(int i=0; i<3; i++)
			{
				if(i + startPos >= spell.varsCount())
					return;
				int icon = spell.getIconForVar(i+startPos);
				mc.renderEngine.bindTexture(guiTextureID);
				drawTexturedModalRect(x + 134, y + 3 + i*33, 22*icon, 175, 22, 22);
				drawCenteredString(fontRenderer, ""+spell.getVar(i+startPos), x + 179, y + 11 + i*33 , -1);
			}
		}
	}
	
	public void drawSelectedSpellsGui(int x, int y, int xMouse, int yMouse)
	{
		for(int i=0; i<9; i++)
		{
			mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/spells.png"));
			Spell spell = player.spells.getEquipedSpell(i);
			if(spell != null)
				drawTexturedModalRect(x + 8 + i*18, y + 8, (spell.getIcon()%16)*16, (spell.getIcon()/16)*16, 16, 16);
		}
		if(xMouse > 7 && xMouse < 169 && yMouse > 7 && yMouse < 25)
		{
			slotUnderMouse = ((xMouse-7)/18);
			drawRect(x+7 + slotUnderMouse*18, y+7, x+7 + ((xMouse-7)/18 + 1)*18, y+7+18, 0x55ffffff);
		}
		else slotUnderMouse = -1;
		if(spellDragged != null)
		{
			drawTexturedModalRect(x + xMouse - 8, y + yMouse - 8, (spellDragged.getIcon()%16)*16, (spellDragged.getIcon()/16)*16, 16, 16);
		}
	}
	
	public void handleMouse(int xMouse, int yMouse)
	{
		if(Mouse.isButtonDown(0))
		{
			if(!isClicking)
			{
				isClicking = true;
				if(xMouse > 0 && xMouse < 102 && yMouse > 0 && yMouse < 105)
				{
					int startPos = Math.max((int) (((float)spellSliderPos/90f)*(player.spells.unlockedSpells.size()-5)), 0);
					spellSelected = startPos + yMouse/21;
					if(xMouse > 2 && xMouse < 18 && yMouse%22 > 2 && yMouse%22 < 18)
						spellDragged = player.spells.getAvailableSpell(startPos + yMouse/21);
				}
				if(slotUnderMouse >= 0)
				{
					spellDragged = player.spells.getEquipedSpell(slotUnderMouse);
					player.spells.setEquipedSpell(slotUnderMouse, null);
				}
				if(yMouse > 0 && yMouse < 100)
				{
					if(xMouse > 102 && xMouse < 115)
						isSlidingSpells = true;
					else if(xMouse > 214 && xMouse < 225)
						isSlidingVars = true;
				}
				
				Spell selectedSpell = mc.thePlayer.spells.getAvailableSpell(spellSelected);
				if(selectedSpell != null)
				{					
					int startPos = Math.max(Math.round((((float)varSliderPos/90f)*(selectedSpell.varsCount()-3))), 0);
					if(selectedSpell.varsCount() > startPos + (yMouse-13)/33)
					{						
						if((yMouse-13)%33 <= 11 && (yMouse-13)/33 >= 0 && (yMouse-13)/33 < 3 && xMouse > 161 && xMouse < 168 && selectedSpell.getVar(startPos + (yMouse-13)/33) > 0)
						{
							selectedSpell.decrVar(startPos + (yMouse-13)/33);
							spellPoints++;
						}
						if((yMouse-13)%33 <= 11 && (yMouse-13)/33 >= 0 && (yMouse-13)/33 < 3 && xMouse > 190 && xMouse < 197 && spellPoints > 0)
						{
							selectedSpell.incrVar(startPos + (yMouse-13)/33);
							spellPoints--;
						}
					}
				}
			}
		}
		else
		{
			if(isClicking && slotUnderMouse >= 0 && spellDragged != null)
				player.spells.setEquipedSpell(slotUnderMouse, spellDragged);
			spellDragged = null;
			isSlidingSpells = false;
			isSlidingVars = false;
			isClicking = false;
		}
		if(isSlidingSpells)
			spellSliderPos = Math.min(Math.max(0, yMouse - 6), 90);
		if(isSlidingVars)
			varSliderPos = Math.min(Math.max(0, yMouse - 6), 90);
	}
	
	private EntityPlayer player;
	private int spellSliderPos, spellSelected, varSliderPos, varSelected;
	private Spell spellDragged;
	private boolean isSlidingSpells, isSlidingVars, isClicking;
	private int slotUnderMouse = -1;
	public int spellPoints;
}
