package com.exa.lexing;

import java.util.ArrayList;
import java.util.List;

import com.exa.parsing.ParsingException;
import com.exa.utils.ManagedException;

public class CharReader implements Cloneable {
	
	public class Buffer {
		protected StringBuilder buffer;
		protected int start;
		protected Integer end = null;
		
		public Buffer(int position) {
			buffer = sharedBuffer.get();
			
			this.start = position - sharedBuffer.start;
		}
		
		public Buffer() { this(position); }

		@Override
		public String toString() {
			int end = (this.end == null ? position - sharedBuffer.start : this.end);
			if(end> buffer.length()) end = buffer.length();
			return buffer.substring(start, end);
		}
		
		public void markPosition() {
			//buffer.setLength(0);
			start = position - sharedBuffer.start;
			//this.initialPosition = CharReader.this.position;
		}
		
		public Buffer release() {
			sharedBuffer.release();
			end = position - sharedBuffer.start;
			return this;
		}
		
		public String rewind() { 
			String res = toString();
			addInAnalysisBuffer(res);
			
			return res;
		}
		
		public Buffer rewindAndRelease() { 
			rewind(); 
			return release(); 
		}
		
	}
	
	class SharedBuffer {
		protected StringBuilder currentBuffer;
		protected int refCount = 0;
		protected int start;
		
		public StringBuilder get() {
			++refCount;
			
			if(refCount == 1) {
				currentBuffer = new StringBuilder();
				
				start = position;
			}
			
			return currentBuffer;
		}
		
		public void release() {
			--refCount;
			if(refCount == 0) currentBuffer = null; 
		}
		
		public void reset() { refCount = 0; currentBuffer = null; }
	}
	
	
	/*public class DataBuffer {
		protected StringBuilder buffer = new StringBuilder();
		
		protected int initialPosition, position;
		protected boolean buffered;
		protected DataBuffer boundDB;
		
		public DataBuffer(int position, boolean buffered) {
			super();
			this.position = position;
			this.initialPosition = position;
			this.buffered = buffered;
			if(buffered) return;
			
			boundDB = sharedNBDB.get();
		}
		
		public DataBuffer(int position) { this(position, true);}
		
		public DataBuffer() {
			this(CharReader.this.position);
		}
		
		public String value() { 
			if(buffered) return buffer.toString(); 
			
			return sharedNBDB.value(initialPosition, position);
		}
		
		public String release() { 
			if(buffered) return CharReader.this.releaseCharReading(this); 
			
			return  sharedNBDB.release();
		}
		
		public void markPosition() {
			buffer.setLength(0);
			this.position = CharReader.this.position;
			this.initialPosition = CharReader.this.position;
		}
		
		public String rewind() { 
			String res = value();
			addInAnalysisBuffer(res);
			
			return res;
		}
		
		public void rewindAndRelease() { rewind(); release(); };
		
	}
	
	class SharedNonBufferedDB {
		protected DataBuffer db = null;
		protected int refCount;
		
		public DataBuffer get() {
			++refCount;
			if(db == null) db = monitorCharReading(true);
			return db;
		}
		
		public String release() {
			--refCount;
			String res = db.value();
			
			if(refCount == 0) {
				db.release();
				db = null;
			}
			return res;
		}
		
		public String value(int start, int end) {
			return db.buffer.substring(start-db.initialPosition, end-db.initialPosition);
		}
	}*/
	
	protected StringBuilder analysisBuffer;
	protected CharIterator charIterator;
	protected EscapeCharMan escapeCharMan;
	//protected List<DataBuffer> dataBuffers = new ArrayList<DataBuffer>();
	
	protected SharedBuffer sharedBuffer = new SharedBuffer();
	
	//protected SharedNonBufferedDB sharedNBDB = new SharedNonBufferedDB();
	
	
	
	protected final List<CharReader> clones;
	
	protected int position;
	
	protected CharReader(CharIterator charIterator, List<CharReader> clones, String analysisBuffer, int position) {
		this.charIterator = charIterator;
		this.analysisBuffer = new StringBuilder(analysisBuffer);
		this.clones = clones;
		this.position = position;
		
		this.escapeCharMan = EscapeCharMan.NO_ESCAPE;
		
		clones.add(this);
		
	}
	
	protected CharReader(EscapeCharMan escapeCharMan, List<CharReader> clones, String analysisBuffer, int position) {
		this.escapeCharMan = escapeCharMan;
		this.charIterator = escapeCharMan.getCharIterator();
		this.analysisBuffer = new StringBuilder(analysisBuffer);
		this.clones = clones;
		this.position = position;
		
		clones.add(this);
		
	}
	
	public CharReader(CharIterator charIterator) {
		this(charIterator, new ArrayList<CharReader>(), "", -1);
	}
	
	public CharReader(EscapeCharMan escapeCharMan) {
		this(escapeCharMan, new ArrayList<CharReader>(), "", -1);
	}
		
	public CharReader(String str) throws ManagedException {
		this(new StringCharIterator(str), new ArrayList<CharReader>(), "", -1);	
	}
	
	public void reset(String str) throws ManagedException {
		this.charIterator = new StringCharIterator(str);
		analysisBuffer.delete(0, analysisBuffer.length());
		position = -1;
		//dataBuffers.clear();
		sharedBuffer.reset();
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
			currentChar = escapeCharMan.translated(currentChar);
			
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
		/*for(DataBuffer db : dataBuffers) {
			if(db.buffered) {
				int startToDelete = db.buffer.length() - nbChar;
				
				if(startToDelete<0) continue;
			
				db.buffer.delete(startToDelete, db.buffer.length());
			
			}
			
			db.position -= nbChar;
		}*/
		
		if(sharedBuffer.refCount<=0) return;
		
		int startToDelete = sharedBuffer.currentBuffer.length() - nbChar;
		if(startToDelete<0) return;
		sharedBuffer.currentBuffer.delete(startToDelete, sharedBuffer.currentBuffer.length());
		
	}
	
	/*public DataBuffer monitorCharReading(boolean toBeBuffered) {
		DataBuffer db = new DataBuffer(position, toBeBuffered);
		dataBuffers.add(db);
		
		return db;
	}*/
	
	public Buffer bufferize() { return new Buffer(position); }
	
	/*public String releaseCharReading(DataBuffer db) {
		dataBuffers.remove(db);
		return db.value();
	}*/

	
	private void addInDataBuffer(Character ch) {
		
		/*for(DataBuffer db : dataBuffers) {
			if(position < db.position) continue;
			
			if(db.buffered)	db.buffer.append(ch);
			db.position++;
		}*/
		if(sharedBuffer.refCount>0) {
			sharedBuffer.currentBuffer.append(ch);
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
