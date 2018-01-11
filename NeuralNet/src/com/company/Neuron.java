package com.company;

public class Neuron {

    private float[][] pixels;
    private float[] pixelsSecond;
    private float[][] weight;
    private int bais;

    public Neuron (float[][] pixels, float[][] weight, int bais){

        this.pixels = pixels;
        this.weight = weight;
        this.bais = bais;

    }

    public Neuron (float[] pixels, float[][] weight, int bais){

        this.pixelsSecond = pixels;
        this.weight = weight;
        this.bais = bais;

    }

    public float sigmoid(float value){
        return (float) (1/(1 + Math.pow(Math.E,value)));
    }

    public float getSum(){

        float sum = bais;

        if(pixels != null) {
            for (int i = 0; i < pixels.length; i++) {
                for (int a = 0; a < pixels[0].length; a++) {

                    sum += (pixels[i][a] * weight[i][a]);
                    //System.out.println(sum);

                }
            }
        }else{
            for (int i = 0; i < pixelsSecond.length - 1; i++) {
                for (int a = 0; a < weight[0].length; a++) {

                    sum += (pixelsSecond[i] * weight[i][a]);
                    //System.out.println(sum);

                }
            }
        }

        return sigmoid(sum);

    }

    public float[][] getPixels() {
        return pixels;
    }

    public float[] getPixelsSecond(){ return pixelsSecond; }

    public void setPixels(float[][] pixels) {
        this.pixels = pixels;
    }

    public float[][] getWeight() {
        return weight;
    }

    public void setWeight(float[][] weight) {
        this.weight = weight;
    }

    public int getBais() {
        return bais;
    }

    public void setBais(int bais) {
        this.bais = bais;
    }

}
