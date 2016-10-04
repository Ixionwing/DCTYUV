import java.lang.*;
import java.io.*;
import java.util.*;

public class DCT {
    private int N = 8;
    private double[] coeffs = new double[N];
    private double[][] lumq = {{16,11,10,16,24,40,51,61},
                                {12,12,14,19,26,58,60,55},
                                {14,13,16,24,40,57,69,56},
                                {14,17,22,29,51,87,80,62},
                                {18,22,37,56,68,109,103,77},
                                {24,35,55,64,81,104,113,92},
                                {49,64,78,87,103,121,120,101},
                                {72,92,95,98,112,100,103,99}
    };
    private int zigZag[][] = new int[64][2];
    private double prevDC = 0;
    private boolean firstEncode;
    private boolean firstDecode;

    public DCT() {
        coeffs[0] = Math.sqrt(2.0)/2.0;
        for (int i=1;i<N;i++) {
            coeffs[i] = 1;
        }
        firstEncode = true;
        firstDecode = true;
    }
    
    
    // acquired from http://www.nyx.net/~smanley/dct/DCT.java (Manley, S.)
    private void initZigZag()
    {
        // j = x,y
        zigZag[0][0] = 0; // 0,0
        zigZag[0][1] = 0;
        zigZag[1][0] = 0; // 0,1
        zigZag[1][1] = 1;
        zigZag[2][0] = 1; // 1,0
        zigZag[2][1] = 0;
        zigZag[3][0] = 2; // 2,0
        zigZag[3][1] = 0;
        zigZag[4][0] = 1; // 1,1
        zigZag[4][1] = 1;
        zigZag[5][0] = 0; // 0,2
        zigZag[5][1] = 2;
        zigZag[6][0] = 0; // 0,3
        zigZag[6][1] = 3;
        zigZag[7][0] = 1; // 1,2
        zigZag[7][1] = 2;
        zigZag[8][0] = 2; // 2,1
        zigZag[8][1] = 1;
        zigZag[9][0] = 3; // 3,0
        zigZag[9][1] = 0;
        zigZag[10][0] = 4; // 4,0
        zigZag[10][1] = 0;
        zigZag[11][0] = 3; // 3,1
        zigZag[11][1] = 1;
        zigZag[12][0] = 2; // 2,2
        zigZag[12][1] = 2;
        zigZag[13][0] = 1; // 1,3
        zigZag[13][1] = 3;
        zigZag[14][0] = 0; // 0,4
        zigZag[14][1] = 4;
        zigZag[15][0] = 0; // 0,5
        zigZag[15][1] = 5;
        zigZag[16][0] = 1; // 1,4
        zigZag[16][1] = 4;
        zigZag[17][0] = 2; // 2,3
        zigZag[17][1] = 3;
        zigZag[18][0] = 3; // 3,2
        zigZag[18][1] = 2;
        zigZag[19][0] = 4; // 4,1
        zigZag[19][1] = 1;
        zigZag[20][0] = 5; // 5,0
        zigZag[20][1] = 0;
        zigZag[21][0] = 6; // 6,0
        zigZag[21][1] = 0;
        zigZag[22][0] = 5; // 5,1
        zigZag[22][1] = 1;
        zigZag[23][0] = 4; // 4,2
        zigZag[23][1] = 2;
        zigZag[24][0] = 3; // 3,3
        zigZag[24][1] = 3;
        zigZag[25][0] = 2; // 2,4
        zigZag[25][1] = 4;
        zigZag[26][0] = 1; // 1,5
        zigZag[26][1] = 5;
        zigZag[27][0] = 0; // 0,6
        zigZag[27][1] = 6;
        zigZag[28][0] = 0; // 0,7
        zigZag[28][1] = 7;
        zigZag[29][0] = 1; // 1,6
        zigZag[29][1] = 6;
        zigZag[30][0] = 2; // 2,5
        zigZag[30][1] = 5;
        zigZag[31][0] = 3; // 3,4
        zigZag[31][1] = 4;
        zigZag[32][0] = 4; // 4,3
        zigZag[32][1] = 3;
        zigZag[33][0] = 5; // 5,2
        zigZag[33][1] = 2;
        zigZag[34][0] = 6; // 6,1
        zigZag[34][1] = 1;
        zigZag[35][0] = 7; // 7,0
        zigZag[35][1] = 0;
        zigZag[36][0] = 7; // 7,1
        zigZag[36][1] = 1;
        zigZag[37][0] = 6; // 6,2
        zigZag[37][1] = 2;
        zigZag[38][0] = 5; // 5,3
        zigZag[38][1] = 3;
        zigZag[39][0] = 4; // 4,4
        zigZag[39][1] = 4;
        zigZag[40][0] = 3; // 3,5
        zigZag[40][1] = 5;
        zigZag[41][0] = 2; // 2,6
        zigZag[41][1] = 6;
        zigZag[42][0] = 1; // 1,7
        zigZag[42][1] = 7;
        zigZag[43][0] = 2; // 2,7
        zigZag[43][1] = 7;
        zigZag[44][0] = 3; // 3,6
        zigZag[44][1] = 6;
        zigZag[45][0] = 4; // 4,5
        zigZag[45][1] = 5;
        zigZag[46][0] = 5; // 5,4
        zigZag[46][1] = 4;
        zigZag[47][0] = 6; // 6,3
        zigZag[47][1] = 3;
        zigZag[48][0] = 7; // 7,2
        zigZag[48][1] = 2;
        zigZag[49][0] = 7; // 7,3
        zigZag[49][1] = 3;
        zigZag[50][0] = 6; // 6,4
        zigZag[50][1] = 4;
        zigZag[51][0] = 5; // 5,5
        zigZag[51][1] = 5;
        zigZag[52][0] = 4; // 4,6
        zigZag[52][1] = 6;
        zigZag[53][0] = 3; // 3,7
        zigZag[53][1] = 7;
        zigZag[54][0] = 4; // 4,7
        zigZag[54][1] = 7;
        zigZag[55][0] = 5; // 5,6
        zigZag[55][1] = 6;
        zigZag[56][0] = 6; // 6,5
        zigZag[56][1] = 5;
        zigZag[57][0] = 7; // 7,4
        zigZag[57][1] = 4;
        zigZag[58][0] = 7; // 7,5
        zigZag[58][1] = 5;
        zigZag[59][0] = 6; // 6,6
        zigZag[59][1] = 6;
        zigZag[60][0] = 5; // 5,7
        zigZag[60][1] = 7;
        zigZag[61][0] = 6; // 6,7
        zigZag[61][1] = 7;
        zigZag[62][0] = 7; // 7,6
        zigZag[62][1] = 6;
        zigZag[63][0] = 7; // 7,7
        zigZag[63][1] = 7;
    }
    

