import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.util.*;

public class YUV{
    
    // I ended up not using these, but they're here for quick reference

    private double[][] rgb2yuvMatrix = {{0.299,0.587,0.114},
                                        {-0.14713,-0.28886,0.436},
                                        {0.615,-0.51449,-0.10001}};
    private double[][] yuv2rgbMatrix =  {{1.0,0.0,1.13983},
                                        {1.0,-0.39465,-0.58060},
                                        {1.0,2.03211,0.0}};
    private double[][] rgb2ycbcrMatrix = {{0.299,0.587,0.114},
                                        {-0.169,-0.331,0.500},
                                        {0.500,-0.419,-0.081}};
    private double[][] ycbcr2rgbMatrix = {{1.0,0.0,1.4},
                                        {1.0,-0.343,-0.711},
                                        {1.0,1.765,0.0}};
    
    
    private int N = 4;
    
    public YUV(){
    }
    
    public double[][] getY(BufferedImage img){
        double[][] Y = new double[img.getHeight()][img.getWidth()];
        Color c;
        int r,g,b;
        
        for (int i = 0; i < img.getHeight(); i++){
            for (int j = 0; j < img.getWidth(); j++){
                c = new Color(img.getRGB(j,i));
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                Y[i][j] = r * 0.299 + g * 0.587 + b * 0.114;
            }
        }
            
        
        return Y;
    }
    
