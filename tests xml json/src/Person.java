/**
 * Created with IntelliJ IDEA.
 * User: dimkas71
 * Date: 8/23/13
 * Time: 11:51 AM
 * To change this template use File | Settings | File Templates.
 */
public class Person {

    private String mName;
    private int mAge;

    public Person(String name, int age) {

        this.mAge = age;
        this.mName = name;

    }


    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getAge() {
        return mAge;
    }

    public void setAge(int mAge) {
        this.mAge = mAge;
    }

    @Override
    public String toString() {

        return " {name = " + getName() + ", " + "age = " + String.format("%d", getAge()) + "}";
    }
}
