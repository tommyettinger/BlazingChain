package blazing.chain.demo.gwt;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.badlogic.gdx.backends.gwt.GwtBareApp;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;

import static blazing.chain.LZSEncoding.compressToEncodedURIComponent;
import static blazing.chain.LZSEncoding.decompressFromEncodedURIComponent;

/**
 * Transmission app in pure GWT so users can copy/paste in and out of it.
 * Created by Tommy Ettinger on 10/24/2016.
 */
public class GwtTransmissionDemo extends GwtBareApp {

    public TextArea currentArea, compressedArea;
    public Preferences preferences;
    public String compressedText, currentText;
    public static final String
            mars =  "I have never told this story, nor shall mortal man see this manuscript until after I have passed\n" +
            "over for eternity. I know that the average human mind will not believe what it cannot grasp, and so\n" +
            "I do not purpose being pilloried by the public, the pulpit, and the press, and held up as a colossal\n" +
            "liar when I am but telling the simple truths which some day science will substantiate. Possibly the\n" +
            "suggestions which I gained upon Mars, and the knowledge which I can set down in this chronicle will\n" +
            "aid in an earlier understanding of the mysteries of our sister planet; mysteries to you, but no\n" +
            "longer mysteries to me.\n" +
            "\n" +
            "A Princess of Mars, Edgar Rice Burroughs\n";

    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(1000, 700);
    }

    public void start()
    {
        preferences = Gdx.app.getPreferences("blazingchain");
        currentText = preferences.getString("normal", "");
        compressedText = preferences.getString("compressed", "");
        if(currentText == null || currentText.isEmpty()) currentText = mars;
        if(compressedText == null || compressedText.isEmpty()) compressedText = compressToEncodedURIComponent(currentText);

        currentArea = new TextArea();
        currentArea.setText(currentText);
        currentArea.setVisibleLines(16);
        currentArea.setWidth("500px");
        currentArea.setHeight("500px");
        currentArea.setText(currentText);
        compressedArea = new TextArea();
        compressedArea.setVisibleLines(16);
        compressedArea.setWidth("500px");
        compressedArea.setHeight("500px");
        compressedArea.setText(compressedText);
        root.add(new Label("Text de/compression demo"));
        HorizontalPanel texts = new HorizontalPanel();
        texts.setSize("1000px", "500px");
        texts.add(currentArea);
        texts.add(compressedArea);
        root.add(texts);

        HorizontalPanel buttonRow = new HorizontalPanel();
        final PushButton compressButton = new PushButton("Compress ->", "Compress ->");
        compressButton.setSize("300px", "28px");
        compressButton.setText("Compress ->");
        compressButton.setEnabled(true);
        compressButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                compressedText = compressToEncodedURIComponent(currentText = currentArea.getText());
                compressedArea.setText(compressedText);
                preferences.putString("normal", currentText);
                preferences.putString("compressed", compressedText);
                preferences.flush();
            }
        });
        buttonRow.add(compressButton);
        final PushButton decompressButton = new PushButton("<- Decompress", "<- Decompress");
        decompressButton.setSize("300px", "28px");
        decompressButton.setText("<- Decompress");
        decompressButton.setEnabled(true);
        decompressButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                currentText = decompressFromEncodedURIComponent(compressedText = compressedArea.getText());
                currentArea.setText(currentText);
                preferences.putString("normal", currentText);
                preferences.putString("compressed", compressedText);
                preferences.flush();
            }
        });
        buttonRow.add(decompressButton);
        RootPanel.get().sinkEvents(Event.ONCLICK); //Event.ONCHANGE |
        buttonRow.setWidth("1000px");
        buttonRow.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        RootPanel.get("embed-html").add(buttonRow);
    }
}
