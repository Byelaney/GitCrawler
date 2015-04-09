package helper;

public class QuickSort {
//	public static int getMiddle(String[] list, int low, int high) {  
//        String tmp = list[low];    //数组的第一个作为中轴  (pivlot)
//        while (low < high) {  
//            while (low < high && compareDate(list[high],tmp) > 0) {  
//                high--;  
//            }  
//            list[low] = list[high];   //比中轴小的记录移到低端  
//            while (low < high && compareDate(list[low],tmp) < 0) {  
//                low++;  
//            }  
//            list[high] = list[low];   //比中轴大的记录移到高端  
//        }  
//        list[low] = tmp;              //中轴记录到尾  
//        return low;                   //返回中轴的位置  
//    }  
//	
//	public static void _quickSort(String[] list, int low, int high) {  
//        if (low < high) {  
//            int middle = getMiddle(list, low, high);  //将list数组进行一分为二  
//            _quickSort(list, low, middle - 1);        //对低字表进行递归排序  
//            _quickSort(list, middle + 1, high);       //对高字表进行递归排序  
//        }  
//    }  
//	
//	public static void quick(String[] str) {  
//        if (str.length > 0) {    //查看数组是否为空  
//            _quickSort(str, 0, str.length - 1);  
//        }  
//    } 
	
	/**
	 * date1 < date2 return -1
	 * @param date1
	 * @param date2
	 * @return
	 */
	private static int compareDate(String date1,String date2){
		if(date1.equals(date2))
    		return 0;
    	else{
    		int year1 = Integer.parseInt(date1.substring(0, 4));
    		int year2 = Integer.parseInt(date2.substring(0, 4));
    		int month1 = Integer.parseInt(date1.substring(5, 7));
    		int month2 = Integer.parseInt(date2.substring(5, 7));
    		int day1 = Integer.parseInt(date1.substring(8, 10));
    		int day2 = Integer.parseInt(date2.substring(8, 10));
    		
    		if(year1 < year2)
    			return -1;
    		else if(year1 > year2)
    			return 1;
    		else{
    			//year is the same
    			if(month1 < month2)
    				return -1;
    			else if(month1 > month2)
    				return 1;
    			else{
    				//year and month is the same
    				if(day1 < day2)
    					return -1;
    				else if(day1 > day2)
    					return 1;
    				else
    					return 0;
    			}
    		}
    	}	
	}
	
	
	public static String [ ] qsort(String items[])
	{
	qs(items, 0, items.length - 1);
	return items;
	}

	private static void qs(String items[], int left, int right)
	{
	int i, j;
	String pivot, temp;
	i = left;
	j = right;
	pivot = items [(left + right) / 2];
	
	do
		{
		
		while ((compareDate(items [i] ,pivot) == -1) && (i < right))
			{
			i++;
			}
		
		while ((compareDate(pivot ,items [j]) == -1) && (j > left))
			{
			j--;
			}
			
		if (i <= j)
			{
			temp = items [i];
			items [i] = items [j];
			items [j] = temp;
			i++;
			j--;
			}
		}
	while (i <= j);
	
	if (left < j)
		{
		qs (items, left, j);
		}
	
	if (i < right)
		{
		qs (items, i, right);
		}
	}
	
	
	
//	public static void main(String[] args) {  
//        // TODO Auto-generated method stub  
//         String[] list={"2014-02-21",
//        		"2014-02-21",
//        		 "2013-07-10",
//        		 "2011-09-12",
//        		 "2011-08-24",
//        		 "2009-09-16",
//        		 "2009-08-14",
//        		 "2009-08-13",
//        		 "2009-03-11",
//        		"2009-03-10",
//        		 "2008-05-08"};  
//         QuickSort qs=new QuickSort();  
//         qs.qsort(list);  
//         for(int i=0;i<list.length;i++){  
//             System.out.print(list[i]+" ");  
//         }  
//         System.out.println();  
//    }  
	
}
