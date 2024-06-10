package Model;

import org.json.JSONObject;

public class User extends NodeContainer {

    private String displayName;
    private String password;
    private final String userName;
    private boolean isAdmin;

    public String getDisplayName (  ) { return displayName; }

    public String getPassword (  ) { return password; }

    public String getUserName (  ) { return userName; }

    public boolean isAdmin (  ) { return isAdmin; }

    public void setDisplayName ( String displayName ) {
        this.displayName = displayName;
    }

    public void setPassword ( String newPassword ){
        this.password = newPassword;
    }
    
    public void setAdministrador ( boolean isAdmin ) {
        this.isAdmin = isAdmin;
    }

    public User ( String userName, String password, String displayName ) {
        this.displayName = displayName;
        this.password = password;
        this.userName = userName;
        this.isAdmin = false;
    }

    public User ( JSONObject data ) {
        this.displayName = data.getString("displayName");
        this.password = data.getString("password");
        this.userName = data.getString("userName");
        this.isAdmin = data.getBoolean("isAdmin");
    }

    @Override
    public JSONObject toJSON (  ) {
        JSONObject data = new JSONObject();

        data.put("displayName", displayName);
        data.put("password", password);
        data.put("userName", userName);
        data.put("isAdmin", isAdmin);

        return data;
    }
}
