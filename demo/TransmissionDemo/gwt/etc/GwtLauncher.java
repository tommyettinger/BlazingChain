package com.github.SquidPony.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.github.SquidPony.BabelBobble;
import com.github.SquidPony.Copier;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.TextArea;

/** Launches the GWT application. */
public class GwtLauncher extends GwtApplication {
    public TextArea textBox;
    public GwtCopier copier;
    @Override
    public GwtApplicationConfiguration getConfig() {
        GwtApplicationConfiguration configuration = new GwtApplicationConfiguration(1000, 700);
        return configuration;
    }

    @Override
    public ApplicationListener createApplicationListener() {
        setLoadingListener(new LoadingListener() {
            @Override
            public void beforeSetup() {}

            @Override
            public void afterSetup() {
                textBox = new TextArea();
                textBox.getElement().getStyle().setPosition(Style.Position.RELATIVE);
                textBox.getElement().getStyle().setWidth(1000, Style.Unit.PX);
                textBox.getElement().getStyle().setHeight(500, Style.Unit.PX);
                textBox.getElement().getStyle().setDisplay(Style.Display.INLINE);
                textBox.getElement().getStyle().setTop(50, Style.Unit.PX);
                textBox.getElement().getStyle().setBackgroundColor("#664477");
                textBox.setEnabled(true);
                getRootPanel().add(textBox);
            }
        });
        return new BabelBobble(new GwtCopier());
    }

    public class GwtCopier implements Copier {
        public GwtCopier() {
        }

        @Override
        public void copy(CharSequence data) {
            textBox.setText(textBox.getText() + data.toString());
        }

        @Override
        public void clear() {
            textBox.setText("");
        }
    }
}