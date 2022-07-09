package hs.trier.dream_app.views;

import hs.trier.dream_app.Presenter;
import javafx.scene.layout.VBox;

public class SearchView
{
    private Presenter presenter;
    private VBox content, metaContent;

    public SearchView()
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

    public VBox getMetaContent()
    {
        return metaContent;
    }
}
