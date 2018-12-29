package jp.co.kumaneko.image;

import jp.co.kumaneko.image.usecase.ResizeInputPort;

import java.io.File;

public class ResizeOrder implements ResizeInputPort {
    private final File target;
    private final File destination;
    private final int size;

    public ResizeOrder(File target, File destination, int size) {
        this.target = target;
        this.destination = destination;
        this.size = size;
    }

    @Override
    public File getTarget() {
        return target;
    }

    @Override
    public File getDestination() {
        return destination;
    }

    @Override
    public int getSize() {
        return size;
    }
}
