package com.exa.lexing;

import java.util.ArrayList;
import java.util.List;

import com.exa.parsing.ParsingException;
import com.exa.utils.ManagedException;

public class CharReader implements Cloneable {
	
	public class DataBuffer {
		
		protected StringBuilder buffer = new StringBuilder();
		
		protected int position;
		
		public DataBuffer(int position) {
			super();
			this.position = position;
		}
		
		public DataBuffer() {
			this(CharReader.this.position);
		}
		
		public String value() { return buffer.toString(); }
		
		public String release() { return CharReader.this.releaseBuffer(this); }
		
		public void reset() {
			buffer.setLength(0);
			this.position = CharReader.this.position;
		}
		
		public String rewind() { 
			String res = value();
			addInAnalysisBuffer(res);
			
			return res;
		}
	}
	
	protected StringBuilder analysisBuffer;
	protected CharIterator charIterator;
	protected List<DataBuffer> dataBuffers = new ArrayList<DataBuffer>();
	
	protected final List<CharReader> clones;
	
	protected int position;
	
	protected CharReader(CharIterator charIterator, List<CharReader> clones, String analysisBuffer, int position) {
		this.charIterator = charIterator;
		this.analysisBuffer = new StringBuilder(analysisBuffer);
		this.clones = clones;
		this.position = position;
		
		clones.add(this);
	}
	
	public CharReader(CharIterator charIterator) {
		this(charIterator, new ArrayList<CharReader>(), "", -1);
	}
		
	public CharReader(String str) throws ManagedException {
		this(new StringCharIterator(str), new ArrayList<CharReader>(), "", -1);	
	}
	
	public void reset(String str) throws ManagedException {
		this.charIterator = new StringCharIterator(str);
		analysisBuffer.delete(0, analysisBuffer.length());
		position = -1;
		dataBuffers.clear();
	}
	
	public int getPosition() { return position; }
	
	public Character nextChar() throws ManagedException {
		if(analysisBuffer.length()>0) {
			Character currentChar = new Character(analysisBuffer.charAt(0));
			analysisBuffer.deleteCharAt(0);
			addInDataBuffer(currentChar);
			addInCloneAnalysisBuffer(currentChar);
			position++;
			return currentChar;
		}
		
		if(charIterator.next()) {
			Character currentChar = charIterator.getCurrentChar();
			addInDataBuffer(currentChar);
			addInCloneAnalysisBuffer(currentChar);
			position++;
			return currentChar;
		}
		
		return null;
	}
	
	private void addInCloneAnalysisBuffer(Character ch) {
		for(CharReader cr : clones) {
			if(cr == this) continue;
			
			cr.addInAnalysisBuffer(ch);
		}
	}
	
	public void nextToRequiredChar(char c) throws ManagedException {
		Character currentChar;
		while((currentChar = nextChar()) != null) {
			if(currentChar == c) return;
		}
		
		throw new ParsingException("Can not find the character '" + c + "'");
	}
	
	public void nextToChar(char c) throws ManagedException {
		Character currentChar;
		while((currentChar = nextChar()) != null) {
			if(currentChar == c) return;
		}
	}

	public Character nextWhileCharIn(String authorizedChars) throws ManagedException {
		Character currentChar;
		while((currentChar = nextChar()) != null) {
			if(authorizedChars.indexOf(currentChar.toString())>=0) continue;
			
			addInAnalysisBuffer(currentChar);
			
			return currentChar;
		}
		return null;
	}
	
	public Character nextForwardChar() throws ManagedException {
		Character currentChar = nextChar();
		if(currentChar == null) return null;
		
		addInAnalysisBuffer(currentChar);
		
		return currentChar;
	}

	public String nextForwardChar(int nb) throws ManagedException {
		StringBuilder res = new StringBuilder();
		
		for(int i=0; i < nb; i++) {
			Character currentChar = nextChar();
			if(currentChar == null) break;
			res.append(currentChar);
		}
		
		addInAnalysisBuffer(res.toString());
		return res.toString();
	}
	
	public void addInAnalysisBuffer(Character ch) {
		analysisBuffer.insert(0, ch);
		position -= 1;
		removeInDataBuffer(1);
	}
	
	public void addInAnalysisBuffer(String ch) {
		analysisBuffer.insert(0, ch);
		position -= ch.length();
		removeInDataBuffer(ch.length());
	}
	
	private void removeInDataBuffer(int nbChar) {
		for(DataBuffer db : dataBuffers) {
			int startToDelete = db.buffer.length() - nbChar;
			
			if(startToDelete<0) continue;
			
			/*if(startToDelete>0)*/ db.buffer.delete(startToDelete, db.buffer.length());
			//else db.buffer.delete(db.buffer.length(), db.buffer.length());
			
			db.position -= nbChar;
		}
		
	}
	
	public DataBuffer bufferize() {
		DataBuffer db = new DataBuffer(position);
		dataBuffers.add(db);
		
		return db;
	}
	
	public String releaseBuffer(DataBuffer db) {
		dataBuffers.remove(db);
		return db.value();
	}

	
	private void addInDataBuffer(Character ch) {
		for(DataBuffer db : dataBuffers) {
			if(position < db.position) continue;
			
			db.buffer.append(ch);
			db.position++;
		}
	}
	
	public void close() throws ManagedException { 
		charIterator.close();
	}
	
	public CharReader cloneReader() {
		CharReader res = new CharReader(charIterator, clones, analysisBuffer.toString(), position);
		
		return res;
	}

	@Override
	protected CharReader clone() {
		return new CharReader(charIterator, clones, analysisBuffer.toString(), position);
	}
	
}
