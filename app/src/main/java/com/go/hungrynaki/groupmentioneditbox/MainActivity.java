package com.go.hungrynaki.groupmentioneditbox;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private EditText editText;
    private String [] names = {"Mimo", "Azad", "Monir", "Asad", "Atik"};
    private ItemArrayAdapter itemArrayAdapter;
    private ArrayList<Item> itemList = new ArrayList<>();
    private TextView messageTv;

    private ArrayList<SelectedItem> selectedItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.mention);
        Button send = findViewById(R.id.send);
        messageTv = findViewById(R.id.messagetv);
        messageTv.setMovementMethod(LinkMovementMethod.getInstance());
        messageTv.setHighlightColor(Color.TRANSPARENT);

        initRecyclerView();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!isNameTapped) {
                    atAction(editText.getText().toString());
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                String finalMessage = editText.getText().toString();
                messageTv.setText(boldWithClick(finalMessage));

                editText.setText("");
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.row_item:
                String name = (String) v.getTag();
                nameAction(editText.getText().toString(), name);
                break;
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    boolean isNameTapped = false;

    private void nameAction(String message, String name) {
        StringBuilder stringBuilder = new StringBuilder(message);
        String nameWithSpace = (name + " ");
        stringBuilder.replace(start, end, nameWithSpace);

        int newCursorPosition = start + nameWithSpace.length();

        String finalMessage = stringBuilder.toString();
        isNameTapped = true;
        editText.setText(boldAction(finalMessage));
        editText.setSelection(newCursorPosition);
        recyclerView.setVisibility(View.GONE);

        (new Handler(Looper.getMainLooper())).postDelayed(new Runnable() {
            @Override
            public void run() {
                isNameTapped = false;
            }
        }, 200);
    }

    private SpannableString boldAction(String message) {
        SpannableString txtSpannable= new SpannableString(message);

        int boldStart = -1;

        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == '@') {
                boldStart = i;
            } else if (message.charAt(i) == ' ') {
                if (boldStart != -1) {
                    String cutName = message.substring((boldStart + 1), i);
                    if (nameContains(cutName)) {
                        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
                        txtSpannable.setSpan(boldSpan, boldStart, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        boldStart = -1;
                    } else {
                        boldStart = -1;
                    }
                }
            }
        }

        return txtSpannable;
    }

    private SpannableString boldWithClick(String message) {
        SpannableString txtSpannable= new SpannableString(message);

        int boldStart = -1;

        for (int i = 0; i < message.length(); i++) {
            if (message.charAt(i) == '@') {
                boldStart = i;
            } else if (message.charAt(i) == ' ') {
                if (boldStart != -1) {
                    final String cutName = message.substring((boldStart + 1), i);
                    if (nameContains(cutName)) {
                        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);

                        ClickableSpan myClickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(@NonNull View widget) {
                                Toast.makeText(MainActivity.this, cutName, Toast.LENGTH_SHORT).show();
                                widget.invalidate();
//
                            }

                            @Override
                            public void updateDrawState(TextPaint ds) {
                                super.updateDrawState(ds);
                                ds.setUnderlineText(false);
                                ds.setColor(getResources().getColor(R.color.colorAccent));

                            }
                        };
                        txtSpannable.setSpan(myClickableSpan, boldStart, i, 0);
                        txtSpannable.setSpan(boldSpan, boldStart, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        boldStart = -1;
                    } else {
                        boldStart = -1;
                    }
                }
            }
        }

        return txtSpannable;
    }

    private boolean nameContains(String cutName) {
        for (String name : names) {
            if (name.equals(cutName))
                return true;
        }
        return false;
    }

    int start, end;

    private void atAction(String message) {
        int cursorPosition = getCursorPosition();

        if (!TextUtils.isEmpty(message)) {
            for (int i = 0; i < message.length(); i++) {
                if (message.charAt(i) == '@') {
                    int startPosition = i + 1;

                    if (cursorPosition >= startPosition) {
                        String mainChar = message.substring(startPosition, cursorPosition);

                        getItems(mainChar);

                        if (itemList.size() > 0) {
                            start = startPosition;
                            end = cursorPosition;

                            recyclerView.setVisibility(View.VISIBLE);
                            itemArrayAdapter.setNames(itemList);
                        } else {
                            recyclerView.setVisibility(View.GONE);
                        }
                    }
                }
            }
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }


    // Mimo - 3, 7
    // Hi @Mimo Tumi kemon aso. @

    //1. Popup open houar shorto(@ or " @" ase ki na and @ or " @" thakle cursor er position theke koto shamne
    // @ ase tar shathe kono name mile jay ki na ta match kora)


    private void initRecyclerView() {

        itemArrayAdapter = new ItemArrayAdapter(R.layout.item_list, this);
        recyclerView = findViewById(R.id.item_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemArrayAdapter);

//        itemArrayAdapter.setNames(getItems(""));
    }

    private void getItems(String filterName) {
        itemList.clear();
        if (TextUtils.isEmpty(filterName)) {

            for (String name : names) {
                itemList.add(new Item(name));
            }
        } else {
            filterName = filterName.toLowerCase();
            for (String name : names) {
                if (name.toLowerCase().startsWith(filterName)) {
                    itemList.add(new Item(name));
                }
            }
        }
    }

    private int getCursorPosition() {
        return editText.getSelectionStart();
    }
}
