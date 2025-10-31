public class EnergySource {
    private double volt, current, efficiency;

    EnergySource() {
        this.volt = 0.0;
        this.current = 0.0;
        this.efficiency = 0.0;
    }
    
    public void setVolt(double volt) {
        this.volt = volt;
    }
    
    public void setCurrent(double current) {
        this.current = current;
    }
    
    public void setEfficiency(double efficiency) {
        this.efficiency = efficiency;
    }
    
    public double getVolt() {
        return this.volt;
    }
    
    public double getCurrent() {
        return this.current;
    }
    
    public double getEfficiency() {
        return this.efficiency;
    }
    
    public double getPower() {
        return this.volt * this.current;
    }
    
    public void showProperty() {
        System.out.println("\n+-------------------- POWER SOURCE --------------------+");
        System.out.printf("| %-15s : %10.2f %s |%n", "Volt", this.volt, "V");
        System.out.printf("| %-15s : %10.2f %s |%n", "Current", this.current, "A");
        System.out.printf("| %-15s : %10s %s |%n", "Power", String.format("%,.2f", getPower()), "W");
        System.out.println("+------------------------------------------------------+\n");
    }
}