package UI;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.util.Date;
import java.util.Scanner;

import Datei.DateienContainer;
import Datei.DateienContainerInterface;
import Datei.Datei;

public class DateiEinlesenlUI {
	
	public static void DateiAuswahlUIAuswahl() throws IOException {
		System.out.println("\ndir\t\t- listet alle Dateien und Unterverzeichnisse auf");
	    System.out.println("cd <dir>\t- wechselt in das angegebene Verzeichnis <dir>");
	    System.out.println("cd ..\t\t- wechselt in das vorige Verzeichnis");
	    System.out.println("info <name>\t- listet Informationen einer Datei/Ordner auf");
	    System.out.println("save <name>\t- speichert Informationen einer Datei/Ordner ab");
	    System.out.println("back\t\t- wechselt zur�ck in die Hauptansicht");
	    System.out.println("----------------");
	    
	    String input = "";
	    File actPath = new File(".");
	    Scanner sc = new Scanner(System.in);
	    
	    while(true){
	        System.out.print(actPath.getCanonicalPath() + "> ");
	        input = sc.nextLine();
	        input = input.trim();
	        if( input.startsWith("dir") ){                
	            DateiEinlesenlUI.dir(actPath);
	        }
	        else if( input.startsWith("cd ") ){               
	            actPath = DateiEinlesenlUI.cd(input.substring(3,input.length()), actPath);
	        }
	        else if( input.startsWith("back")){
	        	hilfUI.clearScreen();
	        	System.out.println("\nWillkommen zur�ck im Hauptmen�!");
	            System.out.println("upload \t\t- wechselt in die Ansicht, um Dokumente hinzuzuf�gen");
	            System.out.println("view \t\t- wechselt in die Ansicht, um Dokumente anzusehen");
	    	    System.out.println("end\t\t- beendet das Programm");
	    	    System.out.println("----------------");
	            break;
	        }
	        else if( input.startsWith("info") ) {
	        	Path pathGet = Paths.get(Paths.get(actPath.getCanonicalPath()) + "\\" + input.substring(5));
	        	DateiEinlesenlUI.info(pathGet, input.substring(5));
	        }
	        else if( input.startsWith("save") ) {
	        	String name = input.substring(5);
	        	Path pathGet = Paths.get(Paths.get(actPath.getCanonicalPath()) + "\\" + input.substring(5));
	        	DateiEinlesenlUI.save(pathGet, name);
	        }
	        else{
	            System.out.println("Unbekannter Befehl");
	        } 
	    }
		
	}

	
	public static File cd(String neuVZ, File actPath) throws IOException {
        System.out.println("Aufruf von cd mit dem Parameter "+neuVZ);
        if(neuVZ.equals("..")){
            File neu = new File(actPath.getCanonicalFile().getParent());
            return neu;
        }
        File neu = new File(actPath.getCanonicalPath()+"/"+neuVZ);
        if(neu.exists()&&neu.isDirectory()){
            return neu;
        }
        System.out.println("Unbekanntes Verzeichnis");
        return actPath;
    }
	
	public static void dir(File actPath) {
        System.out.println("Aufruf von dir");
        File[] liste = actPath.listFiles();
        int files = 0;
        int dirs = 0;
        DateFormat dateformat = DateFormat.getDateInstance();
        DateFormat timeformat = DateFormat.getTimeInstance();
        for(int i=0; i<liste.length; i++){
            System.out.print(dateformat.format(new Date(liste[i].lastModified()))+"\t");
            System.out.print(timeformat.format(new Date(liste[i].lastModified()))+"\t");
            if(liste[i].isFile()){
                System.out.print( liste[i].length() + "\t\t" );
                System.out.println( liste[i].getName() );
                files++;
            } else{
                System.out.println( "<DIR>\t\t" + liste[i].getName() );
                dirs++;
            }
        }
        String dateiString = (files==1) ? "Datei" : "Dateien";
        String verzString = (dirs==1) ? "Verzeichnis" : "Verzeichnisse";
        System.out.println( dirs + " " + verzString + ", " + files + " " + dateiString); 
    }
	
	public static void info(Path file, String eingabe) {
		System.out.println("Aufruf von info mit dem Parameter " + eingabe);
		try {
			System.out.println("Name der Datei: \t\t" + file.getFileName());

			BasicFileAttributes attr = Files.readAttributes(file, BasicFileAttributes.class);

			System.out.println("Erstellungsdatum: \t\t" + attr.creationTime());
			System.out.println("Letzter Zugriff: \t\t" + attr.lastAccessTime());
			System.out.println("Zuletzt bearbeitet: \t\t" + attr.lastModifiedTime());
			
			System.out.println("Gr��e: \t\t\t\t" + attr.size() + " Bytes");
			System.out.println("Pfad der Datei: \t\t" + file + "\n");
			if (attr.isDirectory() == true) {
				System.out.println("Es handelt sich um ein Ordner!");
			}
			if (attr.isRegularFile() == true) {
				System.out.println("Es handelt sich um eine regul�r lesbare Datei!");
			}
			if (attr.isSymbolicLink() == true) {
				System.out.println("Es handelt sich um einen symbolischen Link!");
			}
			if (attr.isOther() == true) {
				System.out.println("Es handelt sich um etwas anderes als einen Ordner, lesbare Datei, symbolischer Link!");
			}
		}
		catch(IOException e) {
			System.out.println("Datei konnte nicht geladen werden! (Stellen Sie sicher, dass Sie den gesamten Namen inkl. Dateierweiterung eingegeben haben)\nFehlercode: " + e);
		}
	}
	
	public static void save(Path file, String name) {
		System.out.println("Aufruf von save mit dem Parameter " + name);
		DateienContainer.getInstance().hochladeDatei(file, name);
	}

}