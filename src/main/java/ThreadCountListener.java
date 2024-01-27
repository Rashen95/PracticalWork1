public class ThreadCountListener extends Thread {
    private volatile boolean isActivate = true;
    private final short frequency;

    public ThreadCountListener(String name, short frequency) {
        super(name);
        this.frequency = frequency;
    }

    public void disable() {
        isActivate = false;
    }

    @Override
    public void run() {
        System.out.printf("%s Start\n", TimeGenerator.getCurrentTime());
        while (isActivate) {
            try {
                sleep(frequency * 1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            if (isActivate && !ThreadWriter.isEmergencySituation()) {
                System.out.printf("%s %s вариантов найдено\n", TimeGenerator.getCurrentTime(), PlacesQueen.getCount());
            }
        }
    }
}