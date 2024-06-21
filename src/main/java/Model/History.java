package Model;
//@author Jesús Hernández

import java.util.Date;

public class History {
    private Date date;
    private String name;
    private double price;
    private int cant;
    private double total;
    /*
    esto por si crean la clase producto
    private product
    public History ( Date date, product product ) {
        this.date = date;
        this.product = new product (  );
    }
    */
    
    public History (  ) { }
    
    public void setDate ( Date date ) { this.date = date; }
    public Date getDate (  ) { return this.date; }
    public void setName ( String name ) { this.name = name; }
    public String getName (  ) { return this.name; }
    public void setPrice ( double price ) { this.price = price; }
    public double getPrice (  ) { return this.price; }
    public void setCant ( int cant ) { this.cant = cant; }
    public int getCant (  ) { return this.cant; }
    public void setTotal ( double total ) { this.total = total; }
    public double getTotal (  ) { return this.total; }

    public History ( Date date, String name, double price, int cant ) {
        this.date = date;
        this.name = name;
        this.price = price;
        this.cant = cant;
        this.total = cant * price;
    }

    @Override
    public String toString (  ) {
        StringBuilder sb = new StringBuilder (  );
        sb.append ( "History{" );
        sb.append ( "date=" ).append ( date );
        sb.append ( ", name=" ).append ( name );
        sb.append ( ", price=" ).append ( price );
        sb.append ( ", cant=" ).append ( cant );
        sb.append ( ", total=" ).append ( total );
        sb.append ( '}' );
        return sb.toString ( );
    }
}