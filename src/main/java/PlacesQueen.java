import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class PlacesQueen {
    public static AtomicLong count = new AtomicLong();
    public static Set<byte[][]> gameBoards = Collections.synchronizedSet(new HashSet<>());
    private final byte gameBoardSize;
    private final byte lowLimitForZeroColumn;
    private final byte upLimitForZeroColumn;

    public PlacesQueen(byte gameBoardSize, byte lowLimitForZeroColumn, byte upLimitForZeroColumn) {
        this.gameBoardSize = gameBoardSize;
        this.lowLimitForZeroColumn = lowLimitForZeroColumn;
        this.upLimitForZeroColumn = upLimitForZeroColumn;
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
    public void tryToPlace(byte[][] gameBoard, byte column) throws InterruptedException {
        if (column == gameBoard.length) {
            byte[][] copiedGameBoard = new byte[gameBoardSize][gameBoardSize];
            for (int i = 0; i < gameBoardSize; i++) {
                System.arraycopy(gameBoard[i], 0, copiedGameBoard[i], 0, gameBoardSize);
            }
            for (int i = 0; i < gameBoardSize; i++) {
                for (int j = 0; j < gameBoardSize; j++) {
                    System.out.print(copiedGameBoard[i][j]);
                }
                System.out.println();
            }
            System.out.println();
            count.getAndIncrement();
            gameBoards.add(copiedGameBoard);
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