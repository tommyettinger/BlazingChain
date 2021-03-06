package blazing.chain.demo.gwt;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.*;

import static blazing.chain.LZSEncoding.*;

/**
 * Transmission app in pure GWT so users can copy/paste in and out of it.
 * Created by Tommy Ettinger on 10/24/2016.
 */
public class GwtTransmissionDemo extends GwtApplication implements ApplicationListener {
    public Panel root;

    public TextArea currentArea, compressedArea;
    public Preferences preferences;
    public String compressedText, currentText;
    public Label timeTaken;
    public static final String
            mars = "I have never told this story, nor shall mortal man see this manuscript until after I have passed\n" +
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
        GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(1000, 1);
        cfg.disableAudio = true;
        cfg.alpha = true;
        return cfg;
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public ApplicationListener createApplicationListener() {
        return this;
    }

    @Override
    public void create() {
        {
            this.root = getRootPanel();
            preferences = Gdx.app.getPreferences("blazingchain");
            currentText = preferences.getString("normal", "");
            compressedText = preferences.getString("compressed", "");
            if (currentText == null || currentText.isEmpty()) currentText = mars;
            if (compressedText == null || compressedText.isEmpty()) compressedText = compressToBase64(currentText);

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

            timeTaken = new Label("");

            HorizontalPanel buttonRow = new HorizontalPanel();
            HorizontalPanel buttonRow2 = new HorizontalPanel();
            final PushButton compressButton = new PushButton("Compress ->", "Compress ->");
            compressButton.setSize("300px", "28px");
            compressButton.setText("Compress ->");
            compressButton.setEnabled(true);
            compressButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    long currentTime = System.currentTimeMillis();
                    compressedText = compressToUTF16(currentText = currentArea.getText());
                    timeTaken.setText("Took " + (System.currentTimeMillis() - currentTime) + " to compress");
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
                    long currentTime = System.currentTimeMillis();
                    currentText = decompressFromUTF16(compressedText = compressedArea.getText());
                    timeTaken.setText("Took " + (System.currentTimeMillis() - currentTime) + " to decompress");
                    currentArea.setText(currentText);
                    preferences.putString("normal", currentText);
                    preferences.putString("compressed", compressedText);
                    preferences.flush();
                }
            });
            buttonRow.add(decompressButton);
//        final PushButton compressButtonOld = new PushButton("Compress ??", "Compress ??");
//        compressButtonOld.setSize("300px", "28px");
//        compressButtonOld.setText("Compress ??");
//        compressButtonOld.setEnabled(true);
//        compressButtonOld.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                long currentTime = System.currentTimeMillis();
//                compressedText = LZSEncodingOriginal.compressToUTF16(currentText = currentArea.getText());
//                timeTaken.setText("Took " + (System.currentTimeMillis() - currentTime) + " to compress");
//                compressedArea.setText(compressedText);
//                preferences.putString("normal", currentText);
//                preferences.putString("compressed", compressedText);
//                preferences.flush();
//            }
//        });
//        buttonRow2.add(compressButtonOld);
//        final PushButton decompressButtonOld = new PushButton("?? Decompress", "?? Decompress");
//        decompressButtonOld.setSize("300px", "28px");
//        decompressButtonOld.setText("?? Decompress");
//        decompressButtonOld.setEnabled(true);
//        decompressButtonOld.addClickHandler(new ClickHandler() {
//            @Override
//            public void onClick(ClickEvent event) {
//                long currentTime = System.currentTimeMillis();
//                currentText = LZSEncodingOriginal.decompressFromUTF16(compressedText = compressedArea.getText());
//                timeTaken.setText("Took " + (System.currentTimeMillis() - currentTime) + " to decompress");
//                currentArea.setText(currentText);
//                preferences.putString("normal", currentText);
//                preferences.putString("compressed", compressedText);
//                preferences.flush();
//            }
//        });
//        buttonRow2.add(decompressButtonOld);

            final PushButton marsButton = new PushButton("Mars", "MARS");
            marsButton.setSize("300px", "28px");
            marsButton.setText("Mars");
            marsButton.setEnabled(true);
            marsButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    currentText = mars;
                    currentArea.setText(currentText);
                }
            });

            final PushButton loadButton = new PushButton("Load Existing", "Load Existing");
            loadButton.setSize("300px", "28px");
            loadButton.setText("Load Existing");
            loadButton.setEnabled(true);
            loadButton.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    compressedText = Gdx.files.internal("PrincessOfMarsUTF16.txt").readString("UTF8");
                    compressedArea.setText(compressedText);
                }
            });
            buttonRow2.add(marsButton);
            buttonRow2.add(loadButton);
            RootPanel.get().sinkEvents(Event.ONCHANGE | Event.ONCLICK); //
            buttonRow.setWidth("1000px");
            buttonRow2.setWidth("1000px");
            buttonRow.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            buttonRow2.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//        root.add(buttonRow);
            timeTaken.setSize("600px", "30px");
            RootPanel.get("embed-html").add(buttonRow);
            RootPanel.get("embed-html").add(buttonRow2);
            root.add(timeTaken);
        }
    }
}
