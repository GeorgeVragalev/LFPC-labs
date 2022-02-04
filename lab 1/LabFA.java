package LABFA;
import java.util.ArrayList;
import java.util.Scanner;

public class LabFA {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        System.out.println("Provide your input below. When finished type !!!\"exit\"!!!");
        ArrayList<ArrayList<Edge>> adjList = new ArrayList<>();
        ArrayList<Character> vertices = new ArrayList<>();
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
        FA.printGraph();
        if (FA.isValid("abc")){
            System.out.println("Correct");
        }else
            System.out.println("Wrong");

        if (FA.isValid("aaaabac")){
            System.out.println("Correct");
        }else
            System.out.println("Wrong");

        if (FA.isValid("aaaabbbabac")){
            System.out.println("Correct");
        }else
            System.out.println("Wrong");
    }
}
/*
SAMPLE INPUT
S aB
B aC
C bB
C c
C aS
B bB
exit

Corresponding Output:
Adjacency list of vertex: S --> B(a)

Adjacency list of vertex: B --> C(a)  --> B(b)

Adjacency list of vertex: C --> B(b)  -->  End Node (c)  --> S(a)
 */
