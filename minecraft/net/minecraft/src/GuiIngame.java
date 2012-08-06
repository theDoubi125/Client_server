package net.minecraft.src;

import java.awt.Color;
import java.util.Random;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cartasiane.spells.Spell;
import cartasiane.spells.SpellInventory;

public class GuiIngame extends Gui
{

	/** doubi */
	
	private int spellBarPos = 0;
    private int spellLoading = 0;
    public boolean selectSpell = false, allwaysShowSpells = false;
	
	/** doubi end */
    private static final RenderItem itemRenderer = new RenderItem();
    private final Random rand = new Random();
    private final Minecraft mc;
    private final GuiNewChat field_73840_e;
    private int updateCounter;

    /** The string specifying which record music is playing */
    private String recordPlaying;

    /** How many ticks the record playing message will be displayed */
    private int recordPlayingUpFor;
    private boolean recordIsPlaying;

    /** Previous frame vignette brightness (slowly changes by 1% each frame) */
    public float prevVignetteBrightness;

    public GuiIngame(Minecraft par1Minecraft)
    {
        updateCounter = 0;
        recordPlaying = "";
        recordPlayingUpFor = 0;
        recordIsPlaying = false;
        prevVignetteBrightness = 1.0F;
        mc = par1Minecraft;
        field_73840_e = new GuiNewChat(par1Minecraft);
    }

