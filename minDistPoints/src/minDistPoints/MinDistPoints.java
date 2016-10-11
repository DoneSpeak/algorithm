/*
 * 说明：
 * 前提:没有重叠的点
 */
package minDistPoints;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JApplet;
import javax.swing.JFrame;

public class MinDistPoints {
	/*
	 * 常量
	 */
	public final int ERRORMODE = -1;
	public final int POINTMODEX = 0;
	public final int POINTMODEY = 1;
	
	public static final int VIOLENCE = 1;
	public static final int DIVIDE = 2;
	
	public static final int MIDDLERL = 1;
	public static final int MIDDLEALL = 2;
	public static final int MIDDLEVIOLENCE = 3;
	
	public static final int VIOLENCEOPTION = 1;
	public static final int DIVIDEOPTIOIN = 2;
	public static final int EXPERIMENTOPTION = 3;
	public static final int TEXTDIVIDE = 4;
	public static final int EXITOPTION = 5;
	

	public static final int NANOTIMEDIVIDER = 10000;
	
	public static boolean debug = false;
	public static boolean showPoints = false;
	public static boolean showEachTime = true;
	public static int testType;

	public int typeGetMiddleMinDist = MIDDLERL;
	
	public static void main(String args[]){
		showTime();
	}

/*********************蛮力法求解*****************************/
	/*
	 * 蛮力法求解
	 * 求出每一个点到其他的点的距离，从而求出最小的距离与相应的两个点
	 * 由组合关系可以得出，共有N(N-1)/2对点对
	 * 也就是需要循环N(N-1)/2次
	 * 时间复杂度为O(N^2)
	 */
	public double getNearestPointsDerectly(Point[] points,Point startPoint,Point endPoint,int size){
		double MinDistance = Double.MAX_VALUE;
		for (int startPointIndex = 0; startPointIndex < size-1; startPointIndex ++)
		{
		     for (int endPointIndex = startPointIndex + 1; endPointIndex < size; endPointIndex ++)  
		     {
		    	 double dist = DistanceOf2Points(points[startPointIndex],points[endPointIndex]);

		         if(dist < MinDistance)
		         {
		        	 MinDistance = dist;
		        	 startPoint.setPointValue(points[startPointIndex]);
		        	 endPoint.setPointValue(points[endPointIndex]);
		         }
		     }
		}
		return MinDistance;
	}
	
	
/********************分治法求解***********************/
	public double getNearestPointsByDivide(Point[] points, Point startPoint, Point endPoint, int size){
		//预处理：分别根据输入点集poins中的x坐标和y坐标进行排序，得到pointSetX和pointSetY两个点集
		Point[] pointSetX = createPointArray(size);
		Point[] pointSetY = createPointArray(size);
		if(showPoints){
			showPoins("产生的点集",points,size,100);
		}
		pointSetX = sort(points,size,POINTMODEX);
		pointSetY = sort(points,size,POINTMODEY);
		if(showPoints){
			showPoins("点集按照  x 坐标排序",pointSetX,size,100);
			showPoins("点集按照  y 坐标排序",pointSetY,size,100);
		}
		
		return getNearestPointsByDivide(pointSetX,pointSetY,startPoint,endPoint,size);
	}
	
	public double getNearestPointsByDivide(Point[] pointSetX,Point[] pointSetY,Point startPoint,Point endPoint,int size) {
		
		//点数为1时
		if(size == 1){
			startPoint.setPointValue(pointSetX[0]);
			endPoint.setPointValue(pointSetX[0]);
			return Double.MAX_VALUE;
		}else if(size == 2){
			startPoint.setPointValue(pointSetX[0]);
			endPoint.setPointValue(pointSetX[1]);
			return DistanceOf2Points(pointSetX[0],pointSetX[1]);
		}else if(size == 3){
			double dist1 = DistanceOf2Points(pointSetX[0],pointSetX[1]);
			double dist2 = DistanceOf2Points(pointSetX[0],pointSetX[2]);
			double dist3 = DistanceOf2Points(pointSetX[1],pointSetX[2]);
			
			dist1 = dist1 < dist2 ? dist1 : dist2;
			dist1 = dist1 < dist3 ? dist1 : dist3;
			if(dist1 == dist2){
				startPoint.setPointValue(pointSetX[0]);
				endPoint.setPointValue(pointSetX[2]);
			}else if(dist1 == dist3){
				startPoint.setPointValue(pointSetX[1]);
				endPoint.setPointValue(pointSetX[2]);			
			}else{
				startPoint.setPointValue(pointSetX[0]);
				endPoint.setPointValue(pointSetX[1]);				
			}
			return dist1;
		}
		//点数>3时，将平面点集分割成为大小大致相等的两个子集pointSetXL和pointSetXR，选取一个垂直线middleLine作为分割直线
		//垂直线可以用下标的中，从而可以保障左右两边的点数都是size/2,防止出现，左边为1，右边为size-1这种不利于算法的情况
		return hasMoreThanThreePoint(pointSetX,pointSetY,startPoint,endPoint,size);
	}
	
