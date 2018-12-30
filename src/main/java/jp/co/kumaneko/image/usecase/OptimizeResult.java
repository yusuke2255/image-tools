package jp.co.kumaneko.image.usecase;

import java.io.File;
import java.util.Optional;

public class OptimizeResult {
    private final Throwable exception;
    private final File target;

    private OptimizeResult(File target, Throwable exception) {
        this.exception = exception;
        this.target = target;
    }

    public static OptimizeResult ofSuccess(File target) {
        return new OptimizeResult(target, null);
    }

    public static OptimizeResult ofFailure(File target, Throwable e) {
        return new OptimizeResult(target, e);
    }

    public Optional<Throwable> getException() {
        return Optional.ofNullable(exception);
    }

    public File getTarget() {
        return target;
    }
}
