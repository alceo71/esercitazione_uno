package com.example.esercitazioneuno;

import androidx.annotation.NonNull;

import com.example.esercitazioneuno.common.util.LogUtil;

import java.util.Stack;

/**
 * Tiene memoria della sequenza di pianeti e torna a quello precendente
 *
 */
public class GestioneBack {

    private Stack<String> navigazione;

    public GestioneBack(){
        navigazione = new Stack<>();
    }

    public void aggiungiPianeta(String pianeta){

        if(! navigazione.empty()){
            // Aggiungi il pianeta solo se è diverso dall'ultimo
            String ultimo = navigazione.get(navigazione.size()-1);
            if(!ultimo.equals(pianeta))
                navigazione.push(pianeta);
        } else{
            navigazione.push(pianeta);
        }
    }

    public String precedentePianeta(){
        if(navigazione.size() <= 1)
            return null;
        else {
            navigazione.pop();
            return navigazione.get(navigazione.size()-1);
        }
    }

    @NonNull
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for(String valore:navigazione){
            sb.append(valore + " - ");
        }
        return sb.toString();
    }
}
