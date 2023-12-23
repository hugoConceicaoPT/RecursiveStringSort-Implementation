package aed.sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

import aed.utils.TimeAnalysisUtils;



//podem alterar esta classe, se quiserem
class Limits
{
    char minChar;
    char maxChar;
    int maxLength;
}

public class RecursiveStringSort extends Sort
{
    private static final Random R = new Random();
    private static final int CUTOFF = 50;


    //esta implementação base do quicksort é fornecida para que possam comparar o tempo de execução do quicksort
    //com a vossa implementação do RecursiveStringSort
    public static <T extends Comparable<T>> void quicksort(T[] a)
    {
        qsort(a, 0, a.length-1);
    }

    private static <T extends Comparable<T>> void qsort(T[] a, int low, int high)
    {
        if (high <= low) return;
        int j = partition(a, low, high);
        qsort(a, low, j-1);
        qsort(a, j+1, high);
    }

    private static <T extends Comparable<T>> int partition(T[] a, int low, int high)
    {
        //partition into a[low...j-1],a[j],[aj+1...high] and return j
        //choose a random pivot
        int pivotIndex = low + R.nextInt(high+1-low);
        exchange(a,low,pivotIndex);
        T v = a[low];
        int i = low, j = high +1;

        while(true)
        {
            while(less(a[++i],v)) if(i == high) break;
            while(less(v,a[--j])) if(j == low) break;

            if(i >= j) break;
            exchange(a , i, j);
        }
        exchange(a, low, j);

        return j;
    }

    

    //método de ordenação insertionSort
    //no entanto este método recebe uma Lista de Strings em vez de um Array de Strings
    public static void insertionSort(List<String> a) 
    {
        int n = a.size();
        if(n < 2)
            return; 
        String[] aArray = a.toArray(new String[0]);

        for (int i = 1; i < n; i++) 
        {
            for (int j = i; j > 0; j--) 
            {
                if (less(aArray[j], aArray[j - 1])) 
                    exchange(aArray, j, j - 1);
                else break;
            }
        }
        for (int i = 0; i < n; i++) 
        {
            a.set(i, aArray[i]);
        }
    }

    public static Limits determineLimits(List<String> a, int characterIndex) 
    {
        Limits limits = new Limits();
    
        if (a == null || a.isEmpty()) return limits; 
    
        limits.minChar = Character.MAX_VALUE;
        limits.maxChar = Character.MIN_VALUE;
        limits.maxLength = 0;
    
        for (String string : a) 
        {
            if (string != null) 
            {
                int size = string.length();
                if (size > limits.maxLength) limits.maxLength = size;
    
                if (size > characterIndex) 
                {
                    char characterAtPosition = string.charAt(characterIndex);
                    if (less(characterAtPosition, limits.minChar))
                        limits.minChar = characterAtPosition;
                    if (!less(characterAtPosition, limits.maxChar) && (characterAtPosition != limits.maxChar))
                        limits.maxChar = characterAtPosition;
                }
                else 
                    limits.minChar = Character.MIN_VALUE;
            }
        }
    
        if (limits.minChar == Character.MAX_VALUE) limits.minChar = '\0';
    
        return limits;
    }
    

    //ponto de entrada principal para o vosso algoritmo de ordenação
    public static void sort(String[] a)
    {
        recursive_sort(Arrays.asList(a),0);
    }

	//mas este é que faz o trabalho todo
    @SuppressWarnings("unchecked")
    public static void recursive_sort(List<String> a, int depth)
    {
        if(a == null) return;
        if(a.size() < CUTOFF)
        {
            insertionSort(a);
            return;
        }

        Limits limits = determineLimits(a, depth);
        List<String> [] arrayOfBuckets =  new ArrayList[limits.maxChar-limits.minChar+1];
        List<String> emptyStringsBuckets  = new ArrayList<>();
        for (String string : a) // O(n)
        {
            if(string != null)
            {
                if (string.length() <= depth) 
                    emptyStringsBuckets.add(string);
                else
                {
                    int indexChar = string.charAt(depth) - limits.minChar;
                    if (arrayOfBuckets[indexChar] == null) 
                        arrayOfBuckets[indexChar] = new ArrayList<>();
                    arrayOfBuckets[indexChar].add(string);
                }
            }
        }

        int i = 0;
        for (String string : emptyStringsBuckets) a.set(i++, string); // O(n)

        for(List<String> bucket : arrayOfBuckets)
        {
            if(bucket != null)
            {
                recursive_sort(bucket, depth+1);
                for(String string : bucket)  a.set(i++, string); // O(n2)
            }
        }
    }  


    public static void fasterSort(String[] a) 
    {
        recursive_fasterSort(Arrays.asList(a), 0);
    }

