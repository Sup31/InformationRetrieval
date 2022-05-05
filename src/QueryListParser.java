import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


public class QueryListParser{
    String number;
    String title;
    String description;
    public static List<QueryListParser> parseQueries(String filename){

        List<QueryListParser> parsedQueryList = new ArrayList<QueryListParser>();
        QueryListParser query = null;
        try { 
            BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir")+"/resources/"+filename));
            String line =  reader.readLine();
            boolean desc = false;
            while (line != null) {
                
                if (line.startsWith("<top>")){
                    if (query != null){
                        parsedQueryList.add(query);
                    }
                    
                    desc =false;
                    query = new QueryListParser();
                }
                else if (line.startsWith("<num>")){
                    String number =line.split(":")[1].trim();
                    
                    query.setNumber(number);

                }
                else if (line.startsWith("<title>")){
                    String title = line.split(">")[1].trim();
                   
                    query.setTitle(title);
                }
                else if (line.startsWith("<desc>") ) {
                   
                    desc=true;
                }
                else if(line.startsWith("</top>"))
                {
                    desc=false;
                }
                else if(!line.startsWith("<desc>") && desc == true)
                    {query.setDescription(line.stripTrailing());;
                }
                line = reader.readLine();
            }
            reader.close();
        }
        catch(Exception e)
        {e.printStackTrace();}
        
        return parsedQueryList;
    }

    public void setNumber(String num)
    {
        this.number=num;
    }
    public void setTitle(String title)
    {
        this.title=title;
    }
    public void setDescription(String desc)
    {
        this.description=desc;
    }
    public String getNumber()
    {
        return this.number;
    }
    public String getTitle()
    {
        return this.title;
    }
    public String getDescription()
    {
        return this.description;
    }   

}
