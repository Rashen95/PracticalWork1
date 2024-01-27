import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;

public class ThreadWriter extends Thread {
    private volatile boolean isActivate = true;
    private volatile static long count = 0L;
    private volatile static boolean emergencySituation = false;
    private boolean flag = true;

    public ThreadWriter(String name) {
        super(name);
    }

    public void disable() {
        isActivate = false;
    }

    public static long getCount() {
        return count;
    }

    public static boolean isEmergencySituation() {
        return emergencySituation;
    }

    public static void setEmergencySituation(boolean emergencySituation) {
        ThreadWriter.emergencySituation = emergencySituation;
    }

    @Override
    public void run() {
        while (isActivate) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            writeToFile(PlacesQueen.getGameBoards());
        }
    }

    public void writeToFile(ArrayDeque<byte[][]> gameBoards) {
        String fileName = String.format("Решения для поля %sx%s.txt", Settings.getGameBoardSize(), Settings.getGameBoardSize());
        try {
            FileWriter writer = new FileWriter(fileName, true);
            while (!gameBoards.isEmpty()) {
                if (emergencySituation && flag) {
                    System.out.printf("%s Найдено слишком много вариантов, поиск приостановлен до момента полного переноса в файл\n", TimeGenerator.getCurrentTime());
                    flag = false;
                }
                writer.write(String.format("[%s вариант]\n", ++count));
                byte[][] gameBoard = gameBoards.pollFirst();
                assert gameBoard != null;
                for (byte[] row : gameBoard) {
                    for (byte cell : row) {
                        if (cell == 1) {
                            writer.write("[X]");
                        } else {
                            writer.write("[ ]");
                        }
                    }
                    writer.write("\n");
                }
                writer.write("\n");
            }
            writer.close();
            if (emergencySituation) {
                System.out.printf("%s Все найденные варианты перенесены в файл, продолжаю поиск\n", TimeGenerator.getCurrentTime());
                PlacesQueen.setDequeFull(false);
                synchronized (ThreadsController.getMonitor()) {
                    ThreadsController.getMonitor().notifyAll();
                }
                emergencySituation = false;
                flag = true;
            }
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл");
        }
    }
}