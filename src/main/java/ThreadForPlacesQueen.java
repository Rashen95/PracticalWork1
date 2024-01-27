public class ThreadForPlacesQueen extends Thread {
    private final PlacesQueen pQ;

    public ThreadForPlacesQueen(String name, PlacesQueen pQ) {
        super(name);
        this.pQ = pQ;
    }

    @Override
    public void run() {
        try {
            pQ.tryToPlace(pQ.createEmptyGameBoard(), (byte) 0);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}