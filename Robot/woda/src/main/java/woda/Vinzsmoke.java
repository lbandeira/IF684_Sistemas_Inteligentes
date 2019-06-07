package woda;
import robocode.*;
import java.awt.Color;

public class Vinzsmoke extends TeamRobot
{

	
        final double PI = Math.PI;			//constante
        double firePower;       			//for�a do tiro
        Inimigo alvo;					// Inimigo
        int direcao = 1;				//1 = pra frente, -1 = pra tras
        public void run() {
        	setColors(Color.white,Color.red,Color.red);
            alvo = new Inimigo();
            alvo.distance = 100000;			//inicialia a distancia
            setAdjustRadarForGunTurn(true);
            setAdjustGunForRobotTurn(true);
            turnRadarRightRadians(2*PI);			//gira todo o campo de visao
               while(true) {
                       Move();				
                       doFirePower();				//ajusta o firepower
                       Scanner();				
                       Gun(); //tenta prever o movimento
                       fire(firePower);
                       execute();				//roda todos os comandos
             }
        }

        void doFirePower() { //OK
                firePower = 300/alvo.distance;//baseado na distnacia do alvo
        }

        void Move() { //OK
                if (getTime()%20 == 0)  { 		
                        direcao *= -1;		//reverte a direcao
                        setAhead(direcao*200);	//move na direcao
                }
                setTurnRightRadians(alvo.bearing + (PI/2)); //da o strafe no inimigo
                
        }

        void Scanner() {
                double radarOffset;
                if (getTime() - alvo.ctime > 4) { 	//se nao encontra um alvo por um tempo
                        radarOffset = 360;		//gira pra tentar achar
                } else {
                        radarOffset = getRadarHeadingRadians() - absbearing(getX(),getY(),alvo.x,alvo.y);

                        if (radarOffset < 0) //pra manter track do alvo
                        radarOffset -= PI/8;
                        else
                        radarOffset += PI/8;
                }
                setTurnRadarLeftRadians(BearingNormalizado(radarOffset));
        }

        void Gun() { //tenta prever o movimento pra atirar
                long tempo = getTime() + ( int )(alvo.distance/(20-(3*firePower)));
                double gunOffset = getGunHeadingRadians() - absbearing(getX(),getY(),alvo.chutarX(tempo),alvo.chutarY(tempo));
                setTurnGunLeftRadians(BearingNormalizado(gunOffset));
        }

        public void Parede(HitWallEvent e){
           direcao *= 1;//muda de direcao quando bate na parede
        }
        
        //pega o menor angulo //OK
        double BearingNormalizado(double angulo) {
                if (angulo > PI)
                angulo -= 2*PI;
                if (angulo < -PI)
                angulo += 2*PI;
                return angulo;
        }

        //retorna a distnacia entre as coordenadas //OK
        public double getrange( double x1,double y1, double x2,double y2 )
        {
                double xo = x2-x1;
                double yo = y2-y1;
                double h = Math.sqrt( xo*xo + yo*yo );
                return h;
        }
        //pega o menor angulo//OK
        double HeadingNormalizado(double angulo) {
                if (angulo > 2*PI)
                angulo -= 2*PI;
                if (angulo < 0)
                angulo += 2*PI;
                return angulo;
        }


        //pega o absolute bearing entre as coordenadas //OK
        public double absbearing( double x1,double y1, double x2,double y2 )
        {
                double xo = x2-x1;
                double yo = y2-y1;
                double h = getrange( x1,y1, x2,y2 );
                if( xo > 0 && yo > 0 )
                {
                        return Math.asin( xo / h );
                }
                if( xo > 0 && yo < 0 )
                {
                        return Math.PI - Math.asin( xo / h );
                }
                if( xo < 0 && yo < 0 )
                {
                        return Math.PI + Math.asin( -xo / h );
                }
                if( xo < 0 && yo > 0 )
                {
                        return 2.0*Math.PI - Math.asin( -xo / h );
                }
                return 0;
        }
        public void onScannedRobot(ScannedRobotEvent e) {
                //se for amigo, ignora
	    		if (isTeammate(e.getName())) {
	                setBack((e.getDistance() - 140));//move para tras
	    			return;
	    		}
                if ((e.getDistance() < alvo.distance)||(alvo.name == e.getName())) {
                        double absbearing_rad = (getHeadingRadians()+e.getBearingRadians())%(2*PI);
                        alvo.name = e.getName();
                        alvo.x = getX()+Math.sin(absbearing_rad)*e.getDistance(); 
                        alvo.y = getY()+Math.cos(absbearing_rad)*e.getDistance();
                        alvo.bearing = e.getBearingRadians();
                        alvo.head = e.getHeadingRadians();
                        alvo.ctime = getTime();				
                        alvo.speed = e.getVelocity();
                        alvo.distance = e.getDistance();
                }
        }

        public void onRobotDeath(RobotDeathEvent e) { //OK
                if (e.getName() == alvo.name)
                alvo.distance = 10000; // quando um robo morre
        }

}
