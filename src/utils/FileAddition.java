package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileAddition
 {
    private File file;

    public FileAddition( String filePathName )
    {
        file = new File(filePathName);
    }

    public FileAddition(File file)
    {
        this.file = file;
    }

    @Override
    public String toString()
    {
        return file.toPath().getFileName().toString();
    }

    public File getFile()
    {
        return file;
    }

    public Boolean isEqualOrParent(FileAddition other)
    {
        String wayToFile = getFile().getAbsolutePath();
        String wayToOther = other.file.getAbsolutePath();
        if (wayToFile.length() <= wayToOther.length()) {
            String line = wayToOther.substring(0, wayToFile.length());
            if (line.equals(wayToFile)) {
                return true;
            }
        }

        return false;
    }

    public void delete() throws IOException
    {
        if (!file.delete()) {
            throw new IOException();
        }
    }

    public void copy(FileAddition destination) throws IOException
    {
        try ( FileChannel sourceChannel = new FileInputStream(this.file).getChannel();
             FileChannel destChannel = new FileOutputStream(destination.file).getChannel())
        {
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }
    }

    public void cut( FileAddition destination ) throws IOException
    {
        File dest = destination.file;
        if ( !file.renameTo(dest) ) {
            throw new IOException();
        }
    }

    public void rename(FileAddition to) throws IOException
    {
        cut(to);
        file = to.file;
    }

    public FileAddition newFolder() throws IOException
    {
        String fileway = file.getAbsolutePath();
        fileway += "/" + "NewFolder";
        FileAddition newFolder = FileUtils.getFileWithSuffix(fileway);

        boolean success = newFolder.mkdir();
        if (!success)
        {
            throw new IOException();
        }
        return newFolder;
    }

     public FileAddition newFile() throws IOException
     {
         String fileway = file.getAbsolutePath();
         fileway += "\\" + "NewFile.txt";
         FileAddition newFile = FileUtils.getFileWithSuffix(fileway);
         return newFile;
     }


    public Boolean isFile()
    {
        return file.isFile();
    }

    public Boolean mkdir()
    {
        return file.mkdir();
    }

    public String getName() {
        return file.getName();
    }

    public String getAbsolutePath()
    {
        return file.getAbsolutePath();
    }

    public Boolean isExist()
    {
        return file.exists();
    }

}