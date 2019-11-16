package com.example.esercitazioneuno.common.model;

public class Pianeta {

    // Nome del pianeta
    private String nome;

    // Distanza dal sole in UA
    private double distanzaUA;

    // Periodo rivoluzione in giorni
    private int rivoluzioneGiorni;

    // Periodo di rotaazione in ore
    private int rotazioneOre;

    // Massa in rapporto alla terra
    private double massa;

    // Diametro in rapporto alla terra
    private double diametro;

    // Numero di satelliti
    private int numeroSatelliti;


    public Pianeta(String nome, double distanzaUA, int rivoluzioneGiorni, int rotazioneOre, double massa, double diametro, int numeroSatelliti) {
        this.nome = nome;
        this.distanzaUA = distanzaUA;
        this.rivoluzioneGiorni = rivoluzioneGiorni;
        this.rotazioneOre = rotazioneOre;
        this.massa = massa;
        this.diametro = diametro;
        this.numeroSatelliti = numeroSatelliti;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getDistanzaUA() {
        return distanzaUA;
    }

    public void setDistanzaUA(double distanzaUA) {
        this.distanzaUA = distanzaUA;
    }

    public int getRivoluzioneGiorni() {
        return rivoluzioneGiorni;
    }

    public int getRivoluzioneAnni() {
        return rivoluzioneGiorni/365;
    }

    public void setRivoluzioneGiorni(int rivoluzioneGiorni) {
        this.rivoluzioneGiorni = rivoluzioneGiorni;
    }

    public int getRotazioneOre() {
        return rotazioneOre;
    }

    public int getRotazioneGiorni() {
        return rotazioneOre/24;
    }

    public void setRotazioneOre(int rotazioneOre) {
        this.rotazioneOre = rotazioneOre;
    }

    public double getMassa() {
        return massa;
    }

    public void setMassa(double massa) {
        this.massa = massa;
    }

    public double getDiametro() {
        return diametro;
    }

    public void setDiametro(double diametro) {
        this.diametro = diametro;
    }

    public int getNumeroSatelliti() {
        return numeroSatelliti;
    }

    public void setNumeroSatelliti(int numeroSatelliti) {
        this.numeroSatelliti = numeroSatelliti;
    }
}
