package net.skytreader.kode.AestheticComputing.PrimeEncoding;

public enum EncodingSymbols{
	IDENTITY ('i'),
	USED_INDICATOR ('|'),
	SKIPPED_INDICATOR ('-'),
	USED_OPENER ('['),
	USED_CLOSER (']'),
	SKIPPED_OPENER ('{'),
	SKIPPED_CLOSER ('}');
	
	private final char symbol;
	
	EncodingSymbols(char symbol){
		this.symbol = symbol;
	}
	
	public char getSymbol(){
		return this.symbol;
	}
}
