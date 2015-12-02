// StatMux holds every network object in it and gives the next hop given
// two ids: start id and end id, which is optimized by looking at possible
// next hops from the start. The network topology is static. There are
// five layers, each of which has some nodes. The first layer and
// the last layer have host nodes. The intermediate layers have routers.
// Routers in the second last layer are connected to every destinations in this
// network to improve simplicity.

import java.util.Hashtable;

public class StatMux{
  private Hashtable<Integer,Node> nodes;
  private int[] routerIds;
  private int[][]connections = {{4},{4,5},{5,6},
                       {7,8},{8,9},{9,10},
                       {11,12,13},{11,12},{12,13,14},{14,15,16}
  };

  StatMux(){
    // default constructor
    // Allocate some memory and then call config.
    this.nodes = new Hashtable<Integer,Node>();
    this.routerIds = new int[13];
    config();
  }
  public Router getRouter(int nth){
    return (Router) this.nodes.get(this.routerIds[nth]);
  }
  public int[][] getConnections(){
    return this.connections;
  }
  public void setRouter(int indx, Router val){
    // insert a router at a given index
    this.nodes.put(this.routerIds[indx],val);
  }
  public void config(){
    // set up our network topology
    Host host1 = new Host();
    Host host2 = new Host();
    Host host3 = new Host();

    Router router0 = new Router(new int[]{0,0,1}, 10,15);
    Router router1 = new Router(new int[]{1,2,2}, 9,11);
    Router router2 = new Router(new int[]{0,1,0}, 20,12);
    Router router3 = new Router(new int[]{1,1,1}, 15,13);
    Router router4 = new Router(new int[]{2,0,1}, 8,16);
    Router router5 = new Router(new int[]{0,2,2}, 11,14);
    Router router6 = new Router(new int[]{1,0,0}, 13,16);
    Router router7 = new Router(new int[]{1,1,1}, 15,17);
    Router router8 = new Router(new int[]{2,0,1}, 8,17);
    Router router9 = new Router(new int[]{0,2,2}, 11,18);
    Router router10 = new Router(new int[]{0,1,2}, 10,15);
    Router router11 = new Router(new int[]{2,0,1}, 10,14);
    Router router12 = new Router(new int[]{1,2,0}, 10,17);

    routerIds[0] = router0.getId();
    routerIds[1] = router1.getId();
    routerIds[2] = router2.getId();
    routerIds[3] = router3.getId();
    routerIds[4] = router4.getId();
    routerIds[5] = router5.getId();
    routerIds[6] = router6.getId();
    routerIds[7] = router7.getId();
    routerIds[8] = router8.getId();
    routerIds[9] = router9.getId();
    routerIds[10] = router10.getId();
    routerIds[11] = router11.getId();
    routerIds[12] = router12.getId();

    Host host17 = new Host();
    Host host18 = new Host();
    Host host19 = new Host();
    Host host20 = new Host();

    host1.put((Node)router0);
    host2.put((Node)router0); host2.put((Node)router1);
    host3.put((Node)router1); host3.put((Node)router2);
    router0.put((Node)router3); router0.put((Node)router4);
    router1.put((Node)router4); router1.put((Node)router5);
    router2.put((Node)router5); router2.put((Node)router6);
    router3.put((Node)router7); router3.put((Node)router8); router3.put((Node)router9);
    router4.put((Node)router7); router4.put((Node)router8);
    router5.put((Node)router8); router5.put((Node)router9); router5.put((Node)router10);
    router6.put((Node)router10); router6.put((Node)router11); router6.put((Node)router12);
    router7.put((Node)host17); router7.put((Node)host18); router7.put((Node)host19); router7.put((Node)host20);
    router8.put((Node)host17); router8.put((Node)host18); router8.put((Node)host19); router8.put((Node)host20);
    router9.put((Node)host17); router9.put((Node)host18); router9.put((Node)host19); router9.put((Node)host20);
    router10.put((Node)host17); router10.put((Node)host18); router10.put((Node)host19); router10.put((Node)host20);
    router11.put((Node)host17); router11.put((Node)host18); router11.put((Node)host19); router11.put((Node)host20);
    router12.put((Node)host17); router12.put((Node)host18); router12.put((Node)host19); router12.put((Node)host20);

    nodes.put(host1.getId(),host1);
    nodes.put(host2.getId(),host2);
    nodes.put(host3.getId(),host3);
    nodes.put(router0.getId(),router0);
    nodes.put(router1.getId(),router1);
    nodes.put(router2.getId(),router2);
    nodes.put(router3.getId(),router3);
    nodes.put(router4.getId(),router4);
    nodes.put(router5.getId(),router5);
    nodes.put(router6.getId(),router6);
    nodes.put(router7.getId(),router7);
    nodes.put(router8.getId(),router8);
    nodes.put(router9.getId(),router9);
    nodes.put(router10.getId(),router10);
    nodes.put(router11.getId(),router11);
    nodes.put(router12.getId(),router12);
    nodes.put(host17.getId(),host17);
    nodes.put(host18.getId(),host18);
    nodes.put(host19.getId(),host19);
    nodes.put(host20.getId(),host20);
  }

  public int findNextNode(int id1, int id2, PacketType type){
    // Returns the next hop id, given the start and end ids with a packet type.
    // The optimized next hop is chosen as one with the minimal effective delay.
    // The effective delay is calculated by the formula:
    //         effective delay = priority * delay + flowlevel
    // Looking at every possible nodes connected to the node with the start ID
    // and calculating the effective delay value, choose the best one.
    Node node = nodes.get(id1);
    Hashtable<Integer,Node> adjs = node.getAdjNodes();
    Hashtable<Integer,Integer> delays = new Hashtable<Integer,Integer>();

    if(node.isConnectedTo(id2)){
      return id2;
    }
    // get feedbacks
    for(int key: adjs.keySet()){
        Router router = (Router) adjs.get(key);
        int id = router.getId();
        int flowlevel = router.getFlowLevel();
        int priority = router.getPriority(type);
        int delay = router.getDelay();
        int effective =  priority*delay + flowlevel;
        delays.put(id,effective);
    }
    return findMinIndx(delays);
  }

  public int findMinIndx(Hashtable<Integer,Integer> hashtable){
    // given a hash table of a pair <id,delay>, returns the index for
    // the minimal delay among all pairs in the table
    int minIdx = -1;
    int minVal = Integer.MAX_VALUE;
    for(int key: hashtable.keySet()){
      if(minVal > hashtable.get(key)){
        minIdx = key;
        minVal = hashtable.get(key);
      }
    }
    return minIdx;
  }
};