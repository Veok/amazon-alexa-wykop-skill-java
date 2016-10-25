package wykop;

/**
 * Created by L on 10.09.2016.
 */
class Item {

    private String body;

    public Item(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return body;
    }
}