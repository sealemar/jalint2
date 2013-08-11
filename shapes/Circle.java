import java.awt.* ;

public class Circle implements Shape
{

    private int x ;
    private int y ;
    private int wide ;
    private int high ;
    private Color color ;
          
    Circle( int x , int y , int wide , int high , Color color )
    {
        this.x = x ;
        this.y = y ;
        this.wide = wide ;
        this.high = high ;
        this.color = color ;
    }
          
    public void draw( Graphics g )
    {
        g.setColor( color );
        g.fillOval( x , y , wide , high );
    }
          
}
