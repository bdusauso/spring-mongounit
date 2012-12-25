package be.codinsanity.spring.mongounit.convention;

import static org.springframework.core.io.support.ResourcePatternUtils.isUrl;
import static org.springframework.util.ClassUtils.classPackageAsResourcePath;
import static org.springframework.util.ObjectUtils.isEmpty;
import static org.springframework.util.StringUtils.cleanPath;
import static org.springframework.util.StringUtils.hasLength;

import org.springframework.util.ResourceUtils;

public class DefaultConfigurationConventions implements ConfigurationConventions {

    /**
     * Default resource name = "dataSet.json"
     */
    private static final String DEFAULT_RESOURCE_NAME = "dataSet.json";

    /**
     * If the supplied <code>location</code> is <code>null</code> or
     * <em>empty</em>, a default location will be
     * {@link #getDefaultLocationByConventions(Class) generated} for the
     * specified {@link Class class} and the {@link #getResourceName resource
     * file name} ; otherwise, the supplied <code>location</code> will be
     * {@link #modifyLocation modified} if necessary and returned.
     *
     * @param clazz
     *            the class with which the location is associated: to be used
     *            when generating a default location
     * @param locations
     *            the raw locations to use for loading the DataSet (can be
     *            <code>null</code> or empty)
     * @return DataSets locations
     * @see #generateDefaultLocation
     * @see #modifyLocation
     */
    public String[] getDataSetResourcesLocations(Class<?> clazz, String[] locations) {

        if (isEmpty(locations)) {
            locations = new String[] { null };
        }

        String[] resourceLocations = new String[locations.length];

        for (int i = 0; i < locations.length; i++) {
            String resourceLocation = !hasLength(locations[i]) ? getDefaultLocationByConventions(clazz) : getRealLocationByConventions(clazz, locations[i]);
            resourceLocations[i] = resourceLocation;
        }

        return resourceLocations;
    }

    /**
     * Generate a modified version of the supplied location and returns it.
     * <p>
     * A plain path, e.g. &quot;myDataSet.xml&quot;, will be treated as a
     * classpath resource from the same package in which the specified class is
     * defined. A path starting with a slash is treated as a fully qualified
     * class path location, e.g.: &quot;/com/example/whatever/foo.xml&quot;. A
     * path which references a URL (e.g., a path prefixed with
     * {@link ResourceUtils#CLASSPATH_URL_PREFIX classpath:},
     * {@link ResourceUtils#FILE_URL_PREFIX file:}, <code>http:</code>, etc.)
     * will be added to the results unchanged.
     * <p>
     * Subclasses can override this method to implement a different
     * <em>location modification</em> strategy.
     *
     * @param clazz
     *            the class with which the locations are associated
     * @param locations
     *            the resource location to be modified
     * @return the modified application context resource location
     */
    private String getRealLocationByConventions(Class<?> clazz, String location) {
        String modifiedLocation = null;
        if (location.startsWith("/")) {
            modifiedLocation = ResourceUtils.CLASSPATH_URL_PREFIX + location;
        } else if (!isUrl(location)) {
            modifiedLocation = ResourceUtils.CLASSPATH_URL_PREFIX + cleanPath(classPackageAsResourcePath(clazz) + "/" + location);
        } else {
            modifiedLocation = cleanPath(location);
        }
        return modifiedLocation;
    }

    /**
     * Generates the default classpath resource location based on the supplied
     * class.
     * <p>
     * For example, if the supplied class is <code>com.example.MyTest</code>,
     * the generated location will be a string with a value of
     * &quot;classpath:/com/example/<code>&lt;suffix&gt;</code>&quot;, where
     * <code>&lt;suffix&gt;</code> is the value of the
     * {@link #getResourceName() resource name} string.
     * <p>
     * Subclasses can override this method to implement a different
     * <em>default location generation</em> strategy.
     *
     * @param clazz
     *            the class for which the default locations are to be generated
     * @return an array of default application context resource locations
     * @see #getResourceSuffix()
     */
    private String getDefaultLocationByConventions(Class<?> clazz) {

        StringBuilder builder = new StringBuilder();
        builder.append(ResourceUtils.CLASSPATH_URL_PREFIX);
        builder.append(cleanPath(classPackageAsResourcePath(clazz)));
        builder.append("/");
        builder.append(DEFAULT_RESOURCE_NAME);
        return builder.toString();
    }
}