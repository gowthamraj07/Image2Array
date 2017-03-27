package com.gowtham.imageutils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Text2LEDDisplayInput {

	public static void main(String[] args) {
		//அம்மா
		//வரவேற்கிறோம்
		//சென்னை - கோயம்புத்தூர்
		int res[][] = ImageProcessor.getBWImage(
				Text2ImageConvertor.transformToLEDMatrix("Chennai")
				);
		
		File inputFile = new File("input.bin");
		try {
			PrintWriter out = new PrintWriter(inputFile);
			for(int i=0;i<res.length;i++) {
				for(int j=0;j<res[i].length;j++) {
					out.print(res[i][j]);
				}
				out.println();
			}
			out.flush();
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
