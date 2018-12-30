package jp.co.kumaneko.image;

class Executor {
    void execute(String[] args) throws IllegalArgumentException {
        final String type = args[0];

        switch (type) {
            case "resize":
                new ResizeExecutor().execute(args);
            case "optimize":
                new OptimizeExecutor().execute(args);
        }
    }
}
