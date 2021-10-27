package pai.utl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pai.model.Poker;
import pai.model.PokerLabel;

public class PokerRule 
{

	//判断牌型
	public static PokerType checkPokerType(List<PokerLabel> list)
	{
		
		Collections.sort(list);
		
		int count=list.size();
		if(count==1)
		{
			//单张
			return PokerType.p_1;
		}
		else if(count==2)
		{
			//对子
			if(isSame(list, count))
			{
				return PokerType.p_2;
			}
			//王炸
			if(isWangZha(list))
			{
				return PokerType.p_2w;
			}
			return PokerType.p_error;
		}
		else if(count==3)
		{
			//三个头
			if(isSame(list, count))
			{
				return PokerType.p_3;
			}
			return PokerType.p_error;
		}
		else if(count==4)
		{
			//炸弹
			if(isSame(list, count))
			{
				return PokerType.p_4;
			}
			//三带一
			if(isSanDaiYi(list))
			{
				return PokerType.p_31;
			}
			return PokerType.p_error;
			
		}
		else if(count>=5)
		{
			//顺子
			if(isShunZi(list))
			{
				return PokerType.p_n;
			}else if(isSanDaiYiDui(list))
			{
				//三代一对
				return PokerType.p_32;
			}else if(isLianDui(list))
			{
				//连对
				return PokerType.p_1122;
			}else if(isFeiJI(list)){
				//双飞or双飞双带
				return PokerType.p_111222;
			}else if(isFeiJIDaiChiBang1(list))
			{
			//双飞or双飞双带
			return PokerType.p_11122234;
		    }
			else if(isFeiJIDaiChiBang2(list))
			{
				//双飞or双飞双带
				return PokerType.p_1112223344;
			}
		}
		return PokerType.p_error;
	}
	
