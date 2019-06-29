package converter.termin.myapplication.CurrencyPojo;

/**
 * Created by temurashurov on 05.01.2018.
 */

public class Valute {
    private String Name;

    private String Value;

    private String ID;

    private String Nominal;

    private String CharCode;

    public String getName ()
    {
        return Name;
    }

    public void setName (String Name)
    {
        this.Name = Name;
    }

    public String getValue ()
    {
        return Value;
    }

    public void setValue (String Value)
    {
        this.Value = Value;
    }

    public String getID ()
    {
        return ID;
    }

    public void setID (String ID)
    {
        this.ID = ID;
    }

    public String getNominal ()
    {
        return Nominal;
    }

    public void setNominal (String Nominal)
    {
        this.Nominal = Nominal;
    }

    public String getCharCode ()
    {
        return CharCode;
    }

    public void setCharCode (String CharCode)
    {
        this.CharCode = CharCode;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Name = "+Name+", Value = "+Value+", ID = "+ID+", Nominal = "+Nominal+", CharCode = "+CharCode+"]";
    }
}
