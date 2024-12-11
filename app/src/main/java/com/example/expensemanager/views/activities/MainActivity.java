package com.example.expensemanager.views.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.expensemanager.adapters.TransactionsAdapter;
import com.example.expensemanager.models.Transaction;
import com.example.expensemanager.utils.Constants;
import com.example.expensemanager.utils.Helper;
import com.example.expensemanager.viewmodels.MainViewModel;
import com.example.expensemanager.views.fragments.AddTransactionFragment;
import com.example.expensemanager.R;
import com.example.expensemanager.databinding.ActivityMainBinding;
import com.example.expensemanager.views.fragments.StatsFragment;
import com.example.expensemanager.views.fragments.TransactionsFragment;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Calendar calendar;
    public MainViewModel viewModel;

    //hii
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        EdgeToEdge.enable(this);
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);


        setSupportActionBar(binding.materialToolbar2);
        getSupportActionBar().setTitle("Transactions");
        Constants.setCategories();

        calendar = Calendar.getInstance();
        FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content,new TransactionsFragment());
        transaction.commit();
        binding.bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction=getSupportFragmentManager().beginTransaction();
                if (item.getItemId()==R.id.transactions){
                 getSupportFragmentManager().popBackStack();
                } else if (item.getItemId()==R.id.stats) {
                    transaction.replace(R.id.content,new StatsFragment());
                    transaction.addToBackStack(null);
                }
                transaction.commit();
                return false;
            }
        });
    }

        public void getTransactions(){
            viewModel.getTransactions(calendar);
        }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            getMenuInflater().inflate(R.menu.top_menu, menu);
            return super.onCreateOptionsMenu(menu);
        }
    }

//RealmResults<Transaction> transactions=realm.where(Transaction.class).findAll();