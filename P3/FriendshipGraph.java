package P3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FriendshipGraph {
	Map<Person,Set<Person>> graph = new HashMap<Person,Set<Person>>();
	Set<String> pername = new HashSet<String>();
	public FriendshipGraph(){
	}
	
	/**
	 * set p as FriendshipGraph.key
	 * @param p Person who is not  FriendshipGraph.key
	 * @throws Exception 
	 */
	public void addVertex(Person p) throws Exception {
		if(!graph.containsKey(p)) {
			Set<Person> sp = new HashSet<Person>();
			graph.put(p, sp);
			pername.add(p.name);
		}
		if(pername.size() != graph.size()) {
			System.out.println("each person should have a unique name");
			throw new Exception("Exception for name repeated");
		}
	}
	/**
	 * add p2 to p1.value
	 * @param p1 Person who is the key of FriendshipGraph
	 * @param p2 Person who will be connected with p1
	 */
	public void addEdge(Person p1,Person p2) {
		Set<Person> sp1 = graph.get(p1);
		sp1.add(p2);
		graph.put(p1, sp1);
	}
	/**
	 *  the shortest distance (an int) between the people,
	 *  or -1 if the two people are not connected 
	 * @param p1 Person 
	 * @param p2 Person who is FriendshipGraph.key
	 * @return 
	 */
	public int getDistance(Person p1,Person p2) { 
		int distance = 0;		//步数，初始化为-1
		for(Person p:graph.keySet()) p.mark = false;   //初始化标记
		if(p1 != p2) {
			Set<Person> pnow = new HashSet<Person>();	//当前研究的集合
			pnow.add(p1);
			while(!pnow.isEmpty()) {
				distance++;
				Set<Person> pnext = new HashSet<Person>();
				for(Person p:pnow) {
					p.mark = true;
					for(Person pfriend:graph.get(p)) {
						if (pfriend.mark == false) pnext.add(pfriend);
						if (pfriend == p2) return distance;
					}
				}
				pnow = pnext;
			}
			if(p2.mark == false) distance = -1;
		}
		return distance;
	}
	
	public static void main(String[] args) throws Exception {
		FriendshipGraph graph = new FriendshipGraph();
		Person rachel = new Person("Rachel");
		Person ross = new Person("Ross");
		Person ben = new Person("Ben");
		Person kramer = new Person("Kramer");
		graph.addVertex(rachel);
		graph.addVertex(ross);
		graph.addVertex(ben);
		graph.addVertex(kramer);
		graph.addEdge(rachel,ross);
		graph.addEdge(ross,rachel);
		graph.addEdge(ross,ben);
		graph.addEdge(ben,ross);
		System.out.println(graph.getDistance(rachel,ross));
		//should print 1
		System.out.println(graph.getDistance(rachel,ben));
		//should print 2
		System.out.println(graph.getDistance(rachel,rachel));
		//should print 0
		System.out.println(graph.getDistance(rachel,kramer));
		//should print -1
	}
}
