package converter.termin.myapplication.CurrencyPojo;

/**
 * Created by temurashurov on 05.01.2018.
 */

public class ValCurs {
    private String Date;

    private String name;

    private Valute[] Valute;

    public String getDate ()
    {
        return Date;
    }

    public void setDate (String Date)
    {
        this.Date = Date;
    }

    public String getName ()
    {
        return name;
    }

    public void setName (String name)
    {
        this.name = name;
    }

    public Valute[] getValute ()
    {
        return Valute;
    }

    public void setValute (Valute[] Valute)
    {
        this.Valute = Valute;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Date = "+Date+", name = "+name+", Valute = "+Valute+"]";
    }
}
