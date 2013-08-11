
import java.awt.* ;

public class Box implements Shape
{

    private int x ;
    private int y ;
    private int wide ;
    private int high ;
    private Color color ;
          
    Box( int x , int y , int wide , int high , Color color )
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
        g.fillRect( x , y , wide , high );
    }
          
}