	/*
	  求中间区域的最小距离
	 */
	//[start] 获取中间区域点集的最小距离，区分左右集合法
	public double hasMoreThanThreePoint(Point[] pointSetX,Point[] pointSetY,Point startPoint,Point endPoint,int size) {
		int middle = size / 2;
		int leftSize = middle;
		int rightSize = size - middle;
		int middleX = pointSetX[middle].x;
		int middleY = pointSetX[middle].y;
		//pointSetX拆分两个X点集
		Point[] pointSetXL = createPointArray(leftSize);
		Point[] pointSetXR = createPointArray(rightSize);
		for(int i = 0; i < middle; i ++){
			pointSetXL[i].setPointValue(pointSetX[i]);
		}
		//middle点再右侧点集
		for(int i = 0; i < rightSize; i ++){
			pointSetXR[i].setPointValue(pointSetX[i + middle]);
		}
		//pointSetY拆分到两个Y点集，使用归并排序MERGE的相反过程
		Point[] pointSetYL = createPointArray(leftSize);
		Point[] pointSetYR = createPointArray(rightSize);
		int leftIndex = 0;
		int rightIndex = 0;
		try{
			for(int i = 0; i < size; i ++){
				//如果有重叠的点，就会发生越界错误
				if(pointSetY[i].x < middleX){
					pointSetYL[leftIndex ++].setPointValue(pointSetY[i]);
				}else if(pointSetY[i].x == middleX){

					if(pointSetY[i].y < middleY){
						//小y放左边
						pointSetYL[leftIndex ++].setPointValue(pointSetY[i]);
					}else{
						pointSetYR[rightIndex ++].setPointValue(pointSetY[i]);
					}
				}else{
					pointSetYR[rightIndex ++].setPointValue(pointSetY[i]);
				}
			}
		}catch(ArrayIndexOutOfBoundsException e){
			System.err.println("leftIndex = " + leftIndex);
			System.err.println("rightIndex = " + rightIndex);
			System.err.println("leftSize = " + leftSize);
			System.err.println("rightSize = " + rightSize);
			this.showPoins("左边点集", pointSetYL, leftIndex,100);
			this.showPoins("右边点集", pointSetYR, rightIndex,100);
			e.printStackTrace();
			Scanner reader = new Scanner(System.in);
			reader.nextLine();
		}
		//两个递归调用，分别求出pointSetL和pointSetR中的最短距离为minDistL和minDistR
		//getNearestPointsBy(Point[] pointSetX,Point[] pointSetY,Point startPoint,Point endPoint,int size)
		Point startPointR = new Point();
		Point endPointR = new Point();
		
		double minDist = getNearestPointsByDivide(pointSetXL,pointSetYL,startPoint,endPoint,leftSize);
		double minDistR = getNearestPointsByDivide(pointSetXR,pointSetYR,startPointR,endPointR,rightSize);
		//取minDist=min(minDistL, minDistR)，确定左右两个点集的最小距离
		if(minDist > minDistR){
			minDist = minDistR;
			startPoint.setPointValue(startPointR);
			endPoint.setPointValue(endPointR);
		}
				
		//在直线middleLine两边分别扩展minDist，得到边界区域middleSet，middleSet本身按照x坐标排序
		//middleSetY是区域middleSet中的点按照y坐标值排序后得到的点集，middleSetY又可分为左右两个集合middleSetYL和middleSetYR
		if(this.typeGetMiddleMinDist == MinDistPoints.MIDDLERL){
			minDist = getMinDistInMiddleSetLR(pointSetYL,pointSetYR,startPoint,endPoint,size,middleX,minDist);
		}else if(this.typeGetMiddleMinDist == MinDistPoints.MIDDLEALL){
			minDist = getMinDistInMiddleSet(pointSetY,startPoint,endPoint,size,middleX,minDist);
		}else{
			minDist = getMinDistInMiddleSetViolence(pointSetY,startPoint,endPoint,size,middleX,minDist);
		}
		return minDist;
	}
	//[end]
	
