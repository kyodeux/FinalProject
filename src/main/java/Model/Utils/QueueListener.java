package Model.Utils;

import Model.Utils.NodeContainer;
import org.json.JSONObject;

public class QueueListener extends NodeContainer {

    public void onNodeAdded(NodeContainer newNode) {};
    public void onNodeRemoved(NodeContainer nodeRemoved) {};
    
    @Override
    public JSONObject toJSON (  ) { return null; }
}