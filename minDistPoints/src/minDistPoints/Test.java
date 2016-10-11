package minDistPoints;

import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

public class Test {
	public static void testHashSet(){
		HashSet<Point> pointSet =  new HashSet<Point>();
		pointSet.add(new Point(1,1));
		pointSet.add(new Point(1,1));
		System.out.println(pointSet.size());
	}
	public static void hasSamePointsRate(int pointNum,int allPointNum){
		double rate = 1;
		System.out.println("allPointNum: " + allPointNum);
		int numbers = allPointNum;
		int i;
		for(i = 1; i <= 100; i ++){
			rate *= numbers;
//			rate /= i;
			rate /= allPointNum;
			numbers --;
			System.out.println("rate: " + rate);
		}
		Scanner reader = new Scanner(System.in);
		reader.nextLine();
		for(; i <= pointNum; i ++){
			rate *= numbers;
//			rate /= i;
			rate /= allPointNum;
			numbers --;
		}

		rate = 1 - rate;
		System.out.println("rate: " + rate);
	}
	
	public static boolean hasSamePoint(int max, int size){
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
			}else{
				return true;
			}
		}
		return false;
	}
	
	public static void testSamePointRate(int time,int size){
		int sameNum = 0;
		int notSame = 0;
		for(int i = 0; i < time; i ++){
			if(Test.hasSamePoint(size, size)){
				sameNum ++;
//				System.out.println("第" + sameNum + "个，重复");
			}else{
				notSame ++;
//				System.out.println("第" + notSame + "个，不重复");
			}
		}
		System.out.println("notSame: " + notSame);
		System.out.println("sameNum: " + sameNum);
		System.out.println("rate: " + (sameNum / 100000.0));
	}
}
