import java.util.Scanner;

/*
 * �涨��
 * 1�����о�ʹ�ô�1��ʼ
 */
public class NQueen {
	public long solutionNum = 0;
	public static boolean showMap = true;
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
				System.out.println("1. �������     2. �ݹ����      3. λ�������      4. ����     5������    6. �˳�");
				int option = input.nextInt();
				switch(option){
				case 1:{
					int binaryType = 3;
					while(binaryType > 2){
						System.out.println("��ѡ��λ����������ͣ�");
						System.out.println("1����ͨ���      2���Գ���");
						binaryType = input.nextInt();
					}
					int queenNum = 0;
					while(queenNum < 2 || queenNum > ((1 << 31) - 1)){
						System.out.println("������ʺ���(1 < n <" + ((1 << 31) - 1) + ")��");
						queenNum = input.nextInt();
					}
					long startTime = System.currentTimeMillis();
				
					if(binaryType == 1){
						nqueen.placeNQueen_iteration(queenNum);
					}else{
						nqueen.placeNQueen_iteration_sym(queenNum);
					}
					long endTime = System.currentTimeMillis();
					System.out.println("��ʱ�� " + (endTime - startTime) + "ms");
					showResult(nqueen.solutionNum);
					break;
				}
				case 2:{
					int binaryType = 3;
					while(binaryType > 2){
						System.out.println("��ѡ��λ����������ͣ�");
						System.out.println("1����ͨ���      2���Գ���");
						binaryType = input.nextInt();
					}
					int queenNum = 0;
					while(queenNum < 2 || queenNum > ((1 << 31) - 1)){
						System.out.println("������ʺ���(1 < n <" + ((1 << 31) - 1) + ")��");
						queenNum = input.nextInt();
					}
					long startTime = System.currentTimeMillis();
					if(binaryType == 1){
						nqueen.placeNQueen_recursion(queenNum);;
					}else{
						nqueen.placeNQueen_recursion_sym(queenNum);;
					}
					long endTime = System.currentTimeMillis();
					System.out.println("��ʱ��" + (endTime - startTime) + "ms");
					showResult(nqueen.solutionNum);
					
					break;
				}
				case 3:{
					int binaryType = 3;
					while(binaryType > 2){
						System.out.println("��ѡ��λ����������ͣ�");
						System.out.println("1����ͨ���      2���Գ���");
						binaryType = input.nextInt();
					}
					
					int queenNum = 0;
					while(queenNum < 2 || queenNum > 64){
						System.out.println("������ʺ���(1 < n < 65)��");
						queenNum = input.nextInt();
					}
					long startTime = System.currentTimeMillis();
					if(binaryType == 1){
						nqueen.placeNQueen_binary(queenNum);
					}else{
						nqueen.placeNQueen_binary_symmetry(queenNum);
					}
					long endTime = System.currentTimeMillis();
					System.out.println("��ʱ��" + (endTime - startTime) + "ms");
					
					showResult(nqueen.solutionNum);
					break;
				}
				case 4:{
					setting();
					break;
				}
				case 5:{
					while(true){
						int queenNum = 0;
						while(queenNum < 2){
							System.out.println("������ʺ���(>1)��");
							queenNum = input.nextInt();
						}
						System.out.println("�����밴�յ�һ�е����һ�е�˳������ʺ�������������1��ʼ��");
						int[] cols  = new int[queenNum + 2];
						for(int i = 1; i <= queenNum; i ++){
							int col = input.nextInt();
							while(col < 1 || col > queenNum){
								System.out.println("�ʺ�λ��Ӧ����0С��" + queenNum);
								col = input.nextInt();
							}
							cols[i] = col;
						}
						
						if(!nqueen.judgeIsRight(cols,queenNum)){
							System.out.println("�ò��ֲ�����Ч���֣�");
						}else{
							System.out.println("��ϲ��&&@-@&&");
							System.out.println("�ò�������Ч����");
						}
						String cont = "A";
						while((!cont.equals("Y")) && (!cont.equals("N"))){
							System.out.println("�Ƿ����[Y/N]");
							cont = input.nextLine();
							cont = input.nextLine();
							cont = cont.toUpperCase();
						}
						if(cont.equals("N")){
							break;
						}
					}
					break;
				}
				case 6:{
					System.out.println("��л����ʹ�� &(@-@)&");
					return;
				}default:{
					continue;
				}
				}
			}
		}
	}

	//�ǵݹ�ʵ��
	public void placeNQueen_iteration(int queenNum){
		//1~row���ûʺ�row + 1����Խ��
		int[] cols = new int[queenNum+2];
		solutionNum = 0;
		int row = 1;
		cols[row] = 1;
		//�������н��б�����Ҳ���ǽ�ÿ���ʺ����з���
		//�ô��������ٱȽ�ͬ��λ��
		while(row > 0){
			if(row <= queenNum && cols[row] <= queenNum ){
				//�����Ԫ��λ��(row,cols[row])
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
					if(showMap){
						showQueenPositions(cols,queenNum,solutionNum);
					}else{
						judgeIsRight(cols,queenNum);
					}
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
	}
	
	//�ǵݹ�ʵ��--���öԳ�
	public void placeNQueen_iteration_sym(int queenNum){
		//1~row���ûʺ�row + 1����Խ��
		int[] cols = new int[queenNum+2];
		solutionNum = 0;
		cols[1] = 1;
		int halfQueenNum = queenNum >> 1;
		//�������н��б�����Ҳ���ǽ�ÿ���ʺ����з���
		while(cols[1] <= halfQueenNum){
			cols[2] = 1;
			queenIter(true,2,cols,queenNum);
		}
		if((queenNum & 1) != 0){
			cols[2] = 1;
			cols[1] = halfQueenNum + 1;
			queenIter(false,2,cols,queenNum);
		}
	}
	
	public void queenIter(boolean isSym,int row,int[] cols,int queenNum){
		while(row > 1){
			if(row <= queenNum && cols[row] <= queenNum ){
				//�����Ԫ��λ��(row,cols[row])
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
					if(showMap){
						showQueenPositions_sym(isSym,cols,queenNum,solutionNum);
					}else{
						judgeIsRight(cols,queenNum);
						if(isSym){
							solutionNum ++;
						}
					}
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
	}
	
	//�ݹ�ʵ��
	public void placeNQueen_recursion(int queenNum){
		solutionNum = 0;
		int[] cols = new int[queenNum + 2];
		placeQueen(false,1,cols,queenNum);
	}
	
	public void placeQueen(boolean isSym,int row, int[] cols, int queenNum){
		if(row > queenNum){
			//��Խ�磺�ҵ�һ����
			solutionNum ++;
			if(showMap){
//				showQueenPositions(cols,queenNum,solutionNum);
				showQueenPositions_sym(isSym,cols,queenNum,solutionNum);
			}else{
				judgeIsRight(cols,queenNum);
				if(isSym){
					solutionNum ++;
				}
			}
		}else{
			//�Ը��е�����λ�ý��б���
			for(int col = 1; col <= queenNum; col ++){
				//�����Ԫ�ص�λ��(row,col)
				if(isAllowPosition(row,col,cols)){
					//���λ�ÿɷţ�����ã�������һ�еĻʺ�
					cols[row] = col;
					placeQueen(isSym,row + 1,cols,queenNum);
				}
				//λ�ò��ɷţ�����һ��
			}
		}
	}
	
	//�ݹ�--���öԳƽ�ʱ�����
	public void placeNQueen_recursion_sym(int queenNum){
		solutionNum = 0;
		int[] cols = new int[queenNum + 2];
		int halfQueenNum = queenNum >> 1;
		cols[1] = 1;
		while(cols[1] <= halfQueenNum){
			placeQueen(true,2,cols,queenNum);
			cols[1] ++;
		}
		if((queenNum & 1) != 0){
			cols[1] = halfQueenNum + 1;
			placeQueen(false,2,cols,queenNum);
		}
	}
	
	//[TODO]λ����ʵ��
	public void placeNQueen_binary(int queenNum){
		solutionNum = 0;
		//queenNumSign���ö����Ƶķ�ʽ��¼�ʺ�ĸ���
		//5���ʺ���Ϊ��10000 - 1 = 000...011111
		long queenNumSigns = (1 << queenNum) - 1;
//		System.out.println("queenNumSigns = " + queenNumSigns);
		int[] cols = new int[queenNum + 2];
		placeBinary(false,cols,1,queenNumSigns,0,0,0);
	}
	
	public void placeNQueen_binary_symmetry(int queenNum){
		solutionNum = 0;
		//queenNumSign���ö����Ƶķ�ʽ��¼�ʺ�ĸ���
		//5���ʺ���Ϊ��10000 - 1 = 000...011111
		long queenNumSigns = (1 << queenNum) - 1;
//		System.out.println("queenNumSigns = " + queenNumSigns);
		int[] cols = new int[queenNum + 2];
		
		//queenNum / 2����Ҫ�ڷ�һ��Ļʺ�
		//5�ʺ� 000011
		long allowPlaces = (1 << (queenNum >> 1)) - 1;
		while(allowPlaces != 0){
			//ȡ�����ұߵ�һλ
			long rightPlace = allowPlaces & (-allowPlaces);
			allowPlaces -= rightPlace;
			cols[1] = queenNum - getNumOfZeroInRight(rightPlace);
			placeBinary(true,cols,2,queenNumSigns,rightPlace,rightPlace >> 1,rightPlace << 1);
		}
		
		if((queenNum & 1) != 0){
			//�������ʺ��м�λ����Ҫ�ж�
			cols[1] = queenNum >> 1 + 1;
			long rightPlace = 1 << (queenNum >> 1);
			placeBinary(false,cols,2,queenNumSigns,rightPlace,rightPlace >> 1,rightPlace << 1);
		}
	}
	
	//binary�����Ǵ����ֿ�ʼ���õ�
	//horizontalLine,leftDiagonal,rightDiagonal��1��ǲ��ɷ��ã�0��ʾ�ɷ���
	//allowPlaces��1��ʾ�ɷ��ã�0��ʾ���ɷ���
	public void placeBinary(boolean isSym,int[] cols,int row,long queenNumSigns,long horizontalLine,long leftDiagonal,long rightDiagonal){
		if(horizontalLine != queenNumSigns){
			//allowPlaces�ϵ�1��ʾ�����Ͽɷŵ�λ�ã����Ϊ0�����ʾ�������޿ɷ���λ��
			long allowPlaces = queenNumSigns & (~(horizontalLine | leftDiagonal | rightDiagonal));
			//ѭ����allowPlaces �ϵ�1 �����0
			while(allowPlaces != 0){
				//�пɷ�λ��
				//��ȡ���ұߵ�λ��
				//-allowPlaces = ~allowPlaces + 1
				long rightPlace = allowPlaces & (-allowPlaces);
				int queenNum = cols.length - 2;
				int col = queenNum - getNumOfZeroInRight(rightPlace);
//				System.out.println("col = " + col);
				cols[row] = col;
				//��ȥ���ұߵ�1
				allowPlaces -= rightPlace;
				placeBinary(isSym,cols,row+1,queenNumSigns,horizontalLine + rightPlace,(leftDiagonal + rightPlace) >> 1, (rightDiagonal + rightPlace)<<1);
			}
		}else{
			//horizontalLine������λ��Ϊ1�����ҵ���һ���ɹ��Ĳ��֣����� 
			solutionNum ++;
			if(showMap){
				showQueenPositions_sym(isSym,cols,row-1,solutionNum);
			}else{
				judgeIsRight(cols,cols.length - 2);
				if(isSym){
					solutionNum ++;
				}
			}
		}
	}
	
	public long getLongSize(){
		//��java�У���Щ���ݴ�С���ǹ̶��ģ�������ϵͳ�ı仯���仯��long���ȶ���64λ
		return Long.SIZE / 8;
	}
	//�Գ���ʹ��
	//ͨ�������ϵ�����ʵ�������һ�������жϼ���ʵ��
	
	//��������

	public static void setting(){
		Scanner input = new Scanner(System.in);
		String option = "";
		while(!option.equals("y") && !option.equals("n") ){
			System.out.println("�Ƿ���Ҫ��ʾ?[Y/N]");
			option = input.nextLine();
			option = option.toLowerCase();
		}
		if(option.equals("y")){
			showMap = true;
		}else{
			showMap = false;
		}
		
	}
	public boolean isAllowPosition(int row,int col,int[] cols){
		//���ѷ��ûʺ��λ�ý��б���
		//�·���Ԫ��(row,col),��ȷ�ϵĸ���Ϊrow - 1
		for (int queenIndex = 1; queenIndex < row; queenIndex ++){
			//�ж��Ƿ���ͬһб�߻���ͬ��
			if ( ( Math.abs(row-queenIndex) == Math.abs(cols[queenIndex]-col) )
					||(cols[queenIndex] == col)){
				return false;
			}
		}
		return true;
	}
	
	public int getNumOfZeroInRight(long binaryNum){
		int counter = 0;
		if(binaryNum == 0){
			return -1;
		}else if ( (binaryNum & 0x1) > 1)		{
			//����������������һλ����1
			counter = 0;
		}else{
			if((binaryNum & 0xffffffff) == 0){
				//1�����32λ��
				binaryNum >>= 32;
				counter += 32;
			}
			if ((binaryNum & 0xffff) == 0) 
			{  
				//1�����16λ��
				binaryNum >>= 16;  
				counter += 16;
			}
			if ((binaryNum & 0xff) == 0) 
			{  
				//1�����8λ��
			    binaryNum >>= 8;  
			    counter += 8;
			}
			if ((binaryNum & 0xf) == 0) 
			{  
				//1�����4λ��
			    binaryNum >>= 4;
			    counter += 4;
			}
			if ((binaryNum & 0x3) == 0) 
			{  
				//1�����2λ��
			    binaryNum >>= 2;
			    counter += 2;
			}
			if((binaryNum & 0x1) == 0) {
				//1�����1λ��
			    counter += 1;
			}
		}
		return counter;
	}
	
	//[TODO]�ж��Ƿ�Ϊ0
	public static void showResult(long solutionNum){
		System.out.println("------------------------------------------");
		System.out.println("һ����[" + solutionNum + "]�ְڷŷ�ʽ");
		System.out.println("==========================================");
	}
	
	public void showQueenPositions(int[] cols,int queenNum,long solutionNo){
		if(!judgeIsRight(cols,queenNum)){
			System.out.println("�㷨����");
		}
		System.out.println("-----------------[��" + solutionNo + "����]----------------");
		for(int row = 1; row <= queenNum; row ++){
			int col = 1;
//			System.out.print("+");
//			while(col <= queenNum){
//				System.out.print("-+");
//				col ++;
//			}
//			System.out.println();
			col = 1;
			while(col < cols[row]){
				System.out.print("|x");
				col ++;
			}
			System.out.print("|o|");
			col ++;
			while(col <= queenNum){
				System.out.print("x|");
				col ++;
			}
			System.out.println();
		}
		System.out.println();
//		System.out.print("+");
//		for(int col = 1; col <= queenNum; col ++){
//			System.out.print("-+");
//		}
//		System.out.println();
	}
	

	public void showQueenPositions_sym(boolean isSym,int[] cols,int queenNum,long solutionNo){
		if(!judgeIsRight(cols,queenNum)){
			System.out.println("�㷨����");
		}
		int time = 1;
		if(isSym){
			time ++;
		}
		while(time-- > 0){
			System.out.println("-----------------[��" + solutionNo + "����]----------------");
			for(int row = 1; row <= queenNum; row ++){
				int queenPlace = cols[row];
				if(time == 1){
					// |<--��>|X|*|*|X|<--��>|
					queenPlace = queenNum - queenPlace + 1;
				}
				int col = 1;
				col = 1;
				while(col < queenPlace){
					System.out.print("|x");
					col ++;
				}
				System.out.print("|o|");
				col ++;
				while(col <= queenNum){
					System.out.print("x|");
					col ++;
				}
				System.out.println();
			}
			System.out.println();
			if(time == 1){
				solutionNum ++;
				solutionNo ++;	
			}
		}
	}
	
	public void showQueenPositions_forText(int[] cols,int queenNum){
		System.out.println("-------���Բ���------");
		for(int row = 1; row <= queenNum; row ++){
			int col = 1;
			col = 1;
			while(col < cols[row]){
				System.out.print("|x");
				col ++;
			}
			System.out.print("|o|");
			col ++;
			while(col <= queenNum){
				System.out.print("x|");
				col ++;
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public boolean judgeIsRight(int[] cols, int queenNum){
		showQueenPositions_forText(cols,queenNum);
		for(int row = 1; row <= queenNum; row ++){			
			if(!isAllowPosition(row,cols[row],cols)){
				return false;
			}
		}
		return true;
	}
}
