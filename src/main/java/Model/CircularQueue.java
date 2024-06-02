package Model;

public class CircularQueue {
    public boolean empty(){return first == null;}
    public int length(){
        if (first == null){return 0;}
        
        int count = 0;
        Node current = first;
        do{
            count++;
            current = current.next;
        }while(current != first);
        
        return count;
    }
    public void push(Node e){ 
        if (last == null){
            last = e;
            first = e;
            
            last.next = e;
            last.prev = e;
            first.next = last;
            first.prev = last;
            
            notifyNodeAdded(e);
            return;
        }
        
        last.next = e;
        e.prev = last;
        e.next = first;
        first.prev = e;
        
        last = e;
        
        notifyNodeAdded(e);
    }
    public Node peekAt(int index){        
        if (index > length() - 1 || index < 0){return null;}
        Node x = first;
        for (int i = 0; i < index; i++){
            x = x.next;
        }
        
        return x;
    }
    public Node pop(){
        if (first == null){return null;}
        if (length() == 1){
            Node x = first;
            
            first = null;
            last = null;
            
            notifyNodeRemoved(x);
            return x;
        }
        
        Node x = first;
        x.prev.next = x.next;
        x.next.prev = x.prev;
        first = x.next;
        
        notifyNodeRemoved(x);
        return x;
    }
    public void addListener(QueueListener listener) {
        listeners.push(listener);
    }
    public void setListeners(CircularQueue listeners) {
        this.listeners = listeners;
    }
    private void notifyNodeRemoved(Node removed){
        if (listeners == null){return;}
        
        for (int i = 0; i < listeners.length(); i++) {
            QueueListener listener = (QueueListener) listeners.peekAt(i);
            listener.onNodeRemoved(removed);
        }
    }
    private void notifyNodeAdded(Node added){
        if (listeners == null){return;}
        
        for (int i = 0; i < listeners.length(); i++) {
            QueueListener listener = (QueueListener) listeners.peekAt(i);
            listener.onNodeAdded(added);
        }
    }
    
    private CircularQueue listeners;
    private Node first;
    private Node last;
}
