package com.example.saansapp1;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.saansapp1.adapters.ReportListRvAdapter;
import com.example.saansapp1.model.MainListModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ReportActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    RecyclerView rv;
    ProgressBar pbRv;

    EditText etSearch;

    TextView tvTotalCount, tvTotalAmount, tvResponse, tvDate, tvFilter, tvRvCount, tvRvDAmt, tvRvNo, tvRvName, tvExcluded;

    RequestQueue requestQueue;

    ImageView ivSaved, ivSearch, ivCross;

    ArrayList<MainListModel> listArray = new ArrayList<>();
    ArrayList<MainListModel> listArray1 = new ArrayList<>();

    String searchWithTicketUrl = "http://teamabroad.online/searchWithTicket.php";
    String getSalesWithDateUrl = "http://teamabroad.online/getSalesWithDate.php";
    String sortItemsWithCountUrl = "http://teamabroad.online/sortItemsWithCount.php";
    String filterSalesBasedNameUrl = "http://teamabroad.online/filterSalesBasedName.php";

    Integer totalCount = 0, totalAmount = 0, exclBtnValue = 0;
    String currentDate, filter, sort, exclFilter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        requestQueue = Volley.newRequestQueue(ReportActivity.this);

        init();
        getCurrentDate();

        listItems();

        ivSearch.setOnClickListener(view -> {
            etSearch.setVisibility(View.VISIBLE);
            ivCross.setVisibility(View.VISIBLE);
            ivSearch.setVisibility(View.GONE);
        });

        ivCross.setOnClickListener(view -> {
            etSearch.getText().clear();
            etSearch.setVisibility(View.GONE);
            ivCross.setVisibility(View.GONE);
            ivSearch.setVisibility(View.VISIBLE);
            listItems();
        });

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch();
                Log.e("-----: ", "Search Ok");
                return true;
            }
            return false;
        });

        ivSaved.setOnClickListener(view -> {
            finish();
        });

        tvDate.setText(currentDate);

        tvDate.setOnClickListener(view -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(), "DATE PICK");
        });

        tvRvName.setOnClickListener(view -> {
            sort = "name";
            sortItemsWithCount();
        });

        tvRvCount.setOnClickListener(view -> {
            sort = "count";
            sortItemsWithCount();
        });

        tvRvNo.setOnClickListener(view -> {
            sort = "ticket";
            sortItemsWithCount();
        });

        tvRvDAmt.setOnClickListener(view -> {
            sort = "price";
            sortItemsWithCount();
        });

        tvExcluded.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(ReportActivity.this, tvExcluded);
            popup.getMenuInflater().inflate(R.menu.report_filter_excl_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(menuItem -> {

                exclFilter = menuItem.getTitle().toString();

                if (exclFilter.equals("Excl: 1")) {
                    tvExcluded.setText(exclFilter);
                    int a = 9;
                    excludedItems(a);
                }
                if (exclFilter.equals("Excl: 2")) {
                    int a = 99;
                    tvExcluded.setText(exclFilter);
                    excludedItems(a);
                }
                if (exclFilter.equals("Excl: 3")) {
                    int a = 999;
                    tvExcluded.setText(exclFilter);
                    excludedItems(a);
                }

                Toast.makeText(ReportActivity.this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            });

            popup.show();
        });

        tvFilter.setOnClickListener(View -> {
            PopupMenu popup = new PopupMenu(ReportActivity.this, tvFilter);
            popup.getMenuInflater().inflate(R.menu.report_filter_menu, popup.getMenu());

            popup.setOnMenuItemClickListener(menuItem -> {
                filter = menuItem.getTitle().toString();
                tvFilter.setText(filter);
                if (filter.equals("ALL")) {
                    listItems();
                } else {
                    filterItems();
                }
                Toast.makeText(ReportActivity.this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            });

            popup.show();
        });

    }

    private void excludedItems(int a) {

        listArray1.clear();

        pbRv.setVisibility(View.VISIBLE);

        if (a == 9) {
            int[] inTicket = new int[9];
            ArrayList<Integer> exTicket = new ArrayList<Integer>();

            for (int i = a-1; i >= 0; i--) {
                exTicket.add(i);
            }

            for (int j = 0; j < listArray.size(); j++) {
                for (int k = 0; k < exTicket.size(); k++) {
                    if (listArray.get(j).getTicket() == exTicket.get(k)) {
                        exTicket.remove(k);
                    }
                }
            }

            for (int i = 0; i < exTicket.size(); i++) {
                listArray1.add(
                        new MainListModel(
                                "-",
                                exTicket.get(i),
                                0,
                                0,
                                "",
                                1
                        )
                );
            }

            ReportListRvAdapter adapter = new ReportListRvAdapter(listArray1);
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(this));
            tvResponse.setVisibility(View.GONE);
            pbRv.setVisibility(View.GONE);

        }
        if (a == 99) {
            int[] inTicket = new int[99];
            ArrayList<Integer> exTicket = new ArrayList<Integer>();

            for (int i = a-1; i >= 10; i--) {
                exTicket.add(i);
            }

            for (int j = 0; j < listArray.size(); j++) {
                for (int k = 0; k < exTicket.size(); k++) {
                    if (listArray.get(j).getTicket() == exTicket.get(k)) {
                        exTicket.remove(k);
                    }
                }
            }

            for (int i = 0; i < exTicket.size(); i++) {
                listArray1.add(
                        new MainListModel(
                                "-",
                                exTicket.get(i),
                                0,
                                0,
                                "",
                                1
                        )
                );
            }

            ReportListRvAdapter adapter = new ReportListRvAdapter(listArray1);
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(this));
            tvResponse.setVisibility(View.GONE);
            pbRv.setVisibility(View.GONE);

        }
        if (a == 999) {
            int[] inTicket = new int[999];
            ArrayList<Integer> exTicket = new ArrayList<Integer>();

            for (int i = a-1; i >= 100; i--) {
                exTicket.add(i);
            }

            for (int j = 0; j < listArray.size(); j++) {
                for (int k = 0; k < exTicket.size(); k++) {
                    if (listArray.get(j).getTicket() == exTicket.get(k)) {
                        exTicket.remove(k);
                    }
                }
            }

            for (int i = 0; i < exTicket.size(); i++) {
                listArray1.add(
                        new MainListModel(
                                "-",
                                exTicket.get(i),
                                0,
                                0,
                                "",
                                1
                        )
                );
            }

            ReportListRvAdapter adapter = new ReportListRvAdapter(listArray1);
            rv.setAdapter(adapter);
            rv.setLayoutManager(new LinearLayoutManager(this));
            tvResponse.setVisibility(View.GONE);
            pbRv.setVisibility(View.GONE);

        }


    }

    private void performSearch() {

        listArray.clear();
        totalCount = 0;
        totalAmount = 0;

        StringRequest strRequest = new StringRequest(Request.Method.POST, searchWithTicketUrl,
                response -> {

                    String json = response;

                    try {

                        JSONObject obj = new JSONObject(json);

                        JSONArray list = obj.getJSONArray("sales");
                        Log.e("_____________resp: ", String.valueOf(list));
                        String[] names = new String[list.length()];
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject item = list.getJSONObject(i);

                            String name = item.getString("name");
                            String ticket = item.getString("ticket");
                            String count = item.getString("count");
                            String price = item.getString("price");
                            String status = item.getString("status");

                            int ticket1 = Integer.parseInt(ticket);
                            int count1 = Integer.parseInt(count);
                            int price1 = Integer.parseInt(price);
                            int status1 = Integer.parseInt(status);

                            totalAmount = totalAmount + (count1 * price1);
                            totalCount = totalCount + count1;

                            listArray.add(
                                    new MainListModel(
                                            name,
                                            ticket1,
                                            count1,
                                            price1,
                                            status,
                                            0
                                    )
                            );

                        }

                        setListItemsAdapter();

                        tvTotalAmount.setText("Total Amount: " + totalAmount.toString());
                        tvTotalCount.setText("Total Count: " + totalCount.toString());

                        pbRv.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
                    }
                },
                error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("date", currentDate);
                params.put("ticket", etSearch.getText().toString());
                Log.e("_____________req: ", params.toString());
                return params;
            }
        };

        requestQueue.add(strRequest);
    }

    private void sortItemsWithCount() {

        if (filter == null) {
            filter = "ALL";
        }

        listArray.clear();
        totalCount = 0;
        totalAmount = 0;
        StringRequest strRequest = new StringRequest(Request.Method.POST, sortItemsWithCountUrl,
                response -> {

                    String json = response;

                    try {

                        JSONObject obj = new JSONObject(json);

                        JSONArray list = obj.getJSONArray("sales");
                        Log.e("_____________req: ", String.valueOf(list));
                        String[] names = new String[list.length()];
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject item = list.getJSONObject(i);

                            String name = item.getString("name");
                            String ticket = item.getString("ticket");
                            String count = item.getString("count");
                            String price = item.getString("price");
                            String status = item.getString("status");

                            int ticket1 = Integer.parseInt(ticket);
                            int count1 = Integer.parseInt(count);
                            int price1 = Integer.parseInt(price);
                            int status1 = Integer.parseInt(status);

                            totalAmount = totalAmount + (count1 * price1);
                            totalCount = totalCount + count1;

                            listArray.add(
                                    new MainListModel(
                                            name,
                                            ticket1,
                                            count1,
                                            price1,
                                            status,
                                            0
                                    )
                            );

                        }

                        setListItemsAdapter();

                        tvTotalAmount.setText("Total Amount: " + totalAmount.toString());
                        tvTotalCount.setText("Total Count: " + totalCount.toString());

                        pbRv.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
                    }
                },
                error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", filter);
                params.put("date", currentDate);
                params.put("order", sort);
                return params;
            }
        };

        requestQueue.add(strRequest);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);


        String currentDateString = DateFormat.getDateInstance(DateFormat.DATE_FIELD).format(c.getTime());

        tvDate.setText(currentDateString);

        currentDate = year + "-" + (month + 1) + "-" + dayOfMonth;

        listItems();

    }

    private void filterItems() {
        listArray.clear();
        totalCount = 0;
        totalAmount = 0;
        StringRequest strRequest = new StringRequest(Request.Method.POST, filterSalesBasedNameUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String json = response;

                        try {

                            JSONObject obj = new JSONObject(json);

                            JSONArray list = obj.getJSONArray("sales");
                            Log.e("_____________resp: ", String.valueOf(list));
                            String[] names = new String[list.length()];
                            for (int i = 0; i < list.length(); i++) {
                                JSONObject item = list.getJSONObject(i);

                                String name = item.getString("name");
                                String ticket = item.getString("ticket");
                                String count = item.getString("count");
                                String price = item.getString("price");
                                String status = item.getString("status");

                                int ticket1 = Integer.parseInt(ticket);
                                int count1 = Integer.parseInt(count);
                                int price1 = Integer.parseInt(price);
                                int status1 = Integer.parseInt(status);

                                totalAmount = totalAmount + (count1 * price1);
                                totalCount = totalCount + count1;

                                listArray.add(
                                        new MainListModel(
                                                name,
                                                ticket1,
                                                count1,
                                                price1,
                                                status,
                                                0
                                        )
                                );

                            }

                            setListItemsAdapter();

                            tvTotalAmount.setText("Total Amount: " + totalAmount.toString());
                            tvTotalCount.setText("Total Count: " + totalCount.toString());

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
                params.put("name", filter);
                params.put("date", currentDate);
                return params;
            }
        };

        requestQueue.add(strRequest);
    }

    private void setListItemsAdapter() {
        ReportListRvAdapter adapter = new ReportListRvAdapter(listArray);
        rv.setAdapter(adapter);
        rv.setLayoutManager(new LinearLayoutManager(this));
        tvResponse.setVisibility(View.GONE);
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

    private void listItems() {

        listArray.clear();
        totalCount = 0;
        totalAmount = 0;
        StringRequest strRequest = new StringRequest(Request.Method.POST, getSalesWithDateUrl,
                response -> {

                    String json = response;

                    try {

                        JSONObject obj = new JSONObject(json);

                        JSONArray list = obj.getJSONArray("sales");
                        Log.e("_____________resp: ", String.valueOf(list));
                        String[] names = new String[list.length()];
                        for (int i = 0; i < list.length(); i++) {
                            JSONObject item = list.getJSONObject(i);

                            String name = item.getString("name");
                            String ticket = item.getString("ticket");
                            String count = item.getString("count");
                            String price = item.getString("price");
                            String status = item.getString("status");

                            int ticket1 = Integer.parseInt(ticket);
                            int count1 = Integer.parseInt(count);
                            int price1 = Integer.parseInt(price);
                            int status1 = Integer.parseInt(status);

                            totalAmount = totalAmount + (count1 * price1);
                            totalCount = totalCount + count1;

                            listArray.add(
                                    new MainListModel(
                                            name,
                                            ticket1,
                                            count1,
                                            price1,
                                            status,
                                            0
                                    )
                            );

                        }

                        setListItemsAdapter();

                        tvTotalAmount.setText("Total Amount: " + totalAmount.toString());
                        tvTotalCount.setText("Total Count: " + totalCount.toString());

                        pbRv.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"");
                    }
                },
                error -> Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("date", currentDate);
                return params;
            }
        };

        requestQueue.add(strRequest);
    }

    private void listItems1() {
        pbRv.setVisibility(View.VISIBLE);
        tvResponse.setVisibility(View.GONE);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                getSalesWithDateUrl, response -> {


            listArray.clear();

            try {
                JSONArray list = response.getJSONArray("sales");
                Log.i("_____________req: ", String.valueOf(list));
                String[] names = new String[list.length()];
                for (int i = 0; i < list.length(); i++) {
                    JSONObject item = list.getJSONObject(i);

                    String name = item.getString("name");
                    String ticket = item.getString("ticket");
                    String count = item.getString("count");
                    String price = item.getString("price");
                    String status = item.getString("status");

                    int ticket1 = Integer.parseInt(ticket);
                    int count1 = Integer.parseInt(count);
                    int price1 = Integer.parseInt(price);
                    int status1 = Integer.parseInt(status);

                    totalAmount = totalAmount + (count1 * price1);
                    totalCount = totalCount + count1;

                    listArray.add(
                            new MainListModel(
                                    name,
                                    ticket1,
                                    count1,
                                    price1,
                                    status,
                                    0
                            )
                    );

                }
                ReportListRvAdapter adapter = new ReportListRvAdapter(listArray);
                rv.setAdapter(adapter);
                rv.setLayoutManager(new LinearLayoutManager(this));

                tvTotalAmount.setText("Total Amount: " + totalAmount.toString());
                tvTotalCount.setText("Total Count: " + totalCount.toString());

                pbRv.setVisibility(View.GONE);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {

        });
        requestQueue.add(jsonObjectRequest);

    }


    private void init() {
        ivSaved = findViewById(R.id.ivSaved);
        rv = findViewById(R.id.rv);
        pbRv = findViewById(R.id.pbRv);
        tvTotalCount = findViewById(R.id.tvTotalCount);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        tvResponse = findViewById(R.id.tvResponse);
        tvDate = findViewById(R.id.tvDate);
        tvFilter = findViewById(R.id.tvFilter);
        tvRvCount = findViewById(R.id.tvRvCount);
        tvRvDAmt = findViewById(R.id.tvRvDAmt);
        tvRvName = findViewById(R.id.tvRvName);
        tvExcluded = findViewById(R.id.tvExcluded);
        tvRvNo = findViewById(R.id.tvRvNo);
        etSearch = findViewById(R.id.etSearch);
        ivSearch = findViewById(R.id.ivSearch);
        ivCross = findViewById(R.id.ivCross);
    }
}