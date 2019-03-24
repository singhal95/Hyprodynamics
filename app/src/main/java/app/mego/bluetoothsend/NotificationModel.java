package app.mego.bluetoothsend;

/**
 * Created by nitinsinghal on 19/03/19.
 */

public class NotificationModel {
    private String ALERTMESSAGE,DATE;

    public NotificationModel(String ALERTMESSAGE, String DATE) {
        this.ALERTMESSAGE = ALERTMESSAGE;
        this.DATE = DATE;

    }

    public void setALERTMESSAGE(String ALERTMESSAGE) {
        this.ALERTMESSAGE = ALERTMESSAGE;
    }

    public void setDATE(String DATE) {
        this.DATE = DATE;
    }



    public String getALERTMESSAGE() {
        return ALERTMESSAGE;
    }

    public String getDATE() {
        return DATE;
    }


}
