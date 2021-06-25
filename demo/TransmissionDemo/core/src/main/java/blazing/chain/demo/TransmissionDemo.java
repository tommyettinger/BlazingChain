package blazing.chain.demo;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.VisUI.SkinScale;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextArea;
import com.kotcrab.vis.ui.widget.VisTextButton;

import static blazing.chain.LZSEncoding.*;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class TransmissionDemo extends ApplicationAdapter {
    private Stage stage;
    public VisTextArea currentArea, compressedArea;
    public VisLabel allGood;
    public Copier copier;
    public Preferences preferences;
    public TransmissionDemo(Copier cop)
    {
        copier = cop;
    }
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
    public String compressedText, currentText;

    @Override
    public void create() {
        VisUI.load(SkinScale.X1);
        preferences = Gdx.app.getPreferences("blazingchain");
        currentText = preferences.getString("normal", "");
        compressedText = preferences.getString("compressed", "");
        if(currentText == null || currentText.isEmpty()) 
            currentText = mars;
        if(compressedText == null || compressedText.isEmpty())
        {
            compressedText = compressToUTF16(currentText);
        }
        byte[] bc = LZByteEncoding.compressToBytes(currentText);
        System.out.println(LZByteEncoding.join(bc));
        System.out.println();
        System.out.println(LZByteEncoding.decompressFromBytes(bc));

        stage = new Stage(new ScreenViewport());

        VisTable root = new VisTable();
        root.setFillParent(true);
        stage.addActor(root);

        currentArea = new VisTextArea(currentText);
        currentArea.setOnlyFontChars(false);
        currentArea.setPrefRows(16);
        currentArea.setWidth(460);
        currentArea.setHeight(450);
        compressedArea = new VisTextArea();
        compressedArea.setOnlyFontChars(false);
        compressedArea.setPrefRows(16);
        compressedArea.setWidth(460);
        compressedArea.setHeight(450);
        root.add("Text de/compression demo").colspan(2).top().pad(10).row();
        root.add(currentArea).bottom().fillX().expandX().pad(10);
        root.add(compressedArea).bottom().fillX().expandX().pad(10).row();
        compressedArea.setText(compressedText);

        final VisTextButton compressButton = new VisTextButton("Compress ->");
        compressButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                compressedText = compressToUTF16(currentText = currentArea.getText());
                compressedArea.setText(compressedText);
                preferences.putString("normal", currentText);
                preferences.putString("compressed", compressedText);
                preferences.flush();
                if(copier != null)
                {
                    copier.clear();
                    copier.copy(currentText);
                    copier.copy("\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
                    copier.copy(compressedText);
                }
            }
        });
        root.add(compressButton).pad(10);

        final VisTextButton decompressButton = new VisTextButton("<- Decompress");
        decompressButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentText = decompressFromUTF16(compressedText = compressedArea.getText());
                currentArea.setText(currentText);
                if(compressToUTF16(currentText).equals(compressedText)) {
                    allGood.setText("All Good!");
                    allGood.setColor(Color.GREEN);
                }
                else{
                    allGood.setText("No Match!");
                    allGood.setColor(Color.RED);
                }
                preferences.putString("normal", currentText);
                preferences.putString("compressed", compressedText);
                preferences.flush();
                if(copier != null)
                {
                    copier.clear();
                    copier.copy(currentText);
                    copier.copy("\n\n~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");
                    copier.copy(compressedText);
                }
            }
        });

        root.add(decompressButton).pad(10).row();

        VisTextButton loadUtf16 = new VisTextButton("Load decompressed text", new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                currentText = mars;
                currentArea.setText(currentText);
                compressedText = Gdx.files.internal("PrincessOfMarsUTF16.txt").readString("UTF16");
                compressedArea.setText(compressedText);
                if(decompressFromUTF16(compressedText).equals(currentText)){
                        allGood.setText("All Good!");
                        allGood.setColor(Color.GREEN);
                }
                else {
                    allGood.setText("No Match!");
                    allGood.setColor(Color.RED);
                }
            }
        });
        root.add(loadUtf16).pad(10).row();

        allGood = new VisLabel("All Good!", Color.GREEN);
        root.add(allGood).pad(10);
        root.pack();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void dispose() {
        VisUI.dispose();
        stage.dispose();
    }
}