
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

    posicion = new PVector (int(random(radio, width-radio)), int(random(-200, -800)));
    velocidad = new PVector ( 0, 1 );
    angulo = 0 ; 
    anguloDeCadaGato = (random (0.2, 1.5)); // valocidad de rotacion de cada gato
  }


  //------------------------------------------------ dibujar gato
  void dibujar () {

    angulo += anguloDeCadaGato ;
    pushMatrix();
    translate( posicion.x, posicion.y);
    rotate(radians(angulo));
    imageMode(CENTER);
    image(cosaImg, 0, 0-radio/2+5);
    popMatrix();
  }

  // -----------------------------------------------presentacion del gato
  void presentacion ()
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
  void caer() {
    posicion.add( velocidad );

    // acelerador de gravedad por tiempo
    if (frameCount%20==0) // cada 20 frames incrementa 0.2 a velocidad
    {
      velocidad.y += 0.2 ;
    }

    // si el gato 'cae' de la pantalla = 
    if (posicion.y > height+radio)
    {
      seCayo();
    }
  }

  void seCayo () 
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
