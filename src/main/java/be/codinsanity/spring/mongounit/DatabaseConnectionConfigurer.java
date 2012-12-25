package be.codinsanity.spring.mongounit;

/**
 * Created by Bruno Dusausoy
 */
public interface DatabaseConnectionConfigurer {
    void configure(DatabaseConfig databaseConfig);
}
