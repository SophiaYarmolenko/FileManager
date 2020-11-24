package utils;
import utils.FileAddition;

import java.io.*;

public class FileUtils
{
    private final static int LENGTH_OF_EXTENSION = 4;
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
            if(filePath.substring(filePath.length() - LENGTH_OF_EXTENSION).equals(".txt"))
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

    public static String getFileExtension(File file)
    {
        String fileName = file.getName();
        if(fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
            return fileName.substring(fileName.lastIndexOf(".")+1);
        else return "";
    }

    public static void rewriteFile(File file, String newFileText)
    {
        FileWriter fileWriter = null;
        try
        {
            fileWriter = new FileWriter(file);
            fileWriter.write(newFileText);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                fileWriter.close();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}