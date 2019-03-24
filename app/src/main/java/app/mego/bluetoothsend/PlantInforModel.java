package app.mego.bluetoothsend;

/**
 * Created by nitinsinghal on 18/03/19.
 */

public class PlantInforModel {

    private String PLANT,PH,EC;

    public PlantInforModel(String PLANT, String PH, String EC) {
        this.PLANT = PLANT;
        this.PH = PH;
        this.EC = EC;
    }

    public String getPLANT() {
        return PLANT;
    }

    public String getPH() {
        return PH;
    }

    public String getEC() {
        return EC;
    }

    public void setPLANT(String PLANT) {
        this.PLANT = PLANT;
    }

    public void setPH(String PH) {
        this.PH = PH;
    }

    public void setEC(String EC) {
        this.EC = EC;
    }
}
