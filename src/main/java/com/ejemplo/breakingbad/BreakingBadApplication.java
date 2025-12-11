package com.ejemplo.breakingbad; // paquete base de la aplicacion

// importamos la clase spring application para arrancar la app springboot
import org.springframework.boot.SpringApplication;
// importamos la config springboot application que marca la clase principal
import org.springframework.boot.autoconfigure.SpringBootApplication;


 //clase principal de la aplicacion spring boot (punto de entrada, similar a un main normal)
 
@SpringBootApplication // anotacion activa la configuracion automatica de springboot
public class BreakingBadApplication { // definicion de la clase publica principal

    
     // metodo main, es el primer metodo que se ejecuta al iniciar la aplicacion
     //SpringApplication.run() arranca el contexto de spring y el servidor web embebido
     
    public static void main(String[] args) { // firma del metodo main con argumentos de línea de comandos
        SpringApplication.run(BreakingBadApplication.class, args); // se inicializa la aplicacion spring boot
    } // fin del main

} // fin de la clase BreakingBadApplication
