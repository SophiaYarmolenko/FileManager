package controllers;

import javafx.fxml.FXML;
import javafx.scene.web.HTMLEditor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.FileUtils;

import java.io.*;
import java.util.HashSet;
import java.util.Scanner;

public class HtmlEditorController
{
    @FXML
    private HTMLEditor htmlEditor = new HTMLEditor();
    private HashSet<String> htmlTags = new HashSet<>();
    private Document doc = null;
    String fileText;
    File htmlFile;

    public HtmlEditorController(File htmlFile) throws FileNotFoundException
    {
        StringBuilder fileTextStringBuilder = new StringBuilder();
        this.htmlFile = htmlFile;
        FileReader fileReader = new FileReader(htmlFile);
        Scanner scanner = new Scanner(fileReader);
        while(scanner.hasNext())
        {
            fileTextStringBuilder.append(scanner.nextLine() + "\n");
        }
        fileText = fileTextStringBuilder.toString();
        htmlEditor.setHtmlText(fileText);

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
        return fileText;
    }

    public String getlistOfTags()
    {
        StringBuilder listOfTags = new StringBuilder();
        htmlTags.clear();
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
        fileText = fileText.replaceAll("<" + previousTag, "<"+newTag);
        fileText = fileText.replaceAll("</" + previousTag + ">", "</"+newTag+">");
        fileText = fileText.replaceAll("<\\s+" + previousTag, "<"+newTag);
        fileText = fileText.replaceAll("</\\s+" + previousTag + "\\s+>", "</"+newTag+">");
        FileUtils.rewriteFile(htmlFile, fileText);

    }

}
