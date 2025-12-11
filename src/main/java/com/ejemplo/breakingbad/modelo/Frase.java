package com.ejemplo.breakingbad.modelo; // paquete donde se encuentran las clases modelo

// Clase Frase
// representa una frase de la serie Breaking Bad, esta clase actua como un "DTO", es un objeto simple de java con atributos,constructores,getters y setters
public class Frase { // definicion de la clase

    // atributo que representa el ID de la frase
    private Long id;

    // atributo que representa el texto de la frase
    private String texto;

    // constructor vacio, es necesario para que spring pueda crear instancias de la clase sin argumentos
    public Frase() { // inicio del constructor vacio
        // cuerpo vacio por que no necesitamos logica aqui
    } // fin del constructor

    
     //constructor con parametros permite crear una instancia de Frase indicando directamente sus valores
     
     //id    ID de la frase
     //texto contenido de la frase
     
    public Frase(Long id, String texto) { // inicio del constructor con argumentos
        this.id = id;       // asignamos el valor de 'id' al atributo de la clase
        this.texto = texto; // asignamos el valor 'texto' al atributo de la clase
    } // fin del constructor con argumentos

     //Getter para el campo 'id' devuelve el valor de un atributo privado.
     //retornamos el identificador de la frase
     
    public Long getId() { // inicio del metodo getter de 'id'
        return id; // devolvemos el valor del atributo 'id'
    } // fin de getter'id'

    
     //Setter para el campo 'id' permite modificar el valor del atributo privado.
     
    //id nuevo valor para el identificador de la frase
     
    public void setId(Long id) { // inicio del metodo setter 'id'
        this.id = id; // asignamos el nuevo valor recibido al atributo 'id'
    } // fin del setter 'id'

    
     //Getter para el campo 'texto' retornamos el texto de la frase
     
    public String getTexto() { // inicio de getter'texto'
        return texto; // devolvemos el valor del atributo 'texto'
    } // fin del método getter

    
     //Setter para el campo 'texto' parametro texto nuevo contenido de la frase
     
    public void setTexto(String texto) { // inicio de setter 'texto'
        this.texto = texto; // asignamos el nuevo valor recibido al atributo 'texto'
    } // fin del metodo

} // fin de la clase Frase
