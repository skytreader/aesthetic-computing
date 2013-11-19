package net.skytreader.kode.AestheticComputing.PrimeEncoding;

import net.skytreader.kode.utils.GenericArray;

import java.util.Vector;
import java.util.Arrays;
import java.util.Stack;

public class PrimeEncoding{
	private Vector<Integer> primes;
	private int limit;
	/*
	These are just indices used by getPrimeFactors
	*/
	private final int PRIME_FACTORS = 0;
	private final int EXPONENTS = 1;
	
	private final String OPENERS = EncodingConstants.USED_OPENER + "" + EncodingConstants.SKIPPED_OPENER;
	private final String CLOSERS = EncodingConstants.USED_CLOSER + "" + EncodingConstants.SKIPPED_CLOSER;
	
	public PrimeEncoding(int HighBound){
		try{
			PrimeSieve ps = new PrimeSieve(HighBound);
			limit = HighBound;
			primes = ps.getPrimes();
			Integer[] primesArray = primes.toArray(new Integer[1]);
			primes = new Vector<Integer>();
			Arrays.sort(primesArray);
			int limit = primesArray.length;
			
			for(int i = 0; i < limit; i++){
				primes.add(primesArray[i]);
			}
		} catch(Exception e){
			e.printStackTrace();
			System.exit(-1);
		}
	}
	
	/*
	Returns the index of the largest prime number less than
	or equal to n.
	*/
	private int getProbableFactor(int n){
		int i = primes.size() - 1;
		
		while(i >= 0){
			if(primes.get(i) <= n){
				return i;
			}
			
			i--;
		}
		
		return -1;
	}
	
	/*
	Returns a GenericArray of Vectors containing the prime factorization
	of n. There are only 2 elements in the GenericArray returned: the first
	one contains the prime factors, the second one contains the exponents.
	
	@see chad.utils.GenericArray
	*/
	private GenericArray<Vector<Integer>> getPrimeFactors(int n){
		if(n == 1){
			GenericArray<Vector<Integer>> foo = new GenericArray<Vector<Integer>>(2);
			Vector<Integer> bar = new Vector<Integer>();
			bar.add(1);
			foo.set(PRIME_FACTORS, bar);
			foo.set(EXPONENTS, bar);
			return foo;
		}

		GenericArray<Vector<Integer>> PrimeFactorization = new GenericArray<Vector<Integer>>(2);
		Vector<Integer> primefactors = new Vector<Integer>();
		Vector<Integer> exponents = new Vector<Integer>();
		int factorIndex = getProbableFactor(n);
		int primefactor = primes.get(factorIndex);
		
		while(n != 1){
			if((n % primefactor) == 0){
				int index = primefactors.indexOf(primefactor);
				if(index < 0){
					primefactors.add(0, primefactor);
					exponents.add(0, 1);
				} else{
					int power = exponents.get(index);
					exponents.set(index, power + 1);
				}
				
				n /= primefactor;
			} else{
				factorIndex--;
				primefactor = primes.get(factorIndex);
			}
		}
		
		PrimeFactorization.set(PRIME_FACTORS, primefactors);
		PrimeFactorization.set(EXPONENTS, exponents);

		return PrimeFactorization;
	}
	
	private String encodeBracketRange(char opener, char closer, int num){
		String BracketEncoding = encode(num);
		BracketEncoding = BracketEncoding.substring(1, BracketEncoding.length());
		return opener + BracketEncoding + closer;
	}
	
	private String encodeUsedExponent(int exponent){
		if(exponent == 1){
			return "" + EncodingConstants.USED_INDICATOR;
		} else{
			return encodeBracketRange(EncodingConstants.USED_OPENER, EncodingConstants.USED_CLOSER, exponent);
		}
	}
	
	/**
	Returns the String symbolizing the encoding of n's prime factorization
	based on the scheme defined in EncodingConstants.java.
	
	For more on encoding schemes, see Aesthetic Computing.
	
	@param n - the integer to be encoded.
	@return If n is greater than what the upper bound defined for the given
	object or less than or equal to 0, the empty string is returned. Otherwise,
	we return its encoding as defined in EncodingConstants.java
	*/
	public String encode(int n){
		if(n >= limit || n <= 0){
			return "";
		} else if(n == 1){
			return "" + EncodingConstants.IDENTITY;
		} else{
			String encoding = "" + EncodingConstants.IDENTITY;
			GenericArray<Vector<Integer>> PrimeFactorization = getPrimeFactors(n);
			Vector<Integer> PrimeFactors = PrimeFactorization.get(PRIME_FACTORS);
			Vector<Integer> Exponents = PrimeFactorization.get(EXPONENTS);
			int i = 0;
			int primeRunner = 0;
			int limit = PrimeFactors.size();
			
			while(i < limit){
				int primefactor = PrimeFactors.get(i);
				int factorindex = primes.indexOf(primefactor);
				int exponent = Exponents.get(i);
				int indexRunnerDifference = factorindex - primeRunner;
				
				if(indexRunnerDifference == 0){
					encoding += encodeUsedExponent(exponent);
					primeRunner++;
				} else if(indexRunnerDifference == 1){
					encoding += EncodingConstants.SKIPPED_INDICATOR;
					encoding += encodeUsedExponent(exponent);
					primeRunner += indexRunnerDifference + 1;
				} else{
					encoding += encodeBracketRange(EncodingConstants.SKIPPED_OPENER, EncodingConstants.SKIPPED_CLOSER, indexRunnerDifference);
					encoding += encodeUsedExponent(exponent);
					primeRunner += indexRunnerDifference + 1;
				}
				
				i++;
			}
			
			return encoding;
		}
	}
	
