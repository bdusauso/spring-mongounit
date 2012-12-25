package be.codinsanity.spring.mongounit;

/**
 * Created by Bruno Dusausoy
 */
public class JSonDataSet implements IDataSet {

    private String[] collectionNames;

    @Override
    public String[] getCollectionNames() {
        return collectionNames;
    }
}
