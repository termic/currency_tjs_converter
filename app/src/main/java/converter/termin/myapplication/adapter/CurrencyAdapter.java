

package converter.termin.myapplication.adapter;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import converter.termin.myapplication.CurrencyApp;
import converter.termin.myapplication.CurrencyPojo.Valute;
import converter.termin.myapplication.R;
import converter.termin.myapplication.model.Model;


public class CurrencyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public interface OnSomethingChanged {
        void generateCurrencyRateCards(Valute currency);
    }

    List<Valute> valuteList;
    List<String> spinnerList;
    private ArrayList<Model> dataSet;
    Context mContext;
    int total_types;
    Boolean isArrowClicked = false;
    OnSomethingChanged somethingChanged;


    public  class CurrencyViewHolder extends RecyclerView.ViewHolder {

        TextView code, name, rate;
        CardView card;

        public CurrencyViewHolder(View itemView) {
            super(itemView);
            this.code = (TextView) itemView.findViewById(R.id.txt_currency_code);
            this.name = (TextView) itemView.findViewById(R.id.txt_currency_name);
            this.rate = (TextView) itemView.findViewById(R.id.txt_currency_rate);
            this.card = (CardView) itemView.findViewById(R.id.card_view);
            code.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "robo.ttf"));
        }

    }

    public  class CurrencyReverserViewHolder extends RecyclerView.ViewHolder {

        ImageView arrow;
        CardView card;
        Spinner spinner;
        EditText editText;
        TextView textCovertedSum;

        public CurrencyReverserViewHolder(View itemView) {
            super(itemView);
            this.arrow = (ImageView) itemView.findViewById(R.id.img_currency_reverse);
            this.card = (CardView) itemView.findViewById(R.id.card_view);
            this.spinner = (Spinner) itemView.findViewById(R.id.sprCurrencyNames);
            this.editText = (EditText) itemView.findViewById(R.id.editTextSum);
            this.textCovertedSum = (TextView) itemView.findViewById(R.id.txt_converted_sum);
            textCovertedSum.setTypeface(Typeface.createFromAsset(mContext.getAssets(), "robo.ttf"));
            itemView.findViewById(R.id.actionInfo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            final ImageView imgArrow = arrow;
            ObjectAnimator scaleDown = ObjectAnimator.ofPropertyValuesHolder(
                    imgArrow,
                    PropertyValuesHolder.ofFloat("scaleX", 1.2f),
                    PropertyValuesHolder.ofFloat("scaleY", 1.2f));
            scaleDown.setDuration(310);

            scaleDown.setRepeatCount(ObjectAnimator.INFINITE);
            scaleDown.setRepeatMode(ObjectAnimator.REVERSE);

            scaleDown.start();
            imgArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isArrowClicked = !isArrowClicked;
                    if(isArrowClicked){
                        ImageViewAnimatedChange(mContext, imgArrow, R.drawable.ic_arrow_forward_black_24dp);

                    } else {
                        ImageViewAnimatedChange(mContext, imgArrow, R.drawable.ic_arrow_back_black_24dp);

                    }
                    editText.setText(editText.getText());
                }
            });

            List<String> currName = new ArrayList<>();
            for (Valute c : getValuteList()
                    ) {
                currName.add(c.getCharCode());

            }

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext,
                    android.R.layout.simple_spinner_dropdown_item, currName);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            this.spinner.setAdapter(dataAdapter);
            this.spinner.setSelection(0);
            this.spinner.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ((TextView) spinner.getSelectedView()).setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
                    ((TextView) spinner.getSelectedView()).setTextColor(Color.WHITE);
                    ((TextView) spinner.getSelectedView()).setTypeface(null, Typeface.BOLD);
                    ((TextView) spinner.getSelectedView()).setGravity(Gravity.CENTER);
                }
            });

            this.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    String currName = adapterView.getSelectedItem().toString();
                    for (Valute v: getValuteList()
                            ) {
                        if(v.getCharCode().equals(currName)){
                            somethingChanged.generateCurrencyRateCards(v);
                            editText.setText(editText.getText());
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            editText.addTextChangedListener(new TextWatcher() {

                @Override
                public void afterTextChanged(Editable s) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start,
                                              int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start,
                                          int before, int count) {
                    if(s.length() != 0 && spinner.getSelectedItem() != null) {
                        DecimalFormat formatter = new DecimalFormat("###,###.###");
                        String selectedForegnCurr = spinner.getSelectedItem().toString();
                        for (Valute v : getValuteList()
                                ) {
                            if (v.getCharCode().equals(selectedForegnCurr)) {
                                double sum;
                                if(isArrowClicked){
                                    sum = Double.valueOf(s.toString()) / Double.valueOf(v.getValue());
                                    textCovertedSum.setText(CurrencyApp.getCurrencySymbol(v.getCharCode()).concat(formatter.format(sum)));

                                }else{
                                    sum = Double.valueOf(s.toString()) * Double.valueOf(v.getValue());
                                    textCovertedSum.setText(formatter.format(sum));
                                }
                            }
                        }
                    }else{
                        textCovertedSum.setText("");
                    }
                }
            });
        }

    }

    public CurrencyAdapter(ArrayList<Model> data, Context context, OnSomethingChanged somethingChanged) {
        this.dataSet = data;
        this.mContext = context;
        total_types = dataSet.size();
        this.somethingChanged = somethingChanged;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case Model.CURRENCY_RATE_1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_currency, parent, false);
                return new CurrencyViewHolder(view);
            case Model.CURRENCY_RATE_2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_currency, parent, false);
                return new CurrencyViewHolder(view);
            case Model.CURRENCY_REVERSER:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_currency_reverser, parent, false);
                return new CurrencyReverserViewHolder(view);
        }
        return null;


    }

    @Override
    public int getItemViewType(int position) {

        return dataSet.get(position).type;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        Model object = dataSet.get(listPosition);
        if (object != null) {
            switch (object.type) {
                case Model.CURRENCY_RATE_1:
                    ((CurrencyViewHolder)holder).code.setText(CurrencyApp.getCurrencySymbol(object.getCurrencyCode()));
                    ((CurrencyViewHolder)holder).name.setText(object.getCurrencyName());
                    ((CurrencyViewHolder)holder).rate.setText(object.getCurrencyRate());
                    ((CurrencyViewHolder)holder).card.setCardBackgroundColor(mContext.getResources().getColor(R.color.cardCurrency2));
                    break;
                case Model.CURRENCY_RATE_2:
                    ((CurrencyViewHolder)holder).code.setText(object.currencyCode);
                    ((CurrencyViewHolder)holder).name.setText(object.currencyName);
                    ((CurrencyViewHolder)holder).rate.setText(object.currencyRate);
                    ((CurrencyViewHolder)holder).card.setCardBackgroundColor(mContext.getResources().getColor(R.color.cardCurrency2));
                    break;
                case Model.CURRENCY_REVERSER:
                    ((CurrencyReverserViewHolder)holder).card.setCardBackgroundColor(mContext.getResources().getColor(R.color.cardCurrency1));
                    break;
            }
        }

    }

    public static void ImageViewAnimatedChange(Context c, final ImageView v, final int resId) {
        final Animation anim_out = AnimationUtils.loadAnimation(c, android.R.anim.fade_out);
        anim_out.setDuration(250);

        final Animation anim_in  = AnimationUtils.loadAnimation(c, android.R.anim.fade_in);
        anim_in.setDuration(250);

        anim_out.setAnimationListener(new Animation.AnimationListener()
        {
            @Override public void onAnimationStart(Animation animation) {}
            @Override public void onAnimationRepeat(Animation animation) {}
            @Override public void onAnimationEnd(Animation animation)
            {
                v.setImageResource(resId);
                anim_in.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {}
                    @Override public void onAnimationRepeat(Animation animation) {}
                    @Override public void onAnimationEnd(Animation animation) {}
                });
                v.startAnimation(anim_in);
            }
        });
        v.startAnimation(anim_out);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public ArrayList<Model> getDataSet() {
        return dataSet;
    }
    public List<Valute> getValuteList() {
        return valuteList;
    }

    public void setValuteList(List<Valute> valuteList) {
        this.valuteList = valuteList;
        notifyDataSetChanged();
    }
}
