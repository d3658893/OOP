/**
 *
 * @author MyPC
 */
public class Department {

    private final String name;
    private final int id;

    public Department(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
}
