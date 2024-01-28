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
                String count = getNumberWithSeparator(Long.toString(PlacesQueen.getCount()));
                System.out.printf("%s %s вариантов удовлетворяющих условию найдено\n", TimeGenerator.getCurrentTime(), count);
            }
        }
    }

    public static String getNumberWithSeparator(String input) {
        if (input == null || input.length() <= 3) {
            return input;
        }
        StringBuilder result = new StringBuilder();
        int length = input.length();
        for (int i = length - 1, count = 0; i >= 0; i--, count++) {
            result.insert(0, input.charAt(i));
            if (count == 2 && i > 0) {
                result.insert(0, '.');
                count = -1;
            }
        }
        return result.toString();
    }
}