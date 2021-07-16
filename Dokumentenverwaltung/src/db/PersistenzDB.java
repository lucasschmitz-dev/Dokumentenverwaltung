package db;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import Verarbeitung.ServiceLocator;

public class PersistenzDB implements PersistenzIF, Serializable{
	private static PersistenzDB uniqueInstance = null;
	
	private PersistenzDB() {}
	public static PersistenzDB getInstance() {
		if (uniqueInstance == null) uniqueInstance = new PersistenzDB();
		return uniqueInstance;
	}

	public void speicher(String dateiname, ServiceLocator sl) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(dateiname));
			out.writeObject(sl);
			out.close();
		} catch (IOException e) {
			System.out.println("Fehler beim Speichern aufgetreten!");
			e.printStackTrace();
			System.exit(0);
		}
	}

	public ServiceLocator lade(String dateiname) {
		ServiceLocator sl = null;
		try {
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(dateiname));
			sl = (ServiceLocator) in.readObject();
			in.close();
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("Fehler beim Laden aufgetreten!");
			e.printStackTrace();
			System.exit(0);
		}
		return sl;
	}

}