package makamys.mixingasm;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.launchwrapper.Launch;

public class DefaultConfigHelper {

    private final String MODID;
    private final Logger LOGGER;

    public DefaultConfigHelper(String modid) {
        this.MODID = modid;
        this.LOGGER = LogManager.getLogger(MODID);
    }

    public Path getDefaultConfigFilePath(Path relPath) throws IOException {
        String resourceRelPath = Paths.get("assets/" + MODID + "/default_config/").resolve(relPath).toString().replace('\\', '/');
        URL resourceURL = new Object() { }.getClass().getEnclosingClass().getClassLoader().getResource(resourceRelPath);

        switch(resourceURL.getProtocol()) {
            case "jar":
                String urlString = resourceURL.getPath();
                int lastExclamation = urlString.lastIndexOf('!');
                String newURLString = urlString.substring(0, lastExclamation);
                return FileSystems.newFileSystem(new File(URI.create(newURLString)).toPath(), null).getPath(resourceRelPath);
            case "file":
                return new File(URI.create(resourceURL.toString())).toPath();
            default:
                return null;
        }
    }

    private void copyDefaultConfigFile(Path src, Path dest) throws IOException {
        Files.createDirectories(getParentSafe(dest));
        LOGGER.debug("Copying " + src + " -> " + dest);
        Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
    }

    public boolean createDefaultConfigFileIfMissing(File destFile, boolean overwrite) {
        Path destConfigFolderPath = Paths.get(new File(Launch.minecraftHome, "config").getPath());
        Path destFilePath = Paths.get(destFile.getPath());

        Path destRelPath = destConfigFolderPath.relativize(destFilePath);

        if (destFilePath.startsWith(destConfigFolderPath)) {
            try {
                Path srcConfigPath = getDefaultConfigFilePath(destRelPath).toAbsolutePath();
                if(Files.isRegularFile(srcConfigPath)) {
                    if(!destFile.exists() || overwrite) {
                        copyDefaultConfigFile(srcConfigPath, destFile.toPath());
                    }
                } else if(Files.isDirectory(srcConfigPath)) {
                    Files.createDirectories(Paths.get(destFile.getPath()));
                    // create contents of directory as well
                    for(Path srcChildPath : Files.walk(srcConfigPath).toArray(Path[]::new)) {
                        Path destPath = destFile.toPath().resolve(srcConfigPath.relativize(srcChildPath).toString());
                        if(!srcChildPath.equals(srcConfigPath) && srcChildPath.startsWith(srcConfigPath)) {
                            if(!createDefaultConfigFileIfMissing(destPath.toFile(), overwrite)) {
                                return false;
                            }
                        }
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Failed to create default config file for " + destRelPath.toString() + ": " + e.getMessage());
                return false;
            }
        } else {
            LOGGER.debug("Invalid argument for creating default config file: " + destRelPath.toString()
                         + " (file is not in the config directory)");
            return false;
        }
        return true;
    }

    public Path getParentSafe(Path p) {
        if(p == null || p.getParent() == null) {
            return Paths.get("");
        } else {
            return p.getParent();
        }
    }
}