package com.example.expensemanager.views.activities;

import android.os.Bundle;
import android.view.Menu;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.expensemanager.adapters.TransactionsAdapter;
import com.example.expensemanager.models.Transaction;
import com.example.expensemanager.utils.Constants;
import com.example.expensemanager.utils.Helper;
import com.example.expensemanager.views.fragments.AddTransactionFragment;
import com.example.expensemanager.R;
import com.example.expensemanager.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
ActivityMainBinding binding;
Calendar calendar;
//hii
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
        setSupportActionBar(binding.materialToolbar2);
        getSupportActionBar().setTitle("Transactions");
        Constants.setCategories();

        calendar=Calendar.getInstance();
        updateDate();
        binding.nextDateBtn.setOnClickListener(c->{
            calendar.add(Calendar.DATE,1);
            updateDate();
        });
        binding.previousDateBtn.setOnClickListener(c->{
            calendar.add(Calendar.DATE,-1);
            updateDate();
        });
        binding.floatingActionButton2.setOnClickListener(c->{
            new AddTransactionFragment().show(getSupportFragmentManager(), null);

        });
        ArrayList<Transaction>transactions=new ArrayList<>();
        transactions.add(new Transaction(Constants.INCOME,"Business","Cash","Some not here",new Date(),500,2));
        transactions.add(new Transaction(Constants.EXPENSE,"Investment","Bank","Some not here",new Date(),-900,4));
        transactions.add(new Transaction(Constants.INCOME,"Rent","Card","Some not here",new Date(),500,5));
        transactions.add(new Transaction(Constants.INCOME,"Business","Other","Some not here",new Date(),500,6));
        TransactionsAdapter transactionsAdapter=new TransactionsAdapter(this,transactions);
        binding.transactionsList.setLayoutManager(new LinearLayoutManager(this));
        binding.transactionsList.setAdapter(transactionsAdapter);
        }
void updateDate(){
//    SimpleDateFormat dateFormat=new SimpleDateFormat("dd MMMM,yyyy");
    binding.currentDate.setText(Helper.formatDate(calendar.getTime()));
}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