    /**
     * Render the ingame overlay with quick icon bar, ...
     */
    public void renderGameOverlay(float par1, boolean par2, int par3, int par4)
    {
    	updateSpellBar();
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        FontRenderer fontrenderer = mc.fontRenderer;
        mc.entityRenderer.setupOverlayRendering();
        GL11.glEnable(GL11.GL_BLEND);

        if (Minecraft.isFancyGraphicsEnabled())
        {
            renderVignette(mc.thePlayer.getBrightness(par1), i, j);
        }
        else
        {
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }

        ItemStack itemstack = mc.thePlayer.inventory.armorItemInSlot(3);

        if (mc.gameSettings.thirdPersonView == 0 && itemstack != null && itemstack.itemID == Block.pumpkin.blockID)
        {
            renderPumpkinBlur(i, j);
        }

        if (!mc.thePlayer.isPotionActive(Potion.confusion))
        {
            float f = mc.thePlayer.prevTimeInPortal + (mc.thePlayer.timeInPortal - mc.thePlayer.prevTimeInPortal) * par1;

            if (f > 0.0F)
            {
                renderPortalOverlay(f, i, j);
            }
        }

        if (!mc.playerControllerMP.func_78747_a())
        {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            drawTargetGui(0, 0, mc.thePlayer);
            if(mc.thePlayer.target != null)
            	drawTargetGui(0, 60, mc.thePlayer.target);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/gui.png"));
            
            renderManaBar();
            
            InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
            zLevel = -90F;
            drawTexturedModalRect(i / 2 - 91, j - 22, 0, 0, 182, 22);
            drawTexturedModalRect((i / 2 - 91 - 1) + inventoryplayer.currentItem * 20, j - 22 - 1, 0, 22, 24, 22);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/icons.png"));
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_ONE_MINUS_DST_COLOR, GL11.GL_ONE_MINUS_SRC_COLOR);
            drawTexturedModalRect(i / 2 - 7, j / 2 - 7, 0, 0, 16, 16);
            GL11.glDisable(GL11.GL_BLEND);
            boolean flag = (mc.thePlayer.heartsLife / 3) % 2 == 1;

            if (mc.thePlayer.heartsLife < 10)
            {
                flag = false;
            }

            int i1 = mc.thePlayer.getHealth();
            int j2 = mc.thePlayer.prevHealth;
            rand.setSeed(updateCounter * 0x4c627);
            boolean flag2 = false;
            FoodStats foodstats = mc.thePlayer.getFoodStats();
            int j4 = foodstats.getFoodLevel();
            int i5 = foodstats.getPrevFoodLevel();
            mc.field_71424_I.startSection("bossHealth");
            renderBossHealth();
            mc.field_71424_I.endSection();

            if (mc.playerControllerMP.shouldDrawHUD())
            {
                int k5 = i / 2 - 91;
                int j6 = i / 2 + 91;
                mc.field_71424_I.startSection("expBar");
                int i7 = mc.thePlayer.xpBarCap();

                if (i7 > 0)
                {
                    char c = '\266';
                    int k8 = (int)(mc.thePlayer.experience * (float)(c + 1));
                    int j9 = (j - 32) + 3;
                    drawTexturedModalRect(k5, j9, 0, 64, c, 5);

                    if (k8 > 0)
                    {
                        drawTexturedModalRect(k5, j9, 0, 69, k8, 5);
                    }
                }

                int l7 = j - 39;
                int l8 = l7 - 10;
                int k9 = mc.thePlayer.getTotalArmorValue();
                int j10 = -1;

                if (mc.thePlayer.isPotionActive(Potion.regeneration))
                {
                    j10 = updateCounter % 25;
                }

                mc.field_71424_I.endStartSection("healthArmor");

                for (int k10 = 0; k10 < 10; k10++)
                {
                    if (k9 > 0)
                    {
                        int j11 = k5 + k10 * 8;

                        if (k10 * 2 + 1 < k9)
                        {
                            drawTexturedModalRect(j11, l8, 34, 9, 9, 9);
                        }

                        if (k10 * 2 + 1 == k9)
                        {
                            drawTexturedModalRect(j11, l8, 25, 9, 9, 9);
                        }

                        if (k10 * 2 + 1 > k9)
                        {
                            drawTexturedModalRect(j11, l8, 16, 9, 9, 9);
                        }
                    }

                    int k11 = 16;

                    if (mc.thePlayer.isPotionActive(Potion.poison))
                    {
                        k11 += 36;
                    }

                    int j12 = 0;

                    if (flag)
                    {
                        j12 = 1;
                    }

                    int i13 = k5 + k10 * 8;
                    int k13 = l7;

                    if (i1 <= 4)
                    {
                        k13 += rand.nextInt(2);
                    }

                    if (k10 == j10)
                    {
                        k13 -= 2;
                    }

                    byte byte3 = 0;

                    if (mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
                    {
                        byte3 = 5;
                    }

                    drawTexturedModalRect(i13, k13, 16 + j12 * 9, 9 * byte3, 9, 9);

                    if (flag)
                    {
                        if (k10 * 2 + 1 < j2)
                        {
                            drawTexturedModalRect(i13, k13, k11 + 54, 9 * byte3, 9, 9);
                        }

                        if (k10 * 2 + 1 == j2)
                        {
                            drawTexturedModalRect(i13, k13, k11 + 63, 9 * byte3, 9, 9);
                        }
                    }

                    if (k10 * 2 + 1 < i1)
                    {
                        drawTexturedModalRect(i13, k13, k11 + 36, 9 * byte3, 9, 9);
                    }

                    if (k10 * 2 + 1 == i1)
                    {
                        drawTexturedModalRect(i13, k13, k11 + 45, 9 * byte3, 9, 9);
                    }
                }

                mc.field_71424_I.endStartSection("food");

                for (int l10 = 0; l10 < 10; l10++)
                {
                    int l11 = l7;
                    int k12 = 16;
                    byte byte2 = 0;

                    if (mc.thePlayer.isPotionActive(Potion.hunger))
                    {
                        k12 += 36;
                        byte2 = 13;
                    }

                    if (mc.thePlayer.getFoodStats().getSaturationLevel() <= 0.0F && updateCounter % (j4 * 3 + 1) == 0)
                    {
                        l11 += rand.nextInt(3) - 1;
                    }

                    if (flag2)
                    {
                        byte2 = 1;
                    }

                    int l13 = j6 - l10 * 8 - 9;
                    drawTexturedModalRect(l13, l11, 16 + byte2 * 9, 27, 9, 9);

                    if (flag2)
                    {
                        if (l10 * 2 + 1 < i5)
                        {
                            drawTexturedModalRect(l13, l11, k12 + 54, 27, 9, 9);
                        }

                        if (l10 * 2 + 1 == i5)
                        {
                            drawTexturedModalRect(l13, l11, k12 + 63, 27, 9, 9);
                        }
                    }

                    if (l10 * 2 + 1 < j4)
                    {
                        drawTexturedModalRect(l13, l11, k12 + 36, 27, 9, 9);
                    }

                    if (l10 * 2 + 1 == j4)
                    {
                        drawTexturedModalRect(l13, l11, k12 + 45, 27, 9, 9);
                    }
                }

                mc.field_71424_I.endStartSection("air");

                if (mc.thePlayer.isInsideOfMaterial(Material.water))
                {
                    int i11 = mc.thePlayer.getAir();
                    int i12 = MathHelper.func_76143_f(((double)(i11 - 2) * 10D) / 300D);
                    int l12 = MathHelper.func_76143_f(((double)i11 * 10D) / 300D) - i12;

                    for (int j13 = 0; j13 < i12 + l12; j13++)
                    {
                        if (j13 < i12)
                        {
                            drawTexturedModalRect(j6 - j13 * 8 - 9, l8, 16, 18, 9, 9);
                        }
                        else
                        {
                            drawTexturedModalRect(j6 - j13 * 8 - 9, l8, 25, 18, 9, 9);
                        }
                    }
                }

                mc.field_71424_I.endSection();
            }

            GL11.glDisable(GL11.GL_BLEND);
            mc.field_71424_I.startSection("actionBar");
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            RenderHelper.enableGUIStandardItemLighting();

            for (int i6 = 0; i6 < 9; i6++)
            {
                int k6 = (i / 2 - 90) + i6 * 20 + 2;
                int j7 = j - 16 - 3;
                renderInventorySlot(i6, k6, j7, par1);
                
                renderSpellSlot(i6, i-spellBarPos+3, j/2 - 88 + 20*i6, par1);
            }

            RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            mc.field_71424_I.endSection();
        }

        if (mc.thePlayer.getSleepTimer() > 0)
        {
            mc.field_71424_I.startSection("sleep");
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            int k = mc.thePlayer.getSleepTimer();
            float f1 = (float)k / 100F;

            if (f1 > 1.0F)
            {
                f1 = 1.0F - (float)(k - 100) / 10F;
            }

            int j1 = (int)(220F * f1) << 24 | 0x101020;
            drawRect(0, 0, i, j, j1);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            mc.field_71424_I.endSection();
        }

        if (mc.playerControllerMP.func_78763_f() && mc.thePlayer.experienceLevel > 0)
        {
            mc.field_71424_I.startSection("expLevel");
            boolean flag1 = false;
            int k1 = flag1 ? 0xffffff : 0x80ff20;
            String s1 = (new StringBuilder()).append("").append(mc.thePlayer.experienceLevel).toString();
            int j3 = (i - fontrenderer.getStringWidth(s1)) / 2;
            int l3 = j - 31 - 4;
            fontrenderer.drawString(s1, j3 + 1, l3, 0);
            fontrenderer.drawString(s1, j3 - 1, l3, 0);
            fontrenderer.drawString(s1, j3, l3 + 1, 0);
            fontrenderer.drawString(s1, j3, l3 - 1, 0);
            fontrenderer.drawString(s1, j3, l3, k1);
            mc.field_71424_I.endSection();
        }

        if (mc.func_71355_q())
        {
            mc.field_71424_I.startSection("demo");
            String s = "";

            if (mc.theWorld.getWorldTime() >= 0x1d6b4L)
            {
                s = StatCollector.translateToLocal("demo.demoExpired");
            }
            else
            {
                s = String.format(StatCollector.translateToLocal("demo.remainingTime"), new Object[]
                        {
                            StringUtils.func_76337_a((int)(0x1d6b4L - mc.theWorld.getWorldTime()))
                        });
            }

            int l1 = fontrenderer.getStringWidth(s);
            fontrenderer.drawStringWithShadow(s, i - l1 - 10, 5, 0xffffff);
            mc.field_71424_I.endSection();
        }

        if (mc.gameSettings.showDebugInfo)
        {
            mc.field_71424_I.startSection("debug");
            GL11.glPushMatrix();
            fontrenderer.drawStringWithShadow((new StringBuilder()).append("Minecraft 1.3.1 (").append(mc.debug).append(")").toString(), 2, 2, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.debugInfoRenders(), 2, 12, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.getEntityDebug(), 2, 22, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.debugInfoEntities(), 2, 32, 0xffffff);
            fontrenderer.drawStringWithShadow(mc.getWorldProviderName(), 2, 42, 0xffffff);
            long l = Runtime.getRuntime().maxMemory();
            long l2 = Runtime.getRuntime().totalMemory();
            long l4 = Runtime.getRuntime().freeMemory();
            long l5 = l2 - l4;
            String s2 = (new StringBuilder()).append("Used memory: ").append((l5 * 100L) / l).append("% (").append(l5 / 1024L / 1024L).append("MB) of ").append(l / 1024L / 1024L).append("MB").toString();
            drawString(fontrenderer, s2, i - fontrenderer.getStringWidth(s2) - 2, 2, 0xe0e0e0);
            s2 = (new StringBuilder()).append("Allocated memory: ").append((l2 * 100L) / l).append("% (").append(l2 / 1024L / 1024L).append("MB)").toString();
            drawString(fontrenderer, s2, i - fontrenderer.getStringWidth(s2) - 2, 12, 0xe0e0e0);
            drawString(fontrenderer, String.format("x: %.5f", new Object[]
                    {
                        Double.valueOf(mc.thePlayer.posX)
                    }), 2, 64, 0xe0e0e0);
            drawString(fontrenderer, String.format("y: %.3f (feet pos, %.3f eyes pos)", new Object[]
                    {
                        Double.valueOf(mc.thePlayer.boundingBox.minY), Double.valueOf(mc.thePlayer.posY)
                    }), 2, 72, 0xe0e0e0);
            drawString(fontrenderer, String.format("z: %.5f", new Object[]
                    {
                        Double.valueOf(mc.thePlayer.posZ)
                    }), 2, 80, 0xe0e0e0);
            drawString(fontrenderer, (new StringBuilder()).append("f: ").append(MathHelper.floor_double((double)((mc.thePlayer.rotationYaw * 4F) / 360F) + 0.5D) & 3).toString(), 2, 88, 0xe0e0e0);
            int i8 = MathHelper.floor_double(mc.thePlayer.posX);
            int i9 = MathHelper.floor_double(mc.thePlayer.posY);
            int l9 = MathHelper.floor_double(mc.thePlayer.posZ);

            if (mc.theWorld != null && mc.theWorld.blockExists(i8, i9, l9))
            {
                Chunk chunk = mc.theWorld.getChunkFromBlockCoords(i8, l9);
                drawString(fontrenderer, (new StringBuilder()).append("lc: ").append(chunk.getTopFilledSegment() + 15).append(" b: ").append(chunk.getBiomeGenForWorldCoords(i8 & 0xf, l9 & 0xf, mc.theWorld.getWorldChunkManager()).biomeName).append(" bl: ").append(chunk.getSavedLightValue(EnumSkyBlock.Block, i8 & 0xf, i9, l9 & 0xf)).append(" sl: ").append(chunk.getSavedLightValue(EnumSkyBlock.Sky, i8 & 0xf, i9, l9 & 0xf)).append(" rl: ").append(chunk.getBlockLightValue(i8 & 0xf, i9, l9 & 0xf, 0)).toString(), 2, 96, 0xe0e0e0);
            }

            drawString(fontrenderer, String.format("ws: %.3f, fs: %.3f, g: %b", new Object[]
                    {
                        Float.valueOf(mc.thePlayer.capabilities.func_75094_b()), Float.valueOf(mc.thePlayer.capabilities.func_75093_a()), Boolean.valueOf(mc.thePlayer.onGround)
                    }), 2, 104, 0xe0e0e0);
            GL11.glPopMatrix();
            mc.field_71424_I.endSection();
        }

        if (recordPlayingUpFor > 0)
        {
            mc.field_71424_I.startSection("overlayMessage");
            float f2 = (float)recordPlayingUpFor - par1;
            int i2 = (int)((f2 * 256F) / 20F);

            if (i2 > 255)
            {
                i2 = 255;
            }

            if (i2 > 0)
            {
                GL11.glPushMatrix();
                GL11.glTranslatef(i / 2, j - 48, 0.0F);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                int k2 = 0xffffff;

                if (recordIsPlaying)
                {
                    k2 = Color.HSBtoRGB(f2 / 50F, 0.7F, 0.6F) & 0xffffff;
                }

                fontrenderer.drawString(recordPlaying, -fontrenderer.getStringWidth(recordPlaying) / 2, -4, k2 + (i2 << 24));
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }

            mc.field_71424_I.endSection();
        }

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glPushMatrix();
        GL11.glTranslatef(0.0F, j - 48, 0.0F);
        mc.field_71424_I.startSection("chat");
        field_73840_e.func_73762_a(updateCounter);
        mc.field_71424_I.endSection();
        GL11.glPopMatrix();

        if (mc.gameSettings.keyBindPlayerList.pressed && (!mc.func_71387_A() || mc.thePlayer.sendQueue.playerInfoList.size() > 1))
        {
            mc.field_71424_I.startSection("playerList");
            NetClientHandler netclienthandler = mc.thePlayer.sendQueue;
            java.util.List list = netclienthandler.playerInfoList;
            int i3 = netclienthandler.currentServerMaxPlayers;
            int k3 = i3;
            int i4 = 1;

            for (; k3 > 20; k3 = ((i3 + i4) - 1) / i4)
            {
                i4++;
            }

            int k4 = 300 / i4;

            if (k4 > 150)
            {
                k4 = 150;
            }

            int j5 = (i - i4 * k4) / 2;
            byte byte0 = 10;
            drawRect(j5 - 1, byte0 - 1, j5 + k4 * i4, byte0 + 9 * k3, 0x80000000);

            for (int l6 = 0; l6 < i3; l6++)
            {
                int k7 = j5 + (l6 % i4) * k4;
                int j8 = byte0 + (l6 / i4) * 9;
                drawRect(k7, j8, (k7 + k4) - 1, j8 + 8, 0x20ffffff);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_ALPHA_TEST);

                if (l6 >= list.size())
                {
                    continue;
                }

                GuiPlayerInfo guiplayerinfo = (GuiPlayerInfo)list.get(l6);
                fontrenderer.drawStringWithShadow(guiplayerinfo.name, k7, j8, 0xffffff);
                mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/icons.png"));
                int i10 = 0;
                byte byte1 = 0;

                if (guiplayerinfo.responseTime < 0)
                {
                    byte1 = 5;
                }
                else if (guiplayerinfo.responseTime < 150)
                {
                    byte1 = 0;
                }
                else if (guiplayerinfo.responseTime < 300)
                {
                    byte1 = 1;
                }
                else if (guiplayerinfo.responseTime < 600)
                {
                    byte1 = 2;
                }
                else if (guiplayerinfo.responseTime < 1000)
                {
                    byte1 = 3;
                }
                else
                {
                    byte1 = 4;
                }

                zLevel += 100F;
                drawTexturedModalRect((k7 + k4) - 12, j8, 0 + i10 * 10, 176 + byte1 * 8, 10, 8);
                zLevel -= 100F;
            }
        }
        
