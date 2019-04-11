package Multcast;

import java.util.HashMap;
import java.util.Set;

/**
 * Os dados da mensagem serao varios parametros organizador em uma HashMap
 */
public class Message extends HashMap<String, String> {

    public Message() {
    }

    public Message(String serial) {
        unserialize(serial);
    }

    public Message(HashMap data) {
        data = data;
    }

    /**
     * Serializamos um objeto para String utilizando o formato:
     * key1::value1//key2::value2
     *
     * @return String serial
     */
    public String serialize() {
        String serial = "";
        Set<String> keys = keySet();

        for (String key : keys) {
            serial = serial + key + "::" + get(key) + "//";
        }

        serial = serial.substring(0, serial.length() - 2);

        return serial;
    }

    /**
     * Deserializamos uma String e armazenamos seu resultado em data.
     */
    public void unserialize(String serial) {
        String[] lines = serial.split("//");

        for (String line : lines) {
            String[] keyValue = line.split("::");
            put(keyValue[0], keyValue[1]);
        }
    }

}
