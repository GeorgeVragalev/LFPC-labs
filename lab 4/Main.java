import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {

        Chomsky chomsky = new Chomsky();

        System.out.println("Initial rules");
        chomsky.readInput();

        System.out.println("Create epsilon productions");
        while (chomsky.hasEpsilon() != ""){
            chomsky.findEpsilonProduction(chomsky.hasEpsilon());
        }
        chomsky.printHashmap();

        System.out.println("Remove dead states if they exist");
        chomsky.removeDeadStates();
        chomsky.printHashmap();

        System.out.println("Remove Unit productions");
        chomsky.findUnitStates();
        chomsky.printHashmap();

        System.out.println("Remove unreachable states");
        chomsky.checkReachableStates();
        chomsky.printHashmap();

        System.out.println("Create Chomsky transitions");
        chomsky.createChomsky(new ArrayList<>(chomsky.getProductions().keySet()));
        chomsky.printHashmap();
    }
}
/*
{S=[AC, a, EF, EA, ES, DG, DS, HJ, KI, EL, HL], A=[a, DS, HJ, KI, EL, HL],
 B=[AC, a, ES, DG, DS, HJ, KI, EL, HL], C=[AB], D=[a], E=[b], F=[AC], G=[AD],
  H=[EC], I=[CE], J=[DI], K=[ED], L=[DE], M=[CA]}

  {S=[AC, a, EF, EA, ES, DG, DS, HJ, KI, EL, HL], A=[a, DS, HJ, KI, EL, HL],
  B=[AC, a, ES, DG, DS, HJ, KI, EL, HL], C=[AB], D=[a], E=[b], F=[AC], G=[AD],
  H=[EC], I=[CE], J=[DI], K=[ED], L=[DE], M=[CA]}

  {S=[AC, a, EF, EA, ES, DG, DS, HJ, KI, EL, HL], A=[a, DS, HJ, KI, EL, HL],
  B=[AC, a, ES, DG, DS, HJ, KI, EL, HL], C=[AB], D=[a], E=[b], F=[AC], G=[AD],
  H=[EC], I=[CE], J=[DI], K=[ED], L=[DE], M=[CA]}
 */