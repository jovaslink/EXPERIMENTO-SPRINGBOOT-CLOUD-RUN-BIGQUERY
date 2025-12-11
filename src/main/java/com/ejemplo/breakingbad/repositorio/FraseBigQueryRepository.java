package com.ejemplo.breakingbad.repositorio; // paquete donde se ubican las clases de acceso a datos
import com.ejemplo.breakingbad.modelo.Frase;// importamos la clase Frase que representa el modelo de datos
// importamos las clases necesarias de BigQuery
import com.google.cloud.bigquery.BigQuery;
import com.google.cloud.bigquery.BigQueryOptions;
import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.QueryJobConfiguration;
import com.google.cloud.bigquery.TableResult;
// importamos la anotacion @Repository para marcar esta clase como componente de acceso a datos
import org.springframework.stereotype.Repository;
// importamos la anotacion @Value para leer valores del archivo de configuración
import org.springframework.beans.factory.annotation.Value;

//La idea es que la capa de servicio llame al repositorio en lugar de comunicrse directamente con BigQuery siguiendo una arquitectura mss limpia

@Repository // indicamos a spring que es una clase es un componente repositorio
public class FraseBigQueryRepository { // definicion de la clase 

    private final BigQuery bigQuery; // cliente de BigQuery que se utilizara para ejecutar consultas
    private final String projectId;// nombre del proyecto de GCP donde esta BigQuery (se lee desde configuracion)
    private final String dataset; // nombre del dataset de BigQuery donde esta la tabla de frases
    private final String table; //nombre de la tabla de BigQuery que contiene las frases

    
    //constructor del repositorio, spring inyecta los valores de configuracion usando @Value, y se construye el cliente de BigQuery usando BigQueryOptions
     
      //projectId identificador del proyecto de GCP
     //dataset   nombre del dataset de BigQuery
     //table nombre de la tabla de BigQuery
     
    public FraseBigQueryRepository(
            @Value("${google.cloud.project-id}") String projectId, // inyecta el ID de proyecto desde la config
            @Value("${google.cloud.bigquery.dataset}") String dataset, // inyecta el dataset de BigQuery
            @Value("${google.cloud.bigquery.table}") String table // inyecta la tabla de BigQuery
    ) { // inicio del constructor
        this.projectId = projectId; // guardamos el ID de proyecto en un atributo
        this.dataset = dataset;     // guardamos el dataset en un atributo
        this.table = table;         // guardamos la tabla en un atributo

        // construimos el cliente de BigQuery usando el ID de proyecto
        this.bigQuery = BigQueryOptions.newBuilder() // creamos un builder de opciones de BigQuery
                .setProjectId(this.projectId)       //configuramos el proyecto en el builder
                .build()                            // construimos las opciones
                .getService();                       // obtenemos la instancia del cliente BigQuery
    } // fin constructor

    
     //Obtiene una frase aleatoria desde BigQuery
     //la logica se implementa en esta capa de acceso a datos
     //retornamos una instancia de Frase con datos obtenidos de BigQuery o null si no hay filas
     
    public Frase obtenerFraseAleatoria() { // inicio del metodo 
        // construimos el nombre completo de la tabla en BigQuery en formato `proyecto.dataset.tabla`
        String tableFqn = String.format("`%s.%s.%s`", projectId, dataset, table);

        // definimos la consulta SQL que selecciona una fila aleatoria
        String query = String.format(
                "SELECT id, texto FROM %s ORDER BY RAND() LIMIT 1", // la consulta selecciona id y texto
                tableFqn                                           // se inserta el nombre completo de la tabla
        );

        // creamos la configuracion del job de consulta usando la cadena SQL
        QueryJobConfiguration queryConfig = QueryJobConfiguration.newBuilder(query).build();

        try { // para capturar excepciones al consultar BigQuery
            // ejecutamos la consulta BigQuery 
            TableResult result = bigQuery.query(queryConfig);

            // iteramos en este caso esperamos máximo una fila
            for (FieldValueList row : result.iterateAll()) {
                // leemos 'id' de la fila y lo convertimos a Long
                Long id = row.get("id").isNull() ? null : row.get("id").getLongValue();
                // leemos "texto" de la fila y lo convertimos a String
                String texto = row.get("texto").isNull() ? null : row.get("texto").getStringValue();
                // creamos y devolvemos una nueva instancia de Frase con los datos obtenidos
                return new Frase(id, texto);
            }

            // si no hay filas devolvemos null para indicar que no hay datos
            return null;

        } catch (InterruptedException e) { // capturamos la excepcion del hilo interrumpido
            // restauramos el estado de interrupcion del hilo actual
            Thread.currentThread().interrupt();
            // lanzamos una RuntimeException con un mensaje
            throw new RuntimeException("La consulta fue interrumpida: " + e.getMessage(), e);
        } catch (Exception e) { // capturamos cualquier excepcion
            // lanzamos RuntimeException para indicar error en la consulta
            throw new RuntimeException("Error al ejecutar la consulta " + e.getMessage(), e);
        } // Fin try catch

    } // fin de obtenerFraseAleatoria

} // fin de FraseBigQueryRepository
