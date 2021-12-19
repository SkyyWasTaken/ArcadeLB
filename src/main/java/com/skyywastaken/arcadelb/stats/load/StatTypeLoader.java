package com.skyywastaken.arcadelb.stats.load;

import com.google.gson.Gson;
import com.skyywastaken.arcadelb.ArcadeLB;
import com.skyywastaken.arcadelb.stats.game.StatTypeHelper;
import org.apache.commons.io.FileUtils;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class StatTypeLoader {
    private final File statSaveDirectory;
    private final StatTypeHelper statTypeHelper;

    private boolean loadingFromJar = false;

    public StatTypeLoader(StatTypeHelper passedHelper) {
        this.statTypeHelper = passedHelper;
        this.statSaveDirectory = new File(ArcadeLB.configDirectory.getPath(), "stats");
        if (!this.statSaveDirectory.exists() && !this.statSaveDirectory.mkdirs()) {
            ArcadeLB.getLogger().warn("Couldn't create the stats directory! Loading games from jar.");
            this.loadingFromJar = true;
        }
    }

    public void loadStats() {
        if (this.loadingFromJar) {
            loadStatsFromJar();
        } else {
            copyStatsToDrive();
            loadStatsFromDisk();
        }
    }

    private void loadStatsFromJar() {
        for (String currentFileName : getJarStatTypeFileNames()) {
            URL currentFileURL = getClass().getResource("/" + currentFileName);
            if (currentFileURL == null) {
                continue;
            }
            Gson gson = new Gson();
            InputStream currentFileStream;
            try {
                currentFileStream = currentFileURL.openStream();
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
            StatTypeHolder statTypeHolder = gson.fromJson(new InputStreamReader(currentFileStream), StatTypeHolder.class);
            this.statTypeHelper.registerStats(statTypeHolder);
        }
    }

    private void loadStatsFromDisk() {
        String[] files = this.statSaveDirectory.list();
        if (files == null) {
            return;
        }
        Gson gson = new Gson();
        for (String fileName : files) {
            File currentFile = new File(this.statSaveDirectory, fileName);
            StatTypeHolder statTypeHolder;
            try {
                statTypeHolder = gson.fromJson(new FileReader(currentFile), StatTypeHolder.class);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                continue;
            }
            this.statTypeHelper.registerStats(statTypeHolder);
        }
    }

    private void copyStatsToDrive() {
        List<String> statsOnDriveList = getStatsOnDrive();
        Set<String> statsInJar = getJarStatTypeFileNames();
        for (String currentString : statsInJar) {
            String fileName = currentString.substring(currentString.lastIndexOf("/") + 1);
            if (!statsOnDriveList.contains(fileName)) {
                URL jarStatLocation = getClass().getResource("/" + currentString);
                if (jarStatLocation == null) {
                    continue;
                }
                try {
                    FileUtils.copyURLToFile(jarStatLocation, new File(this.statSaveDirectory, fileName));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Set<String> getJarStatTypeFileNames() {
        String REFLECTIONS_STAT_TYPE_LOCATION = "assets.arcadelb.stats";
        Reflections reflections = new Reflections(REFLECTIONS_STAT_TYPE_LOCATION, Scanners.Resources);
        return reflections.getResources(".*\\.json");
    }

    private List<String> getStatsOnDrive() {
        String[] statsOnDrive = this.statSaveDirectory.list();
        if (statsOnDrive == null) {
            return new ArrayList<>();
        } else {
            return Arrays.asList(statsOnDrive);
        }
    }
}
