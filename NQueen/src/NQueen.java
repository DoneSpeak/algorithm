import java.util.Scanner;

/*
 * 规定：
 * 1、横列均使用从1开始
 */
public class NQueen {
	public long solutionNum = 0;
	public static boolean showMap = true;
	public static void main(String[] args){
		showTime();
	}
	
	public static void showTime(){
		System.out.println("---------&&&@欢迎来到N皇后的世界@&&&---------");
		Scanner input = new Scanner(System.in);
		NQueen nqueen = new NQueen();
		while(true){
			while(true){
				System.out.println("请选择您想要的类型：");
				System.out.println("1. 迭代求解     2. 递归求解      3. 位运算求解      4. 设置     5、测试    6. 退出");
				int option = input.nextInt();
				switch(option){
				case 1:{
					int binaryType = 3;
					while(binaryType > 2){
						System.out.println("请选择位运算求解类型：");
						System.out.println("1、普通求解      2、对称求法");
						binaryType = input.nextInt();
					}
					int queenNum = 0;
					while(queenNum < 2 || queenNum > ((1 << 31) - 1)){
						System.out.println("请输入皇后数(1 < n <" + ((1 << 31) - 1) + ")：");
						queenNum = input.nextInt();
					}
					long startTime = System.currentTimeMillis();
				
					if(binaryType == 1){
						nqueen.placeNQueen_iteration(queenNum);
					}else{
						nqueen.placeNQueen_iteration_sym(queenNum);
					}
					long endTime = System.currentTimeMillis();
					System.out.println("耗时： " + (endTime - startTime) + "ms");
					showResult(nqueen.solutionNum);
					break;
				}
				case 2:{
					int binaryType = 3;
					while(binaryType > 2){
						System.out.println("请选择位运算求解类型：");
						System.out.println("1、普通求解      2、对称求法");
						binaryType = input.nextInt();
					}
					int queenNum = 0;
					while(queenNum < 2 || queenNum > ((1 << 31) - 1)){
						System.out.println("请输入皇后数(1 < n <" + ((1 << 31) - 1) + ")：");
						queenNum = input.nextInt();
					}
					long startTime = System.currentTimeMillis();
					if(binaryType == 1){
						nqueen.placeNQueen_recursion(queenNum);;
					}else{
						nqueen.placeNQueen_recursion_sym(queenNum);;
					}
					long endTime = System.currentTimeMillis();
					System.out.println("耗时：" + (endTime - startTime) + "ms");
					showResult(nqueen.solutionNum);
					
					break;
				}
				case 3:{
					int binaryType = 3;
					while(binaryType > 2){
						System.out.println("请选择位运算求解类型：");
						System.out.println("1、普通求解      2、对称求法");
						binaryType = input.nextInt();
					}
					
					int queenNum = 0;
					while(queenNum < 2 || queenNum > 64){
						System.out.println("请输入皇后数(1 < n < 65)：");
						queenNum = input.nextInt();
					}
					long startTime = System.currentTimeMillis();
					if(binaryType == 1){
						nqueen.placeNQueen_binary(queenNum);
					}else{
						nqueen.placeNQueen_binary_symmetry(queenNum);
					}
					long endTime = System.currentTimeMillis();
					System.out.println("耗时：" + (endTime - startTime) + "ms");
					
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
							System.out.println("请输入皇后数(>1)：");
							queenNum = input.nextInt();
						}
						System.out.println("请输入按照第一行到最后一行的顺序输入皇后列数（列数从1开始）");
						int[] cols  = new int[queenNum + 2];
						for(int i = 1; i <= queenNum; i ++){
							int col = input.nextInt();
							while(col < 1 || col > queenNum){
								System.out.println("皇后位置应大于0小于" + queenNum);
								col = input.nextInt();
							}
							cols[i] = col;
						}
						
						if(!nqueen.judgeIsRight(cols,queenNum)){
							System.out.println("该布局不是有效布局！");
						}else{
							System.out.println("恭喜！&&@-@&&");
							System.out.println("该布局是有效布局");
						}
						String cont = "A";
						while((!cont.equals("Y")) && (!cont.equals("N"))){
							System.out.println("是否继续[Y/N]");
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
					System.out.println("感谢您的使用 &(@-@)&");
					return;
				}default:{
					continue;
				}
				}
			}
		}
	}

