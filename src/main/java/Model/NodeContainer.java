package Model;

import org.json.JSONObject;

public abstract class NodeContainer {
    public NodeContainer next;
    public NodeContainer prev;
    
    public abstract JSONObject toJSON();
}
