import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class Driver
{
	public static void main(String[] args)
	{
		//the number of elements to add to the collection
		int number = 10000000;

		//used to generate to collection that i will be working with
		//ArrayList<Person> list = generateArrayList(number);
		//LinkedList<Person> list = generateLinkedList(number);
		//HashSet<Person> list = genertateHashSet(number);
		//HashMap<Integer, Person> list = generateHashMap(number);
		
		//timer start
		long start = System.currentTimeMillis();
		
		//**place code to time here**//
		
		//timer finish
		long finish = System.currentTimeMillis();
		
		//calculate total time
		long totalTime = (finish - start);
		
		//System.out.println(p);
		System.out.println(totalTime + " Mili Seconds to insert " + number + " for run ");
	}
	
	static LinkedList<Person> generateLinkedList(int number)
	{
		LinkedList<Person> list = new LinkedList<Person>();
		for (int i = 0; i < number; i++)
		{
			list.add(new Person("Person" + i , i));
		}
		return list;
	}
		
	static ArrayList<Person> generateArrayList(int number)
	{
		ArrayList<Person> list = new ArrayList<Person>();
		for (int i = 0; i < number; i++)
		{
			list.add(new Person("Person" + i , i));
		}
		return list;
	}	
		
	static HashSet<Person> genertateHashSet(int number)
	{
		HashSet<Person> list = new HashSet<Person>();
		for (int i = 0; i < number; i++)
		{
			list.add(new Person("Person" + i , i));
		}
		return list;
		
	}
	
	static HashMap<Integer, Person> generateHashMap(int number)
	{
		HashMap<Integer, Person> list = new HashMap<Integer, Person>();
		for (int i = 0; i < number; i++)
		{
			list.put(i, new Person("Person" + i , i));
		}
		return list;
	}
	
	static Boolean findInstanceInArrayList(ArrayList<Person> list, int number)
	{
		Boolean found = list.contains(new Person("Person" + number/2, number/2));
		
		return found;
	}
	
	static Boolean findInstanceInLinkedList(LinkedList<Person> list, int number)
	{
		Boolean found = list.contains(new Person("Person" + number/2, number/2));
		
		return found;
	}
	
	static Boolean findInstanceInHashSet(HashSet<Person> list, int number)
	{
		Boolean found = list.contains(new Person("Person" + number/2, number/2));
		
		return found;
	}
	
	static Boolean findInstanceInHashMap(HashMap<Integer, Person> list, int number)
	{
		Boolean found = list.containsValue(new Person("Person" + number/2, number/2));
		
		return found;
	}

	static Person findInstanceInArrayListUsingIndex(ArrayList<Person> list, int number)
	{
		Person found = list.get(number/2);
		
		return found;
	}

	static Person findInstanceInLinkedListUsingIndex(LinkedList<Person> list, int number)
	{
		Person found = list.get(number/2);
		
		return found;
	}

	static Person findInstanceInArrayListUsingName(ArrayList<Person> list, int number)
	{
		int found = 0;
		for(int i = 0; i < list.size(); i++)
		{
			Person current = list.get(i);
			if (current.name == "Person" + number/2)
			{
				found = i;
				break;
			}
		}
		return list.get(found);
	}

	static Person findInstanceInLinkedListUsingName(LinkedList<Person> list, int number)
	{
		int found = 0;
		for(int i = 0; i < list.size(); i++)
		{
			Person current = list.get(i);
			if (current.name == "Person" + number/2)
			{
				found = i;
				break;
			}
		}
		return list.get(found);
	}
	
	static void findInstanceInHashSetUsingName(HashSet<Person> list, int number)
	{
		for(Person p : list)
		{
			if(p.equals(new Person("Person" + number/2, number/2)));
			break;
		}
	}
	
	static Person findInstanceInHashMapUsingName(HashMap<Integer, Person> list, int number)
	{
		int found = 0;
		for(int i = 0; i < list.size(); i++)
		{
			Person current = list.get(i);
			if (current.name == "Person" + number/2)
			{
				found = i;
				break;
			}
		}
		return list.get(found);
	}

	static void removeInstanceFromArrayList(ArrayList<Person> list, int number)
	{
		list.remove(new Person("Person" + number/2, number/2));
	}
	
	static void removeInstanceFromLinkedList(LinkedList<Person> list, int number)
	{
		list.remove(new Person("Person" + number/2, number/2));
	}
	
	static void removeInstanceFromHashSet(HashSet<Person> list, int number)
	{
		list.remove(new Person("Person" + number/2, number/2));
	}
	
	static void removeInstanceFromHashMap(HashMap<Integer, Person> list, int number)
	{
		list.remove(new Person("Person" + number/2, number/2));
	}
}

