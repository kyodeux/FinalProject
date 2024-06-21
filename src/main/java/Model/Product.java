package Model;

import Model.Utils.NodeContainer;
import org.json.JSONObject;

public class Product extends NodeContainer{
    private String name;
    private double cost;
    private int sales;
    
    public String getName(){
        return name;
    }
    public double getCost(){
        return cost;
    }
    public double getSales(){
        return sales;
    }
    
    public Product(){
        
    }
    public Product(JSONObject data){
        this.name = data.getString("name");
        this.cost = data.getDouble("cost");
        this.sales = data.getInt("sales");
    }
    
    @Override
    public JSONObject toJSON() {
        JSONObject data = new JSONObject();
        data.put("name", name);
        data.put("cost", cost);
        data.put("sales", sales);
        
        return data;
    }
}
