package snow.app.padelbook.session;

import android.content.Context;
import android.content.SharedPreferences;
import com.google.gson.Gson;

import snow.app.padelbook.network.responses.login.LoginData;
import snow.app.padelbook.network.responses.searchResponse.searchNearBy.recentHistory.RecentData;

public class SessionClass {
    private static final String PREF_NAME = "padelbook_device_token";
    private static final String PREF_NAME_TWO = "padelbook_device_token_two";

    private SharedPreferences.Editor editor;
    private SharedPreferences pref;

    SharedPreferences.Editor editorTwo;
    SharedPreferences prefTwo;
    int PRIVATE_MODE;
    Context _context;
    int securityQuestionLength = 0;


    public SessionClass(Context context) {

        PRIVATE_MODE = 0;
        _context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        prefTwo = _context.getSharedPreferences(PREF_NAME_TWO, PRIVATE_MODE);
        editorTwo = prefTwo.edit();

    }
    public Boolean isRememberMe() {
        return prefTwo.getBoolean("is_remember_me", false);
    }

    public void setRememberMe(boolean mFlag) {
        editorTwo.putBoolean("is_remember_me", mFlag);
        editorTwo.commit();
    }

    public Boolean isLogin() {
        return pref.getBoolean("is_login", false);
    }

    public void setLogin(boolean mFlag) {
        editor.putBoolean("is_login", mFlag);
        editor.commit();
    }

    public String getEmailRememberMe() {
        return prefTwo.getString("EmailRememberMe", "");
    }

    public void setEmailRememberMe(String mEmail) {
        editorTwo.putString("EmailRememberMe", mEmail);
        editorTwo.commit();
    }

    public String getPwdRememberMe() {
        return prefTwo.getString("PwdRememberMe", "");
    }

    public void setPwdRememberMe(String mPwd) {
        editorTwo.putString("PwdRememberMe", mPwd);
        editorTwo.commit();
    }

    public void setLoginData(LoginData data) {
        Gson gson = new Gson();
        String sData = gson.toJson(data,LoginData.class);
        editor.putString("loginData",sData);
        editor.commit();
    }

    public LoginData getLoginData(){
        return new Gson().fromJson(pref.getString("loginData", ""), LoginData.class);
    }

    public void setRecentData(RecentData data){
        Gson gson = new Gson();
        String sData = gson.toJson(data,RecentData.class);
        editor.putString("recentData",sData);
        editor.commit();
    }
    public RecentData getRecentData(){
        return new Gson().fromJson(pref.getString("recentData", ""), RecentData.class);
    }

    public void clearSession() {
        editor.clear();
        editor.commit();
    }

    public String getDeviceFCMToken() {
        return pref.getString("device_fcm_token", "");
    }

    public void setDeviceFCMToken(String token) {
        editor.putString("device_fcm_token", token);
        editor.commit();
    }
}
