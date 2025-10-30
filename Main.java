import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner kb = new Scanner(System.in);
        ElectronicDevices devices = new ElectronicDevices();
        SolarBattery sBattery = new SolarBattery();
        SolarPanel sPanel = new SolarPanel();
        
        int choice, hours;
        
        sPanel.listPanel();
        sPanel.setPanelVC(Integer.parseInt(input("CHOICE: ", kb)));
        sPanel.setQuantity(Integer.parseInt(input("QUANTITY: ", kb)));
        sPanel.setSunlightHour(Integer.parseInt(input("SUNLIGHT HOUR: ", kb)));
        
        sBattery.listVolt();        
        sBattery.setVolt(Integer.parseInt(input("CHOICE: ", kb)));
        sBattery.listCurrent();
        sBattery.setCurrent(Integer.parseInt(input("CHOICE: ", kb)));
        sBattery.setQuantity(Integer.parseInt(input("QUANTITY: ", kb)));
        
        do {
            devices.listDevices();
            choice = parseIntSafe(input("DEVICE: ", kb), -1);

            switch (choice) {
                case 1: 
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                    int watts = devices.getDeviceWattage(choice);
                    String label = devices.getDeviceLabel(choice);
                    if (watts < 0) {
                        System.out.println("Invalid device selection.");
                        break;
                    }
                    if (choice == 5) {
                        System.out.println("Your " + label + " consumes " + (watts) + " watts/hour");
                        hours = 24;
                    } else {
                        System.out.println("Your " + label + " consumes " + watts + " watts/hour");
                        hours = parseIntSafe(input("Hours used: ", kb), 0);
                    }
                    devices.addUsage(choice, hours);
                    break;

                case 7:
                    devices.displayReceipt();
                    break;

                default:
                    System.out.println("Invalid choice! Please select again.");
            }
        } while (choice >= 1 && choice <= 6);

        sBattery.showProperty();
        if (sBattery.getPower() > devices.getTotalWattHours()) {
            System.out.printf("Your battery is sufficient with extra %.0f watts", sBattery.getPower() - devices.getTotalWattHours());
        } else {
            System.out.printf("Your battery is insufficient with deficit of %.0f watts", devices.getTotalWattHours() - sBattery.getPower());
        }
        
        sPanel.showProperty();
        if (sPanel.getPower() > sBattery.getPower()) {
            System.out.printf("Your panel is sufficient with extra %.0f watts", sPanel.getPower() - sBattery.getPower());
        } else {
            System.out.printf("Your panel is insufficient with deficit of %.0f watts", sBattery.getPower() - sPanel.getPower());
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
}