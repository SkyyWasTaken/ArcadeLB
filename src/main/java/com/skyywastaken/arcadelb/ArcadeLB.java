package com.skyywastaken.arcadelb;

import com.skyywastaken.arcadelb.command.ArcadeLBCommand;
import com.skyywastaken.arcadelb.event.EventThing;
import com.skyywastaken.arcadelb.stats.ArcadeLeaderboard;
import com.skyywastaken.arcadelb.stats.game.StatTypeHelper;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;

@Mod(modid = ArcadeLB.MODID, version = ArcadeLB.VERSION, name = ArcadeLB.NAME)
public class ArcadeLB {
    public static final String NAME = "ArcadeLB";
    public static final String MODID = "arcadelb";
    public static final String VERSION = "1.4.3 ALPHA";
    public static Configuration configuration;
    private static Logger logger;
    public static File configDirectory;

    public static org.apache.logging.log4j.Logger getLogger() {
        return logger;
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        logger = e.getModLog();
        configDirectory = new File(e.getModConfigurationDirectory().getPath() + "/arcadelb");
        configuration = new Configuration(new File(configDirectory, "arcadelb.cfg"));
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        logger.info("Preparing to make lives easier");
        StatTypeHelper statTypeTracker = new StatTypeHelper();
        ArcadeLeaderboard arcadeLeaderboard = new ArcadeLeaderboard(statTypeTracker);
        Thread getStartingLeaderboard = new Thread(arcadeLeaderboard::loadLeaderboardFromConfig);
        getStartingLeaderboard.start();
        MinecraftForge.EVENT_BUS.register(new EventThing(arcadeLeaderboard));
        ClientCommandHandler.instance.registerCommand(new ArcadeLBCommand(arcadeLeaderboard, statTypeTracker));
    }
}
