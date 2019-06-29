package converter.termin.myapplication.CurrencyPojo;

/**
 * Created by temurashurov on 05.01.2018.
 */

public class NbtCurrency
{
    private ValCurs ValCurs;

    public ValCurs getValCurs ()
    {
        return ValCurs;
    }

    public void setValCurs (ValCurs ValCurs)
    {
        this.ValCurs = ValCurs;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ValCurs = "+ValCurs+"]";
    }
}