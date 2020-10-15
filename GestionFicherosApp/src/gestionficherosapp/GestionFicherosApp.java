package gestionficherosapp;

import gestionficheros.MainGUI;

public class GestionFicherosApp {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		GestionFicherosImpl getFicherosImpl = new GestionFicherosImpl();
		new MainGUI(getFicherosImpl).setVisible(true);
		
	}

}