	//求出中间带的最近点
	public double getMinDistInMiddleSetLR(Point[] pointSetYL,Point[] pointSetYR,Point startPoint,Point endPoint,
			int size,int middleX,double minDist) {
		//在直线middleLine两边分别扩展minDist，得到边界区域middleSet，middleSet本身按照x坐标排序
		//middleSetY是区域middleSet中的点按照y坐标值排序后得到的点集，middleSetY又可分为左右两个集合middleSetYL和middleSetYR
		int leftSize = pointSetYL.length;
		int rightSize = pointSetYR.length;
		Point[] middleSetYL = createPointArray(leftSize);
		Point[] middleSetYR = createPointArray(rightSize);
		int leftIndex = 0;
		for(int i = 0; i < leftSize; i ++){
			// |---+-*--|
			if(middleX - pointSetYL[i].x <= minDist){
				middleSetYL[leftIndex ++].setPointValue(pointSetYL[i]);
			}
		}
		int middleSetYLeftLen = leftIndex;
		
		int rightIndex = 0;
		try{
			for(int i = 0; i < rightSize; i ++){
				// |---+-*--|
				if(pointSetYR[i].x - middleX <= minDist){
					middleSetYR[rightIndex ++].setPointValue(pointSetYR[i]);
				}
			}
		}catch(Exception e){
			System.err.println("leftIndex: " + leftIndex);
			System.err.println("rightIndex: " + rightIndex);
			System.err.println("leftSize: " + leftSize);
			System.err.println("rightSize: " + rightSize);
			e.printStackTrace();
			Scanner reader = new Scanner(System.in);
			reader.nextLine();
		}
		int middleSetYRightLen = rightIndex;
		//[DEBUG]
		if(debug){
			this.showPoins("左边点集", middleSetYL, middleSetYLeftLen,100);
			this.showPoins("右边点集", middleSetYR, middleSetYRightLen,100);
		}
		
		//对于middleSetYL中的每一点，检查middleSetYR中的点与它的距离，更新所获得的最近距离
		int nearestIndex = 0;
		for(leftIndex = 0; leftIndex < middleSetYLeftLen; leftIndex ++){
			//找到右侧距离左侧每个点的最近六点
			try{
				int diffLeftToRightY = Integer.MAX_VALUE;
				while(nearestIndex < middleSetYRightLen && diffLeftToRightY > minDist){
					diffLeftToRightY = middleSetYL[leftIndex].y - middleSetYR[nearestIndex].y;
					nearestIndex ++;
				}
				//结束上面的循环时有两种可能：
				//1：找到矩形内的点；
				//2：右侧出现第一个比左侧点middleSetYR[nearestIndex]的y坐标大minDist的点，此时diffLeftToRightY < -minDist,还未找到矩形内的点
				if(diffLeftToRightY < -minDist){
					//可能性2
					/*
					 *    | +
					 *    |    +
					 *    |  + 
					 *  + | 
					 */
					//回到上一个点
					nearestIndex --;
					nearestIndex = nearestIndex > 0 ? nearestIndex : 0;
					continue;
				}
			}catch(Exception e){
				System.err.println("leftIndex: " + leftIndex);
				System.err.println("rightIndex: " + rightIndex);
				System.err.println("middleSetYLeftLen: " + middleSetYLeftLen);
				System.err.println("middleSetYRightLen: " + middleSetYRightLen);
				System.err.println("nearestIndex: " + nearestIndex);
				e.printStackTrace();
				Scanner reader = new Scanner(System.in);
				reader.nextLine();
			}
			int sixBegin = nearestIndex - 2 > 0 ? nearestIndex - 3 : 0;
			int sixEnd = nearestIndex + 3 < middleSetYRightLen - 1 ? nearestIndex + 3 : middleSetYRightLen - 1;

			for(rightIndex = sixBegin; rightIndex <= sixEnd; rightIndex ++){
				double dist = DistanceOf2Points(middleSetYL[leftIndex],middleSetYR[rightIndex]);
				if(dist < minDist){
					minDist = dist;
					startPoint.setPointValue(middleSetYL[leftIndex]);
					endPoint.setPointValue(middleSetYR[rightIndex]);
				}
			}
			//回到上一个点
			nearestIndex --;
			nearestIndex = nearestIndex > 0 ? nearestIndex : 0;
		}
		return minDist;
	}
	/*
	 * 将中间区域作为一个点集进行处理
	 */
	//[start] 获取中间区域点集的最小距离
	public double getMinDistInMiddleSet(Point[] pointSetY,Point startPoint,Point endPoint,
			int numPointsInMiddleSet,int middleX,double minDist) {
		Point[] middleSet = createPointArray(numPointsInMiddleSet);
		//获得中间带中的点
		int middleSetSize = 0;
		for(int i = 0; i < numPointsInMiddleSet; i ++){
			if(Math.abs(pointSetY[i].x - middleX) <= minDist){
				middleSet[middleSetSize ++].setPointValue(pointSetY[i]);
			}
		}
		for(int startPointIndex = 0; startPointIndex < middleSetSize; startPointIndex ++){
			for(int endPointIndex = startPointIndex + 1; endPointIndex < middleSetSize; endPointIndex ++ ){
				if(middleSet[endPointIndex].y - middleSet[startPointIndex].y > minDist){
					break;
				}else{
					double dist = middleSet[startPointIndex].distanceToOtherPoint(middleSet[endPointIndex]);
					if(dist < minDist){
						minDist = dist;
						startPoint.setPointValue(middleSet[startPointIndex]);
						endPoint.setPointValue(middleSet[endPointIndex]);
					}
				}
			}
		}
		return minDist;
	}
	
