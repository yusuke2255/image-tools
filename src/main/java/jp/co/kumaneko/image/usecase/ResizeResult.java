package jp.co.kumaneko.image.usecase;

public class ResizeResult {
    private final int result;

    private ResizeResult(int result) {
        this.result = result;
    }

    public static ResizeResult ok() {
        return new ResizeResult(0);
    }

    public int getResult() {
        return result;
    }
}
