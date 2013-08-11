import java.awt.* ;

public class Poly implements Shape
{

    int[] x ;
    int[] y ;
    private Color color ;
          
    Poly( int[] x , int[] y , Color color )
    {
        this.x = x ;
        this.y = y ;
        this.color = color ;
    }
          
    public void draw( Graphics g )
    {
        g.setColor( color );
        g.fillPolygon( x , y , x.length );
    }
          
}

