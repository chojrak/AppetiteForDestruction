import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class AppetiteForDestruction {

    public static void deleteChanges (String path, String destrChange) {

        File file = new File(path);
        List <File> list  = Arrays.asList(file.listFiles());

        for (File f : list) {
            if (f.isDirectory()) deleteChanges(f.getAbsolutePath(), destrChange);
            else if (f.getAbsolutePath().toLowerCase().contains(".xml")) checkFile(f, destrChange);
        }

    }

    public static void checkFile (File file, String destrChange) {


        StringBuilder sb = new StringBuilder ();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Scanner scn = new Scanner(fis);

        while (scn.hasNextLine()) {
            sb.append(scn.nextLine());
            sb.append("\r\n");
        }

        try {
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        scn.close();

        if (sb.toString().contains(destrChange)) {
            modifyFile(file, sb, destrChange);
        }

    }

    public static void modifyFile (File file, StringBuilder sb, String destrChange) {
        List <Integer> indexes = allIndexes(sb.toString(), destrChange);
        List <Integer> openTags = allIndexes(sb.toString(), "<");
        List <Integer> closeTags = allIndexes(sb.toString(), ">");

        StringBuilder temp = new StringBuilder();
        int openTag = 0;
        int closeTag = 2147483647;
        for (Integer i : indexes){
            for (Integer j : openTags)
                if (j < i && j > openTag) openTag = j;
            for (Integer k : closeTags)
                if (k > i && k < closeTag) closeTag = k;

            temp.append(sb.substring(0,openTag));
            temp.append("\r\n");
            temp.append("<!--destructiveChange>");
            temp.append("\r\n");
            temp.append(sb.substring(openTag, closeTag+1));
            temp.append("\r\n");
            temp.append("</destructiveChange-->");
            temp.append("\r\n");
            temp.append(sb.substring(closeTag+1));

            sb = new StringBuilder();
            sb.append(temp);
            temp = new StringBuilder ();

        }

        OutputStreamWriter osw = null;
        try {
            osw = new OutputStreamWriter(new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        PrintWriter pw = new PrintWriter(osw);
        pw.write(sb.toString());
        pw.close();
    }

    public static List<Integer> allIndexes (String sb, String str){
        int length = sb.length();
        int index = 0;
        List <Integer> list = new LinkedList <Integer>();


        while (sb.indexOf(str, index) >= 0){
            list.add(sb.indexOf(str, index));
            index = sb.indexOf(str, index)+1;
        }
        return list;

    }

}
