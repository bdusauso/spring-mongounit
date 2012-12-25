package be.codinsanity.spring.mongounit;

import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

/**
 * Created by Bruno Dusausoy
 */
@ContextConfiguration("classpath:applicationContext-test.xml")
public class DefaultDataLoaderTest extends AbstractJUnit4SpringContextTests{

    @Test
    public void test() throws Exception {
        DefaultDataLoader loader = new DefaultDataLoader();
        DataSetConfiguration configuration = DataSetConfiguration.Builder.newDataSetConfiguration()
                .withDataSetResourceLocations(new String[] {"/test.json"})
                .build();
        loader.executeBefore(applicationContext, configuration);
    }
}
