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
	
	//�����㷨ѡ��
    public final int SELECTSORTTYPE = 0;
    public final int BUBBLESORTTYPE = 1;
    public final int MERGESORTTYPE = 2;
    public final int QUICHSORTTYPE = 3;
    public final int INSERTSORTTYPE = 4;
    public final int ALLTYPES = 5;
    
    //��������
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
				System.out.println("��ѡ�������㷨��");
				System.out.println("0.ѡ������    1.ð������    2.�鲢����    3.��������    4.��������    5.ȫ��");
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
				System.out.println("�ļ��򿪳���");
				e.printStackTrace();
			}  
			System.out.println("�Ƿ������[Y/N]");
			Scanner reader = new Scanner(System.in);
			endOrNot = reader.nextLine();
		}
		System.out.println("ллʹ��");
	}
	

	//���������㷨ѡ�ѡ����ʵ������㷨���м���
	//���ذ����и����㷨��ͬ��ģ��20�μ����ƽ��ֵArrayList
    public ArrayList<ArrayList<String>> excuteSortAlgorithmAndGetResultArray(int chooseType)
    {
    	//���������㷨ѡ�����typeStart��typeEnd
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

        //��ӱ�ͷ��Ϣ��ArrayList
        resultArray.add(getHeadArray());
        
        //����ʱ��
        //�����㷨ѡ��ѭ��
        for (int type = typeStart; type < typeEnd; type++)
        {
            int size = INITSIZE;
            String typeString = addSortType(type, oneType);
            System.out.println(typeString + ":");
            //��ģ����ѭ��
            while(size <= SCALEALLLEVEL)
            {
                double timeSpendedSum = 0.0;
            	System.out.println("��ģ��" + size);
            	System.out.printf("��ʱ(ms)��");
            	//����20��ѭ��
                for (int i = 0; i < TIMES; i++)
                {
                    int[] numbers = createRandomNumberArray(0, size * 100, size);
                    
                    long startTime=System.currentTimeMillis(); 
                    //����
                    sortExecute(type, numbers, size);
                    long endTime=System.currentTimeMillis();
                    //��������ʱ��/ms
                    long timeUsed = endTime - startTime;
                    timeSpendedSum += timeUsed;
                    System.out.printf("%s ", timeUsed);
                    numbers = null;
                }
                double average = timeSpendedSum / TIMES;
                oneType.add(average + "");
                System.out.println("\nƽ��ʱ�䣺(ms)" + average);
                //�ı��ģ
                size = getSize(size);
            }
            resultArray.add(oneType);
            oneType = new ArrayList<String>();
        }
        return resultArray;
    }
    
    //������ģΪsize��int[]�������
    public int[] createRandomNumberArray(int min,int max,int size){
		int[] numbers = new int[size];
		Random random = new Random();
		for(int i = 0; i < size; i ++){
			int randNum = random.nextInt(max)%(max-min+1) + min;
			numbers[i] = randNum;
		}
		return numbers;
	}
    
    //��ȡ��ͷ��Ϣ��ArrayList
    public ArrayList<String> getHeadArray()
    {
        ArrayList<String> headArray = new ArrayList<String>();
        headArray.add("��ģ");
        int size = INITSIZE;
        while(size <= SCALEALLLEVEL)
        {
            headArray.add(size + "");
            size = getSize(size);
        }
        return headArray;
    }
    
    //�����ģ
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
    
    //�ڿ���̨������
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
    
    //��ӡ���ָ�����
    public void printfLine(int cellNum){

		System.out.printf("----------");
    	for(int i = 0; i < cellNum; i ++){
    		System.out.printf("-------------");
    	}
    	System.out.println();
    }
    
    //����������͵�oneType����
    public String addSortType(int type, ArrayList<String> oneType)
    {
    	String typeString = "";
        switch (type)
        {
            case SELECTSORTTYPE:
                {
                    typeString = "ѡ������/ms";
                    break;
                }
            case BUBBLESORTTYPE:
                {
                    typeString = "ð������/ms";
                    break;
                }
            case MERGESORTTYPE:
                {
                    typeString = "�ϲ�����/ms";
                    break;
                }
            case QUICHSORTTYPE:
                {
                    typeString = "��������/ms";
                    break;
                }
            case INSERTSORTTYPE:
                {
                    typeString = "��������/ms";
                    break;
                }
        }
        oneType.add(typeString);
        return typeString;
    }

    //�����ۺϷ���
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

    //��������
    public void insertSort(int[] num, int n)
    {
        //С��������
        for (int pointer = 1; pointer < n; pointer++)
        {
            //С��ǰ��
            for (int scanner = pointer - 1; scanner >= 0 && num[scanner] > num[scanner + 1]; scanner--)
            {
                int temp = num[scanner];
                num[scanner] = num[scanner + 1];
                num[scanner + 1] = temp;
            }
        }
    }

    //ѡ������
    public void selectSort(int[] num, int n)
    {
        for (int pointer = 0; pointer < n; pointer++)
        {
            //�ҵ�С��
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

    //ð������
    public void bubbleSort(int[] num, int n)
    {
        //���ߺ���
        int firstOneInBigList;
        int biggerOneIndex;
        int scanner;

        biggerOneIndex = n;
        while (biggerOneIndex > 0)
        {
        	//�˲�������ʹ��λ�������˵�������������������
            firstOneInBigList = biggerOneIndex;
            biggerOneIndex = 0;
            for (scanner = 1; scanner < firstOneInBigList; scanner++)
            {
                if (num[scanner - 1] > num[scanner])
                {
                    int temp = num[scanner - 1];
                    num[scanner - 1] = num[scanner];
                    num[scanner] = temp;
                    //biggerOneIndex��¼�ź���Ĵ���ֵ����С��һ��
                    biggerOneIndex = scanner;
                }
            }
        }
    }
    
  //ð������
    public void bubbleSortClassic(int[] num, int n)
    {
        //���ߺ���
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

    //��������
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
            //���ұ���һ��С����,���������ұ߿�ʼ���Ա�֤���һ���ҵ�����С��holeValue��ֵ
            while (num[right] >= holeValue && left < right)
            {
                right--;
            }
            //�������һ�������
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
        //����ʱ��left == right
        num[holeIndex] = num[left];
        num[left] = holeValue;

        return left;
    }

    //�鲢����
    //�鲢 -- �ϲ��������������
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
            //��������
            mergeSort(num, first, mid, size,store);
            //�Ұ������
            mergeSort(num, mid + 1, last, size, store);
            //�ϲ�������������
            mergeSort(num, first, mid, last, size, store);
        }
    }

    //�˺�����������Ϊ���������Ѿ�������ֻҪ�����߲���ѡ��С�ĺϲ��Ϳ�����
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

        //�鲢ʣ���
        while (left <= mid)
        {
            store[storeIndex++] = num[left++];
        }
        while (right <= last)
        {
            store[storeIndex++] = num[right++];
        }

        //��ԭ��ԭ������
        for (left = 0; left < storeIndex; left++)
        {
            num[first + left] = store[left];
        }
    }
    
    //����������excel�ļ�
    //https://poi.apache.org/spreadsheet/
    //�����չ��Ϊxls������Ҫ��HSSF
    //�����չ��Ϊxlsx������Ҫ��XSSF
    public void exportToExcel(String sheetTitle, ArrayList<ArrayList<String>> result, String filePath){
    	// ����һ��������  
        HSSFWorkbook workbook = new HSSFWorkbook();  
        // ����һ�����  
        HSSFSheet sheet = workbook.createSheet(sheetTitle);
        // ���ñ��Ĭ���п��Ϊ15���ֽ�  
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
