package com.skyywastaken.arcadelb;

import com.skyywastaken.arcadelb.command.ArcadeLBCommand;
import com.skyywastaken.arcadelb.stats.ArcadeLeaderboard;
import com.skyywastaken.arcadelb.stats.game.PartyGames;
import com.skyywastaken.arcadelb.stats.statupdater.LeaderboardUpdateHelper;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Mod(modid = ArcadeLB.MODID, version = ArcadeLB.VERSION)
public class ArcadeLB {

    public static final String MODID = "arcadelb";
    public static final String VERSION = "0.0.1 INDEV";
    public static Configuration configuration;
    private static Logger logger;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent e) {
        logger = e.getModLog();
        configuration = new Configuration(e.getSuggestedConfigurationFile());
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        final ArcadeLeaderboard arcadeLeaderboard = new ArcadeLeaderboard();
        final Thread getStartingLeaderboard = new Thread(() -> arcadeLeaderboard.setLeaderboardFromVenomJson(PartyGames.WINS));
        getStartingLeaderboard.start();
        MinecraftForge.EVENT_BUS.register(new EventThing(arcadeLeaderboard));
        ClientCommandHandler.instance.registerCommand(new ArcadeLBCommand(arcadeLeaderboard));
    }

    public static org.apache.logging.log4j.Logger getLogger() {
        return logger;
    }
}
