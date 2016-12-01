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
//		��ʼʱ����·��ͨ��������Ϊ0
		for(int start = 1; start <= nodeNum; start ++){
			for(int end = 1; end <= nodeNum; end ++){
				flow[start][end] = 0; 
			}
		}
//		��¼��������
		int iterTimes = 0;
//		Ѱ������·��--֪���������в��ٴ�������·��Ϊֹ
		while(true){
			iterTimes ++;
//			ÿ�ζԲ�����Ѱ������·�����൱�ڶ�һ��ȫ�µ�����ͼѰ�����·��
			for(int i = 1; i <= nodeNum; i ++){
				visited[i] = false;
			}
//			����Դ�ڵ����һ���ڵ�Ϊ-1����ʾû����һ���ڵ�
			preNode[src] = -1;
//			����BFS�Բ�����Ѱ�����·����Ҳ����Ѱ������·����Ҳ�����ж��Ƿ񻹴�������·��
			boolean stillHasPath = BFS(src, des);
//			boolean stillHasPath = DFS(src, des);
			if(!stillHasPath){
//				����������·��ʱ�������Բ���������Ѱ������·��
				break;
			}
			
//			�������ӵ�����
//			���ҵ�·���е�������Ȼ�����бߵ���������С���Ǹ���·����������С��������
//			�������С�� - ���ҵ�������·�������и�����������С�����жϿ���֪������С�������Ǹ�����·���������
//			���������ָ���ǲ�������������Ҳ���� capacity - flow
			int flowAmount = Integer.MAX_VALUE;
//			����preNodeȷ��·��
			for(int i = des; preNode[i] >= src; i = preNode[i]){
				flowAmount = Math.min(flowAmount, capacity[preNode[i]][i] - flow[preNode[i]][i]);
			}
//			��������
			for(int i = des; preNode[i] >= src; i = preNode[i]){
				flow[preNode[i]][i] += flowAmount;
//				����������Ҫ������
				flow[i][preNode[i]] -= flowAmount;
			}
//			�������������һ��
			maxFlow += flowAmount;
//			�����м���̴�ӡ����
			printFlow(src, des, iterTimes, maxFlow, flow);
		}
		return maxFlow;
	}
	
//	��ӡ��ǰ�����ֲ����
	public void printFlow(int src, int des, int iterTimes, int maxFlow, int[][] flow){
		System.out.println("**************** ��[" + iterTimes + "]�ε��� ******************");
		
		System.out.println("��ǰ�������" + maxFlow);
		System.out.println("��ǰ·����");
		int[] next = new int[nodeNum + 1];
		for(int i = des; preNode[i] >= src;  i = preNode[i]){
			next[preNode[i]] = i;
		}
		next[des] = Integer.MAX_VALUE; 
		for(int i = src; i <= des; i = next[i]){
			System.out.print(" [" + i + "] ");
		}
		System.out.println();
		System.out.println("·������������");
		for(int start = 1; start <= nodeNum; start ++){
			for(int end = 1; end <= nodeNum; end ++){
				System.out.print(" [" + flow[start][end] + "] ");
			}
			System.out.println();
		}
	}
	
	public boolean BFS(int src, int des){
//		���ö��н���BFS
		Queue<Integer> queue = new LinkedList<Integer>();
		
		queue.add(src);
		visited[src] = true;
		
		while(!queue.isEmpty()){
			
			int start = queue.poll();
			
			for( int end = 1; end <= nodeNum; end ++){
//				���������ɻ��������ı߹���
				if( !visited[end] && (capacity[start][end] - flow[start][end] > 0) ){
					queue.add(end);
					visited[end] = true;
					preNode[end] = start;
				}
			}
		}
//		�����Ƿ��ҵ�����·����Ҳ����visited[des]���Ϊtrue�������ҵ�����·��
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