	public double getMinDistInMiddleSetViolence(Point[] pointSetY,Point startPoint,Point endPoint,
			int numPointsInMiddleSet,int middleX,double minDist) {
		Point[] middleSet = createPointArray(numPointsInMiddleSet);
		//获得中间带中的点
		int middleSetSize = 0;
		for(int i = 0; i < numPointsInMiddleSet; i ++){
			if(Math.abs(pointSetY[i].x - middleX) <= minDist){
				middleSet[middleSetSize ++].setPointValue(pointSetY[i]);
			}
		}
		for(int startPointIndex = 0; startPointIndex < middleSetSize; startPointIndex ++){
			for(int endPointIndex = startPointIndex + 1; endPointIndex < middleSetSize; endPointIndex ++ ){
				double dist = middleSet[startPointIndex].distanceToOtherPoint(middleSet[endPointIndex]);
				if(dist < minDist){
					minDist = dist;
					startPoint.setPointValue(middleSet[startPointIndex]);
					endPoint.setPointValue(middleSet[endPointIndex]);
				}
			}
		}
		return minDist;
	}
/********************归并排序*************************/
	
	public Point[] sort(Point[] points, int size,int mode){
		Point[] newPoints = clonePointArray(points);
		mergeSort(newPoints,size,mode);
		return newPoints;
	}
    //归并 -- 合并两个有序的数组
    public void mergeSort(Point[] point, int size,int mode)
    {
    	Point[] store = createPointArray(size);
        mergeSort(point, 0, size - 1, size, store,mode);
    }

    public void mergeSort(Point[] point, int first, int last, int size,Point[] store,int mode)
    {
        if (first < last)
        {
            int mid = (first + last) / 2;
            //左半边排序
            mergeSort(point, first, mid, size,store,mode);
            //右半边排序
            mergeSort(point, mid + 1, last, size, store,mode);
            //合并两个有序数组
            mergeSort(point, first, mid, last, size, store,mode);
        }
    }

