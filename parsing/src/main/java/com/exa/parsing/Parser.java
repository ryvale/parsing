package com.exa.parsing;

import java.util.HashSet;
import java.util.Set;

import com.exa.lexing.Language;
import com.exa.lexing.WordIterator;
import com.exa.utils.ManagedException;

public abstract class Parser<T> implements IParser<T> {
	protected Language language;
	protected boolean debugOn;
	
	protected Set<ParsingEntity> pesToListen = new HashSet<>();
	
	public Parser(Language language, boolean debugOn) {
		this.language = language;
		this.debugOn = debugOn;
	}
	
	public Parser(Language language) { this(language, false); }
	
	/* (non-Javadoc)
	 * @see com.exa.parsing.IParser#parse(java.lang.String)
	 */
	@Override
	public T parse(String str) throws ManagedException {
		WordIterator wi = newStringWIterator(str);
		return parse(wi, createExpMan(wi));
	}
	
	public T parse(WordIterator wi, ExpMan<T> ires) throws ManagedException {
		Parsing<T> parsing = createParsing(wi, ires);
		
		T res = parsing.compute();
		
		wi.close();
		return res;
	}
	
	public Language getLanguage() {	return language; }
		
	/* (non-Javadoc)
	 * @see com.exa.parsing.IParser#validates(java.lang.String)
	 */
	@Override
	public boolean validates(String exp) throws ManagedException {
		WordIterator wi = newStringWIterator(exp);
		
		Parsing<T> parsing = createParsing(wi, createExpMan(wi));
		
		return parsing.expressionIsValid();
	}
	
	protected Parsing<T> createParsing(WordIterator wi, ExpMan<T> em) { 
		return new Parsing<T>(this, wi, em, null, debugOn); 
	}
	
	public abstract ExpMan<T> createExpMan(WordIterator wi) throws ManagedException;// { return null; }
	
	protected WordIterator newStringWIterator(String str) throws ManagedException {
		return language.newStringWordIterator(str);
	}
	
	protected WordIterator newFileWIterator(String fileName, String charset) throws ManagedException {
		return language.newFileWordIterator(fileName, charset);
	}
	
	public boolean notifyEvent(ParsingEntity pe, ParsingEntity result)  {
		if(result != ParsingEntity.PE_NEXT_CHECK && result.failed()) return false;
		return listen(pe);
	}
	
	public boolean listen(ParsingEntity pe) { return pesToListen.contains(pe); }

}
