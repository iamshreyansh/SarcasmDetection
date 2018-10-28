import java.util.*;
import java.io.*;

public class Split {

 static HashSet<String> stopwords = new HashSet<String>();

    public static void addStopwords()
    {
     try{
      BufferedReader br = new BufferedReader(new FileReader("stopwords.txt"));

      while(br.ready())
      {
       stopwords.add(br.readLine());
      }

     }catch(Exception e){System.out.println(e);}
    }

    public static ArrayList<String> words(String line)
    {
     if(stopwords.size() == 0)
      addStopwords();

     ArrayList<String> result = new ArrayList<String>();

     String[] words = line.split("[ \t\n,\\.\"!?$~()\\[\\]\\{\\}:;/\\\\<>+=%*]");
     for(int i=0; i < words.length; i++)
     {
      if(words[i] != null && !words[i].equals(""))
      {
       String word = words[i].toLowerCase();
       if(!stopwords.contains(word))
       {
        result.add(Stemmer.stem(word));
       }
      }
     }

     return result;
    }

  public static void main(String[] args)
   {
      
   }
}