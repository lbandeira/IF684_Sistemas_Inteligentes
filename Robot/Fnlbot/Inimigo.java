package FnlBot;

public class Inimigo {
    String name;
    public double bearing;
    public double head;
    public long ctime; 
    public double speed;
    public double x,y;
    public double distance;
    public double chutarX(long quando)
    {
            long diferenca = quando - ctime;
            return x+Math.sin(head)*speed*diferenca;
    }
    public double chutarY(long quando)
    {
            long diferenca = quando - ctime;
            return y+Math.cos(head)*speed*diferenca;
    }

}