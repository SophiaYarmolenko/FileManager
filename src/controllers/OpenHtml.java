package controllers;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;

public class OpenHtml
{
    private File file;
    OpenHtml(File file)
    {
        this.file = file;
    }

    public void open()
    {
        HtmlEditorController htmlEditorController = null;
        try
        {
            htmlEditorController = new HtmlEditorController(new File("htmlFile.html"));
        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

        HTMLEditor htmlEditor = htmlEditorController.getHtmlEditor();
        Scene htmlEditorScene = new Scene(htmlEditor);
        Stage htmlEditorWindow = new Stage();
        htmlEditorWindow.setTitle("Html Editor");
        htmlEditorWindow.setScene(htmlEditorScene);
        htmlEditorWindow.setWidth(500);
        htmlEditorWindow.setHeight(700);
        htmlEditorWindow.setX(0);
        htmlEditorWindow.setY(50);
        htmlEditorWindow.show();

        TextArea htmlCodeTextArea = new TextArea(htmlEditorController.getHtmlCode());
        Scene htmlCodeScene = new Scene(htmlCodeTextArea);
        Stage htmlCodeWindow = new Stage();
        htmlCodeWindow.setTitle("Code");
        htmlCodeWindow.setScene(htmlCodeScene);
        htmlCodeWindow.setWidth(800);
        htmlCodeWindow.setHeight(700);
        htmlCodeWindow.setX(500);
        htmlCodeWindow.setY(50);
        htmlCodeWindow.show();

        TextArea listOfTagsTextArea = new TextArea(htmlEditorController.getlistOfTags());
        Scene listOfTagsScene = new Scene(listOfTagsTextArea);
        Stage listOfTagsWindow = new Stage();
        listOfTagsWindow.setTitle("list Of Tags");
        listOfTagsWindow.setScene(listOfTagsScene);
        listOfTagsWindow.setWidth(200);
        listOfTagsWindow.setHeight(700);
        listOfTagsWindow.setX(1300);
        listOfTagsWindow.setY(50);
        listOfTagsWindow.show();

        TextField previousTag = new TextField();
        previousTag.setPromptText("previous tag");
        previousTag.setEditable(true);
        previousTag.setPrefWidth(200);
        TextField newTag = new TextField();
        newTag.setPromptText("new tag");
        newTag.setPrefWidth(200);
        newTag.setEditable(true);
        Button changeTagName = new Button();
        changeTagName.setText("Change");
        HtmlEditorController finalHtmlEditorController = htmlEditorController;
        changeTagName.setOnAction(actionEvent ->
        {
            String previousTagText = previousTag.getText();
            String newTagText = newTag.getText();
            finalHtmlEditorController.replaceTag(previousTagText, newTagText);
            htmlCodeTextArea.setText(finalHtmlEditorController.fileText.toString());
            listOfTagsTextArea.setText(finalHtmlEditorController.getlistOfTags());
        });

        FlowPane changeTagsAnchorPane = new FlowPane();
        changeTagsAnchorPane.getChildren().add(previousTag);
        changeTagsAnchorPane.getChildren().add(changeTagName);
        changeTagsAnchorPane.getChildren().add(newTag);
        Scene changeScene = new Scene(changeTagsAnchorPane);
        Stage changeWindow = new Stage();
        changeWindow.setTitle("Change");
        changeWindow.setScene(changeScene);
        changeWindow.setWidth(500);
        changeWindow.setHeight(200);
        changeWindow.setX(0);
        changeWindow.setY(0);
        changeWindow.show();
    }

}
