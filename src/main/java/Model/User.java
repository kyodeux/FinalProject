package Model;

import org.json.JSONObject;

public class User extends NodeContainer{
    private final String user;
    private final String password;
    private String userName;
    private boolean isAdmin;
    
    public String getUser(){return user;}
    public String getPassword(){return password;}
    public String getUserName(){return userName;}
    public boolean isAdmin(){return isAdmin;}
    public void setUserName(String userName){
        this.userName = userName;
    }
    public void setAdministrador(boolean isAdmin){
        this.isAdmin = isAdmin;
    }
    public User(String user, String password, String userName){
        this.user = user;
        this.password = password;
        this.userName = userName;
        this.isAdmin = false;
    }
    
    public User(JSONObject data){
        this.user = data.getString("user");
        this.password = data.getString("password");
        this.userName = data.getString("userName");
        this.isAdmin = data.getBoolean("isAdmin");
    }
    public JSONObject toJSON(){
        JSONObject data = new JSONObject();
        
        data.put("user", user);
        data.put("password", password);
        data.put("userName", userName);
        data.put("isAdmin", isAdmin);
        
        return data;
    }
}
