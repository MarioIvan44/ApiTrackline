package apiTrackline.proyectoPTC;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProyectoPtcApplication {

	public static void main(String[] args) {
        loadEnvironmentVariables();

        //Esta linea no se borra
        SpringApplication.run(ProyectoPtcApplication.class, args);
    }

    static void loadEnvironmentVariables() {
        // Carga los valores del archivo .env (en la raiz del proyecto) sobre
        // application.properties para la ejecucion LOCAL.
        // ignoreIfMissing() evita que truene si el .env no existe.
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );
    }
}