    public double[][] forwardDCT(double[][] f, int mode) {
        double[][] fp = new double[N][N];
        
        for (int u=0;u<N;u++) {
          for (int v=0;v<N;v++) {
          
            double sum = 0.0;
            
            for (int i=0;i<N;i++) {
              for (int j=0;j<N;j++) {
                sum += Math.cos( (2*i+1) * u * Math.PI / (2.0*N) ) * Math.cos( (2*j+1) * v * Math.PI / (2.0*N) ) * f[i][j];
              }
            }
            
            sum *= ((coeffs[u]*coeffs[v]) / 4.0); // 2C(u)C(v)/sqrt(64) = (2/8)C(u)C(v) = C(u)C(v)/4
            
            switch (mode){
                
                case 1:
                    // DC only
                    if (u+v == 0)
                        fp[u][v] = sum;
                    break;
                
                case 2:
                    // DC + 5AC
                    if (u+v < 3)
                        fp[u][v] = sum;
                    break;
                
                default:
                    // normal or quantized
                    fp[u][v] = sum;
            }
            
          }
        }
        
        if (mode == 3){
            fp = quantizeImage(fp);
        }
        
        return fp;
    }

    public double[][] inverseDCT(double[][] fp, int mode) {
        double[][] f = new double[N][N];
        
        if (mode == 3){
            fp = dequantizeImage(fp);
        }
        
        for (int i=0;i<N;i++) {
          for (int j=0;j<N;j++) {
          
            double sum = 0.0;
            
            for (int u=0;u<N;u++) {
              for (int v=0;v<N;v++) {
              
                // 2C(u)C(v)/sqrt(64) = (2/8)C(u)C(v) = C(u)C(v)/4
                sum += ( (coeffs[u]*coeffs[v]) / 4.0 ) * Math.cos( (2*i+1) * u * Math.PI / (N*2.0)) * Math.cos((2*j+1) * v * Math.PI / (N*2.0) )*fp[u][v];
              
              }
            }
            
            f[i][j]=Math.round(sum);
            
          }
        }
        
        return f;
    }
    
