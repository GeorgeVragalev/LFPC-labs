import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Chomsky {
    private LinkedHashMap<String, ArrayList<String>> productions = new LinkedHashMap<>();

    public void readInput() throws IOException {
        File file = new File("C:\\Users\\vraga\\OneDrive\\Desktop\\UNI\\LFPC\\LFPC labs\\lab 4\\src\\input.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String string;
        while ((string = br.readLine()) != null) { //A aB
            if (!productions.containsKey(string.substring(0, 1))) {
                ArrayList<String> arrayList = new ArrayList<>();
                arrayList.add(string.substring(2));
                productions.put(string.substring(0, 1), arrayList);
            } else {
                productions.get(string.substring(0, 1)).add(string.substring(2));
            }
        }
        System.out.println(productions);
    }

    //////////////////////////Finds the key with has an epsilon production: C-> # //////////////////////////
    // loops through the hashmap and finds the key that contains an empty state
    public String hasEpsilon() {
        for (String nonTerminal : productions.keySet()) {
            for (int i = 0; i < productions.get(nonTerminal).size(); i++) {
                if (productions.get(nonTerminal).contains("#")) {
                    return nonTerminal;
                }
            }
        }
        return "";
    }

    // loops through the productions and creates null states with the already found epsilon state
    // the empty state key gets passed into this function which finds each occurrence of this key and calls another function to create combinations with it
    public void findEpsilonProduction(String epsilon) {
        for (String key : productions.keySet()) {
            for (int values = 0; values < productions.get(key).size(); values++) {
                // if current string contatins epsilon key then we call a function that create combinations with that string and stores the result in an array
                if (productions.get(key).get(values).contains(epsilon)) {
                    ArrayList<String> combinations = new ArrayList<>();
                    createEpsilonProductions(productions.get(key).get(values), epsilon, combinations);
                    //copy result strings into the key's array if it doesn't contain it already
                    for (String combo : combinations) {
                        if (!productions.get(key).contains(combo))
                            productions.get(key).add(combo);
                    }
                }
            }
        }
        //remove the epsilon key
        productions.get(epsilon).remove("#");
    }

    //function that create epsilon combinations of a given production string and stores the result in an array
    public static void createEpsilonProductions(String production, String epsilon, ArrayList<String> result) {
        StringBuilder copy;
        copy = new StringBuilder(production); // we use a copy to keep track of our original string
        for (int i = 0; i < production.length(); i++) {
            //if we find a epsilon key then we delete it and store the result in the array as a new combination
            if (epsilon.equals(String.valueOf(production.charAt(i)))) {
                copy.deleteCharAt(i);
                if (!result.contains(String.valueOf(copy)) && production.length() != 1) {
                    result.add(String.valueOf(copy));
                }
                //we call the function recursively but already with the new copy string as the production
                createEpsilonProductions(copy.toString(), epsilon, result);
                //we reset the copy string back to the production for the next iteration
                copy = new StringBuilder(production);
            }
        }
    }
////////////////////////////////////////////////////////////////////////////

    /////////////////////   Removes states that have no productions like C->___  , this happens if C->epsilon , at the start    /////////////////
    public void removeDeadStates() {
        for (String key : productions.keySet()) {
            if (productions.get(key).size() == 0) {
                // if a key has an empty array we remove all occurrences of this key in the hashmap
                findStatesContainingDeadStates(key);
                productions.remove(key);
                //we call remove dead states again to make sure we don't have any other new states after deletion
                removeDeadStates();
                break;
            }
        }
    }

    //Finds all the states that have dead states: C -> # then AC and CA will be removed
    public void findStatesContainingDeadStates(String deadState) {
        //we loop through the hashmap and if a value contains a dead key, meaning a key which doesn't have any productions then we remove this value
        for (String key : productions.keySet()) {
            for (int value = 0; value < productions.get(key).size(); value++) {
                if (productions.get(key).get(value).contains(deadState)) {
                    productions.get(key).remove(value);
                    //we call the fucntion again to make sure we don't have any other dead key occurrences since the size already changed
                    findStatesContainingDeadStates(deadState);
                    break;
                }
            }
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    /////////////////////// Unit transitions ///////////////////////////////////////////////////////
    //loops through hashmap and find single capital letters
    public void findUnitStates() {
        String production;
        for (String key : productions.keySet()) {
            for (int value = 0; value < productions.get(key).size(); value++) {
                production = productions.get(key).get(value);
                // if length is 1 and it is uppercase
                if (production.length() == 1 && production.equals(production.toUpperCase())) {
                    // we create unit production of the production key
                    unitProduction(key, production);
                    productions.get(key).remove(production);
                    findUnitStates();
                    break;
                }
            }
        }
    }

    public void unitProduction(String key, String unitProd) {
        // we copy the contents of the unitProd key array into the key array
        for (String value : productions.get(unitProd)) {
            if (!productions.get(key).contains(value))
                productions.get(key).add(value);
        }
    }
//////////////////////////////////////////////////////////////////////////////

    // we check the reachable states starting from S
    public void checkReachableStates() {
        //condition if it needs to be checked again
        boolean checkAgain = false;
        ArrayList<String> reachableKeys = new ArrayList<>();
        reachableKeys.add("S");
        for (int key = 0; key < reachableKeys.size(); key++) {
            String currentKey = reachableKeys.get(key);
            // if the keySet contains the current key we are looping though
            if (productions.containsKey(currentKey)) {
                for (int keyValue = 0; keyValue < productions.get(currentKey).size(); keyValue++) {
                    //loop through each letter in the current string
                    for (char letter : productions.get(currentKey).get(keyValue).toCharArray()) {
                        // if it's uppercase and it doesn't exist in the array then we add it to the reachableKeys array
                        if (Character.isUpperCase(letter) && !reachableKeys.contains(String.valueOf(letter)))
                            reachableKeys.add(String.valueOf(letter));
                    }
                }
            }
            // if it doesn't contain we have to remove all the occurrences of the currentKey in the hashmap
            else { //if  A-> Cab  but C isn't a production key then we remove all occurrences of C
                removeUnusedKeys(currentKey);
                reachableKeys.remove(currentKey);
                checkAgain = true; // we check again since the result may have new unreachable states
            }
        }

        //we remove a key from hashmap if the reachableKeys array doesn't contain a key from the keySet
        System.out.println("Reacheable keys" + reachableKeys);

        ArrayList<String> keysToRemove = new ArrayList<>();
        for (String key : productions.keySet()) {
            if (!reachableKeys.contains(key)) {
                keysToRemove.add(key);
            }
        }
        for (String  key: keysToRemove) {
            productions.remove(key);
        }

        if (checkAgain)
            checkReachableStates();
    }

    // remove occurrence of deadKey from the hashmap
    //if  A-> Cab  but C isn't a production key then we remove all occurrences of C
    public void removeUnusedKeys(String deadKey) {
        ArrayList<String> toRemove = new ArrayList<>();
        for (String key : productions.keySet()) {
            for (int value = 0; value < productions.get(key).size(); value++) {
                // if current string contains a deadKey then we add it to the toRemove array
                if (productions.get(key).get(value).contains(deadKey))
                    toRemove.add(productions.get(key).get(value));
            }
            // remove all productions from the key array that contain the dead Key
            productions.get(key).removeAll(toRemove);
        }
    }
//////////////////////////////////////////////////////////////////////////////////////////

    //fucntion that loop through each state and calls other functions based on the type
    public void createChomsky(ArrayList<String> passed) {
        //We loop through our keys array while it's not empty
        if (!passed.isEmpty()) {
            for (String key : passed) {
                //we loop through each production
                for (int value = 0; value < productions.get(key).size(); value++) {
                    String currentKey = productions.get(key).get(value);
                    //if it's not a valid production A->a or A->BC
                    if (!validProduction(currentKey)) {
                        String newProduction = "";

                        //Simple case of length 2 like Aa ->  AX where X->a
                        if (currentKey.length() == 2) {
                            newProduction = createProduction(currentKey);
                        }
                        //bCaCb returns FG for example so we replace bCaCb with FG
                        else {
                            newProduction = splitProduction(productions.get(key).get(value));
                        }
                        //add new transition and key to hashmap
                        ArrayList<String> newList = productions.get(key);
                        newList.remove(value);
                        newList.add(newProduction);
                        productions.replace(key, newList);
                        //call function again since the array is now resized
                        createChomsky(passed);
                        break;
                    }
                }
                //printHashmap();
                //remove the key from passed array, so we don't loop through it again
                passed.remove(key);
                createChomsky(passed);
                break;
            }
        }
    }

    public boolean validProduction(String production) {
        //if the production is of the form A -> AB  or A -> a then it's valid
        return (production.length() == 2 && production.equals(production.toUpperCase())) || production.length() == 1 && production.equals(production.toLowerCase());
    }

    //recursive function that takes a production as input for example bCab and
    public String splitProduction(String production) { // Aa | aAa
        //create a single production
        if (production.length() == 1) {
            return createFinalProduction(production);
        }
        //create a double production of the form: AA aa Aa
        else if (production.length() == 2) {
            String newProd = createProduction(production);
            return createFinalProduction(newProd);
        } else {
            int half = production.length() / 2;
            String firstHalf = splitProduction(production.substring(0, half));
            String secondHalf = splitProduction(production.substring(half));

            if (firstHalf.length() == 2) {
                firstHalf = createFinalProduction(firstHalf);
            }
            if (secondHalf.length() == 2) {
                secondHalf = createFinalProduction(secondHalf);
            }
            return firstHalf + secondHalf;
        }
    }

    //returns an existing production or creates a new one if it doesn't exist
    public String createFinalProduction(String production) {
        for (String key : productions.keySet()) {
            if (productions.get(key).size() == 1) {
                if (productions.get(key).contains(production)) {
                    return key;
                }
            }
        }
        return addTransition(production);
    }

    public String createProduction(String production) { //aA or Aa -> AX
        if (production.equals(production.toLowerCase())) {
            return createLowerCaseProduction(production);
        }
        else if(production.equals(production.toUpperCase())){
            return production;
        }
        else {
            //get the small letter from a double production like Aa
            String smallLetter = "";
            for (int i = 0; i < production.length(); i++)
                if (Character.isLowerCase(production.charAt(i)))
                    smallLetter = String.valueOf(production.charAt(i));

            //check if there exists a transition that goes into a
            String newKey = "";
            for (String key : productions.keySet())
                if (productions.get(key).size() == 1 && productions.get(key).contains(smallLetter))
                    newKey = key;

            //if it doesn't exist we create a new tranistion
            if (newKey.equals(""))
                newKey = addTransition(smallLetter);

            // we replace the obtained production with the small letter
            return production.replace(smallLetter, newKey);
        }
    }

    // deal with productions of the form aa |  aba
    public String createLowerCaseProduction(String production) {
        StringBuilder newProduction = new StringBuilder();
        //We check for existing characters and if there doesnt exist we create a new transition and continue to the next letter
        for (char letter : production.toCharArray()) {
            boolean exists = false;
            for (String key : productions.keySet()) {
                if (productions.get(key).size() == 1 && productions.get(key).contains(String.valueOf(letter))) {
                    newProduction.append(key);
                    exists = true;
                    break;
                }
            }
            //create a new transition for the lower case letter
            if (!exists) {
                String newKey = addTransition(String.valueOf(letter));
                newProduction.append(newKey);
            }
        }
        return newProduction.toString();
    }

    // add a new transition to the hashmap
    public String addTransition(String production){
        ArrayList<String> singleList = new ArrayList<>();
        singleList.add(String.valueOf(production));
        String newKey = getValidKey();
        productions.put(newKey, singleList);
        return newKey;
    }

    // get a valid new key
    public String getValidKey() {
        String keys = "ZYXWVUTSRQPONMLKJIHGFEDCBA";
        for (char key : keys.toCharArray()) {
            //returns a key which is not present in map
            if (!productions.containsKey(String.valueOf(key))) {
                return String.valueOf(key);
            }
        }
        return "";
    }

    public LinkedHashMap<String, ArrayList<String>> getProductions() {
        return productions;
    }

    //I do not know what this function does
    public void printHashmap() {
        System.out.println(productions);
        System.out.println();
    }

}
