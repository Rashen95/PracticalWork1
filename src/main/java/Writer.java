import java.io.FileWriter;
import java.io.IOException;

public class Writer {
    public static void writingTheDecision(byte[][] gameBoard, long count) {
        try {
            FileWriter writer = new FileWriter(String.format("Решения для поля %sx%s", Settings.getGameBoardSize(), Settings.getGameBoardSize()), true);
            writer.write(count + " Вариант\n");
            for (byte[] bytes : gameBoard) {
                for (byte b : bytes) {
                    if (b == 1) {
                        writer.write("[X]");
                    } else {
                        writer.write("[ ]");
                    }
                }
                writer.write("\n");
            }
            writer.write("\n");
            writer.close();
        } catch (IOException ignored) {

        }
    }
}
