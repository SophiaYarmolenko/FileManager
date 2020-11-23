package controllers;

import javafx.scene.control.*;
import utils.FileAddition;
import utils.FileUtils;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class FileSearcher
{
    private FileTreeItem fileTreeItem;
    private String limitation;

    public FileSearcher(FileTreeItem fileTreeItem, String limitation)
    {
        this.limitation = limitation;
        this.fileTreeItem = fileTreeItem;
    }

    public List<FileTreeItem> search()
    {
        LinkedList<FileTreeItem> list = new LinkedList<>();
        search(fileTreeItem, list);
        return list;
    }

    private void search(FileTreeItem item, LinkedList<FileTreeItem> list)
    {
        if(item.isLeaf())
        {
            if( checkLimitation(item.getValue().getFile()))
            {
                list.add(item);
            }
        }
        else
            {
                if(item.getChildren() != null)
                {
                    for(TreeItem<FileAddition> item1 : item.getChildren())
                    {
                        FileTreeItem fileTreeItem = (FileTreeItem) item1;
                        search(fileTreeItem, list);
                    }
                }
            }
    }

    private boolean checkLimitation(File file)
    {
        if(limitation.equals(""))
            return true;
        if( limitation.contains(file.getName()) || file.getName().contains(limitation))
            return true;
        else
            {
                if( limitation.substring(0,1).equals(".") )
                {
                    if(FileUtils.getFileExtension(file).equals(limitation.substring(1)))
                    {
                        return true;
                    }
                }
            }
        return false;
    }

}


