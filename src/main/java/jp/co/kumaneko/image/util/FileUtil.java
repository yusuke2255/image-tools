package jp.co.kumaneko.image.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

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

    public static Predicate<File> maxWidthBasedPredicate(int thresholdWidth) {
        return (file) -> {
            try {
                BufferedImage image = toImage(file);
                if (image == null) {
                    System.out.println(String.format("[WARN] This is not image file.[path=%s]", file.getAbsolutePath()));
                    return false;
                }
                return image.getWidth() > thresholdWidth;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    public static Predicate<File> maxSizeBasedPredicate(long threshold) {
        return (file) -> {
            try {
                BufferedImage image = toImage(file);
                if (image == null) {
                    System.out.println(String.format("[WARN] This is not image file.[path=%s]", file.getAbsolutePath()));
                    return false;
                }
                return file.length() > threshold;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }

    private static BufferedImage toImage(File file) throws IOException {
        try (FileInputStream in = new FileInputStream(file.getAbsoluteFile())) {
            return ImageIO.read(in);
        }
    }
}
