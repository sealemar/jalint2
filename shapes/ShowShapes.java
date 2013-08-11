import java.awt.* ;

public class ShowShapes extends Frame
{

    static int[] vx = { 200 , 220 , 240 , 260 , 280 , 250 , 230 };
    static int[] vy = { 150 , 150 , 190 , 150 , 150 , 210 , 210 };

    static Shape[] shapes =
    {
        // J
        new Box( 50 , 70 , 100 , 20 , Color.red ) ,
        new Box( 90 , 70 , 20 , 110 , Color.blue ) ,
        new Circle( 50 , 150 , 60 , 60 , Color.green ) ,
        new Circle( 70 , 170 , 20 , 20 , Color.white ) ,
        new Box( 50 , 90 , 40 , 90 , Color.white ) ,
              
        // a
        new Circle( 130 , 150 , 60 , 60 , Color.green ) ,
        new Box( 170 , 180 , 20 , 30 , Color.blue ) ,
        new Circle( 150 , 170 , 20 , 20 , Color.white ) ,
              
        // v
        new Poly( vx , vy , Color.black ) ,
              
        // a
        new Circle( 290 , 150 , 60 , 60 , Color.green ) ,
        new Box( 330 , 180 , 20 , 30 , Color.blue ) ,
        new Circle( 310 , 170 , 20 , 20 , Color.white ) ,
    };
          
    ShowShapes()
    {
        setBounds( 200 ,150 , 400 , 250 );
        setVisible( true );
    }
          
    public void paint( Graphics g )
    {
        for( int i = 0 ; i < shapes.length ; i++ )
            {
                shapes[ i ].draw( g );
            }
    }
          
    public static void main( String[] args )
    {
        new ShowShapes();
    }
          
}

