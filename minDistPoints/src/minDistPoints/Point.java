package minDistPoints;

import java.util.HashSet;
import java.util.Random;

public class Point {
	public int x;
	public int y;
	
	public Point(){
		this.x = 0;
		this.y = 0;
	}
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
	public Point(Point point){
		this.x = point.x;
		this.y = point.y;
	}
	
	public void setPointValue(Point point){
		this.x = point.x;
		this.y = point.y;
	}
	
	public boolean isSamePoints(Point point){
		return (x == point.x && y == point.y);
	}
	
	public double distanceToOtherPoint(Point otherPoint){
		double diffX = x - otherPoint.x;
		double diffY = y - otherPoint.y;
		return Math.sqrt(diffX * diffX + diffY * diffY);
	}
	
	public static Point[] createPointsRandomInt(int max, int size){
		Point[] points = new Point[size];
		HashSet<Point> pointSet = new HashSet<Point>();
		Random random = new Random();
		int i = 0;
		while( pointSet.size() < size){
			int randX = random.nextInt(max);
			int randY = random.nextInt(max);
			Point point = new Point(randX,randY);
			if(!pointSet.contains(point)){
				pointSet.add(point);
				points[i ++] = point;
			}
		}
		return points;
	}
	
	//区分不同的Point,由于Set中做不重复处理
	//具有相同的HashCode的对象会才会调用equals方法进行比较，判断是否相同
	@Override
	public boolean equals(Object obj){
		Point point =  (Point)obj;
		if(x == point.x && y == point.y){
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return new Double(x).hashCode();
	}
}
