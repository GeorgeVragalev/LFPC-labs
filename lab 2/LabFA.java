import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class LabFA {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Provide your input below. When finished type !!!\"exit\"!!!");

        LinkedHashMap<String, ArrayList<Edge>> adjList = new LinkedHashMap<>();
        LinkedHashMap<String, ArrayList<Edge>> adjListNFA = new LinkedHashMap<>();
        LinkedHashMap<String, ArrayList<Edge>> adjListDFA = new LinkedHashMap<>();
        ArrayList<String> vertices = new ArrayList<>();
        Graph FA = new Graph(adjList, vertices);

        while (true) {
            //Input S aB
            String userInput = sc.nextLine();
            if (userInput.equals("exit") || userInput.equals("EXIT") || userInput.equals("Exit")) {
                break;
            } else {
                FA.addEdge(userInput);
            }
        }
        System.out.println("----------------Finite automaton state----------------");
        FA.printGraph();

        System.out.println("------------------------NFA state------------------------");
        NFA nfa = new NFA(FA, adjListNFA);
        nfa.graphToNFA();
        nfa.printNFA();

        System.out.println("------------------------DFA state------------------------");
        DFA dfa = new DFA(nfa, adjListDFA);
        dfa.nfaToDfa();
        dfa.printDFA();
    }
}
/*
Variant 23
q0 a q0
q0 a q1
q1 b q2
q0 b q0
q2 b q2
q1 a q0
exit

Another sample input
q0 a q1
q1 b q2
q2 c q0
q1 a q3
q0 b q2
q2 c q3
exit

--------------------OUTPUT----------------
----------------Finite automaton state----------------
q0 : q0 (a) q0  |  q0 (a) q1  |  q0 (b) q0  |
q1 : q1 (b) q2  |  q1 (a) q0  |
q2 : q2 (b) q2  |
------------------------NFA state------------------------
->q0 : q0 (a) q0q1  |  q0 (b) q0  |
q1 : q1 (b) q2  |  q1 (a) q0  |
*q2 : q2 (b) q2  |
------------------------DFA state------------------------
->q0 : q0 (a) q0q1  |  q0 (b) q0  |
q0q1 : q0q1 (a) q0q1  |  q0q1 (b) q0q2  |
*q0q2 : q0q2 (a) q0q1  |  q0q2 (b) q0q2  |
------------------------DFA state input------------------------
q0 a q0q1
q0 b q0
q0q1 a q0q1
q0q1 b q0q2
q0q2 a q0q1
q0q2 b q0q2

*/
