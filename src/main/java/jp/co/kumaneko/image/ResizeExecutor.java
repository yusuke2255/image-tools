package jp.co.kumaneko.image;

import jp.co.kumaneko.image.usecase.Resize;
import jp.co.kumaneko.image.usecase.ResizeInputPort;
import jp.co.kumaneko.image.usecase.ResizeResult;
import jp.co.kumaneko.image.usecase.UseCase;
import jp.co.kumaneko.image.util.FileUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class ResizeExecutor {
    void execute(String[] args) throws IllegalArgumentException {
        final UseCase<ResizeInputPort, ResizeResult> resize = new Resize();
        extractResizeOrders(args).stream().parallel().forEach(in -> {
            try {
                resize.execute(in);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    private List<ResizeOrder> extractResizeOrders(String[] args) throws IllegalArgumentException {
        File target = null;
        File dest = null;
        int size = 0;
        for (int i = 1; i < args.length; i++) {
            final String option = args[i];
            final String[] keyValue = option.split("=");
            final String key = keyValue[0];
            final String value = keyValue[1];
            switch (key) {
                case "target":
                    target = new File(value);
                    break;
                case "dest":
                    dest = new File(value);
                    break;
                case "size":
                    size = Integer.parseInt(value);
                    break;
                default:
                    System.out.println(String.format("Ignored illegal option [option=%s]", option));
            }
        }

        if (target == null) {
            throw new IllegalArgumentException("Illegal option error.'target' is required.");
        }
        if (dest == null) {
            throw new IllegalArgumentException("Illegal option error.'dest' is required.");
        }
        if (!target.exists()) {
            throw new IllegalArgumentException(String.format("Illegal option error.'target' is not existed.[target=%s]", target.getAbsolutePath()));
        }

        final Predicate<File> isTarget = isTarget(size);
        if (target.isDirectory()) {
            Function<File, ResizeOrder> toOrders = toOrders(target, dest, size);
            return FileUtil.extractFiles(target)
                    .stream()
                    .parallel()
                    .filter(isTarget)
                    .map(toOrders)
                    .collect(Collectors.toList());
        } else {
            if (isTarget.test(target)) {
                return Collections.singletonList(new ResizeOrder(target, dest, size));
            }

            return Collections.emptyList();
        }
    }

    private Function<File, ResizeOrder> toOrders(File target, File dest, int size) {
        return (f) -> {
            String convertedFilePath = FileUtil.toDestPath(target, f, dest);
            return new ResizeOrder(f, new File(convertedFilePath), size);
        };
    }

    private static Predicate<File> isTarget(int thresholdWidth) {
        return (file) -> {
            try (FileInputStream in = new FileInputStream(file.getAbsoluteFile())) {
                BufferedImage image = ImageIO.read(in);
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
}
