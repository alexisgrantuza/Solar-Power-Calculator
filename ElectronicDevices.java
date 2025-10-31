import java.util.ArrayList;
import java.util.List;

public class ElectronicDevices {
    private double totalWattHours = 0.0;
    private double ratePerKWh = 13.85;
    private final ArrayList<ReceiptItem> receipt = new ArrayList<>();

    // ----- DEVICE INFORMATION -----
    public void listDevices() {
        System.out.println("\nHOME ELECTRONICS");
        System.out.println("[1] Television");
        System.out.println("[2] Rice Cooker");
        System.out.println("[3] Air Conditioner");
        System.out.println("[4] Electric Fan");
        System.out.println("[5] Refrigerator");
        System.out.println("[6] Light");
        System.out.println("[7] End Choice");
    }

    public String getDeviceLabel(int choice) {
        switch (choice) {
            case 1: return "Television";
            case 2: return "Rice Cooker";
            case 3: return "Air Conditioner";
            case 4: return "Electric Fan";
            case 5: return "Refrigerator";
            case 6: return "Light";
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
        double watts = getDeviceWattage(choice);
        String label = getDeviceLabel(choice);
        if (watts < 0 || label == null || hours <= 0) {
            return;
        }
        if (choice == 5) {
            watts *= 0.6; // duty cycle
        }
        addUsageCustom(choice, hours, watts, 1);
    }

    public void addUsageCustom(int choice, int hours, double watts) {
        String label = getDeviceLabel(choice);
        if (label == null || hours <= 0 || watts <= 0) {
            return;
        }
        addUsageCustom(choice, hours, watts, 1);
    }

    public void addUsageCustom(int choice, int hours, double watts, int quantity) {
        String label = getDeviceLabel(choice);
        if (label == null || hours <= 0 || watts <= 0 || quantity <= 0) {
            return;
        }
        double energy = watts * hours * quantity;
        totalWattHours += energy;
        receipt.add(new ReceiptItem(label, watts, hours, quantity));
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
        ArrayList<String> lines = new ArrayList<>();
        for (ReceiptItem it : receipt) {
            lines.add(String.format("%-30s %8.1f W  x %3d h  x %2d  = %8.1f Wh", it.label, it.watts, it.hours, it.quantity, it.getEnergyWh()));
        }
        return lines;
    }

    public void displayReceipt() {
        String border = "+----+------------------------------+------------+-------+----------+------------+";
        System.out.println("\n" + border);
        System.out.printf("| %-2s | %-28s | %10s | %5s | %8s | %10s |%n", "#", "Device", "Watts(W)", "Hours", "Qty", "EnergyWh");
        System.out.println(border);
        int idx = 1;
        for (ReceiptItem it : receipt) {
            System.out.printf("| %2d | %-28s | %10.1f | %5d | %8d | %10.1f |%n", idx++, it.label, it.watts, it.hours, it.quantity, it.getEnergyWh());
        }
        System.out.println(border);
        System.out.println("\n===================RECEIPT=====================\n");

        System.out.printf("Total Daily Consumption: %,.2f Wh (%.2f kWh)%n", getTotalWattHours(), getTotalKWh());
        System.out.printf("Estimated Daily Bill:    %,.2f pesos%n", getEstimatedCost());
        System.out.printf("Estimated Monthly Bill:  %,.2f pesos%n", getEstimatedCost() * 30);
        System.out.println("===============================================");
        System.out.println();
    }

    public void displayReceiptWithSolar(double solarWh) {
        String border = "+----+------------------------------+------------+-------+----------+------------+";
        System.out.println("\n" + border);
        System.out.printf("| %-2s | %-28s | %10s | %5s | %8s | %10s |%n", "#", "Device", "Watts(W)", "Hours", "Qty", "EnergyWh");
        System.out.println(border);
        int idx = 1;
        for (ReceiptItem it : receipt) {
            System.out.printf("| %2d | %-28s | %10.1f | %5d | %8d | %10.1f |%n", idx++, it.label, it.watts, it.hours, it.quantity, it.getEnergyWh());
        }
        System.out.println(border);
        System.out.println("\n=================== RECEIPT ===================\n");

        double loadWh = getTotalWattHours();
        double loadKWh = loadWh / 1000.0;
        System.out.printf("Total Daily Consumption: %,.2f Wh (%.2f kWh)%n", loadWh, loadKWh);
        System.out.printf("Base Daily Bill (@ %.2f/kWh): %,.2f pesos%n", getRatePerKWh(), getEstimatedCost());
        System.out.printf("Estimated Daily Bill:    %,.2f pesos%n", getEstimatedCost());
        System.out.printf("Estimated Monthly Bill:  %,.2f pesos%n", getEstimatedCost() * 30);
        System.out.println("\n----------------- After Solar -----------------");
        double deficitWh = Math.max(loadWh - solarWh, 0);
        double deficitKWh = deficitWh / 1000.0;
        double dailyFinalBill = deficitKWh * getRatePerKWh();
        double monthlyFinalBill = dailyFinalBill * 30;
        System.out.printf("Solar Energy: %,.2f Wh (%.2f kWh)%n", solarWh, solarWh / 1000.0);
        System.out.printf("Grid Deficit: %,.2f Wh (%.2f kWh)%n", deficitWh, deficitKWh);
        System.out.printf("Final Daily Bill:   %,.2f pesos%n", dailyFinalBill);
        System.out.printf("Final Monthly Bill: %,.2f pesos%n", monthlyFinalBill);
        System.out.println("===============================================\n");
    }

    private static class ReceiptItem {
        String label;
        double watts;
        int hours;
        int quantity;
        ReceiptItem(String label, double watts, int hours, int quantity) {
            this.label = label;
            this.watts = watts;
            this.hours = hours;
            this.quantity = quantity;
        }
        double getEnergyWh() { return watts * hours * quantity; }
    }
}
