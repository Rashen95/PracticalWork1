import java.util.Scanner;
import java.util.regex.Pattern;

public class Menu {
    public void menu() {
        greetings();
        changeMultithreadingMode();
        changeGameBoardSize();
        changeWriteMode();
        ThreadsController tC = new ThreadsController();
        try {
            tC.run();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void greetings() {
        System.out.println("""
                Данная программа находит все возможные варианты расположения
                N королев на шахматном поле размером N x N,
                так чтобы они не били друг друга
                """);
    }

    private void changeMultithreadingMode() {
        System.out.print("""
                Вы хотите задействовать все потоки
                вашего процессора для решения этой задачи? (y/n):""");
        while (true) {
            Scanner scan = new Scanner(System.in);
            String change = scan.nextLine().trim().toLowerCase();
            if (change.equals("y")) {
                Settings.setUseAllThreads(true);
                System.out.println();
                break;
            } else if (change.equals("n")) {
                Settings.setUseAllThreads(false);
                System.out.println();
                break;
            } else {
                System.out.print("Вы ввели неверный ответ, попробуйте еще раз. Введите y или n:");
            }
        }
    }

    private void changeGameBoardSize() {
        System.out.print("""
                Введите число (от 1 до 100), которое будет являться размером поля
                N x N и количеством королев одновременно:""");
        while (true) {
            Scanner scan = new Scanner(System.in);
            int gameBoardSizeInt;
            String gameBoardSizeString = scan.nextLine().trim();
            if (Pattern.matches("-?\\d+", gameBoardSizeString)) {
                gameBoardSizeInt = Integer.parseInt(gameBoardSizeString);
                if (gameBoardSizeInt > 0 && gameBoardSizeInt <= 100) {
                    Settings.setGameBoardSize((byte) gameBoardSizeInt);
                    System.out.println();
                    break;
                } else {
                    System.out.print("Вы ввели неверное число, попробуйте еще раз. Введите число от 1 до 100:");
                }
            } else {
                System.out.print("Вы ввели не число, попробуйте еще раз. Введите число от 1 до 100:");
            }
        }
    }

    public void changeWriteMode() {
        System.out.print("""
                Я могу записать от 1 до 100000 найденных вариантов в файл.
                Введите 0, если запись не нужна или количество от 1 до 100000, если запись требуется.
                (учтите, что пока файл будет записываться время поиска будет замедлено из за затрат на запись)
                Сколько вариантов необходимо записать в файл? (0-100000):""");
        while (true) {
            Scanner scan = new Scanner(System.in);
            int needWrite;
            String gameBoardSizeString = scan.nextLine().trim();
            if (Pattern.matches("-?\\d+", gameBoardSizeString)) {
                needWrite = Integer.parseInt(gameBoardSizeString);
                if (needWrite == 0) {
                    Settings.setNeedWrite((byte) needWrite);
                    System.out.println();
                    break;
                } else if (needWrite > 0 && needWrite <= 100000) {
                    Settings.setNeedWrite((byte) needWrite);
                    System.out.println();
                    break;
                } else {
                    System.out.print("Вы ввели неверное число, попробуйте еще раз. Введите число от 0 до 100000:");
                }
            } else {
                System.out.print("Вы ввели не число, попробуйте еще раз. Введите число от 0 до 100000:");
            }
        }
    }
}