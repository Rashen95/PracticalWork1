public class ThreadForPlacesQueen extends Thread {
    private final byte boardSize;
    private final byte lowLimitForZeroColumn;
    private final byte upLimitForZeroColumn;

    public ThreadForPlacesQueen(String name, byte boardSize, byte lowLimitForZeroColumn, byte upLimitForZeroColumn) {
        super(name);
        this.boardSize = boardSize;
        this.lowLimitForZeroColumn = lowLimitForZeroColumn;
        this.upLimitForZeroColumn = upLimitForZeroColumn;
    }

    @Override
    public void run() {
        PlacesQueen pQ = new PlacesQueen(boardSize, lowLimitForZeroColumn, upLimitForZeroColumn);
        try {
            pQ.tryToPlace(pQ.createEmptyGameBoard(), (byte) 0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}