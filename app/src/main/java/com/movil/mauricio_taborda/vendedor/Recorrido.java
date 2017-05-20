package com.movil.mauricio_taborda.vendedor;

import java.util.ArrayList;

/**
 * Created by AForce on 19/05/2017.
 */

public class Recorrido {
    public String date;
    public ArrayList<Punto> puntos;

    public Recorrido(String date, ArrayList<Punto> puntos) {
        this.date = date;
        this.puntos = puntos;
    }

    public void AddPunto(Punto punto) {
        this.puntos.add(punto);
    }
}
