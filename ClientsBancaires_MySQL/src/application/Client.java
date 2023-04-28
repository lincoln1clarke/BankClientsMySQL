/**
 * 
 */
package application;

/**
 * @author linco
 *
 */
public class Client {
	private String name;
	private String lastName;
	private int age;
	private long money;
	private long networth;
	private String riskLevel;
	
	public Client() {
		this(null,null);
	}
	
	public Client(String name, String lastName) {
		this.lastName = lastName;
		this.name = name;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public long getMoney() {
		return money;
	}
	public void setMoney(long money) {
		this.money = money;
	}
	public long getNetworth() {
		return networth;
	}
	public void setNetworth(long networth) {
		this.networth = networth;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
}
