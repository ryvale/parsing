package com.exa.parsing;

import java.util.List;

import com.exa.buffer.CharReader.ClientBuffer;

public class ParsingEvent {
	
	public abstract class WordMan {
		public String trimValue() {
			String str = toString();
			if(str == null) return null;
			return parsing.trimLeft(str); 
		}
	}
	
	public class WMSubPEVs extends WordMan {
		protected List<ParsingEvent> pevs;
		
		public WMSubPEVs(List<ParsingEvent> pevs) {
			this.pevs = pevs;
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			for(ParsingEvent pev : pevs) {
				if(pev.isParent()) continue;
				String word = pev.getWord();
				if(word == null) continue;
				sb.append(word);
			}
			//if(sb.length()>0) return sb.substring(1);
			return sb.toString(); 
		}
	}
	
	public class WMConstant extends WordMan {
		protected ClientBuffer buffer;
		
		public WMConstant(ClientBuffer buffer) {
			this.buffer = buffer;
		}
		
		@Override
		public String toString() { return buffer == null ? null : buffer.toString(); }
	};
	
	protected ClientBuffer buffer;
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
		this.buffer = null;
		for(ParsingEvent pev : pevs) {
			if(pev.isParent()) continue;
			++nb;
		}
		this.parsingEntity = parsingEntity;
		this.result = result;
	}
	
	public ParsingEvent(ParsingEntity parsingEntity, ParsingEntity result, Parsing<?> parsing, ClientBuffer buffer) {
		this.parsing = parsing;
		this.parent = false;
		
		wordMan = new WMConstant(buffer);
		this.buffer = buffer;
		this.nb = 1;
		this.parsingEntity = parsingEntity;
		this.result = result;
	}
	
	/*public ParsingEvent(ParsingEntity parsingEntity, ParsingEntity result, Parsing<?> parsing, int nb) {
		this(parsingEntity, result, parsing, null, nb);
	}*/

	public String getWord() { 		
		return wordMan.toString();
	}
	
	public String getTrimWord() { 		
		return wordMan.trimValue();
	}

	public ParsingEntity getParsingEntity() { return parsingEntity;	}

	public ParsingEntity getResult() { return result; }

	public Parsing<?> getParsing() { return parsing; }

	public int getNb() { return nb;	}

	public boolean isParent() { return parent; }
	
}
