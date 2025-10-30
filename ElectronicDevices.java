import java.util.ArrayList;
import java.util.List;

public class ElectronicDevices {
    private double totalWattHours = 0.0;
    private double ratePerKWh = 13.85;
    private final ArrayList<String> receipt = new ArrayList<>();

    // ----- DEVICE INFORMATION -----
    public void listDevices() {
        System.out.println("\nHOME ELECTRONICS");
        System.out.println("[1] Television");
        System.out.println("[2] Rice Cooker");
        System.out.println("[3] Air Conditioner");
        System.out.println("[4] Electric Fan");
        System.out.println("[5] Refrigirator");
        System.out.println("[6] Light");
        System.out.println("[7] End Choice");
    }

    public String getDeviceLabel(int choice) {
        switch (choice) {
            case 1: return "43 inch LCD Television";
            case 2: return "Toshiba Family Rice Cooker";
            case 3: return "1HP Non-Inverter Air Conditioner";
            case 4: return "Wall Electric Fan";
            case 5: return "Inverter Refrigirator";
            case 6: return "LED Light";
            default: return null;
        }
    }

    public int getDeviceWattage(int choice) {
        switch (choice) {
            case 1: return 45;
            case 2: return 400;
            case 3: return 550;
            case 4: return 40;
            case 5: return 70;
            case 6: return 32;
            default: return -1;
        }
    }

    // ----- USAGE MANAGEMENT -----
    public void addUsage(int choice, int hours) {
        int watts = getDeviceWattage(choice);
        String label = getDeviceLabel(choice);
        if (watts < 0 || label == null || hours <= 0) {
            return;
        }
        if (choice == 5) {
            watts *= .6;
        }
        totalWattHours += watts * hours;
        receipt.add(label + " - " + watts + " watts, " + hours + " hours");
    }

    // ----- COMPUTATIONS -----
    public double getTotalKWh() {
        return totalWattHours / 1000.0;
    }

    public double getRatePerKWh() {
        return ratePerKWh;
    }
    
    public double getTotalWattHours() {
        return totalWattHours;
    }    

    public void setRatePerKWh(double ratePerKWh) {
        if (ratePerKWh > 0) {
            this.ratePerKWh = ratePerKWh;
        }
    }

    public double getEstimatedCost() {
        return getTotalKWh() * ratePerKWh;
    }

    // ----- REPORTING -----
    public List<String> getReceipt() {
        return new ArrayList<>(receipt);
    }

    public void displayReceipt() {
        System.out.println("\nRECEIPT");
        for (String line : receipt) {
            System.out.println(line);
        }
        System.out.printf("Your total daily consumption is %.2f kWh.%n", getTotalKWh());
        System.out.printf("Your total daily bill is %.2f pesos.%n", getEstimatedCost());
        System.out.printf("Your total monthly bill is %.2f pesos.%n", getEstimatedCost() * 30 );
    }
}
