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
		System.out.println("��ӭ���� 0/1 ���� ������");
		while(true){
			int option = 0;
			Scanner input = new Scanner(System.in);
			while(option < 1 || option > 3){
				System.out.println("1���򵥲���          2��ʵ�����ݻ�ȡ          3���˳�");
				option = input.nextInt();
			}
			switch(option){
			case 1:{
				simpleTest();
				break;
			}
			case 2:{
				System.out.println("1����̬�滮             2�������㷨");
				int type = input.nextInt();
				while(type < 1 || type > 2){
					System.out.println("1����̬�滮             2�������㷨");
					type = input.nextInt();
				}
				Test(type);				
				break;
			}
			case 3:{
				System.out.println("ллʹ�ã�");
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
			System.out.print(goodsNum + "��Ʒ\t");
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
		
		System.out.println("��������Ʒ��Ŀ�ͱ����������");
		int goodsNum = input.nextInt();
		int volume = input.nextInt();
		
		while(goodsNum <= 0 || volume <= 0){
			System.out.println("����С�ڻ��ߵ���0����ֵ�����������");
			goodsNum = input.nextInt();
			volume = input.nextInt();
		}
		Goods[] goodses = cteateGoodsArray(goodsNum,volume);
		knap.testViolence(goodses,goodsNum,volume);
		knap.solveKnapsack(goodses,goodsNum,volume);
		
		
	}
	
	public void solveKnapsack(Goods[] goodses,int goodsNum,int totalVolume){
		
		//��̬�滮�ǻ���ԭ���ļ�¼���е�һ�������ã�����ֻ��Ҫ����һά����������ݴ洢����
		//ÿ�ζ��Ǵ�����濪ʼ���и��£�ǰ���Ϊ��ʷ��¼�������ܹ���Ч�ı�����ʷ��¼�����ٿռ临�Ӷ�
		int values[] = new int[totalVolume + 1];
		boolean pulled[][] = new boolean[goodsNum][totalVolume + 1];
		
		//���ü�¼������Ʒ�Ķ�λ����Ϊfalse
		for(int i = 0; i < goodsNum; i ++){
			for(int j = 0; j < totalVolume; j ++){
				pulled[i][j] = false;
			}
		}
		
		//����Ʒ���������С��������
		Arrays.sort(goodses,new GoodsComprator());
		//��ʾ��Ʒ����
		if(needShow){
			showGoodses(goodses,goodsNum,totalVolume);
		}
		//��̬�滮
		for(int i = 0; i < goodsNum; i ++){
			//������Ʒ�Ѿ����������������������������ֻҪvol - goodses[i].volume���ɽ�������ѭ��
			//������Ʒ�����ٽ����ж�
			for(int vol = totalVolume; vol - goodses[i].volume >= 0; vol --){
				if(values[vol]<values[vol - goodses[i].volume] + goodses[i].value){
					values[vol] = values[vol - goodses[i].volume] + goodses[i].value;
					//��i����Ʒ����
					pulled[i][vol] = true;
				}
			}
			/*
			for(int vol = totalVolume; vol > 0; vol --){
				if(vol - goodses[i].volume >= 0 && values[vol]<values[vol - goodses[i].volume] + goodses[i].value){
					values[vol] = values[vol - goodses[i].volume] + goodses[i].value;
					//��i����Ʒ����
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
		System.out.println("�����㷨������߼�ֵ��" + value);
		System.out.println();
	}
	
	public void showGoodses(Goods[] goodses, int goodsNum, int totalVolume){
		System.out.println("��Ʒ�ܸ�����" + goodsNum);
		System.out.println("��Ʒ�������" + totalVolume);
		System.out.println("��Ʒ��ϸ(volume,value)��");
		int totalValues = 0;
		for(int i = 0; i < goodsNum; i ++){
			System.out.print("(" + goodses[i].volume + "," + goodses[i].value + ") ");
			totalValues += goodses[i].value;
			
		}
		System.out.println();
		System.out.println("��Ʒ�ܼ�ֵ��" + totalValues);
		System.out.println();
	}
	
	//���ս���Ʒ�Ӱ���ȥ���ķ�ʽ���жϰ�������Щ��Ʒ
	public void showResult(int heightestValue, int goodsNum, int totalVolume, boolean pulled[][], Goods goodses[]){
		System.out.println("��߼�ֵΪ��" + heightestValue);
		System.out.println("������е���Ʒ�У�");
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
		//�������������Ʒ���
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