    //此函数可以设想为数组两边已经是有序，只要将两边不断选择小的合并就可以了
    public void mergeSort(Point[] point, int first, int mid, int last, int size,Point[] store,int mode)
    {
        int left = first;
        int right = mid + 1;
        int storeIndex = 0;

        while (left <= mid && right <= last)
        {
        	if (compareTwoPointXorY(point[left],point[right],mode))
            {
                store[storeIndex].setPointValue(point[left]);
                storeIndex++;
                left++;
            }
            else
            {
                store[storeIndex].setPointValue(point[right]);
                storeIndex++;
                right++;
            }
        }

        //归并剩余的
        while (left <= mid)
        {
            store[storeIndex].setPointValue(point[left]);
            storeIndex++;
            left++;
        }
        while (right <= last)
        {
            store[storeIndex].setPointValue(point[right]);
            storeIndex++;
            right ++;
        }

        //还原到原来数组
        for (left = 0; left < storeIndex; left++)
        {
        	point[first + left].setPointValue(store[left]);
        }
    }
    
    /*
     * 归并排序规则定义
     * X: 相同x的按照y排序（从小到大）
     * Y: 相同y的按照x排序（从小到大）
     */
    public boolean compareTwoPointXorY(Point leftPoint,Point rightPoint,int mode){
    	switch(mode){
        	case POINTMODEX:{
        		if(leftPoint.x == rightPoint.x){
        			return leftPoint.y < rightPoint.y;
        		}
        		return leftPoint.x < rightPoint.x;
        	}
        	case POINTMODEY:{
        		if(leftPoint.y == rightPoint.y){
        			return leftPoint.x < rightPoint.x;
        		}
        		return leftPoint.y < rightPoint.y;
        	}
        	default:{
        		return false;
        	}
    	}
    }    
/********************其他函数***********************/

    public static void showTime(){
    	
    	while(true){
	    	System.out.println("请选择您想要的操作：");
	    	System.out.println("1、展示蛮力法    2、展示分治法    3、获取运行时间    4、测试分治法    5、退出");
	    	int option;
	    	Scanner input = new Scanner(System.in);
	    	option = input.nextInt();
	    	switch(option){
		    	case MinDistPoints.VIOLENCEOPTION:{
		    		int size = 0;
		    		while(size < 2){
			    		System.out.println("请输入规模(>1)：");
			    		size = input.nextInt();
		    		}
		    		showMeHow(VIOLENCE,size);
		    		break;
		    	}
		    	case MinDistPoints.DIVIDEOPTIOIN:{
		    		int size = 0;
		    		while(size < 2){
			    		System.out.println("请输入规模(>1)：");
			    		size = input.nextInt();
		    		}
		    		showMeHow(DIVIDE,size);
		    		break;
		    	}case MinDistPoints.EXPERIMENTOPTION:{
		    		experimentForHomework();
		    		break;
		    	}case MinDistPoints.TEXTDIVIDE:{
		    		int testType = 4;
		    		int testTime = 10000;
		    		int scale = 1000;
		    		while(testType > 3){
			    		System.out.println("请选择测试模式：");
			    		System.out.println("1：求中间带时，区分左右区间，找右侧六点求最近点");
			    		System.out.println("2：求中间带时，不区分左右区间看成整体求最近点");
			    		System.out.println("3：求中间带时，使用蛮力法，无优化");
			    		System.out.println("注：测试规模为[" + scale + "],测试次数为[" + testTime + "]");
			    		testType = input.nextInt();
		    		}
		    		long startTime = System.currentTimeMillis();
		    		TestDivide(testTime,scale);
		    		long endTime = System.currentTimeMillis();
		    		long usedTime = endTime - startTime;
		    		System.out.println("===================【测试使用时间】=================");
		    		System.out.println(usedTime + " ms");
		    		break;
		    	}case MinDistPoints.EXITOPTION:{
		        	System.out.println("感谢您的使用    &(@-@)&");
		    		return;
		    	}default:{
		    		System.out.printf("请输入 1、2 或者 3\n\n");
		    		continue;
		    	}
	    	}
	    	System.out.println("===========================================================");
    	}
    }
    
    public static double showMeHow(int type,int size){
    	
		System.out.printf("-------------------[");
		if(type == MinDistPoints.VIOLENCE){
			System.out.printf("@蛮力法@");
		}else{
			System.out.printf("M分治法M");
		}
		System.out.println("]-------------------");
//		minDistPoints.showPoins("随机生成的点集",points, size,100);
		if(size <= 10){
			showTenPointDetail(type);
		}
		//[TODO]
		Point[] points = Point.createPointsRandomInt(size*100,size);
		
		return showMeHow(points,type,size);
    }
    
