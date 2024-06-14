package Model;
//14 - 06 - 2024
import Model.Utils.NodeContainer;
import org.json.JSONObject;

public class Product extends NodeContainer{
    private int Cant;
    private int ContId;
    private int Id;
    private String Name;
    private double Price;
    private double Total;
    
    public Product (  ) { this.ContId = 0; }
    
    public int getCant (  ) { return Cant; }
    public void setCant ( int Cant ) { this.Cant = Cant; }
    public int getContId (  ) { return ContId; }
    public void setContId ( int ContId ) { this.ContId = ContId; }
    public int getId (  ) { return Id; }
    public void setId ( int Id ) { this.Id = Id; }
    public String getName (  ) { return Name; }
    public void setName ( String Name ) { this.Name = Name; }
    public double getPrice (  ) { return Price; }
    public void setPrice ( double Price ) { this.Price = Price; }
    public double getTotal (  ) { return Total; }
    public void setTotal ( double Total ) { this.Total = Total; }
    
    public Product ( JSONObject data, int Cant, int Id, String Name,
            double Price ) {
        this.Cant = Cant;
        this.Id = ++ContId;
        this.Name = Name;
        this.Price = Price;
        this.Total = Cant * Price;
    }

    @Override
    public String toString (  ) {
        StringBuilder sb = new StringBuilder (  );
        sb.append ( "Product{" );
        sb.append ( "Cant=" ).append ( Cant );
        sb.append ( ", ContId=" ).append ( ContId );
        sb.append ( ", Id=" ).append ( Id );
        sb.append ( ", Name=" ).append ( Name );
        sb.append ( ", Price=" ).append ( Price );
        sb.append ( ", Total=" ).append ( Total );
        sb.append ( '}');
        return sb.toString();
    }
    
    @Override
    public JSONObject toJSON (  ) { return null; }
}