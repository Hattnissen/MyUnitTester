public class MyUnitTester
{
    public static void main(String[] args)
    {
        try
        {
            Model model = new Model();
            View view = new View();
            Controller controller = new Controller(model, view);
        }
        catch (Exception e)
        {
            e.getCause();
        }
    }
}