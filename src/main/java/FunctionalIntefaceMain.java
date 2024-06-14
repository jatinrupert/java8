public class FunctionalIntefaceMain {
    public static void main(String[] args)
    {
        FunctionalIntefaceMain pMain=new FunctionalIntefaceMain();
        pMain.printForm(() -> System.out.println("Printing form"));
    }

    public void printForm(CustInterface c)
    {
        c.print();
    }
}
