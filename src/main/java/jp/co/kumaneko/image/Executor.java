package jp.co.kumaneko.image;

class Executor {
    void execute(String[] args) throws Exception {
        final String type = args[0];

        switch (type) {
            case "resize":
                new ResizeExecutor().execute(args);
                break;
            case "optimize":
                new OptimizeExecutor().execute(args);
                break;
        }
    }
}
