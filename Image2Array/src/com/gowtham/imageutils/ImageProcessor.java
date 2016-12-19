package com.gowtham.imageutils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

public class ImageProcessor {

	public static void main(String[] args) {
		try {
			BufferedImage img = ImageIO.read(new File("Text.png"));
			/*byte[] pixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
			for(byte b : pixels) {
				System.out.println(b);
			}*/
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd hh:mm:ss");
			System.out.println("Starting at : "+sdf.format(new Date()));
			getBWImage(img);
			//processImage(img);
			System.out.println("Ending at   : "+sdf.format(new Date()));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void processImage(BufferedImage img) {
		int[][] res = convertTo2DWithoutUsingGetRGB(img);
		System.out.println("Size : "+res.length+"x"+res[0].length);
		for(int i=0;i<res.length;i++) {
			for(int j=0;j<res[i].length;j++) {
				System.out.print(""+res[i][j]+" ");
			}
			System.out.println();
		}
	}
	
	public static int[][] getBWImage(BufferedImage img) {
		int[][] res = convertTo2DWithoutUsingGetRGB(img);
		int[][] byteRes = new int[res.length][res[0].length];
		System.out.println("Size : "+res.length+"x"+res[0].length);
		
		for(int i=0;i<res.length;i++){
  		  for(int j=0; j<res[i].length;j++) {
  			  
  			  byteRes[i][j] =  (res[i][j] >= 0 ? 0 : 1);
  			  System.out.print(byteRes[i][j]);
  		  }
  		  System.out.println();
  	  }
		
		return byteRes;
	}
	
	public static int[][] convertTo2DWithoutUsingGetRGB(BufferedImage image) {

	      //final byte[] pixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
	      final int width = image.getWidth();
	      final int height = image.getHeight();
	      //final boolean hasAlphaChannel = image.getAlphaRaster() != null;
	      System.out.println("Type : "+image.getType());

	      int[][] result = new int[height][width];
	      /*if (hasAlphaChannel) {
	         final int pixelLength = 4;
	         for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
	            int argb = 0;
	            argb += (((int) pixels[pixel] & 0xff) << 24); // alpha
	            argb += ((int) pixels[pixel + 1] & 0xff); // blue
	            argb += (((int) pixels[pixel + 2] & 0xff) << 8); // green
	            argb += (((int) pixels[pixel + 3] & 0xff) << 16); // red
	            result[row][col] = argb;
	            col++;
	            if (col == width) {
	               col = 0;
	               row++;
	            }
	         }
	      } else */
	      //if(image.getType() == 13 || image.getType() == 6) {
	    	  for(int i=0;i<image.getHeight();i++){
	    		  for(int j=0; j<image.getWidth();j++) {
	    			  int rgb = image.getRGB(j, i);
	    			  result[i][j] = rgb; 
	    		  }
	    	  }
	      //}
	    	  /*else {
	         final int pixelLength = 3;
	         for (int pixel = 0, row = 0, col = 0; pixel < pixels.length; pixel += pixelLength) {
	            int argb = 0;
	            argb += -16777216; // 255 alpha
	            argb += ((int) pixels[pixel] & 0xff); // blue
	            argb += (((int) pixels[pixel + 1] & 0xff) << 8); // green
	            argb += (((int) pixels[pixel + 2] & 0xff) << 16); // red
	            result[row][col] = argb;
	            col++;
	            if (col == width) {
	               col = 0;
	               row++;
	            }
	         }
	      }*/

	      return result;
	   }

}
