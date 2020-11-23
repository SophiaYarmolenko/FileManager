package controllers;
import app.Controller;
import utils.Icons;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.image.ImageView;
import utils.FileAddition;

import java.io.File;
import java.net.MalformedURLException;

public class FileTreeItem extends TreeItem<FileAddition>
{
    private Boolean hasLoadedChilds;

    public FileTreeItem(FileAddition value)
    {
        super(value);
        hasLoadedChilds = false;
        if(!isLeaf())
        {
            if(getValue().getAbsolutePath().substring(0,1).equals("D"))
            {
                Controller.DfolderList.put(getValue().getName(), getValue().getAbsolutePath());
            }
            else
                {
                    Controller.CfolderList.put(getValue().getName(), getValue().getAbsolutePath());
                }
        }
    }

    public FileTreeItem(FileAddition value, Node graphic)
    {
        super(value, graphic);
        hasLoadedChilds = false;
        if(!isLeaf())
        {
            if(getValue().getAbsolutePath().substring(0,1).equals("D"))
            {
                Controller.DfolderList.put(getValue().getName(), getValue().getAbsolutePath());
            }
            else
            {
                Controller.CfolderList.put(getValue().getName(), getValue().getAbsolutePath());
            }
        }
    }

    @Override
    public boolean isLeaf()
    {
        return getValue().isFile();
    }

    @Override
    public ObservableList< TreeItem<FileAddition> > getChildren()
    {
        if (!hasLoadedChilds)
        {
                hasLoadedChilds = true;
                try
                {
                    addChildes();
                }
                catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
                try
                {
                    setGraphic(Icons.openFolder());
                }
                catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }

        }
        return super.getChildren();
    }

    public void addNewChild(FileAddition file) throws MalformedURLException
    {
        if (hasLoadedChilds)
        {
            FileTreeItem newFolderItem = createItem(file);
            super.getChildren().addAll(newFolderItem);
        }
    }

    public void removeChildItem(FileAddition file)
    {
        TreeItem<FileAddition> itemToRemove = null;
        for (TreeItem<FileAddition> fileExtensionTreeItem : getChildren())
        {
            String fileName = fileExtensionTreeItem.getValue().getFile().getName();
            if (file.getName().equals(fileName))
            {
                itemToRemove = fileExtensionTreeItem;
                break;
            }
        }

        if (itemToRemove != null)
        {
            getChildren().removeAll(itemToRemove);
        }
    }

    public void removeThisFromParent()
    {
        getParent().getChildren().removeAll(this);
    }

    private void addChildes() throws MalformedURLException
    {
        if (!isLeaf())
        {
            File[] childs = getValue().getFile().listFiles();
            ObservableList<TreeItem<FileAddition>> childList = FXCollections.observableArrayList();

            if (childs != null)
            {
                for (File child : childs)
                {
                    childList.add(createItem(new FileAddition(child)));
                }
            }
            super.getChildren().addAll(childList);
        }
    }

    private FileTreeItem createItem(FileAddition child) throws MalformedURLException
    {
        ImageView image;
        if (!child.isFile())
        {
            image = Icons.closedFolder();
        }
        else
            {
                image = Icons.file();
            }

        FileTreeItem item = new FileTreeItem(child, image);

        item.expandedProperty().addListener( (observable, oldValue, newValue) ->
        {

            if (newValue)
            {
                try
                {
                    item.setGraphic(Icons.openFolder());
                }
                catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
            }
            else
                {
                    try
                    {
                        item.setGraphic(Icons.closedFolder());
                    }
                    catch (MalformedURLException e)
                    {
                        e.printStackTrace();
                    }
                }
        });
        return item;
    }
}