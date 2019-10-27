
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class run_test {
	
	private static int size = 9;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("running test");
		
		//initiate the grid
		Grid gd = new Grid(size);
		
		//fill a grid location with color
		gd.fillSingleGridLocation("Yellow", 8, 2);
		gd.fillSingleGridLocation("Blue", 8, 4);
		gd.fillSingleGridLocation("Blue", 9, 5);
		
		//print all grid locations
		System.out.println("locations filled for sinle fills:");
		gd.printAllLocations();
		
		//fill a single row with provided boundaries(y, firstInColumn, lastInColumn, colour)
		gd.fillRowWithBoundaries(3, 2, 7, "Blue");
		gd.fillRowWithBoundaries(4, 5, 6, "Blue");
		
		//print all grid locations
		System.out.println("locations filled for row with boundaries:");
		gd.printAllLocations();
		
		//fill a single column with provided boundaries(x, firstInRow, lastInRow colour)
		gd.fillColumnWithBoundaries(3, 2, 6, "Green");
		
		//print all grid locations
		System.out.println("locations filled for column with boundaries:");
		gd.printAllLocations();
		
		//flood a pixel: with red
		floodPixelWithColor(6,3,"Red",gd);
		
		//flood a pixel: with Orange
		floodPixelWithColor(3,3,"Orange",gd);
		
		//flood a pixel: with Purple
		floodPixelWithColor(5,4,"Purple",gd);
		
		//flood a pixel: with Grey
		floodPixelWithColor(1,1,"Grey",gd);

		
				
				
		

	}
	
	private static void floodPixelWithColor(int x, int y, String newColor, Grid gd){
		String oldColor = gd.getLocationColor(x, y);
		int[] xy = gd.floodPixel(x,y, newColor, oldColor); //(x,y, newColor, oldColor)
		boolean notEnd = true;
		while(notEnd){			
			if(xy!=null)
				xy = gd.floodPixel(xy[0], xy[1], newColor, oldColor);
			else
				notEnd = false;
		}
				
		//print all grid locations
		System.out.println("locations flooded: With "+newColor);
		gd.printAllLocationsWithCertainColor(newColor);
	}
	
	

}


//the grid object
class Grid{
	int x;//Horizontal
	int y;//Vertical
	public int size;//size of x and y
	HashMap<String, String> filledLocations = new HashMap<String, String>(); //(x,y)->"color filled"
	
	boolean northPixelFound = false;//temporary holder for any pixel above found for the current row
	boolean southPixelFound = false;//temporary holder for any pixel below found for the current row
	
	int northPixelFoundX;//temporary holder for any pixel above found for the current row
	int northPixelFoundY;//temporary holder for any pixel above found for the current row
	
	int southPixelFoundX;//temporary holder for any pixel below found for the current row
	int southPixelFoundY;//temporary holder for any pixel below found for the current row

	public Grid(int size){
		this.x = size;
		this.y = size;
		this.size = size;
	}
	
	public void fillSingleGridLocation(String color, int x, int y){
		
		if(x>size||y>size){
			System.out.println("out of bound");
		}else{
			filledLocations.put("("+x+","+y+")", color);
		}		
	}
	
	public void fillRowWithBoundaries(int y, int firstInColumn, int lastInColumn, String color){
		if(firstInColumn>lastInColumn){
			System.out.println("wrong boundaries");
		}else{
			for (int i= firstInColumn; i<=lastInColumn; i++){
				fillSingleGridLocation(color, i, y);
			}
		}
	}
	
	public void fillColumnWithBoundaries(int x, int firstInRow, int lastInRow, String color){
		if(firstInRow>lastInRow){
			System.out.println("wrong boundaries");
		}else{
			for (int i= firstInRow; i<=lastInRow; i++){
				fillSingleGridLocation(color, x, i);
			}
		}
	}
	
