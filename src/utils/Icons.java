package utils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;
import java.net.MalformedURLException;

public class Icons
{
    private static final String WAY_TO_CLOSED_FOLDER_ICON = "icons\\closeFolder.png";
    private static final String WAY_TO_OPEN_FOLDER_ICON = "icons\\openFolder.png";
    private static final String WAY_TO_FILE_ICON = "icons\\file.png";
    private static final double SIZE_OF_ICON = 40;

    public static ImageView closedFolder() throws MalformedURLException
    {
        return getImageView(WAY_TO_CLOSED_FOLDER_ICON);
    }

    public static ImageView file() throws MalformedURLException
    {
        return getImageView(WAY_TO_FILE_ICON);
    }

    public static ImageView openFolder() throws MalformedURLException
    {
        return getImageView(WAY_TO_OPEN_FOLDER_ICON);
    }

    private static ImageView getImageView(String fileWay) throws MalformedURLException
    {
        File file = new File(fileWay);
        String localUrl = file.toURI().toURL().toString();
        Image image = new Image(localUrl);

        ImageView view = new ImageView(image);
        view.setFitWidth(SIZE_OF_ICON);
        view.setFitHeight(SIZE_OF_ICON);
        return view;
    }
}