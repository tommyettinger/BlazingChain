package blazing.chain.demo;

/**
 * Small interface to allow GWT to copy out of the application. Null values should be tolerated for desktop/Android.
 * Created by Tommy Ettinger on 9/25/2016.
 */
public interface Copier {
    void copy(CharSequence data);
    void clear();
}
