package com.exa.parsing;

import java.util.List;

public class ParsingEvent {
	
	public static abstract class WordMan {
		public abstract String getWord();
	}
	
	public static class WMSubPEVs extends WordMan {
		protected List<ParsingEvent> pevs;
		
		public WMSubPEVs(List<ParsingEvent> pevs) {
			this.pevs = pevs;
		}
		
		@Override
		public String getWord() {
			StringBuilder sb = new StringBuilder();
			for(ParsingEvent pev : pevs) {
				if(pev.isParent()) continue;
				String word = pev.getWord();
				if(word == null) continue;
				sb.append(" ").append(pev.getWord());
			}
			if(sb.length()>0) return sb.substring(1);
			return sb.toString(); 
		}
	}
	
	public static class WMConstant extends WordMan {
		protected String word;
		
		public WMConstant(String word) {
			this.word = word;
		}
		
		@Override
		public String getWord() { return word; }
	};
	
	protected String word;
	protected ParsingEntity parsingEntity;
	protected ParsingEntity result;
	protected Parsing<?> parsing;
	protected int nb;
	protected WordMan wordMan;
	protected boolean parent;
	
	public ParsingEvent(ParsingEntity parsingEntity, ParsingEntity result, Parsing<?> parsing, List<ParsingEvent> pevs) {
		this.parsing = parsing;
		this.parent = true;
		
		nb = 0;
		wordMan = new WMSubPEVs(pevs);
		this.word = null;
		for(ParsingEvent pev : pevs) {
			if(pev.isParent()) continue;
			++nb;
		}
		this.parsingEntity = parsingEntity;
		this.result = result;
	}
	
	public ParsingEvent(ParsingEntity parsingEntity, ParsingEntity result, Parsing<?> parsing, String word) {
		this.parsing = parsing;
		this.parent = false;
		
		wordMan = new WMConstant(word);
		this.word = word;
		this.nb = 1;
		this.parsingEntity = parsingEntity;
		this.result = result;
	}
	
	/*public ParsingEvent(ParsingEntity parsingEntity, ParsingEntity result, Parsing<?> parsing, int nb) {
		this(parsingEntity, result, parsing, null, nb);
	}*/

	public String getWord() { 
		if(word != null) return word; 
		
		return word = wordMan.getWord();
	}

	public ParsingEntity getParsingEntity() { return parsingEntity;	}

	public ParsingEntity getResult() { return result; }

	public Parsing<?> getParsing() { return parsing; }

	public int getNb() { return nb;	}

	public boolean isParent() { return parent; }
	
}
