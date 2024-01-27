public class Settings {
    private static byte gameBoardSize;
    private static boolean isNeedWrite;
    private static boolean useAllThreads;

    public static int getGameBoardSize() {
        return gameBoardSize;
    }

    public static void setGameBoardSize(byte gameBoardSize) {
        Settings.gameBoardSize = gameBoardSize;
    }

    public static boolean getNeedWrite() {
        return isNeedWrite;
    }

    public static void setNeedWrite(boolean isNeedWrite) {
        Settings.isNeedWrite = isNeedWrite;
    }

    public static boolean isUseAllThreads() {
        return useAllThreads;
    }

    public static void setUseAllThreads(boolean useAllThreads) {
        Settings.useAllThreads = useAllThreads;
    }
}
