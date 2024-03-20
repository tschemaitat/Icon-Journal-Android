import java.util.ArrayList;
import java.util.HashMap;

public class KeyMap {
    public HashMap<String, ArrayList<String>> map = new HashMap<>();
    public KeyMap(DataTree header, DataTree data){
        convert(header, data);
    }

    public void convert(DataTree header, DataTree data){

    }


    public ArrayList<String> getData(String key){
        return map.get(key);
    }

    public boolean contains(String key, String value){
        return map.get(key).contains(value);
    }

    public void add(String key){
        map.computeIfAbsent(key, k -> new ArrayList<>());
    }

    public boolean contains(String key){
        return map.get(key) == null;
    }

    public void put(String key, String value){
        ArrayList<String> values = map.get(key);
        if( ! values.contains(value))
            values.add(value);
    }


}
