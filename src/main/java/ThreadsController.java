import java.util.ArrayList;
import java.util.List;

public class ThreadsController {
    private static final Object monitor = new Object();
    private final ThreadCountListener listener = new ThreadCountListener("listener", (short) 1);
    private final ThreadWriter writer = new ThreadWriter("writer");
    private final List<Thread> ALL_THREADS_FOR_PLACES_QUEEN = new ArrayList<>();
    private byte queensPerProcessor = 1;
    private byte remainder = 0;
    private byte lowLimitForZeroColumn = 0;
    private byte upLimitForZeroColumn = 0;

    public static Object getMonitor() {
        return monitor;
    }

    public void start() {
        if (Settings.isUseAllThreads()) {
            MultiThreadingRun();
        } else {
            singleThreadingRun();
        }
        System.out.printf("%s Работа приложения завершена\n", TimeGenerator.getCurrentTime());
    }

    private void singleThreadingRun() {
        Thread oneThread = new ThreadForPlacesQueen(
                String.valueOf(1),
                new PlacesQueen(
                        (byte) Settings.getGameBoardSize(),
                        lowLimitForZeroColumn,
                        (byte) Settings.getGameBoardSize(),
                        monitor));
        //Запуск listener и writer
        listener.start();
        if (Settings.getNeedWrite()) {
            writer.start();
        }

        //Запуск потока
        oneThread.start();
        try {
            oneThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        //Отключение listener и writer
        listener.disable();

        System.out.printf("%s %s всего вариантов существует\n", TimeGenerator.getCurrentTime(), PlacesQueen.getCount());
        System.out.printf("%s Дозаписываю файл\n", TimeGenerator.getCurrentTime());

        while (true) {
            if (ThreadWriter.getCount() == PlacesQueen.getCount()) {
                writer.disable();
                break;
            }
        }
    }

    private void MultiThreadingRun() {
        //Распределяем нагрузку на потоки исходя из количества колонок
        byte processors = (byte) Runtime.getRuntime().availableProcessors();

        //Если колонок меньше процессоров, то выделяем под каждую колонку по 1 процессору
        if (processors > Settings.getGameBoardSize()) {
            upLimitForZeroColumn = queensPerProcessor;

            //Запускаем listener и writer
            listener.start();
            if (Settings.getNeedWrite()) {
                writer.start();
            }

            //Запускаем все потоки
            for (byte i = 0; i < Settings.getGameBoardSize(); i++) {
                createThreads(i);
            }
        } else {
            /*
            Если колонок больше чем процессоров то выделяем поровну
            для каждого потока и остаток также распределяем
             */
            queensPerProcessor = (byte) (Settings.getGameBoardSize() / processors);
            remainder = (byte) (Settings.getGameBoardSize() % processors);
            if (remainder > 0) {
                upLimitForZeroColumn = (byte) (queensPerProcessor + 1);
                remainder--;
            } else {
                upLimitForZeroColumn = queensPerProcessor;
            }

            //Запускаем listener и writer
            listener.start();
            if (Settings.getNeedWrite()) {
                writer.start();
            }

            //Запускаем все процессоры
            for (byte i = 0; i < processors; i++) {
                createThreads(i);
            }
        }

        //Просим основной поток ждать завершения работы всех процессоров
        for (Thread t : ALL_THREADS_FOR_PLACES_QUEEN) {
            try {
                t.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //Выключаем слушателя
        listener.disable();

        System.out.printf("%s %s всего вариантов существует\n", TimeGenerator.getCurrentTime(), PlacesQueen.getCount());
        System.out.printf("%s Дозаписываю файл\n", TimeGenerator.getCurrentTime());

        while (true) {
            if (ThreadWriter.getCount() == PlacesQueen.getCount()) {
                writer.disable();
                break;
            }
        }
    }

    /**
     * Создает и запускает поток, а так же смещает лимиты
     * @param nameThread имя потока
     */
    private void createThreads(byte nameThread) {
        Thread newThread = new ThreadForPlacesQueen(
                String.valueOf(nameThread),
                new PlacesQueen(
                        (byte) Settings.getGameBoardSize(),
                        lowLimitForZeroColumn,
                        upLimitForZeroColumn,
                        monitor));
        newThread.start();
        ALL_THREADS_FOR_PLACES_QUEEN.add(newThread);
        lowLimitForZeroColumn = upLimitForZeroColumn;
        if (remainder > 0) {
            upLimitForZeroColumn = (byte) (upLimitForZeroColumn + queensPerProcessor + 1);
            remainder--;
        } else {
            upLimitForZeroColumn += queensPerProcessor;
        }
    }
}