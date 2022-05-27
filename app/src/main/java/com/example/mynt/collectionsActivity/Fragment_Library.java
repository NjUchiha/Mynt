package com.example.mynt.collectionsActivity;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;


import com.example.mynt.Interface_RecyclerView;
import com.example.mynt.collectionsActivity.adapters.Adapter_Coin;
import com.example.mynt.collectionsActivity.models.Model_Coin;

import com.example.mynt.R;
import com.example.mynt.collectionsActivity.models.Model_Collections;
import com.example.mynt.dataAccessLayer.Database_Lite;

import com.example.mynt.collectionsActivity.adapters.Adapter_Library_Options;
import com.example.mynt.collectionsActivity.models.Model_Library_Options;
import com.example.mynt.collectionsActivity.models.Model_User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_Library#} factory method to
 * create an instance of this fragment.
 */
public class Fragment_Library extends Fragment implements Interface_RecyclerView {
    //Variable Declarations
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ListView optionListView;
    private ImageButton loginButton;
    private Adapter_Library_Options optionsListAdapter;
    private View libraryView;
    private ArrayList<Model_Coin> arrayList_recent_coins;
    private Database_Lite db;
    private ArrayList<Model_User> users;
    private ArrayList<Integer> userCollectionIDs;
    private ArrayList<Model_Collections> allCollections;
    private ArrayList<Model_Collections> allUserCollections;
    private ArrayList<Integer> allCoinsWithCollection;
    private ArrayList<Model_Coin> AllCoinsInDatabase;
    private boolean found;
    private String fileName;
    private ArrayList<Model_Coin> dbCoins;
    private ArrayList<Model_Coin> currentUserCoins;
    private ArrayList<Integer> collectionCoins;
    private ArrayList<Model_Library_Options> arrayList_library_navigation;


    private Model_User user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        libraryView = inflater.inflate(R.layout.fragment_library, container, false);

        //Initializing variables
        //String email = getArguments().getString("userEmail");

        optionListView = libraryView.findViewById(R.id.listView_navigation_library);
        loginButton = libraryView.findViewById(R.id.imageButton_userActivity_library);
        arrayList_library_navigation = new ArrayList<>();
        arrayList_recent_coins = new ArrayList<>();

        ReturnToRegister();
        ViewLoggedInUser();
        NavigationToOtherPages();
        DisplayAllLocalCollections();
        DisplayAllLocalCoins();

        //Populating Library Options List
        arrayList_library_navigation.add(new Model_Library_Options( R.drawable.img_app_logo,
                getResources().getString(R.string.library_option_coins),
                0,
                currentUserCoins.size()));

        arrayList_library_navigation.add(new Model_Library_Options( R.drawable.ic_collection_icon,
                getResources().getString(R.string.library_option_collections),
                0,
                0));

        arrayList_library_navigation.add(new Model_Library_Options( R.drawable.ic_goal_icon,
                getResources().getString(R.string.library_option_goals),
                62,
                62));

        int i=currentUserCoins.size();

        if(i>0)
            do {
                i--;
                arrayList_recent_coins.add(currentUserCoins.get(i));
                if(arrayList_recent_coins.size()>3 || i==0)
                {

                    break;
                }
            }while (i>0);


        //Passing data to list recycler view
        recyclerView = (RecyclerView) libraryView.findViewById(R.id.recyclerView_recentCoins_library);
        //recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);

        //Ensuring the recycler view layout contains 2 item in each row
        layoutManager = new StaggeredGridLayoutManager(2,1);
        recyclerView.setLayoutManager(layoutManager);

        //Setting up adapters
        //ListView
        optionsListAdapter = new Adapter_Library_Options(getContext(),arrayList_library_navigation,user);
        optionListView.setAdapter(optionsListAdapter);
        //recyclerView
        mAdapter = new Adapter_Coin(arrayList_recent_coins, getContext(),this);
        recyclerView.setAdapter(mAdapter);





