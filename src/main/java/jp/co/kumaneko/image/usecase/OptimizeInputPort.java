package jp.co.kumaneko.image.usecase;

import java.io.File;

public interface OptimizeInputPort {
    File getTargetFile();
    File getDestinationFile();
}
