import java.util.*;
import java.util.stream.*;

public class Week10_Labs {
    public static void main(String[] args) {
        List<String> fruit = Arrays.asList("cherry","banana","berry","apple","cherry","kiwi","fig","date","lemon","honeydew","cherry","elderberry","apple","banana","grape");
    
        // 1) Collect elements into a set
        Set<String> fruitSet = fruit.stream() 
                                .collect(Collectors.toSet());
        // [banana, date, cherry, apple, honeydew, berry, kiwi, lemon, fig, grape, elderberry]

        // 2) Collect the fruit into groups based on their first character
        Map<Character,List<String>>groupByLength = fruit.stream()
                                .collect(Collectors.groupingBy(s->s.charAt(0)));
        // {a=[apple, apple], b=[banana, berry, banana], c=[cherry, cherry, cherry], d=[date], e=[elderberry], f=[fig], g=[grape], h=[honeydew], k=[kiwi], l=[lemon]}

		// 3) Group fruit by the length of the name
        Map<Integer, List<String>> groupByFruitLength = fruit.stream()
                                .collect(Collectors.groupingBy(String::length));

		// 4) Collect the fruit that has erry in it
        List<String>containsErry = fruit.stream()
                                .filter(s->s.contains("erry"))
                                .collect(Collectors.toList());
        // [cherry, berry, cherry, cherry, elderberry]

		// 5) Create a partition of fruit based on if it contains erry
        Map<Boolean, List<String>> partitionErry = fruit.stream()  
                                .collect(Collectors.partitioningBy(s->s.contains("erry")));
        // {false=[banana, apple, kiwi, fig, date, lemon, honeydew, apple, banana, grape], true=[cherry, berry, cherry, cherry, elderberry]}

		// 6) collect/ the fruit that has 5 or less symbols
        List<String> shortFruits = fruit.stream()  
                                .filter(s->s.length()<= 5)
                                .collect(Collectors.toList());
        // [berry, apple, kiwi, fig, date, lemon, apple, grape]

		// 7) find the total number of symbols in all the fruit stored
        int totalSymbols = fruit.stream()  
                        .mapToInt(String::length)
                        .sum();
        // 84


		List<Integer> data = Arrays.asList(87, 23, 45, 100, 6, 78, 92, 44, 13, 56, 34, 99, 82, 19, 1012, 78, 45, 90, 23, 56, 78, 100, 3, 43, 67, 89, 21, 34, 10);

        // 8) Partition data based on if >=50
        Map<Boolean, List<Integer>> part50 = data.stream() 
                        .collect(Collectors.partitioningBy(n->n>=50));
        // {false=[23, 45, 6, 44, 13, 34, 19, 45, 23, 3, 43, 21, 34, 10], true=[87, 100, 78, 92, 56, 99, 82, 1012, 78, 90, 56, 78, 100, 67, 89]}

		// 9) divide data into groups based on the remainder when divided by 7
        Map<Integer, List<Integer>> groupByRemainder7 = data.stream()
                        .collect(Collectors.groupingBy(n->n % 7));
        // {0=[56, 56, 21], 1=[78, 92, 99, 78, 78, 43], 2=[23, 100, 44, 23, 100], 3=[87, 45, 45, 3, 10], 4=[1012, 67], 5=[82, 19, 89], 6=[6, 13, 34, 90, 34]}  

		// 10) find the sum of the data
        int sumData = data.stream()
                        .mapToInt(Integer::intValue)
                        .sum();
        // 2527

		// 11) collect the unique values
        List<Integer> uniqueValues = data.stream()
                        .distinct()
                        .collect(Collectors.toList());
        // [87, 23, 45, 100, 6, 78, 92, 44, 13, 56, 34, 99, 82, 19, 1012, 90, 3, 43, 67, 89, 21, 10]

        // 12) compute the cube of each values
        List<Integer> cubes = data.stream()
                        .map(n->n*n*n)
                        .collect(Collectors.toList());
        // [658503, 12167, 91125, 1000000, 216, 474552, 778688, 85184, 2197, 175616, 39304, 970299, 551368, 6859, 1036433728, 474552, 91125, 729000, 12167, 175616, 474552, 1000000, 27, 79507, 300763, 704969, 9261, 39304, 1000]

		// 13) find the sum of the cubes of each value
        long sumOfCubes = data.stream() 
                        .mapToLong(n-> (long) n*n*n)
                        .sum();
        // 1045371649

		// 14) increase the value of each element by 5
        List<Integer> increasedBy5 = data.stream()  
                        .map(n-> n+5)
                        .collect(Collectors.toList());
        // [92, 28, 50, 105, 11, 83, 97, 49, 18, 61, 39, 104, 87, 24, 1017, 83, 50, 95, 28, 61, 83, 105, 8, 48, 72, 94, 26, 39, 15]

		// 15) compute the cube of the even values
        List<Integer> evenCubes = data.stream() 
                        .filter(n-> n % 2 == 0)
                        .map(n->n*n*n)
                        .collect(Collectors.toList());
        // [1000000, 216, 474552, 778688, 85184, 175616, 39304, 551368, 1036433728, 474552, 729000, 175616, 474552, 1000000, 39304, 1000]
    
    // Put all variables into a list
    List<Object> allResults = Arrays.asList(fruitSet, groupByLength, groupByFruitLength, 
        containsErry, partitionErry, shortFruits, totalSymbols, part50, groupByRemainder7,
        sumData, uniqueValues, cubes, sumOfCubes, increasedBy5, evenCubes
    );
    // Print that list of variables
    allResults.forEach(res -> {
    System.out.println(res);
    System.out.println(); 
});



    }



}