	//非递归实现
	public void placeNQueen_iteration(int queenNum){
		//1~row放置皇后，row + 1用于越界
		int[] cols = new int[queenNum+2];
		solutionNum = 0;
		int row = 1;
		cols[row] = 1;
		//对所有行进行遍历，也就是将每个皇后逐行放置
		//好处：无需再比较同行位置
		while(row > 0){
			if(row <= queenNum && cols[row] <= queenNum ){
				//放入的元素位置(row,cols[row])
				if(isAllowPosition(row,cols[row],cols)){
					//如果该位置可以放置皇后
					//则开始放置下一行的皇后
					row ++;
					//每一行的皇后都从第一列的位置开始放置
					cols[row] = 1;
				}else{
					//该位置节点不可放置
					//则将节点放置到同行的下一位置
					//越界问题会在下一次循环中解决
					cols[row] ++;
				}
			}else{
				//越界:行越界和列越界
				//行越界：已放置完所有行的皇后，一个解生成
				if(row > queenNum){
					solutionNum ++;
					if(showMap){
						showQueenPositions(cols,queenNum,solutionNum);
					}else{
						judgeIsRight(cols,queenNum);
					}
				}
				//列越界：回溯到上一行，皇后往后一列位置放置
				//|x|x|x|o| |
				//|x|x|x|x|x|*
				//行越界：找到一个解，但是仍需要将皇后放在最后一行其他位置进行判断
				//|o| | | | |
				//|x|x|o|-|-|
				row --;
				cols[row] ++;
			}
		}
	}
	