        renderSpellLoadingBar();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
    }

    /**
     * Renders dragon's (boss) health on the HUD
     */
    private void renderBossHealth()
    {
        if (RenderDragon.entityDragon == null)
        {
            return;
        }

        EntityDragon entitydragon = RenderDragon.entityDragon;
        RenderDragon.entityDragon = null;
        FontRenderer fontrenderer = mc.fontRenderer;
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int i = scaledresolution.getScaledWidth();
        char c = '\266';
        int j = i / 2 - c / 2;
        int k = (int)(((float)entitydragon.getDragonHealth() / (float)entitydragon.getMaxHealth()) * (float)(c + 1));
        byte byte0 = 12;
        drawTexturedModalRect(j, byte0, 0, 74, c, 5);
        drawTexturedModalRect(j, byte0, 0, 74, c, 5);

        if (k > 0)
        {
            drawTexturedModalRect(j, byte0, 0, 79, k, 5);
        }

        String s = "Boss health";
        fontrenderer.drawStringWithShadow(s, i / 2 - fontrenderer.getStringWidth(s) / 2, byte0 - 10, 0xff00ff);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/icons.png"));
    }

    private void renderPumpkinBlur(int par1, int par2)
    {
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("%blur%/misc/pumpkinblur.png"));
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0D, par2, -90D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(par1, par2, -90D, 1.0D, 1.0D);
        tessellator.addVertexWithUV(par1, 0.0D, -90D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(0.0D, 0.0D, -90D, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Renders the vignette. Args: vignetteBrightness, width, height
     */
    private void renderVignette(float par1, int par2, int par3)
    {
        par1 = 1.0F - par1;

        if (par1 < 0.0F)
        {
            par1 = 0.0F;
        }

        if (par1 > 1.0F)
        {
            par1 = 1.0F;
        }

        prevVignetteBrightness += (double)(par1 - prevVignetteBrightness) * 0.01D;
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_ZERO, GL11.GL_ONE_MINUS_SRC_COLOR);
        GL11.glColor4f(prevVignetteBrightness, prevVignetteBrightness, prevVignetteBrightness, 1.0F);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("%blur%/misc/vignette.png"));
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0D, par3, -90D, 0.0D, 1.0D);
        tessellator.addVertexWithUV(par2, par3, -90D, 1.0D, 1.0D);
        tessellator.addVertexWithUV(par2, 0.0D, -90D, 1.0D, 0.0D);
        tessellator.addVertexWithUV(0.0D, 0.0D, -90D, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * Renders the portal overlay. Args: portalStrength, width, height
     */
    private void renderPortalOverlay(float par1, int par2, int par3)
    {
        if (par1 < 1.0F)
        {
            par1 *= par1;
            par1 *= par1;
            par1 = par1 * 0.8F + 0.2F;
        }

        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, par1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/terrain.png"));
        float f = (float)(Block.portal.blockIndexInTexture % 16) / 16F;
        float f1 = (float)(Block.portal.blockIndexInTexture / 16) / 16F;
        float f2 = (float)(Block.portal.blockIndexInTexture % 16 + 1) / 16F;
        float f3 = (float)(Block.portal.blockIndexInTexture / 16 + 1) / 16F;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0D, par3, -90D, f, f3);
        tessellator.addVertexWithUV(par2, par3, -90D, f2, f3);
        tessellator.addVertexWithUV(par2, 0.0D, -90D, f2, f1);
        tessellator.addVertexWithUV(0.0D, 0.0D, -90D, f, f1);
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Renders the specified item of the inventory slot at the specified location. Args: slot, x, y, partialTick
     */
    private void renderInventorySlot(int par1, int par2, int par3, float par4)
    {
        ItemStack itemstack = mc.thePlayer.inventory.mainInventory[par1];

        if (itemstack == null)
        {
            return;
        }

        float f = (float)itemstack.animationsToGo - par4;

        if (f > 0.0F)
        {
            GL11.glPushMatrix();
            float f1 = 1.0F + f / 5F;
            GL11.glTranslatef(par2 + 8, par3 + 12, 0.0F);
            GL11.glScalef(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
            GL11.glTranslatef(-(par2 + 8), -(par3 + 12), 0.0F);
        }

        itemRenderer.renderItemIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, par2, par3);

        if (f > 0.0F)
        {
            GL11.glPopMatrix();
        }

        itemRenderer.renderItemOverlayIntoGUI(mc.fontRenderer, mc.renderEngine, itemstack, par2, par3);
    }

    /**
     * The update tick for the ingame UI
     */
    public void updateTick()
    {
        if (recordPlayingUpFor > 0)
        {
            recordPlayingUpFor--;
        }

        updateCounter++;
    }

    public void setRecordPlayingMessage(String par1Str)
    {
        recordPlaying = (new StringBuilder()).append("Now playing: ").append(par1Str).toString();
        recordPlayingUpFor = 60;
        recordIsPlaying = true;
    }

    public GuiNewChat func_73827_b()
    {
        return field_73840_e;
    }

    public int func_73834_c()
    {
        return updateCounter;
    }
    
    /** doubi */
    
    public void renderManaBar()
    {
    	ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
    	int i = scaledresolution.getScaledWidth();
    	int j = scaledresolution.getScaledHeight();
    	InventoryPlayer inventoryplayer = mc.thePlayer.inventory;
    	SpellInventory spells = mc.thePlayer.spells;
        drawTexturedModalRect(i - spellBarPos, j/2 - 90, 235, 21, 21, 181);
        if(spellBarPos > 0)
        	drawTexturedModalRect(i - 1 - spellBarPos, j/2 - 92 + 20*spells.currentSpell, 0, 22, 24, 24);
        drawTexturedModalRect(i - 18 - spellBarPos, j/2 - 92, 223, 19, 13, 185);
    	int manaH = 185*mc.thePlayer.mana/mc.thePlayer.maxMana;
        drawTexturedModalRect(i - 18 - spellBarPos, j/2 - 92 + 185 - manaH, 210, 19 + 185 - manaH, 13, manaH);
        drawTexturedModalRect(i / 2 - 91, j - 22, 0, 0, 182, 22);
    }
    
    public void updateSpellBar()
    {
    	if(selectSpell || allwaysShowSpells)
    		spellBarPos+=2;
    	else
    		spellBarPos-=2;
    	
    	spellBarPos = Math.min(22, Math.max(0, spellBarPos));
    }
    
    private void renderSpellSlot(int id, int x, int y, float time)
    {
    	GL11.glDisable(GL11.GL_LIGHTING);
        Spell spell = mc.thePlayer.spells.getEquipedSpell(id);
        if(spell == null)
        	return;
        mc.renderEngine.bindTexture(mc.renderEngine.getTexture("/gui/spells.png"));
        itemRenderer.renderTexturedQuad(x, y, (spell.getIcon() % 16) * 16, (spell.getIcon() / 16) * 16, 16, 16);
        if(!spell.reloaded())
        {
        	int h = (int) (spell.reloadRatio()*16);
        	if(spell.enoughMana(mc.thePlayer))
        		drawRect(x, y + h, x+16, y+16, 0x88ffcc00);
        	else
        		drawRect(x, y + h, x+16, y+16, 0x55ff0000);
        }
        else if(!spell.enoughMana(mc.thePlayer))
        	drawRect(x, y, x+16, y+16, 0x55ff0000);
    	GL11.glEnable(GL11.GL_LIGHTING);
    } 
    
    private void renderSpellLoadingBar()
    {
    	if(mc.thePlayer.getSelectedSpell() == null || mc.thePlayer.getSelectedSpell().getLoadTime() <= 5)
    		return;
    	ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
    	int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/gui/gui.png"));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
        float ratio = ((float)mc.thePlayer.spellCharge)/((float)mc.thePlayer.getSelectedSpell().getLoadTime());
        if(ratio != 0)
        	drawTexturedModalRect(i / 2 - 10, j/2 - 10, 20*(int)(Math.max(0, Math.min(9, ratio*10))), 146, 20, 19);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }
    
    private void drawTargetGui(int x, int y, EntityLiving target)
    {
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    	ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
		int width = scaledresolution.getScaledWidth();
		int height = scaledresolution.getScaledHeight();
		FontRenderer fontrenderer = mc.fontRenderer;
		
		if (!mc.gameSettings.showDebugInfo)
		{
			fontrenderer.drawStringWithShadow("Cartasiane Client "+GuiMainMenu.cartaState+" "+GuiMainMenu.cartaVer, width - 162, 2, 0xffffff);
			fontrenderer.drawStringWithShadow("Work in progress.", width - 92, 12, 0xffffff);
		}
		
		drawRect(x, y, x+150, y+60, 0x80000000);
		drawRect(x, y, x+149, y+59, 0x20ffffff);
		drawRect(x+5, y+5, x+53, y+53, 0x20000000);
		drawRect(x+59, y+16, x+143, y+17, 0x50000000);
		fontrenderer.drawString(target.getName(), x+60, y+6, 0xffEBA000);
		fontrenderer.drawString(target.getBreed() + ", niv. \247e"+target.getLevel(), x+60, y+20, 0xffffff);
		int w = target.health*80/target.getMaxHealth();
		if(target.getHealth() > 0)
		{
			drawRect(x+59, y+31, x+59+w, y+41, 0xaa00FF00);
			fontrenderer.drawString("Vie: "+target.getHealth()+"/"+target.getMaxHealth(), x+61, y+32, 0xffffff);
		}
		else
		{
			fontrenderer.drawString("Vie: "+target.getHealth() + "/"+target.getMaxHealth(), x+61, y+32, 0xffffff);
		}
		if(target instanceof EntityPlayer)
		{
			w = ((EntityPlayer)target).mana*80/((EntityPlayer)target).maxMana;
			drawRect(x+59, y+43, x+59 + w, y+53, 0xaaBD00DA);
			fontrenderer.drawString("Mana: "+((EntityPlayer)target).mana+"/"+((EntityPlayer)target).maxMana, x+61, y+44, 0xffffff);
		}
		float f = 4.5f;
		GL11.glPushMatrix();

		GL11.glTranslatef(x, y, 0);
		
		GL11.glScalef(f, f, f);
		fontrenderer.drawString("\2474?", 4, 3, 0xffffff);
		
		GL11.glPopMatrix();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		GL11.glEnable(GL11.GL_BLEND);
	}
}