        return libraryView;
    }

    private void ViewLoggedInUser(){

        db = new Database_Lite(getContext());

        users = db.getAllUsers();
        for (int i=0; i<users.size(); i++)
        {
            if(users.get(i).getState()==1)
            {
                user = users.get(i);
            }
        }
    }

    private void DisplayAllLocalCollections(){

        userCollectionIDs = db.getAllCollectionsForUser(user);
        allCollections = db.getAllCollections();

        allUserCollections = new ArrayList<>();

        for (int i=0; i<allCollections.size(); i++)
        {
            if(userCollectionIDs.contains(allCollections.get(i).getCollectionID()))
                allUserCollections.add(allCollections.get(i));
        }

        allCoinsWithCollection = db.getAllCoinsWithACollection();

        AllCoinsInDatabase = db.getAllCoins();
        if(allCoinsWithCollection.size() != AllCoinsInDatabase.size() )
        {
            for (int i=0; i<AllCoinsInDatabase.size(); i++)
            {
                found = false;
                for (int b=0; b<allCoinsWithCollection.size(); b++)
                {
                    if(AllCoinsInDatabase.get(i).getCoinID() == allCoinsWithCollection.get(b))
                    {
                        found = true;
                        break;
                    }
                }
                if(!found)
                {
                    //delete current coin from database
                    db.deleteCoin(AllCoinsInDatabase.get(i).getCoinID());;
                    fileName = AllCoinsInDatabase.get(i).getCoinID() + ".jpg";
                    //Delete image from files
                    requireContext().deleteFile(fileName);
                    //imageID.add(AllCoinsInDatabase.get(i).getImageId());
                }
            }
        }

        userCollectionIDs = db.getAllCollectionsForUser(user);
        allCollections = db.getAllCollections();

        allUserCollections = new ArrayList<>();

        for (int i=0; i<allCollections.size(); i++)
        {
            if(userCollectionIDs.contains(allCollections.get(i).getCollectionID()))
                allUserCollections.add(allCollections.get(i));
        }
    }

    private void DisplayAllLocalCoins(){

        allCoinsWithCollection = db.getAllCoinsWithACollection();

        AllCoinsInDatabase = db.getAllCoins();
        if(allCoinsWithCollection.size() != AllCoinsInDatabase.size() )
        {
            for (int i=0; i<AllCoinsInDatabase.size(); i++)
            {
                found = false;
                for (int b=0; b<allCoinsWithCollection.size(); b++)
                {
                    if(AllCoinsInDatabase.get(i).getCoinID() == allCoinsWithCollection.get(b))
                    {
                        found = true;
                        break;
                    }
                }
                if(!found)
                {
                    //delete current coin from database
                    db.deleteCoin(AllCoinsInDatabase.get(i).getCoinID());;
                    fileName = AllCoinsInDatabase.get(i).getCoinID() + ".jpg";
                    //Delete image from files
                    requireContext().deleteFile(fileName);
                    //imageID.add(AllCoinsInDatabase.get(i).getImageId());
                }
            }
        }

        dbCoins = new ArrayList<>();
        dbCoins = db.getAllCoins();

        currentUserCoins = new ArrayList<>();

        for (int b=0; b<allUserCollections.size(); b++) {
            collectionCoins = db.getAllCoinsInCollection(allUserCollections.get(b).getCollectionID());
            for (int i = 0; i < collectionCoins.size(); i++) {
                for (int s = 0; s < dbCoins.size(); s++) {
                    if (collectionCoins.get(i) == dbCoins.get(s).getCoinID()) {
                        currentUserCoins.add(dbCoins.get(s));
                        //add final list here
                        break;
                    }
                }
            }
        }


    }

    private void NavigationToOtherPages(){


        //Onclick Listeners
        optionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView parent, View v, int position, long id){
                Bundle bundle = new Bundle();
                bundle.putInt("User",user.getUserID());
                //Intent collections = new Intent(getContext(), Activity_Collections.class);
                if(position==0)
                {
                    bundle.putInt("Task",2);
                    Navigation.findNavController(libraryView).navigate(R.id.action_fragment_home_main_to_fragment_Coins,bundle);
                    //collections.putExtra("action","coins");
                }else if (position==1)
                {
                    bundle.putInt("Task", 0);
                    Navigation.findNavController(libraryView).navigate(R.id.action_fragment_home_main_to_fragment_Collections, bundle);
                    //collections.putExtra("action","collections");
                    //Navigation.findNavController(libraryView).navigate(R.id.action_fragment_Library_to_fragment_Collections);
                }else if (position==2)
                {
                    //POE
                    //Goals Activity
                }

                //startActivity(collections);
            }
        });


    }

    private void ReturnToRegister(){


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bundle.Add.Extra(Name of coin, year, country)
                Navigation.findNavController(libraryView).navigate(R.id.action_fragment_home_main_to_fragment_Register);
            }
        });

    }
    //Implementing RecyclerViewInterface Method
    @Override
    public void onItemClick(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("Task", 0);
        bundle.putInt("CoinID", arrayList_recent_coins.get(position).getCoinID());;
        Navigation.findNavController(libraryView).navigate(R.id.action_fragment_home_main_to_fragment_Coin_Details,bundle);;
    }



}