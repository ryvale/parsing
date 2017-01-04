package com.exa.parsing.ebnf;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.exa.lexing.Language;
import com.exa.parsing.PEAtomic;
import com.exa.utils.ManagedException;

public class RuleScript {
	public static abstract class CreationNotifier {
		public abstract void notify(CompiledRule cr);
	}
	protected String name;
	protected String src;
	protected List<CreationNotifier> creationNotifiers = new ArrayList<>();
	
	protected CompiledRule compiled = null;
	
	private boolean compilationInProgress = false;
	
	public RuleScript(String name, String src) {
		super();
		this.name = name;
		this.src = src;
	}
	
	public String name() { return name; }
	
	public String src() { return src; }
	
	/*public CompiledRule compiled() { return compiled; }
	
	public void compiled(CompiledRule compiled) { 
		this.compiled = compiled;
		
	}*/
	
	public CompiledRule compileWith(RuleParser rp) throws ManagedException {
		if(compilationInProgress) {
			final PEAtomic peAtomic = new PEAtomic(null);
			final Language language = new Language(null, null, peAtomic);
			final CompiledRule comiledRule = compiled == null ? new CompiledRule(language, null, null) : new CompiledRule(language, compiled.fields, compiled.fieldComputers);
			creationNotifiers.add(new CreationNotifier() {
				
				@Override
				public void notify(CompiledRule cr) {
					Language l = cr.language();
					peAtomic.setPERoot(l.getPERoot());
					
					language.setBlankStrings(l.getBlankStrings());
					language.setLexingRules(l.getLexingRules());
					if(compiled != null) return;
					
					comiledRule.fieldComputers = cr.fieldComputers;
					comiledRule.fields= cr.fields;
				}
				
			});
			
			
			
			return comiledRule;
		}
		compilationInProgress=true;
		compiled = rp.parse(src);
		compilationInProgress=false;
		
		Iterator<CreationNotifier> it = creationNotifiers.iterator();
		while(it.hasNext()) {
			CreationNotifier notifier = it.next();
			notifier.notify(compiled);
			it.remove();
		}
		return compiled;
	}
	
	/*public CompiledRule getLastOrcompileWith(RuleParser rp) throws ManagedException {
		if(compiled == null) return compileWith(rp);
		
		return compiled.clone();
	}*/
	
	@Override
	public String toString() { return src; }

}
