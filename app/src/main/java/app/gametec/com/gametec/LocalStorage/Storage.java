package app.gametec.com.gametec.LocalStorage;

import android.content.Context;
import android.content.SharedPreferences;

public class Storage {

    private static final boolean LOGIN_SATE = false;
    private static final String ACCESS_TOKEN = null;
    private static final String REFRESH_TOKEN = null;
    private static final String ACCESS_TYPE = "Bearer";
    private static final String ROLE = null;
    private static final  int CURRENT_MACHINE = 0;

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

    public void SaveRole(String role){

        getPreferencesEditor().putString("role", role).commit();

    }

    public String getRole(){

        return getsharedPreferences().getString("role", ROLE);
    }

    public void SaveExit(boolean exit){

        getPreferencesEditor().putBoolean("exit", exit);
    }

    public boolean getExit(){

        return  getsharedPreferences().getBoolean("exit", false);
    }

    public void saveCurrentMachine(int id){

        getPreferencesEditor().putInt("machine", id).commit();

    }

    public int getCurrentMachine(){

        return getsharedPreferences().getInt("machine", CURRENT_MACHINE);

    }
}
