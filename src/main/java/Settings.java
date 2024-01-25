public class Settings {
    private static byte gameBoardSize;
    private static int needWrite;
    private static boolean useAllThreads;

    public static int getGameBoardSize() {
        return gameBoardSize;
    }

    public static void setGameBoardSize(byte gameBoardSize) {
        Settings.gameBoardSize = gameBoardSize;
    }

    public static int getNeedWrite() {
        return needWrite;
    }

    public static void setNeedWrite(int needWrite) {
        Settings.needWrite = needWrite;
    }

    public static boolean isUseAllThreads() {
        return useAllThreads;
    }

    public static void setUseAllThreads(boolean useAllThreads) {
        Settings.useAllThreads = useAllThreads;
    }
}
