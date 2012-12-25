package be.codinsanity.spring.mongounit;

/**
 * Created by Bruno Dusausoy
 */
public @interface DataSet {
    /**
     * alias for locations
     */
    String[] value() default {};

    /**
     * @return Dataset files locations
     */
    String[] locations() default {};

    /**
     * @return DataSource name in the Spring Context. If empty, expect one and
     *         only one DataSource in the Spring Context.
     */
    String dataSourceSpringName() default "";

    /**
     * @return DBOperation for setup.
     */
    DBOperation[] setup() default DBOperation.CLEAN_INSERT;

    /**
     *
     * @return DBOperation for teardown;
     */
    DBOperation[] tearDown() default DBOperation.DELETE;
}
