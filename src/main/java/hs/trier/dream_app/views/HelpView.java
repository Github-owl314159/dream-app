package hs.trier.dream_app.views;

import hs.trier.dream_app.Presenter;
import javafx.scene.layout.VBox;

public class HelpView
{
    private Presenter presenter;
    private VBox content;

    public HelpView()
    {
        this.presenter = Presenter.getPresenter();
        init();
    }

    private void init()
    {
        content = new VBox();
    }

    public VBox getContent()
    {
        return content;
    }
}
