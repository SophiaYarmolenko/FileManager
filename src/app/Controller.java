package app;

import controllers.FileTreeCell;
import controllers.FileTreeItem;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import utils.FileAddition;

import java.util.Collection;
import java.util.LinkedList;

public class Controller
{
    private TreeView<FileAddition> DfileView;
    private FileTreeItem DfileTreeItem;
    private TreeView<FileAddition> CfileView;
    private FileTreeItem CfileTreeItem;

    private TreeView<FileAddition> DfileSearchView;
    private TreeView<FileAddition> CfileSearchView;

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private ScrollPane CfileScrollPane;

    @FXML
    private ScrollPane DfileScrollPane;

    @FXML
    private ScrollPane СfileSearchScrollPane;

    @FXML
    private ScrollPane DfileSearchScrollPane;

    @FXML
    private Button CSearchButton;

    @FXML
    private TextField CSearchTextField;

    @FXML
    private TextField DSearchTextField;

    @FXML
    private Button DSearchButton;

    @FXML
    void initialize()
    {
        initializeC();
        initializeD();
    }

    private void initializeC()
    {
        FileAddition CfileRoot = new FileAddition("C:/");
        CfileTreeItem = new FileTreeItem(CfileRoot);
        CfileView = new TreeView<>(CfileTreeItem);
        CfileView.setShowRoot(false);
        CfileView.setEditable(true);
        CfileView.setCellFactory(param -> new FileTreeCell());
        CfileScrollPane.setContent(CfileView);
        CfileScrollPane.setFitToHeight(true);
        CfileScrollPane.setFitToWidth(true);
    }

    private void initializeD()
    {
        FileAddition DfileRoot = new FileAddition("D:/");
        DfileTreeItem = new FileTreeItem(DfileRoot);
        DfileView = new TreeView<>(DfileTreeItem);
        DfileView.setShowRoot(false);
        DfileView.setEditable(true);
        DfileView.setCellFactory(param -> new FileTreeCell());
        DfileScrollPane.setContent(DfileView);
        DfileScrollPane.setFitToHeight(true);
        DfileScrollPane.setFitToWidth(true);
    }

    private void search()
    {
        CfileSearchView = new TreeView<>();


        search();
        CfileScrollPane.setContent(CfileSearchView);
    }

    private void search(TreeItem<FileAddition> item)
    {
        ObservableList<FileAddition> list = FXCollections.observableArrayList();
            if(item.isLeaf())
                //CfileSearchView.setCellFactory(param->new FileTreeCell());
        CfileSearchView.setCellFactory(list);
    }

}