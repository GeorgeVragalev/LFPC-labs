package LABFA;
import java.util.ArrayList;

public class Graph {
    private ArrayList<ArrayList<Edge>> adjList;
    private ArrayList<Character> vertices;

    public Graph(ArrayList<ArrayList<Edge>> adjList, ArrayList<Character> vertices) {
        this.adjList = adjList;
        this.vertices = vertices;
    }
                                //S aB
    public void addEdge(String userInput) {
        char[] chars = userInput.toCharArray();

        //When input is length 4 "S aB"
        if (chars.length == 4) {
            //Check if Node exists
            if (!vertices.contains(chars[0])) {
                vertices.add(chars[0]);
                adjList.add(new ArrayList<>());
                adjList.get(vertices.size() - 1).add(new Edge(chars[0], chars[3], chars[2]));   //Create new Arraylist and add new node to start
            } else {//existing character
                adjList.get(getIndex(adjList, chars[0])).add(new Edge(chars[0], chars[3], chars[2]));
            }
            //When input is length 3 "A a"
        } else if (chars.length == 3) {
            //Check if Node exists
            if (!vertices.contains(chars[0])) {
                vertices.add(chars[0]);
                adjList.add(new ArrayList<>());
                adjList.get(vertices.size() - 1).add(new Edge(chars[0], ' ', chars[2])); //Create new Arraylist and add new node to start
            } else {//existing character
                adjList.get(getIndex(adjList, chars[0])).add(new Edge(chars[0], ' ', chars[2]));
            }
        }
    }

    public void printGraph(){
        for (int i = 0; i < adjList.size(); i++) {
            System.out.print("\nAdjacency list of vertex: " + adjList.get(i).get(0).getSrc());
            for (int j = 0; j < adjList.get(i).size(); j++) {
                if (adjList.get(i).get(j).getDest()==' '){
                    System.out.print(" -->  End Node ("+ adjList.get(i).get(j).getWeight() +") ");
                }else{
                    System.out.print(" --> " + adjList.get(i).get(j).getDest() + "("+ adjList.get(i).get(j).getWeight() +") ");
                }
            }
            System.out.println();
        }
    }

    public static int getIndex(ArrayList<ArrayList<Edge>> adj, char start) { //S
        for (int i = 0; i < adj.size(); i++) {
            Edge e = adj.get(i).get(0);
            if (e.getSrc() == start)
                return i;
        }
        return -1;
    }

    public boolean isValid(String sequence){
        char key = 'S';
        //if sequence doesnt end with the ending character c then it is wrong
        if (sequence.indexOf('c') != sequence.length()-1 ){
            return false;
        }
        //loop through the check string - sequence
        for (Character c: sequence.toCharArray()) {
            //for each adjacency list, whose starting character is key
            for (Edge e: adjList.get(getIndex(adjList, key))) {
                //if edge weight is that of the character c we set the key to the destination of that edge and go to the next character
                if (e.getWeight()==c){
                    System.out.println(e.getSrc() + " " + e.getDest() + " " + e.getWeight());
                    key = e.getDest();
                    System.out.println(key+ " ");
                    break;
                }
                //if we dont find the character having looped through the whole sub array then its false
                else if(adjList.get(getIndex(adjList, key)).indexOf(e) == adjList.get(getIndex(adjList, key)).size()-1){
                    return false;
                }
            }
            //if the next destination node is empty, and we have reached the end of string TRUE
            if (key == ' ' && sequence.indexOf(c)==sequence.length()-1){
                return true;
            }
            //if the next destination node is empty, but we have NOT reached the end of string FALSE
            else if (key == ' ' && sequence.indexOf(c)!=sequence.length()-1){
                return false;
            }
        }
        return true;
    }
}