package be.codinsanity.spring.mongounit;

import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Bruno Dusausoy
 */
public class DefaultDataLoader implements DataLoader {

    private static Logger logger = LoggerFactory.getLogger(DefaultDataLoader.class);

    private static final String COLLECTIONS = "collections";

    private static final ResourcePatternResolver RESOURCE_LOADER = new PathMatchingResourcePatternResolver();

    private MongoTemplate template;

    private DB db;

    private Gson gson = new Gson();

    @Override
    public void executeBefore(ApplicationContext applicationContext, DataSetConfiguration configuration) throws Exception {
        String[] locations = configuration.getDataSetResourceLocations();
        String location = locations[0];
        Resource resource = RESOURCE_LOADER.getResource(location);

        DBObject dataset = (DBObject) JSON.parse(FileUtils.readFileToString(resource.getFile()));
        logger.debug("{}", dataset);

        template = applicationContext.getBean(MongoTemplate.class);
        db = template.getDb();

        for (DBObjectWrapper objectWrapper : createDBObjectWrappers(dataset)) {
            objectWrapper.collection.insert(objectWrapper.object);
        }
    }

    @Override
    public void executeAfter(ApplicationContext applicationContext, DataSetConfiguration configuration) throws Exception {

    }

    private Collection<DBObjectWrapper> createDBObjectWrappers(DBObject dataset) {
        BasicDBList collections = (BasicDBList) dataset.get(COLLECTIONS);
        Set<DBObjectWrapper> objectWrappers = new HashSet<DBObjectWrapper>();

        for (Object collection : collections) {
            DBObject obj = (DBObject) collection;
            DBCollection dbColl = db.getCollection((String) obj.get("collection"));
            BasicDBList objects = (BasicDBList) obj.get("objects");
            for (Object object : objects) {
                objectWrappers.add(new DBObjectWrapper(dbColl, (DBObject) object));
            }
        }

        return objectWrappers;
    }

    private static class DBObjectWrapper {
        public final DBCollection collection;
        public final DBObject object;

        private DBObjectWrapper(DBCollection collection, DBObject object) {
            this.collection = collection;
            this.object = object;
        }
    }
}
