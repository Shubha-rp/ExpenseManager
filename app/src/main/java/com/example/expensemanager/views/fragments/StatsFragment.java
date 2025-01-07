package com.example.expensemanager.views.fragments;

import static com.example.expensemanager.utils.Constants.SELECTED_TAB_STATS_TYPE;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.AnyChart;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.anychart.enums.Align;
import com.anychart.enums.LegendLayout;
import com.example.expensemanager.R;
import com.example.expensemanager.databinding.FragmentStatsBinding;
import com.example.expensemanager.models.Transaction;
import com.example.expensemanager.utils.Constants;
import com.example.expensemanager.utils.Helper;
import com.example.expensemanager.viewmodels.MainViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.RealmResults;


public class StatsFragment extends Fragment {



    public StatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    FragmentStatsBinding binding;
    Calendar calendar;
    public MainViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentStatsBinding.inflate(inflater);
        viewModel = new ViewModelProvider(requireActivity()).get(MainViewModel.class);
        calendar= Calendar.getInstance();
        updateDate();
        binding.incomeBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.income_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.greenColor));
           SELECTED_TAB_STATS_TYPE=Constants.INCOME;
           updateDate();
        });
        binding.expenseBtn.setOnClickListener(view -> {
            binding.incomeBtn.setBackground(getContext().getDrawable(R.drawable.default_selector));
            binding.expenseBtn.setBackground(getContext().getDrawable(R.drawable.expense_selector));
            binding.incomeBtn.setTextColor(getContext().getColor(R.color.textColor));
            binding.expenseBtn.setTextColor(getContext().getColor(R.color.redColor));
            SELECTED_TAB_STATS_TYPE=Constants.EXPENSE;
            updateDate();
        });

        binding.nextDateBtn.setOnClickListener(c->{
            if(Constants.SELECTED_TAB_STATS==Constants.DAILY){
                calendar.add(Calendar.DATE,1);
            }else if (Constants.SELECTED_TAB_STATS==Constants.MONTHLY){
                calendar.add(Calendar.MONTH,1);
            }

            updateDate();
        });
        binding.previousDateBtn.setOnClickListener(c->{
            if (Constants.SELECTED_TAB_STATS==Constants.DAILY){
                calendar.add(Calendar.DATE,-1);
            } else if (Constants.SELECTED_TAB_STATS==Constants.MONTHLY) {
                calendar.add(Calendar.MONTH,-1);

            }

            updateDate();
        });
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals("Daily")){
//                    Toast.makeText(MainActivity.this,tab.getText().toString(),Toast.LENGTH_SHORT).show();
                    Constants.SELECTED_TAB_STATS=0;
                    updateDate();

                } else if (tab.getText().equals("Monthly")) {

                    Constants.SELECTED_TAB_STATS=1;
                    updateDate();

                }
                //Toast.makeText(MainActivity.this,tab.getText().toString(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
            });

        Pie pie = AnyChart.pie();

        viewModel.categoriesTransactions.observe(getViewLifecycleOwner(), new Observer<RealmResults<Transaction>>() {
            @Override
            public void onChanged(RealmResults<Transaction> transactions) {

                if(transactions.size()>0){

                    binding.emptyState.setVisibility(View.GONE);
                    binding.anyChart.setVisibility(View.VISIBLE);
                    List<DataEntry> data = new ArrayList<>();
                    Map<String,Double> categroyMap=new HashMap<>();
                    for(Transaction transaction:transactions){
                        String category=transaction.getCategory();
                        double amount=transaction.getAmount();
                        if(categroyMap.containsKey(category)){
                            double currentTotal=categroyMap.get(category).doubleValue();
                            currentTotal+=Math.abs(amount);

                            categroyMap.put(category,currentTotal);
                        }else{
                            categroyMap.put(category,Math.abs(amount));
                        }
                    }
                    for (Map.Entry<String,Double> entry:categroyMap.entrySet()){
                       data.add(new ValueDataEntry(entry.getKey(),entry.getValue()));

                    }
                    pie.data(data);
                }else {
                    binding.emptyState.setVisibility(View.VISIBLE);
                    binding.anyChart.setVisibility(View.GONE);
                }
            }
        });
        viewModel.getTransactions(calendar,SELECTED_TAB_STATS_TYPE);

        binding.anyChart.setChart(pie);
        return binding.getRoot();
    }
    void updateDate(){
        if (Constants.SELECTED_TAB_STATS==Constants.DAILY){

            binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
        }else if (Constants.SELECTED_TAB_STATS==Constants.MONTHLY){

            binding.currentDate.setText(Helper.formatDateByMonth(calendar.getTime()));
        }
//    SimpleDateFormat dateFormat=new SimpleDateFormat("dd MMMM,yyyy");

        viewModel.getTransactions(calendar,SELECTED_TAB_STATS_TYPE);
    }

}