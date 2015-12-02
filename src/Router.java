// Router is derived from Node class
// A router has priorities corresponding to each packet type.
// Each priority is 0, 1, or 2. 0 is the highest.
// Higher priority means packets of that type have higher precedence at the router.
// In addition, each router has delay, throughput, and flowlevel member variables.

import java.util.Random;
public class Router extends Node{
  private int priorities[];
  private int delay;
  private int through;
  private int flowLevel;

  public Router(int[] priorities, int delay,int thr){
    // parameterized constructor
    super();
    this.priorities = priorities;
    this.delay = delay;
    this.through = thr;
    this.flowLevel = getRandomFlowLevel();
  }

  public int getDelay(){
    // return the delay of the router
    return this.delay;
  }
  public int getThroughput(){
     // return the throughput of the router
    return this.through;
  }
  public int getPriority(PacketType type){
    // return a priority specified by given type
    return priorities[type.getValue()];
  }
  public void setPriority(int value, PacketType type){
    // set a priority specified by given type with a given value
    this.priorities[type.getValue()] = value;
  }
  public void setPriorityList(int[] value){
    // set the whole priorities with an given array
    this.priorities = value;
  }
  public int[] getPriorityList(){
    // return the array of priorities
    return this.priorities;
  }
  public int getFlowLevel(){
    // return the flow level
    return this.flowLevel;
  }
  public void resetFlowLevel(){
    // reset the flow level with a random value
    this.flowLevel = getRandomFlowLevel();
  }
  public int getRandomFlowLevel(){
    // return a random integer value in the range [0,20)
    Random rand = new Random();
    return rand.nextInt(20);
  }
}