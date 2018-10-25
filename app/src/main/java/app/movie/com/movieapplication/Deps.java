package app.movie.com.movieapplication;

import javax.inject.Singleton;
import dagger.Component;


/**
 * This Interface Contains the Dependencies Needed By All Activities.
 *
 */

@Singleton
@Component(modules = {NetworkModule.class})
public interface Deps {

    /**
     * This Method Injects the Dependencies in Defined Activities.
     *
     * @param activity
     */
    void inject(MovieListActivity activity);

    void inject(MovieDetailsActivity activity);
}