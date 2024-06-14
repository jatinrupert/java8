public interface CustInterface{


    void print();
    default void printColor()
    {
        System.out.println("Printing the color");
    }
}
