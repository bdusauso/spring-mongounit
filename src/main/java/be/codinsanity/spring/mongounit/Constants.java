package be.codinsanity.spring.mongounit;

/**
 * Created by Bruno Dusausoy
 */
public interface Constants {

    public static class ConfigurationDefaults {
        public static final DBOperation DEFAULT_SETUP_OPERATION = DBOperation.CLEAN_INSERT;
        public static final DBOperation DEFAULT_TEARDOWN_OPERATION = DBOperation.NONE;
    }
}
