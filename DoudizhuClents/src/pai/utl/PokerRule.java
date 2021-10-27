package pai.utl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import pai.model.Poker;
import pai.model.PokerLabel;

public class PokerRule 
{

	//�ж�����
	public static PokerType checkPokerType(List<PokerLabel> list)
	{
		
		Collections.sort(list);
		
		int count=list.size();
		if(count==1)
		{
			//����
			return PokerType.p_1;
		}
		else if(count==2)
		{
			//����
			if(isSame(list, count))
			{
				return PokerType.p_2;
			}
			//��ը
			if(isWangZha(list))
			{
				return PokerType.p_2w;
			}
			return PokerType.p_error;
		}
		else if(count==3)
		{
			//����ͷ
			if(isSame(list, count))
			{
				return PokerType.p_3;
			}
			return PokerType.p_error;
		}
		else if(count==4)
		{
			//ը��
			if(isSame(list, count))
			{
				return PokerType.p_4;
			}
			//����һ
			if(isSanDaiYi(list))
			{
				return PokerType.p_31;
			}
			return PokerType.p_error;
			
		}
		else if(count>=5)
		{
			//˳��
			if(isShunZi(list))
			{
				return PokerType.p_n;
			}else if(isSanDaiYiDui(list))
			{
				//����һ��
				return PokerType.p_32;
			}else if(isLianDui(list))
			{
				//����
				return PokerType.p_1122;
			}else if(isFeiJI(list)){
				//˫��or˫��˫��
				return PokerType.p_111222;
			}else if(isFeiJIDaiChiBang1(list))
			{
			//˫��or˫��˫��
			return PokerType.p_11122234;
		    }
			else if(isFeiJIDaiChiBang2(list))
			{
				//˫��or˫��˫��
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
	 * �ж�list���˿��Ƿ���ͬ
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
	 *  �ж��Ƿ�������һ
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
	 *  �ж��Ƿ�������һ��
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
	 * �ж��Ƿ���˳��
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
	 * �ж��Ƿ�������
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
	 * �ж��ǲ���˫��
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
	
	//�ɻ������ 3311
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
	
	//�ɻ������3322
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
	 * �ж�����ͷ�м���
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
	 * �ж�˫���ж����м���
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
	 * �ж�˫���е����м���
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
	
	//�ȴ�С
	public static boolean isBigger(List<PokerLabel> prevList,List<PokerLabel> currentList)
	{
		// �����ж������ǲ���һ��
		PokerType paiXing = checkPokerType(prevList);
		if (paiXing.equals(checkPokerType(currentList))) 
		{
			// �����������жϴ�С
			if (PokerType.p_1.equals(paiXing)) 
			{
				// ����
				if (compareLast(prevList, currentList)) 
				{
					return true;
				}
				return false;
			} 
			else if (PokerType.p_2w.equals(paiXing)) 
			{
				// ��ը
				return false;
			} else if (PokerType.p_2.equals(paiXing)) 
			{
				// ����
				if (compareLast(prevList, currentList)) 
				{
					return true;
				}
				return false;
			} 
			else if (PokerType.p_3.equals(paiXing)) 
			{
				// ����
				if (compareLast(prevList, currentList)) 
				{
					return true;
				}
				return false;
			} 
			else if (PokerType.p_31.equals(paiXing)) 
			{
				// ����һ
				if (compareLast(prevList, currentList)) 
				{
					return true;
				}
				return false;
			} 
			else if (PokerType.p_32.equals(paiXing)) 
			{
				// ����һ��
				if (compare(prevList, currentList)) 
				{
					return true;
				}
				return false;
			}
			else if (PokerType.p_4.equals(paiXing)) 
			{
				// ը��
				if (compareLast(prevList, currentList)) 
				{
					return true;
				}
				return false;
			} 
			else if (PokerType.p_n.equals(paiXing)) 
			{
				// ˳��
				if (compareLast(prevList, currentList)) 
				{
					return true;
				}
				return false;
			} 
			else if (PokerType.p_1122.equals(paiXing)) 
			{
				// ����
				if (compareLast(prevList, currentList)) 
				{
					return true;
				}
				return false;
			} 
			else if (PokerType.p_111222.equals(paiXing)) 
			{
				// ˫��
				if (compare(prevList, currentList)) 
				{
					return true;
				}
				return false;
			} 
			else if (PokerType.p_11122234.equals(paiXing)) 
			{
				// �ɻ�����򣨵��ţ�
				if (compare(prevList, currentList))
				{
					return true;
				}
				return false;
			} 
			else if (PokerType.p_1112223344.equals(paiXing)) 
			{
				// �ɻ�����򣨶��ӣ�
				if (compare(prevList, currentList))
				{
					return true;
				}
				return false;
			} 
				
		}
		else if(currentList.size()==2)
		{
			//�ж��ǲ�����ը
			if(isWangZha(currentList))
			{
				return true;
			}
			return false;
		} 
		else if(currentList.size()==4)
		{
			//�ж��ǲ���ը��			
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