    public static double showMeHow(Point[] points, int type, int size){
		MinDistPoints minDistPoints = new MinDistPoints();
		//设置测试类型，默认为区分左右求最近点
		minDistPoints.typeGetMiddleMinDist = testType;
    	Point startPoint = new Point();
		Point endPoint = new Point();
		double minDist = -1;
		
		long startTime=System.nanoTime();
		if(type == MinDistPoints.VIOLENCE){
			minDist = minDistPoints.getNearestPointsDerectly(points,startPoint,endPoint,size);
		}else if(type == MinDistPoints.DIVIDE){
			minDist = minDistPoints.getNearestPointsByDivide(points,startPoint,endPoint,size);
		}
		long endTime=System.nanoTime();
        long timeUsed = (endTime - startTime) / NANOTIMEDIVIDER;
        System.out.println();
        System.out.println("--- [数据规模：" + size + "] ---");
        minDistPoints.showResult(minDist,startPoint,endPoint);
		System.out.println("用时：" + timeUsed + "e" + (int)Math.log10(NANOTIMEDIVIDER) + " ns");
		System.out.println();
		return minDist;
    }
    
    public static void showTenPointDetail(int type){
    	final int SIZE = 10;
    	MinDistPoints minDistPoints = new MinDistPoints();
    	
		Point[] points = minDistPoints.createPointsRandomInt(SIZE * 100,10);
		minDistPoints.showPoins("随机生成点集", points, SIZE, 2);
		System.out.println();
		Point startPoint = new Point();
		Point endPoint = new Point();
		double minDist = -1;
		//十个点的详细情况
		Line line = new Line();
		ArrayList<Line> lineArr =  new ArrayList<Line>();
		for (int startPointIndex = 0; startPointIndex < SIZE-1; startPointIndex ++)
		{
		     for (int endPointIndex = startPointIndex + 1; endPointIndex < SIZE; endPointIndex ++)  
		     {
		    	 line = new Line(points[startPointIndex],points[endPointIndex]);
		    	 lineArr.add(line);
		     }
		}
		lineArr.sort(new LineComparator());
		Line.showLineArr(lineArr);
		System.out.println();
		
		//最短距离
    	long startTime=System.nanoTime();
		if(type == MinDistPoints.VIOLENCE){
			minDist = minDistPoints.getNearestPointsDerectly(points,startPoint,endPoint,SIZE);
		}else if(type == MinDistPoints.DIVIDE){
			minDist = minDistPoints.getNearestPointsByDivide(points,startPoint,endPoint,SIZE);
		}
		long endTime=System.nanoTime();
        long timeUsed = (endTime - startTime)/NANOTIMEDIVIDER;

        minDistPoints.showResult(minDist,startPoint,endPoint);
		System.out.println("用时：" + timeUsed + "e" + (int)Math.log10(NANOTIMEDIVIDER) + " ns");
    }
    
    public static void TestDivide(int time, int size){
    	for(int i = 0; i < time; i ++){
    		Point[] points = Point.createPointsRandomInt(size*100,size);
    		System.out.println("------------第[" + (i+1) + "]次-----------");
    		System.out.println("--[@蛮力法@]--");
    		double minDist = showMeHow(points,MinDistPoints.VIOLENCE,1000);
    		System.out.println("--[M分治法M]--");
    		double minDistDivide =  showMeHow(points,MinDistPoints.DIVIDE,1000);

			System.out.println("--[&测试结果&]--");
    		if(minDist != minDistDivide){
    			System.err.println("有误");
    			Scanner reader = new Scanner(System.in);
    			reader.nextLine();
    		}else{
				System.out.printf("正确\n\n");
    		}
    	}
    }
    
