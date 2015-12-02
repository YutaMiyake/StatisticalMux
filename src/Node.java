// Node class represents a basic node in network graph.
// Each has an unique id and keeps track of adjacent nodes.
// Adjacent nodes are stored in a hashtable with a pair of
// integer id and the object of Node class.
import java.util.Hashtable;

public class Node{
  private static int unique_id = 1;
  protected int id;
  private Hashtable<Integer,Node> adjNodes;

  public Node(){
  // constructor
    this.id = getUID();
    adjNodes = new Hashtable<Integer,Node>();
  }
  private static int getUID(){
    // return the unique id
    return unique_id++;
  }
  public int getId(){
  // return id
    return this.id;
  }
  public void put(Node node){
  // add a given node to the hashtable for adjacent nodes
    this.adjNodes.put(node.getId(), node);
  }
  public boolean isConnectedTo(int id){
  // return if a node is connected to the node with the given id
    return this.adjNodes.containsKey(id);
  }
  public Hashtable<Integer,Node> getAdjNodes(){
  // returns the hashtable for adjacent nodes
    return this.adjNodes;
  }
}