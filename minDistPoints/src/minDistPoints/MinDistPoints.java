/*
 * ˵����
 * ǰ��:û���ص��ĵ�
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
	 * ����
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

/*********************���������*****************************/
	/*
	 * ���������
	 * ���ÿһ���㵽�����ĵ�ľ��룬�Ӷ������С�ľ�������Ӧ��������
	 * ����Ϲ�ϵ���Եó�������N(N-1)/2�Ե��
	 * Ҳ������Ҫѭ��N(N-1)/2��
	 * ʱ�临�Ӷ�ΪO(N^2)
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
	
	
/********************���η����***********************/
	public double getNearestPointsByDivide(Point[] points, Point startPoint, Point endPoint, int size){
		//Ԥ�����ֱ��������㼯poins�е�x�����y����������򣬵õ�pointSetX��pointSetY�����㼯
		Point[] pointSetX = createPointArray(size);
		Point[] pointSetY = createPointArray(size);
		if(showPoints){
			showPoins("�����ĵ㼯",points,size,100);
		}
		pointSetX = sort(points,size,POINTMODEX);
		pointSetY = sort(points,size,POINTMODEY);
		if(showPoints){
			showPoins("�㼯����  x ��������",pointSetX,size,100);
			showPoins("�㼯����  y ��������",pointSetY,size,100);
		}
		
		return getNearestPointsByDivide(pointSetX,pointSetY,startPoint,endPoint,size);
	}
	
	public double getNearestPointsByDivide(Point[] pointSetX,Point[] pointSetY,Point startPoint,Point endPoint,int size) {
		
		//����Ϊ1ʱ
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
		//����>3ʱ����ƽ��㼯�ָ��Ϊ��С������ȵ������Ӽ�pointSetXL��pointSetXR��ѡȡһ����ֱ��middleLine��Ϊ�ָ�ֱ��
		//��ֱ�߿������±���У��Ӷ����Ա����������ߵĵ�������size/2,��ֹ���֣����Ϊ1���ұ�Ϊsize-1���ֲ������㷨�����
		return hasMoreThanThreePoint(pointSetX,pointSetY,startPoint,endPoint,size);
	}
	
	/*
	  ���м��������С����
	 */
	//[start] ��ȡ�м�����㼯����С���룬�������Ҽ��Ϸ�
	public double hasMoreThanThreePoint(Point[] pointSetX,Point[] pointSetY,Point startPoint,Point endPoint,int size) {
		int middle = size / 2;
		int leftSize = middle;
		int rightSize = size - middle;
		int middleX = pointSetX[middle].x;
		int middleY = pointSetX[middle].y;
		//pointSetX�������X�㼯
		Point[] pointSetXL = createPointArray(leftSize);
		Point[] pointSetXR = createPointArray(rightSize);
		for(int i = 0; i < middle; i ++){
			pointSetXL[i].setPointValue(pointSetX[i]);
		}
		//middle�����Ҳ�㼯
		for(int i = 0; i < rightSize; i ++){
			pointSetXR[i].setPointValue(pointSetX[i + middle]);
		}
		//pointSetY��ֵ�����Y�㼯��ʹ�ù鲢����MERGE���෴����
		Point[] pointSetYL = createPointArray(leftSize);
		Point[] pointSetYR = createPointArray(rightSize);
		int leftIndex = 0;
		int rightIndex = 0;
		try{
			for(int i = 0; i < size; i ++){
				//������ص��ĵ㣬�ͻᷢ��Խ�����
				if(pointSetY[i].x < middleX){
					pointSetYL[leftIndex ++].setPointValue(pointSetY[i]);
				}else if(pointSetY[i].x == middleX){

					if(pointSetY[i].y < middleY){
						//Сy�����
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
			this.showPoins("��ߵ㼯", pointSetYL, leftIndex,100);
			this.showPoins("�ұߵ㼯", pointSetYR, rightIndex,100);
			e.printStackTrace();
			Scanner reader = new Scanner(System.in);
			reader.nextLine();
		}
		//�����ݹ���ã��ֱ����pointSetL��pointSetR�е���̾���ΪminDistL��minDistR
		//getNearestPointsBy(Point[] pointSetX,Point[] pointSetY,Point startPoint,Point endPoint,int size)
		Point startPointR = new Point();
		Point endPointR = new Point();
		
		double minDist = getNearestPointsByDivide(pointSetXL,pointSetYL,startPoint,endPoint,leftSize);
		double minDistR = getNearestPointsByDivide(pointSetXR,pointSetYR,startPointR,endPointR,rightSize);
		//ȡminDist=min(minDistL, minDistR)��ȷ�����������㼯����С����
		if(minDist > minDistR){
			minDist = minDistR;
			startPoint.setPointValue(startPointR);
			endPoint.setPointValue(endPointR);
		}
				
		//��ֱ��middleLine���߷ֱ���չminDist���õ��߽�����middleSet��middleSet������x��������
		//middleSetY������middleSet�еĵ㰴��y����ֵ�����õ��ĵ㼯��middleSetY�ֿɷ�Ϊ������������middleSetYL��middleSetYR
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
	
	//����м���������
	public double getMinDistInMiddleSetLR(Point[] pointSetYL,Point[] pointSetYR,Point startPoint,Point endPoint,
			int size,int middleX,double minDist) {
		//��ֱ��middleLine���߷ֱ���չminDist���õ��߽�����middleSet��middleSet������x��������
		//middleSetY������middleSet�еĵ㰴��y����ֵ�����õ��ĵ㼯��middleSetY�ֿɷ�Ϊ������������middleSetYL��middleSetYR
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
			this.showPoins("��ߵ㼯", middleSetYL, middleSetYLeftLen,100);
			this.showPoins("�ұߵ㼯", middleSetYR, middleSetYRightLen,100);
		}
		
		//����middleSetYL�е�ÿһ�㣬���middleSetYR�еĵ������ľ��룬��������õ��������
		int nearestIndex = 0;
		for(leftIndex = 0; leftIndex < middleSetYLeftLen; leftIndex ++){
			//�ҵ��Ҳ�������ÿ������������
			try{
				int diffLeftToRightY = Integer.MAX_VALUE;
				while(nearestIndex < middleSetYRightLen && diffLeftToRightY > minDist){
					diffLeftToRightY = middleSetYL[leftIndex].y - middleSetYR[nearestIndex].y;
					nearestIndex ++;
				}
				//���������ѭ��ʱ�����ֿ��ܣ�
				//1���ҵ������ڵĵ㣻
				//2���Ҳ���ֵ�һ��������middleSetYR[nearestIndex]��y�����minDist�ĵ㣬��ʱdiffLeftToRightY < -minDist,��δ�ҵ������ڵĵ�
				if(diffLeftToRightY < -minDist){
					//������2
					/*
					 *    | +
					 *    |    +
					 *    |  + 
					 *  + | 
					 */
					//�ص���һ����
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
			//�ص���һ����
			nearestIndex --;
			nearestIndex = nearestIndex > 0 ? nearestIndex : 0;
		}
		return minDist;
	}
	/*
	 * ���м�������Ϊһ���㼯���д���
	 */
	//[start] ��ȡ�м�����㼯����С����
	public double getMinDistInMiddleSet(Point[] pointSetY,Point startPoint,Point endPoint,
			int numPointsInMiddleSet,int middleX,double minDist) {
		Point[] middleSet = createPointArray(numPointsInMiddleSet);
		//����м���еĵ�
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
		//����м���еĵ�
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
/********************�鲢����*************************/
	
	public Point[] sort(Point[] points, int size,int mode){
		Point[] newPoints = clonePointArray(points);
		mergeSort(newPoints,size,mode);
		return newPoints;
	}
    //�鲢 -- �ϲ��������������
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
            //��������
            mergeSort(point, first, mid, size,store,mode);
            //�Ұ������
            mergeSort(point, mid + 1, last, size, store,mode);
            //�ϲ�������������
            mergeSort(point, first, mid, last, size, store,mode);
        }
    }

    //�˺�����������Ϊ���������Ѿ�������ֻҪ�����߲���ѡ��С�ĺϲ��Ϳ�����
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

        //�鲢ʣ���
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

        //��ԭ��ԭ������
        for (left = 0; left < storeIndex; left++)
        {
        	point[first + left].setPointValue(store[left]);
        }
    }
    
    /*
     * �鲢���������
     * X: ��ͬx�İ���y���򣨴�С����
     * Y: ��ͬy�İ���x���򣨴�С����
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
/********************��������***********************/

    public static void showTime(){
    	
    	while(true){
	    	System.out.println("��ѡ������Ҫ�Ĳ�����");
	    	System.out.println("1��չʾ������    2��չʾ���η�    3����ȡ����ʱ��    4�����Է��η�    5���˳�");
	    	int option;
	    	Scanner input = new Scanner(System.in);
	    	option = input.nextInt();
	    	switch(option){
		    	case MinDistPoints.VIOLENCEOPTION:{
		    		int size = 0;
		    		while(size < 2){
			    		System.out.println("�������ģ(>1)��");
			    		size = input.nextInt();
		    		}
		    		showMeHow(VIOLENCE,size);
		    		break;
		    	}
		    	case MinDistPoints.DIVIDEOPTIOIN:{
		    		int size = 0;
		    		while(size < 2){
			    		System.out.println("�������ģ(>1)��");
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
			    		System.out.println("��ѡ�����ģʽ��");
			    		System.out.println("1�����м��ʱ�������������䣬���Ҳ������������");
			    		System.out.println("2�����м��ʱ���������������俴�������������");
			    		System.out.println("3�����м��ʱ��ʹ�������������Ż�");
			    		System.out.println("ע�����Թ�ģΪ[" + scale + "],���Դ���Ϊ[" + testTime + "]");
			    		testType = input.nextInt();
		    		}
		    		long startTime = System.currentTimeMillis();
		    		TestDivide(testTime,scale);
		    		long endTime = System.currentTimeMillis();
		    		long usedTime = endTime - startTime;
		    		System.out.println("===================������ʹ��ʱ�䡿=================");
		    		System.out.println(usedTime + " ms");
		    		break;
		    	}case MinDistPoints.EXITOPTION:{
		        	System.out.println("��л����ʹ��    &(@-@)&");
		    		return;
		    	}default:{
		    		System.out.printf("������ 1��2 ���� 3\n\n");
		    		continue;
		    	}
	    	}
	    	System.out.println("===========================================================");
    	}
    }
    
    public static double showMeHow(int type,int size){
    	
		System.out.printf("-------------------[");
		if(type == MinDistPoints.VIOLENCE){
			System.out.printf("@������@");
		}else{
			System.out.printf("M���η�M");
		}
		System.out.println("]-------------------");
//		minDistPoints.showPoins("������ɵĵ㼯",points, size,100);
		if(size <= 10){
			showTenPointDetail(type);
		}
		//[TODO]
		Point[] points = Point.createPointsRandomInt(size*100,size);
		
		return showMeHow(points,type,size);
    }
    
    public static double showMeHow(Point[] points, int type, int size){
		MinDistPoints minDistPoints = new MinDistPoints();
		//���ò������ͣ�Ĭ��Ϊ���������������
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
        System.out.println("--- [���ݹ�ģ��" + size + "] ---");
        minDistPoints.showResult(minDist,startPoint,endPoint);
		System.out.println("��ʱ��" + timeUsed + "e" + (int)Math.log10(NANOTIMEDIVIDER) + " ns");
		System.out.println();
		return minDist;
    }
    
    public static void showTenPointDetail(int type){
    	final int SIZE = 10;
    	MinDistPoints minDistPoints = new MinDistPoints();
    	
		Point[] points = minDistPoints.createPointsRandomInt(SIZE * 100,10);
		minDistPoints.showPoins("������ɵ㼯", points, SIZE, 2);
		System.out.println();
		Point startPoint = new Point();
		Point endPoint = new Point();
		double minDist = -1;
		//ʮ�������ϸ���
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
		
		//��̾���
    	long startTime=System.nanoTime();
		if(type == MinDistPoints.VIOLENCE){
			minDist = minDistPoints.getNearestPointsDerectly(points,startPoint,endPoint,SIZE);
		}else if(type == MinDistPoints.DIVIDE){
			minDist = minDistPoints.getNearestPointsByDivide(points,startPoint,endPoint,SIZE);
		}
		long endTime=System.nanoTime();
        long timeUsed = (endTime - startTime)/NANOTIMEDIVIDER;

        minDistPoints.showResult(minDist,startPoint,endPoint);
		System.out.println("��ʱ��" + timeUsed + "e" + (int)Math.log10(NANOTIMEDIVIDER) + " ns");
    }
    
    public static void TestDivide(int time, int size){
    	for(int i = 0; i < time; i ++){
    		Point[] points = Point.createPointsRandomInt(size*100,size);
    		System.out.println("------------��[" + (i+1) + "]��-----------");
    		System.out.println("--[@������@]--");
    		double minDist = showMeHow(points,MinDistPoints.VIOLENCE,1000);
    		System.out.println("--[M���η�M]--");
    		double minDistDivide =  showMeHow(points,MinDistPoints.DIVIDE,1000);

			System.out.println("--[&���Խ��&]--");
    		if(minDist != minDistDivide){
    			System.err.println("����");
    			Scanner reader = new Scanner(System.in);
    			reader.nextLine();
    		}else{
				System.out.printf("��ȷ\n\n");
    		}
    	}
    }
    
    /*
     * ÿ�ַ�������20�Σ�ȡƽ��ֵ
     * ���ݹ�ģ�ֱ�Ϊ��N=100,1 000,10 000,100 000��1 000 000,10 000 000
     * ����˳���ȼ������������ټ���鲢��
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
			System.out.println("-----------[ ���ݹ�ģΪ��" + size + " ]-------------");
			
			for(int i = 0; i < TIME; i ++){
				MinDistPoints minDistPoints = new MinDistPoints();
				Point[] points = minDistPoints.createPointsRandomInt(size*100,size);
				if(showPoints){
					minDistPoints.showPoins("������ɵĵ㼯",points, size,100);
				}
				Point startPoint = new Point();
				Point endPoint = new Point();
				double minDist = -1;
				//��������������
				System.out.println("-@��������������@-");
				long startTime = System.nanoTime();
				minDist = minDistPoints.getNearestPointsDerectly(points,startPoint,endPoint,size);
				long endTime = System.nanoTime();
				
				long usedTime = (endTime - startTime)/NANOTIMEDIVIDER; 
				sumUsedTimeOfViolence += usedTime;
				if(showEachTime){
					System.out.println("��ʱ��" + usedTime + "e" + (int)Math.log10(NANOTIMEDIVIDER) + " ns");
					minDistPoints.showResult(minDist,startPoint,endPoint);
					System.out.println();
				}
				
				//���η���������
				System.out.println("-M���η���������M-");
				Point startPoint2 = new Point();
				Point endPoint2 = new Point();
				double minDist2 = -1;
				
				startTime = System.nanoTime();
				minDist2 = minDistPoints.getNearestPointsByDivide(points,startPoint2,endPoint2,size);
				endTime = System.nanoTime();
				
				usedTime = (endTime - startTime)/NANOTIMEDIVIDER;
				sumUsedTimeOfDivide += usedTime;
				if(showEachTime){
					System.out.println("��ʱ��" + usedTime + "e" + (int)Math.log10(NANOTIMEDIVIDER) + " ns");			
					minDistPoints.showResult(minDist2,startPoint2,endPoint2);
					System.out.println("---------------[��  " + i + " ����� ]----------------");
				}
				if(minDist != minDist2){
					System.err.println("����������η���õ���С���벻ͬ���㷨����");
					Scanner reader = new Scanner(System.in);
					reader.nextLine();
				}else{
					
				}
			}
			long averTimeViolence = sumUsedTimeOfViolence / TIME;
			averTimeV.add(averTimeViolence);
			long averTimeDivide = sumUsedTimeOfDivide / TIME;
			averTimeM.add(averTimeDivide);
			System.out.println(">>>---\\ƽ����ʱ/--->");
			System.out.println("��������" + averTimeViolence + "e" + (int)Math.log10(NANOTIMEDIVIDER) + " ns");
			System.out.println("���η���" + averTimeDivide + "e" + (int)Math.log10(NANOTIMEDIVIDER) + " ns");
			System.out.println();
		}
		System.out.print("\n\n>>>>>>>>> ���ս�� <<<<<<<<<\n");
		showUsedTimeResult("������","���η�",averTimeV,averTimeM,scales);
		int scale1000Index = 2;
		showUsedTimeTheory("������","���η�",averTimeV,averTimeM,scales,scale1000Index);
    }
    
    public static void showUsedTimeResult(String typeOneName,String typeTwoName,
    		ArrayList<Long> typeOneTimeArr,ArrayList<Long> typeTwoTimeArr,ArrayList<Integer> scales){
    	
    	System.out.println("-----------------------------------------[ʵ��ֵ]-----------------------------------");

    	System.out.print("��ģ��С/��" + "\t");
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

    	System.out.println("----------------------------------[����ֵ]----------------------------------");

    	System.out.print("��ģ��С/��" + "\t");
		
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
	 * ������ɵ�����
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
	 * ��ʾ������
	 */
    public void showPoins(String tips,Point[] points,int size, int rowSize){
    	rowSize = rowSize > size ? size : rowSize;
    	size = size > points.length ? points.length : size;
    	System.out.println( tips + "��" + "(��" + size + "����)");
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
    	System.out.println("��̾���Ϊ��" + minDist);
    	System.out.println("����ֱ�Ϊ��(" + startPoint.x + "," + startPoint.y + "), (" + endPoint.x + "," + endPoint.y + ")");
    }

	/*
	 * ��������
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