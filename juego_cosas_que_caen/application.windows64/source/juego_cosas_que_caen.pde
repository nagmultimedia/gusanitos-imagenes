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

void setup() {

  cosas = new Cosas() ; // inicializo las cosas
  size(800, 600);

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


void draw() {

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
void mousePressed()
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
      cosas.velocidad.y += 0.4 ;
    }
  }
}
// ----------------------------------- instrucciones
void instrucciones() 
{
  background(200);
  fill (80); 
  textFont(f3);
  text  (" INTRUCCIONES ", width/2-95, height/2 - 150 ) ;     
  text( " Haz clic en el gatito, y no lo ejes caer!!", width/2-220, 260 );
  text ( " Si llega al piso perderás puntos y vidas.", width/2-220, 320 ) ;
  text (" SUERTE! ", width/2 - 60, height - 150  );
}