	public int[] floodPixel(int x, int y, String newColor, String oldColor) {
		// paint main pixel:
		boolean locationAlreadyFilled = false;
		locationAlreadyFilled = checkIfLocationAlreadyFilled(x,y);
		
		if(locationAlreadyFilled){
			fillSingleGridLocation(newColor, x, y);
		}
			
		else{
			System.out.println("This location is not filled with any color previously");
			return null;
		}
			

		// check if there is a pixel above or below
		checkForNorthPixels(oldColor, x, y);
		checkForSouthPixels(oldColor, x, y);

		// check to the right:
		boolean keepChecking = true;
		int xChecker = x + 1;
		while (keepChecking) {
			String filledLocation = filledLocations.get("(" + xChecker + ","
					+ y + ")");
			if (filledLocation != null && !filledLocation.isEmpty()) {
				if (filledLocation.equals(oldColor)) {
					fillSingleGridLocation(newColor, xChecker, y);
					if (!northPixelFound)
						checkForNorthPixels(oldColor, xChecker, y);
					if (!southPixelFound)
						checkForSouthPixels(oldColor, xChecker, y);
					xChecker++;
				} else {
					keepChecking = false;
				}
			} else {
				keepChecking = false;
			}

		}

		// check to the left:
		keepChecking = true;
		xChecker = x - 1;
		while (keepChecking) {
			String filledLocation = filledLocations.get("(" + xChecker + ","
					+ y + ")");
			if (filledLocation != null && !filledLocation.isEmpty()) {
				if (filledLocation.equals(oldColor)) {
					fillSingleGridLocation(newColor, xChecker, y);
					if (!northPixelFound)
						checkForNorthPixels(oldColor, xChecker, y);
					if (!southPixelFound)
						checkForSouthPixels(oldColor, xChecker, y);
					xChecker--;
				} else {
					keepChecking = false;
				}
			} else {
				keepChecking = false;
			}

		}

		if (northPixelFound) {
			y = northPixelFoundY;
			x = northPixelFoundX;
			northPixelFound = false;
			//floodPixel(x, y, newColor, oldColor);
			int[] xy = {x, y};
			return xy;
		} else if (southPixelFound) {
			y = southPixelFoundY;
			x = southPixelFoundX;
			southPixelFound = false;
			//floodPixel(x, y, newColor, oldColor);
			int[] xy = {x, y};
			return xy;
		}
		return null;

	}

	private boolean checkIfLocationAlreadyFilled(int x, int y) {
		// TODO Auto-generated method stub
		String filledLocation = filledLocations.get("("+x+","+y+")");
		if (filledLocation != null && !filledLocation.isEmpty())
			return true;
		else
			return false;
	}

	private void checkForNorthPixels(String oldColor, int x, int y) {
		// TODO Auto-generated method stub
			int yDash = y-1;
			String filledLocation = filledLocations.get("("+x+","+yDash+")");
			if(filledLocation!=null && !filledLocation.isEmpty()){
				if(filledLocation.equals(oldColor)){
					northPixelFound = true;
					northPixelFoundX = x;
					northPixelFoundY = y-1;
				}
				else
				{
					northPixelFound = false;
				}
			}
			else
			{
				northPixelFound = false;
			}
	}

	private void checkForSouthPixels(String oldColor, int x, int y) {
		// TODO Auto-generated method stub
		if(!southPixelFound){
			int yDash = y+1;
			String filledLocation = filledLocations.get("("+x+","+yDash+")");
			if(filledLocation!=null && !filledLocation.isEmpty()){
				if(filledLocation.equals(oldColor)){
					southPixelFound = true;
					southPixelFoundX = x;
					southPixelFoundY = y+1;
				}
				else
				{
					southPixelFound = false;
				}
			}
			else
			{
				southPixelFound = false;
			}
		}
	}

	public void printAllLocations(){
		if(!filledLocations.isEmpty()){
			Iterator it = filledLocations.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry pair = (Map.Entry)it.next();
				System.out.println("Location: "+pair.getKey()+" is filled with => "+pair.getValue());
				//it.remove();
			}
		}
	}
	
	public void printAllLocationsWithCertainColor(String color){
		if(!filledLocations.isEmpty()){
			Iterator it = filledLocations.entrySet().iterator();
			while(it.hasNext()){
				Map.Entry pair = (Map.Entry)it.next();
				if(pair.getValue().equals(color))
					System.out.println("Location: "+pair.getKey()+" is filled with => "+pair.getValue());
			}
		}
	}
	
	public String getLocationColor(int x, int y){
		return filledLocations.get("("+x+","+y+")");
	}
}
