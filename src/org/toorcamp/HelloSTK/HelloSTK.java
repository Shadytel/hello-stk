package org.toorcamp.HelloSTK;

import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISOException;

import sim.toolkit.EnvelopeHandler;
import sim.toolkit.ProactiveHandler;
import sim.toolkit.ToolkitConstants;
import sim.toolkit.ToolkitException;
import sim.toolkit.ToolkitInterface;
import sim.toolkit.ToolkitRegistry;

public class HelloSTK extends Applet implements ToolkitInterface, ToolkitConstants {
	// DON'T DECLARE USELESS INSTANCE VARIABLES! They get saved to the EEPROM,
	// which has a limited number of write cycles.
	private byte helloMenuItem;
	
	static byte[] welcomeMsg = new byte[] { 'W', 'e', 'l', 'c', 'o', 'm', 'e', ' ',
                                            't', 'o', ' ', 'T', 'o', 'o', 'r', 'C',
                                            'a', 'm', 'p', ' ', '2', '0', '1', '2' };
	
	static byte[] menuItemText = new byte[] { 'H', 'e', 'l', 'l', 'o', ',', ' ', 'S', 'T', 'K'};
	
	private HelloSTK() {
		// This is the interface to the STK applet registry (which is separate
		// from the JavaCard applet registry!)
		ToolkitRegistry reg = ToolkitRegistry.getEntry();
	
		// Define the applet Menu Entry
		helloMenuItem = reg.initMenuEntry(menuItemText, (short)0, (short)menuItemText.length,
				PRO_CMD_SELECT_ITEM, false, (byte)0, (short)0);
	}
	
	// This method is called by the card when the applet is installed. You must
	// instantiate your applet and register it here.
	public static void install(byte[] bArray, short bOffset, byte bLength) {
		HelloSTK applet = new HelloSTK();
		applet.register();
	}
	
	// This processes APDUs sent directly to the applet. For STK applets, this
	// interface isn't really used.
	public void process(APDU arg0) throws ISOException {
		// ignore the applet select command dispached to the process
		if (selectingApplet())
			return;
	}

	// This processes STK events.
	public void processToolkit(byte event) throws ToolkitException {
		EnvelopeHandler envHdlr = EnvelopeHandler.getTheHandler();

		if (event == EVENT_MENU_SELECTION) {
			byte selectedItemId = envHdlr.getItemIdentifier();

			if (selectedItemId == helloMenuItem) {
				showHello();
			}
		}
	}
	
	private void showHello() {
		ProactiveHandler proHdlr = ProactiveHandler.getTheHandler();
		proHdlr.initDisplayText((byte)0, DCS_8_BIT_DATA, welcomeMsg, (short)0, 
				(short)(welcomeMsg.length));
		proHdlr.send();
		return;
	}
}
