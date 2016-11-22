package com.exa.parsing;

public class ParsingEvent {
	protected String word;
	protected ParsingEntity parsingEntity;
	protected ParsingEntity result;
	protected Parsing<?> parsing;
	protected int nb;
	
	public ParsingEvent(ParsingEntity parsingEntity, ParsingEntity result, Parsing<?> parsing, String word, int nb) {
		this.parsing = parsing;
		this.word = word;
		this.nb = nb;
		this.parsingEntity = parsingEntity;
		this.result = result;
	}
	
	public ParsingEvent(ParsingEntity parsingEntity, ParsingEntity result, Parsing<?> parsing, String word) {
		this(parsingEntity, result, parsing, word, 1);
	}
	
	public ParsingEvent(ParsingEntity parsingEntity, ParsingEntity result, Parsing<?> parsing, int nb) {
		this(parsingEntity, result, parsing, null, nb);
	}

	public String getWord() { return word; }

	public ParsingEntity getParsingEntity() { return parsingEntity;	}

	public ParsingEntity getResult() { return result; }

	public Parsing<?> getParsing() { return parsing; }

	public int getNb() { return nb;	}
	
}
