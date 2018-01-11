package com.company;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
    //global classes
    static float[][] weightFirst;
    static float[][] weightSecond;
    static float[][] weightThird;

    static float[] firstLayerNeurons = new float[16];
    static float[] secondLayerNeurons = new float[16];
    static float[] calcAnswer = new float[10];

    static float[][] firstChanges = new float[16][10];
    static float[][] secondChanges = new float[16][16];
    static float[][] thirdChanges = new float[784][16];

    static float[] zero = {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    static float[] one = {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0};

    static float learningRate = (float) 10;
    static float[] error = new float[10];

    static Random rd = new Random();

    static int baise = rd.nextInt(10) + 1;

    public static float sigmoid(float value){
        return (float) (1/(1 + Math.pow(Math.E,value)));
    }

    public static float getSum(int bais, float[] neron, float[][] weight,int row){

        float sum = bais;
        //System.out.println(sum);

            for (int i = 0; i < neron.length; i++) {

                    //for (int a = 0; a < weight[0].length; a++) {

                        //math of 0 = 0 and since majority is black pixels it must be converted to the opposite
                        //neurons are constantly being 0
                        //System.out.println(neron[i]);
                        //System.out.println(weight[i][b]);
                        sum += (neron[i] * weight[i][row]);//math is wrong
                        //System.out.println(sum);

                    //}

            }
            //System.out.println(sum);
        //System.out.println("done");
        //System.out.println("sigmoid = "+sigmoid(sum));
        return sigmoid(sum);

    }

    public static void defineWeights(int x, int y) {

        weightFirst = new float[784][16];
        weightSecond = new float[16][16];
        weightThird = new float[16][10];

        for (int i = 0; i < weightFirst.length; i++) {
            for (int a = 0; a < weightFirst[0].length; a++) {

                weightFirst[i][a] = rd.nextFloat() * 2 - 1;

                //System.out.println(weightFirst[i][a]);
            }
        }

        for (int i = 0; i < weightSecond.length; i++) {
            for (int a = 0; a < weightSecond[0].length; a++) {

                weightSecond[i][a] = rd.nextFloat() * 2 - 1;

                //System.out.println(weightFirst[i][a]);
            }
        }

        for (int i = 0; i < weightThird.length; i++) {
            for (int a = 0; a < weightThird[0].length; a++) {

                weightThird[i][a] = rd.nextFloat() * 2 - 1;

                //System.out.println(weightFirst[i][a]);
            }
        }

        //System.out.println(weightSecond.length);

    }

    //gradient calculator isnt working correctly make sure to calculate the amount of change required for ever individual weight not all of them in general


    public static void defineNeurons(float[] neurons , float[] neron, float[][] weight , int bs){

        for(int i = 0; i < neurons.length; i++){
            neurons[i] = getSum(bs,neron,weight,i);
            //System.out.println(neurons[i]);
        }
        //System.out.println("done");
    }

    public static void applyChanges(float[][] change,float[][] weight){

        //System.out.println("Weights: ");
        for (int i = 0; i < weight.length; i++) {
            for (int a = 0; a < weight[0].length; a++) {
                float prev =  weight[i][a];

                weight[i][a] += change[i][a];

               /* if(prev !=  weight[i][a])
                System.out.println("change: " + weight[i][a] + " from: " + prev);*/
            }
        }
    }

    public static void algorithm(BufferedImage img, int target){

        boolean achieved = false;

        do {
            float[] pixels = new float[img.getWidth() * img.getHeight()];
            float[][] twodPixels = new float[img.getWidth()][img.getHeight()];
            for (int i = 0; i < img.getHeight(); i++) {
                for (int a = 0; a < img.getWidth(); a++) {

                    twodPixels[i][a] = ((img.getRGB(i, a) >> 16) & 0xff) / (float) (255.00);

                    //System.out.println(pixels[b]);
                }
            }

            List<Float> list = new ArrayList<Float>();
            for (int i = 0; i < twodPixels.length; i++) {
                // tiny change 1: proper dimensions
                for (int j = 0; j < twodPixels[i].length; j++) {
                    // tiny change 2: actually store the values
                    list.add(twodPixels[i][j]);
                }
            }

            // now you need to find a mode in the list.

            // tiny change 3, if you definitely need an array
            for (int i = 0; i < pixels.length; i++) {
                pixels[i] = list.get(i);
            }

            defineNeurons(firstLayerNeurons, pixels, weightFirst, baise);
            defineNeurons(secondLayerNeurons, firstLayerNeurons, weightSecond, baise);
            defineNeurons(calcAnswer, secondLayerNeurons, weightThird, baise);

            float compare = 0;
            int max = 0;
            for (int i = 0; i < calcAnswer.length; i++) {

                if (calcAnswer[i] > compare) {
                    max = i;
                    compare = calcAnswer[i];
                }

            }
            System.out.println(max);

            if (max != target) {
                if (target == 0) {

                    for(int i = 0; i < error.length; i++) {
                        error[i] = zero[i] - calcAnswer[i];
                        System.out.println(error[i]);
                        for(int a =0; a< firstChanges.length; a++) {
                            for(int b = 0; b < firstChanges[0].length; b++) {
                                firstChanges[a][b] = learningRate * error[i] * secondLayerNeurons[a] * calcAnswer[i] * (1 - calcAnswer[i]);
                            }
                        }
                    }

                    for(int i = 0; i < error.length; i++) {
                        for(int a =0; a< secondChanges.length; a++) {
                            for(int b = 0; b < secondChanges[0].length; b++) {
                                secondChanges[a][b] = learningRate * error[i] * firstLayerNeurons[a] * secondLayerNeurons[a] * (1 - secondLayerNeurons[a]);
                            }
                        }
                    }

                    for(int i = 0; i < error.length; i++) {
                        for(int a =0; a< thirdChanges.length; a++) {
                            for(int b = 0; b < thirdChanges[0].length; b++) {
                                thirdChanges[a][b] = learningRate * error[i] * pixels[a] * firstLayerNeurons[i] * (1 - firstLayerNeurons[i]);
                            }
                        }
                    }


                   // firstChanges = gradientCalculator(calcAnswer, secondLayerNeurons, zero, 16, 10, weightThird);
                    //secondChanges = gradientCalculator(secondLayerNeurons, firstLayerNeurons, calcAnswer, 16, 16, weightSecond);
                    //thirdChanges = gradientCalculator(firstLayerNeurons, pixels, firstLayerNeurons, 784, 16, weightFirst);

                    //System.out.println("Zero Cost: " + cost(finalSum, zero));
                    applyChanges(firstChanges, weightThird);
                    //System.out.println("Weights: ");
                    applyChanges(secondChanges, weightSecond);

                    applyChanges(thirdChanges, weightFirst);


                } else if (target == 1) {

                   // firstChanges = gradientCalculator(calcAnswer, secondLayerNeurons, one, 16, 10, weightThird);
                    /*secondChanges = gradientCalculator(secondLayerNeurons, firstLayerNeurons, calcAnswer, 16, 16, weightSecond);
                    thirdChanges = gradientCalculator(firstLayerNeurons, pixels, firstLayerNeurons, 784, 16, weightFirst);*/

                    //System.out.println("Zero Cost: " + cost(finalSum, zero));
                   // applyChanges(firstChanges, weightThird);
                    //System.out.println("Weights: ");
                    /*applyChanges(secondChanges, weightSecond);

                    applyChanges(thirdChanges, weightFirst);*/

                }
            }else {
                achieved = true;
            }
        }while (!achieved);

    }

    public static void main(String[] args) {
        // write your code here
        defineWeights(784,784);
        Scanner sc = new Scanner(System.in);
        boolean treh = true;
        int a = 0;
        try {
            while (treh) {
                String num = sc.next();
                File fl = new File("test-images/" + num);
                File[] img = fl.listFiles();

                for (File f : img) {
                    if (f.isFile()) {

                        BufferedImage Img = ImageIO.read(f);
                        algorithm(Img,Integer.parseInt(num));

                    }
                }
            }
        } catch (IOException e) {

            System.out.println(e);


        }
    }
}
