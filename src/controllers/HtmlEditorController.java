package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.web.HTMLEditor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;

public class HtmlEditorController
{
    @FXML
    private HTMLEditor htmlEditor = new HTMLEditor();
    private HashSet<String> htmlTags = new HashSet<>();
    StringBuilder listOfTags = new StringBuilder("Tags:\n");
    private Document doc = null;
    StringBuilder fileText = new StringBuilder();

    public HtmlEditorController(File htmlFile) throws FileNotFoundException
    {
        FileReader fileReader = new FileReader(htmlFile);
        Scanner scanner = new Scanner(fileReader);
        while(scanner.hasNext())
        {
            fileText.append(scanner.nextLine() + "\n");
        }
        htmlEditor.setHtmlText(fileText.toString());

        try
        {
            doc = Jsoup.parse(htmlFile, "UTF-8");
        } catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public HTMLEditor getHtmlEditor()
    {
        return this.htmlEditor;
    }

    public String getHtmlCode()
    {
        return fileText.toString();
    }

    public String getlistOfTags()
    {
        listOfTags = new StringBuilder();
        for(Element element : doc.getAllElements())
        {
            if(!htmlTags.contains(element.tagName()))
            {
                htmlTags.add(element.tagName());
                listOfTags.append(element.tagName() + "\n");
            }
        }
        return listOfTags.toString();
    }

    public void replaceTag(String previousTag, String newTag)
    {
        Elements elements = doc.getElementsByTag(previousTag);
        for( Element element : elements )
        {
            element.tagName(newTag);
        }
    }
}
