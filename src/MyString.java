public class MyString {
    private String str = "";

    public MyString()
    {
        str = "test";
    }

    public void addTestToString()
    {
        str += "test";
    }

    public void nullString()
    {
        str = null;
    }

    public String getStr() {
        return str;
    }
}
