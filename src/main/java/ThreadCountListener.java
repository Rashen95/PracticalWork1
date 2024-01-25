import java.time.LocalDateTime;

public class ThreadCountListener extends Thread {
    private final short frequency;
    private boolean isActivate = true;

    public ThreadCountListener(String name, short frequency) {
        super(name);
        this.frequency = frequency;
    }

    public void disable() {
        isActivate = false;
    }

    @Override
    public void run() {
        LocalDateTime lDT = LocalDateTime.now();
        byte hour = (byte) lDT.getHour();
        byte minute = (byte) lDT.getMinute();
        byte second = (byte) lDT.getSecond();
        System.out.printf("[%s:%s:%s] Start\n", hour, minute, second);
        while (isActivate) {
            try {
                sleep(frequency * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (isActivate) {
                lDT = LocalDateTime.now();
                hour = (byte) lDT.getHour();
                minute = (byte) lDT.getMinute();
                second = (byte) lDT.getSecond();
                System.out.printf("[%s:%s:%s] %s вариантов найдено\n", hour, minute, second, PlacesQueen.count.get());
            }
        }
    }
}
