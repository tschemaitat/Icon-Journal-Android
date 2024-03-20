import java.util.ArrayList;

public class DictEntry {
    public static final String dictionary = "dictionary";
    public static final String special = "special";

    public String key;
    public DataTree header;
    public ArrayList<DataTree> entries;
    public String type;



    public DictEntry(String key, DataTree header, ArrayList<DataTree> entries, String type) {
        this.key = key;
        this.header = header;
        this.entries = entries;
        this.type = type;
    }



}
