package server;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileServer {
    int capacity;
    Map<String, Boolean> files;

    public FileServer() {
        this.capacity = 10;
        files = generateMap();
    }

    private Map<String, Boolean> generateMap() {
      Map<String, Boolean> map = new HashMap<>();
        for (int i = 1; i < 11; i++) {
            map.put("file" + i, false);
        }
        return map;
    }

    boolean add(String name) {
        if (files.containsKey(name) && !files.get(name)) {
            files.put(name, true);
            return true;
        }
        return false;
    }

    boolean get(String name) {
        return files.get(name);
    }

    boolean delete(String name) {
        if (files.containsKey(name) && files.get(name)) {
            files.put(name, false);
            return true;
        }
        return false;
    }
}
