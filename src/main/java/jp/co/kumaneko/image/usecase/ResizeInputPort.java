package jp.co.kumaneko.image.usecase;

import java.io.File;

public interface ResizeInputPort {
    File getTarget();
    File getDestination();
    int getSize();
}

