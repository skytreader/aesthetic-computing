package net.skytreader.kode.AestheticComputing.PrimeEncoding;

import java.util.Vector;

/**
Creates a look-up table for primality testing using the Sieve of Eratosthenes.

(Note to self: For my Gateway AMD Turion64 Laptop with 2GHz dual-core processor,
with 2GB RAM running Ubuntu 10.04 Lucid Lynx, the size of the largest
sieve I've tested so far is 350,000,000.

TODO: That is not yet the tightest bound. Test some more.)

This implementation takes advantage of the fact that Java's default value for
booleans is false. Though it may seem instinctive to say that sieve[i] == true
means i is prime, to do so wil incur as an O(n) overhead (where n is the size
of the sieve) due to the flipping of the default false values stored in the
sieve. Remember that, in this program, asking sieve[i] is tantamount to asking
if i is _not_ prime.

@author Chad Estioco
@version 2nd Semester, AY 2010-2011
*/
public class PrimeSieve{
	private boolean[] sieve;
	
	public PrimeSieve(int SieveSize){
		try{
			sieve = new boolean[SieveSize];
			generateSieve();
		} catch(OutOfMemoryError oome){
			System.err.println("PrimeSieve constructor: Sieve size is too large.");
			System.exit(-1);
		}
	}
	
	private void generateSieve(){
		int i = 1;
		int sqrtLimit = (int) Math.ceil(Math.sqrt((double) sieve.length));
		
		while(i <= sqrtLimit){
			if(!sieve[i]){
				int multiplesIndex = i + 1;
				int runnerIndex = i + multiplesIndex;
				int lenlimit = sieve.length;
				
				while(runnerIndex < lenlimit){
					sieve[runnerIndex] = true;
					runnerIndex += multiplesIndex;
				}
			}
			
			i++;
		}
				
	}
	
	public boolean isPrime(int x) throws Exception{
		if(x > sieve.length){
			throw new Exception("isPrime: The given integer is larger than the sieve.");
		} else if(x <= 0 || x == 1){
			return false;
		} else{
			return !sieve[x - 1];
		}
	}
	
	public Vector<Integer> getPrimes() throws Exception{
		int i = sieve.length - 1;
		Vector<Integer> primes = new Vector<Integer>();
		
		while(i >= 0){
			if(isPrime(i + 1)){
				primes.add(i + 1);
			}
			
			i--;
		}
		
		return primes;
	}
}
