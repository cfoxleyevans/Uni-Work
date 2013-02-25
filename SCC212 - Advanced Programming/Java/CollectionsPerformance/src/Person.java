public class Person 
{
	public String name;
	public int age;

	public Person (String name, int age)
	{
		this.name = name;
		this.age = age;
	}

	public String toString()
	{
		return "Person: " + name + " (" + age + ")";
	}

	public boolean equals (Object o)
	{
		return (o instanceof Person && ((Person)o).name.equals(name));
	}

	public int hashCode() {
		return name.hashCode();
	}
}
