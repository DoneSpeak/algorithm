import java.util.LinkedList;
import java.util.Queue;

public class MaxFlowProblem {
	private int nodeNum = 6;
	private int egdeNum = 8;
	private int[][] capacity ={
			{0,0,0,0,0,0,0},
			{0,0,6,0,7,0,0},
			{0,0,0,4,0,4,0},
			{0,0,0,0,0,0,4},
			{0,0,0,3,0,2,0},
			{0,0,0,0,0,0,8},
			{0,0,0,0,0,0,0}
	};
	private int[][] flow;
	private int preNode[];
	private boolean visited[];
	
	public MaxFlowProblem(){
		this.flow = new int[nodeNum + 1][nodeNum + 1];
		for(int i = 1; i <= nodeNum; i ++){
			this.flow[i] = new int[nodeNum + 1];
		}
		
		visited = new boolean[nodeNum + 1];
		
		preNode = new int[nodeNum + 1];
	}
	
	public int getMaxFlow(int src, int des){
		int maxFlow = 0;
//		开始时所有路径通过的流量为0
		for(int start = 1; start <= nodeNum; start ++){
			for(int end = 1; end <= nodeNum; end ++){
				flow[start][end] = 0; 
			}
		}
//		记录迭代次数
		int iterTimes = 0;
//		寻找增广路径--知道残留网中不再存在增广路径为止
		while(true){
			iterTimes ++;
//			每次对残留网寻找增广路径都相当于对一个全新的网络图寻找最短路径
			for(int i = 1; i <= nodeNum; i ++){
				visited[i] = false;
			}
//			设置源节点的上一个节点为-1，表示没有上一个节点
			preNode[src] = -1;
//			利用BFS对残留网寻找最短路径，也就是寻找增广路径，也就是判断是否还存在增广路径
			boolean stillHasPath = BFS(src, des);
//			boolean stillHasPath = DFS(src, des);
			if(!stillHasPath){
//				不再有增广路径时，结束对残留网继续寻找增广路径
				break;
			}
			
//			计算增加的流量
//			所找到路径中的流量必然是所有边的中容量最小的那个，路径流量受最小容量限制
//			最大流最小割 - 对找到的增广路径进行切割，按照最大流最小割来判断可以知道，最小容量就是该增广路径的最大流
//			这里的容量指的是残留网的流量，也就是 capacity - flow
			int flowAmount = Integer.MAX_VALUE;
//			利用preNode确定路径
			for(int i = des; preNode[i] >= src; i = preNode[i]){
				flowAmount = Math.min(flowAmount, capacity[preNode[i]][i] - flow[preNode[i]][i]);
			}
//			更新流量
			for(int i = des; preNode[i] >= src; i = preNode[i]){
				flow[preNode[i]][i] += flowAmount;
//				反向流量需要做减法
				flow[i][preNode[i]] -= flowAmount;
			}
//			积累最大流增加一次
			maxFlow += flowAmount;
//			将此中间过程打印出来
			printFlow(src, des, iterTimes, maxFlow, flow);
		}
		return maxFlow;
	}
	
//	打印当前流量分布情况
	public void printFlow(int src, int des, int iterTimes, int maxFlow, int[][] flow){
		System.out.println("**************** 第[" + iterTimes + "]次迭代 ******************");
		
		System.out.println("当前最大流：" + maxFlow);
		System.out.println("当前路径：");
		int[] next = new int[nodeNum + 1];
		for(int i = des; preNode[i] >= src;  i = preNode[i]){
			next[preNode[i]] = i;
		}
		next[des] = Integer.MAX_VALUE; 
		for(int i = src; i <= des; i = next[i]){
			System.out.print(" [" + i + "] ");
		}
		System.out.println();
		System.out.println("路径积累流量：");
		for(int start = 1; start <= nodeNum; start ++){
			for(int end = 1; end <= nodeNum; end ++){
				System.out.print(" [" + flow[start][end] + "] ");
			}
			System.out.println();
		}
	}
	
	public boolean BFS(int src, int des){
//		利用队列进行BFS
		Queue<Integer> queue = new LinkedList<Integer>();
		
		queue.add(src);
		visited[src] = true;
		
		while(!queue.isEmpty()){
			
			int start = queue.poll();
			
			for( int end = 1; end <= nodeNum; end ++){
//				残留网是由还有容量的边构成
				if( !visited[end] && (capacity[start][end] - flow[start][end] > 0) ){
					queue.add(end);
					visited[end] = true;
					preNode[end] = start;
				}
			}
		}
//		返回是否找到增广路径，也就是visited[des]如果为true，就是找到增广路径
		return visited[des];
	}
	
	public boolean DFS(int src, int des){
		visited[src] = true;
		for(int start = 1; start <= nodeNum; start ++){
			if(!visited[start] && (capacity[src][start] - flow[src][start] > 0)){
				preNode[start] = src;
				visited[start] = true;
				DFS(start, des);
			}
				
		}
		return visited[des];
	}
	
	public static void main(String args[]){
		new MaxFlowProblem().getMaxFlow(1,6);
	}
}
