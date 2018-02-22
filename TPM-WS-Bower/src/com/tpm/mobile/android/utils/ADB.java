/*
 * Copyright (C) 2009-2013 adakoda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tpm.mobile.android.utils;
import java.io.File;

import org.apache.log4j.Logger;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;

public class ADB {
	private AndroidDebugBridge mAndroidDebugBridge;
	private static final Logger log = Logger.getLogger(ADB.class);
	public String adbLocation;
	public boolean initialize(String[] args) {
		boolean success = true;

		//String adbLocation = System.getProperty("com.android.screenshot.bindir");

		// You can specify android sdk directory using first argument
		// A) If you lunch jar from eclipse, set arguments in Run/Debug configurations to android sdk directory .
		//    /Applications/adt-bundle-mac-x86_64/sdk
		// A) If you lunch jar from terminal, set arguments to android sdk directory or $ANDROID_HOME environment variable.
		//    java -jar ./jar/asm.jar $ANDROID_HOME
		if (adbLocation == null) {
			if ((args != null) && (args.length > 0)) {
				adbLocation = args[0];
			} else {
				adbLocation = System.getenv("ANDROID_HOME");
			}
			// Here, adbLocation may be android sdk directory
			if (adbLocation != null) {
				adbLocation += File.separator + "platform-tools";
			}
		}

		// for debugging (follwing line is a example)
//		adbLocation = "C:\\ ... \\android-sdk-windows\\platform-tools"; // Windows
//		adbLocation = "/ ... /adt-bundle-mac-x86_64/sdk/platform-tools"; // MacOS X
		
		if (success) {
			if ((adbLocation != null) && (adbLocation.length() != 0)) {
				adbLocation += File.separator + "adb";
			} else {
				adbLocation = "adb";
			}
			log.info("adb path is " + adbLocation);
			// Get a device bridge instance. Initialize, create and restart.
			try {
				AndroidDebugBridge.init(false);
			} catch (IllegalStateException ise) {
				log.error(ise);
				log.error("The IllegalStateException is not a show " +
				"stopper. It has been handled. This is just debug spew." +
				" Please proceed.");
			}
			
			mAndroidDebugBridge = AndroidDebugBridge.getBridge();
			if (mAndroidDebugBridge == null) {
				mAndroidDebugBridge = AndroidDebugBridge.createBridge(adbLocation,true);
			}
				
			
			if (mAndroidDebugBridge == null) {
				success = false;
			}
			
			
		}

		if (success) {
			int count = 0;
			while (mAndroidDebugBridge.isConnected() && mAndroidDebugBridge.hasInitialDeviceList() == false) {
				try {
					Thread.sleep(100);
					count++;
				} catch (InterruptedException e) {
				}
				if (count > 100) {
					success = false;
					break;
				}
			}
		}

		if (!success) {
			try {
				terminate();
			} catch (InterruptedException e) {
			}
		}
	
		return success;
	}

	public void terminate() throws InterruptedException {
		AndroidDebugBridge.terminate();
		log.info("adb terminated");
	}

	public IDevice[] getDevices() {
		IDevice[] devices = null;
		if (mAndroidDebugBridge != null) {
			devices = mAndroidDebugBridge.getDevices();
		}
		return devices;
	}
}