    @SuppressWarnings("unchecked")
    public static void recursive_fasterSort (List<String> a, int depth)
    {
        if (a == null) return;
        if (a.size() < CUTOFF) {
            insertionSort(a);
            return;
        }
    
        List<String>[] arrayOfBuckets = new ArrayList[256];
        List<String>  emptyStringsBuckets = new ArrayList<>();
    
        for (String string : a) 
        {
            if (string.length() <= depth) 
                emptyStringsBuckets.add(string);
            else
            {
                int indexChar = string.charAt(depth) - Character.MIN_VALUE;
                if(indexChar >= arrayOfBuckets.length)
                {
                    int newLength = Math.max(indexChar + 1, arrayOfBuckets.length * 2);
                    arrayOfBuckets = Arrays.copyOf(arrayOfBuckets, newLength);
                }
                if (arrayOfBuckets[indexChar] == null) 
                    arrayOfBuckets[indexChar] = new ArrayList<>();
                arrayOfBuckets[indexChar].add(string);
            }
        }
    
        int i = 0;
        for (String string :  emptyStringsBuckets)  a.set(i++, string);

        for (List<String> bucket : arrayOfBuckets) 
        {
            if (bucket != null) 
            {
                recursive_fasterSort(bucket, depth+1);
                for (String string : bucket) a.set(i++, string);
            }
        }
    }
        
    public static <T extends Comparable<T>> void runSpatialComplexityAnalysis(Function<Integer, T[]> exampleGenerator,Consumer<T[]> sortMethod,int iterations) 
    {
        assert iterations > 0;
        int n = 256;

        long previousMemory = getUsedMemory(exampleGenerator.apply(n), sortMethod, 30);
        System.out.println("i\tarray size\theap memory(bytes)\testimated r");
        System.out.println("0\t" + n + "\t" + previousMemory + "\t ---");

        long newMemory;
        double doublingRatio;

        for (int i = 0; i < iterations; i++) {
            n *= 2;
            T[] newArray = exampleGenerator.apply(n);
            newMemory = getUsedMemory(newArray, sortMethod, 30);

            if (previousMemory > 0) {
                doublingRatio = (double) newMemory / previousMemory;
            } else {
                doublingRatio = 0;
            }

            previousMemory = newMemory;
            System.out.println(i + 1 + "\t" + n + "\t" + newMemory + "\t" + doublingRatio);
        }
    }

    public static <T extends Comparable<T>> long getUsedMemory(T[] array, Consumer<T[]> sortMethod, int trials) 
    {
        long usedMemory = 0;

        for (int i = 0; i < trials; i++) {
            T[] newArray = Arrays.copyOf(array, array.length); 
            System.gc(); 
            long beforeMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

            sortMethod.accept(newArray);

            long afterMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            usedMemory += afterMemory - beforeMemory;
        }

        return usedMemory / trials;
    }
    
    public static void main(String [] args)
    {
        int iterations = 30;
        Function<Integer, String[]> exampleArrayGenerator = capacity -> {
            String[] newArray = new String[capacity];
        
            for (int i = 0; i < capacity; i++) 
            {
                int length = R.nextInt(10) + 1; 
                char[] randomChars = new char[length];
                
                for (int j = 0; j < length; j++) {
                    char randomChar = (char) (R.nextInt(26) + 'A'); 
                    randomChars[j] = randomChar;
                }
        
                newArray[i] = new String(randomChars);
            }
            return newArray;
        };

        Function<Integer, String[]> exampleWorstCaseArrayGenerator = capacity -> {
            String[] newArray = new String[capacity];
            char[] characters = new char[capacity];

            for (int i = 0; i < capacity; i++) {
                Arrays.fill(characters, 'A');
                characters[capacity - 1] = (char) ('A' + i);
                newArray[i] = new String(characters);
            }
            return newArray;
        };

        System.out.println("RecursiveSort Caso Médio Operation");
        Consumer<String[]> methodToTestRecursiveSort = array -> sort(array);
        TimeAnalysisUtils.runDoublingRatioTest(exampleArrayGenerator, methodToTestRecursiveSort, iterations);

        System.out.println("RecursiveSort Pior Caso Operation");
        TimeAnalysisUtils.runDoublingRatioTest(exampleWorstCaseArrayGenerator, methodToTestRecursiveSort, 5);

        System.out.println("FasterSort Operation");
        Consumer<String []> methodToTestFasterSort = array -> fasterSort(array);
        TimeAnalysisUtils.runDoublingRatioTest(exampleArrayGenerator, methodToTestFasterSort, iterations);

        System.out.println("QuickSort Operation");
        Consumer<String []> methodToTestQuickSort = array -> quicksort(array);
        TimeAnalysisUtils.runDoublingRatioTest(exampleArrayGenerator, methodToTestQuickSort, iterations);

        System.out.println("MemoryRecursiveSort Operation");
        runSpatialComplexityAnalysis(exampleArrayGenerator, methodToTestRecursiveSort, iterations);

    }
}
