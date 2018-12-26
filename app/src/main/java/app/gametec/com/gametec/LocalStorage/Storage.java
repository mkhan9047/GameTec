package app.gametec.com.gametec.LocalStorage;

import android.content.Context;
import android.content.SharedPreferences;

public class Storage {

    private static final boolean LOGIN_SATE = false;
    private static final String ACCESS_TOKEN = null;
    private static final String REFRESH_TOKEN = null;
    private static final String ACCESS_TYPE = "Bearer";
    private static final String ROLE = null;
    private static final int CURRENT_MACHINE = 0;
    private static final String CURRENT_MACHINE_NAME = "";
    private static final boolean HAS_PIN = false;
    private static final String PIN_NUMBER = "";

    private Context context;

    public Storage(Context context) {
        this.context = context;
    }


    private SharedPreferences.Editor getPreferencesEditor() {
        return getsharedPreferences().edit();
    }

    private SharedPreferences getsharedPreferences() {

        return context.getSharedPreferences("MyData", Context.MODE_PRIVATE);
    }


    public void SaveLogInSate(boolean p) {

        getPreferencesEditor().putBoolean("logged_in", p).commit();

    }

    public boolean getLogInState() {

        return getsharedPreferences().getBoolean("logged_in", LOGIN_SATE);
    }

    public void SaveAccessToken(String token) {

        getPreferencesEditor().putString("token", token).commit();

    }

    public String getAccessToken() {

        return getsharedPreferences().getString("token", ACCESS_TOKEN);
    }

    public void SaveAccessType(String type) {

        getPreferencesEditor().putString("type", type).commit();

    }

    public String getAccessType() {

        return getsharedPreferences().getString("type", ACCESS_TYPE);
    }


    public void SaveRefreshToken(String token) {

        getPreferencesEditor().putString("refresh", token).commit();

    }

    public String getRefreshToken() {

        return getsharedPreferences().getString("refresh", REFRESH_TOKEN);
    }

    public void SaveRole(String role) {

        getPreferencesEditor().putString("role", role).commit();

    }

    public String getRole() {

        return getsharedPreferences().getString("role", ROLE);
    }

    public void SaveExit(boolean exit) {

        getPreferencesEditor().putBoolean("exit", exit);
    }

    public boolean getExit() {

        return getsharedPreferences().getBoolean("exit", false);
    }

    public void saveCurrentMachine(int id) {

        getPreferencesEditor().putInt("machine", id).commit();

    }

    public int getCurrentMachine() {

        return getsharedPreferences().getInt("machine", CURRENT_MACHINE);

    }

    public void saveCurrentMachineName(String name) {

        getPreferencesEditor().putString("machine_name", name).commit();
    }

    public String getCurrentMachineName() {

        return getsharedPreferences().getString("machine_name", CURRENT_MACHINE_NAME);
    }

    public void saveHasPin(boolean has) {

        getPreferencesEditor().putBoolean("has_pin", has).commit();

    }

    public boolean getHasPin() {

        return getsharedPreferences().getBoolean("has_pin", HAS_PIN);
    }





    public void savePin(String pin) {
        getPreferencesEditor().putString("pin", pin).commit();
    }

    public String getPin() {
        return getsharedPreferences().getString("pin", PIN_NUMBER);
    }
}
