package be.codinsanity.spring.mongounit.convention;

/**
 * Configuration conventions
 *
 * @author <a href="mailto:slandelle@excilys.com">Stephane LANDELLE</a>
 */
public interface ConfigurationConventions {

    /**
     * @param clazz
     *            the JUnit test class
     * @param locations
     *            the locations parameter as specified in the DataSet annotation
     * @return the corresponding resource locations
     */
    String[] getDataSetResourcesLocations(Class<?> clazz, String[] locations);
}