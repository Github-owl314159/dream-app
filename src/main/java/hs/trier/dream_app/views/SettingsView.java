package hs.trier.dream_app.views;

import hs.trier.dream_app.Presenter;
import javafx.scene.layout.VBox;

public class SettingsView
{
    private Presenter presenter;
    private VBox content;

    public SettingsView()
    {
        this.presenter = Presenter.getPresenter();
        init();
    }

    public VBox getContent()
    {
        return content;
    }

    private void init()
    {
        content = new VBox();
    }

}
