package controllers;
import app.Controller;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import utils.FileUtils;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.input.KeyCode;
import utils.Dialogs;
import utils.FileAddition;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class FileTreeCell extends TreeCell<FileAddition>
{
    private static FileTreeItem itemToMove = null;

    private static Boolean isCopy = null;

    private MenuItem pasteItem = new MenuItem("Paste");
    private MenuItem newFolderItem = new MenuItem("New Folder");
    private MenuItem copyItem = new MenuItem("Copy");
    private MenuItem cutItem = new MenuItem("Cut");
    private MenuItem deleteItem = new MenuItem("Delete");
    private MenuItem newFileItem = new MenuItem("New File");
    private MenuItem openFileItem = new MenuItem("Open");

    private ContextMenu contextMenu = new ContextMenu();//menu with copy, delete, cut, paste, newFolder
    private TextField textField;


    public FileTreeCell()
    {
        super();
        newFolderItem.setOnAction(t -> createNewFolder());
        newFileItem.setOnAction(event -> createNewFile());
        copyItem.setOnAction(event -> setupForMovingFile(true));
        cutItem.setOnAction(event -> setupForMovingFile(false));
        deleteItem.setOnAction(event -> deleteFile());
        pasteItem.setOnAction(event ->
        {
            try
            {
                moveFile();
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
        });
        openFileItem.setOnAction(event -> {
            try {
                openFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        contextMenu.getItems().addAll(copyItem, cutItem, deleteItem);
    }

    private void moveFile() throws MalformedURLException
    {
        FileAddition fileToMove = itemToMove.getValue();
        FileAddition folderToMove = getTreeItem().getValue();
        FileAddition destinationFile = FileUtils.getResultOfMovement(fileToMove, folderToMove);

        if (destinationFile.isExist())
        {
            Character answer = Dialogs.choiceIfExistsFile();
            switch (answer) {
                case 'p':
                    {
                        try
                        {
                            destinationFile.delete();
                            FileTreeItem item = (FileTreeItem) getTreeItem();
                            item.removeChildItem(destinationFile);
                        }
                        catch (IOException e)
                        {
                            error(e, "replacing");
                            return;
                        }
                        break;
                    }
                case 'n':
                    {
                        destinationFile = FileUtils.getFileWithSuffix(destinationFile.getAbsolutePath());
                        break;
                    }
                default:
                    {
                    return;
                }
            }
        }

        if (isCopy)
        {
            try
            {
                fileToMove.copy(destinationFile);
            }
            catch (IOException e)
            {
                error(e, "moving");
                return;
            }
        }
        else
            {
                itemToMove.removeThisFromParent();
                try
                {
                    fileToMove.cut(destinationFile);
                }
                catch (IOException e)
                {
                    error(e, "moving");
                    return;
                }
                itemToMove = null;
                isCopy = null;
            }

        FileTreeItem item = (FileTreeItem) getTreeItem();
        item.addNewChild(destinationFile);
        updateItem();
    }

    private void error(IOException e, String operation)
    {
        Dialogs.errorDialog("Error with "  + operation + " file");
        e.printStackTrace();
    }

    private void deleteFile()
    {
        FileAddition fileToRemove = getItem();

        if (itemToMove != null && fileToRemove.isEqualOrParent(itemToMove.getValue()))
        {
            itemToMove = null;
            isCopy = null;
        }

        try
        {
            if(!fileToRemove.isFile())
            {
                if(fileToRemove.getFile().getAbsolutePath().substring(0,1).equals("D"))
                {
                    if(Controller.DfolderList.containsKey(fileToRemove.getFile().getName()))
                    {
                        Controller.DfolderList.remove(fileToRemove.getFile().getName());
                    }
                }
                else
                    {
                        if(Controller.CfolderList.containsKey(fileToRemove.getFile().getName()))
                        {
                            Controller.CfolderList.remove(fileToRemove.getFile().getName());
                        }
                    }
            }
            fileToRemove.delete();
            FileTreeItem item = (FileTreeItem) getTreeItem();
            item.removeThisFromParent();
        }
        catch (IOException e)
        {
            error(e, "deleting");
        }
    }

    private void openFile() throws Exception
    {
        FileAddition currentFile = getTreeItem().getValue();
        IOpenFileFactory openFile;
        if(FileUtils.getFileExtension(currentFile.getFile()).equals("html"))
        {
            openFile = new OpenHtml(currentFile.getFile());
            openFile.open();
        }
        else
            {
                Desktop desktop = null;
                if (Desktop.isDesktopSupported())
                {
                    desktop = Desktop.getDesktop();
                }
                try
                {
                    desktop.open(currentFile.getFile());
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                }
            }
    }
    private void createNewFolder()
    {
        FileAddition currentFolder = getTreeItem().getValue();
        try
        {
            FileAddition newFolder = currentFolder.newFolder();
            FileTreeItem treeItem = (FileTreeItem) getTreeItem();
            treeItem.addNewChild(newFolder);
            treeItem.setExpanded(true);
        }
        catch (IOException e)
        {}
    }

    private void createNewFile()
    {
        FileAddition currentFolder = getTreeItem().getValue();
        try
        {
            FileAddition newFile = currentFolder.newFile();
            FileTreeItem treeItem = (FileTreeItem) getTreeItem();
            treeItem.addNewChild(newFile);
            treeItem.setExpanded(true);
        }
        catch (IOException e)
        {}
    }

    @Override
    public void startEdit()
    {
        super.startEdit();

        if (textField == null)
        {
            createTextField();
        }
        else
            {
                textField.setText(getText());
            }

        setText(null);
        setGraphic(textField);
        textField.selectAll();
    }

    @Override
    public void cancelEdit()
    {
        super.cancelEdit();
        updateItem(getItem(), isEmpty());
    }


    private void commitEdit(String newValue)
    {
        FileAddition fileExtension = getItem();
        if (newValue == null || newValue.isEmpty())
        {
            cancelEdit();
        } else
            {
            FileAddition destinationFile = FileUtils.resultOfRename(fileExtension, newValue);
            if (destinationFile.isExist())
            {
                Character answer = Dialogs.choiceIfExistsFile();
                switch (answer)
                {
                    case 'p': {
                        try {
                            destinationFile.delete();
                        } catch (IOException e) {
                            error(e, "replacing");
                            return;
                        }
                        break;
                    }

                    case 'n': {
                        destinationFile = FileUtils.getFileWithSuffix(destinationFile.getAbsolutePath());
                        break;
                    }

                    default: {
                        return;
                    }

                }
            }

            try
            {
                String previousName = fileExtension.getName();
                fileExtension.rename(destinationFile);
                updateItem();
                super.commitEdit(fileExtension);
            }
            catch (IOException e)
            {
                error(e, "renaming");
            }

        }

    }

    @Override
    protected void updateItem(FileAddition item, boolean empty)
    {
        super.updateItem(item, empty);
        if (empty)
        {
            setText(null);
            setGraphic(null);
        }
        else
            {
                setText(getTreeItem().getValue().toString());
                setGraphic(getTreeItem().getGraphic());
                setupMenu();
            }
    }


    private void setupMenu()
    {
        File file = getTreeItem().getValue().getFile();
        ObservableList<MenuItem> menuItems = contextMenu.getItems();

        if (!file.isFile())
        {
            if(menuItems.size() == 4)
            {
                menuItems.remove(openFileItem);
            }
            if (menuItems.size() == 3)
            {
                menuItems.add(newFolderItem);
                menuItems.add(newFileItem);
            }

            if (menuItems.size() == 5 || menuItems.size() == 6)
            {
                menuItems.removeAll(pasteItem);
            }

            if (itemToMove != null)
            {
                pasteItem.setText("Paste (" + itemToMove.getValue().toString() + ")");
                menuItems.add(pasteItem);
            }
        } else
            {
                if (menuItems.size() < 4)
                {
                    menuItems.add(openFileItem);
                }
                if (menuItems.size() > 4)
                {
                    menuItems.removeAll(pasteItem, newFolderItem, newFileItem);
                }
            }

        setContextMenu(contextMenu);
    }

    private void updateScreen()
    {
        updateTreeView(getTreeView());
    }

    private void updateItem()
    {
        updateTreeItem(getTreeItem());
    }

    private void setupForMovingFile(Boolean isCopy)
    {
        FileTreeCell.isCopy = isCopy;
        itemToMove = (FileTreeItem) getTreeItem();
        updateScreen();
    }

    private void createTextField()
    {
        textField = new TextField(getText());
        textField.setOnKeyReleased(t ->
        {
            if (t.getCode() == KeyCode.ENTER)
            {
                commitEdit(textField.getText());
            }
            else if (t.getCode() == KeyCode.ESCAPE)
            {
                cancelEdit();
            }
        });
    }

}
