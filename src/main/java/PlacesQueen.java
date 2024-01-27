import java.util.ArrayDeque;
import java.util.Arrays;

public class PlacesQueen {
    private volatile static Long count = 0L;
    private volatile static boolean dequeFull = false;
    private static final ArrayDeque<byte[][]> gameBoards = new ArrayDeque<>();
    private final byte gameBoardSize;
    private final byte lowLimitForZeroColumn;
    private final byte upLimitForZeroColumn;
    private final Object monitor;

    public PlacesQueen(byte gameBoardSize, byte lowLimitForZeroColumn, byte upLimitForZeroColumn, Object monitor) {
        this.gameBoardSize = gameBoardSize;
        this.lowLimitForZeroColumn = lowLimitForZeroColumn;
        this.upLimitForZeroColumn = upLimitForZeroColumn;
        this.monitor = monitor;
    }

    public static Long getCount() {
        return count;
    }

    public static void setDequeFull(boolean dequeFull) {
        PlacesQueen.dequeFull = dequeFull;
    }

    public static ArrayDeque<byte[][]> getGameBoards() {
        return gameBoards;
    }

    /**
     * Метод создает квадратное пустое игровое поле размером gameBoardSize
     *
     * @return пустое игровое поле
     */
    public byte[][] createEmptyGameBoard() {
        byte[][] emptyGameBoard = new byte[gameBoardSize][gameBoardSize];
        for (byte[] b : emptyGameBoard) {
            Arrays.fill(b, (byte) 0);
        }
        return emptyGameBoard;
    }

    /**
     * Метод проверяет безопасно ли установить ферзя в текущую клетку
     *
     * @param gameBoard игровое поле
     * @param row       строка в, которую планируется установка ферзя
     * @param column    столбец в, который планируется установка ферзя
     * @return установка безопасна?
     */
    private boolean isSafe(byte[][] gameBoard, byte row, byte column) {
        for (byte i = 0; i < column; i++) {
            if (gameBoard[row][i] == 1) {
                return false;
            }
        }
        for (byte i = row, j = column; i >= 0 && j >= 0; i--, j--) {
            if (gameBoard[i][j] == 1) {
                return false;
            }
        }
        for (byte i = row, j = column; i < gameBoard.length && j >= 0; i++, j--) {
            if (gameBoard[i][j] == 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Рекурсивный метод для установки ферзей.
     *
     * @param gameBoard игровое поле
     * @param column    колонка для установки ферзя
     */
    public void tryToPlace(byte[][] gameBoard, byte column) {
        if (column == gameBoard.length) {
            byte[][] copiedGameBoard = new byte[gameBoardSize][gameBoardSize];
            for (byte i = 0; i < gameBoardSize; i++) {
                System.arraycopy(gameBoard[i], 0, copiedGameBoard[i], 0, gameBoardSize);
            }
            synchronized (monitor) {
                if (count - ThreadWriter.getCount() >= 3_000_000) {
                    dequeFull = true;
                    ThreadWriter.setEmergencySituation(true);
                    while (dequeFull) {
                        try {
                            monitor.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                count++;
                gameBoards.addLast(copiedGameBoard);
            }
            return;
        }
        if (column == 0) {
            // Первая колонка разделяется на потоки построчно, чтобы распределить нагрузку
            for (byte i = lowLimitForZeroColumn; i < upLimitForZeroColumn; i++) {
                if (isSafe(gameBoard, i, column)) {
                    gameBoard[i][column] = 1;
                    tryToPlace(gameBoard, (byte) (column + 1));
                    gameBoard[i][column] = 0;
                }
            }
        } else {
            for (byte i = 0; i < gameBoard.length; i++) {
                if (isSafe(gameBoard, i, column)) {
                    gameBoard[i][column] = 1;
                    tryToPlace(gameBoard, (byte) (column + 1));
                    gameBoard[i][column] = 0;
                }
            }
        }
    }
}