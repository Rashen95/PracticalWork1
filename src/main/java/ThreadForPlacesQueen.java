public class ThreadForPlacesQueen extends Thread {
    private final PlacesQueen pQ;

    public ThreadForPlacesQueen(String name, PlacesQueen pQ) {
        super(name);
        this.pQ = pQ;
    }

    @Override
    public void run() {
        pQ.tryToPlace(pQ.createEmptyGameBoard(), (byte) 0);
    }
}