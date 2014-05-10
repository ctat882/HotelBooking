package misc.assign2;

public class SqlCreator {

	public static void main(String[] args) {
		// Generate Single rooms
		for (int j = 1; j < 6; j++) {		
		
			for(int i = 1; i < 16;i++) {
				System.out.println("INSERT INTO Rooms VALUES (" + i +"," + j + ",'Single',70.00,'Available');");
//				System.out.println("INSERT INTO Rooms VALUES (DEFAULT," + j + ",'Single',70.00,'Available');");
			}
		}
		// Generate Twin rooms
		for (int j = 1; j < 6; j++) {				
			for(int i = 16; i < 26;i++) {
				System.out.println("INSERT INTO Rooms VALUES (" + i +"," + j + ",'Twin',120.00,'Available');");
			}
		}
		// Generate Queen rooms
		for (int j = 1; j < 6; j++) {				
			for(int i = 26; i < 36;i++) {
				System.out.println("INSERT INTO Rooms VALUES (" + i +"," + j + ",'Queen',120.00,'Available');");
			}
		}
		// Generate Executive rooms
		for (int j = 1; j < 6; j++) {				
			for(int i = 36; i < 41;i++) {
				System.out.println("INSERT INTO Rooms VALUES (" + i +"," + j + ",'Executive',180.00,'Available');");
			}
		}
		// Generate Suites
		for (int j = 1; j < 6; j++) {				
			for(int i = 41; i < 43;i++) {
				System.out.println("INSERT INTO Rooms VALUES (" + i +"," + j + ",'Suite',300.00,'Available');");
			}
		}
	}

}
