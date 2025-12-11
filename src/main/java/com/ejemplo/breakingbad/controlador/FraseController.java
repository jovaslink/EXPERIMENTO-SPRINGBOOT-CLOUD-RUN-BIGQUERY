package com.ejemplo.breakingbad.controlador; // paquete donde se ubican los controladores REST
  // importamos la clase Frase que se devolvera como respuesta
import com.ejemplo.breakingbad.modelo.Frase;
//Importamos el servicio que contiene la logica de negocio
import com.ejemplo.breakingbad.servicio.FraseService;


//importamos las anotaciones de Spring para construir controladores REST
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


 //Controlador REST para el recurso "frases".
 //esta clase expone endpoints HTTP que pueden ser consumidos por clientes (frontends,postman)
 @RestController // indica que esta clase es un controlador REST y que los métodos devuelven datos JSON
@RequestMapping("/api/frases") // define la ruta base para todos los endpoints de este controlador
public class FraseController { // definicion de la clase 

    // referencia al servicio que maneja la logica de negocio relacionada con las frases
    private final FraseService servicio;

    //Constructor del controlador.
     //Spring inyecta automaticamente el servicio usando inyección de dependencias
     // servicio instancia de FraseService inyectada por spring
     
    public FraseController(FraseService servicio) { // constructor
        this.servicio = servicio; // guardamos la referencia del servicio en el atributo de la clase
    } // fin constructor

    
     //Endpoint GET que devuelve una frase aleatoria, la URL final sera: /api/frases/aleatoria
     //retornamos un objeto Frase que sera serializado automáticamente a JSON
     
    @GetMapping("/aleatoria") // mapeamos el metodo a HTTP GET y a la ruta /aleatoria
    public Frase obtenerFraseAleatoria() { // definicion del metodo que maneja la petición GET
        // llamamos al servicio para obtener una frase aleatoria desde BigQuery
        Frase frase = servicio.obtenerFraseAleatoria();
        // devolvemos la frase spring la convierte a JSON automáticamente
        return frase;
    } // fin del metodo 

} // fin de la clase