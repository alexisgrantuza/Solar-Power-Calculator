import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        ElectronicDevices devices = new ElectronicDevices();
        SolarBattery sBattery = new SolarBattery();
        SolarPanel sPanel = new SolarPanel();
        
        int choice, hours;
        printBanner("SOLAR POWER CALCULATOR");
        printSection("RATES");
        double rate = parseDoubleSafe(input("Electricity rate (pesos/kWh) [default " + devices.getRatePerKWh() + "]: ", kb), devices.getRatePerKWh());
        if (rate > 0) {
            devices.setRatePerKWh(rate);
        }
        printSection("SOLAR PANEL SETUP");
        sPanel.listPanel();
        int panelChoice = promptIntInRange(kb, "CHOICE (1-6): ", 1, 6);
        sPanel.setPanelVC(panelChoice);
        sPanel.setQuantity(promptPositiveInt(kb, "QUANTITY: "));
        sPanel.setSunlightHour(promptPositiveInt(kb, "SUNLIGHT HOUR: "));
        
        printSection("BATTERY SETUP");
        sBattery.listVolt();        
        sBattery.setVolt(promptIntInRange(kb, "CHOICE (1-3): ", 1, 3));
        sBattery.listCurrent();
        sBattery.setCurrent(promptIntInRange(kb, "CHOICE (1-5): ", 1, 5));
        sBattery.setQuantity(promptPositiveInt(kb, "QUANTITY: "));
        
        do {
            printSection("HOME ELECTRONICS");
            devices.listDevices();
            choice = parseIntSafe(input("DEVICE: ", kb), -1);

            switch (choice) {
                case 1: 
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    int defaultWatts = devices.getDeviceWattage(choice);
                    String label = devices.getDeviceLabel(choice);
                    if (defaultWatts < 0) {
                        System.out.println("Invalid device selection.");
                        break;
                    }
                    System.out.println("Selected: " + label);
                    double customWatts = parseDoubleSafe(input("Watts (W) of your device consumes per hour [default " + defaultWatts + "]: ", kb), defaultWatts);
                    hours = promptPositiveInt(kb, "Hours used: ");
                    int quantity = promptPositiveInt(kb, "Quantity: ");
                    if (customWatts <= 0) {
                        System.out.println("Invalid watts. Skipping this item.");
                        break;
                    }
                    devices.addUsageCustom(choice, hours, customWatts, quantity);
                    break;

                case 7:
                    printSection("SUMMARY");
                    devices.displayReceiptWithSolar(sPanel.getDailyEnergyWh());
                    break;

                default:
                    System.out.println("Invalid choice! Please select again.");
            }
        } while (choice >= 1 && choice <= 6);

        sBattery.showProperty();
        if (sBattery.getEnergyWh() > devices.getTotalWattHours()) {
            System.out.printf("Your battery is sufficient with extra %.0f Wh", sBattery.getEnergyWh() - devices.getTotalWattHours());
        } else {
            System.out.printf("Your battery is insufficient with deficit of %.0f Wh", devices.getTotalWattHours() - sBattery.getEnergyWh());
        }
        
        sPanel.showProperty();
        System.out.printf("Estimated daily solar energy: %.0f Wh\n", sPanel.getDailyEnergyWh());
        if (sPanel.getDailyEnergyWh() > devices.getTotalWattHours()) {
            System.out.printf("Your panels are sufficient for your daily load with extra %.0f Wh", sPanel.getDailyEnergyWh() - devices.getTotalWattHours());
        } else {
            System.out.printf("Your panels are insufficient for your daily load with deficit of %.0f Wh", devices.getTotalWattHours() - sPanel.getDailyEnergyWh());
        }
    }

    public static String input(String message, Scanner kb) {
        System.out.print(message);
        return kb.nextLine();
    }

    private static int parseIntSafe(String s, int fallback) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return fallback;
        }
    }

    private static double parseDoubleSafe(String s, double fallback) {
        try {
            return Double.parseDouble(s.trim());
        } catch (Exception e) {
            return fallback;
        }
    }

    private static int promptPositiveInt(Scanner kb, String message) {
        int v;
        do {
            v = parseIntSafe(input(message, kb), -1);
            if (v <= 0) System.out.println("Please enter a positive number.");
        } while (v <= 0);
        return v;
    }

    private static int promptIntInRange(Scanner kb, String message, int min, int max) {
        int v;
        do {
            v = parseIntSafe(input(message, kb), Integer.MIN_VALUE);
            if (v < min || v > max) System.out.println("Please enter a value between " + min + " and " + max + ".");
        } while (v < min || v > max);
        return v;
    }

    private static void printBanner(String title) {
        System.out.println("\n===============================================");
        System.out.println("           " + title);
        System.out.println("===============================================");
    }

    private static void printSection(String title) {
        System.out.println("\n-------------------- " + title + " --------------------");
    }
}