package be.codinsanity.spring.mongounit;

import org.springframework.context.ApplicationContext;

/**
 * @author <a href="mailto:slandelle@excilys.com">Stephane LANDELLE</a>
 */
public interface DataLoader {

    void executeBefore(ApplicationContext applicationContext, DataSetConfiguration configuration) throws Exception;

    void executeAfter(ApplicationContext applicationContext, DataSetConfiguration configuration) throws Exception;
}
