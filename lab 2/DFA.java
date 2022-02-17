import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

public class DFA {
    private NFA nfa;
    private LinkedHashMap<String, ArrayList<Edge>> dfa;
    private ArrayList<String> weights;


    public DFA(NFA nfa, LinkedHashMap<String, ArrayList<Edge>> dfa) {
        this.nfa = nfa;
        this.dfa = dfa;
        this.weights = nfa.uniqueWeightsVoid();
    }

    public void nfaToDfa() {
        dfa.put("q0", nfa.getNfa().get("q0")); //q0 : (a) q1 , (b) q2

        //until we dont find a new empty state
        while (!findNewState().equals("empty")) {
            String newState = findNewState();

            //if the state is single
            if (newState.length() == 2) {
                //we put that new state in dfa!
                dfa.put(newState, nfa.getNfa().get(newState));
            }
            //if the state is not single we create a new space for it in dfa
            else {
                dfa.put(newState, new ArrayList<Edge>());
                //we concatenate the nodes at each weight using function
                concatenateNodes(newState);
            }
        }
    }

    //for a double+ state we have to combine the nodes at each weight
    public void concatenateNodes(String nodes) {
        //split the state into separate nodes to loop through
        String[] nodesList = usingSplitMethod(nodes);//q0 q1

        // for each weight we find an edge in the node array we are looping through the edge that has a specific weight
        for (String weight : this.weights) {
            String resultNode = ""; //q0q1q0
            for (String node : nodesList) {
                if (!findEdgeWithWeight(node, weight).equals("")) {
                    resultNode += findEdgeWithWeight(node, weight); //append the result of found edge of a specified weight to result
                }
            }
            //avoids adding empty nodes in dfa
            if (!resultNode.equals("")){
                resultNode = removeDuplicates(resultNode);
                Edge newNode = new Edge(nodes, resultNode, weight);
                dfa.get(nodes).add(newNode);
            }
        }
    }

    //Removing duplicates from string
    public String[] usingSplitMethod(String text) {
        return text.split("(?<=\\G.{" + 2 + "})");
    }
    public String removeDuplicates(String s) {
        String[] variables = usingSplitMethod(s); //q0 q0 q1 q2
        String result = ""; //q0q1q2
        for (String node : variables) {
            if (!result.contains(node)) {
                result += node;
            }
        }
        return result;
    }

    //Searches through a specific array list of a node and find an edge that has a specific weight
    public String findEdgeWithWeight(String node, String weight) {
        for (Edge e : nfa.getNfa().get(node)) {
            if (e.getWeight().equals(weight)) {
                return e.getDest();
            }
        }
        return "";
    }

    //loops through whole dfa and finds states that haven't been added to dfa yet
    public String findNewState() {
        for (String s: dfa.keySet()) {
            for (Edge edge : dfa.get(s)) {
                if (!dfa.containsKey(edge.getDest()) && !exists(edge.getDest())) {
                    return edge.getDest();
                }
            }
        }
        return "empty";
    }

    //function that checks to see if a possibly new node already exists in dfa just in different order
    public boolean exists(String newNode){
        int check = 0;
        for (String node: dfa.keySet()){
            if (node.length() > 2){
                //create arrays of separate nodes for a complex node like //q0q1
                String[] nodesListNewNode = usingSplitMethod(newNode);//q0 q1
                String[] nodesListNode = usingSplitMethod(node);//q0 q1
                for (String s: nodesListNewNode) {
                    if ( !Arrays.asList(nodesListNode).contains(s)){
                        check = 0;
                        break;
                    }else{
                        check++;
                    }
                }
                //if we have a check condition that was satisfied the number of elements times, then the node exists and we should output true that it does
                if (check== nodesListNewNode.length && check == nodesListNode.length){
                    return true;
                }
            }
        }
        //if the true condition isn't satisfied we return false
        return false;
    }

    //Prints dfa table and input for python code to generate the graph
    public void printDFA() {
        String endState = "";
        int nOfElements=nfa.getNfa().keySet().size()-1;
        int count = 0;
        for (String key: nfa.getNfa().keySet()){
            if (count==nOfElements){
                endState = key;
            }
            count++;
        }

        for (String s : dfa.keySet()) {
            if (s.contains(endState) && !endState.equals("")){
                System.out.print("*" + s + " : ");
            }
            else if (s.equals("q0")){
                System.out.print("->" + s + " : ");
            }
            else{
                System.out.print(s + " : ");
            }

            for (Edge e : dfa.get(s))
                e.printEdge();
            System.out.println();
        }

        System.out.println("------------------------DFA state input------------------------");
        for (String s : dfa.keySet()) {
            for (Edge e : dfa.get(s)){
                System.out.println(e.getSrc() + " " + e.getWeight() + " " + e.getDest());
            }
        }
    }

}
