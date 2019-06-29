package converter.termin.myapplication;

import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.vlonjatg.progressactivity.ProgressLayout;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import converter.termin.myapplication.CurrencyPojo.Valute;
import converter.termin.myapplication.CurrencyPojo.XmlContentHandler;
import converter.termin.myapplication.adapter.CurrencyAdapter;
import converter.termin.myapplication.model.Model;


/**
 * Created by temurashurov on 15.05.2018.
 */

public class CurrencyActivity extends AppCompatActivity implements CurrencyAdapter.OnSomethingChanged {

    private RecyclerView mRecyclerView;
    private CurrencyAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressLayout progressLayout;
    private CurrencyService currencyService;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);

        progressLayout = findViewById(R.id.progressActivity);
        // initializing animation drawable by getting background from constraint layout
        animationDrawable = (AnimationDrawable) ((View) progressLayout).getBackground();

        // setting enter fade animation duration to 5 seconds
        animationDrawable.setEnterFadeDuration(5000);

        // setting exit fade animation duration to 2 seconds
        animationDrawable.setExitFadeDuration(2000);

        mRecyclerView = findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        animationDrawable.start();
        fireAsync();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (animationDrawable != null && !animationDrawable.isRunning()) {
            // start the animation
            animationDrawable.start();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (animationDrawable != null && animationDrawable.isRunning()) {
            // stop the animation
            animationDrawable.stop();
        }
    }
    @Override
    public void generateCurrencyRateCards(Valute currency) {
        adapter.getDataSet().get(1).setCurrencyCode(currency.getCharCode());
        adapter.getDataSet().get(1).setCurrencyName(currency.getName());
        adapter.getDataSet().get(1).setCurrencyRate(currency.getNominal());
        adapter.getDataSet().get(2).setCurrencyCode("TJS");
        adapter.getDataSet().get(2).setCurrencyName("Таджикский сомони");
        adapter.getDataSet().get(2).setCurrencyRate(currency.getValue());
        adapter.notifyDataSetChanged();
    }
    
    private void fireAsync(){
        if (currencyService == null
                || currencyService.getStatus() == AsyncTask.Status.FINISHED) {
            currencyService = new CurrencyService();
            currencyService.execute();
        }
    }
    
    private class CurrencyService extends AsyncTask<String, String, List<Valute>> {
        String LOG_TAG = "CNY";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressLayout.showLoading();
        }

        @Override
        protected List<Valute> doInBackground(String... strings) {
            return getCurrencyList();
        }

        @Override
        protected void onPostExecute(List<Valute> filteredCurrency) {
            if (!filteredCurrency.isEmpty()) {
                progressLayout.showContent();
                Log.v(LOG_TAG, "filtered count: " + filteredCurrency.size());
                initializeAdapter(filteredCurrency);
            } else {
                progressLayout.showError(R.drawable.ic_no_connection, "No Connection",
                        "We could not establish a connection with our servers. Try again when you are connected to the interne.",
                        "Try Again", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                fireAsync();
                            }
                        });
            }
        }
    }

    private void initializeAdapter(List<Valute> filteredCurrency) {
        adapter = new CurrencyAdapter(getModels(), this, this);
        adapter.setValuteList(filteredCurrency);
        mRecyclerView.setAdapter(adapter);
    }

    public List<Valute> getCurrencyList() {

        HttpURLConnection connection = null;
        List<Valute> filteredCurrency = new ArrayList<>();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(Calendar.getInstance().getTime());

        System.out.println("formattedDate : " + formattedDate);

        try {
            connection = (HttpURLConnection) new URL("http://nbt.tj/ru/kurs/export_xml.php?date="
                    + formattedDate
                    + "&export=xmlout")
                    .openConnection();
            System.setProperty("http.keepAlive", "false");
            connection.setDoInput(true);
            connection.setReadTimeout(5000 /*milliseconds*/);
            connection.setConnectTimeout(3500 /* milliseconds */);
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.connect();

            int HttpResult = connection.getResponseCode();

            if (HttpResult == HttpURLConnection.HTTP_OK) {
                InputSource inputSource = new InputSource(connection.getInputStream());

                // instantiate SAX parser
                SAXParserFactory saxParserFactory = SAXParserFactory
                        .newInstance();
                SAXParser saxParser = saxParserFactory.newSAXParser();

                // get the XML reader
                XMLReader xmlReader = saxParser.getXMLReader();
                // prepare and set the XML content or data handler before
                // parsing
                XmlContentHandler xmlContentHandler = new XmlContentHandler();
                xmlReader.setContentHandler(xmlContentHandler);
                // parse the XML input source
                xmlReader.parse(inputSource);

                // put the parsed data to a List
                List<Valute> parsedDataSet = xmlContentHandler
                        .getParsedData();

                // we'll use an iterator so we can loop through the data
                Iterator<Valute> i = parsedDataSet.iterator();


                String LOG_TAG = "LOGGER";
                Log.v(LOG_TAG, "parsedDataSet size : " + parsedDataSet.size());
                while (i.hasNext()) {

                    Valute dataItem = (Valute) i.next();

                    String parentTag = dataItem.getID();
                    Log.v(LOG_TAG, "parentTag: " + parentTag);

                    if (parentTag.equals("Valutes")
                            && new ArrayList<>(Arrays.asList("USD", "EUR", "RUB", "GBP")).contains(dataItem.getCharCode())) {
                        Log.v(LOG_TAG, "Char code: " + dataItem.getCharCode());
                        Log.v(LOG_TAG, "nominal: " + dataItem.getNominal());
                        Log.v(LOG_TAG, "Name: " + dataItem.getName());
                        Log.v(LOG_TAG, "value: " + dataItem.getValue());
                        filteredCurrency.add(dataItem);
                    }

                }

            }
        } catch (Exception e) {
            //e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
                return filteredCurrency;
            }
        }

        return filteredCurrency;

    }

    private ArrayList<Model> getModels() {
        return new ArrayList<>(Arrays.asList(
                new Model(Model.CURRENCY_REVERSER, 0),
                new Model(Model.CURRENCY_RATE_1, 0),
                new Model(Model.CURRENCY_RATE_2, 0)

        ));
    }

}