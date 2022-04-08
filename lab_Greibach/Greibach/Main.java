package Greibach;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Greibach greibach = new Greibach();

        System.out.println("Initial rules");
        greibach.readInput();

        System.out.println("Create epsilon productions");
        while (!greibach.hasEpsilon().equals("")){
            greibach.findEpsilonProduction(greibach.hasEpsilon());
        }
        greibach.printHashmap();

        System.out.println("Remove dead states if they exist");
        greibach.removeDeadStates();
        greibach.printHashmap();

        System.out.println("Remove Unit productions");
        greibach.findUnitStates();
        greibach.printHashmap();

        System.out.println("Remove unreachable states");
        greibach.checkReachableStates();
        greibach.printHashmap();

//        System.out.println("Create Chomsky transitions");
//        greibach.createChomsky(new ArrayList<>(greibach.getProductions().keySet()));
//        greibach.printHashmap();

        System.out.println("Greibach Normal Form");
        greibach.initialise();
        while (!greibach.findGreibachTransitions()){
            //break;
        }
        greibach.printHashmap();
    }
}
