import java.util.Scanner;

/*
 * 规定：
 * 1、横列均使用从1开始
 */
public class NQueen {
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
				System.out.println("1. 迭代求解     2. 递归求解      3. 位运算求解      4. 设置      5. 退出");
				int option = input.nextInt();
				switch(option){
				case 1:{
					System.out.println("请输入皇后数：");
					int queenNum = input.nextInt();
					int solutionNum = nqueen.placeNQueen_iteration(queenNum);
					showResult(solutionNum);
					break;
				}
				case 2:{
					System.out.println("请输入皇后数：");
					int queenNum = input.nextInt();
					int solutionNum = nqueen.placeNQueen_recursion(queenNum);
					showResult(solutionNum);
					break;
				}
				case 3:{
					System.out.println("请输入皇后数：");
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

	//非递归实现
	public int placeNQueen_iteration(int queenNum){
		int[] cols = new int[queenNum+1];
		int solutionNum = 0;
		int row = 1;
		cols[row] = 1;
		//对所有行进行遍历，也就是将每个皇后逐行放置
		//好处：无需再比较同行位置
		while(row > 0){
			if(row <= queenNum && cols[row] <= queenNum ){

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
					showQueenPositions(cols,queenNum);
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
		return solutionNum;
	}
	//递归实现
	
	public int placeNQueen_recursion(int queenNum){
		int solutionNum = 0;
		int[] cols = new int[queenNum + 1];
		return placeQueen(1,cols,queenNum,solutionNum);
	}
	
	public int placeQueen(int row, int[] cols, int queenNum,int solutionNum){
		if(row > queenNum){
			//行越界：找到一个解
			showQueenPositions(cols,queenNum);
			solutionNum ++;
		}else{
			//对该行的所有位置进行遍历
			for(int col = 1; col <= queenNum; col ++){
				if(isAllowPosition(row,col,cols)){
					//如果位置可放，则放置，并找下一行的皇后
					cols[row] = col;
					solutionNum = placeQueen(row + 1,cols,queenNum,solutionNum);
				}
				//位置不可放，放下一列
			}
		}
		return solutionNum;
	}
	
	//[TODO]位运算实现
	public int placeNQueen_binary(int queenNum){
		return 0;
	}
	//对称性使用
	//通过在以上的三种实现中添加一个条件判断即可实现
	
	//其他函数

	public static void setting(){
		
	}
	public boolean isAllowPosition(int row,int col,int[] cols){
		//对已放置皇后的位置进行遍历
		for (int queenIndex = 1; queenIndex <= row; queenIndex ++){
			//判断是否在同一斜线或者同列
			if ( ( Math.abs(col-queenIndex) == Math.abs(cols[queenIndex]-cols[row]) )
					||(cols[queenIndex] == cols[row])){
				return false;
			}
		}
		return true;
	}
	
	//[TODO]判断是否为0
	public static void showResult(int solutionNum){
		System.out.println("一共有：" + solutionNum + "个摆放方式");
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
