import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class juego_cosas_que_caen extends PApplet {

// El juego es un gatito que cae y al que hay que 
// atrapar.

//------------------- imagenes
PImage cosaImg ;
PImage fondo ;

//------------------objeto cosas es objeto gatito
Cosas cosas ;

int puntos ; //puntos para el usuario
int contadorParaAcelerar ; // contador para simular gravedad en la caida
int vidas ; // vidas del usuario
int puntosParaGanar ; // puntos necesarios para que el usuario gane
boolean presentacion ; // presentacion del juego, que arranca en true

//---------------------- textFonts para todo el juego
PFont f; // vidas y puntos
PFont f2 ; // ganar y perder
PFont f3 ; // presentacion e instrucciones

public void setup() {

  cosas = new Cosas() ; // inicializo las cosas
  

  //-----------------------parametros generales al inicio del juego
  puntos = 0 ;
  puntosParaGanar = 1500 ;
  contadorParaAcelerar = 0 ;

  presentacion = true ;
  vidas = 3 ;

  //------------------- imagenes 
  cosaImg = loadImage("cosaQueCae.png"); // img del gatito
  fondo = loadImage("secu6.jpg"); // img del fondo
  //-------------------- textFonts
  f2 = createFont("Verdana", 70); 

  f3 = createFont("Verdana", 20 );

  f = createFont("Verdana", 15);
  textFont(f);
}


public void draw() {

  if ( presentacion ) // inicio el modo presentacion
  {
    background (200);
    noStroke() ;
    fill(255, 60);
    ellipse (width/2, height / 2, radio*2, radio*2 ) ; // ellipse que esta atras del gatito

    cosas.presentacion(); // el gato se autopresenta
    // textos de la presentacion    
    fill(80);
    textFont(f3);
    text ("Presiona I para ver las instrucciones", width/2 - 200, height/2 - 150 ) ;
    text ( "Haz Clic para jugar", width/2-120, height - 150 );

    if ( keyPressed  ) // si se apreta i se activa el void instrucciones
    {
      if (key == 'i' )
      {

        instrucciones() ;
      }
    } 

    if ( mousePressed ) // si se cliquea se inicial el juego
    {

      presentacion = false ; 
      // NOTA: si mousePressed se apreta mientras está presionado 'I', gana mousePressed y el juego comienza
    }
  } 
  else {
    if (vidas >= 1 && puntos < puntosParaGanar ) // mientras usuario tenga mas de una vida y no haya ganado por puntos
    {   
      imageMode(CORNER);
      image(fondo, 0, 0);
      cosas.dibujar();
      cosas.caer();

      textFont(f);
      fill(180, 38, 43);
      text(" PUNTOS " + puntos, width-150, 30 );
      text ("VIDAS " + vidas, 30, 30 ) ;
    }

    if (puntos>=puntosParaGanar) //usuario gana por puntos
    {
      textFont(f2);  
      fill(245, 143, 143);
      text("GANASTE!", width/2-200, height/2+20 );
    }

    if (vidas<=0) // usuario pierde por vidas
    {
      textFont(f2);  
      fill(206, 92, 92);
      text("PERDISTE", width/2-200, height/2+20 );
    }

    if (puntos < 0 ) // para que puntos siempre sea un numero natural (N = enteros positivos)
    {
      puntos = 0 ;
    }
  }
}

//---------------------------------------------- atrapar
public void mousePressed()
{

  // si la distancia en x e y de mouse es muy cercana al x e y del gato y se hace clic
  if (mouseX-cosas.posicion.x < radio && mouseY-cosas.posicion.y < radio) 
  {
    cosas.posicion.y = random(-50, -600); // los pone arriba de la ventana 
    cosas.posicion.x = random( radio, width-radio); // en posicion random en x
    angulo = ( random ( 0, 500) ) ; // en cualquier angulo de giro
    puntos = puntos + 50 ;


    // el contador sirve para ir acumentando la dificultad en el tiempo 
    // y para simular gravedad
    contadorParaAcelerar ++ ;
    if (contadorParaAcelerar%30==0) // cada 30 frames añade 0.4 a velocidad
    {
      cosas.velocidad.y += 0.4f ;
    }
  }
}
// ----------------------------------- instrucciones
public void instrucciones() 
{
  background(200);
  fill (80); 
  textFont(f3);
  text  (" INTRUCCIONES ", width/2-95, height/2 - 150 ) ;     
  text( " Haz clic en el gatito, y no lo ejes caer!!", width/2-220, 260 );
  text ( " Si llega al piso perderás puntos y vidas.", width/2-220, 320 ) ;
  text (" SUERTE! ", width/2 - 60, height - 150  );
}

// variables globales de gato/'Cosas'
int radio = 50 ;
float angulo ;

class Cosas {

  float anguloDeCadaGato ;
  PVector posicion ;
  PVector velocidad ;


  Cosas () { 

    // inicializar aqui estas variables hace que cada garo/cosa tenga su propio valor para c/variable
    // de esta manera, todos 'son diferentes' dentro de los rangos establecidos

    posicion = new PVector (PApplet.parseInt(random(radio, width-radio)), PApplet.parseInt(random(-200, -800)));
    velocidad = new PVector ( 0, 1 );
    angulo = 0 ; 
    anguloDeCadaGato = (random (0.2f, 1.5f)); // valocidad de rotacion de cada gato
  }


  //------------------------------------------------ dibujar gato
  public void dibujar () {

    angulo += anguloDeCadaGato ;
    pushMatrix();
    translate( posicion.x, posicion.y);
    rotate(radians(angulo));
    imageMode(CENTER);
    image(cosaImg, 0, 0-radio/2+5);
    popMatrix();
  }

  // -----------------------------------------------presentacion del gato
  public void presentacion ()
  {
    angulo += anguloDeCadaGato ;
    pushMatrix();
    translate( width/2, height/2);
    rotate(radians(angulo));
    imageMode(CENTER);
    image(cosaImg, 0, 0);
    popMatrix();
  }


  //----------------------------------- accion de bajar por pantalla
  public void caer() {
    posicion.add( velocidad );

    // acelerador de gravedad por tiempo
    if (frameCount%20==0) // cada 20 frames incrementa 0.2 a velocidad
    {
      velocidad.y += 0.2f ;
    }

    // si el gato 'cae' de la pantalla = 
    if (posicion.y > height+radio)
    {
      seCayo();
    }
  }

  public void seCayo () 
  {
    // le doy nuevos valores antes de reingresar
    posicion.y = - 400 ; // para darle tiempo a usuario a que se serene un poco
    posicion.x = (random(radio, width-radio));
    angulo = ( random ( 0, 100) ) ;

    // depende la velocidad a la que iba, la velocidad a la que reingresa en pantalla
    if (velocidad.y < 10 )
    {
      velocidad.y = 2 ; // si no iba muy avanzado
    } 
    else if ( velocidad.y >=10 )
    {
      velocidad.y = 4 ; // si iba rapidisimo
    }


    puntos = puntos - 100 ;
    vidas = vidas - 1 ;    


    //-----------destello 
    background(255, 0, 0);

    if (vidas == 0 )
    {     
      imageMode(CORNER);
      image(fondo, 0, 0);
      if (puntos < 0 )
      {
        puntos = 0 ;
      }
    }
  }
}
  public void settings() {  size(800, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "juego_cosas_que_caen" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
