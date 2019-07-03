package com.badlogic.gdx.backends.gwt;

import com.badlogic.gdx.*;
import com.badlogic.gdx.utils.Clipboard;
import com.badlogic.gdx.utils.ObjectMap;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Created by Tommy Ettinger on 10/24/2016.
 */
public abstract class GwtBareApp implements EntryPoint, Application {
    private ObjectMap<String, Preferences> prefs = new ObjectMap<String, Preferences>();
    public GwtApplicationConfiguration config;
    public VerticalPanel root;
    private ApplicationLogger applicationLogger;
    private int logLevel = LOG_ERROR;
    public abstract void start();
    public abstract GwtApplicationConfiguration getConfig();
    @Override
    public void onModuleLoad () {
        this.config = getConfig();
        applicationLogger = new GwtApplicationLogger(this.config.log);
        Element element = Document.get().getElementById("embed-html");
        if (element == null) {
            VerticalPanel panel = new VerticalPanel();
            panel.setWidth("" + config.width + "px");
            panel.setHeight("" + config.height + "px");
            panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
            RootPanel.get().add(panel);
            RootPanel.get().setWidth("" + config.width + "px");
            RootPanel.get().setHeight("" + config.height + "px");
            this.root = panel;
        } else {
            VerticalPanel panel = new VerticalPanel();
            panel.setWidth("" + config.width + "px");
            panel.setHeight("" + config.height + "px");
            panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
            element.appendChild(panel.getElement());
            root = panel;
        }
        Gdx.app = this;
        start();
    }
    public Preferences getPreferences (String name) {
        Preferences pref = prefs.get(name);
        if (pref == null) {
            pref = new GwtPreferences(name);
            prefs.put(name, pref);
        }
        return pref;
    }

    /**
     * @return what {@link ApplicationType} this application has, e.g. Android or Desktop
     */
    @Override
    public ApplicationType getType() {
        return ApplicationType.WebGL;
    }

    /**
     * @return the {@link ApplicationListener} instance
     */
    @Override
    public ApplicationListener getApplicationListener() {
        return null;
    }

    /**
     * @return the {@link Graphics} instance
     */
    @Override
    public Graphics getGraphics() {
        return null;
    }

    /**
     * @return the {@link Audio} instance
     */
    @Override
    public Audio getAudio() {
        return null;
    }

    /**
     * @return the {@link Input} instance
     */
    @Override
    public Input getInput() {
        return null;
    }

    /**
     * @return the {@link Files} instance
     */
    @Override
    public Files getFiles() {
        return null;
    }

    /**
     * @return the {@link Net} instance
     */
    @Override
    public Net getNet() {
        return null;
    }

    @Override
    public void log (String tag, String message) {
        if (logLevel >= LOG_INFO) getApplicationLogger().log(tag, message);
    }

    @Override
    public void log (String tag, String message, Throwable exception) {
        if (logLevel >= LOG_INFO) getApplicationLogger().log(tag, message, exception);
    }

    @Override
    public void error (String tag, String message) {
        if (logLevel >= LOG_ERROR) getApplicationLogger().error(tag, message);
    }

    @Override
    public void error (String tag, String message, Throwable exception) {
        if (logLevel >= LOG_ERROR) getApplicationLogger().error(tag, message, exception);
    }

    @Override
    public void debug (String tag, String message) {
        if (logLevel >= LOG_DEBUG) getApplicationLogger().debug(tag, message);
    }

    @Override
    public void debug (String tag, String message, Throwable exception) {
        if (logLevel >= LOG_DEBUG) getApplicationLogger().debug(tag, message, exception);
    }

    @Override
    public void setLogLevel (int logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public int getLogLevel() {
        return logLevel;
    }

    /**
     * @return the Android API level on Android, the major OS version on iOS (5, 6, 7, ..), or 0 on the desktop.
     */
    @Override
    public int getVersion() {
        return 0;
    }

    /**
     * @return the Java heap memory use in bytes
     */
    @Override
    public long getJavaHeap() {
        return 0;
    }

    /**
     * @return the Native heap memory use in bytes
     */
    @Override
    public long getNativeHeap() {
        return 0;
    }

    @Override
    public Clipboard getClipboard() {
        return null;
    }

    /**
     * Posts a {@link Runnable} on the main loop thread.
     *
     * @param runnable the runnable.
     */
    @Override
    public void postRunnable(Runnable runnable) {

    }

    /**
     * Schedule an exit from the application. On android, this will cause a call to pause() and dispose() some time in the future,
     * it will not immediately finish your application.
     * On iOS this should be avoided in production as it breaks Apples guidelines
     */
    @Override
    public void exit() {

    }

    /**
     * Adds a new {@link LifecycleListener} to the application. This can be used by extensions to hook into the lifecycle more
     * easily. The {@link ApplicationListener} methods are sufficient for application level development.
     *
     * @param listener
     */
    @Override
    public void addLifecycleListener(LifecycleListener listener) {

    }

    /**
     * Removes the {@link LifecycleListener}.
     *
     * @param listener
     */
    @Override
    public void removeLifecycleListener(LifecycleListener listener) {

    }

    @Override
    public void setApplicationLogger(ApplicationLogger applicationLogger) {
        this.applicationLogger = applicationLogger;
    }

    @Override
    public ApplicationLogger getApplicationLogger() {
        return applicationLogger;
    }
}
