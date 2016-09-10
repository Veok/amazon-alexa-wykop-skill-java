package wykop;

import java.util.List;

/**
 * Created by L on 10.09.2016.
 */
class DataObject { //This class should match your json object structure
    private String body;
    private List<Item> item;

    DataObject(String body, List<Item> item) {
        this.body = body;
        this.item = item;
    }

    @Override
    public String toString() {
        return body + item;
    }
}