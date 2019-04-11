package Multcast;

public class Utils {
    
    public static String[] remove(String[] data, String delete) {
        if (data.length == 1 && data[0].equals(delete)) {
            String[] r = {};
            return r;
        }
        
        String dataString = "";
        
        for (int i = 0; i < data.length; i++) {
            if (!data[i].equals(delete)) {
                dataString += data[i] + ",";
            }
        }
        
        dataString = dataString.substring(0, dataString.length() - 1);
        
        return dataString.split(",");
    }
    
    public static String implode(String[] data) {
        if (data.length == 0)
            return "";
            
        String dataString = "";
        
        for (int i = 0; i < data.length; i++) {
            dataString += data[i] + ",";
        }
        
//        if (data.length > 1)
        dataString = dataString.substring(0, dataString.length() -1);
        
        return dataString;
    }
    
}
