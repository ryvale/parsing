package com.exa.parsing;

public class ParsingEvent {
	protected String word;
	protected ParsingEntity parsingEntity;
	protected ParsingEntity result;
	protected Parsing<?> parsing;
	
	public ParsingEvent(ParsingEntity parsingEntity, ParsingEntity result, Parsing<?> parsing) {
		super();
		this.parsing = parsing;
		this.word = parsing.currentWord();
		this.parsingEntity = parsingEntity;
		this.result = result;
	}

	public String getWord() { return word; }

	public ParsingEntity getParsingEntity() { return parsingEntity;	}

	public ParsingEntity getResult() { return result; }

	public Parsing<?> getParsing() { return parsing; }
	
}
