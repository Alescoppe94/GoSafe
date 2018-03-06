package org.teamids.gestionemappe.model;

public class Beacon {

    private int id;
    private float los;
    private float v;
    private float r;
    private float k;
    private float l;
    private float area;

    public Beacon() {
    }

    public int getId() {
        return id;
    }

    public float getLos() {
        return los;
    }

    public float getV() {
        return v;
    }

    public void setV(float v) {
        this.v = v;
    }

    public float getR() {
        return r;
    }

    public void setR(float r) {
        this.r = r;
    }

    public float getK() {
        return k;
    }

    public void setK(float k) {
        this.k = k;
    }

    public float getL() {
        return l;
    }

    public void setL(float l) {
        this.l = l;
    }

    public float getArea() {
        return area;
    }

    public void setArea(float area) {
        this.area = area;
    }

   /* private int calcolaNumeroPersone(){

    }

    public float calcolaLos(){

    }*/

}
