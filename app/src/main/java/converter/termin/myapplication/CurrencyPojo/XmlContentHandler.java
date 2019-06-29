package converter.termin.myapplication.CurrencyPojo;

import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by temurashurov on 05.01.2018.
 */

public class XmlContentHandler extends DefaultHandler {

    private static final String LOG_TAG = "XmlContentHandler";

    // used to track of what tags are we
    private boolean inOwner = false;

    // accumulate the values
    private StringBuilder mStringBuilder = new StringBuilder();

    // new object
    private Valute mParsedDataSet = new Valute();

    // the list of data
    private List<Valute> mParsedDataSetList = new ArrayList<Valute>();

    /*
     * Called when parsed data is requested.
     */
    public List<Valute> getParsedData() {
        Log.v(LOG_TAG, "Returning mParsedDataSetList");
        return this.mParsedDataSetList;
    }

    // Methods below are built in, we just have to do the tweaks.

    /*
     * @Receive notification of the start of an element.
     *
     * @Called in opening tags such as <Owner>
     */
    @Override
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts) throws SAXException {

        if (localName.equals("Valute")) {
            // meaning new data object will be made
            this.mParsedDataSet = new Valute();
            this.inOwner = true;
        }


    }

    /*
     * @Receive notification of the end of an element.
     *
     * @Called in end tags such as </Owner>
     */
    @Override
    public void endElement(String namespaceURI, String localName, String qName)
            throws SAXException {

        // Owners
        if (this.inOwner == true && localName.equals("Valute")) {
            this.mParsedDataSetList.add(mParsedDataSet);
            mParsedDataSet.setID("Valutes");
            this.inOwner = false;
        }

        else if (this.inOwner == true && localName.equals("CharCode")) {
            mParsedDataSet.setCharCode(mStringBuilder.toString().trim());
        }

        else if (this.inOwner == true && localName.equals("Nominal")) {
            mParsedDataSet.setNominal(mStringBuilder.toString().trim());
        }

        else if (this.inOwner == true && localName.equals("Name")) {
            mParsedDataSet.setName(mStringBuilder.toString().trim());
        }
        else if (this.inOwner == true && localName.equals("Value")) {
            mParsedDataSet.setValue(mStringBuilder.toString().trim());
        }


        // empty our string builder
        mStringBuilder.setLength(0);
    }

    /*
     * @Receive notification of character data inside an element.
     *
     * @Gets be called on the following structure: <tag>characters</tag>
     */
    @Override
    public void characters(char ch[], int start, int length) {
        // append the value to our string builder
        mStringBuilder.append(ch, start, length);
    }
}