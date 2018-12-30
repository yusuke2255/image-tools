package jp.co.kumaneko.image.usecase;

import com.tinify.Source;
import com.tinify.Tinify;
import org.apache.commons.io.FileUtils;

public class Optimize implements UseCase<OptimizeInputPort, OptimizeResult> {

    public Optimize(String tinifyApiKey) {
        Tinify.setKey(tinifyApiKey);
    }

    @Override
    public OptimizeResult execute(OptimizeInputPort input) {
        System.out.println(String.format("[INFO] Start optimize.[target=%s, destination=%s]", input.getTargetFile().getAbsolutePath(), input.getDestinationFile().getAbsolutePath()));
        try {
            final Source tinified = Tinify.fromFile(input.getTargetFile().getAbsolutePath());
            FileUtils.writeByteArrayToFile(input.getDestinationFile(), tinified.toBuffer());
        } catch (Exception e) {
            return OptimizeResult.ofFailure(input.getTargetFile(), e);
        }
        return OptimizeResult.ofSuccess(input.getTargetFile());
    }
}
