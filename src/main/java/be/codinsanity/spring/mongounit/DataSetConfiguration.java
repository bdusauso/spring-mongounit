package be.codinsanity.spring.mongounit;

import org.springframework.util.Assert;

import static be.codinsanity.spring.mongounit.Constants.ConfigurationDefaults;

/**
 * Created by Bruno Dusausoy
 */
public class DataSetConfiguration implements DatabaseConnectionConfigurer {

    private String dataSourceSpringName;
    private DBOperation setUpOperation[] = new DBOperation[] { ConfigurationDefaults.DEFAULT_SETUP_OPERATION };
    private DBOperation tearDownOperation[] = new DBOperation[] { ConfigurationDefaults.DEFAULT_TEARDOWN_OPERATION };
    private String[] dataSetResourceLocations = new String[] { "classpath:dataSet.json" };


    public static class Builder {

        private DataSetConfiguration configuration = new DataSetConfiguration();

        public static Builder newDataSetConfiguration() {
            return new Builder();
        }

        public Builder withDataSourceSpringName(String dataSourceSpringName) {
            configuration.dataSourceSpringName = dataSourceSpringName;
            return this;
        }

        public Builder withSetUpOp(DBOperation[] setUpOp) {
            configuration.setUpOperation = setUpOp;
            return this;
        }

        public Builder withTearDownOp(DBOperation[] tearDownOp) {
            configuration.tearDownOperation = tearDownOp;
            return this;
        }

        public Builder withDataSetResourceLocations(String[] dataSetResourceLocations) {
            configuration.dataSetResourceLocations = dataSetResourceLocations;
            return this;
        }

        public DataSetConfiguration build() {
            Assert.notNull(configuration.dataSetResourceLocations, "dataSetResourceLocations is required");
            Assert.notNull(configuration.setUpOperation, "setUpOperation is required");
            Assert.notNull(configuration.tearDownOperation, "tearDownOperation is required");

            return configuration;
        }
    }

    public String getDataSourceSpringName() {
        return dataSourceSpringName;
    }

    public DBOperation[] getSetUpOperation() {
        return setUpOperation;
    }

    public DBOperation[] getTearDownOperation() {
        return tearDownOperation;
    }

    public String[] getDataSetResourceLocations() {
        return dataSetResourceLocations;
    }

    @Override
    public void configure(DatabaseConfig databaseConfig) {

    }
}
