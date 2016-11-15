package com.exa.lexing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.apache.commons.httpclient.util.URIUtil;

import com.exa.utils.ManagedException;

public class BufferedFileCharIterator implements CharIterator {
	private String fileName;

	private Charset charSet;
	private long startPosition;
	
	protected final static int DEFAULT_BUFFER_SIZE = 1024*1024;
	protected char[] buffer;
	protected int lineNumber, colPosInLine, charPos, readCharCount;
	protected long currentPosition;
	
	protected InputStreamReader fr;
	protected char currentChar;
	
	protected int bufferizeInc = 0;
	protected StringBuilder dataBuffer = new StringBuilder();
	
	public BufferedFileCharIterator(String fileName) throws ManagedException {
		this(fileName, Charset.defaultCharset(), DEFAULT_BUFFER_SIZE, 0, true);
	}
	
	public BufferedFileCharIterator(String fileName, String charset) throws ManagedException {
		this(fileName, Charset.forName(charset), DEFAULT_BUFFER_SIZE, 0, true);
	}
	public BufferedFileCharIterator(String fileName, int charBufferSize) throws ManagedException {
		this(fileName, Charset.defaultCharset(), charBufferSize, 0, true);
	}
	
	public BufferedFileCharIterator(String fileName, Charset charSet, int charBufferSize, long startPosition, boolean autoOpen) throws ManagedException {
		if(startPosition<0) throw new ManagedException("The start position should not be lesser than 0.");
		
		this.fileName = fileName;
		
		this.charSet = charSet;
		this.startPosition = startPosition;
		
		buffer = new char[charBufferSize];
		
		if(autoOpen) open();
		
	}
	
	public BufferedFileCharIterator(File file, Charset charSet, int charBufferSize, long startPosition, boolean autoOpen) throws ManagedException {
		this.fileName = file.toURI().toString();
		
		this.charSet = charSet;
		this.startPosition = startPosition;
		
		buffer = new char[charBufferSize];
		
		if(autoOpen) open(file);
	}
	
	public BufferedFileCharIterator(String fileName, Charset charSet) throws ManagedException {
		this(fileName, charSet, DEFAULT_BUFFER_SIZE, 0, true);
	}
	
	public BufferedFileCharIterator(String fileName, String charsetName, int charBufferSize) throws ManagedException {
		this(fileName, Charset.forName(charsetName), charBufferSize, 0, true);
	}

	public BufferedFileCharIterator(String fileName, long startPos) throws ManagedException {
		this(fileName, Charset.defaultCharset(), DEFAULT_BUFFER_SIZE, startPos, true);
	}
	
	public BufferedFileCharIterator(File file) throws ManagedException {
		this(file, Charset.defaultCharset(), DEFAULT_BUFFER_SIZE, 0, true);
	}
	
	public BufferedFileCharIterator(File file, String charset) throws ManagedException {
		this(file, Charset.forName(charset), DEFAULT_BUFFER_SIZE, 0, true);
	}
	
	public String canonicalFileURI() { return fileName; }
	
	@Override
	public boolean next() throws ManagedException {
		try {
			if(charPos+1>=readCharCount) {
				if(readCharCount<buffer.length) {
					readCharCount = -1;
					return false;
				}
				boolean rb = readBloc();
				if(!rb) return false;
			}
			
			currentPosition++;
			char lastChar = currentChar;
			
			currentChar = buffer[++charPos];
			if(bufferizeInc>0) dataBuffer.append(currentChar);
			
			updateLineProperties(lastChar);
						
			return true;
		} catch (IOException e) {
			throw new ManagedException(e);
		}
	}
	
