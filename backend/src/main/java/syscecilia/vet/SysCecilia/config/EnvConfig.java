package syscecilia.vet.SysCecilia.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração para carregar variáveis de ambiente do arquivo .env
 * Inicializa o Dotenv durante a inicialização da aplicação
 */
@Configuration
public class EnvConfig {

    static {
        try {
            Dotenv dotenv = Dotenv.configure()
                    .ignoreIfMissing()
                    .load();
            
            if (dotenv != null) {
                dotenv.entries().forEach(entry ->
                        System.setProperty(entry.getKey(), entry.getValue())
                );
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar arquivo .env: " + e.getMessage());
            System.err.println("A aplicação continuará com as variáveis de ambiente do sistema.");
        }
    }
}

