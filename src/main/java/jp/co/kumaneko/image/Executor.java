package jp.co.kumaneko.image;

public class Executor {
    public void execute(String[] args) throws IllegalArgumentException {
        final String type = args[0];

        switch (type) {
            case "resize":
                new ResizeExecutor().execute(args);
        }
    }


}
