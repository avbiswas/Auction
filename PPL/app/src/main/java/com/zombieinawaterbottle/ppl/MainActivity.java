package com.zombieinawaterbottle.ppl;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    final String TAG = "Auction";
    final int MAX_PLAYERS_ALLOWED = 17;
    final int maxCash = 150;
    ArrayList<String> users = new ArrayList<>();
    ArrayList<Player> pendingPlayers = new ArrayList<>();
    public static ArrayList<Player> totalPlayers = new ArrayList<>();
    public static Map<String, Integer> countryBG = new HashMap<String, Integer>();
    public static Map<String,String> keywords = new HashMap<>();
    boolean isAuctioner = false;
    boolean isTimerRunning = false;

    Calendar calendar = Calendar.getInstance();

    enum AuctionState {
        IDLE, WAITING, READY, PLAY
    }
    enum TeamFilters{
        FULL, BAT, BALL, WK, ALL
    }
    TeamFilters currentTeamFilter = TeamFilters.FULL;
    boolean viewingAuctionList = false;
    AuctionState auctionState;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DateFormat dateFormatForView = new SimpleDateFormat("HH:mm:ss");
    private static DecimalFormat df = new DecimalFormat("0.0");

    //Reciever myReceiver;
    int myTeamSize=0;
    boolean fetchingTeam = false;
    boolean isCashUpdated = false;
    PlayerListAdapter squadArrayAdapter;
    ArrayList<Player> team;
    Player currentPlayer;
    TextView welcome;
    double cash = 100;
    double maxBid;
    Button buy;
    Button auctionList;
    Button squad_full;
    ImageButton squad_bat, squad_ball, squad_allrounder, squad_wk;

    double currentAuctionPrice;
    String myName;
    LinearLayout layout;
    ProgressBar battingOvr;
    ProgressBar bowlingOvr;
    TextView status;
    TextView my_cash;
    TextView opp_cash[] = new TextView[4];
    TextView playerName;
    TextView playerRole;
    TextView playerType;
    ImageView nationality;
    TextView currentTime;
    TextView battingRatingTV;
    TextView bowlingRatingTV;
    TextView noOfPlayers;
    ListView squad;

    long interval;
    boolean timerOn = false;
    AuctionTimer currentAuction;
    FirebaseFirestore db;
    ListenerRegistration transactionTimer;
    ListenerRegistration listenCash;
    ListenerRegistration endGame;
    public Comparator<Player> batRatingOrder = new Comparator<Player>() {
        @Override
        public int compare(Player player, Player t1) {
            if (player.getBatovr() < t1.getBatovr()) return 1;
            else if (player.getBatovr() > t1.getBatovr()) return -1;
            else return 0;

        }
    };

    public Comparator<Player> bowlRatingOrder = new Comparator<Player>() {
        @Override
        public int compare(Player player, Player t1) {
            if (player.getBallovr() < t1.getBallovr()) return 1;
            else return -1;

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        myName = intent.getStringExtra("userName");
        isAuctioner = intent.getBooleanExtra("isAuctioner", false);
        users = (ArrayList<String>) intent.getSerializableExtra("allUsers");
        auctionState = AuctionState.IDLE;
        db = FirebaseFirestore.getInstance();
        team = new ArrayList<>();
        setContentView(R.layout.activity_auction);
        bindIds();
        populateCountryBG();
        processKeyword();

        squadArrayAdapter = new PlayerListAdapter(this, team);
        squad.setAdapter(squadArrayAdapter);
        interval = 100;

        welcome.setText("Yo " + myName + "!");


        refreshView();

        final Handler someHandler = new Handler(getMainLooper());
        someHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Calendar sysdate = Calendar.getInstance();
                currentTime.setText(dateFormatForView.format(sysdate.getTime()));
                if (isCashUpdated) {
                    if (listenCash != null) listenCash.remove();
                }
                if (auctionState == AuctionState.READY) {
                    if (calendar.getTimeInMillis() <= sysdate.getTimeInMillis()) {
                        Log.d("Auction", "HERE STARTING");

                        timerOn = true;
                        buy.setClickable(true);
                        startAuction();
                    }
                }
                someHandler.postDelayed(this, 1000);
            }
        }, 10);

        db.collection("Metadata").document("refresh").
                addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        makeToast("Refreshing...");
                        refreshView();
                    }
                });


        db.collection("Transactions").document("current").
                addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (!documentSnapshot.exists())
                            return;
                        if (!timerOn) {
                            maxBid = 0;
                            isCashUpdated = false;
                            status.setTextColor(Color.GRAY);
                            status.setText("Auction will begin soon!");

                            currentPlayer = documentSnapshot.toObject(Player.class);
                            if (currentPlayer.getDate() == null) return;
                            try {
                                calendar.setTime(dateFormat.parse(currentPlayer.getDate()));
                            } catch (ParseException e1) {
                                return;
                            }
                            playerName.setText(currentPlayer.getName());
                            playerRole.setText(getKeyword(currentPlayer.getType()));

                            String playerTypeStr;
                            if (currentPlayer.getBowltype() == null){
                                playerTypeStr = getKeyword(currentPlayer.getBattype());
                            }
                            else{
                                if (currentPlayer.getBatovr() >= currentPlayer.getBallovr()){
                                    playerTypeStr = getKeyword(currentPlayer.getBattype()) + "\n" + getKeyword(currentPlayer.getBowltype());
                                }
                                else{
                                    playerTypeStr = getKeyword(currentPlayer.getBowltype()) + "\n" + getKeyword(currentPlayer.getBattype()) ;
                                }
                            }
                            playerType.setText(playerTypeStr);

                            battingOvr.setProgress(currentPlayer.getBatovr());
                            bowlingOvr.setProgress(currentPlayer.getBallovr());
                            battingRatingTV.setText(currentPlayer.getBatovr()+"");
                            bowlingRatingTV.setText(currentPlayer.getBallovr()+"");
                            nationality.setImageResource(countryBG.get(currentPlayer.getCountry()));
                            status.setText("Auction will start at " + dateFormatForView.format(calendar.getTime()));

                            buy.setText(currentPlayer.getPrice() + "");
                            buy.setTextColor(Color.GRAY);
                            buy.setClickable(false);
                            currentAuction = new AuctionTimer(currentPlayer.getPrice() * 1000, interval);

                            removePlayerFromAuctionList(currentPlayer.getName());
                            if (pendingPlayers.size() <= 1) {
                                endGame = db.collection("Metadata").document("Auction")
                                        .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                if (documentSnapshot.get("auctionOver") != null)
                                                if ((Boolean)documentSnapshot.get("auctionOver"))
                                                    status.setText("Auction Over Bro");
                                            }
                                        });
                            }
                            if (documentSnapshot.get(myName) == null) {
                                    auctionState = AuctionState.READY;
                            }
                            if (isAuctioner) {
                                transactionTimer = db.collection("Transactions").document("currentTimer").
                                        addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                            @Override
                                            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                                                boolean auctionOver = true;
                                                for (String user : users) {
                                                    if(documentSnapshot.get(user)!= null)
                                                    if ((Boolean) documentSnapshot.get(user) == false) {
                                                        auctionOver = false;
                                                        break;
                                                    }
                                                }
                                                if (auctionOver == true) {
                                                    db.collection("Transactions").document("current").
                                                            update("auctionOver", true);
                                                }
                                            }
                                        });
                            }

                        }
                        if (documentSnapshot.get("auctionOver") != null) {
                            if ((Boolean) documentSnapshot.get("auctionOver")) {
                                auctionState = AuctionState.IDLE;
                                String winner = "No one";
                                double currentBid = 0;
                                for (String user : users) {
                                    if (documentSnapshot.get(user) != null) {

                                        currentBid = Double.parseDouble(documentSnapshot.get(user).toString());
                                        if (maxBid <= currentBid) {
                                            winner = user;
                                            maxBid = currentBid;
                                        }
                                    }
                                }

                                Log.d("Auction", "WINNER IS " + winner + " " + maxBid);
                                Log.d("Auction", auctionState.toString());
                                status.setText("Sold to " + winner + " for " + maxBid + " pingas!");
                                buy.setText(maxBid+"");
                                if (isTimerRunning) {
                                    Log.d("Auction", "HERE STOPPING");
                                    currentAuction.cancel();
                                    timerOn = false;

                                }
                                buy.setClickable(true);

                                if (maxBid <= 0){
                                    status.setText("Unsold");
                                    if (isAuctioner)
                                        db.collection("Unsold").document(documentSnapshot.get("name")+"").
                                                set(documentSnapshot);
                                }
                                else if (myName.equalsIgnoreCase(winner)) {

                                    if (myTeamSize >= MAX_PLAYERS_ALLOWED){
                                        makeToast("Maximum players allowed is"+MAX_PLAYERS_ALLOWED+"!");
                                    }
                                    else {
                                        HashMap<String, String> sold = new HashMap<>();
                                        sold.put("type", documentSnapshot.get("type").toString());
                                        sold.put("name", documentSnapshot.get("name").toString());
                                        sold.put("sellingPrice", maxBid + "");
                                        //sold.put("type", documentSnapshot.get("type").toString());
                                        //sold.put("type", documentSnapshot.get("type").toString());

                                        db.collection("Users").document(winner).collection("Players").
                                                document(documentSnapshot.get("name").toString())
                                                .set(sold);
                                        cash = Double.parseDouble(df.format(cash - maxBid));
                                        if (!isCashUpdated) {
                                            isCashUpdated = true;
                                            db.collection("Cash").document("current").
                                                    update(winner, cash)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            refreshView();
                                                            refreshTeam(myName);
                                                        }
                                                    });
                                        }
                                    }
                                }
                                else if (!myName.equalsIgnoreCase(winner)){
                                    if (!isCashUpdated) {


                                        listenCash = db.collection("Cash")
                                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                                                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                                                            switch (dc.getType()) {
                                                                case MODIFIED:
                                                                    refreshView();
                                                                    Log.d("Auction", "LISTEN CASH");

                                                                    isCashUpdated = true;
                                                                    Log.d("Auction", isCashUpdated+"");
                                                                    break;

                                                            }
                                                        }
                                                    }
                                                });
                                    }
                                }


                                if (isAuctioner){
                                    transactionTimer.remove();

                                }
                            } else {
                                String winner = "No one";
                                double currentBid = 0;
                                for (String user : users) {
                                    if (documentSnapshot.get(user) != null) {

                                        currentBid = Double.parseDouble(documentSnapshot.get(user).toString());
                                        if (maxBid < currentBid) {
                                            winner = user;
                                            maxBid = currentBid;
                                        }
                                    }
                                }

                            }

                        }

                    }
                    });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAuctionList();
        refreshView();
    }

    private void refreshTeam(final String name) {
        if (fetchingTeam) return;
        fetchingTeam = true;
        makeToast("Fetching Team...");
        db.collection("Users").document(name).collection("Players").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        team = new ArrayList();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            //makeToast();
                            //Player temp = document.toObject(Player.class);
                            Player temp = getPlayerInfo(document.getId());
                            temp.setSellingPrice(document.get("sellingPrice")+"");
                            team.add(temp);
                        }
                        updateTeamUI();
                        fetchingTeam = false;
                        if(name.equalsIgnoreCase(myName))
                            myTeamSize = team.size();
                        //squadArrayAdapter.setArrayList(team);
                    }
                });
    }

    private void makeToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onLongClick(View view) {
        if (!isAuctioner) return false;
        makeToast("Cleaning...");
        for (final String user: users){

            db.collection("Users").document(user).collection("Players").get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            double spentCash = 0;
                            if (task.isSuccessful()){
                                for (DocumentSnapshot doc : task.getResult()){
                                    spentCash = spentCash + Double.parseDouble(doc.get("sellingPrice").toString());
                                }
                            }
                            db.collection("Cash").document("current").
                                    update(user, Double.parseDouble(df.format(maxCash - spentCash)));
                            if (user.equalsIgnoreCase(users.get(users.size() - 1))) {
                                db.collection("Metadata").document("refresh").update("refresh",FieldValue.increment(1))
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            makeToast("Cleaned");
                                        }
                                    });

                            }




                        }
                    });


        }
        return true;
    }

    private void updateTeamUI() {
        ArrayList<Player> teamList = new ArrayList<>();
        switch (currentTeamFilter){
            case ALL: teamList = filterBy("ALL");
                if (!viewingAuctionList)
                    Collections.sort(teamList, batRatingOrder);
                break;

            case WK:  teamList = filterBy("WK");
                if (!viewingAuctionList)
                    Collections.sort(teamList, batRatingOrder);
                break;
            case BAT: teamList = filterBy("BAT");
                if (!viewingAuctionList)
                    Collections.sort(teamList, batRatingOrder);
                break;
            case BALL: teamList = filterBy("BOWL");
                if (!viewingAuctionList)
                    Collections.sort(teamList, bowlRatingOrder);
                break;
            case FULL: teamList = team;

        }
        squadArrayAdapter = new PlayerListAdapter(this, teamList);
        squad.setAdapter(squadArrayAdapter);
        noOfPlayers.setText(team.size()+"/"+MAX_PLAYERS_ALLOWED);
    }

    private ArrayList<Player>  filterBy(String type) {
        ArrayList<Player> teamList = new ArrayList<>();
        for (Player player : team){
            if (player.getType().equalsIgnoreCase(type)){
                teamList.add(player);
            }
        }


        return teamList;
    }

    private void refreshView() {
        db.collection("Cash").document("current").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot doc = task.getResult();
                        int c = 0;
                        for (final String username : users) {
                            if (username.equalsIgnoreCase(myName)) {
                                my_cash.setText(doc.get(myName).toString());
                                cash = Double.parseDouble(doc.get(myName).toString());
                                my_cash.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        viewingAuctionList = false;
                                        refreshTeam(myName);
                                        updateTeamUI();
                                    }
                                });
                                continue;
                            }

                            opp_cash[c].setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    viewingAuctionList = false;
                                    refreshTeam(username);
                                    updateTeamUI();

                                }
                            });
                            opp_cash[c++].setText(username + ":" + doc.get(username));

                        }
                    }
                });

    }


    public class AuctionTimer extends CountDownTimer {

        public AuctionTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            status.setText("No Bid Submitted");
            status.setTextColor(Color.RED);
            timerOn = false;
            auctionState = AuctionState.IDLE;
            isTimerRunning = false;
            buy.setText("0.0");
            endAndInform();
        }

        private void endAndInform() {
            isTimerRunning = false;
            db.collection("Transactions").document("currentTimer")
                    .update(myName, true);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            //buy.setTextColor(Color.RED);
            isTimerRunning = true;

            buy.setText("" + df.format((double)millisUntilFinished / 1000));
            currentAuctionPrice = Double.parseDouble(df.format((double)millisUntilFinished / 1000));
            if (currentAuctionPrice > cash){
                buy.setClickable(false);
                buy.setTextColor(Color.GRAY);
                status.setText("Olpo boyeshei bhikhaari?");
            }
            else if(myTeamSize >= MAX_PLAYERS_ALLOWED){
                buy.setClickable(false);
                buy.setTextColor(Color.GRAY);
                status.setText("Thank you... Your part is done.");
            }
            else{
                buy.setClickable(true);
                buy.setTextColor(Color.BLUE);
                status.setText("Auction on!");
            }
            //Log.d("Auction", currentAuctionPrice + " " + maxBid);
            if (currentAuctionPrice < maxBid) {
                buy.setClickable(false);
                currentAuction.cancel();
                auctionState = AuctionState.IDLE;
                makeToast("Bid lost!");

                timerOn = false;
                endAndInform();
            }
        }
    }

    @Override
    public void onClick(View view) {
        makeToast(myTeamSize+"");
        if(view == auctionList){
            team = pendingPlayers;
            viewingAuctionList = true;
            updateTeamUI();
        }
        if (view == squad_full){
            currentTeamFilter = TeamFilters.FULL;
            updateTeamUI();
        }
        else if (view == squad_bat){

            currentTeamFilter = TeamFilters.BAT;
            updateTeamUI();
        }
        else if (view == squad_ball){
            currentTeamFilter = TeamFilters.BALL;
            updateTeamUI();
        }
        else if (view == squad_allrounder){
            currentTeamFilter = TeamFilters.ALL;
            updateTeamUI();
        }
        else if (view == squad_wk){
            currentTeamFilter = TeamFilters.WK;
            updateTeamUI();
        }
        else if (view == buy) {
            if (!timerOn && !isAuctioner) {
                return;

            }
            if (!timerOn && isAuctioner) {

                status.setTextColor(Color.GRAY);
                status.setText("Wait kor... Player asche..");
                buy.setEnabled(false);


                db.collection("players").whereGreaterThan("auctionID", 0).orderBy("auctionID").limit(1)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.getResult().size() == 0){
                                    makeToast("OVER");
                                    db.collection("Metadata").document("Auction").update("auctionOver", true);
                                }
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Player tempPlayer = document.toObject(Player.class);

                                    Date date = startAlert().getTime();
                                    tempPlayer.setDate(dateFormat.format(date));
                                    //squad.setText(dateFormat.format(date)+" "+date.toString());
                                    Map<String, Object> currentTimer = new HashMap<>();
                                    for (String user : users) {
                                        currentTimer.put(user, false);
                                    }
                                    db.collection("Transactions").document("current").set(tempPlayer);
                                    db.collection("Transactions").document("current").update("auctionOver", false);
                                    db.collection("Transactions").document("currentTimer")
                                            .set(currentTimer);
                                    buy.setEnabled(true);
                                    db.collection("players").document(document.getId()).update("auctionID", -1);
                                    auctionState = AuctionState.WAITING;
                                }
                            }
                        });


            } else {


                currentAuction.cancel();
                status.setText("Bid Submitted");
                auctionState = AuctionState.IDLE;
                if (cash < currentAuctionPrice) {
                    status.setText("Olpo Boyeshe Bhikhari Hoe Geli Bhai");
                    return;
                }

                db.collection("Transactions").document("current")
                        .update(myName, currentAuctionPrice)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                makeToast("Bid Submitted");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                status.setText("Bid Failed Man!");
                            }
                        });

                db.collection("Transactions").document("currentTimer")
                        .update(myName, true);

                //my_cash.setText(cash+"");
                status.setTextColor(Color.BLUE);

                currentPlayer = null;
                //layout.removeView(button);
            }
        }
    }

    @Override
    protected void onPause() {
        //unregisterReceiver(myReceiver);

        super.onPause();

    }

    public Calendar startAlert() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int seconds = calendar.get(Calendar.SECOND);

        if (seconds >= 0 && seconds <= 20) {
            seconds = 30;
        } else if (seconds > 20 && seconds < 40) {
            seconds = 50;

        } else {
            seconds = 10;
            minute++;
        }

        if (minute >= 60) {
            hour++;
            minute = 0;
        }

        if (hour >= 24) {
            day++;
            hour = 0;
        }
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, seconds);
        return calendar;
    }

    private void getAuctionList(){
        fetchingTeam = true;
        Log.d("Auction", "HERE OUT");
        db.collection("players")
                //.whereGreaterThan("auctionID", 0)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        pendingPlayers = new ArrayList<>();
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                Player temp = doc.toObject(Player.class);
                                if (doc.get("auctionID")!= null)
                                    if (Integer.parseInt(doc.get("auctionID").toString()) > 0)
                                        pendingPlayers.add(temp);
                                totalPlayers.add(temp);
                            }
                        }
                        fetchingTeam = false;
                        Collections.sort(pendingPlayers);
                        Collections.sort(totalPlayers);
                        refreshTeam(myName);
                        makeToast("READY");
                    }
                });

    }

    private void removePlayerFromAuctionList(String name){
        for (Player pl:pendingPlayers){
            if (pl.getName().equalsIgnoreCase(name)){
                pendingPlayers.remove(pl);
                break;
            }
        }
    }
    public void startAuction() {
        //Toast.makeText(this, Calendar.getInstance().getTime()+"", Toast.LENGTH_SHORT).show();

        if (isAuctioner) {
            transactionTimer = db.collection("Transactions").document("currentTimer").
                    addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                            if (!timerOn) return;
                            boolean auctionOver = true;
                            for (String user : users) {
                                if ((Boolean) documentSnapshot.get(user) == false) {
                                    auctionOver = false;
                                    break;
                                }
                            }
                            if (auctionOver == true) {
                                db.collection("Transactions").document("current").
                                        update("auctionOver", true);
                            }
                        }
                    });
        }
        buy.setClickable(true);
        buy.setTextColor(Color.BLUE);
        auctionState = AuctionState.PLAY;
        currentAuction.start();
    }

    private void bindIds() {
        layout = (LinearLayout) findViewById(R.id.layout);
        welcome = (TextView) findViewById(R.id.welcome);
        my_cash = (TextView) findViewById(R.id.my_budget);
        opp_cash[0] = findViewById(R.id.opp1);
        opp_cash[1] = findViewById(R.id.opp2);
        opp_cash[2] = findViewById(R.id.opp3);
        opp_cash[3] = findViewById(R.id.opp4);
        playerName = (TextView) findViewById(R.id.Name);
        playerRole = (TextView) findViewById(R.id.Role);
        nationality = (ImageView) findViewById(R.id.Nationality);
        currentTime = (TextView) findViewById(R.id.currentTime);
        battingRatingTV = (TextView) findViewById(R.id.battingRatingTV);
        bowlingRatingTV = (TextView) findViewById(R.id.bowlingRatingTV);
        playerType = (TextView) findViewById(R.id.type);
        noOfPlayers=(TextView) findViewById(R.id.totalPlayers);
        squad = (ListView) findViewById(R.id.squad);
        auctionList = (Button) findViewById(R.id.auctionlist);

        squad_full = (Button) findViewById(R.id.squad_full);

        squad_bat = (ImageButton) findViewById(R.id.squad_bat);
        squad_ball= (ImageButton) findViewById(R.id.squad_ball);
        squad_allrounder = (ImageButton) findViewById(R.id.squad_allrounder);
        squad_wk = (ImageButton) findViewById(R.id.squad_wk);


        squad_full.setOnClickListener(this);
        squad_bat.setOnClickListener(this);
        squad_allrounder.setOnClickListener(this);
        squad_ball.setOnClickListener(this);
        squad_wk.setOnClickListener(this);


        battingOvr = (ProgressBar) findViewById(R.id.battingRating);
        bowlingOvr = (ProgressBar) findViewById(R.id.bowlingRating);
        battingOvr.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
        bowlingOvr.getProgressDrawable().setColorFilter(Color.YELLOW, PorterDuff.Mode.SRC_IN);
        battingOvr.setProgress(70);
        bowlingOvr.setProgress(80);

        buy = (Button) findViewById(R.id.buy);
        buy.setOnClickListener(this);

        if (isAuctioner) {
            welcome.setLongClickable(true);
            welcome.setOnLongClickListener(this);
        }

        auctionList.setOnClickListener(this);
        status = (TextView) findViewById(R.id.status);
    }

    private void populateCountryBG() {
        countryBG.put("IND", R.drawable.ind);
        countryBG.put("NZ", R.drawable.nz);
        countryBG.put("BAN", R.drawable.ban);
        countryBG.put("AUS", R.drawable.aus);
        countryBG.put("PAK", R.drawable.pak);
        countryBG.put("ENG", R.drawable.eng);
        countryBG.put("WI", R.drawable.wi);
        countryBG.put("SL", R.drawable.sl);
        countryBG.put("RSA", R.drawable.rsa);
        countryBG.put("AFG", R.drawable.afg);
    }
    private void processKeyword(){
        keywords.put("BAT", "Batsman");
        keywords.put("BOWL", "Bowler");
        keywords.put("ALL", "All Rounder");
        keywords.put("WK", "Wicket Keeper");
        keywords.put("RHB", "Right Hand Bat");
        keywords.put("LHB", "Left Hand Bat");
        keywords.put("LOS", "Left Arm Off Spin");
        keywords.put("ROS", "Right Arm Off Spin");
        keywords.put("LLS", "Left Arm Leg Spin");
        keywords.put("RLS", "Right Arm Leg Spin");
        keywords.put("RF", "Right Arm Fast");
        keywords.put("LF", "Left Arm Fast");
        keywords.put("RFM", "Right Arm Fast Med");
        keywords.put("LFM", "Left Arm Fast Med");
        keywords.put("RM", "Right Arm Medium");
        keywords.put("LM", "Left Arm Medium");

    }
    public static String getKeyword(String str){
        return keywords.containsKey(str)?keywords.get(str):str;
    }
    public static Player getPlayerInfo(String name){
        for (Player player : totalPlayers){
            Log.d("Auction", player.getName() + ", " + name);
            if (player.getName().equalsIgnoreCase(name))
                return player;
        }
        return  null;
    }
}