    public double[][] getY(double[][] img){
        int height = img.length;
        int width = img[0].length;
        double[][] Y = new double[height][width];
        Color c;
        int r,g,b;
        
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                c = new Color((int)img[i][j]);
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                Y[i][j] = r * 0.299 + g * 0.587 + b * 0.114;
            }
        }
        
        return Y;
    }
    
    public double[][] getU(BufferedImage img){
        double[][] U = new double[img.getHeight()][img.getWidth()];
        Color c;
        int r,g,b;
        
        for (int i = 0; i < img.getHeight(); i++){
            for (int j = 0; j < img.getWidth(); j++){
                c = new Color(img.getRGB(j,i));
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                U[i][j] = r * -0.14713 + g * -0.28886 + b * 0.436;
            }
        }
            
        
        return U;
    }
    
    public double[][] getU(double[][] img){
        int height = img.length;
        int width = img[0].length;
        double[][] U = new double[height][width];
        Color c;
        int r,g,b;
        
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                c = new Color((int)img[i][j]);
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                U[i][j] = r * -0.14713 + g * -0.28886 + b * 0.436;
            }
        }
        
        return U;
    }
    
    
    public double[][] getV(BufferedImage img){
        double[][] V = new double[img.getHeight()][img.getWidth()];
        Color c;
        int r,g,b;
        
        for (int i = 0; i < img.getHeight(); i++){
            for (int j = 0; j < img.getWidth(); j++){
                c = new Color(img.getRGB(j,i));
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                V[i][j] = r * 0.615 + g * -0.51449 + b * -0.10001;
            }
        }
            
        
        return V;
    }
    
    public double[][] getV(double[][] img){
        int height = img.length;
        int width = img[0].length;
        double[][] V = new double[height][width];
        Color c;
        int r,g,b;
        
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                c = new Color((int)img[i][j]);
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                V[i][j] = r * 0.615 + g * -0.51449 + b * -0.10001;
            }
        }
        
        return V;
    }
    
    
    public double[][] getCb(BufferedImage img){
        double[][] Cb = new double[img.getHeight()][img.getWidth()];
        Color c;
        int r,g,b;
        
        for (int i = 0; i < img.getHeight(); i++){
            for (int j = 0; j < img.getWidth(); j++){
                c = new Color(img.getRGB(j,i));
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                Cb[i][j] = r * -0.169 + g * -0.331 + b * 0.500 + 128;
            }
        }

        return Cb;
    }
    
    public double[][] getCb(double[][] img){
        int height = img.length;
        int width = img[0].length;
        double[][] Cb = new double[height][width];
        Color c;
        int r,g,b;
        
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                c = new Color((int)img[i][j]);
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                Cb[i][j] = r * -0.169 + g * -0.331 + b * 0.500 + 128;
            }
        }
        
        return Cb;
    }
    
    public double[][] getCr(BufferedImage img){
        double[][] Cr = new double[img.getHeight()][img.getWidth()];
        Color c;
        int r,g,b;
        
        for (int i = 0; i < img.getHeight(); i++){
            for (int j = 0; j < img.getWidth(); j++){
                c = new Color(img.getRGB(j,i));
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                Cr[i][j] = r * 0.500 + g * -0.419 + b * -0.081 + 128;
            }
        }

        return Cr;
    }
    
    public double[][] getCr(double[][] img){
        int height = img.length;
        int width = img[0].length;
        double[][] Cr = new double[height][width];
        Color c;
        int r,g,b;
        
        for (int i = 0; i < height; i++){
            for (int j = 0; j < width; j++){
                c = new Color((int)img[i][j]);
                r = c.getRed();
                g = c.getGreen();
                b = c.getBlue();
                Cr[i][j] = r * 0.500 + g * -0.419 + b * -0.081 + 128;
            }
        }
        
        return Cr;
    }
    
    public double[][] getGrayscale(double[][] img, double[][] sub){
        int r,g,b;
        int h = img.length;
        int w = img[0].length;
        double[][] gr = new double[h][w];
        Color c;
        
        if (sub == null){
            for (int i = 0; i < h; i++){
                for (int j = 0; j < w; j++){
                    r = g = b = (int)img[i][j];
                    c = new Color(r,g,b);
                    gr[i][j] = c.getRGB();
                }
            }
        }
        
        else {
            for (int i = 0; i < h; i++){
                for (int j = 0; j < w; j++){
                    r = g = b = (int)(img[i][j]) + (int)(sub[i][j]);
                    c = new Color(r,g,b);
                    gr[i][j] = c.getRGB();
                }
            }
        }
        
        return gr;
    }
    
    public void sample422(double[][] chroma){
        int r = chroma.length;
        int c = chroma[0].length;
        
        for (int i = 0; i < r; i++){
            for (int j = 1; j < c; j+=2){
                chroma[i][j] = chroma[i][j-1];
            }
        }
    }
    
    public void sample420(double[][] chroma){
        int r = chroma.length;
        int c = chroma[0].length;
        
        for (int i = 0; i < r; i+=2){
            for (int j = 1; j < c; j+=2){
                chroma[i][j] = chroma[i][j-1];
                chroma[i+1][j-1] = chroma[i][j-1];
                chroma[i+1][j] = chroma[i][j-1];
            }
        }
    }
    
    public double[][] ycbcr2rgb(double[][] Y, double[][] Cb, double[][] Cr){
        int h = Y.length;
        int w = Y[0].length;
        double[][] result = new double[h][w];
        int r,g,b;
        Color c;
        
        for (int i = 0; i < h; i++){
            for (int j = 0; j < w; j++){
                r = (int)(Y[i][j] + (Cr[i][j]-128) * 1.4);
                g = (int)(Y[i][j] + (Cb[i][j]-128) * -0.343 + (Cr[i][j]-128) * -0.711);
                b = (int)(Y[i][j] + (Cb[i][j]-128) * 1.765);
                
                if (r > 255) r = 255;
                if (r < 0) r = 0;
                if (g > 255) g = 255;
                if (g < 0) g = 0;
                if (b > 255) b = 255;
                if (b < 0) b = 0;
                
                c = new Color(r,g,b);
                result[i][j] = c.getRGB();
            }
        }
        return result;
    }
    
    public double[][] yuv2rgb(double[][] Y, double[][] U, double[][] V){
        int h = Y.length;
        int w = Y[0].length;
        double[][] result = new double[h][w];
        int r,g,b;
        Color c;
        
        for (int i = 0; i < h; i++){
            for (int j = 0; j < w; j++){
                r = (int)(Y[i][j] + (V[i][j]) * 1.13983);
                g = (int)(Y[i][j] + (U[i][j]) * -0.39465 + (V[i][j]) * -0.58060);
                b = (int)(Y[i][j] + (U[i][j]) * 2.03211);
                
                if (r > 255) r = 255;
                if (r < 0) r = 0;
                if (g > 255) g = 255;
                if (g < 0) g = 0;
                if (b > 255) b = 255;
                if (b < 0) b = 0;
                
                c = new Color(r,g,b);
                result[i][j] = c.getRGB();
            }
        }
        return result;
    }
    
    public double[][] sample(BufferedImage img, int mode){
        double[][] Y, Cb, Cr, resultArray;
        
        Y = getY(img);
        Cb = getCb(img);
        Cr = getCr(img);
        
        if (mode == 1){
            System.out.println("Applying 4:2:2...");
            sample422(Cb);
            sample422(Cr);
        }
        else if (mode == 2){
            System.out.println("Applying 4:2:0...");
            sample420(Cb);
            sample420(Cr);
        }
        
        resultArray = ycbcr2rgb(Y,Cb,Cr);
        
        return resultArray;
    }
    
    public void div2(double[][] arr){
        int h = arr.length;
        int w = arr[0].length;
        
        for (int i = 0; i < h; i++){
            for (int j = 0; j < w; j++){
                arr[i][j] /= 2.0;
            }
        }
    }
}