import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        ElectronicDevices devices = new ElectronicDevices();
        SolarBattery sBattery = new SolarBattery();
        SolarPanel sPanel = new SolarPanel();
        
        int choice, hours;
        printBanner("SOLAR POWER CALCULATOR");
        printSection("MODE");
        System.out.println("[1] Electricity bill calculator");
        System.out.println("[2] Sizing calculator");
        int mode = promptIntInRange(kb, "Choose mode (1-2): ", 1, 2);

        if (mode == 1) {
            // ===== EXISTING FLOW =====
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
        } else {
            // ===== SIZING CALCULATOR =====
            

            printSection("SIZING INPUTS");
            double psh; // Peak Sun Hours
            do {
                psh = parseDoubleSafe(input("Peak Sun Hours (hours/day): ", kb), -1);
                if (psh <= 0) System.out.println("Please enter a positive number.");
            } while (psh <= 0);

            System.out.println("System Voltage:");
            System.out.println("[1] 12V");
            System.out.println("[2] 24V");
            System.out.println("[3] 48V");
            int sysChoice = promptIntInRange(kb, "CHOICE (1-3): ", 1, 3);
            int systemV = (sysChoice == 1 ? 12 : (sysChoice == 2 ? 24 : 48));

            double dodPct; // Depth of Discharge
            do {
                dodPct = parseDoubleSafe(input("Depth of Discharge (DoD %) e.g., 50: ", kb), -1);
                if (dodPct <= 0 || dodPct > 100) System.out.println("Please enter a percent between 1 and 100.");
            } while (dodPct <= 0 || dodPct > 100);
            int days = promptPositiveInt(kb, "Days of Autonomy (backup days): ");

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
                        double customWatts = parseDoubleSafe(input("Watts (W) [default " + defaultWatts + "]: ", kb), defaultWatts);
                        hours = promptPositiveInt(kb, "Hours used: ");
                        int quantity = promptPositiveInt(kb, "Quantity: ");
                        if (customWatts <= 0) {
                            System.out.println("Invalid watts. Skipping this item.");
                            break;
                        }
                        devices.addUsageCustom(choice, hours, customWatts, quantity);
                        break;

                    case 7:
                        // Compute sizing
                        double loadWh = devices.getTotalWattHours();
                        double derate = 0.8; 
                        double requiredPanelW = Math.ceil(loadWh / (psh * derate));
                        double dod = dodPct / 100.0; 
                        double requiredBattWh = Math.ceil((loadWh * days) / dod);
                        double requiredBattAh = Math.ceil(requiredBattWh / systemV);
                        double invSafety = 1.25;
                        double recommendedInverterW = Math.ceil(devices.getApproxPeakWatts() * invSafety);
                        double ctrlSafety = 1.25;
                        double recommendedControllerA = Math.ceil((requiredPanelW / systemV) * ctrlSafety);

                        printSection("SIZING RESULTS");
                        System.out.printf("Total Daily Load:        %,.2f Wh (%.2f kWh)%n", loadWh, loadWh/1000.0);
                        System.out.printf("Peak Sun Hours (PSH):    %.2f h/day%n", psh);
                        System.out.printf("System Voltage:          %d V%n", systemV);
                        System.out.printf("Days of Autonomy:        %d days%n", days);
                        System.out.printf("DoD (usable):            %.0f%%%n", dodPct);
                        System.out.println("----------------------------------------------");
                        System.out.printf("Required Solar Panel:    %,.0f W  (derate=%.0f%%)%n", requiredPanelW, derate*100);
                        System.out.printf("Required Battery:        %,.0f Ah @ %d V  (%,.0f Wh)%n", requiredBattAh, systemV, requiredBattWh);
                        System.out.printf("Recommended Inverter:    %,.0f W  (%.0fx safety)%n", recommendedInverterW, invSafety);
                        System.out.printf("Charge Controller Size:  %,.0f A  (%.0fx safety)%n", recommendedControllerA, ctrlSafety);
                        System.out.println();
                        break;

                    default:
                        System.out.println("Invalid choice! Please select again.");
                }
            } while (choice >= 1 && choice <= 6);
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