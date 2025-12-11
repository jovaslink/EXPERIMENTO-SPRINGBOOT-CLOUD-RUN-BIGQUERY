package com.ejemplo.breakingbad.servicio; // paquete donde se ubican las clases de la logica del negocio

// importamos la clase Frase que representa el modelo
import com.ejemplo.breakingbad.modelo.Frase;
import com.ejemplo.breakingbad.repositorio.FraseBigQueryRepository; // importamos el repositorio que se comunica con BigQuery
// importamos la anotacion @Service para marcar esta clase como un servicio de spring
import org.springframework.stereotype.Service;

//servicio para trabajar con frases, esta capa se encarga de la logica y usa el repositorio para acceder a los datos
@Service // indicamos que esta clase es un componente de tipo servicio
public class FraseService { // definición de la clase publica FraseService

    // referencia al repositorio que accede a BigQuery
    private final FraseBigQueryRepository repositorio;

    //constructor del servicio, spring se encarga de inyectar una instancia del repositorio automticamente.
     
     //repositorio instancia de FraseBigQueryRepository inyectada por spring
     public FraseService(FraseBigQueryRepository repositorio) { // Inicio del constructor
        this.repositorio = repositorio; // Guardamos el repositorio en el atributo de la clase
    } // Fin del constructor

    
     //obtiene una frase aleatoria desde el repositorio
     
     //retorna una instancia de Frase con los datos de BigQuery
     
    public Frase obtenerFraseAleatoria() { // inicio del metodo publico obtenerFraseAleatoria
        // llamamos al repositorio para obtener la frase aleatoria
        Frase frase = repositorio.obtenerFraseAleatoria();

        // si el repositorio devuelve null significa que no hay datos
        if (frase == null) { // comprobamos si la frase es null
            // devolvemos una frase por defecto para evitar errores en el controlador
            return new Frase(-1L, "No hay frases disponibles en BigQuery."); // retornamos un objeto Frase por defecto
        } // fin del if

        // si la frase no es null la devolvemos tal cual
        return frase; // retornamos la frase obtenida del repositorio
    } // fin del metodo obtenerFraseAleatoria

} // fin de la clase FraseService