	public static boolean isWangZha(List<PokerLabel> list)
	{
		if((list.get(0).getNum()==16&&list.get(1).getNum()==17)||(list.get(0).getNum()==17&&list.get(1).getNum()==16))
		{
			return true;
		}
		return false;
	}
	/**
	 * 判断list内扑克是否相同
	 * @param list
	 * @param i
	 * @return
	 */
	public static boolean isSame(List<PokerLabel> list,int count)
	{
		for(int j=0;j<count-1;j++)
		{
			int a=list.get(j).getNum();
			int b=list.get(j+1).getNum();
			if(a!=b)
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 *  判断是否是三带一
	 * @param list
	 * @return
	 */
	public static boolean isSanDaiYi(List<PokerLabel> list)
	{
		List<PokerLabel> temp=new ArrayList<PokerLabel>();
		temp.addAll(list);
		if(isSame(temp, 3))
		{
			return true;
		}
		temp.remove(0);
		if(isSame(temp, 3))
		{
			return true;
		}
		return false;
	}
	
	/**
	 *  判断是否是三带一对
	 * @param list
	 * @return
	 */
	public static boolean isSanDaiYiDui(List<PokerLabel> list)
	{
		List<PokerLabel> temp=new ArrayList<PokerLabel>();
		temp.addAll(list);
		if(temp.size()==5)
		{
			if(isSame(temp, 3))
			{
				temp.remove(0);
				temp.remove(0);
				temp.remove(0);
				if(isSame(temp, 2))
				{
					return true;
				}
				return false;
			}
			if(isSame(temp, 2))
			{
				temp.remove(0);
				temp.remove(0);
				if(isSame(temp, 3))
				{
					return true;
				}
				return false;
			}
		}
		return false;
	}
	
	
	/**
	 * 判断是否是顺子
	 * @param list
	 * @return
	 */
	public static boolean isShunZi(List<PokerLabel> list)
	{
		for(int i=0;i<list.size()-1;i++)
		{
			int a=list.get(i).getNum();
			int b=list.get(i+1).getNum();
			if(a-b!=1)
			{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 判断是否是连对
	 * @param list
	 * @return
	 */
	public static boolean isLianDui(List<PokerLabel> list)
	{
		int size=list.size();
		if(size<6&&size%2!=0)
		{
			return false;
		}
		for(int i=0;i<size;i++)
		{
			int a=list.get(i).getNum();
			int b=list.get(i+1).getNum();
			if(a!=b)
			{
				return false;
			}
			i++;
		}
		for(int i=0;i<size;i++)
		{
			int a=list.get(i).getNum();
			int b=list.get(i+2).getNum();
			if(a-b!=1){
				return false;
			}
			i++;
			i++;
		}
		return true;
	}
	
	/**
	 * 判断是不是双飞
	 * @param list
	 * @return
	 */
	public static boolean isFeiJI(List<PokerLabel> list)
	{
		List<Integer> count=feiCount(list);
		if(count!=null&&count.size()>=2)
		{
			int len=count.size();
			for(int i=0;i<len-1;i++)
			{
				if(count.get(i+1)-count.get(i)!=1)
				{
					return false;
				}
			}
			int dui=feiDuiCount(list,count);
			if(dui==0)
			{
				return true;
			}
		}
		return false;
	}
	
	//飞机带翅膀 3311
	public static boolean isFeiJIDaiChiBang2(List<PokerLabel> list)
	{
		List<Integer> feiji=feiCount(list);
		if(feiji!=null&&feiji.size()>=2)
		{
			int len=feiji.size();
			for(int i=0;i<len-1;i++)
			{
				if(feiji.get(i)-feiji.get(i+1)!=1)
				{
					return false;
				}
			}
			int dui=feiDuiCount(list,feiji);
			if(dui==feiji.size())
			{
				return true;
			}
		}
		return false;
	}
	
	//飞机带翅膀3322
	public static boolean isFeiJIDaiChiBang1(List<PokerLabel> list)
	{
		List<Integer> feiji=feiCount(list);
		if(feiji!=null&&feiji.size()>=2)
		{
			int len=feiji.size();
			for(int i=0;i<len-1;i++)
			{
				if(feiji.get(i)-feiji.get(i+1)!=1)
				{
					return false;
				}
			}
			int dui=feiDanZhangCount(list,feiji);
			if(dui==feiji.size())
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 判断三个头有几个
	 * @param list
	 * @return
	 */
	public static List<Integer> feiCount(List<PokerLabel> list)
	{
		List<Integer> cnt = new ArrayList<Integer>();
		for(int i=0;i<list.size()-2;i++)
		{
			int a=list.get(i).getNum();
			int b=list.get(i+1).getNum();
			int c=list.get(i+2).getNum();
			if(a==b&&a==c)
			{
				cnt.add(a);
			}
		}
		return cnt;
	}
	/**
	 * 判断双飞中对子有几个
	 * @param list
	 * @return
	 */
	public static int feiDuiCount(List<PokerLabel> list,List<Integer> feiji)
	{
		List<PokerLabel> temp=new ArrayList<PokerLabel>();
		temp.addAll(list);
		int cnt = 0;
		for(int i=0;i<temp.size();i++)
		{
			for(int j=0;j<feiji.size();j++)
			{
				int a=list.get(i).getNum();
				if(a==feiji.get(j))
				{
					temp.remove(i);
				}
			}
		}
		int size=temp.size();
		if(size>0&&size%2==0)
		{
			for(int i=0;i<temp.size()-1;i++)
			{
				int a=list.get(i).getNum();
				int b=list.get(i+1).getNum();
				if(a==b)
				{
					cnt++;
				}
			}
		}
		return cnt;
	}
	
	/**
	 * 判断双飞中单张有几个
	 * @param list
	 * @return
	 */
	public static int feiDanZhangCount(List<PokerLabel> list,List<Integer> feiji)
	{
		List<PokerLabel> temp=new ArrayList<PokerLabel>();
		temp.addAll(list);
		int cnt = 0;
		for(int i=0;i<temp.size();i++)
		{
			for(int j=0;j<feiji.size();j++)
			{
				int a=list.get(i).getNum();
				if(a==feiji.get(j))
				{
					temp.remove(i);
				}
			}
		}
		int size=temp.size();
		if(size>0){
			for(int i=0;i<temp.size()-1;i++)
			{
				int a=list.get(i).getNum();
				int b=list.get(i+1).getNum();
				if(a!=b)
				{
					cnt++;
				}
			}
		}
		return cnt;
	}
	
	//比大小
	public static boolean isBigger(List<PokerLabel> prevList,List<PokerLabel> currentList)
	{
		// 首先判断牌型是不是一样
		PokerType paiXing = checkPokerType(prevList);
		if (paiXing.equals(checkPokerType(currentList))) 
		{
			// 根据牌型来判断大小
			if (PokerType.p_1.equals(paiXing)) 
			{
				// 单张
				if (compareLast(prevList, currentList)) 
				{
					return true;
				}
				return false;
			} 
			else if (PokerType.p_2w.equals(paiXing)) 
			{
				// 王炸
				return false;
			} else if (PokerType.p_2.equals(paiXing)) 
			{
				// 对子
				if (compareLast(prevList, currentList)) 
				{
					return true;
				}
				return false;
			} 
			else if (PokerType.p_3.equals(paiXing)) 
			{
				// 三张
				if (compareLast(prevList, currentList)) 
				{
					return true;
				}
				return false;
			} 
			else if (PokerType.p_31.equals(paiXing)) 
			{
				// 三带一
				if (compareLast(prevList, currentList)) 
				{
					return true;
				}
				return false;
			} 
			else if (PokerType.p_32.equals(paiXing)) 
			{
				// 三带一对
				if (compare(prevList, currentList)) 
				{
					return true;
				}
				return false;
			}
			else if (PokerType.p_4.equals(paiXing)) 
			{
				// 炸弹
				if (compareLast(prevList, currentList)) 
				{
					return true;
				}
				return false;
			} 
			else if (PokerType.p_n.equals(paiXing)) 
			{
				// 顺子
				if (compareLast(prevList, currentList)) 
				{
					return true;
				}
				return false;
			} 
			else if (PokerType.p_1122.equals(paiXing)) 
			{
				// 连对
				if (compareLast(prevList, currentList)) 
				{
					return true;
				}
				return false;
			} 
			else if (PokerType.p_111222.equals(paiXing)) 
			{
				// 双飞
				if (compare(prevList, currentList)) 
				{
					return true;
				}
				return false;
			} 
			else if (PokerType.p_11122234.equals(paiXing)) 
			{
				// 飞机带翅膀（单张）
				if (compare(prevList, currentList))
				{
					return true;
				}
				return false;
			} 
			else if (PokerType.p_1112223344.equals(paiXing)) 
			{
				// 飞机带翅膀（对子）
				if (compare(prevList, currentList))
				{
					return true;
				}
				return false;
			} 
				
		}
		else if(currentList.size()==2)
		{
			//判断是不是王炸
			if(isWangZha(currentList))
			{
				return true;
			}
			return false;
		} 
		else if(currentList.size()==4)
		{
			//判断是不是炸弹			
			if(isSame(currentList, 4))
			{
				return true;
			}
			return false;
		}
		return false;
	}
	
	public static boolean compareLast(List<PokerLabel> prevList,List<PokerLabel> currentList)
	{
		if(prevList.get(prevList.size()-1).getNum()<currentList.get(currentList.size()-1).getNum())
		{
			return true;
		}
		return false;
	}
	
	public static boolean compare(List<PokerLabel> prevList,List<PokerLabel> currentList)
	{
		int a=san(prevList);
		int b=san(currentList);
		if(a==-1||b==-1)
		{
			return false;
		}
		if(b>a)
		{
			return true;
		}
		return false;
	}
	
	public static int san(List<PokerLabel> list)
	{
		for(int i=0;i<list.size()-2;i++)
		{
			int a=list.get(i).getNum();
			int b=list.get(i+1).getNum();
			int c=list.get(i+2).getNum();
			if(a==b&&a==c)
			{
				return a;
			}
		}
		return -1;
	}
	
	
}
