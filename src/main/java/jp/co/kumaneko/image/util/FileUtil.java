package jp.co.kumaneko.image.util;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    public static List<File> extractFiles(File dir) throws IllegalArgumentException {
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException(String.format("This is not directory.[path=%s]", dir.getAbsolutePath()));
        }

        List<File> targetFiles = new ArrayList<>();
        try {
            Files.walk(Paths.get(dir.getAbsolutePath()))
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .forEach(targetFiles::add);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return targetFiles;
    }

    public static String toDestPath(File targetDir, File targetFile, File dest) {
        String relativePath = targetFile.getAbsolutePath().replaceFirst(targetDir.getAbsolutePath(), "");
        if (relativePath.startsWith("/")) {
            // /foo/bar/image.jpg => foo/bar/image.jpg
            relativePath = relativePath.replaceFirst("/", "");
        }

        return dest.getAbsolutePath().endsWith("/") ?
                dest.getAbsolutePath()+ relativePath :
                dest.getAbsolutePath()  + "/" + relativePath;
    }
}
