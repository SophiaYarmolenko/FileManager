package app;

import controllers.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import utils.FileAddition;

import java.io.File;
import java.net.MalformedURLException;
import java.util.HashMap;


public class Controller
{
    private TreeView<FileAddition> DfileView;
    private FileTreeItem DfileTreeItem;
    private TreeView<FileAddition> CfileView;
    private FileTreeItem CfileTreeItem;
    private static final int SCROLL_PANE_PREF_WIDTH = 350;
    private static final int VIEW_ITEM_PREF_HEIGHT = 60;
    public static HashMap<String, String> DfolderList = new HashMap<>();
    public static HashMap<String, String> CfolderList = new HashMap<>();

    @FXML
    private AnchorPane mainAnchorPane;

    @FXML
    private ScrollPane CfileScrollPane;

    @FXML
    private ScrollPane DfileScrollPane;

    @FXML
    private Button CSearchButton;

    @FXML
    private TextField CSearchTextField;

    @FXML
    private TextField DSearchTextField;

    @FXML
    private Button DSearchButton;

    @FXML
    private ScrollPane DfileSearchScrollPane;

    @FXML
    private MenuButton DFolderMenu;

    @FXML
    private ScrollPane CfileSearchScrollPane;

    @FXML
    private MenuButton CFolderMenu;

    @FXML
    void initialize() throws MalformedURLException
    {
        DfolderList.clear();
        CfolderList.clear();
        initializeC();
        initializeD();
        setDFolderMenu();
        setCFolderMenu();
        DSearchButton.setOnAction(actionEvent ->
        {
            String DFolderToSearch = DFolderMenu.getText();
            searchInD(DFolderToSearch);
        });
        CSearchButton.setOnAction(actionEvent ->
        {
            String CFolderToSearch = CFolderMenu.getText();
            searchInC(CFolderToSearch);
        });
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
    private void searchInD(String DFolderToSearch)
    {
        String limitation = DSearchTextField.getText();
        search(DfileSearchScrollPane, new FileAddition(new File(DfolderList.get(DFolderToSearch))), limitation);
    }
    private void setDFolderMenu()
    {
        for(String folder:DfolderList.keySet())
        {
            if(folder.equals(""))
                continue;
            MenuItem item = new MenuItem(folder);
            item.setOnAction(actionEvent -> { DFolderMenu.setText(folder); }
            );
            DFolderMenu.getItems().add(item);
        }
    }

    private void searchInC(String CFolderToSearch)
    {
        String limitation = CSearchTextField.getText();
        search(CfileSearchScrollPane, new FileAddition(new File(CfolderList.get(CFolderToSearch))), limitation);
    }

    private void setCFolderMenu()
    {
        for(String folder:CfolderList.keySet())
        {
            if(folder.equals(""))
                continue;
            MenuItem item = new MenuItem(folder);
            item.setOnAction(actionEvent -> { CFolderMenu.setText(folder); }
            );
            CFolderMenu.getItems().add(item);
        }
    }

    private void search(ScrollPane fileSearchScrollPane, FileAddition fileRootSearch, String limitation)
    {
        FileTreeItem fileTreeItemSearch = new FileTreeItem(fileRootSearch);
        FileSearcher fileSearcher = new FileSearcher(fileTreeItemSearch, limitation);
        final FlowPane container = new FlowPane();
        container.setPrefWidth(SCROLL_PANE_PREF_WIDTH);

        for (FileTreeItem fileItem : fileSearcher.search())
        {
            TreeView<FileAddition> fileSearchView = new TreeView<>(fileItem);
            fileSearchView.setShowRoot(true);
            fileSearchView.setEditable(true);
            fileSearchView.setCellFactory(param -> new FileTreeCell());
            fileSearchView.setPrefWidth(SCROLL_PANE_PREF_WIDTH);
            fileSearchView.setPrefHeight(VIEW_ITEM_PREF_HEIGHT);
            container.getChildren().add(fileSearchView);
        }

        fileSearchScrollPane.setContent(container);
        fileSearchScrollPane.contentProperty();
        fileSearchScrollPane.setFitToHeight(true);
        fileSearchScrollPane.setFitToWidth(true);
    }
}