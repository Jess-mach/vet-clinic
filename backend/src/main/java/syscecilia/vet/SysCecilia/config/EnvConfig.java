package syscecilia.vet.SysCecilia.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuração para carregar variáveis de ambiente do arquivo .env
 * Ativo APENAS para desenvolvimento local (profile "dev")
 * Em produção, usa variáveis de ambiente do sistema operacional
 */
@Configuration
// @Profile("dev")
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
                System.out.println("✅ Variáveis carregadas do arquivo .env (Ambiente: DEV)");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Erro ao carregar arquivo .env: " + e.getMessage());
            System.err.println("A aplicação continuará com as variáveis de ambiente do sistema.");
        }
    }
}

