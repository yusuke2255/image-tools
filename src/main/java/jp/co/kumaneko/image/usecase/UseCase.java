package jp.co.kumaneko.image.usecase;

public interface UseCase<I, O> {
    O execute(I input) throws Exception;
}
