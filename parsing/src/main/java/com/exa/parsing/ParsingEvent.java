package com.exa.parsing;

public class ParsingEvent {
	protected String word;
	protected ParsingEntity parsingEntity;
	protected ParsingEntity result;
	
	public ParsingEvent(ParsingEntity parsingEntity, ParsingEntity result, String word) {
		super();
		this.word = word;
		this.parsingEntity = parsingEntity;
		this.result = result;
	}

	public String getWord() { return word; }

	public ParsingEntity getParsingEntity() { return parsingEntity;	}

	public ParsingEntity getResult() { return result; }
	
}