	@Override
	public void skip(long n) throws ManagedException {
		if(readCharCount == -1) return;
		
		if(n<(readCharCount - charPos)) {
			updateCharStreamProperties((int)(charPos + n));
			currentPosition += n;
			return;
		}
		
		long nbCharToSkip = n - (readCharCount - charPos)+1;
		
		updateCharStreamProperties(readCharCount-1);
		
		while(nbCharToSkip > readCharCount) {
			try { 
				if(!readBloc())
					throw new ManagedException("The position " + currentPosition + (n-nbCharToSkip) + " is beyond the length of the file.");
				updateCharStreamProperties(readCharCount-1);
				
				nbCharToSkip -= readCharCount;
			} catch (Exception e) { throw new ManagedException(e); }
		}
		
		if(nbCharToSkip>0) {
			try { 
				if(!readBloc())
					throw new ManagedException("The position " + currentPosition + (n-nbCharToSkip) + " is beyond the length of the file.");
				
				updateCharStreamProperties((int)nbCharToSkip-1);
			} catch (Exception e) { throw new ManagedException(e); }
		}
		else charPos = 0;
		
		currentPosition += n;
	}
	
	private int updateCharStreamProperties(int charCountLimit) {
		int nbOfLine = 0;
		
		int cp = charPos;
		while(cp<charCountLimit) {
			char lastChar = currentChar;
			
			currentChar = buffer[++cp];
			
			updateLineProperties(lastChar);
		}
		
		charPos = charCountLimit;
		return nbOfLine;
	}
	
	private void updateLineProperties(char lastChar) {
		if(lastChar=='\r' && currentChar=='\n') return;
		
		else if(currentChar=='\n' || currentChar=='\r') {
			lineNumber++;
			colPosInLine = 0;
		}
		else colPosInLine++;
	}
	
	protected boolean readBloc() throws IOException {
		readCharCount=fr.read(buffer);
		
		if(readCharCount == -1) return false;
		charPos = -1;
		return true;
	}
	
	public void closeFile() throws IOException {
		fr.close();
	}
	
	@Override
	public int lineNumber() { return lineNumber; }

	@Override
	public char getCurrentChar() { return currentChar; }
	
	@Override
	public int getColPositionInLine() {	return colPosInLine; }
	
	@Override
	public long getPosition() {	return currentPosition;	}

	@Override
	public void reset() throws ManagedException {
		try {
			reset(new File(URIUtil.decode(fileName)));
		} catch (Exception e) {
			throw new ManagedException(e);
		}
	}
	
	private void reset(File file) throws ManagedException {
		lineNumber = 0; colPosInLine = 0; charPos = -1; readCharCount = 0;
		currentPosition = -1;
		
		currentChar = 0;
		
		if(fr != null) {
			try { fr.close(); } catch(Exception e) {}
			fr = null;
		}
		
		try {
		fr = new InputStreamReader(new FileInputStream(file), charSet);
		
		readBloc();
		} catch(Exception e) {
			if(fr != null) {
				try {fr.close(); } catch(Exception e1) {}
			}
			throw new ManagedException(e);
		}
		
		if(startPosition > 0) skip(startPosition);
		
	}

	@Override
	public void reset(long startPosition) throws ManagedException {
		if(startPosition<this.currentPosition) {
			this.startPosition = startPosition;
			reset();
		}
		else skip(startPosition - this.currentPosition);
	}

	@Override
	public boolean eof() { return readCharCount == -1; }
	
	@Override
	public BufferedFileCharIterator cloneIterator() throws ManagedException {
		return new BufferedFileCharIterator(fileName, charSet, buffer.length, startPosition, true);
	}

	@Override
	public void open() throws ManagedException {
		reset();
	}

	public void open(File f) throws ManagedException {
		reset(f);
		/*lineNumber = 0; colPosInLine = 0; charPos = -1; readCharCount = 0;
		currentPosition = -1;
		
		currentChar = 0;
		
		if(fr != null) {
			try { fr.close(); } catch(Exception e) {}
			fr = null;
		}
		
		try {
		fr = new InputStreamReader(new FileInputStream(f), charSet);
		
		readBloc();
		} catch(Exception e) {
			if(fr != null) {
				try {fr.close(); } catch(Exception e1) {}
			}
			throw new ManagedException(e);
		}
		
		if(startPosition > 0) skip(startPosition);*/
	}
	
	@Override
	public void close() throws ManagedException {
		try {
			if(fr == null) return;
			fr.close();
			fr = null;
		} catch (IOException e) {
			throw new ManagedException(e);
		}
	}

	public Charset charSet() { return charSet; }

	@Override
	protected void finalize() throws Throwable {
		if(fr != null) fr.close();
	}
	
	
}
