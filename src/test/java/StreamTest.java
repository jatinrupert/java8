import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StreamTest {

    private final OutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void stream(){

        Stream.of(1, 1, 3, 2, 4, 3)
                .peek(System.out::print)
                .limit(3)
                .distinct()
                .forEach(System.out::print);
        assertEquals("11133", outContent.toString());
    }

    @Test
    public void optional(){
        int min1 = Arrays.stream(new int[]{1, 2, 3, 4, 5})
                .min()
                .orElse(0);
        assertEquals(1, min1);

        int min2 = Arrays.stream(new int[]{})
                .min()
                .orElse(0);
        assertEquals(0, min2);

    }

    @Test
    public void streamTerminal() {
        System.out.println("Stream without terminal operation");

        Arrays.stream(new int[] { 1, 2, 3 }).map(i -> {
            System.out.println("doubling " + i);
            return i * 2;
        });

        System.out.println("Stream with terminal operation");
        Arrays.stream(new int[] { 1, 2, 3 }).map(i -> {
            System.out.println("doubling " + i);
            return i * 2;
        }).sum();
    }

    @Test
    public void flatmap() {
        Map<String, List<String>> people = new HashMap<>();
        people.put("John", Arrays.asList("555-1123", "555-3389"));
        people.put("Mary", Arrays.asList("555-2243", "555-5264"));
        people.put("Steve", Arrays.asList("555-6654", "555-3242"));

        List<String> phones = people.values().stream()
                .flatMap(Collection::stream)
                .collect(Collectors.toList());

        phones.forEach(System.out::println);

    }

    @Test
    public void orElseGet() {
        String name1 = Optional.of("test")
                .orElse(this.getRandom());
        System.out.println(name1);

        String name2 = Optional.of("test")
                .orElseGet(this::getRandom);
        System.out.println(name2);

        assertEquals("random\r\n" +
                "test\r\n" +
                "test\r\n", outContent.toString());
    }

    @Test
    public void consumer() {

        List<String> cities = new ArrayList<>();
        cities.add("Delhi");
        cities.add("Mumbai");
        cities.add("Goa");
        cities.add("Pune");

        Consumer<String> printConsumer= System.out::println;
        cities.forEach(printConsumer);
    }

    @Test
    public void predicate() {

        List<String> cities = new ArrayList<>();
        cities.add("Delhi");
        cities.add("Mumbai");
        cities.add("Goa");
        cities.add("Pune");

        Predicate<String> filterCity = city -> city.equals("Mumbai");
        cities.stream().filter(filterCity).forEach(System.out::println);
    }

    @Test
    public void function() {

        List<String> cities = new ArrayList<>();
        cities.add("Delhi");
        cities.add("Mumbai");
        cities.add("Goa");
        cities.add("Pune");

        Function<String, Character> getFirstCharFunction = city -> city.charAt(0);
        cities.stream().map(getFirstCharFunction)
                .forEach(System.out::println);
    }

    @Test
    public void supplier() {

        Supplier<String[]> citySupplier = () -> new String[]{"Mumbai", "Delhi", "Goa", "Pune"};
        Arrays.asList(citySupplier.get()).forEach(System.out::println);
    }

    @Test
    public void bifunction() {
        BiFunction<Integer, Integer, Integer> func = Integer::sum;

        Integer result = func.apply(2, 3);

        System.out.println(result); // 5

        // take two Integers and return an Double
        BiFunction<Integer, Integer, Double> func2 = Math::pow;

        Double result2 = func2.apply(2, 4);

        System.out.println(result2);    // 16.0

        // take two Integers and return a List<Integer>
        BiFunction<Integer, Integer, List<Integer>> func3 = (x1, x2) -> List.of(x1 + x2);

        List<Integer> result3 = func3.apply(2, 3);

        System.out.println(result3);
    }

    @Test
    public void biandfunction() {
        // Math.pow(a1, a2) returns Double
        BiFunction<Integer, Integer, Double> func1 = Math::pow;

        // takes Double, returns String
        Function<Double, String> func2 = (input) -> "Result : " + input;

        String result = func1.andThen(func2).apply(2, 4);

        System.out.println(result);
    }

    @Test
    public void bipredicate() {
        BiPredicate<String, Integer> filter = (x, y) -> x.length() == y;

        boolean result = filter.test("mkyong", 6);
        assertEquals(true, result);  // true

        boolean result2 = filter.test("java", 10);
        assertEquals(false, result2);  // true
    }

    @Test
    public void biconsumer() {
        BiConsumer<Integer, Integer> addTwo = (x, y) -> System.out.print(x + y);
        addTwo.accept(1, 2);    // 3
        assertEquals("3", outContent.toString());
    }

    @Test
    public void random() {
        Random random = new Random();

        random.ints().limit(10).forEach(System.out::println);
    }

    @Test
    public void reduce() {

        List<Integer> integers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        Integer minimum = integers.stream().reduce((x, y) -> x.compareTo(y) <= 0  ? x : y).get();
        assertEquals(Integer.valueOf(2), minimum);
    }

    @Test
    public void sum() {

        List<Integer> integers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        Integer reduceMinimum = integers.stream().reduce(Integer::sum).get();
        assertEquals(Integer.valueOf(25), reduceMinimum);

        Integer minimum = integers.stream().mapToInt(Integer::intValue).sum();
        assertEquals(Integer.valueOf(25), minimum);
    }

    @Test
    public void summarystatistics() {
        List<Integer> integers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);

        IntSummaryStatistics stats = integers.stream().mapToInt(x -> x).summaryStatistics();

        System.out.println("Lowest number in List : " + stats.getMin());
    }

    @Test
    public void spliterator() {
        List<String> names = new ArrayList<>();
        names.add("Rams");
        names.add("Posa");
        names.add("Chinni");

        // Getting Spliterator
        Spliterator<String> namesSpliterator = names.spliterator();

        // Traversing elements
        namesSpliterator.forEachRemaining(System.out::println);
    }

    @Test
    public void methodreference() {
        List<String> names = new ArrayList<>();

            names.add("John");
            names.add("Alice");
            names.add("Bob");
            names.add("Emily");

        // Method reference to static method
            names.forEach(System.out::println);

        // Method reference to instance method of a particular object
            names.forEach(String::toUpperCase);

        // Method reference to instance method of an arbitrary object of a particular type
            names.sort(String::compareToIgnoreCase);

        // Method reference to constructor
            names.stream()
                    .map(String::new)
                    .forEach(System.out::println);
    }

    @Test
    public void instream() {
        // Create an IntStream from 1 to 5 (inclusive)
        IntStream intStream = IntStream.rangeClosed(1, 5);

        // Print the elements of the IntStream
            intStream.forEach(System.out::println);
    }

    @Test
    public void grouping() {
        List<String> fruits = Arrays.asList("Apple", "Banana", "Cherry", "Date", "Apple", "Banana");

        // Example 1: Grouping fruits by their length
        Map<Integer, List<String>> groupedByLength = fruits.stream()
                .collect(Collectors.groupingBy(String::length));
        System.out.println("Grouped by length: " + groupedByLength);

        // Example 2: Partitioning fruits into two groups: odd length and even length
        Map<Boolean, List<String>> partitionedByLength = fruits.stream()
                .collect(Collectors.partitioningBy(fruit -> fruit.length() % 2 == 0));
        System.out.println("Partitioned by length: " + partitionedByLength);

        // Example 3: Counting the occurrences of each fruit
        Map<String, Long> fruitCounts = fruits.stream()
                .collect(Collectors.groupingBy(fruit -> fruit, Collectors.counting()));
        System.out.println("Fruit counts: " + fruitCounts);

        // Example 4: Mapping fruit names to their lengths
        List<Integer> fruitLengths = fruits.stream()
                .collect(Collectors.mapping(String::length, Collectors.toList()));
        System.out.println("Fruit lengths: " + fruitLengths);
    }

    @Test
    public void stringjoiner() {
        StringJoiner joiner = new StringJoiner(", "); // Create a StringJoiner with the delimiter ', '

        // Add strings to the joiner
        joiner.add("Hello");
        joiner.add("World");
        joiner.add("!");

        // Concatenate the strings with the delimiter
        String result = joiner.toString();

        System.out.println(result); // Output: Hello, World, !
    }

    @Test
    public void ofNullable() {
        String name = "test";
        Optional<String> opt = Optional.ofNullable(name);
        assertTrue(opt.isPresent());
    }

    @Test
    public void ifPresent() {
        Optional<String> opt = Optional.of("baeldung");
        opt.ifPresent(name -> System.out.println(name.length()));
    }

    @Test
    public void givenNonNull_whenCreatesNonNullable_thenCorrect() {
        String name = "test";
        Optional<String> opt = Optional.of(name);
        assertTrue(opt.isPresent());

        name = null;
        assertFalse(Optional.ofNullable(name).isPresent());
    }

    @Test(expected = NullPointerException.class)
    public void givenNull_whenThrowsErrorOnCreate_thenCorrect() {
        String name = null;
        Optional.of(name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenOrElseThrowWorks_thenCorrect() {
        String nullName = null;
        String name = Optional.ofNullable(nullName).orElseThrow(
                IllegalArgumentException::new);
    }

    private String getRandom() {
        System.out.println("random");
        return "random";
    }

}
