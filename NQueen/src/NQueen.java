import java.util.Scanner;

/*
 * �涨��
 * 1�����о�ʹ�ô�1��ʼ
 */
public class NQueen {
	public static void main(String[] args){
		showTime();
	}
	
	public static void showTime(){
		System.out.println("---------&&&@��ӭ����N�ʺ������@&&&---------");
		Scanner input = new Scanner(System.in);
		NQueen nqueen = new NQueen();
		while(true){
			while(true){
				System.out.println("��ѡ������Ҫ�����ͣ�");
				System.out.println("1. �������     2. �ݹ����      3. λ�������      4. ����      5. �˳�");
				int option = input.nextInt();
				switch(option){
				case 1:{
					System.out.println("������ʺ�����");
					int queenNum = input.nextInt();
					int solutionNum = nqueen.placeNQueen_iteration(queenNum);
					showResult(solutionNum);
					break;
				}
				case 2:{
					System.out.println("������ʺ�����");
					int queenNum = input.nextInt();
					int solutionNum = nqueen.placeNQueen_recursion(queenNum);
					showResult(solutionNum);
					break;
				}
				case 3:{
					System.out.println("������ʺ�����");
					int queenNum = input.nextInt();
					int solutionNum = nqueen.placeNQueen_binary(queenNum);
					showResult(solutionNum);
					break;
				}
				case 4:{
					setting();
					break;
				}
				case 5:{
					return;
				}default:{
					continue;
				}
				}
			}
		}
	}

	//�ǵݹ�ʵ��
	public int placeNQueen_iteration(int queenNum){
		int[] cols = new int[queenNum+1];
		int solutionNum = 0;
		int row = 1;
		cols[row] = 1;
		//�������н��б�����Ҳ���ǽ�ÿ���ʺ����з���
		//�ô��������ٱȽ�ͬ��λ��
		while(row > 0){
			if(row <= queenNum && cols[row] <= queenNum ){

				if(isAllowPosition(row,cols[row],cols)){
					//�����λ�ÿ��Է��ûʺ�
					//��ʼ������һ�еĻʺ�
					row ++;
					//ÿһ�еĻʺ󶼴ӵ�һ�е�λ�ÿ�ʼ����
					cols[row] = 1;
				}else{
					//��λ�ýڵ㲻�ɷ���
					//�򽫽ڵ���õ�ͬ�е���һλ��
					//Խ�����������һ��ѭ���н��
					cols[row] ++;
				}
			}else{
				//Խ��:��Խ�����Խ��
				//��Խ�磺�ѷ����������еĻʺ�һ��������
				if(row > queenNum){
					solutionNum ++;
					showQueenPositions(cols,queenNum);
				}
				//��Խ�磺���ݵ���һ�У��ʺ�����һ��λ�÷���
				//|x|x|x|o| |
				//|x|x|x|x|x|*
				//��Խ�磺�ҵ�һ���⣬��������Ҫ���ʺ�������һ������λ�ý����ж�
				//|o| | | | |
				//|x|x|o|-|-|
				row --;
				cols[row] ++;
			}
		}
		return solutionNum;
	}
	//�ݹ�ʵ��
	
	public int placeNQueen_recursion(int queenNum){
		int solutionNum = 0;
		int[] cols = new int[queenNum + 1];
		return placeQueen(1,cols,queenNum,solutionNum);
	}
	
	public int placeQueen(int row, int[] cols, int queenNum,int solutionNum){
		if(row > queenNum){
			//��Խ�磺�ҵ�һ����
			showQueenPositions(cols,queenNum);
			solutionNum ++;
		}else{
			//�Ը��е�����λ�ý��б���
			for(int col = 1; col <= queenNum; col ++){
				if(isAllowPosition(row,col,cols)){
					//���λ�ÿɷţ�����ã�������һ�еĻʺ�
					cols[row] = col;
					solutionNum = placeQueen(row + 1,cols,queenNum,solutionNum);
				}
				//λ�ò��ɷţ�����һ��
			}
		}
		return solutionNum;
	}
	
	//[TODO]λ����ʵ��
	public int placeNQueen_binary(int queenNum){
		return 0;
	}
	//�Գ���ʹ��
	//ͨ�������ϵ�����ʵ�������һ�������жϼ���ʵ��
	
	//��������

	public static void setting(){
		
	}
	public boolean isAllowPosition(int row,int col,int[] cols){
		//���ѷ��ûʺ��λ�ý��б���
		for (int queenIndex = 1; queenIndex <= row; queenIndex ++){
			//�ж��Ƿ���ͬһб�߻���ͬ��
			if ( ( Math.abs(col-queenIndex) == Math.abs(cols[queenIndex]-cols[row]) )
					||(cols[queenIndex] == cols[row])){
				return false;
			}
		}
		return true;
	}
	
	//[TODO]�ж��Ƿ�Ϊ0
	public static void showResult(int solutionNum){
		System.out.println("һ���У�" + solutionNum + "���ڷŷ�ʽ");
	}
	
	public void showQueenPositions(int[] cols,int queenNum){
		for(int row = 1; row <= queenNum; row ++){
			int col = 1;
			System.out.println("+");
			while(col <= queenNum){
				System.out.println("-+");
				col ++;
			}
			col = 1;
			while(col < cols[row]){
				System.out.println("|x");
				col ++;
			}
			System.out.println("|o|");
			col ++;
			while(col <= queenNum){
				System.out.println("x|");
				col ++;
			}
		}
		
		System.out.println("+");
		for(int col = 1; col <= queenNum; col ++){
			System.out.println("-+");
		}
	}
}