    /*
     * 每种方法计算20次，取平均值
     * 数据规模分别为：N=100,1 000,10 000,100 000，1 000 000,10 000 000
     * 计算顺序：先计算蛮力法，再计算归并法
     */
    public static void experimentForHomework(){
    	
		final int TIME = 1;
		
		ArrayList<Long> averTimeV = new ArrayList<Long>();
		ArrayList<Long> averTimeM = new ArrayList<Long>();
		
		ArrayList<Integer> scales = createScaleArray();
		
		for(int scaleIndex = 0; scaleIndex < scales.size(); scaleIndex ++){
			long sumUsedTimeOfViolence = 0;
			long sumUsedTimeOfDivide = 0;
			int size = scales.get(scaleIndex);
			System.out.println("-----------[ 数据规模为：" + size + " ]-------------");
			
			for(int i = 0; i < TIME; i ++){
				MinDistPoints minDistPoints = new MinDistPoints();
				Point[] points = minDistPoints.createPointsRandomInt(size*100,size);
				if(showPoints){
					minDistPoints.showPoins("随机生成的点集",points, size,100);
				}
				Point startPoint = new Point();
				Point endPoint = new Point();
				double minDist = -1;
				//蛮力法求最近点对
				System.out.println("-@蛮力法求最近点对@-");
				long startTime = System.nanoTime();
				minDist = minDistPoints.getNearestPointsDerectly(points,startPoint,endPoint,size);
				long endTime = System.nanoTime();
				
				long usedTime = (endTime - startTime)/NANOTIMEDIVIDER; 
				sumUsedTimeOfViolence += usedTime;
				if(showEachTime){
					System.out.println("用时：" + usedTime + "e" + (int)Math.log10(NANOTIMEDIVIDER) + " ns");
					minDistPoints.showResult(minDist,startPoint,endPoint);
					System.out.println();
				}
				
				//分治法求最近点对
				System.out.println("-M分治法求最近点对M-");
				Point startPoint2 = new Point();
				Point endPoint2 = new Point();
				double minDist2 = -1;
				
				startTime = System.nanoTime();
				minDist2 = minDistPoints.getNearestPointsByDivide(points,startPoint2,endPoint2,size);
				endTime = System.nanoTime();
				
				usedTime = (endTime - startTime)/NANOTIMEDIVIDER;
				sumUsedTimeOfDivide += usedTime;
				if(showEachTime){
					System.out.println("用时：" + usedTime + "e" + (int)Math.log10(NANOTIMEDIVIDER) + " ns");			
					minDistPoints.showResult(minDist2,startPoint2,endPoint2);
					System.out.println("---------------[第  " + i + " 次完成 ]----------------");
				}
				if(minDist != minDist2){
					System.err.println("蛮力法与分治法求得的最小距离不同，算法有误！");
					Scanner reader = new Scanner(System.in);
					reader.nextLine();
				}else{
					
				}
			}
			long averTimeViolence = sumUsedTimeOfViolence / TIME;
			averTimeV.add(averTimeViolence);
			long averTimeDivide = sumUsedTimeOfDivide / TIME;
			averTimeM.add(averTimeDivide);
			System.out.println(">>>---\\平均耗时/--->");
			System.out.println("蛮力法：" + averTimeViolence + "e" + (int)Math.log10(NANOTIMEDIVIDER) + " ns");
			System.out.println("分治法：" + averTimeDivide + "e" + (int)Math.log10(NANOTIMEDIVIDER) + " ns");
			System.out.println();
		}
		System.out.print("\n\n>>>>>>>>> 最终结果 <<<<<<<<<\n");
		showUsedTimeResult("蛮力法","分治法",averTimeV,averTimeM,scales);
		int scale1000Index = 2;
		showUsedTimeTheory("蛮力法","分治法",averTimeV,averTimeM,scales,scale1000Index);
    }
    
    public static void showUsedTimeResult(String typeOneName,String typeTwoName,
    		ArrayList<Long> typeOneTimeArr,ArrayList<Long> typeTwoTimeArr,ArrayList<Integer> scales){
    	
    	System.out.println("-----------------------------------------[实际值]-----------------------------------");

    	System.out.print("规模大小/个" + "\t");
    	for(int i = 0; i < scales.size(); i ++){
    		System.out.print(scales.get(i) + "\t");
    	}
    	System.out.println();
    	
    	System.out.print(typeOneName + "/ms\t");
    	for(int i = 0; i < typeOneTimeArr.size(); i ++){
    		System.out.print(typeOneTimeArr.get(i) + "\t");
    	}
    	System.out.println();
    	
    	System.out.print(typeTwoName + "/ms\t");
    	for(int i = 0; i < typeTwoTimeArr.size(); i ++){
    		System.out.print(typeTwoTimeArr.get(i) + "\t");
    	}
    	System.out.printf("\n-------------------------------------------------------------------\n");
    }

