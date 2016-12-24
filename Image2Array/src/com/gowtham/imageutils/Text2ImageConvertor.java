package com.gowtham.imageutils;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Text2ImageConvertor {

	public static void main(String[] args) {
		String text = "அம்மா";

        /*
           Because font metrics is based on a graphics context, we need to create
           a small, temporary image so we can ascertain the width and height
           of the final image
         */
        
        try {
            ImageIO.write(transformToLEDMatrix(text), "png", new File("Text.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	
	public static BufferedImage getBufferedImage(String text) {
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(Color.WHITE);
        g2d.drawString(text, 0, fm.getAscent());
        g2d.dispose();
        return img;
	}	
	
	public static BufferedImage rotate(BufferedImage img) {
		AffineTransform transform = new AffineTransform();
		System.out.println("Before rotating : height : "+img.getHeight()+", width : "+img.getWidth());
		transform.rotate(Math.toRadians(90), img.getHeight(), img.getWidth());
		AffineTransformOp op = new AffineTransformOp(transform, AffineTransformOp.TYPE_BILINEAR);
		img = op.filter(img, null);
		return img;
	}
	
	public static BufferedImage flipHorizondally(BufferedImage img) {
		for(int i=0;i<img.getWidth();i++) {
			for(int j=0;j<img.getHeight()/2;j++) {
				int tmp = img.getRGB(i, j);
				img.setRGB(i, j, img.getRGB(i, img.getHeight()-j-1));
				img.setRGB(i, img.getHeight()-j-1, tmp);
			}
		}
		
		return img;
	}
	
	/*public static BufferedImage trim(BufferedImage img) {
		int width = getTrimmedWidth(img);
		int height = getTrimmedHeight(img);
		
		BufferedImage newImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = newImg.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
		g2d.drawImage(img, 0, 0, null);
		return newImg;
	}
	
	private static int getTrimmedWidth(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();
		int trimmedWidthRight = 0;
		int trimmedWidthLeft = width;
		
		for(int i=0;i<height;i++) {
			for(int j=width -1;j>=0;j--) {
				if(img.getRGB(j, i) != 0 && j > trimmedWidthRight) {
					trimmedWidthRight = j;
					break;
				}
			}
		}
		
		for(int i=0;i<height;i++) {
			for(int j= 0 ;j<width;j++) {
				if(img.getRGB(j, i) != 0 && j < trimmedWidthLeft) {
					trimmedWidthLeft = j;
					break;
				}
			}
		}
		
		System.out.println("trimmedWidthRight : "+trimmedWidthRight);
		System.out.println("trimmedWidthLeft : "+trimmedWidthLeft);
		
		return trimmedWidthRight - trimmedWidthLeft;
	}
	
	private static int getTrimmedHeight(BufferedImage img) {
		int height = img.getHeight();
		int width = img.getWidth();
		int trimmedHeight = 0;
		
		for(int i=0;i<width;i++) {
			for(int j=height -1;j>=0;j--) {
				if(img.getRGB(i, j) != Color.WHITE.getRGB() && j > trimmedHeight) {
					trimmedHeight = j;
					break;
				}
			}
		}
		
		return trimmedHeight;
	}*/
	
	public static BufferedImage crop(BufferedImage image) {
		int minY = 0, maxY = 0, minX = Integer.MAX_VALUE, maxX = 0;
	    boolean isBlank, minYIsDefined = false;
	    Raster raster = image.getRaster();

	    for (int y = 0; y < image.getHeight(); y++) {
	        isBlank = true;

	        for (int x = 0; x < image.getWidth(); x++) {
	            //Change condition to (raster.getSample(x, y, 3) != 0) 
	            //for better performance
	            if (raster.getPixel(x, y, (int[]) null)[3] != 0) {
	                isBlank = false;

	                if (x < minX) minX = x;
	                if (x > maxX) maxX = x;
	            }
	        }

	        if (!isBlank) {
	            if (!minYIsDefined) {
	                minY = y;
	                minYIsDefined = true;
	            } else {
	                if (y > maxY) maxY = y;
	            }
	        }
	    }

	    return image.getSubimage(minX, minY, maxX - minX + 1, maxY - minY + 1);
	}
	
	public static BufferedImage transformToLEDMatrix(String text) {
		BufferedImage img =  rotate(flipHorizondally(getBufferedImage(text)));
		return crop(img);
	}
}
