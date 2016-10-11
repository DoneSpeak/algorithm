package minDistPoints;

import java.util.ArrayList;
import java.util.Comparator;

public class Line {
	public Point startPoint;
	public Point endPoint;
	public double length;
	
	public Line(){
		startPoint = new Point();
		startPoint = new Point();
		length = 0;
	}
	
	public Line(Point startPoint, Point endPoint){
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		length = startPoint.distanceToOtherPoint(endPoint);
	}
	
	public static void showLineArr(ArrayList<Line> lineArr){
		System.out.println("共有" + lineArr.size() + "条线段：");
		for(int i = 0; i < lineArr.size(); i ++){
			Line line = lineArr.get(i);
			System.out.print("第 " + (i + 1) + "条线： (" +line.startPoint.x + "," + line.startPoint.y + "),");
			System.out.print("(" +line.endPoint.x + "," + line.endPoint.y + ")  ");
			System.out.println("length: " + line.length);
		}
	}
}

class LineComparator implements Comparator<Line>{  
    @Override  
    public int compare(Line one, Line two) {
    	if(one.length > two.length){
    		return 1;
    	}else{
    		return -1;
    	}
    }
}