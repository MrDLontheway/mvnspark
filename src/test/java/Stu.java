import java.io.Serializable;

public class Stu implements Serializable{
    public String name;
    public int age;

    public Stu() {
        super();
    }
    public Stu(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Stu stu = (Stu) o;

        return name != null ? name.equals(stu.name) : stu.name == null;
    }

    /*@Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }*/
}