	//非递归实现--利用对称
	public void placeNQueen_iteration_sym(int queenNum){
		//1~row放置皇后，row + 1用于越界
		int[] cols = new int[queenNum+2];
		solutionNum = 0;
		cols[1] = 1;
		int halfQueenNum = queenNum >> 1;
		//对所有行进行遍历，也就是将每个皇后逐行放置
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
				//放入的元素位置(row,cols[row])
				if(isAllowPosition(row,cols[row],cols)){
					//如果该位置可以放置皇后
					//则开始放置下一行的皇后
					row ++;
					//每一行的皇后都从第一列的位置开始放置
					cols[row] = 1;
				}else{
					//该位置节点不可放置
					//则将节点放置到同行的下一位置
					//越界问题会在下一次循环中解决
					cols[row] ++;
				}
			}else{
				//越界:行越界和列越界
				//行越界：已放置完所有行的皇后，一个解生成
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
				//列越界：回溯到上一行，皇后往后一列位置放置
				//|x|x|x|o| |
				//|x|x|x|x|x|*
				//行越界：找到一个解，但是仍需要将皇后放在最后一行其他位置进行判断
				//|o| | | | |
				//|x|x|o|-|-|
				row --;
				cols[row] ++;
			}
		}
	}
	
	//递归实现
	public void placeNQueen_recursion(int queenNum){
		solutionNum = 0;
		int[] cols = new int[queenNum + 2];
		placeQueen(false,1,cols,queenNum);
	}
	
	public void placeQueen(boolean isSym,int row, int[] cols, int queenNum){
		if(row > queenNum){
			//行越界：找到一个解
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
			//对该行的所有位置进行遍历
			for(int col = 1; col <= queenNum; col ++){
				//放入的元素的位置(row,col)
				if(isAllowPosition(row,col,cols)){
					//如果位置可放，则放置，并找下一行的皇后
					cols[row] = col;
					placeQueen(isSym,row + 1,cols,queenNum);
				}
				//位置不可放，放下一列
			}
		}
	}
	
	//递归--利用对称将时间减半
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
	
	//[TODO]位运算实现
	public void placeNQueen_binary(int queenNum){
		solutionNum = 0;
		//queenNumSign利用二进制的方式记录皇后的个数
		//5个皇后则为：10000 - 1 = 000...011111
		long queenNumSigns = (1 << queenNum) - 1;
//		System.out.println("queenNumSigns = " + queenNumSigns);
		int[] cols = new int[queenNum + 2];
		placeBinary(false,cols,1,queenNumSigns,0,0,0);
	}
	
	public void placeNQueen_binary_symmetry(int queenNum){
		solutionNum = 0;
		//queenNumSign利用二进制的方式记录皇后的个数
		//5个皇后则为：10000 - 1 = 000...011111
		long queenNumSigns = (1 << queenNum) - 1;
//		System.out.println("queenNumSigns = " + queenNumSigns);
		int[] cols = new int[queenNum + 2];
		
		//queenNum / 2仅需要摆放一半的皇后
		//5皇后 000011
		long allowPlaces = (1 << (queenNum >> 1)) - 1;
		while(allowPlaces != 0){
			//取出最右边的一位
			long rightPlace = allowPlaces & (-allowPlaces);
			allowPlaces -= rightPlace;
			cols[1] = queenNum - getNumOfZeroInRight(rightPlace);
			placeBinary(true,cols,2,queenNumSigns,rightPlace,rightPlace >> 1,rightPlace << 1);
		}
		
		if((queenNum & 1) != 0){
			//奇数个皇后，中间位置需要判断
			cols[1] = queenNum >> 1 + 1;
			long rightPlace = 1 << (queenNum >> 1);
			placeBinary(false,cols,2,queenNumSigns,rightPlace,rightPlace >> 1,rightPlace << 1);
		}
	}
	
	//binary方法是从最又开始放置的
	//horizontalLine,leftDiagonal,rightDiagonal用1标记不可放置，0表示可放置
	//allowPlaces中1表示可放置，0表示不可放置
	public void placeBinary(boolean isSym,int[] cols,int row,long queenNumSigns,long horizontalLine,long leftDiagonal,long rightDiagonal){
		if(horizontalLine != queenNumSigns){
			//allowPlaces上的1表示该行上可放的位置，如果为0，则表示该行以无可放置位置
			long allowPlaces = queenNumSigns & (~(horizontalLine | leftDiagonal | rightDiagonal));
			//循环到allowPlaces 上的1 都变成0
			while(allowPlaces != 0){
				//有可放位置
				//获取最右边的位置
				//-allowPlaces = ~allowPlaces + 1
				long rightPlace = allowPlaces & (-allowPlaces);
				int queenNum = cols.length - 2;
				int col = queenNum - getNumOfZeroInRight(rightPlace);
//				System.out.println("col = " + col);
				cols[row] = col;
				//减去最右边的1
				allowPlaces -= rightPlace;
				placeBinary(isSym,cols,row+1,queenNumSigns,horizontalLine + rightPlace,(leftDiagonal + rightPlace) >> 1, (rightDiagonal + rightPlace)<<1);
			}
		}else{
			//horizontalLine的所有位都为1，即找到了一个成功的布局，回溯 
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
		//在java中，这些数据大小都是固定的，不会随系统的变化而变化，long长度都是64位
		return Long.SIZE / 8;
	}
	//对称性使用
	//通过在以上的三种实现中添加一个条件判断即可实现
	
	//其他函数

	public static void setting(){
		Scanner input = new Scanner(System.in);
		String option = "";
		while(!option.equals("y") && !option.equals("n") ){
			System.out.println("是否需要显示?[Y/N]");
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
		//对已放置皇后的位置进行遍历
		//新放入元素(row,col),以确认的个数为row - 1
		for (int queenIndex = 1; queenIndex < row; queenIndex ++){
			//判断是否在同一斜线或者同列
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
			//如果是奇数，则最后一位便是1
			counter = 0;
		}else{
			if((binaryNum & 0xffffffff) == 0){
				//1在左边32位内
				binaryNum >>= 32;
				counter += 32;
			}
			if ((binaryNum & 0xffff) == 0) 
			{  
				//1在左边16位内
				binaryNum >>= 16;  
				counter += 16;
			}
			if ((binaryNum & 0xff) == 0) 
			{  
				//1在左边8位内
			    binaryNum >>= 8;  
			    counter += 8;
			}
			if ((binaryNum & 0xf) == 0) 
			{  
				//1在左边4位内
			    binaryNum >>= 4;
			    counter += 4;
			}
			if ((binaryNum & 0x3) == 0) 
			{  
				//1在左边2位内
			    binaryNum >>= 2;
			    counter += 2;
			}
			if((binaryNum & 0x1) == 0) {
				//1在左边1位内
			    counter += 1;
			}
		}
		return counter;
	}
	
	//[TODO]判断是否为0
	public static void showResult(long solutionNum){
		System.out.println("------------------------------------------");
		System.out.println("一共有[" + solutionNum + "]种摆放方式");
		System.out.println("==========================================");
	}
	
	public void showQueenPositions(int[] cols,int queenNum,long solutionNo){
		if(!judgeIsRight(cols,queenNum)){
			System.out.println("算法有误！");
		}
		System.out.println("-----------------[第" + solutionNo + "个解]----------------");
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
			System.out.println("算法有误！");
		}
		int time = 1;
		if(isSym){
			time ++;
		}
		while(time-- > 0){
			System.out.println("-----------------[第" + solutionNo + "个解]----------------");
			for(int row = 1; row <= queenNum; row ++){
				int queenPlace = cols[row];
				if(time == 1){
					// |<--—>|X|*|*|X|<--—>|
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
		System.out.println("-------测试布局------");
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
