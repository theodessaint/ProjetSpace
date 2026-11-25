package station.config;

import javax.sql.DataSource;

import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.stereotype.Component;

@Component
public class SqlLoader {

    private final DataSource dataSource;

    public SqlLoader(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void loadSqlFile(String pathFile) {

        ClassPathResource resource = new ClassPathResource(pathFile);

        if (!resource.exists()) {
            throw new IllegalStateException(
                "Fichier SQL introuvable : " + pathFile
            );
        }

        ResourceDatabasePopulator populator =
            new ResourceDatabasePopulator(resource);

        populator.setSqlScriptEncoding("UTF-8");
        populator.execute(dataSource);
    }
}
