package Model.Utils;

import org.json.JSONObject;

public abstract class NodeContainer {

    public NodeContainer next;
    public NodeContainer prev;

    public abstract JSONObject toJSON();
}
