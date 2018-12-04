package com.ridbparis8.meteo;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ClimatAdapteur extends RecyclerView.Adapter<ClimatAdapteur.ViewHolder> {

    Climat climat;

    public ClimatAdapteur(Climat climat){
        this.climat = climat;
    }

    @NonNull
    @Override
    // Ou l'on dit a l'adaptateur a quoi ressemble notre layout
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Declare que item_climat est le template
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_climat, parent, false);

        return new ViewHolder(itemView);
    }

    // Quel information faut il mettre
    // Une fois que les informations sont crées dans la class VIEWHOLDER, elles sont maintenant accessible ici
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        // Si on est la position 0 il prendra climat info 0, si 1 alors climatinfo 1 etc
        ClimatInfo climatInfo = climat.climatInfoArray.get(position);
        Temps temps = climat.getTempsArray().get(position);

        String temperatureString = (int) climatInfo.getTemperature() + "°C";
        viewHolder.jour.setText(temps.getNomDeJours());
        viewHolder.temperature.setText(temperatureString);

        // Récupération des images pour l'affichage correspondant
        Context context = viewHolder.image.getContext();
        String iconUri = Utilites.getIconUri(climatInfo.getClimat_id());
        int iconId = context.getResources().getIdentifier(iconUri,null,context.getPackageName());
        Drawable iconDrawable = context.getResources().getDrawable(iconId);
        viewHolder.image.setImageDrawable(iconDrawable);
    }

    // Définit combien de fois on doit executer la positon
    // SI 5 elements alors 5 view a construire
    @Override
    public int getItemCount() {
        return climat.climatInfoArray.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView jour;
        TextView temperature;
        ImageView image;

        public ViewHolder(View itemView) {

            super(itemView);

            jour = (TextView)itemView.findViewById(R.id.jour_tv);
            temperature = (TextView)itemView.findViewById(R.id.temperature_tv);
            image  = (ImageView)itemView.findViewById(R.id.icon_iv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        Context context = v.getContext();
                        int position = getLayoutPosition();
                        Intent intent = new Intent(v.getContext(), DetailActivity.class);
                        intent.putExtra(DetailActivity.LOCATION_CLEF, climat.location); // Nécessite une clé String : 'location'
                        intent.putExtra(DetailActivity.TEMPS_CLEF, climat.tempsArray.get(position)); // Uniquement celui sur lequel on clic
                        intent.putExtra(DetailActivity.CLIMATINFO_CLEF, climat.climatInfoArray.get(position));
                        context.startActivity(intent);
                }
            });
        }
    }


}
