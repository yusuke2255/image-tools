package jp.co.kumaneko.image.usecase;

import org.im4java.core.ConvertCmd;
import org.im4java.core.IMOperation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class Resize implements UseCase<ResizeInputPort, ResizeResult> {

    @Override
    public ResizeResult execute(ResizeInputPort input) throws Exception {
        final File convertedFile = input.getDestination();
        final File imageFile = input.getTarget();

        createNewFile(convertedFile);

        IMOperation op = new IMOperation();
        op.addImage(imageFile.getAbsolutePath());
        op.resize(input.getSize(), null);
        op.strip();
        op.addImage(convertedFile.getAbsolutePath());
        System.out.println(op.getCmdArgs());
        ConvertCmd convert = new ConvertCmd();
        convert.run(op);
        return ResizeResult.ok();
    }

    private void createNewFile(File convertedFile) throws IOException {
        final File parent = new File(convertedFile.getParent());
        if (!parent.exists()) {
            Files.createDirectories(parent.toPath());
            System.out.println(String.format("[INFO] New directory was created.[path=%s]", parent.getAbsolutePath()));
        }

        if (!convertedFile.exists()) {
            Files.createFile(convertedFile.toPath());
        }
    }

}
