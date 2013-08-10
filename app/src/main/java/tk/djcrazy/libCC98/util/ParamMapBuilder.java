package tk.djcrazy.libCC98.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ding on 13-8-10.
 */
public class ParamMapBuilder {

    private Map<String, String> map;

    public ParamMapBuilder() {
        map = new HashMap<String, String>();
    }

    public ParamMapBuilder param(String name, String value) {
        map.put(name, value);
        return this;
    }
    public ParamMapBuilder param(String name, int value) {
        map.put(name, String.valueOf(value));
        return this;
    }
    public Map<String, String> buildMap() {
        return map;
    }
}
