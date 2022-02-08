package com.example.saansapp1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.saansapp1.adapters.MainListRvAdapter;
import com.example.saansapp1.model.MainListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {

    TextView tvDate, tvInputName, tvTotalCount, tvTotalAmount, tvSave, tvA, tvB, tvC, tvALL1, tvAB, tvAC, tvBC, tvALL2, tvSuper, tvBoxx, tvBoxSu, tvResponse;
    EditText etTicket, etCount;
    RadioGroup rg;
    RadioButton rb1, rb2, rb3;
    ImageView ivReport;
    RequestQueue requestQueue;
    RecyclerView rv;
    ConstraintLayout clRb1, clRb2, clRb3;
    ProgressBar pbRv;

    ArrayList<MainListModel> listArray = new ArrayList<>();

    String currentDate;

    String[] buttonName = new String[3];
    String[] buttonPrice = new String[3];

    String buttonName3, buttonPrice3, buttonNameAll;

    Integer etInputSize = 1;

    int tagA = 1, tagB = 1, tagC = 1, tagAB = 1, tagAC = 1, tagBC = 1;

    public String nameFromAdapter, dateTimeFromAdapter, ticketFromAdapter;

    Integer itemsCount = 0, itemSelected = 0;
    Integer tCount = 0, tAmount = 0;

    String insertSalesWith3CombinationsUrl = "http://teamabroad.online/insertSalesWith3Combinations.php";
    String insertSalesUrl = "http://teamabroad.online/insertSales.php";
    String getSalesWithDateTimeUrl = "http://teamabroad.online/getSalesWithDateTime.php";
    String insertSalesAll1Url = "http://teamabroad.online/insertSalesAll1.php";
    String insertSalesAll2Url = "http://teamabroad.online/insertSalesAll2.php";
    String dropSalesUrl = "http://teamabroad.online/dropSales.php";

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        getCurrentDate();

        controlButtons();

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        rb1.setChecked(true);
        rg.setOnCheckedChangeListener((radioGroup, i) -> {
            if (rb1.isChecked()) {
                controlButtons();
                etInputSize = 1;
                clRb1.setVisibility(View.VISIBLE);
                clRb2.setVisibility(View.GONE);
                clRb3.setVisibility(View.GONE);
                etTicket.getText().clear();
                etTicket.setFilters(new InputFilter[]{new InputFilter.LengthFilter(1)});
            } else if (rb2.isChecked()) {
                controlButtons();
                etInputSize = 2;
                clRb2.setVisibility(View.VISIBLE);
                clRb3.setVisibility(View.GONE);
                etTicket.getText().clear();
                clRb1.setVisibility(View.GONE);
                etTicket.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});
            } else if (rb3.isChecked()) {
                etInputSize = 3;
                clRb3.setVisibility(View.VISIBLE);
                clRb2.setVisibility(View.GONE);
                clRb1.setVisibility(View.GONE);
                etTicket.getText().clear();
                etTicket.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
            }
        });

        ivReport.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ReportActivity.class);
            MainActivity.this.startActivity(intent);
        });

        tvSave.setOnClickListener(View -> {
            itemsCount = 0;
            listItems();
            Toast toast = Toast.makeText(MainActivity.this, "Data Saved", Toast.LENGTH_SHORT);
            toast.show();
        });


        tvA.setOnClickListener(view -> {

            buttonNameAll = "";

            if (tagA == 1) {

                if (buttonName[0] == "") {
                    buttonName[0] = "A";
                }

                tagA = 0;
            } else if (tagA == 0) {

                if (buttonName[0] == "A") {
                    buttonName[0] = "";
                }

                tagA = 1;
            }

            tvInputName.setText("Name: " + buttonName[0] + "  " + buttonName[1] + "  " + buttonName[2]);


        });

        tvB.setOnClickListener(view -> {
            buttonNameAll = "";

            if (tagB == 1) {

                if (buttonName[1] == "") {
                    buttonName[1] = "B";
                }

                tagB = 0;
            } else if (tagB == 0) {

                if (buttonName[1] == "B") {
                    buttonName[1] = "";
                }

                tagB = 1;
            }

            tvInputName.setText("Name: " + buttonName[0] + "  " + buttonName[1] + "  " + buttonName[2]);

        });

        tvC.setOnClickListener(view -> {
            buttonNameAll = "";

            if (tagC == 1) {

                if (buttonName[2] == "") {
                    buttonName[2] = "C";
                }

                tagC = 0;
            } else if (tagC == 0) {

                if (buttonName[2] == "C") {
                    buttonName[2] = "";
                }

                tagC = 1;
            }

            tvInputName.setText("Name: " + buttonName[0] + "  " + buttonName[1] + "  " + buttonName[2]);

        });

        tvALL1.setOnClickListener(view -> {
            buttonNameAll = "ALL1";
            tvInputName.setText("Name: ALL 1");
        });


        tvAB.setOnClickListener(view -> {
            buttonNameAll = "";

            if (tagAB == 1) {

                if (buttonName[0] == "") {
                    buttonName[0] = "AB";
                }

                tagAB = 0;
            } else if (tagAB == 0) {

                if (buttonName[0] == "AB") {
                    buttonName[0] = "";
                }

                tagAB = 1;
            }

            tvInputName.setText("Name: " + buttonName[0] + "  " + buttonName[1] + "  " + buttonName[2]);

        });

        tvAC.setOnClickListener(view -> {
            buttonNameAll = "";

            if (tagAC == 1) {

                if (buttonName[1] == "") {
                    buttonName[1] = "AC";
                }

                tagAC = 0;
            } else if (tagAC == 0) {

                if (buttonName[1] == "AC") {
                    buttonName[1] = "";
                }

                tagAC = 1;
            }

            tvInputName.setText("Name: " + buttonName[0] + "  " + buttonName[1] + "  " + buttonName[2]);

        });

        tvBC.setOnClickListener(view -> {
            buttonNameAll = "";

            if (tagBC == 1) {

                if (buttonName[2] == "") {
                    buttonName[2] = "BC";
                }

                tagBC = 0;
            } else if (tagBC == 0) {

                if (buttonName[2] == "BC") {
                    buttonName[2] = "";
                }

                tagBC = 1;
            }

            tvInputName.setText("Name: " + buttonName[0] + "  " + buttonName[1] + "  " + buttonName[2]);

        });

        tvALL2.setOnClickListener(view -> {
            buttonNameAll = "ALL2";
            tvInputName.setText("Name: ALL 2");
        });

        tvSuper.setOnClickListener(view -> {
            buttonNameAll = "";
            buttonName3 = "SUPER";
            buttonPrice3 = "9";
            tvInputName.setText("Name: " + buttonName3);
        });

        tvBoxx.setOnClickListener(view -> {
            buttonNameAll = "";
            buttonName3 = "BOXX";
            buttonPrice3 = "9";
            tvInputName.setText("Name: " + buttonName3);
        });

        tvBoxSu.setOnClickListener(view -> {
            buttonNameAll = "";
            buttonName3 = "BOX-SU";
            buttonPrice3 = "9";
            tvInputName.setText("Name: " + buttonName3);
        });

        etTicket.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (etTicket.getText().toString().length() == etInputSize) {
                    etCount.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        etCount.setOnEditorActionListener((v, actionId, event) -> {
            if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_NEXT)) {

                if (buttonName == null) {
                    Toast.makeText(MainActivity.this, "Please select a text", Toast.LENGTH_SHORT).show();
                } else if (buttonNameAll == "ALL1") {
                    insertAll1Values();
                } else if (buttonNameAll == "ALL2") {
                    insertAll2Values();
                } else if (buttonName3 == "BOXX" || buttonName3 == "BOX-SU") {
                    insertSalesWith3Combinations();
                } else if (buttonName3 == "SUPER") {
                    insertValuesSuper();
                } else {
                    insertValues();
                }
            }
            return false;
        });

    }

    private void controlButtons() {

        tagA = 1;
        tagB = 1;
        tagC = 1;
        tagAB = 1;
        tagAC = 1;
        tagBC = 1;

        buttonName[0] = "";
        buttonName[1] = "";
        buttonName[2] = "";
        buttonPrice[0] = "";
        buttonPrice[1] = "";
        buttonPrice[2] = "";
    }

    private void insertSalesWith3Combinations() {
        String txt1 = etCount.getText().toString();
        String txt2 = etTicket.getText().toString();
        if (txt2.matches("")) {
            Context context = getApplicationContext();
            CharSequence text = "Please enter ticket!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (txt1.matches("")) {
            Context context = getApplicationContext();
            CharSequence text = "Please enter count!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {

            pbRv.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, insertSalesWith3CombinationsUrl, response -> {

                String resp = response;
                Integer len = resp.length();

                itemsCount = itemsCount + len;

                pbRv.setVisibility(View.GONE);

//                itemsCount = itemsCount + 3;
                listItems();
                etCount.getText().clear();
                etTicket.getText().clear();
            }, error -> {

            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("name", buttonName3);
                    parameters.put("price", buttonPrice3);
                    parameters.put("ticket", etTicket.getText().toString());
                    parameters.put("count", etCount.getText().toString());
                    parameters.put("status", "1");

                    return parameters;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    private void insertAll2Values() {
        String txt1 = etCount.getText().toString();
        String txt2 = etTicket.getText().toString();
        if (txt2.matches("")) {
            Context context = getApplicationContext();
            CharSequence text = "Please enter ticket!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (txt1.matches("")) {
            Context context = getApplicationContext();
            CharSequence text = "Please enter count!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {

            pbRv.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, insertSalesAll2Url, response -> {
                pbRv.setVisibility(View.GONE);
//                listItems1();
                itemsCount = itemsCount + 3;
                listItems();
                etTicket.getText().clear();
                etCount.getText().clear();
            }, error -> {

            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("ticket", etTicket.getText().toString());
                    parameters.put("count", etCount.getText().toString());
                    parameters.put("status", "1");


                    return parameters;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    private void insertAll1Values() {
        String txt1 = etCount.getText().toString();
        String txt2 = etTicket.getText().toString();
        if (txt2.matches("")) {
            Context context = getApplicationContext();
            CharSequence text = "Please enter ticket!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (txt1.matches("")) {
            Context context = getApplicationContext();
            CharSequence text = "Please enter count!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {

            pbRv.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, insertSalesAll1Url, response -> {
                pbRv.setVisibility(View.GONE);
//                listItems1();
                itemsCount = itemsCount + 3;
                listItems();
                etCount.getText().clear();
                etTicket.getText().clear();
            }, error -> {

            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> parameters = new HashMap<>();
                    parameters.put("ticket", etTicket.getText().toString());
                    parameters.put("count", etCount.getText().toString());
                    parameters.put("status", "1");


                    return parameters;
                }
            };
            requestQueue.add(stringRequest);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    private void getCurrentDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter dtfReq = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();
        String date1 = dtf.format(now);
        currentDate = dtfReq.format(now);
        tvDate.setText(date1);
    }

    private void listItems1() {
        MainListRvAdapter adapter = new MainListRvAdapter(listArray, MainActivity.this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        tvResponse.setVisibility(View.GONE);
    }

    public void dropSales() {

        pbRv.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, dropSalesUrl, response -> {
            pbRv.setVisibility(View.GONE);
//            listItems1();
            itemsCount = itemsCount - 1;
            listItems();
            etCount.getText().clear();
            etTicket.getText().clear();
        }, error -> {
            Toast toast = Toast.makeText(MainActivity.this, "An error occurred \n Please check your internet connection..", Toast.LENGTH_SHORT);
            toast.show();
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> parameters = new HashMap<>();
                parameters.put("name", nameFromAdapter);
                parameters.put("dateTime", dateTimeFromAdapter);
                parameters.put("ticket", ticketFromAdapter);

                return parameters;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void listItems() {

        listArray.clear();
        tCount = 0;
        tAmount = 0;
        StringRequest strRequest = new StringRequest(Request.Method.POST, getSalesWithDateTimeUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String json = response;

                        try {

                            JSONObject obj = new JSONObject(json);

                            JSONArray list = obj.getJSONArray("sales");
                            Log.e("_____________resp: ", String.valueOf(list));
                            String[] names = new String[list.length()];
                            for (int i = 0; i < itemsCount; i++) {
                                JSONObject item = list.getJSONObject(i);

                                String name = item.getString("name");
                                String ticket = item.getString("ticket");
                                String count = item.getString("count");
                                String price = item.getString("price");
                                String dateTime = item.getString("dateTime");

                                int ticket1 = Integer.parseInt(ticket);
                                int count1 = Integer.parseInt(count);
                                int price1 = Integer.parseInt(price);

                                tAmount = tAmount + (count1 * price1);
                                tCount = tCount + count1;

                                listArray.add(
                                        new MainListModel(
                                                name,
                                                ticket1,
                                                count1,
                                                price1,
                                                dateTime,
                                                0
                                        )
                                );

                            }

                            setListItemsAdapter();

                            tvTotalAmount.setText("Total Amount: " + tAmount.toString());
                            tvTotalCount.setText("Total Count: " + tCount.toString());

                            pbRv.setVisibility(View.GONE);

                        } catch (JSONException e) {
                            Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("date", currentDate);
                return params;
            }
        };

        requestQueue.add(strRequest);
    }

    private void setListItemsAdapter() {
        MainListRvAdapter adapter = new MainListRvAdapter(listArray, MainActivity.this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        tvResponse.setVisibility(View.GONE);
        pbRv.setVisibility(View.GONE);
    }

    private void listItems2() {
        pbRv.setVisibility(View.VISIBLE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                getSalesWithDateTimeUrl, response -> {

            listArray.clear();

            try {
                tAmount = 0;
                tCount = 0;
                JSONArray list = response.getJSONArray("sales");
                Log.i("_____________resp: ", String.valueOf(list));
                String[] names = new String[list.length()];
                for (int i = 0; i < itemsCount; i++) {
                    JSONObject item = list.getJSONObject(i);

                    String name = item.getString("name");
                    String ticket = item.getString("ticket");
                    String count = item.getString("count");
                    String price = item.getString("price");
                    String dateTime = item.getString("dateTime");

                    int ticket1 = Integer.parseInt(ticket);
                    int count1 = Integer.parseInt(count);
                    int price1 = Integer.parseInt(price);

                    tCount = tCount + count1;
                    tAmount = tAmount + (price1 * count1);

                    listArray.add(
                            new MainListModel(
                                    name,
                                    ticket1,
                                    count1,
                                    price1,
                                    dateTime,
                                    0
                            )
                    );

                }

                tvTotalAmount.setText("Total Amount: " + tAmount);
                tvTotalCount.setText("Total Count: " + tCount);

                MainListRvAdapter adapter = new MainListRvAdapter(listArray, MainActivity.this);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(this));
                tvResponse.setVisibility(View.GONE);
                pbRv.setVisibility(View.GONE);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

        });
        requestQueue.add(jsonObjectRequest);

    }

    private void insertValuesSuper() {

        String txt1 = etCount.getText().toString();
        String txt2 = etTicket.getText().toString();
        if (txt2.matches("")) {
            Context context = getApplicationContext();
            CharSequence text = "Please enter ticket!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (txt1.matches("")) {
            Context context = getApplicationContext();
            CharSequence text = "Please enter count!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            pbRv.setVisibility(View.VISIBLE);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, insertSalesUrl, response -> {
                pbRv.setVisibility(View.GONE);
//                listItems1();
                itemsCount = itemsCount + 1;
                listItems();
                etTicket.getText().clear();
                etCount.getText().clear();

            }, error -> {
                Toast toast = Toast.makeText(MainActivity.this, "An error occurred \n Please check your internet connection..", Toast.LENGTH_SHORT);
                toast.show();
            }) {
                @Override
                protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                    Map<String, String> parameters = new HashMap<String, String>();
                    parameters.put("name", buttonName3);
                    parameters.put("ticket", etTicket.getText().toString());
                    parameters.put("count", etCount.getText().toString());
                    parameters.put("price", buttonPrice3);
                    parameters.put("status", "1");

                    return parameters;
                }
            };
            requestQueue.add(stringRequest);
        }
    }

    private void insertValues() {

        String txt1 = etCount.getText().toString();
        String txt2 = etTicket.getText().toString();
        if (txt2.matches("")) {
            Context context = getApplicationContext();
            CharSequence text = "Please enter ticket!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (txt1.matches("")) {
            Context context = getApplicationContext();
            CharSequence text = "Please enter count!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {

            if (buttonName[0] == "") {
                if (buttonName[1] == "") {
                    if (buttonName[2] == "") {
                        Toast.makeText(MainActivity.this, "Please select a name", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            if (buttonName[0] != "" && buttonName[1] != "" && buttonName[2] != "") {
                Toast.makeText(MainActivity.this, "Please select any 2 items", Toast.LENGTH_SHORT).show();
            } else {
                int count1 = 0;
                for (int i = 0; i < buttonName.length; i++) {
                    if (buttonName[i] == "A" || buttonName[i] == "AB") {
                        count1 = 0;
                    }
                    if (buttonName[i] == "B" || buttonName[i] == "AC") {
                        count1 = 1;
                    }
                    if (buttonName[i] == "C" || buttonName[i] == "BC") {
                        count1 = 2;
                    }
                }
                for (int i = 0; i < buttonName.length; i++) {
                    if (buttonName[i] != "") {
                        pbRv.setVisibility(View.VISIBLE);
                        int finalI = i;
                        int finalCount = count1;
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, insertSalesUrl, response -> {
                            pbRv.setVisibility(View.GONE);
//                listItems1();
                            itemsCount = itemsCount + 1;
                            Log.e("----------", "finalCount: " + finalCount);
                            if (finalI == finalCount) {
                                listItems();
                                etTicket.getText().clear();
                                etCount.getText().clear();
                            }

                        }, error -> {
                            Toast toast = Toast.makeText(MainActivity.this, "An error occurred \n Please check your internet connection..", Toast.LENGTH_SHORT);
                            toast.show();
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                                Map<String, String> parameters = new HashMap<String, String>();
                                parameters.put("name", buttonName[finalI]);
                                parameters.put("ticket", etTicket.getText().toString());
                                parameters.put("count", etCount.getText().toString());
                                parameters.put("price", buttonPrice[finalI]);
                                parameters.put("status", "1");

                                return parameters;
                            }
                        };
                        requestQueue.add(stringRequest);
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Exit..?");
        builder.setMessage("Do you want to exit?");
        builder.setPositiveButton("Yes", (dialog, id) -> {
            finish();
            finishAffinity();
        });
        builder.setNegativeButton("No", (dialog, id) -> {

        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void init() {
        ivReport = findViewById(R.id.ivReport);

        clRb1 = findViewById(R.id.clRb1);
        clRb2 = findViewById(R.id.clRb2);
        clRb3 = findViewById(R.id.clRb3);

        rg = findViewById(R.id.rg);
        rb1 = findViewById(R.id.rb1);
        rb2 = findViewById(R.id.rb2);
        rb3 = findViewById(R.id.rb3);


        etTicket = findViewById(R.id.etTicket);
        etCount = findViewById(R.id.etCount);


        tvDate = findViewById(R.id.tvDate);
        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
//        tvDeletePink = findViewById(R.id.tvDeletePink);
//        tvDeleteGray = findViewById(R.id.tvDeleteGray);
//        tvCancel = findViewById(R.id.tvCancel);
        tvSave = findViewById(R.id.tvSave);
        tvResponse = findViewById(R.id.tvResponse);
        tvInputName = findViewById(R.id.tvInputName);

        tvA = findViewById(R.id.tvA);
        tvB = findViewById(R.id.tvB);
        tvC = findViewById(R.id.tvC);
        tvALL1 = findViewById(R.id.tvALL);

        tvAB = findViewById(R.id.tvAB);
        tvAC = findViewById(R.id.tvAC);
        tvBC = findViewById(R.id.tvBC);
        tvALL2 = findViewById(R.id.tvALL2);

        tvSuper = findViewById(R.id.tvSuper);
        tvBoxx = findViewById(R.id.tvBoxx);
        tvBoxSu = findViewById(R.id.tvBoxSu);


        rv = findViewById(R.id.rv);

        pbRv = findViewById(R.id.pbRv);
    }
}