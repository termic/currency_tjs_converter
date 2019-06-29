package converter.termin.myapplication;

import android.app.Application;
import android.content.Context;
import android.util.Log;

/**
 * Created by temurashurov on 26.05.2018.
 */

public class CurrencyApp extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

    }
    public static String getCurrencySymbol(String currency){
        if(currency == null){return "";}
        Log.v("curr is ", currency);
        String[] symbol_array = mContext.getResources().getStringArray(R.array.symbols_by_currency);
        for (String symbol:symbol_array
                ) {
            Log.v("symbol is ", symbol);
            if(symbol.contains(currency)){
                Log.v("symbol found ", symbol + " for " + currency);
                return symbol.replace(currency,"");
            }
        }
        return "";
    }
}