	private boolean isWellFormed(String encoded){
		if(encoded.length() <= 0 || encoded.charAt(0) != EncodingConstants.IDENTITY){
			return false;
		}
		
		int i = 1;
		int limit = encoded.length();
		int runningsum = 0;
		Stack<Character> openers = new Stack<Character>();
		
		while(i < limit){
			if(runningsum < 0){
				return false;
			}
			
			char thischar = encoded.charAt(i);
			
			if(OPENERS.indexOf(thischar) >= 0){
				openers.push(encoded.charAt(i));
				runningsum++;
			} else if(CLOSERS.indexOf(thischar) >= 0){
				char lastOpener = openers.pop();
				
				if(OPENERS.indexOf(lastOpener) != CLOSERS.indexOf(thischar)){
					return false;
				}
				
				runningsum--;
			}
			
			i++;
		}
		
		return runningsum == 0;
	}
	
	/*
	Returns the index + 1 of the closer that closes an expression.
	*/
	public int getSubcode(String encoded, int encloser){
		int i = encloser + 1;
		int runningsum = 1;
		int limit = encoded.length();
		
		while(i < limit && runningsum != 0){
			char thischar = encoded.charAt(i);
			
			if(OPENERS.indexOf(thischar) >= 0){
				runningsum++;
			} else if(CLOSERS.indexOf(thischar) >= 0){
				runningsum--;
			}
			
			i++;
		}
		
		return i;
	}
	
	private int decode(String encoded, boolean isRecursiveCall) throws Exception{
		int decoded = 1;
		int primerunner = 0;
		
		if(isWellFormed(encoded) || isRecursiveCall){
			int i = 1; // Since we know that the first character is just the EncodingConstants.IDENTITY
			int limit = encoded.length();
			
			while(i < limit){
				char thischar = encoded.charAt(i);
				
				if(thischar == EncodingConstants.USED_INDICATOR){
					decoded *= primes.get(primerunner);
					primerunner++;
					i++;
				} else if(thischar == EncodingConstants.SKIPPED_INDICATOR){
					primerunner++;
					i++;
				} else if(thischar == EncodingConstants.USED_OPENER){
					int subcodeEnd = getSubcode(encoded, i);
					int exponent = decode(encoded.substring(i, subcodeEnd), true);
					int term = (int) Math.pow(primes.get(primerunner), exponent);
					decoded *= term;
					primerunner++;
					i = subcodeEnd;
				} else if(thischar == EncodingConstants.SKIPPED_OPENER){
					int subcodeEnd = getSubcode(encoded, i);
					primerunner += decode(encoded.substring(i, subcodeEnd), true);
					i = subcodeEnd;
				} else if(CLOSERS.indexOf(thischar) >= 0){
					i++;
				}
			}
			
			return decoded;
		} else{
			System.err.println("Error occurred!");
			throw new Exception(encoded + " is not a well-formed expression.");
		}
	}
	
	public int decode(String encoded) throws Exception{
		return decode(encoded, false);
	}
	/*
	public static void main(String[] args) throws Exception{
		System.out.println("Constructing sieve...");
		PrimeEncoding pe = new PrimeEncoding(800000);
		int[] integers = new int[25];
		String[] encodings = new String[25];
		
		System.out.println("Encoding integers...");
		for(int i = 1; i <= 25 ; i++){
			String encoding = pe.encode(i);
			integers[i - 1] = i;
			encodings[i - 1] = encoding;
			System.out.println(i + " " + encoding);
		}
		/*
		System.out.println(712080 + " " + pe.encode(712080));
		System.out.println(99961 + " " + pe.encode(99961));
		System.out.println(61 + " " + pe.encode(61));
		*
		System.out.println("Decoding encodings...");
		for(int i = 0; i < 25; i++){
			System.out.println(encodings[i] + " " + pe.decode(encodings[i]));
		}
		
	}
	/*
	public static void main(String[] args) throws Exception{
		PrimeEncoding pe = new PrimeEncoding(800000);
		String twentyfive = pe.encode(28);
		System.out.println("The encoding of 28 is " + twentyfive);
		System.out.println("That is " + pe.decode(twentyfive));
	}*/
	
	public int getLimit(){
		return limit;
	}
}
