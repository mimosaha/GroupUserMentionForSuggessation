package com.go.hungrynaki.groupmentioneditbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView recyclerView;
    private EditText editText;

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

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.row_item:
                String name = (String) v.getTag();
                executeMessage(name);
                break;
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

    String prevMessage;

    private void executeMessage(String name) {

        String message = editText.getText().toString();
        if (message.contains(" @")) {
            int position = message.indexOf(" @");
            int updatePos = position + 1;
            int cursorPosition = editText.getSelectionStart();

            String replacedMessage = message.substring(updatePos, cursorPosition);

            String nameWithSpace = (name + " ");
            String finalText = message.replace(replacedMessage, nameWithSpace);

            cursorPosition = updatePos + nameWithSpace.length();

            char spaceChar = finalText.charAt(cursorPosition);

            if (spaceChar == ' ') {
                finalText = message.replace(replacedMessage, name);
                cursorPosition = updatePos + name.length() + 1;
            }

            editText.setText(finalText);
            editText.setSelection(cursorPosition);
        }


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

    private void initRecyclerView() {

        ArrayList<Item> itemList = new ArrayList<>();

        String [] names = {"Mimo", "Tashfin", "Mukit", "Farhan", "Rashedul"};

        for (String name : names) {
            itemList.add(new Item(name));
        }

        ItemArrayAdapter itemArrayAdapter = new ItemArrayAdapter(R.layout.item_list, itemList, this);
        recyclerView = (RecyclerView) findViewById(R.id.item_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(itemArrayAdapter);
    }
}
