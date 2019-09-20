package com.zombieinawaterbottle.ppl;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class PlayerListAdapter extends ArrayAdapter<Player> {
    private Context mContext;
    private List<Player> squad;

    public PlayerListAdapter(Context context, ArrayList<Player> squad) {
        super(context, 0, squad);
        this.squad = squad;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.squad_listview,parent,false);

        final Player currentPlayer = squad.get(position);

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = LayoutInflater.from(mContext);
                View alertLayout = inflater.inflate(R.layout.custom_dialog, null);
                final TextView textBatHand = alertLayout.findViewById(R.id.dialog_bathand);
                final TextView textBowlType = alertLayout.findViewById(R.id.dialog_bowltype);
                final TextView textBatOvr = alertLayout.findViewById(R.id.dialog_batskill);
                final TextView textBallOvr = alertLayout.findViewById(R.id.dialog_bowlskill);
                final TextView textRole = alertLayout.findViewById(R.id.dialog_role);
                final ImageView imgCountry = alertLayout.findViewById(R.id.dialog_nationality);
                Player playerInfo = MainActivity.getPlayerInfo(currentPlayer.getName());
                if (playerInfo == null) {
                    Toast.makeText(getContext(), currentPlayer.getName() , Toast.LENGTH_SHORT).show();
                    return;
                }
                textBatHand.setText(MainActivity.getKeyword(playerInfo.getBattype()));
                textBowlType.setText(MainActivity.getKeyword(playerInfo.getBowltype()));
                textBatOvr.setText(playerInfo.getBatovr()+"");
                textBallOvr.setText(playerInfo.getBallovr()+"");
                textRole.setText(playerInfo.getType());
                imgCountry.setImageResource(MainActivity.countryBG.get(playerInfo.getCountry()));


                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle(currentPlayer.getName());
                alert.setView(alertLayout);
                alert.setCancelable(true);
                AlertDialog alertDialog = alert.create();
                alertDialog.show();


            }
        });
        TextView name = (TextView) listItem.findViewById(R.id.squad_player_name);
        name.setText(currentPlayer.getName());

        ImageView role = (ImageView) listItem.findViewById(R.id.squad_player_role);
        role.setImageResource(getImage(currentPlayer.getType()));

        TextView price = (TextView) listItem.findViewById(R.id.squad_player_price);
        if (currentPlayer.getSellingPrice() == null){
            price.setText(currentPlayer.getPrice()+"");
        }
        else{
            price.setText(currentPlayer.getSellingPrice());
        }


        return listItem;
    }

    private int getImage(String type) {
        if (type.equalsIgnoreCase("BAT")) {
            return R.drawable.bat01;
        }
        else if (type.equalsIgnoreCase("BOWL")){
            return  R.drawable.ball2;
        }
        else if (type.equalsIgnoreCase("WK")){
            return R.drawable.wicketkeeper;
        }
        else if (type.equalsIgnoreCase("ALL")){
            return R.drawable.allrounder;
        }
        return R.drawable.allrounder;
    }

    public void setArrayList(ArrayList<Player> squad) {
        this.squad = squad;
        notifyDataSetChanged();
    }

}
