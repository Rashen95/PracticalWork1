import java.util.ArrayList;
import java.util.List;

public class ThreadsController {
    private ThreadCountListener listener;
    private final List<Thread> ALL_THREADS = new ArrayList<>();
    private byte queensPerProcessor;
    private byte remainder;
    private byte lowLimit;
    private byte upLimit;

    public void run() throws InterruptedException {
        listener = new ThreadCountListener("listener", (short) 1);
        if (Settings.isUseAllThreads()) {
            MultiThreadingRun();
        } else {
            singleThreadingRun();
        }
        System.out.printf("%s всего вариантов\n", PlacesQueen.count.get());
    }

    public void singleThreadingRun() throws InterruptedException {
        Thread oneThread = new ThreadForPlacesQueen(String.valueOf(1),
                (byte) Settings.getGameBoardSize(), (byte) 0, (byte) Settings.getGameBoardSize());
        listener.start();
        oneThread.start();
        oneThread.join();
        listener.disable();
    }

    public void MultiThreadingRun() throws InterruptedException {
        // Распределяем нагрузку на потоки исходя из количества колонок
        byte processors = (byte) Runtime.getRuntime().availableProcessors();
        queensPerProcessor = (byte) (Settings.getGameBoardSize() / processors);
        remainder = (byte) (Settings.getGameBoardSize() % processors);
        lowLimit = 0;
        if (remainder > 0) {
            upLimit = (byte) (queensPerProcessor + 1);
            remainder--;
        } else {
            upLimit = queensPerProcessor;
        }

        // Запускаем слушателя
        listener.start();

        // Запускаем потоки
        if (processors > Settings.getGameBoardSize()) {
            for (byte i = 0; i < Settings.getGameBoardSize(); i++) {
                createThreads(i);
            }
        } else {
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
        Thread newThread = new ThreadForPlacesQueen(String.valueOf(nameThread), (byte) Settings.getGameBoardSize(), lowLimit, upLimit);
        newThread.start();
        ALL_THREADS.add(newThread);
        lowLimit = upLimit;
        if (remainder > 0) {
            upLimit = (byte) (upLimit + queensPerProcessor + 1);
            remainder--;
        } else {
            upLimit += queensPerProcessor;
        }
    }
}