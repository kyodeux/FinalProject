package Model;

import Model.Utils.CircularQueue;
import Model.Utils.NodeContainer;
import org.json.JSONArray;
import org.json.JSONObject;

public class User extends NodeContainer {

    private String displayName;
    private String password;
    private final String authURI;
    private final String userName;
    private boolean isAdmin;
    public boolean forgotPassword = false;
    private final CircularQueue favorites = new CircularQueue();
    private final CircularQueue history = new CircularQueue();
    private final CircularQueue cart = new CircularQueue();

    public String getDisplayName() {
        return displayName;
    }
    public String getPassword() {
        return password;
    }
    public String getUserName() {
        return userName;
    }
    public String getAuthURI(){
        return authURI;
    }
    public boolean isAdmin() {
        return isAdmin;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public void setPassword(String newPassword){
        this.password = newPassword;
    }
    public void setAdministrador(boolean isAdmin) {
        
        this.isAdmin = isAdmin;
    }
    public User(String userName, String password, String displayName, String authURI) {
        this.displayName = displayName;
        this.password = password;
        this.userName = userName;
        this.isAdmin = false;
        this.authURI = authURI;
    }
    public User(JSONObject data) {
        this.displayName = data.getString("displayName");
        this.password = data.getString("password");
        this.userName = data.getString("userName");
        this.isAdmin = data.getBoolean("isAdmin");
        this.authURI = data.getString("authURI");
        
        JSONArray favoritesJSON = (JSONArray) data.get("favorites");
        JSONArray historyJSON = (JSONArray) data.get("favorites");
        JSONArray cartJSON = (JSONArray) data.get("favorites");
        
        for (int i = 0; i < favoritesJSON.length(); i++){
            JSONObject x = favoritesJSON.getJSONObject(i);
            favorites.push(new FavoriteItem(x));
        }
        for (int i = 0; i < historyJSON.length(); i++){
            JSONObject x = historyJSON.getJSONObject(i);
            history.push(new HistoryItem(x));
        }
        for (int i = 0; i < cartJSON.length(); i++){
            JSONObject x = cartJSON.getJSONObject(i);
            cart.push(new CartItem(x));
        }
    }
    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        data.put("displayName", displayName);
        data.put("password", password);
        data.put("userName", userName);
        data.put("isAdmin", isAdmin);
        data.put("authURI", authURI);
        data.put("favorites", favorites.toJSON());
        data.put("history", history.toJSON());
        data.put("cart", cart.toJSON());
        
        return data;
    }
}
