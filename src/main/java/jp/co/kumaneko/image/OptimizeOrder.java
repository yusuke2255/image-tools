package jp.co.kumaneko.image;

import jp.co.kumaneko.image.usecase.OptimizeInputPort;

import java.io.File;

public class OptimizeOrder implements OptimizeInputPort {

    private final File targetFile;
    private final File destinationFile;

    OptimizeOrder(File targetFile, File destinationFile) {
        this.targetFile = targetFile;
        this.destinationFile = destinationFile;
    }

    @Override
    public File getTargetFile() {
        return targetFile;
    }

    @Override
    public File getDestinationFile() {
        return destinationFile;
    }
}
