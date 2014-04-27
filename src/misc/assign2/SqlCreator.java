package misc.assign2;

public class SqlCreator {

	public static void main(String[] args) {
		// Generate Single rooms
		/*for (int j = 1; j < 6; j++) {		
		
			for(int i = 1; i < 16;i++) {
				System.out.println("INSERT INTO Room VALUES (" + i +"," + j + ",'Single',70.00);");
//				System.out.println("INSERT INTO Room VALUES (DEFAULT," + j + ",'Single',70.00);");
			}
		}*/
		// Generate Twin rooms
		for (int j = 1; j < 6; j++) {				
			for(int i = 16; i < 27;i++) {
				System.out.println("INSERT INTO Room VALUES (" + i +"," + j + ",'Twin',120.00);");
			}
		}
		/*// Generate Queen rooms
		for (int j = 1; j < 6; j++) {				
			for(int i = 27; i < 28;i++) {
				System.out.println("INSERT INTO Room VALUES (" + i +"," + j + ",'Queen',120.00);");
			}
		}*/
		/*// Generate Executive rooms
		for (int j = 1; j < 6; j++) {				
			for(int i = 28; i < 34;i++) {
				System.out.println("INSERT INTO Room VALUES (" + i +"," + j + ",'Executive',180.00);");
			}
		}*/
		/*// Generate Suites
		for (int j = 1; j < 6; j++) {				
			for(int i = 34; i < 37;i++) {
				System.out.println("INSERT INTO Room VALUES (" + i +"," + j + ",'Suite',300.00);");
			}
		}*/
	}

}
