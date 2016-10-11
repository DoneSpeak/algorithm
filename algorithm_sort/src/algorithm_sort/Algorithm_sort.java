package algorithm_sort;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class Algorithm_sort {
	
	//排序算法选项
    public final int SELECTSORTTYPE = 0;
    public final int BUBBLESORTTYPE = 1;
    public final int MERGESORTTYPE = 2;
    public final int QUICHSORTTYPE = 3;
    public final int INSERTSORTTYPE = 4;
    public final int ALLTYPES = 5;
    
    //其他常数
	public static int INITSIZE = 10;
	public static int TIMES = 20;
	public static int TYPENUM = 5;
	public static int MULTIPLE10 = 10;
	public static int MULTIPLE20000 = 20000;
	public static int SCALEALLLEVEL = 10000 * 10; 
	
	public static void main(String[] args){
		String endOrNot = "N";
		while(endOrNot.equalsIgnoreCase("N")){
			int type = -1;
			while(type < 0 || type > 5){
				System.out.println("请选择排序算法：");
				System.out.println("0.选择排序    1.冒泡排序    2.归并排序    3.快速排序    4.插入排序    5.全部");
				Scanner reader = new Scanner(System.in);
				type = reader.nextInt();
			}
			Algorithm_sort sorter = new Algorithm_sort();
			ArrayList<ArrayList<String>> result = sorter.excuteSortAlgorithmAndGetResultArray(type);
			
			sorter.showResult(result);
			
			Date now = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			String filePath = ".\\sortResult" + dateFormat.format(now) + ".xls";
			sorter.exportToExcel("algorithm_sort",result,filePath);
			try {
				Runtime.getRuntime().exec("cmd  /c  start " + filePath);
			} catch (IOException e) {
				System.out.println("文件打开出错！");
				e.printStackTrace();
			}  
			System.out.println("是否结束？[Y/N]");
			Scanner reader = new Scanner(System.in);
			endOrNot = reader.nextLine();
		}
		System.out.println("谢谢使用");
	}
	

	//根据排序算法选项，选择合适的排序算法进行计算
	//返回包含有各个算法不同规模的20次计算的平均值ArrayList
    public ArrayList<ArrayList<String>> excuteSortAlgorithmAndGetResultArray(int chooseType)
    {
    	//根据排序算法选项，设置typeStart和typeEnd
    	int typeStart = 0;
    	int typeEnd = TYPENUM;
    	switch(chooseType){
	    	case SELECTSORTTYPE:{
	    		typeStart = SELECTSORTTYPE;
	    		typeEnd = SELECTSORTTYPE + 1;
	    		break;
	    	}
	    	case BUBBLESORTTYPE:{
	    		typeStart = BUBBLESORTTYPE;
	    		typeEnd = BUBBLESORTTYPE + 1;
	    		break;
	    	}
	    	case MERGESORTTYPE:{
	    		typeStart = MERGESORTTYPE;
	    		typeEnd = MERGESORTTYPE + 1;
	    		break;
	    	}
	    	case QUICHSORTTYPE:{
	    		typeStart = QUICHSORTTYPE;
	    		typeEnd = QUICHSORTTYPE + 1;
	    		break;
	    	}
	    	case INSERTSORTTYPE:{
	    		typeStart = INSERTSORTTYPE;
	    		typeEnd = INSERTSORTTYPE;
	    		break;
	    	}
	    	case ALLTYPES:{
	    		break;
	    	}
    	}

        ArrayList<ArrayList<String>> resultArray = new ArrayList<ArrayList<String>>();
        ArrayList<String> oneType = new ArrayList<String>();

        //添加表头信息的ArrayList
        resultArray.add(getHeadArray());
        
        //计算时间
        //排序算法选项循环
        for (int type = typeStart; type < typeEnd; type++)
        {
            int size = INITSIZE;
            String typeString = addSortType(type, oneType);
            System.out.println(typeString + ":");
            //规模递增循环
            while(size <= SCALEALLLEVEL)
            {
                double timeSpendedSum = 0.0;
            	System.out.println("规模：" + size);
            	System.out.printf("耗时(ms)：");
            	//测试20次循环
                for (int i = 0; i < TIMES; i++)
                {
                    int[] numbers = createRandomNumberArray(0, size * 100, size);
                    
                    long startTime=System.currentTimeMillis(); 
                    //排序
                    sortExecute(type, numbers, size);
                    long endTime=System.currentTimeMillis();
                    //计算所用时间/ms
                    long timeUsed = endTime - startTime;
                    timeSpendedSum += timeUsed;
                    System.out.printf("%s ", timeUsed);
                    numbers = null;
                }
                double average = timeSpendedSum / TIMES;
                oneType.add(average + "");
                System.out.println("\n平均时间：(ms)" + average);
                //改变规模
                size = getSize(size);
            }
            resultArray.add(oneType);
            oneType = new ArrayList<String>();
        }
        return resultArray;
    }
    
    //创建规模为size的int[]随机数组
    public int[] createRandomNumberArray(int min,int max,int size){
		int[] numbers = new int[size];
		Random random = new Random();
		for(int i = 0; i < size; i ++){
			int randNum = random.nextInt(max)%(max-min+1) + min;
			numbers[i] = randNum;
		}
		return numbers;
	}
    
    //获取表头信息的ArrayList
    public ArrayList<String> getHeadArray()
    {
        ArrayList<String> headArray = new ArrayList<String>();
        headArray.add("规模");
        int size = INITSIZE;
        while(size <= SCALEALLLEVEL)
        {
            headArray.add(size + "");
            size = getSize(size);
        }
        return headArray;
    }
    
    //计算规模
    public int getSize(int size){
    	if(size < 10000){
        	size *= MULTIPLE10;
        }else{
        	if(size == 10000){
        		size *= 2;
        	}else{
            	size += MULTIPLE20000;        		
        	}
        }
    	return size;
    }
    
    //在控制台输出结果
    public void showResult(ArrayList<ArrayList<String>> result){
    	int cellNum = result.get(0).size();
    	printfLine(cellNum);
    	for(int row = 0; row < result.size(); row ++){
    		ArrayList<String> oneType = result.get(row);
    		System.out.printf("|%8s|", oneType.get(0));
    		for(int cell = 1; cell < oneType.size(); cell ++){
    			System.out.printf("%13s|",oneType.get(cell));
    		}
    		System.out.println();
    		printfLine(cellNum);
    	}
    }
    
    //打印表格分隔横线
    public void printfLine(int cellNum){

		System.out.printf("----------");
    	for(int i = 0; i < cellNum; i ++){
    		System.out.printf("-------------");
    	}
    	System.out.println();
    }
    
    //添加排序类型到oneType数组
    public String addSortType(int type, ArrayList<String> oneType)
    {
    	String typeString = "";
        switch (type)
        {
            case SELECTSORTTYPE:
                {
                    typeString = "选择排序/ms";
                    break;
                }
            case BUBBLESORTTYPE:
                {
                    typeString = "冒泡排序/ms";
                    break;
                }
            case MERGESORTTYPE:
                {
                    typeString = "合并排序/ms";
                    break;
                }
            case QUICHSORTTYPE:
                {
                    typeString = "快速排序/ms";
                    break;
                }
            case INSERTSORTTYPE:
                {
                    typeString = "插入排序/ms";
                    break;
                }
        }
        oneType.add(typeString);
        return typeString;
    }

    //排序综合方法
    public void sortExecute(int type, int[] numbers, int size)
    {
        switch (type)
        {
            case SELECTSORTTYPE:
                {
                    selectSort(numbers, size);
                    break;
                }
            case BUBBLESORTTYPE:
                {
                    bubbleSort(numbers, size);
                    break;
                }
            case MERGESORTTYPE:
                {
                    mergeSort(numbers, size);
                    break;
                }
            case QUICHSORTTYPE:
                {
                    quickSort(numbers, size);
                    break;
                }
            case INSERTSORTTYPE:
                {
                    insertSort(numbers, size);
                    break;
                }
        }
    }

    //插入排序
    public void insertSort(int[] num, int n)
    {
        //小部分有序化
        for (int pointer = 1; pointer < n; pointer++)
        {
            //小者前移
            for (int scanner = pointer - 1; scanner >= 0 && num[scanner] > num[scanner + 1]; scanner--)
            {
                int temp = num[scanner];
                num[scanner] = num[scanner + 1];
                num[scanner + 1] = temp;
            }
        }
    }

    //选择排序
    public void selectSort(int[] num, int n)
    {
        for (int pointer = 0; pointer < n; pointer++)
        {
            //找到小的
            int minIndex = pointer;
            for (int scanner = pointer + 1; scanner < n; scanner++)
            {
                if (num[minIndex] > num[scanner])
                {
                    minIndex = scanner;
                }
            }
            int temp = num[pointer];
            num[pointer] = num[minIndex];
            num[minIndex] = temp;
        }
    }

    //冒泡排序
    public void bubbleSort(int[] num, int n)
    {
        //大者后移
        int firstOneInBigList;
        int biggerOneIndex;
        int scanner;

        biggerOneIndex = n;
        while (biggerOneIndex > 0)
        {
        	//此操作可以使得位于数组后端的有序数组无需再排序
            firstOneInBigList = biggerOneIndex;
            biggerOneIndex = 0;
            for (scanner = 1; scanner < firstOneInBigList; scanner++)
            {
                if (num[scanner - 1] > num[scanner])
                {
                    int temp = num[scanner - 1];
                    num[scanner - 1] = num[scanner];
                    num[scanner] = temp;
                    //biggerOneIndex记录排好序的大数值中最小的一个
                    biggerOneIndex = scanner;
                }
            }
        }
    }
    
  //冒泡排序
    public void bubbleSortClassic(int[] num, int n)
    {
        //大者后移
        for (int  i = 0; i < n; i ++)
        {
            for (int scanner = 0; scanner < n - 1 - i; scanner++)
            {
                if (num[scanner] > num[scanner + 1])
                {
                    int temp = num[scanner - 1];
                    num[scanner - 1] = num[scanner];
                    num[scanner] = temp;
                }
            }
        }
    }

    //快速排序
    public void quickSort(int[] num, int size)
    {
        quickSort(num, 0, size - 1);
    }

    public void quickSort(int[] num, int left, int right)
    {
        if (left <= right)
        {
            int mid = partion(num, left, right);
            quickSort(num, left, mid - 1);
            quickSort(num, mid + 1, right);
        }
    }

    public int partion(int[] num, int left, int right)
    {
        int holeValue = num[left];
        int holeIndex = left;
        while (left != right)
        {
            //在右边找一个小的数,这里必须从右边开始，以保证最后一次找到的是小于holeValue的值
            while (num[right] >= holeValue && left < right)
            {
                right--;
            }
            //在左边找一个大的数
            while (num[left] <= holeValue && left < right)
            {
                left++;
            }
            if (left < right)
            {
                int temp = num[left];
                num[left] = num[right];
                num[right] = temp;
            }
        }
        //跳出时，left == right
        num[holeIndex] = num[left];
        num[left] = holeValue;

        return left;
    }

    //归并排序
    //归并 -- 合并两个有序的数组
    public void mergeSort(int[] num, int size)
    {
        int[] store = new int[size];
        mergeSort(num, 0, size - 1, size, store);
    }

    public void mergeSort(int[] num, int first, int last, int size,int[] store)
    {
        if (first < last)
        {
            int mid = (first + last) / 2;
            //左半边排序
            mergeSort(num, first, mid, size,store);
            //右半边排序
            mergeSort(num, mid + 1, last, size, store);
            //合并两个有序数组
            mergeSort(num, first, mid, last, size, store);
        }
    }

    //此函数可以设想为数组两边已经是有序，只要将两边不断选择小的合并就可以了
    public void mergeSort(int[] num, int first, int mid, int last, int size,int[] store)
    {
        int left = first;
        int right = mid + 1;
        int storeIndex = 0;

        while (left <= mid && right <= last)
        {
            if (num[left] < num[right])
            {
                store[storeIndex++] = num[left++];
            }
            else
            {
                store[storeIndex++] = num[right++];
            }
        }

        //归并剩余的
        while (left <= mid)
        {
            store[storeIndex++] = num[left++];
        }
        while (right <= last)
        {
            store[storeIndex++] = num[right++];
        }

        //还原到原来数组
        for (left = 0; left < storeIndex; left++)
        {
            num[first + left] = store[left];
        }
    }
    
    //将结果输出到excel文件
    //https://poi.apache.org/spreadsheet/
    //如果拓展名为xls，则需要用HSSF
    //如果拓展名为xlsx，则需要用XSSF
    public void exportToExcel(String sheetTitle, ArrayList<ArrayList<String>> result, String filePath){
    	// 声明一个工作薄  
        HSSFWorkbook workbook = new HSSFWorkbook();  
        // 生成一个表格  
        HSSFSheet sheet = workbook.createSheet(sheetTitle);
        // 设置表格默认列宽度为15个字节  
        sheet.setDefaultColumnWidth((short) 15);
        
    	for(int rowNum = 0; rowNum < result.size(); rowNum ++){
    		ArrayList<String> oneType = result.get(rowNum);
    		HSSFRow row = sheet.createRow(rowNum);
    		for(int colNum = 0; colNum < oneType.size(); colNum ++){
    			HSSFCell cell = row.createCell(colNum);
                HSSFRichTextString text = new HSSFRichTextString(oneType.get(colNum));  
                cell.setCellValue(text);
    		}
    	}
    	OutputStream out = null;
		try  
		{  
			out = new FileOutputStream(filePath); 
		    workbook.write(out);
		}  
		catch (IOException e)  
		{  
		    e.printStackTrace();  
		}finally{
			if(out != null){
				try {
					workbook.close();
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
    }
}
