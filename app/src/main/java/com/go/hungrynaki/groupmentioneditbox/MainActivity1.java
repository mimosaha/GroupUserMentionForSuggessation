package com.go.hungrynaki.groupmentioneditbox;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

public class MainActivity1 extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private EditText editText;
    private String [] names = {"Mimo", "Azad", "Monir", "Asad", "Atik"};
    private ItemArrayAdapter itemArrayAdapter;
    private ArrayList<Item> itemList = new ArrayList<>();

    private ArrayList<SelectedItem> selectedItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.mention);
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
                filterMessageForUser();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.row_item:
                String name = (String) v.getTag();
                executeNameAfterTap(name);
                break;
        }
    }

    private void filterMessageForUser() {
        String message = editText.getText().toString();
        if (message.contains(" @")) {
            int position = message.indexOf(" @");
            int updatePos = position + 2;
            nameRetrieveFromFilterMessage(message, updatePos);
        } else {
            if (message.length() > 0 && message.charAt(0) == '@') {
                int position = message.indexOf("@");
                int updatePos = position + 1;
                nameRetrieveFromFilterMessage(message, updatePos);
            } else {
                recyclerView.setVisibility(View.GONE);
            }
        }
    }

    private void nameRetrieveFromFilterMessage(String message, int updatePos) {
        int cursorPosition = editText.getSelectionStart();

        if (updatePos == cursorPosition) {
            getItems("");
        } else {
            String filterName = message.substring(updatePos, cursorPosition);
            getItems(filterName);
        }

        if (itemList.size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            itemArrayAdapter.setNames(itemList);
        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }

    private void executeNameAfterTap(String name) {

        String message = editText.getText().toString();
        if (message.contains(" @") || (message.length() > 0 && message.charAt(0) == '@')) {
            int position;
            int updatePos;
            if ((message.charAt(0) == '@')) {
                position = message.indexOf("@");
                updatePos = position;
            } else {
                position = message.indexOf(" @");
                updatePos = position + 1;
            }
            int cursorPosition = editText.getSelectionStart();

            String replacedMessage = message.substring(updatePos, cursorPosition);

            String nameWithSpace = (name + " ");
            String finalText = message.replace(replacedMessage, nameWithSpace);

            cursorPosition = updatePos + nameWithSpace.length();

            if (finalText.length() > cursorPosition) {
                char spaceChar = finalText.charAt(cursorPosition);

                if (spaceChar == ' ') {
                    finalText = message.replace(replacedMessage, name);
                    cursorPosition = updatePos + name.length() + 1;
                }
            }

            editText.setText(getNameWithFinalText(finalText, updatePos, cursorPosition, name));
            editText.setSelection(cursorPosition);
        }
    }

    // Mimo - 3, 7
    // Hi @Mimo Tumi kemon aso. @

    //1. Popup open houar shorto(@ or " @" ase ki na and @ or " @" thakle cursor er position theke koto shamne
    // @ ase tar shathe kono name mile jay ki na ta match kora)

    private SpannableString getNameWithFinalText(String fullText, int start, int end, String name) {

        for (int i = 0; i < selectedItems.size(); i++) {
            SelectedItem selectedItem = selectedItems.get(i);
            if (start <= selectedItem.getStart()) {
                int newNameLength = end - start;
                selectedItem.setStart((selectedItem.getStart() + newNameLength))
                        .setEnd((selectedItem.getEnd() + newNameLength));
            }
        }

        SelectedItem selectedItem = new SelectedItem().setName(name).setStart(start).setEnd(end);
        selectedItems.add(selectedItem);

        SpannableString txtSpannable= new SpannableString(fullText);

        for (SelectedItem retrieveItems : selectedItems) {
            StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
            txtSpannable.setSpan(boldSpan, retrieveItems.getStart(), retrieveItems.getEnd(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        return txtSpannable;
    }

    private void initRecyclerView() {

        itemArrayAdapter = new ItemArrayAdapter(R.layout.item_list, this);
        recyclerView = findViewById(R.id.item_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemArrayAdapter);

//        itemArrayAdapter.setNames(getItems(""));
    }

    private List<Item> getItems(String filterName) {
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
        return itemList;
    }

    private void setOperationText(String name) {
        String editMessage = editText.getText().toString();

        int cursorPosition = editText.getSelectionStart();

        String[] spaceTexts = editMessage.split(" ");
        for (String texts : spaceTexts) {
            if (texts.startsWith("@")) {

            }
        }
    }

    private boolean operation(String message) {
        if (TextUtils.isEmpty(message))
            return false;

        if (message.contains("@")) {
            String[] spaceChar = message.split(" ");
            for (String name : spaceChar) {
                if (name.contains("@") && name.startsWith("@")) {
                    return true;
                }
            }
        }
        return true;
    }
}
