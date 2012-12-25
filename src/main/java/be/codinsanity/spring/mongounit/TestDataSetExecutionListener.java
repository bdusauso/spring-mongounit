package be.codinsanity.spring.mongounit;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

/**
 * Created by Bruno Dusausoy
 */
public class TestDataSetExecutionListener extends AbstractTestExecutionListener {

    private DataLoader dataLoader = new DefaultDataLoader();
    private TestConfigurationProcessor processor = new TestConfigurationProcessor();

    @Override
    public void afterTestMethod(TestContext testContext) throws Exception {
        dataLoader.executeBefore(testContext.getApplicationContext(), processor.getConfiguration(testContext));
    }

    @Override
    public void beforeTestMethod(TestContext testContext) throws Exception {
        dataLoader.executeBefore(testContext.getApplicationContext(), processor.getConfiguration(testContext));
    }
}
