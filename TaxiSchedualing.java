/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taxi;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;


public class TaxiSchedualing {
    int l;
    double alpha;
    int m;
    int x;
    int c;
    int n;
    Taxi[] taxis;
    TaxiScanner scanner = TaxiScanner.getInstance();
    boolean[][] adMat;
    Queue<Customer> orderQueue = new LinkedList<Customer>();
    
    public void initialize() {
        l = Integer.parseInt(scanner.nextLine());
        alpha = Double.parseDouble(scanner.nextLine());
        m = Integer.parseInt(scanner.nextLine());
        String[] parts = scanner.nextLine().split(" ");
        x = Integer.parseInt(parts[0]);
        taxis = new Taxi[x];
        for(int i=0; i<x; i++){
            Taxi taxi = new Taxi();
            taxis[i] = taxi;
        }
        c = Integer.parseInt(parts[1]);
//        for(int i=0; i<taxis.length; i++){
//            taxis[i].setCap(c);
//        }
        n = Integer.parseInt(scanner.nextLine());
        adMat = new boolean[n][n];
        for(int i=0;i<n;i++){
            String[] adjacent = scanner.nextLine().split(" ");
            for(int j=1; j<=Integer.parseInt(adjacent[0]); j++) {
                //System.out.println(Integer.parseInt(adjacent[j]));
                adMat[i][Integer.parseInt(adjacent[j])] = true;
            }
        }
        scanner.nextLine();//THIS REMOVES T AND T' !!!!!!!!! TEMP SOLUTION>>>
    }
    
    public void printAdMat() {
        for(int j=0; j<=n*2; j++) {
                System.out.printf("-");
            }
            System.out.printf("\n");
        for(int i=0; i<n; i++) {
            for(int j=0; j<n; j++) {
                if(adMat[i][j]) {
                    System.out.printf("|x");
                } else {
                    System.out.printf("| ");
                }
            }
            System.out.printf("|\n");
        }
        for(int j=0; j<=n*2; j++) {
            System.out.printf("-");
        }
        System.out.printf("\n");
    }
    
    public Integer[] inefficientShortestPath(int start, int goal) {
        Integer[] distance  = new Integer[n];
        boolean[] visited = new boolean[n];
        for(int i=0; i<n; i++) {
            if(i==start) {
                distance[i]=0;
            }else {
                distance[i]=2*n;
            }
        }
        int current = start;
        for(int i=0; i<n; i++) {
            //System.out.println(current);
            visited[current] = true;
            for(int j=0; j<n; j++) {
                if(adMat[current][j] && !visited[j] && distance[current]<distance[j]) {
                    distance[j]=distance[current]+1;
                    //System.out.println(Arrays.toString(distance));
                }
            }
            //System.out.println(Arrays.toString(visited));
            //System.out.println(Arrays.toString(distance));
            //System.out.println(distance.length);
            int smallest = 2*n;
            int index = -1;
            for(int j=0; j<distance.length; j++) {
                if(distance[j]<smallest && !visited[j]) {
                    smallest = distance[j];
                    index = j;
                }
                //System.out.println("Smallest: "+smallest+"|Current: "+current);
            }
            
            //System.out.println(Arrays.toString(distance));
            current = index;
            //System.out.println("current: "+current);
        }
        //System.out.println(Arrays.toString(distance));
        Integer[] path = new Integer[distance[goal]+1];
        current = goal;
        for(int i=distance[goal]; i>=0; i--) {
            //System.out.println(i);
            path[i] = current;
            int smallest = 2*n;
            int index = -1;
            for(int j=0; j<n; j++) {
                if(distance[j]<smallest && adMat[current][j]) {
                    smallest = distance[j];
                    index = j;
                }
            }
            current = index;
        }
        //System.out.println(Arrays.toString(distance));
        return path;
    }
    
    public void getOrders(String s) {
        int p = Integer.parseInt(s.split(" ")[0]);
        for(int i=0; i<p;i++){
            Customer c = new Customer();
            c.setLoc(Integer.parseInt(s.split(" ")[i*2+1]));
            c.setDest(Integer.parseInt(s.split(" ")[i*2+2]));
            orderQueue.add(c);
        }
    }
    
    public boolean directWalk(Taxi t, Customer c) {
        System.out.println(t.path);
        if(t.getLoc() == c.getDest() && t.isIn(c)){
            //System.out.println("A");
            t.dropPas();
            return true;
        } else if(t.getLoc() != c.getLoc() && !t.isIn(c)) {
            //System.out.println("B");
            if(t.path.isEmpty()){
                t.setPath(inefficientShortestPath(t.getLoc(), c.getLoc()));
            }
            t.setLoc(t.getPath());
        } else if(t.getLoc() == c.getLoc() && !t.isIn(c)){
            //System.out.println("C");
            t.addPas(c);
            t.setPath(inefficientShortestPath(t.getLoc(), c.getDest()));
        } else if(t.getLoc() != c.getDest() && t.isIn(c)){
            //System.out.println("D");
            t.setLoc(t.getPath());
        }
        return false;
        
        
    }
    
    public void run(){
        boolean done=false;
        //System.out.println("hi");
        initialize();
        //System.out.printf("Initial: %d, %f, %d, %d, %d\n", l, alpha, m, x, c);
        printAdMat();
        //System.out.println(Arrays.toString(inefficientShortestPath(2,6)));
        
        //Initialize position...
        taxis[0].setLoc(0);
        scanner.println("c");
        while(!done){
            if(scanner.hasNextLine()){
                getOrders(scanner.nextLine());
            }
            //System.out.println("Queue size: "+orderQueue.size());
            boolean status = directWalk(taxis[0], orderQueue.peek());
            if(status){
                orderQueue.remove();
            }
            scanner.println("c");
            if(!scanner.hasNextLine() && orderQueue.isEmpty()){
                done=true;
            }
            
        }
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        (new TaxiSchedualing()).run();
    }
    
}
