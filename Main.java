import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;
import java.util.*;
 
/**
 * This class demonstrates how to load an Image from an external file
 */
public class Main{
 
    public static void main(String[] args) {
        final int N = 8;
        BufferedImage img, tempImg, newImg;
        String filename, newFilename;
        int mode = 0;
        int choice;
        double tempArr[][] = new double[N][N];
        double yArr[][] = new double[N][N];
        double cbArr[][] = new double[N][N];
        double crArr[][] = new double[N][N];
        double yArrFull[][];
        double uArrFull[][];
        double vArrFull[][];
        double cbArrFull[][];
        double crArrFull[][];
        double tempArrFull[][];
        int padX=0, padY=0;
        Scanner s = new Scanner(System.in);
        DCT dctObj = new DCT();
        YUV yuvObj = new YUV();
        
        img = null;
        
        System.out.print("Please enter a filename (.bmp will be automatically added): ");
        filename = s.nextLine();
        
        
        try {
           img = ImageIO.read(new File(filename+".bmp"));
        } catch (IOException e) {
           System.out.println("File not found. Closing program.");
           System.exit(0);
        }
        
        if (img.getWidth() % N != 0)
            padX = N - img.getWidth()%N;
        if (img.getHeight() % N != 0)
            padY = N - img.getHeight()%N;
            
        newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        tempImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        tempArrFull = new double[img.getHeight()][img.getWidth()];
        
        System.out.println("Width: " + img.getWidth() + " Height: " + img.getHeight());
        //System.out.println("X Padding: " + padX + " Y Padding: " + padY);
        System.out.println("Max blockX: " + (img.getWidth() + padX)/N + " Max blockY: " + (img.getHeight() + padY)/8);
        
        System.out.println("\n[0] DCT\n[1] YUV");
        System.out.print("Which process would you like to try? ");
        choice = s.nextInt();
        
        if (choice == 0){
            System.out.println("\n[0] Normal\n[1] DC only\n[2] DC+5AC\n[3] Normal+Quant");
            System.out.print("Which DCT mode would you like to use? ");
            mode = s.nextInt();
            
            // for each 8x8 block, L->R, T->B
            for (int blockY = 0; blockY < (img.getHeight() + padY)/N; blockY++){
                for (int blockX = 0; blockX < (img.getWidth() + padX)/N; blockX++){
                    // construct color matrix, applying white padding to the right/bottom of the image if size is not neatly divisible by 8
                    for (int y = blockY*N; y < (blockY+1)*N; y++){
                        for (int x = blockX*N; x < (blockX+1)*N; x++){
                            try{
                            tempArr[x-(blockX*N)][y-(blockY*N)] = (double)(img.getRGB(x,y));
                            } catch (ArrayIndexOutOfBoundsException e){
                               tempArr[x-(blockX*N)][y-(blockY*N)] = -1.0; //set white padding pixel
                            }
                        }
                    }
                    
                    // acquire YCbCr matrices
                    yArr = yuvObj.getY(tempArr);
                    cbArr = yuvObj.getCb(tempArr);
                    crArr = yuvObj.getCr(tempArr);
                    
                    // apply DCT to the Y image
                    tempArr = dctObj.forwardDCT(yArr, mode);
                    
                    // write processed image
                    for (int y = blockY*N; y < (blockY+1)*N; y++){
                        for (int x = blockX*N; x < (blockX+1)*N; x++){
                            try{
                            tempImg.setRGB(x,y,(int)tempArr[x-(blockX*N)][y-(blockY*N)]);
                            } catch (ArrayIndexOutOfBoundsException e){
                            }
                        }
                    }
                    
                    // apply IDCT to processed Y image
                    tempArr = dctObj.inverseDCT(tempArr, mode);
                    
                    for (int y = blockY*N; y < (blockY+1)*N; y++){
                        for (int x = blockX*N; x < (blockX+1)*N; x++){
                            try{
                            tempArrFull[x][y] = tempArr[x-(blockX*N)][y-(blockY*N)];
                            } catch (ArrayIndexOutOfBoundsException e){
                            }
                        }
                    }
                    
                    // reconvert from YCbCr to RGB color space
                    tempArr = yuvObj.ycbcr2rgb(tempArr, cbArr, crArr);
                    
                    for (int y = blockY*N; y < (blockY+1)*N; y++){
                        for (int x = blockX*N; x < (blockX+1)*N; x++){
                            try{
                            newImg.setRGB(x,y,(int)tempArr[x-(blockX*N)][y-(blockY*N)]);
                            } catch (ArrayIndexOutOfBoundsException e){
                            }
                        }
                    }
                    
                }
            }
            
            try {
                
                switch (mode){
                    case 0:
                        newFilename = filename + "_reverted_normal.bmp";
                        ImageIO.write(newImg, "bmp", new File(newFilename));
                        ImageIO.write(tempImg, "bmp", new File(filename + "_DCT_normal.bmp"));
                        break;
                    
                    case 1:
                        newFilename = filename+ "_reverted_DCOnly.bmp";
                        ImageIO.write(newImg, "bmp", new File(newFilename));
                        ImageIO.write(tempImg, "bmp", new File(filename + "_DCT_DCOnly.bmp"));
                        break;
                    
                    case 2:
                        newFilename = filename+ "_reverted_DC5AC.bmp";
                        ImageIO.write(tempImg, "bmp", new File(filename + "_DCT_DC5AC.bmp"));
                        break;
                    
                    default:
                        ImageIO.write(tempImg, "bmp", new File(filename + "_DCT_quant.bmp"));
                        newFilename = filename+ "_reverted_quant.bmp";
                    
                }      
                
                ImageIO.write(newImg, "bmp", new File(newFilename));
                
                yArrFull = yuvObj.getY(img);
                
                // acquire PSNR between original and processed images
                System.out.println("PSNR: " + dctObj.psnr(yArrFull, tempArrFull)); 
                
            }   catch (IOException e) {
                System.exit(0);
            }
        }
        
        else {
            
            System.out.println("\n[0] 4:4:4\n[1] 4:2:2\n[2] 4:2:0");
            System.out.print("Which sampling mode would you like to use? ");
            mode = s.nextInt();
            switch (mode){
                case 1:
                    filename += "_422";
                    break;
                
                case 2:
                    filename += "_420";
                    break;
                
                default:
                    filename += "_444";
            }
            
            // apply chroma sampling
            tempArrFull = yuvObj.sample(img,mode);
            
            // acquire YUV components
            yArrFull = yuvObj.getY(tempArrFull);
            uArrFull = yuvObj.getU(tempArrFull);
            vArrFull = yuvObj.getV(tempArrFull);
            
            // write components to files
            tempArrFull = yuvObj.getGrayscale(yArrFull, null);
            
            for (int i = 0; i < img.getHeight(); i++){
                for (int j = 0; j < img.getWidth(); j++){
                    newImg.setRGB(j, i, (int)tempArrFull[i][j]);
                }
            }
            
            try {
                ImageIO.write(newImg, "bmp", new File(filename + "_Y_normal.bmp"));
            } catch (IOException e) {
                System.exit(0);
            }
            
            tempArrFull = yuvObj.getGrayscale(yArrFull, uArrFull);
            
            for (int i = 0; i < img.getHeight(); i++){
                for (int j = 0; j < img.getWidth(); j++){
                    newImg.setRGB(j, i, (int)tempArrFull[i][j]);
                }
            }
            
            try {
                ImageIO.write(newImg, "bmp", new File(filename + "_U_normal.bmp"));
            } catch (IOException e) {
                System.exit(0);
            }
            
            tempArrFull = yuvObj.getGrayscale(yArrFull, vArrFull);
            
            for (int i = 0; i < img.getHeight(); i++){
                for (int j = 0; j < img.getWidth(); j++){
                    newImg.setRGB(j, i, (int)tempArrFull[i][j]);
                }
            }
            
            try {
                ImageIO.write(newImg, "bmp", new File(filename + "_V_normal.bmp"));
            } catch (IOException e) {
                System.exit(0);
            }
            
            
            // reconstruct image
            tempArrFull = yuvObj.yuv2rgb(yArrFull, uArrFull, vArrFull);
            
            for (int i = 0; i < img.getHeight(); i++){
                for (int j = 0; j < img.getWidth(); j++){
                    newImg.setRGB(j, i, (int)tempArrFull[i][j]);
                }
            }
            
            try {
                ImageIO.write(newImg, "bmp", new File(filename + "_YUV_reconstructed.bmp"));
            } catch (IOException e) {
                System.exit(0);
            }
            
            // diminish Y value incrementally
            for(int p = 2; p <= 8; p*=2){
                yuvObj.div2(yArrFull);
                
                tempArrFull = yuvObj.yuv2rgb(yArrFull, uArrFull, vArrFull);
                
                for (int i = 0; i < img.getHeight(); i++){
                    for (int j = 0; j < img.getWidth(); j++){
                        newImg.setRGB(j, i, (int)tempArrFull[i][j]);
                    }
                }
            
                try {
                    ImageIO.write(newImg, "bmp", new File(filename + "_YUV_div" + p + "_reconstructed.bmp"));
                } catch (IOException e) {
                    System.exit(0);
                }
                
            }
        }
        
    }
}