package com.skyywastaken.arcadelb;

import com.skyywastaken.arcadelb.command.ArcadeLBCommand;
import com.skyywastaken.arcadelb.stats.ArcadeLeaderboard;
import com.skyywastaken.arcadelb.stats.game.StatTypeHelper;
import com.skyywastaken.arcadelb.util.thread.MessageHelper;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = ArcadeLB.MODID, version = ArcadeLB.VERSION)
public class ArcadeLB {

    public static final String MODID = "arcadelb";
    public static final String VERSION = "0.0.1 INDEV";
    public static Configuration configuration;
    private static Logger logger;

    public static org.apache.logging.log4j.Logger getLogger() {
        return logger;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        logger = e.getModLog();
        configuration = new Configuration(e.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MessageHelper.sendThreadSafeMessage(new ChatComponentText("lol"));
        StatTypeHelper statTypeTracker = new StatTypeHelper();
        ArcadeLeaderboard arcadeLeaderboard = new ArcadeLeaderboard(statTypeTracker);
        Thread getStartingLeaderboard = new Thread(arcadeLeaderboard::loadLeaderboardFromConfig);
        getStartingLeaderboard.start();
        MinecraftForge.EVENT_BUS.register(new EventThing(arcadeLeaderboard));
        ClientCommandHandler.instance.registerCommand(new ArcadeLBCommand(arcadeLeaderboard, statTypeTracker));
    }
}
