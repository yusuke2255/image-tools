package jp.co.kumaneko.image;

import jp.co.kumaneko.image.usecase.*;
import jp.co.kumaneko.image.util.FileUtil;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class OptimizeExecutor {
    void execute(String[] args) throws Exception {
        final ExtractedOrders extractedOrders = extractOptimizeOrders(args);
        final UseCase<OptimizeInputPort, OptimizeResult> optimize = new Optimize(extractedOrders.getTinifyApiKey());

        extractedOrders.getOrders()
                .stream()
                .parallel()
                .forEach(in -> {
                    try {
                        final OptimizeResult result = optimize.execute(in);
                        result.getException().ifPresent(e -> {
                            e.printStackTrace();
                            System.err.println(String.format("Fail to optimize.[target=%s]", result.getTarget().getAbsolutePath()));
                        });
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private ExtractedOrders extractOptimizeOrders(String[] args) throws IllegalArgumentException, InterruptedException {
        String tinifyApiKey = null;
        boolean overwrite = false;
        File target = null;
        File dest = null;
        int threshold = 0;
        for (int i = 1; i < args.length; i++) {
            final String option = args[i];
            final String[] keyValue = option.split("=");
            final String key = keyValue[0];
            final String value = keyValue[1];
            switch (key) {
                case "tinify-api-key":
                    tinifyApiKey = value;
                    break;
                case "target":
                    target = new File(value);
                    break;
                case "dest":
                    dest = new File(value);
                    break;
                case "threshold":
                    threshold = Integer.parseInt(value);
                    break;
                case "overwrite":
                    overwrite = "yes".equals(value);
                    break;
                default:
                    System.out.println(String.format("Ignored illegal option [option=%s]", option));
            }
        }


        if (tinifyApiKey == null) {
            throw new IllegalArgumentException("Illegal option error.'tinify-api-key' is required.");
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

        final Predicate<File> isTarget = FileUtil.maxSizeBasedPredicate(threshold);
        if (target.isDirectory()) {
            Function<File, OptimizeOrder> toOrders = toOrders(target, dest);
            final List<File> targets = FileUtil.extractFiles(target)
                    .stream()
                    .parallel()
                    .filter(isTarget)
                    .collect(Collectors.toList());

            System.out.println(String.format("Target file count is %d", targets.size()));
            Thread.sleep(10000L);

            final Predicate<OptimizeOrder> removeOverwrite = removeOverwrite(overwrite);
            final List<OptimizeOrder> orders = targets
                    .stream()
                    .parallel()
                    .map(toOrders)
                    .filter(removeOverwrite)
                    .collect(Collectors.toList());

            return new ExtractedOrders(tinifyApiKey, orders);
        } else {
            if (isTarget.test(target)) {
                return new ExtractedOrders(tinifyApiKey, Collections.singletonList(new OptimizeOrder(target, dest)));
            }

            return new ExtractedOrders(tinifyApiKey, Collections.emptyList());
        }
    }

    private Function<File, OptimizeOrder> toOrders(File target, File dest) {
        return (f) -> {
            String convertedFilePath = FileUtil.toDestPath(target, f, dest);
            return new OptimizeOrder(f, new File(convertedFilePath));
        };
    }

    private Predicate<OptimizeOrder> removeOverwrite(boolean isOverwrite) {
        return (order) -> {
            if (isOverwrite) return true;

            if (order.getDestinationFile().exists()){
                System.out.println(String.format("This file is already existed.[dest=%s]", order.getDestinationFile().getAbsolutePath()));
                return false;
            }

            return true;
        };
    }

    private class ExtractedOrders {
        private final String tinifyApiKey;
        private final List<OptimizeOrder> orders;

        ExtractedOrders(String tinifyApiKey, List<OptimizeOrder> orders) {
            this.tinifyApiKey = tinifyApiKey;
            this.orders = orders;
        }

        String getTinifyApiKey() {
            return tinifyApiKey;
        }

        List<OptimizeOrder> getOrders() {
            return orders;
        }
    }
}
