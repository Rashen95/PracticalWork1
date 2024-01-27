import java.util.Scanner;
import java.util.regex.Pattern;

public class Menu {
    public void start() {
        greetings();
        changeMultithreadingMode();
        changeGameBoardSize();
        changeWriteMode();
        ThreadsController tC = new ThreadsController();
        try {
            tC.start();
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
                Я могу записать все найденные варианты в txt файл,
                но учтите, что у поля 12х12 уже будет 14200 вариантов, а дальше все больше в геометрической прогрессии
                Вы хотите записать все найденные результаты в файл? (y/n):""");
        while (true) {
            Scanner scan = new Scanner(System.in);
            String isNeedWrite = scan.nextLine().trim().toLowerCase();
            if (isNeedWrite.equals("y")) {
                Settings.setNeedWrite(true);
                System.out.println();
                break;
            } else if (isNeedWrite.equals("n")) {
                Settings.setNeedWrite(false);
                System.out.println();
                break;
            } else {
                System.out.print("Вы ввели неверный ответ, попробуйте еще раз. Введите y или n:");
            }
        }
    }
}