package com.skyywastaken.arcadelb.stats.load;

import com.google.gson.Gson;
import com.skyywastaken.arcadelb.ArcadeLB;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

public class StatTypeLoader {
    private final File statSaveDirectory;
    private FileSystem jarFileSystem = null;

    private boolean loadingFromJar = false;

    public StatTypeLoader() {
        this.statSaveDirectory = new File(ArcadeLB.configDirectory.getPath(), "stats");
        if (!this.statSaveDirectory.exists() && !this.statSaveDirectory.mkdirs()) {
            ArcadeLB.getLogger().warn("Couldn't create the stats directory! Loading games from jar.");
            this.loadingFromJar = true;
        }
        loadStats();
    }

    public ArrayList<StatTypeHolder> loadStats() {
        if (this.loadingFromJar) {
            return loadStatsFromJar();
        } else {
            copyStatsToDrive();
            return loadStatsFromDisk();
        }
    }

    private ArrayList<StatTypeHolder> loadStatsFromJar() {
        ArrayList<StatTypeHolder> returnStats = new ArrayList<>();
        for (String currentFileName : getJarStatTypeFileNames()) {
            URL currentFileURL = getClass().getResource(currentFileName);
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
            returnStats.add(statTypeHolder);
        }
        return returnStats;
    }

    private ArrayList<StatTypeHolder> loadStatsFromDisk() {
        String[] files = this.statSaveDirectory.list();
        if (files == null) {
            return new ArrayList<>();
        }
        ArrayList<StatTypeHolder> returnStats = new ArrayList<>();
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
            returnStats.add(statTypeHolder);
        }
        return returnStats;
    }

    private void copyStatsToDrive() {
        List<String> statsOnDriveList = getStatsOnDrive();
        List<String> statsInJar = getJarStatTypeFileNames();
        for (String currentString : statsInJar) {
            String fileName = currentString.substring(currentString.lastIndexOf("/") + 1);
            if (!statsOnDriveList.contains(fileName)) {
                InputStream resourceStream = getClass().getResourceAsStream(currentString);
                if (resourceStream == null) {
                    continue;
                }
                try {
                    File outputFile = new File(this.statSaveDirectory, fileName);
                    Files.copy(resourceStream, outputFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private List<String> getJarStatTypeFileNames() {
        String resourceLocation = "/assets/arcadelb/stats/";
        URI uri = null;
        try {
            uri = Objects.requireNonNull(getClass().getResource(resourceLocation)).toURI();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        Path resourceFolder;
        assert uri != null;
        if (uri.getScheme().equals("jar")) {
            if (jarFileSystem == null) {
                try {
                    this.jarFileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            resourceFolder = this.jarFileSystem.getPath(resourceLocation);
        } else {
            resourceFolder = Paths.get(uri);
        }
        Stream<Path> walk = null;
        try {
            walk = Files.walk(resourceFolder, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> resourcePaths = new ArrayList<>();
        for (Iterator<Path> it = walk.iterator(); it.hasNext(); ) {
            String path = it.next().toString();
            if (path.contains(".")) {
                resourcePaths.add(resourceLocation + path.substring(path.lastIndexOf("/") + 1));
            }
        }
        return resourcePaths;
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
