package com.exa.parsing;

public abstract class PERule {
	
	public static final PERule FAIL = new PERule() {
		
		@Override
		public boolean isOK(ParsingEntity pe, Parsing<?> parsing) {
			return pe.asPEFail() != null;
		}

	};
	
	/*public static final PERule NON_FATAL_FAIL = new PERNonMutable() {
		
		@Override
		public boolean isOK(ParsingEntity pe, Parsing<?, ?>.ProcessingChannel pc) {
			PEFail peFail = pe.asPEFail();
			if(peFail == null) return false;
			
			return !peFail.isFatal();
		}

	};*/
	
	public static final PERule EOS = new PERule() {
		
		@Override
		public boolean isOK(ParsingEntity pe, Parsing<?> parsing) {
			return pe == ParsingEntity.EOS;
		}
		
	};
	
	public static final PERule OK = new PERule() {
		
		@Override
		public boolean isOK(ParsingEntity pe, Parsing<?> parsing) {
			return pe == ParsingEntity.OK;
		}

	};
	
	public static final PERule FINAL = new PERule() {
		
		@Override
		public boolean isOK(ParsingEntity pe, Parsing<?> parsing) {
			return pe.isFinal();
		}

	};
	
	public static final PERule NON_FAIL_FINAL = new PERule() {
		
		@Override
		public boolean isOK(ParsingEntity pe, Parsing<?> parsing) {
			return pe.isFinal() && pe.asPEFail() == null;
		}

	};	
	
	public static final PERule ANY_CASE = new PERule() {
		
		@Override
		public boolean isOK(ParsingEntity pe, Parsing<?> parsing) {
			return true;
		}

	};
	
	public static final PERule NEVER = new PERule() {
		
		@Override
		public boolean isOK(ParsingEntity pe, Parsing<?> parsing) {
			return false;
		}

	};
	
	public abstract boolean isOK(ParsingEntity pe, Parsing<?> parsing);
	
}
