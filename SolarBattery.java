public class SolarBattery extends Battery {
    private boolean bluetooth;
    private int quantity = 1;
    
    public void setBluetooth(boolean bluetooth) {
        this.bluetooth = bluetooth;
    }
    
    public boolean getBluetooth() {
        return this.bluetooth;
    }
    
    public void listVolt() {
        System.out.println("\nBATTERY SYSTEM VOLT");
        System.out.println("[1] 12v System");
        System.out.println("[2] 24v System");
        System.out.println("[3] 48v System");
    }
    
    public void listCurrent() {
        System.out.println("\nBATTERY SYSTEM CURRENT");
        System.out.println("[1] 50 a/h");
        System.out.println("[2] 100 a/h");
        System.out.println("[3] 150 a/h");
        System.out.println("[4] 200 a/h");
        System.out.println("[5] 280 a/h");
    }

    public void setVolt(int choice) {
        switch(choice) {
            case 1: setVolt(12.8); break;
            case 2: setVolt(25.6); break;
            case 3: setVolt(51.2); break;
        }
    }
    
    public void setCurrent(int choice) {
        switch(choice) {
            case 1: setCurrent(50.0); break;
            case 2: setCurrent(100.0); break;
            case 3: setCurrent(150.0); break;
            case 4: setCurrent(200.0); break;
            case 5: setCurrent(280.0); break;
        }        
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPower() {
        return (getVolt() * getCurrent()) * this.quantity;
    }
}