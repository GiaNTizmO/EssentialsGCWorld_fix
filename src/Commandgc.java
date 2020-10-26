package com.earth2me.essentials.commands;

import com.earth2me.essentials.CommandSource;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.utils.DateUtil;
import com.earth2me.essentials.utils.NumberUtil;
import java.lang.management.ManagementFactory;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Server;
import org.bukkit.World;

public class Commandgc extends EssentialsCommand {
    public Commandgc() {
        super("gc");
    }

    protected void run(Server server, CommandSource sender, String commandLabel, String[] args) throws Exception {
        ChatColor color;
        double tps = this.ess.getTimer().getAverageTPS();
        if (tps >= 18.0D) {
            color = ChatColor.GREEN;
        } else if (tps >= 15.0D) {
            color = ChatColor.YELLOW;
        } else {
            color = ChatColor.RED;
        }
        sender.sendMessage(I18n.tl("uptime", new Object[] {
                DateUtil.formatDateDiff(ManagementFactory.getRuntimeMXBean().getStartTime())
        }));
        sender.sendMessage(I18n.tl("tps", new Object[] {
                "" + color + NumberUtil.formatDouble(tps)
        }));
        sender.sendMessage(I18n.tl("gcmax", new Object[] {
                Long.valueOf(Runtime.getRuntime().maxMemory() / 1024L / 1024L)
        }));
        sender.sendMessage(I18n.tl("gctotal", new Object[] {
                Long.valueOf(Runtime.getRuntime().totalMemory() / 1024L / 1024L)
        }));
        sender.sendMessage(I18n.tl("gcfree", new Object[] {
                Long.valueOf(Runtime.getRuntime().freeMemory() / 1024L / 1024L)
        }));
        List < World > worlds = server.getWorlds();
        for (World w: worlds) {
            if (Integer.valueOf((w.getLoadedChunks()).length) == 0) //IF world has 0 LoadedChunks - it will be skipped from for
                continue;
            String worldType = "World";
            switch (w.getEnvironment()) {
                case NETHER:
                    worldType = "Nether";
                    break;
                case THE_END:
                    worldType = "The End";
                    break;
                default:
                    break;
            }
            int tileEntities = 0;
            try {
                for (Chunk chunk: w.getLoadedChunks())
                    tileEntities += (chunk.getTileEntities()).length;
            } catch (ClassCastException ex) {
                Bukkit.getLogger().log(Level.SEVERE, "Corrupted chunk data on world " + w, ex);
            }
            sender.sendMessage(I18n.tl("gcWorld", new Object[] {
                    worldType,
                    w.getName(),
                    Integer.valueOf((w.getLoadedChunks()).length),
                    Integer.valueOf(w.getEntities().size()),
                    Integer.valueOf(tileEntities)
            }));
        }
    }
}
