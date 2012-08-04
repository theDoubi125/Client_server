package net.minecraft.src;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.server.MinecraftServer;

public class CommandServerBanIp extends CommandBase
{
    public static final Pattern field_71545_a = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    public String getCommandName()
    {
        return "ban-ip";
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return MinecraftServer.getServer().func_71203_ab().func_72363_f().func_73710_b() && super.canCommandSenderUseCommand(par1ICommandSender);
    }

    public String getCommandUsage(ICommandSender par1ICommandSender)
    {
        return par1ICommandSender.translateString("commands.banip.usage", new Object[0]);
    }

    public void processCommand(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        if (par2ArrayOfStr.length >= 1 && par2ArrayOfStr[0].length() > 1)
        {
            Matcher var3 = field_71545_a.matcher(par2ArrayOfStr[0]);
            String var4 = null;

            if (par2ArrayOfStr.length >= 2)
            {
                var4 = joinString(par2ArrayOfStr, 1);
            }

            if (var3.matches())
            {
                this.func_71544_a(par1ICommandSender, par2ArrayOfStr[0], var4);
            }
            else
            {
                EntityPlayerMP var5 = MinecraftServer.getServer().func_71203_ab().getPlayerEntity(par2ArrayOfStr[0]);

                if (var5 == null)
                {
                    throw new PlayerNotFoundException("commands.banip.invalid", new Object[0]);
                }

                this.func_71544_a(par1ICommandSender, var5.func_71114_r(), var4);
            }
        }
        else
        {
            throw new WrongUsageException("commands.banip.usage", new Object[0]);
        }
    }

    /**
     * Adds the strings available in this command to the given list of tab completion options.
     */
    public List addTabCompletionOptions(ICommandSender par1ICommandSender, String[] par2ArrayOfStr)
    {
        return par2ArrayOfStr.length == 1 ? getListOfStringsMatchingLastWord(par2ArrayOfStr, MinecraftServer.getServer().getPlayerNamesAsList()) : null;
    }

    protected void func_71544_a(ICommandSender par1ICommandSender, String par2Str, String par3Str)
    {
        BanEntry var4 = new BanEntry(par2Str);
        var4.func_73687_a(par1ICommandSender.getCommandSenderName());

        if (par3Str != null)
        {
            var4.func_73689_b(par3Str);
        }

        MinecraftServer.getServer().func_71203_ab().func_72363_f().func_73706_a(var4);
        List var5 = MinecraftServer.getServer().func_71203_ab().func_72382_j(par2Str);
        String[] var6 = new String[var5.size()];
        int var7 = 0;
        EntityPlayerMP var9;

        for (Iterator var8 = var5.iterator(); var8.hasNext(); var6[var7++] = var9.getEntityName())
        {
            var9 = (EntityPlayerMP)var8.next();
            var9.playerNetServerHandler.kickPlayer("You have been IP banned.");
        }

        if (var5.isEmpty())
        {
            func_71522_a(par1ICommandSender, "commands.banip.success", new Object[] {par2Str});
        }
        else
        {
            func_71522_a(par1ICommandSender, "commands.banip.success.players", new Object[] {par2Str, joinNiceString(var6)});
        }
    }
}
