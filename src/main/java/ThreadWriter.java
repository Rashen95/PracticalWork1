import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

public class ThreadWriter extends Thread {
    private volatile boolean isActivate = true;

    public ThreadWriter(String name) {
        super(name);
    }

    public void disable() {
        isActivate = false;
    }

    @Override
    public void run() {
        while (isActivate) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (isActivate && PlacesQueen.hashSetIsFull) {
                writeToFile(PlacesQueen.gameBoards);
                PlacesQueen.hashSetIsFull = false;
                PlacesQueen.gameBoards.clear();
                synchronized (ThreadsController.monitor) {
                    ThreadsController.monitor.notifyAll();
                }
            }
        }
    }

    public void writeToFile(HashSet<byte[][]> gameBoards) {
        String fileName = String.format("Решения для поля %sx%s.txt", Settings.getGameBoardSize(), Settings.getGameBoardSize());
        try {
            FileWriter writer = new FileWriter(fileName);
            for (byte[][] gameBoard : gameBoards) {
                for (byte[] row : gameBoard) {
                    for (byte cell : row) {
                        if (cell == 1) {
                            writer.write("[X]");
                        }
                        else {
                            writer.write("[ ]");
                        }
                    }
                    writer.write("\n");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            System.out.println("Ошибка при записи в файл");
        }
    }
}
