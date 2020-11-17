package utils;
import utils.FileAddition;

import java.io.*;

public class FileUtils
{
    public static FileAddition getResultOfMovement(FileAddition source, FileAddition folderDestination )
    {
        String fileName = source.getName();
        String wayToNewFile = folderDestination.getAbsolutePath() + "/" + fileName;
        return new FileAddition( new File(wayToNewFile) );
    }

    public static FileAddition getFileWithSuffix(String filePath)
    {
        File newFile = new File(filePath);
        int i = 0;
        while (newFile.exists())
        {
            i++;
            if(filePath.substring(filePath.length()-4, filePath.length()).equals(".txt"))
            {
                newFile = new File(filePath.substring(0, filePath.length()-4) + " (" + i + ")"+".txt");
            }
            else
                {
                    newFile = new File(filePath + " (" + i + ")");
                }
        }
        try
        {
            newFile.createNewFile();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return new FileAddition(newFile);
    }

    public static FileAddition resultOfRename(FileAddition source, String newName)
    {
        String path = source.getAbsolutePath();
        String newPath = path.substring(0, path.length() - source.getName().length()) + newName;
        return new FileAddition(new File( newPath));
    }
}