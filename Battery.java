public class Battery {
    private double volt, current;
    
    Battery() {
        this.volt = 0.0;
        this.current = 0.0;
    }
    
    public void setVolt(double volt) {
        this.volt = volt;
    }
    
    public void setCurrent(double current) {
        this.current = current;
    }
    
    public double getVolt() {
        return this.volt;
    }
    
    public double getCurrent() {
        return this.current;
    }
    
    public double getPower() {
        return this.volt * this.current;
    }
    
    public double getEnergyWh() {
        return getPower();
    }
    
    public void showProperty() {
        System.out.println("\n+---------------------- BATTERY ----------------------+");
        System.out.printf("| %-15s : %10.2f %s |%n", "Volt", this.volt, "V ");
        System.out.printf("| %-15s : %10.2f %s |%n", "Current", this.current, "Ah");
        System.out.printf("| %-15s : %10s %s |%n", "Capacity", String.format("%,.2f", getEnergyWh()), "Wh");
        System.out.println("+-----------------------------------------------------+\n");
    }
}