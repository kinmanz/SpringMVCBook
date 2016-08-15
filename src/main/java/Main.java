/**
 * Created by Sergey.Luchko on 12.08.2016.
 */


class A {

    {
        System.out.println("I'm class A");
    }
}

public class Main {

    public static void main(String[] args) {
        A a = new A();
        {
            int b = 2;
            System.out.println();
        }
    }

}
