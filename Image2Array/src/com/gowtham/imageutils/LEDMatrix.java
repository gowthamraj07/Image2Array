package com.gowtham.imageutils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import javax.imageio.IIOException;
import javax.imageio.ImageIO;

import com.pi4j.io.i2c.I2CBus;
import com.pi4j.io.i2c.I2CDevice;
import com.pi4j.io.i2c.I2CFactory;
import com.pi4j.system.NetworkInfo;
import com.pi4j.system.SystemInfo;

public class LEDMatrix {
	
	private static final int MATRIX_HEIGHT = 16;
	private static final int MATRIX_WIDTH = 16;
	private static final List<I2CDevice> DEVICES = new LinkedList<I2CDevice>();
	

	// package de.buschbaum.java.pathfinder;

	/**
	 * Hello world!
	 */

	public static void main(String[] args) {
		try {
			printSystemInformation();
			System.out.println("Creating I2C bus");
			I2CBus bus = I2CFactory.getInstance(I2CBus.BUS_1);
			System.out.println("Creating I2C device");
			//I2CDevice device = bus.getDevice(0x08);
			for(String deviceId : args) {
				try {
					DEVICES.add(bus.getDevice(Integer.parseInt(deviceId)));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			}

			long waitTimeSent = 1000;
			
			/*for (int z = 0; z < 16; z++) {
			try {
				int result[][] = getEmptyMatrix();
				result[z][z] = 1;
				for (int i = 0; i < result.length; i++) {
					for (int j = 0; j < result[i].length; j++) {
						System.out.print(Integer.valueOf(result[i][j]).byteValue());
						device.write(Integer.valueOf(result[i][j]).byteValue());

					}
					System.out.println();
				}
				device.write(Integer.valueOf(2).byteValue());
				Thread.sleep(waitTimeSent);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}*/
			
			try {

				int i = 0, j = 0, width = 0, height = 0, displacement = 0, prefix = DEVICES.size() * MATRIX_WIDTH;
				File image = new File("Text.png");
				System.out.println("File Name : " + image.getAbsolutePath());
				int res[][] = ImageProcessor.getBWImage(ImageIO.read(image));


				while (true) {

					int devicePosition = 1;
					for(I2CDevice device : DEVICES) {

						//Calculate HEIGHT to display
						if(MATRIX_HEIGHT < res.length) {
							height = MATRIX_HEIGHT;
						} else {
							height = res.length;
						}

						//Calculate WIDTH to display
						if((displacement + (MATRIX_WIDTH*devicePosition)) < res[0].length) {
							width = (displacement + (MATRIX_WIDTH*devicePosition));
						} else {
							width = res[0].length;
						}

						//Write to Displays
						for (i = 0; i < height; i++) {
							for (j = displacement + (MATRIX_WIDTH * (devicePosition-1)) ; j < width; j++) {
								if(j<prefix) { //To display preceding empty spaces
									System.out.print(Integer.valueOf(0).byteValue());
									device.write(Integer.valueOf(0).byteValue());
								} else { // To display the actual data
									System.out.print(Integer.valueOf(res[i][j-prefix]).byteValue());
									device.write(Integer.valueOf(res[i][j-prefix]).byteValue());
								}
							}
							if(width == res[0].length) { //To Display the following empty spaces
								for (int x = 0; x < width % MATRIX_WIDTH; x++) { 
									System.out.print(Integer.valueOf(0).byteValue());
									device.write(Integer.valueOf(0).byteValue());
								}
							}
							System.out.println();
						}
						device.write(Integer.valueOf(2).byteValue());
					}
					Thread.sleep(waitTimeSent);
					
					if(prefix > 0){
						prefix--;
					} else {
						displacement++;
					}
					
					if(displacement == (res[0].length * 2) ) { //To restart the loop from right end
						displacement = 0;
					}
				}
			} catch (IIOException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (UnsatisfiedLinkError e) {
				e.printStackTrace();
			} catch (NoClassDefFoundError e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

	private static int[][] getEmptyMatrix() {
		int res[][] = new int[16][16];
		for (int i = 0; i < 16; i++) {
			for (int j = 0; j < 16; j++) {
				res[i][j] = 0;
			}
		}
		return res;
	}

	public static void printSystemInformation() throws IOException, InterruptedException {
		// display a few of the available system information properties
		System.out.println("----------------------------------------------------");
		System.out.println("HARDWARE INFO");
		System.out.println("----------------------------------------------------");
		System.out.println("Serial Number     :  " + SystemInfo.getSerial());
		System.out.println("CPU Revision      :  " + SystemInfo.getCpuRevision());
		System.out.println("CPU Architecture  :  " + SystemInfo.getCpuArchitecture());
		System.out.println("CPU Part          :  " + SystemInfo.getCpuPart());
		// System.out.println("CPU Temperature : " +
		// SystemInfo.getCpuTemperature());
		// System.out.println("CPU Core Voltage : " +
		// SystemInfo.getCpuVoltage());
		// System.out.println("CPU Model Name : " + SystemInfo.getModelName());
		// System.out.println("Processor : " + SystemInfo.getProcessor());
		System.out.println("Hardware Revision :  " + SystemInfo.getRevision());
		System.out.println("Is Hard Float ABI :  " + SystemInfo.isHardFloatAbi());
		// System.out.println("Board Type : " +
		// SystemInfo.getBoardType().name());

		System.out.println("----------------------------------------------------");
		System.out.println("MEMORY INFO");
		System.out.println("----------------------------------------------------");
		System.out.println("Total Memory      :  " + SystemInfo.getMemoryTotal());
		System.out.println("Used Memory       :  " + SystemInfo.getMemoryUsed());
		System.out.println("Free Memory       :  " + SystemInfo.getMemoryFree());
		System.out.println("Shared Memory     :  " + SystemInfo.getMemoryShared());
		System.out.println("Memory Buffers    :  " + SystemInfo.getMemoryBuffers());
		System.out.println("Cached Memory     :  " + SystemInfo.getMemoryCached());
		System.out.println("SDRAM_C Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_C());
		System.out.println("SDRAM_I Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_I());
		System.out.println("SDRAM_P Voltage   :  " + SystemInfo.getMemoryVoltageSDRam_P());

		System.out.println("----------------------------------------------------");
		System.out.println("OPERATING SYSTEM INFO");
		System.out.println("----------------------------------------------------");
		System.out.println("OS Name           :  " + SystemInfo.getOsName());
		System.out.println("OS Version        :  " + SystemInfo.getOsVersion());
		System.out.println("OS Architecture   :  " + SystemInfo.getOsArch());
		System.out.println("OS Firmware Build :  " + SystemInfo.getOsFirmwareBuild());

		System.out.println("----------------------------------------------------");
		System.out.println("JAVA ENVIRONMENT INFO");
		System.out.println("----------------------------------------------------");
		System.out.println("Java Vendor       :  " + SystemInfo.getJavaVendor());
		System.out.println("Java Vendor URL   :  " + SystemInfo.getJavaVendorUrl());
		System.out.println("Java Version      :  " + SystemInfo.getJavaVersion());
		System.out.println("Java VM           :  " + SystemInfo.getJavaVirtualMachine());
		System.out.println("Java Runtime      :  " + SystemInfo.getJavaRuntime());

		System.out.println("----------------------------------------------------");
		System.out.println("NETWORK INFO");
		System.out.println("----------------------------------------------------");

		// display some of the network information
		System.out.println("Hostname          :  " + NetworkInfo.getHostname());
		for (String ipAddress : NetworkInfo.getIPAddresses()) {
			System.out.println("IP Addresses      :  " + ipAddress);
		}
		for (String fqdn : NetworkInfo.getFQDNs()) {
			System.out.println("FQDN              :  " + fqdn);
		}
		for (String nameserver : NetworkInfo.getNameservers()) {
			System.out.println("Nameserver        :  " + nameserver);
		}

		System.out.println("----------------------------------------------------");
		System.out.println("CODEC INFO");
		System.out.println("----------------------------------------------------");
		System.out.println("H264 Codec Enabled:  " + SystemInfo.getCodecH264Enabled());
		System.out.println("MPG2 Codec Enabled:  " + SystemInfo.getCodecMPG2Enabled());
		System.out.println("WVC1 Codec Enabled:  " + SystemInfo.getCodecWVC1Enabled());

		System.out.println("----------------------------------------------------");
		System.out.println("CLOCK INFO");
		System.out.println("----------------------------------------------------");
		System.out.println("ARM Frequency     :  " + SystemInfo.getClockFrequencyArm());
		System.out.println("CORE Frequency    :  " + SystemInfo.getClockFrequencyCore());
		System.out.println("H264 Frequency    :  " + SystemInfo.getClockFrequencyH264());
		System.out.println("ISP Frequency     :  " + SystemInfo.getClockFrequencyISP());
		System.out.println("V3D Frequency     :  " + SystemInfo.getClockFrequencyV3D());
		System.out.println("UART Frequency    :  " + SystemInfo.getClockFrequencyUART());
		System.out.println("PWM Frequency     :  " + SystemInfo.getClockFrequencyPWM());
		System.out.println("EMMC Frequency    :  " + SystemInfo.getClockFrequencyEMMC());
		System.out.println("Pixel Frequency   :  " + SystemInfo.getClockFrequencyPixel());
		System.out.println("VEC Frequency     :  " + SystemInfo.getClockFrequencyVEC());
		System.out.println("HDMI Frequency    :  " + SystemInfo.getClockFrequencyHDMI());
		System.out.println("DPI Frequency     :  " + SystemInfo.getClockFrequencyDPI());
	}
}