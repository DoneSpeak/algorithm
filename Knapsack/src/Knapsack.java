import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.Scanner;

public class Knapsack {
	public static boolean needShow = true;
	public static final int TESTTIME = 20;
	public static final int MAXGOODSNUM = 10000;
	public static final int MAXVOL = 10000;
	public static final int MUTILGOODSNUM = 2;
	public static final int MUTILVOL = 2;
	public static final int INITGOODSNUM = 100;
	public static final int INITVOL = 100;
	
	public static final int DP = 1;
	public static final int VIOLENCE = 2;
	
	public static void main(String args[]){
		showTime();
	}
	public static void showTime(){
		System.out.println("欢迎来到 0/1 背包 的世界");
		while(true){
			int option = 0;
			Scanner input = new Scanner(System.in);
			while(option < 1 || option > 3){
				System.out.println("1、简单测试          2、实验数据获取          3、退出");
				option = input.nextInt();
			}
			switch(option){
			case 1:{
				simpleTest();
				break;
			}
			case 2:{
				System.out.println("1、动态规划             2、回溯算法");
				int type = input.nextInt();
				while(type < 1 || type > 2){
					System.out.println("1、动态规划             2、回溯算法");
					type = input.nextInt();
				}
				Test(type);				
				break;
			}
			case 3:{
				System.out.println("谢谢使用！");
				return;
			}
			default:{
				break;
			}
			}
		}
	}
	public static void Test(int type){
		needShow = false;
		Knapsack knap = new Knapsack();
		System.out.print("n\\w\t");
		for(int vol = INITVOL; vol <= MAXVOL; vol *= MUTILVOL){
			System.out.print(vol + "\t");
		}
		System.out.println();
		for(int goodsNum = INITGOODSNUM; goodsNum <=MAXGOODSNUM; goodsNum *= MUTILGOODSNUM){
			System.out.print(goodsNum + "物品\t");
			for(int volume = INITVOL; volume <=MAXVOL; volume *= MUTILVOL){
				long usedTime = 0;
				for(int time = 0; time < TESTTIME; time ++){
					
					Goods[] goodses = cteateGoodsArray(goodsNum,volume);
					
					long startTime = System.nanoTime();
					if(type == DP){
						knap.solveKnapsack(goodses,goodsNum,volume);
					}else{
						knap.solveViolence(goodses,goodsNum,volume,0,0,0);
					}
					long endTime = System.nanoTime();
					
					usedTime += endTime - startTime;
				}
				System.out.print(usedTime/TESTTIME + "\t");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static void simpleTest(){
		needShow = true;
		Scanner input = new Scanner(System.in);
		Knapsack knap = new Knapsack(); 
		
		System.out.println("请输入物品数目和背包总体积：");
		int goodsNum = input.nextInt();
		int volume = input.nextInt();
		
		while(goodsNum <= 0 || volume <= 0){
			System.out.println("存在小于或者等于0的数值，请从新输入");
			goodsNum = input.nextInt();
			volume = input.nextInt();
		}
		Goods[] goodses = cteateGoodsArray(goodsNum,volume);
		knap.testViolence(goodses,goodsNum,volume);
		knap.solveKnapsack(goodses,goodsNum,volume);
		
		
	}
	
	public void solveKnapsack(Goods[] goodses,int goodsNum,int totalVolume){
		
		//动态规划是基于原来的记录进行的一种求解放置，这里只需要利用一维数组进行数据存储即可
		//每次都是从最后面开始进行更新，前面的为历史记录，这样能够有效的保存历史记录，减少空间复杂度
		int values[] = new int[totalVolume + 1];
		boolean pulled[][] = new boolean[goodsNum][totalVolume + 1];
		
		//设置记录放置物品的二位数组为false
		for(int i = 0; i < goodsNum; i ++){
			for(int j = 0; j < totalVolume; j ++){
				pulled[i][j] = false;
			}
		}
		
		//将物品按照体积从小到大排序
		Arrays.sort(goodses,new GoodsComprator());
		//显示商品数组
		if(needShow){
			showGoodses(goodses,goodsNum,totalVolume);
		}
		//动态规划
		for(int i = 0; i < goodsNum; i ++){
			//由于物品已经按照体积进行升序排序，所以这里只要vol - goodses[i].volume即可结束以下循环
			//其后的物品无需再进行判断
			for(int vol = totalVolume; vol - goodses[i].volume >= 0; vol --){
				if(values[vol]<values[vol - goodses[i].volume] + goodses[i].value){
					values[vol] = values[vol - goodses[i].volume] + goodses[i].value;
					//第i个物品放置
					pulled[i][vol] = true;
				}
			}
			/*
			for(int vol = totalVolume; vol > 0; vol --){
				if(vol - goodses[i].volume >= 0 && values[vol]<values[vol - goodses[i].volume] + goodses[i].value){
					values[vol] = values[vol - goodses[i].volume] + goodses[i].value;
					//第i个物品放置
					pulled[i][vol] = true;
				}
			}
			*/
		}
		if(needShow){
			showResult(values[totalVolume], goodsNum, totalVolume, pulled,goodses);
		}
	}
	
	public int solveViolence(Goods[] goodses,int goodsNum,int totalVolume,int index,int heightValue, int volumePulled){
		int value = heightValue;
		for(int i = index; i < goodsNum; i ++){
			int tempValue = heightValue;
			int tempPulled = volumePulled;
			if(totalVolume >= goodses[i].volume + tempPulled){
				tempValue += goodses[i].value;
				tempPulled += goodses[i].volume;
				
				tempValue = solveViolence(goodses,goodsNum,totalVolume,i+1,tempValue,tempPulled);
				value = value > tempValue? value : tempValue;
			}
		}
		return value;
	}
	
	public void testViolence(Goods[] goodses,int goodsNum,int totalVolume){

		showGoodses(goodses,goodsNum,totalVolume);
		
		int value = solveViolence(goodses,goodsNum,totalVolume,0,0,0);
		System.out.println("回溯算法所得最高价值：" + value);
		System.out.println();
	}
	
	public void showGoodses(Goods[] goodses, int goodsNum, int totalVolume){
		System.out.println("物品总个数：" + goodsNum);
		System.out.println("物品总体积：" + totalVolume);
		System.out.println("物品详细(volume,value)：");
		int totalValues = 0;
		for(int i = 0; i < goodsNum; i ++){
			System.out.print("(" + goodses[i].volume + "," + goodses[i].value + ") ");
			totalValues += goodses[i].value;
			
		}
		System.out.println();
		System.out.println("物品总价值：" + totalValues);
		System.out.println();
	}
	
	//按照将物品从包中去出的方式来判断包中有哪些物品
	public void showResult(int heightestValue, int goodsNum, int totalVolume, boolean pulled[][], Goods goodses[]){
		System.out.println("最高价值为：" + heightestValue);
		System.out.println("放入包中的物品有：");
		for(int i = goodsNum-1, vol = totalVolume; i >= 0 ; i --){
			if(pulled[i][vol]){
				System.out.print("(" + goodses[i].volume + "," + goodses[i].value + ") ");
				vol -= goodses[i].volume;
			}
		}
		System.out.println();
		System.out.println();
	}
	
	public static Goods[] cteateGoodsArray(int goodsNum,int totalVolume){
		Random random = new Random();
		Goods[] goodses = new Goods[goodsNum];
		//随机产生各个物品体积
		for(int i = 0; i < goodsNum; i ++){
			int goodsVol = Math.abs(random.nextInt()%totalVolume);
			while(goodsVol <= 0){
				goodsVol = Math.abs(random.nextInt()%totalVolume);
			}
			int goodsVal = Math.abs(random.nextInt()%(totalVolume * 100));
			while(goodsVal <= 0){
				goodsVal = Math.abs(random.nextInt()%(totalVolume * 100));
			}
			goodses[i] = new Goods(goodsVol,goodsVal);
		}
		return goodses;
	}
}


class GoodsComprator implements Comparator<Object>{
	@Override
	public int compare(Object obj1, Object obj2){
		Goods g1 = (Goods)obj1;
		Goods g2 = (Goods)obj2;
		if(g1.volume > g2.volume){
			return 1;
		}else if(g1.volume < g2.volume){
			return -1;
		}else{
			return 0;
		}
	}
}
