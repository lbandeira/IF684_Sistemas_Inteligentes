package Vinzsmoke;

public class Information implements java.io.Serializable {

	 private static final long serialVersionUID = 1L;
	 public String name;
	 public double bearing;
	 public double head;
	 public long ctime; 
	 public double speed;
	 public double x,y;
	 public double distance;
	public Information(Inimigo alvo) {
        this.name = alvo.name;
        this.x = alvo.x;
        this.y = alvo.y;
        this.bearing = alvo.bearing;
        this.head = alvo.head;
        this.ctime = alvo.ctime;	
        this.speed = alvo.speed;
        this.distance = alvo.distance;
	}
	void getinfo(Inimigo alvo) {
		alvo.name = this.name;
		alvo.bearing = this.bearing;
		alvo.x = this.x;
		alvo.y = this.y;
		alvo.speed = this.speed;
		alvo.ctime = this.ctime;
		alvo.distance = distance;
		alvo.head = this.head;
	}
}