    public static void showUsedTimeTheory(String typeViolenceName,String typeDivideName,
    		ArrayList<Long> typeViolenceTimeArr,ArrayList<Long> typeDivideTimeArr,ArrayList<Integer> scales, int baseIndex){

    	System.out.println("----------------------------------[理论值]----------------------------------");

    	System.out.print("规模大小/个" + "\t");
		
    	for(int i = 0; i < scales.size(); i ++){
    		System.out.print(scales.get(i) + "\t");
    	}
    	System.out.println();
    	
    	int baseSize = scales.get(baseIndex);
    	System.out.print(typeViolenceName + "/ms\t");

    	for(int i = 0; i < typeViolenceTimeArr.size(); i ++){
    		System.out.print(getTheoryUsedTimeViolenceType(baseSize,scales.get(i),typeViolenceTimeArr.get(baseIndex)) + "\t");
    	}
    	System.out.println();
    	
    	System.out.print(typeDivideName + "/ms\t");
    	
    	for(int i = 0; i < typeDivideTimeArr.size(); i ++){
    		System.out.print(getTheoryUsedTimeDivideType(baseSize,scales.get(i),typeDivideTimeArr.get(baseIndex)) + "\t");
    	}
    	
    	System.out.printf("\n----------------------------------------------------------------------\n");
    }
    
	/*
	 * 随机生成点数组
	 */	
	public Point[] createPointsRandomInt(int max, int size){
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
	
	public Point[] createPointArray(int size){
		Point[] points = new Point[size];
		for(int i = 0; i < size; i ++){
			points[i] = new Point();
		}
		return points;
	}
	
	public Point[] clonePointArray(Point[] points){
		Point[] newPoints = new Point[points.length];
		for(int i = 0; i < points.length; i ++){
			newPoints[i] = new Point(points[i]);
		}
		return newPoints;
	}
	/*
	 * 显示点数组
	 */
    public void showPoins(String tips,Point[] points,int size, int rowSize){
    	rowSize = rowSize > size ? size : rowSize;
    	size = size > points.length ? points.length : size;
    	System.out.println( tips + "：" + "(共" + size + "个点)");
    	int i;
    	for(i = 0; i < size-1; i ++){    		
    		System.out.print("(" + points[i].x + "," + points[i].y + "), ");
    		if((i + 1) % rowSize == 0){
    			System.out.println();
    		}
    	}
		System.out.println("(" + points[i].x + "," + points[i].y + ")");
    }
    
    public void showResult(double minDist,Point startPoint,Point endPoint){
    	System.out.println("最短距离为：" + minDist);
    	System.out.println("两点分别为：(" + startPoint.x + "," + startPoint.y + "), (" + endPoint.x + "," + endPoint.y + ")");
    }

	/*
	 * 两点间距离
	 */
	public double DistanceOf2Points(Point startPoint,Point endPoint){
		double diffX = startPoint.x - endPoint.x;
		double diffY = startPoint.y - endPoint.y;
		return Math.sqrt(diffX * diffX + diffY * diffY);
	}
	
	public double min(double minL,double minR){
		return minL < minR? minL : minR;
	}
	
	public static long getTheoryUsedTimeDivideType(int baseSize, int size, long baseRealUsedTime){
		double baseTime = baseSize * Math.log10(baseSize);
		double theoryTime = size * Math.log10(size);
		return (long)(baseRealUsedTime * theoryTime / baseTime);
	}
	public static long getTheoryUsedTimeViolenceType(int baseSize, int size, long baseRealUsedTime){
		double mutil = size*1.0 / baseSize;
		return (long)(baseRealUsedTime * mutil * mutil);
	}
	
	public static ArrayList<Integer> createScaleArray(){
    	final int INCREASESLOWPOINT = 10000;
    	final int MAXSIZE = INCREASESLOWPOINT * 10;
    	
		int baseSize = 100;
    	int multiNum = 10;
    	ArrayList<Integer> scales = new ArrayList<Integer>();
    	
    	int size = baseSize;
    	while(size < INCREASESLOWPOINT){
    		scales.add(size);
    		size *= multiNum;
    	}
    	while(size <= MAXSIZE){
    		scales.add(size);
    		size += INCREASESLOWPOINT;
    	}
    	return scales;
	}
}