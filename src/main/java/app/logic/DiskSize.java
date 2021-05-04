package app.logic;

public class DiskSize {

    private final long value;

    public DiskSize(long value) {
        this.value = value;
    }

    public String convert() {
        String result;

        double sizeKb = (double) value / 1024;
        double sizeMb = sizeKb / 1024;
        double sizeGb = sizeMb / 1024 ;

        if (sizeGb > 0){
            result = String.format("%.2f %s", sizeGb, "Gb");
        } else if (sizeMb > 0){
            result = String.format("%.2f %s", sizeMb, "Mb");
        } else {
            result = String.format("%.2f %s", sizeKb, "Kb");
        }

        return result;
    }
}
