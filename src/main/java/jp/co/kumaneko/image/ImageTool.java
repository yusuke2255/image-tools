package jp.co.kumaneko.image;

public class ImageTool {
    public static void main(String[] args) {
        try {
            new Executor().execute(args);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
