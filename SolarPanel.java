public class SolarPanel extends EnergySource {
    private int quantity = 1, sunlightHour = 0;

    public void listPanel() {
        System.out.println("SOLAR PANEL SIZE");
        System.out.println("[1] 100 watts");
        System.out.println("[2] 200 watts");
        System.out.println("[3] 300 watts");
        System.out.println("[4] 450 watts");
        System.out.println("[5] 500 watts");
        System.out.println("[6] 600 watts");
    }

    public void setPanelVC(int choice) {
        switch(choice) {
            case 1: setVolt(18); setCurrent(5.56); break;
            case 2: setVolt(18); setCurrent(11.11); break;
            case 3: setVolt(32); setCurrent(9.38); break;
            case 4: setVolt(41); setCurrent(10.98); break;
            case 5: setVolt(42); setCurrent(11.9); break;
            case 6: setVolt(45); setCurrent(13.4); break;
        }    
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public void setSunlightHour(int sunlightHour) {
        this.sunlightHour = sunlightHour;
    }
    
    public int getSunlightHour() {
        return this.sunlightHour;
    }

    public double getPower() {
        return ((getVolt() * getCurrent()) * this.quantity) * getSunlightHour();
    }
}