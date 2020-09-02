import model.manager.StockManager;
import model.manager.StockManagerImpl;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;

/**
 * Hello world!
 *
 */
@ApplicationPath("/")
public class App extends ResourceConfig
{
    public App() {
        register(rest.Resource.class);
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(StockManagerImpl.class)
                        .to(StockManager.class)
                        .in(Singleton.class);
            }
        });
    }
}
