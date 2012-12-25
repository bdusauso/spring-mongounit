package be.codinsanity.spring.mongounit;

import be.codinsanity.spring.mongounit.convention.ConfigurationConventions;
import be.codinsanity.spring.mongounit.convention.DefaultConfigurationConventions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.test.context.TestContext;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

/**
 * @author <a href="mailto:slandelle@excilys.com">Stephane LANDELLE</a>
 */
public class TestConfigurationProcessor implements ConfigurationProcessor<TestContext> {

    /**
     * The logger
     */
    protected final Logger LOGGER = LoggerFactory.getLogger(getClass());

    private final ConfigurationConventions conventions;

    /**
     * A configuration cache used between setup and teardown
     */
    protected final Map<Method, DataSetConfiguration> configurationCache = Collections.synchronizedMap(new IdentityHashMap<Method, DataSetConfiguration>());

    /**
     * Configure with default conventions
     */
    public TestConfigurationProcessor() {
        conventions = new DefaultConfigurationConventions();
    }

    /**
     * Configure with custom conventions
     *
     * @param conventions
     */
    public TestConfigurationProcessor(ConfigurationConventions conventions) {
        this.conventions = conventions;
    }

    /**
     * @param testContext
     *            the context
     * @return the configuration
     * @throws java.io.IOException
     *             I/O failureDataSet
     * @throws DatabaseUnitException
     *             DBUnit failure
     */
    public DataSetConfiguration getConfiguration(TestContext testContext) throws IOException {

        DataSetConfiguration configuration = configurationCache.get(testContext.getTestMethod());

        if (configuration == null) {
            // no cached configuration --> instancing it
            DataSet dataSetAnnotation = findAnnotation(testContext.getTestMethod(), testContext.getTestClass(), DataSet.class);

            if (dataSetAnnotation != null) {
                configuration = buildConfiguration(dataSetAnnotation, testContext);
                configurationCache.put(testContext.getTestMethod(), configuration);

            } else {
                LOGGER.info("DataSetTestExecutionListener was configured but without any DataSet or DataSets! DataSet features are disabled");
            }
        }

        return configuration;
    }

    protected final DataSetConfiguration buildConfiguration(DataSet annotation, TestContext testContext) throws IOException {

        String[] dataSetResourceLocations = getResourceLocationsByConventions(annotation, testContext);

        return DataSetConfiguration.Builder.newDataSetConfiguration()
                .withDataSetResourceLocations(dataSetResourceLocations)
                .withDataSourceSpringName(StringUtils.hasText(annotation.dataSourceSpringName()) ? annotation.dataSourceSpringName() : null)
                .withSetUpOp(annotation.setup())
                .withTearDownOp(annotation.tearDown())
                .build();
    }

    /**
     * @param locations
     *            the possibly numerous files
     * @param testContext
     *            the context
     * @return an Inputstream, possibly the concatenation of several files
     * @throws IOException
     *             I/O failure
     */
    private String[] getResourceLocationsByConventions(DataSet annotation, TestContext testContext) throws IOException {

        String[] valueLocations = annotation.value();
        String[] locations = annotation.locations();
        if (!ObjectUtils.isEmpty(valueLocations)) {
            locations = valueLocations;
        }

        return conventions.getDataSetResourcesLocations(testContext.getTestClass(), locations);
    }

    /**
     * @param method
     *            the test method
     * @param clazz
     *            the test class
     * @return the {@link DataSet} at method level if found, otherwise at class
     *         level
     */
    private <A extends Annotation> A findAnnotation(Method method, Class<?> clazz, Class<A> annotationType) {
        A annotation = AnnotationUtils.findAnnotation(method, annotationType);
        return annotation == null ? annotation = AnnotationUtils.findAnnotation(clazz, annotationType) : annotation;
    }
}
