import java.util.Scanner;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class WordPuzzle{
    public static void main(String[] args) throws Exception{
        //Taking user input for rows and columns
        Scanner sc = new Scanner(System.in);
        System.out.println("Please input a value for the rows of the grid in range of (10 - 40): ");
        int rows = sc.nextInt();
        if(rows <10 || rows>40){
            sc.close();
            throw new Exception("Invalid Input");
        }
        System.out.println("Please input a value for the columns of the grid in range of (10 - 40): ");
        int columns = sc.nextInt();
        if(columns <10 || columns>40){
            sc.close();
            throw new Exception("Invalid Input");
        }
        char a[][] = new char[10000][10000];
        sc.close();

        //Declating Hash table , Helper hashmap and final map which will store words
        QuadraticProbingHashTable<String> qpht = new QuadraticProbingHashTable<String>();
        HashMap<String, Integer> finalMap = new HashMap<>();
        HashMap<String, Integer> helperHash = new HashMap<>();

        //creating a random grid
        System.out.println("Created grid of random characters: ");
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                a[i][j] = (char) (Math.random() * 26 + 'a');
                System.out.print(a[i][j] + " ");
            }
            System.out.print('\n');
        }
        try{
            //Reading words from dictionary
            FileReader fr = new FileReader("dictionary.txt");
            BufferedReader bufferedReader = new BufferedReader(fr);

            String dictWords;
            while((dictWords = bufferedReader.readLine()) != null){   
                String currWord = new String();
                currWord = "";
                //storing each prefix of the word for enhancement
                for(int i = 0; i < dictWords.length(); i++){
                    currWord = currWord + String.valueOf(dictWords.charAt(i));
                    if(helperHash.get(currWord) == null){
                        //hashmap storing all prefix for later use
                        helperHash.put(currWord,1);
                    }
                }
                //addding word in dictionary
                qpht.insert(dictWords);
            }
            bufferedReader.close();
        }catch(IOException e){
            System.out.println("File not found");
        }

        //Starting to calculate running time after creating table
        long startTime = System.nanoTime();
        //read words form the grid and write them into a array list
        ArrayList<String> gridWordList = new ArrayList<>();
        ArrayList<String> finalList = new ArrayList<>();
        String newWord = new String();
        //Going through all possible combination of words
        for(int i = 0; i < rows; i++){
            for(int j = 0; j < columns; j++){
                newWord = "";
                for(int m = j; m >= 0; m--){
                    newWord = newWord + String.valueOf(a[i][m]);
                    //Check if prefix is present in hashmap
                    if (helperHash.get(newWord) == null){
                        break;
                    }
                    gridWordList.add(newWord);
                }
                newWord = String.valueOf(a[i][j]);
                //Check if prefix is present in hashmap
                if (helperHash.get(newWord) == null){
                    break;
                }

                for(int m = j + 1; m < columns; m++){
                    newWord = newWord + String.valueOf(a[i][m]);
                    //Check if prefix is present in hashmap
                    if (helperHash.get(newWord) == null){
                        break;
                    }
                    gridWordList.add(newWord);
                }

                newWord = String.valueOf(a[i][j]);
                for(int m = i - 1; m >= 0; m--){
                    newWord = newWord + String.valueOf(a[m][j]);
                    //Check if prefix is present in hashmap
                    if (helperHash.get(newWord) == null){
                        break;
                    }
                    gridWordList.add(newWord);
                }

                newWord = String.valueOf(a[i][j]);
                for(int m = i + 1; m < rows; m++){
                    newWord = newWord + String.valueOf(a[m][j]);
                    //Check if prefix is present in hashmap
                    if (helperHash.get(newWord) == null){
                        break;
                    }
                    gridWordList.add(newWord);
                }

                newWord = String.valueOf(a[i][j]);
                for(int q = i - 1, r = j - 1; q >= 0 && r >= 0; q--, r--){
                    newWord = newWord + String.valueOf(a[q][r]);
                    //Check if prefix is present in hashmap
                    if(helperHash.get(newWord) == null){
                        break;
                    }
                    gridWordList.add(newWord);
                }

                newWord = String.valueOf(a[i][j]);
                for(int q = i - 1, r = j + 1; q >= 0 && r < columns; q--, r++){
                    newWord = newWord + String.valueOf(a[q][r]);
                    //Check if prefix is present in hashmap
                    if (helperHash.get(newWord) == null){
                        break;
                    }
                    gridWordList.add(newWord);
                }

                newWord = String.valueOf(a[i][j]);
                for(int q = i + 1, r = j - 1; q < rows && r >= 0; q++, r--){
                    newWord = newWord + String.valueOf(a[q][r]);
                    //Check if prefix is present in hashmap
                    if (helperHash.get(newWord) == null){
                        break;
                    }
                    gridWordList.add(newWord);
                }

                newWord = String.valueOf(a[i][j]);
                for(int q = i + 1, r = j + 1; q < rows && r < columns; q++, r++){
                    newWord = newWord + String.valueOf(a[q][r]);
                    //Check if prefix is present in hashmap
                    if (helperHash.get(newWord) == null){
                        break;
                    }
                    gridWordList.add(newWord);
                }
            }
        }

        //output the words
        System.out.println('\n' + "Words in this grid after enhancement");
        for(int m = 0; m < gridWordList.size(); m++){
            System.out.println(gridWordList.get(m));
        }

        System.out.println('\n' + "Words present in the dictionary :\n");
        //check if any of the words exist in the grid by searching hash table
        for(int m = 0; m < gridWordList.size(); m++){
            if(qpht.contains(gridWordList.get(m)) && finalMap.get(gridWordList.get(m)) == null){
                //adding words in final list and avoiding duplicates
                finalList.add(gridWordList.get(m));
                finalMap.put(gridWordList.get(m), 1);
                System.out.println("Searched word from hash table dictionary is: " + gridWordList.get(m));
            }
        }
        //sorting the final list
        Collections.sort(finalList);
        System.out.println("Dictionary words after sorting:");
        for(String counter: finalList){
            System.out.println(counter);
        }
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        double elapsedTimeInSecond = (double) totalTime / 1_000_000_000;
        //calculating the total runtime of program and printing
        System.out.println("Execution time is " + elapsedTimeInSecond + " seconds");
        System.out.println("END");
    }
}