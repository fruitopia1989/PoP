public class Computations {

    public static int fibonacci(int n)
    {
        if (n<0) 
        {
            throw new IllegalArgumentException("n must be non-negative");
        }
        if (n==0) return 0;
        if (n==1) return 1;
        return fibonacci(n-1)+fibonacci(n-2);
    }
	
    public static boolean isPrime(int n)
    {
        if (n<=1) return false;
        for (int i = 2; i <= Math.sqrt(n); i++)
        {
            if (n % i == 0) return false;
        }
        return true;
    }
	
    public static boolean isEven(int number) { return number % 2 == 0; }
    public static boolean isOdd(int number) { return !isEven(number); }
	
    public static double toCelsius(double fahrenheit) { return (fahrenheit - 32) * 5.0 / 9.0; }
    public static double toFahrenheit(double celsius) { return (celsius * 9.0 / 5.0) + 32; }
}
