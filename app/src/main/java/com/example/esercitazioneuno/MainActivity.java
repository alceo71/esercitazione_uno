package com.example.esercitazioneuno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.constraintlayout.widget.Placeholder;

import android.os.Bundle;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.esercitazioneuno.common.model.Pianeta;
import com.example.esercitazioneuno.common.util.LogUtil;
import com.example.esercitazioneuno.service.PianetaService;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static GestioneBack gestioneBack;

    // Elenco di tutti i pianeti
    List<Pianeta> pianeti;

    // Pianeta attualmente selezionato
    Pianeta pianetaSelezionato;

    TextView titolo;
    TextView distanzaValore;
    TextView massaValore;
    TextView rotazioneValore;
    TextView rivoluzioneValore;
    TextView diametroValore;
    TextView satellitiValore;

    ImageView immagineGrande;

    // Usato nel landscape per il cambio di grandezza
    Placeholder placeholder;
    ConstraintLayout constraintLayout;
    int widthUltimoPianeta = 0;
    int heightUltimoPianeta = 0;

    int widthPlaceholder;
    int heightPlaceholder = 0;

    // Informazioni introduzione e pianeta
    Group introduzione;
    Group informazioniPianeta;

    // La chiave per salvare e recuperare il pianeta visualizzato
    public final String BUNDLE_PIANETA = "pianeta";

    // Indica se si sta visualizzando l'intro o un pianeta
    private boolean visualizzazioneIntro;

    // Indica se la visualizzazione è in landscape
    private boolean inLandscape;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inizializza la lista dei pianeti
        pianeti = PianetaService.findAll();


        visualizzazioneIntro = true;

        // trova gli elementi con contenuto dinamico
        titolo = findViewById(R.id.titolo);
        distanzaValore = findViewById(R.id.distanza_valore);
        massaValore = findViewById(R.id.massa_valore);
        rotazioneValore = findViewById(R.id.rotazione_valore);
        rivoluzioneValore = findViewById(R.id.rivoluzione_valore);
        diametroValore = findViewById(R.id.diametro_valore);
        satellitiValore = findViewById(R.id.satelliti_valore);

        immagineGrande = findViewById(R.id.immagine_grande);

        if(pianetaSelezionato != null){
            assegnaContenuto(pianetaSelezionato);
        }

        // Esiste solo nel landscape
        placeholder = findViewById(R.id.immagine_placeholder);
        constraintLayout = findViewById(R.id.constraintLayout);
        if(placeholder != null){
            widthPlaceholder = placeholder.getLayoutParams().width;
            heightPlaceholder = placeholder.getLayoutParams().width;
            inLandscape = true;
        }

        //TextView intro = findViewById(R.id.introduzione);
        //intro.setText(Html.fromHtml(getString(R.string.introduzione)));

        introduzione = findViewById(R.id.introduzione);
        informazioniPianeta  = findViewById(R.id.informazioni_pianeta);

        // Controlla se un pianeta esiste già
        if(savedInstanceState != null && savedInstanceState.getString(BUNDLE_PIANETA) != null){
            String nomePianeta = savedInstanceState.getString(BUNDLE_PIANETA);
            LogUtil.warn("Trovato il pianeta " + nomePianeta);

            int idPianeta = idPianeta(nomePianeta);
            ImageView support = findViewById(idPianeta);
            LogUtil.debug("Risorsa associata a  " + idPianeta + " è " + support);
            caricaValoriPianeta(support);
        }

        // Se non esiste già creo la mia gestione Back
        if(gestioneBack == null)
            gestioneBack = new GestioneBack();

    }

    public void assegnaContenuto(Pianeta pianeta){
        // Nome del pianeta
        titolo.setText( getString(risorsaNomePianeta(pianeta.getNome())));

        // Distanza
        distanzaValore.setText(getString(R.string.valore_distanza,pianeta.getDistanzaUA()));

        // Massa
        massaValore.setText(getString(R.string.valore_massa,pianeta.getMassa()));

        // Rotazione
        if(pianeta.getRotazioneOre() > 48){
            rotazioneValore.setText(getString(R.string.valore_rotazione_giorni, pianeta.getRotazioneGiorni()));
        } else {
            rotazioneValore.setText(getString(R.string.valore_rotazione_ore, pianeta.getRotazioneOre()));
        }

        // Rivoluzione
        if(pianeta.getRivoluzioneGiorni() > 700){
            rotazioneValore.setText(getString(R.string.valore_rivoluzione_anni, pianeta.getRivoluzioneAnni()));
        } else{
            rotazioneValore.setText(getString(R.string.valore_rivoluzione_giorni, pianeta.getRivoluzioneGiorni()));
        }

        // Diametro
        diametroValore.setText(getString(R.string.valore_diametro,pianeta.getDiametro()));

        // Satelliti
        if(pianeta.getNumeroSatelliti() == 0){
            satellitiValore.setText(R.string.valore_satellite_zero);
        } else {
            String etichetta = getResources().getQuantityString(R.plurals.valore_satellite, pianeta.getNumeroSatelliti(),pianeta.getNumeroSatelliti());
            satellitiValore.setText(etichetta);
        }

        immagineGrande.setImageResource(risorsaImmagine(pianeta.getNome()));
    }


    /**
     * Salva il nome del pianeta. Necessario per recuperare eventualmente il pianeta selezionato quando si passa da land a portrait
     *
     * @param outState
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if(pianetaSelezionato != null){
            LogUtil.warn("Salva il pianeta " + pianetaSelezionato.getNome());
            outState.putString(BUNDLE_PIANETA,pianetaSelezionato.getNome());
        }

    }

    /**
     * Carica i valori del pianeta
     *
     * @param view
     */
    public void caricaValoriPianeta(View view){
        // Controlla se è presente l'introduzione
        if(visualizzazioneIntro){
            mostraLayoutPianeta();
        }

        // Recupera l'oggetto pianeta da selezionare a partire dal suo id
        pianetaSelezionato = getPianeta(view.getId());

        assegnaContenuto(pianetaSelezionato);

        // Controlla se sono in landscape
        if(inLandscape){
            ViewGroup.LayoutParams params = view.getLayoutParams();

            // Memorizzo il valore originale dei larghezza e altezza
            int widthIniziale = params.width;
            int heightIniziale = params.height;

            // Cambio altezza e grandezza
            params.width = widthPlaceholder;
            params.height = heightPlaceholder;
            view.setLayoutParams(params);
            view.requestLayout();

            if(placeholder.getContent() != null){
                ViewGroup.LayoutParams params2 = placeholder.getContent().getLayoutParams();
                params2.width = widthUltimoPianeta;
                params2.height = heightUltimoPianeta;
                placeholder.getContent().setLayoutParams(params2);
                placeholder.getContent().requestLayout();
            }

            widthUltimoPianeta = widthIniziale;
            heightUltimoPianeta = heightIniziale;

            // Cerca il place holder
            TransitionManager.beginDelayedTransition(constraintLayout);
            placeholder.setContentId(view.getId());
        }
    }


    public void mostraLayoutPianeta(){
        introduzione.setVisibility(View.GONE);
        informazioniPianeta.setVisibility(View.VISIBLE);
        visualizzazioneIntro = false;
    }


    /**
     * Mostra la visualizzazione dell'intro
     *
     */
    public void mostraLayoutIntro(){
        introduzione.setVisibility(View.VISIBLE);
        informazioniPianeta.setVisibility(View.GONE);
        immagineGrande.setImageResource(R.drawable.pianeti);
        titolo.setText(R.string.titolo);

        // Distingui tra landscape e portratit
        if(inLandscape){
            // Devo cambiare il placeholder e tenere conto dell'ultimo valore di grandezza

            if(placeholder.getContent() != null){
                ViewGroup.LayoutParams params2 = placeholder.getContent().getLayoutParams();
                params2.width = widthUltimoPianeta;
                params2.height = heightUltimoPianeta;
                placeholder.getContent().setLayoutParams(params2);
                placeholder.getContent().requestLayout();
            }

            TransitionManager.beginDelayedTransition(constraintLayout);
            placeholder.setContentId(R.id.immagine_placeholder);
        }

        visualizzazioneIntro = true;
    }

    /**
     * Evento click su uno dei pianeti
     *
     * @param view
     */
    public void clickSuPianeta(View view){
        LogUtil.warn("Click sul pianeta " + getResources().getResourceEntryName(view.getId()) );

        // Aggiungi il pianeta alla navigazione
        gestioneBack.aggiungiPianeta(nomePianeta(view.getId()));
        caricaValoriPianeta(view);
    }


    /**
     * Ritorna l'oggetto Pianeta a partire dal suo id
     *
     * @param idPianeta
     * @return
     */
    public Pianeta getPianeta(int idPianeta){
        String nomePianeta = nomePianeta(idPianeta);
        return getPianeta(nomePianeta);
    }

    /**
     * Ritorna l'oggetto Pianeta a partire dal suo nome
     *
     * @param nomePianeta
     * @return
     */
    public Pianeta getPianeta(String nomePianeta){
        Pianeta result = null;

        for(Pianeta pianeta:pianeti){
            if(pianeta.getNome().equals(nomePianeta)){
                result = pianeta;
                // Interrompi il ciclo
                break;
            }
        }
        return result;
    }

    /**
     * Ritorna il nome del pianeta associato all'ID definito nel layout
     *
     * @param idPianeta
     * @return
     */
    public String nomePianeta(int idPianeta){
        String nomePianeta = null;
        if(idPianeta == R.id.ic_mercurio)
            nomePianeta = "mercurio";
        else if(idPianeta == R.id.ic_venere)
            nomePianeta = "venere";
        else if(idPianeta == R.id.ic_terra)
            nomePianeta = "terra";
        else if(idPianeta == R.id.ic_marte)
            nomePianeta = "marte";
        else if(idPianeta == R.id.ic_giove)
            nomePianeta = "giove";
        else if(idPianeta == R.id.ic_saturno)
            nomePianeta = "saturno";
        else if(idPianeta == R.id.ic_urano)
            nomePianeta = "urano";
        else if(idPianeta == R.id.ic_nettuno)
            nomePianeta = "nettuno";
        return nomePianeta;
    }

    public int idPianeta(String nomePianeta){
        int idPianeta = -1;
        if(nomePianeta.equals("mercurio"))
            idPianeta = R.id.ic_mercurio;
        else if(nomePianeta.equals("venere"))
            idPianeta = R.id.ic_venere;
        else if(nomePianeta.equals("terra"))
            idPianeta = R.id.ic_terra;
        else if(nomePianeta.equals("marte"))
            idPianeta = R.id.ic_marte;
        else if(nomePianeta.equals("giove"))
            idPianeta = R.id.ic_giove;
        else if(nomePianeta.equals("saturno"))
            idPianeta = R.id.ic_saturno;
        else if(nomePianeta.equals("urano"))
            idPianeta = R.id.ic_urano;
        else if(nomePianeta.equals("nettuno"))
            idPianeta = R.id.ic_nettuno;
        return idPianeta;
    }

    public int risorsaNomePianeta(String nomePianeta){
        int idPianeta = -1;
        if(nomePianeta.equals("mercurio"))
            idPianeta = R.string.mercurio;
        else if(nomePianeta.equals("venere"))
            idPianeta = R.string.venere;
        else if(nomePianeta.equals("terra"))
            idPianeta = R.string.terra;
        else if(nomePianeta.equals("marte"))
            idPianeta = R.string.marte;
        else if(nomePianeta.equals("giove"))
            idPianeta = R.string.giove;
        else if(nomePianeta.equals("saturno"))
            idPianeta = R.string.saturno;
        else if(nomePianeta.equals("urano"))
            idPianeta = R.string.urano;
        else if(nomePianeta.equals("nettuno"))
            idPianeta = R.string.nettuno;
        return idPianeta;
    }

    public int risorsaImmagine(String nomePianeta){
        int idPianeta = -1;
        if(nomePianeta.equals("mercurio"))
            idPianeta = R.drawable.mercurio;
        else if(nomePianeta.equals("venere"))
            idPianeta = R.drawable.venere;
        else if(nomePianeta.equals("terra"))
            idPianeta = R.drawable.terra;
        else if(nomePianeta.equals("marte"))
            idPianeta = R.drawable.marte;
        else if(nomePianeta.equals("giove"))
            idPianeta = R.drawable.giove;
        else if(nomePianeta.equals("saturno"))
            idPianeta = R.drawable.saturno;
        else if(nomePianeta.equals("urano"))
            idPianeta = R.drawable.urano;
        else if(nomePianeta.equals("nettuno"))
            idPianeta = R.drawable.nettuno;
        return idPianeta;
    }

    @Override
    public void onBackPressed() {
        LogUtil.warn("cliccato back " + gestioneBack.toString());

        // Controlla se ci sono pianeti
        String precedente = gestioneBack.precedentePianeta();
        if(precedente != null){
            // Mostra il pianeta
            int idPianeta = idPianeta(precedente);
            ImageView support = findViewById(idPianeta);
            LogUtil.debug("Risorsa associata a  " + idPianeta + " è " + support);
            caricaValoriPianeta(support);
        } else{
            if(visualizzazioneIntro){
                // Normale back, esce dall'applicazione
                super.onBackPressed();
            } else {
                // Mostriamo di nuovo l'intro
                mostraLayoutIntro();
            }
        }

        //
    }
}