    // acquired from http://www.nyx.net/~smanley/dct/DCT.java (Manley, S.) and modified to suit this program's paradigm
    public double[][] quantizeImage(double inputData[][])
    {
        double outputData[][] = new double[N][N];
        int r,c;

        double result;
        
        for (int i = 0; i < (N*N); i++){
                r = zigZag[i][0];
                c = zigZag[i][1];
                
                if (r==0 && c==0){
                    result = inputData[r][c] - prevDC;
                    prevDC = inputData[r][c];
                }
                
                // TODO: RLC all ACs at this step?
                else
                    result = (inputData[r][c] / lumq[r][c]);
                    
                outputData[r][c] = (int)(Math.round(result));
        }


        return outputData;
    }
    
    public double[][] dequantizeImage(double inputData[][])
    {
        double outputData[][] = new double[N][N];
        int r,c;
        double result;
        
        for (int i = 0; i < (N*N); i++){
                r = zigZag[i][0];
                c = zigZag[i][1];
                
                if (r==0 && c==0){
                    result = inputData[r][c] + prevDC;
                    prevDC = result;
                }
                
                else
                    result = (inputData[r][c] * lumq[r][c]);
                
                outputData[r][c] = (int)(Math.round(result));
        }


        return outputData;
    }
    
    
    public double psnr(double[][] img1, double[][] img2){
        double p = 0, n = 0, mse = 0;
        int r = img1.length;
        int c = img1[0].length;
        
        for (int i = 0; i < r; i++){
            for (int j = 0; j < c; j++){
                n += Math.pow((img1[i][j] - img2[i][j]), 2);
                p = Math.max(p,img1[i][j]);
            }
        }
        
        mse = n/(r*c);
        
        return 10 * Math.log(Math.pow(p,2)/mse) / Math.log(10);
    }
    
    /*
    // acquired from http://www.cyut.edu.tw/~yltang/program/ArrayIO.java (Tang, Y.L.)
    private void readByteArray(String filename, int arr[][], int r, int c) {
        try {
        File file = new File(filename);
        FileInputStream fin = new FileInputStream(file);
        
        for (int i=0; i<r; i++)
            for (int j=0; j<c; j++)
                arr[i][j] = (int)(0xFF & fin.read());
                
        fin.close();
        }
        
        catch (Exception e) {
          System.out.println(e.getMessage());
          System.exit(0);
        }
    }
    
    public double psnr(int r, int c, String file1, String file2){
        double p = 0, n = 0, mse = 0;
        int img1[][] = new int[r][c];
        int img2[][] = new int[r][c];
        
        readByteArray(file1, img1, r, c);
        readByteArray(file2, img2, r, c);
        
        for (int i = 0; i < r; i++){
            for (int j = 0; j < c; j++){
                n += Math.pow((img1[i][j] - img2[i][j]), 2);
                p = Math.max(p,img1[i][j]);
            }
        }
        
        mse = n/(r*c);
        
        return 10 * Math.log(Math.pow(p,2)/mse) / Math.log(10);
    } */
    
}