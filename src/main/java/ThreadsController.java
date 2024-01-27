import java.util.ArrayList;
import java.util.List;

public class ThreadsController {
    private ThreadCountListener listener;
    private final List<Thread> ALL_THREADS = new ArrayList<>();
    private byte queensPerProcessor;
    private byte remainder;
    private byte lowLimitForZeroColumn = 0;
    private byte upLimitForZeroColumn = 0;
    private final Object monitor = new Object();

    public void start() throws InterruptedException {
        listener = new ThreadCountListener("listener1", (short) 1);
        if (Settings.isUseAllThreads()) {
            MultiThreadingRun();
        } else {
            singleThreadingRun();
        }
        System.out.printf("%s всего вариантов\n", PlacesQueen.count);
        System.out.printf("%s вариантов закинуто в HashSet", PlacesQueen.gameBoards.size());
    }

    public void singleThreadingRun() throws InterruptedException {
        Thread oneThread = new ThreadForPlacesQueen(
                String.valueOf(1),
                new PlacesQueen(
                        (byte) Settings.getGameBoardSize(),
                        lowLimitForZeroColumn,
                        (byte) Settings.getGameBoardSize(),
                        monitor));
        listener.start();
        oneThread.start();
        oneThread.join();
        listener.disable();
    }

    public void MultiThreadingRun() throws InterruptedException {
        // Распределяем нагрузку на потоки исходя из количества колонок
        byte processors = (byte) Runtime.getRuntime().availableProcessors();

        if (processors > Settings.getGameBoardSize()) {
            queensPerProcessor = 1;
            remainder = 0;
            upLimitForZeroColumn = queensPerProcessor;
            // Запускаем слушателя
            listener.start();
            for (byte i = 0; i < Settings.getGameBoardSize(); i++) {
                createThreads(i);
            }
        } else {
            queensPerProcessor = (byte) (Settings.getGameBoardSize() / processors);
            remainder = (byte) (Settings.getGameBoardSize() % processors);
            if (remainder > 0) {
                upLimitForZeroColumn = (byte) (queensPerProcessor + 1);
                remainder--;
            } else {
                upLimitForZeroColumn = queensPerProcessor;
            }
            listener.start();
            for (byte i = 0; i < processors; i++) {
                createThreads(i);
            }
        }

        for (Thread t : ALL_THREADS) {
            t.join();
        }

        //Выключаем слушателя
        listener.disable();
    }

    private void createThreads(byte nameThread) {
        Thread newThread = new ThreadForPlacesQueen(
                String.valueOf(nameThread),
                new PlacesQueen(
                        (byte) Settings.getGameBoardSize(),
                        lowLimitForZeroColumn,
                        upLimitForZeroColumn,
                        monitor));
        newThread.start();
        ALL_THREADS.add(newThread);
        lowLimitForZeroColumn = upLimitForZeroColumn;
        if (remainder > 0) {
            upLimitForZeroColumn = (byte) (upLimitForZeroColumn + queensPerProcessor + 1);
            remainder--;
        } else {
            upLimitForZeroColumn += queensPerProcessor;
        }
    }
}