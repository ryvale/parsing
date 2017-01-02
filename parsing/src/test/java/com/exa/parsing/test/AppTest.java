package com.exa.parsing.test;

import java.util.HashSet;

import com.exa.lexing.Language;
import com.exa.lexing.LexingRules;
import com.exa.lexing.WordIterator;
import com.exa.parsing.ExpMan;
import com.exa.parsing.IParser;
import com.exa.parsing.PENotWord;
import com.exa.parsing.PEOptional;
import com.exa.parsing.PEOr;
import com.exa.parsing.PERepeat;
import com.exa.parsing.PEUnordered;
import com.exa.parsing.PEUntilNextString;
import com.exa.parsing.PEWord;
import com.exa.parsing.Parser;
import com.exa.parsing.ParsingEntity;
import com.exa.parsing.ParsingRuleBuilder;
import com.exa.parsing.ebnf.CompiledRule;
import com.exa.parsing.ebnf.OutputParser1;
import com.exa.parsing.ebnf.ParsedMap;
import com.exa.parsing.ebnf.RuleParser;
import com.exa.parsing.ebnf.predefs.PreParser;
import com.exa.utils.ManagedException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    
    public void testEBNF0() throws ManagedException {
		PreParser parser = new PreParser();
		
		System.out.print(parser.parse("nom0:x;"));
		
		System.out.print(parser.parse("nom1:'x';"));
		System.out.print(parser.parse("nom2: 'x';"));
		System.out.print(parser.parse(" nom3:'x';"));
		System.out.print(parser.parse("nom4 :'x';"));
		System.out.print(parser.parse(" nom5 :'x';"));
		System.out.print(parser.parse(" nom6 : 'x' ;"));
		System.out.print(parser.parse("nom7:('x'|'Y');"));
		
		System.out.print(parser.parse("nom8: 'x'; nom9: 'y';"));
		System.out.print(parser.parse("nom9: !'x';"));
		
		assertTrue(parser.validates("ignore ' \t\n';"));
		assertTrue(parser.validates("separators ' ';"));
		assertTrue(parser.validates("ignore ' \t\n'; separators '+';"));
		assertTrue(parser.validates("separators '+'; ignore ' \t\n';"));
		assertTrue(parser.validates("separators '+'; ignore ' \t\n'; nom :x;"));
		
		assertTrue(parser.validates("nom0:x;"));
		
		assertFalse(parser.validates(":nom1"));
		assertFalse(parser.validates("nom2"));
		
		System.out.print(parser.parse("nom : ['a'];"));
    }
    
    public void testEBNF1() throws ManagedException {
    	RuleParser parser = new RuleParser(new PreParser(false).parse("root : 'a';"), false);
    	
    	assertTrue(parser.validates("'abh'"));
    	assertTrue(parser.validates("'a\\''"));
    	assertTrue(parser.validates("$"));
    	assertTrue(parser.validates("'a'|'b'"));
    	assertTrue(parser.validates("'a'|'b'|'c'"));
    	
    	assertTrue(parser.validates("nom"));
    	assertTrue(parser.validates("nom='c'"));
    	assertTrue(parser.validates("nom$='c'"));
    	assertTrue(parser.validates("nom1=nom2"));
    	assertTrue(parser.validates("nom []= 'a'"));
    	
    	assertTrue(parser.validates("('a')"));
    	assertTrue(parser.validates("('a'|'b')"));
    	assertTrue(parser.validates("('a')+"));
    	
    	assertTrue(parser.validates("!'a'"));
    	
    	assertTrue(parser.validates("b"));
    	
    	assertTrue(parser.validates("['b']"));
    	
    	assertTrue(parser.validates("['a'|'b']"));
    	
    	assertTrue(parser.validates("'a'..'c'"));
    	
    	assertFalse(parser.validates("'a"));
    }
    
    
    public void testEBNF2() throws ManagedException {
    	RuleParser ebnfParser = new RuleParser(new PreParser(false).parse("ignore ' ';separators '+','-';root : 'a';"), false);
    
    	CompiledRule cr = ebnfParser.parse("'a'");
    	IParser<?> p = new OutputParser1(cr, false);
    	assertTrue(p.validates("a"));
    	assertFalse(p.validates("b"));
    	
    	cr = ebnfParser.parse("$");
    	p = new OutputParser1(cr, false);
    	assertTrue(p.validates("a"));
    	assertTrue(p.validates("b"));
    	
    	assertFalse(p.validates(""));
    	
    	cr = ebnfParser.parse("'a'*");
    	p = new OutputParser1(cr, false);
    	
    	assertTrue(p.validates(""));
    	assertTrue(p.validates("a a"));
    	assertFalse(p.validates("aa"));
    	
    	cr = ebnfParser.parse("'a'+");
    	p = new OutputParser1(cr, false);
    	
    	assertTrue(p.validates("a a"));
    	assertTrue(p.validates("a"));
    	
    	assertFalse(p.validates("aa"));
    	assertFalse(p.validates(""));
    	
    	cr = ebnfParser.parse("'a'?");
    	p = new OutputParser1(cr, false);
    	
    	assertTrue(p.validates(""));
    	assertTrue(p.validates("a"));
    	
    	assertFalse(p.validates("b"));
    	
    	cr = ebnfParser.parse("'a'|'b'");
    	p = new OutputParser1(cr, false);
    	
    	assertTrue(p.validates("a"));
    	assertTrue(p.validates("b"));
    	
    	assertFalse(p.validates(""));
    	assertFalse(p.validates("c"));
    	
    	cr = ebnfParser.parse("nom='a'");
    	p = new OutputParser1(cr);
    	
    	assertTrue(p.validates("a"));
    	
    	assertFalse(p.validates("b"));
    	
    	cr = ebnfParser.parse("nom []='a'");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("a"));
    	
    	cr = ebnfParser.parse("nom$=$");
    	p = new OutputParser1(cr);
    	
    	assertTrue(p.validates("x"));
    	assertTrue(p.validates("y"));
    	
    	assertFalse(p.validates(""));
    	
    	cr = ebnfParser.parse("nom=$ ':' valeur=$");
    	p = new OutputParser1(cr);
    	
    	assertTrue(p.validates("x : y"));
    	assertTrue(p.validates("y : x"));
    	assertFalse(p.validates("x"));
    	
    	cr = ebnfParser.parse("!'a'");
    	p = new OutputParser1(cr);
    	
    	assertTrue(p.validates("b"));
    	
    	assertFalse(p.validates("a"));
    	
    	cr = ebnfParser.parse("('a')");
    	p = new OutputParser1(cr);
    	
    	assertTrue(p.validates("a"));
    	
    	assertFalse(p.validates("b"));
    	
    	cr = ebnfParser.parse("['a']");
    	p = new OutputParser1(cr);
    	
    	assertTrue(p.validates("a"));
    	
    	assertFalse(p.validates("ab"));
    	
    	cr = ebnfParser.parse("['ab']");
    	p = new OutputParser1(cr);
    	
    	assertTrue(p.validates("ab"));
    	
    	assertFalse(p.validates("a"));
    	
    	cr = ebnfParser.parse("'a'..'c'");
    	p = new OutputParser1(cr);
    	
    	assertTrue(p.validates("a"));
    	assertTrue(p.validates("b"));
    	assertTrue(p.validates("c"));
    	
    	assertFalse(p.validates("d"));
    	
    	/*cr = ebnfParser.parse("((['_']('0'..'9'|'a'..'z'|'A'..'Z'))|'a'..'z'|'A'..'Z')('0'..'9'|'a'..'z'|'A'..'Z'|['_'])*");
    	p = new OutputParser1(cr);
    	
    	assertTrue(p.validates("identifiant"));
    	assertTrue(p.validates("vaRiable"));
    	assertTrue(p.validates("_vaRiable"));
    	assertTrue(p.validates("_1variable"));
    	assertTrue(p.validates("var1able"));
    	assertTrue(p.validates("vari_able"));
    	
    	assertFalse(p.validates("9variable"));*/
    	
    }
    
    public void testEBNF3() throws ManagedException {
    	RuleParser ebnfParser = new RuleParser(new PreParser().parse("ignore ' \t'; separators ':'; row : nom = !':'+ ':' valeur=(!'\n'|!'\r')+ '\n'?; test : 'a'; digit : '0'|'1'|'2';"), false);
    	
    	CompiledRule cr = ebnfParser.parse("'a''b'");
    	IParser<?> p = new OutputParser1(cr);
    	
    	assertTrue(p.validates("a b"));
    	
    	assertFalse(p.validates("ab"));
    	assertFalse(p.validates("a a"));
    	assertFalse(p.validates("a"));
    	assertFalse(p.validates("b a"));
    	assertFalse(p.validates("b"));
    	
    	cr = ebnfParser.parse("'a''b'?");
    	p = new OutputParser1(cr);
    	
    	assertTrue(p.validates("a b"));
    	assertTrue(p.validates("a"));
    	
    	assertFalse(p.validates("b"));
    	assertFalse(p.validates("ab"));
    	
    	cr = ebnfParser.parse("'a'*'b'");
    	p = new OutputParser1(cr);
    	
    	assertTrue(p.validates("a b"));
    	assertTrue(p.validates("a a b"));
    	assertTrue(p.validates("b"));
    	
    	cr = ebnfParser.parse("'a'|'b'|'c'");
    	p = new OutputParser1(cr);
    	
    	assertTrue(p.validates("a"));
    	assertTrue(p.validates("b"));
    	assertTrue(p.validates("c"));
    	
    	assertFalse(p.validates(""));
    	assertFalse(p.validates("d"));
    	assertFalse(p.validates("a b"));
    	
    	cr = ebnfParser.parse("'a'('b'|'c')+");
    	p = new OutputParser1(cr);
    	
    	assertTrue(p.validates("a b"));
    	assertTrue(p.validates("a c"));
    	assertTrue(p.validates("a b b"));
    	assertTrue(p.validates("a b c"));
    	assertTrue(p.validates("a c c"));
    	assertFalse(p.validates("a"));
    	assertFalse(p.validates("b"));
    	
    	
    	cr = ebnfParser.parse("'a'('b'|'c')*");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("a b"));
    	assertTrue(p.validates("a c"));
    	assertTrue(p.validates("a b b"));
    	assertTrue(p.validates("a b c"));
    	assertTrue(p.validates("a c c"));
    	assertTrue(p.validates("a"));
    	assertFalse(p.validates("b"));
    	
    	
    	cr = ebnfParser.parse("(nomA='a')|(nomB='b')");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("a"));
    	assertTrue(p.validates("a"));
    	assertFalse(p.validates("c"));
    	
    	
    	cr = ebnfParser.parse("nom='a' nom $= 'b'");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("a b"));
    	assertFalse(p.validates("c"));
    	
    	
    	cr = ebnfParser.parse("nom=('a'|'b')");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("a"));
    	assertTrue(p.validates("b"));
    	assertFalse(p.validates("c"));
    	
    	
    	cr = ebnfParser.parse("nom $= $+");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("a"));
    	assertTrue(p.validates("b"));
    	assertTrue(p.validates("a b"));
    	
    	
    	cr = ebnfParser.parse("valeur=($+)");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("a"));
    	assertTrue(p.validates("b"));
    	assertTrue(p.validates("a b"));
    	
    	
    	cr = ebnfParser.parse("valeur=!'\n'+'\n'?");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("a"));
    	assertTrue(p.validates("a b"));
    	assertTrue(p.validates("a\n"));
    	assertTrue(p.validates("a \n"));
    	assertFalse(p.validates("\n"));
    	
    	
    	cr = ebnfParser.parse("valeur=!'\n'+'\n'?");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("a"));
    	assertTrue(p.validates("a b"));
    	assertTrue(p.validates("a\n"));
    	assertTrue(p.validates("a \n"));
    	assertFalse(p.validates("\n"));
    	
    	
    	cr = ebnfParser.parse("nom=test");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("a"));
    	assertFalse(p.validates("b"));
    	
    	
    	cr = ebnfParser.parse("nom=$ ':' valeur=$");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("nom : Kouakou"));
    	
    	cr = ebnfParser.parse("!'a'+");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("b"));
    	assertTrue(p.validates("b c"));
    	assertTrue(p.validates("bc de"));
    	
    	assertFalse(p.validates(""));
    	assertFalse(p.validates("a"));
    	
    	cr = ebnfParser.parse("(nom='a')");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("a"));
    	assertFalse(p.validates("b"));
    	
    	cr = ebnfParser.parse("nom = !':'+ ':' valeur=$+");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("nom : Kouakou Koffi"));
    	assertTrue(p.validates("Prénoms : Joseph François"));
    	
    	cr = ebnfParser.parse("nom = !':'+ ':' valeur=(!'\n'|!'\r')+ '\n'?");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("nom : Kouakou Koffi"));
    	assertTrue(p.validates("Prénoms : Joseph François"));
    	assertTrue(p.validates("nom : Kouakou Koffi\n"));
    		
    	cr = ebnfParser.parse("valeur $='a' ('b')?");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("a"));
    	assertTrue(p.validates("a b"));
    	
    	cr = ebnfParser.parse("valeur $='a'+ ('b')");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("a b"));
    	assertFalse(p.validates("a"));
    	
    	cr = ebnfParser.parse("row");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("nom : Kouakou Koffi"));
    	
    	
    	cr = ebnfParser.parse("propriete = row");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("nom : Kouakou Koffi\n"));
    	assertTrue(p.validates("Prénoms : Joseph François"));
    	
    	cr = ebnfParser.parse("[('0'|'1'|'2')*]");
    	p = new OutputParser1(cr);
    	
    	assertTrue(p.validates("102"));
    	
    	cr = ebnfParser.parse("['0'|'1'|'2']*");
    	p = new OutputParser1(cr);
    	
    	assertTrue(p.validates("102"));
    	
    	assertFalse(p.validates("1024"));
    	
    	cr = ebnfParser.parse("[digit]+");
    	p = new OutputParser1(cr);
    	
    	assertTrue(p.validates("102"));
    	
    	cr = ebnfParser.parse("digit+");
    	p = new OutputParser1(cr);
    	
    	assertFalse(p.validates("102"));
    }
    
    
    public void testEBNF4() throws ManagedException {
    	RuleParser ebnfParser = new RuleParser(new PreParser().parse("ignore ' \t'; row : nom = !':'+ ':' valeur = !('\n'|'\r')+ '\n'?; root : 'a';"), false);
    	CompiledRule cr = ebnfParser.parse("nomA='a'|nomB='b'");
    	OutputParser1 p = new OutputParser1(cr);
    	
    	ParsedMap pm = p.parse("a");
    	assertTrue(pm.get("nomA").toString().equals("a"));
    	assertFalse(pm.containsKey("nomB"));
    	
    	pm = p.parse("b");
    	assertTrue(pm.get("nomB").toString().equals("b"));
    	assertFalse(pm.containsKey("nomA"));
    	
    	cr = ebnfParser.parse("nom=('a'|'b')");
    	p = new OutputParser1(cr);
    	
    	pm = p.parse("a");
    	assertTrue(pm.get("nom").toString().equals("a"));
    	
    	pm = p.parse("b");
    	assertTrue(pm.get("nom").toString().equals("b"));
    	
    	cr = ebnfParser.parse("nom$=$+");
    	p = new OutputParser1(cr);
    	pm = p.parse("a");
    	assertTrue(pm.get("nom").toString().equals("a"));
    	pm = p.parse("a b");
    	assertTrue(pm.get("nom").toString().equals("a b"));
    	pm = p.parse("a\tb");
    	assertTrue(pm.get("nom").toString().equals("a\tb"));
    	
    	cr = ebnfParser.parse("nom []='a'");
    	p = new OutputParser1(cr);
    	pm = p.parse("a");
    	assertTrue(pm.get("nom").asParsedArray().get(0).toString().equals("a"));
    	
    	cr = ebnfParser.parse("nom []='a'+");
    	p = new OutputParser1(cr);
    	pm = p.parse("a");
    	assertTrue(pm.get("nom").asParsedArray().get(0).toString().equals("a"));
    	/*pm = p.parse("a a");
    	assertTrue(pm.get("nom").asParsedArray().get(1).toString().equals("a"));*/
    	
    	cr = ebnfParser.parse("(nom []='a')+");
    	p = new OutputParser1(cr);
    	pm = p.parse("a");
    	assertTrue(pm.get("nom").asParsedArray().get(0).toString().equals("a"));
    	pm = p.parse("a a");
    	assertTrue(pm.get("nom").asParsedArray().get(1).toString().equals("a"));
    	
    	cr = ebnfParser.parse("nom='a' nom $= 'b'");
    	p = new OutputParser1(cr);
    	pm = p.parse("a b");
    	assertTrue(pm.get("nom").toString().equals("a b"));
    	
    	cr = ebnfParser.parse("nom=$ ':' valeur=$");
    	p = new OutputParser1(cr);
    	
    	pm = p.parse("nom : Kouakou");
    	assertTrue(pm.get("nom").toString().equals("nom"));
    	assertTrue(pm.get("valeur").toString().equals("Kouakou"));
    	
    	cr = ebnfParser.parse("nom $= !':'+ ':' valeur $= $+");
    	p = new OutputParser1(cr);
    	
    	pm = p.parse("nom : Kouakou Koffi");
    	
    	assertTrue(pm.get("nom").toString().equals("nom"));
    	assertTrue(pm.get("valeur").toString().equals("Kouakou Koffi"));
    	
    	pm = p.parse("Prénoms : Joseph François");
    	assertTrue(pm.get("nom").toString().equals("Prénoms"));
    	assertTrue(pm.get("valeur").toString().equals("Joseph François"));
    	
    	cr = ebnfParser.parse("propriete = !':'+ ':' valeur = !('\n'|'\r')+ ('\n'|'\r')?");
    	p = new OutputParser1(cr);
    	pm = p.parse("nom : Kouakou Koffi");
    	
    	assertTrue(pm.get("propriete").toString().equals("nom"));
    	assertTrue(pm.get("valeur").toString().equals("Kouakou Koffi"));
    	
    	cr = ebnfParser.parse("propriete = row");
    	p = new OutputParser1(cr);
    	
    	pm = p.parse("nom : Kouakou Koffi");
    	assertTrue(pm.get("propriete").asParsedMap().get("nom").toString().equals("nom"));
    	assertTrue(pm.get("propriete").asParsedMap().get("valeur").toString().equals("Kouakou Koffi"));
    	
    	cr = ebnfParser.parse("propriete = row+");
    	p = new OutputParser1(cr);
    	
    	pm = p.parse("nom : Kouakou Koffi\nnom: Jocelyn");
    	System.out.println(pm.get("propriete").asParsedString().getValue());    	
    	
    	cr = ebnfParser.parse("(v[]='a')+");
    	p = new OutputParser1(cr);
    	pm = p.parse("a");
    	assertTrue(pm.get("v").get(0).toString().equals("a"));
    	
    	cr = ebnfParser.parse("(v[]=('a''b'))+");
    	p = new OutputParser1(cr);
    	pm = p.parse("a b");
    	assertTrue(pm.get("v").get(0).toString().equals("a b"));
    	
    	pm = p.parse("a b a b");
    	assertTrue(pm.get("v").get(0).toString().equals("a b"));
    	assertTrue(pm.get("v").get(1).toString().equals("a b"));
    }
    
    private Parser<?> parserForTest(ParsingEntity pe) {
    	return new Parser<Boolean>(new Language(new LexingRules(), new HashSet<String>(), pe)) {

			@Override
			public ExpMan<Boolean> createExpMan(WordIterator wi) throws ManagedException {
				wi.getLexingRules().addWordSeparator(":");
				return new ExpMan<Boolean>(null);
			}

			@Override
			public boolean listen(ParsingEntity pe) {
				return true;
			}
					
		};
    }
    
    public void testParsing0() throws ManagedException {
    	Parser<?> parser = parserForTest(new PEWord("a"));
		
		assertTrue(parser.validates("a"));
		assertTrue(parser.validates("a "));
		
		assertFalse(parser.validates("b"));
		
		parser = parserForTest(new PEWord("a b"));
		assertTrue(parser.validates("a b"));
		
		assertFalse(parser.validates("a"));
		assertFalse(parser.validates("b"));
		
		parser = parserForTest(new PEWord("a", "b"));
		assertTrue(parser.validates("a"));
		assertTrue(parser.validates("b"));
		
		assertFalse(parser.validates("c"));
		
		parser = parserForTest(new PENotWord("a"));
		assertTrue(parser.validates("b"));
		
		assertFalse(parser.validates("a"));
		assertFalse(parser.validates("b c"));
		
		parser = parserForTest(new PENotWord("a b"));
		assertTrue(parser.validates("cd"));
		assertTrue(parser.validates("ac"));
		
		assertFalse(parser.validates("a b"));
		assertFalse(parser.validates("a b c d"));
		
		parser = parserForTest(new PEOptional("a"));
		assertTrue(parser.validates("a"));
		assertTrue(parser.validates(""));
		
		ParsingEntity peRoot = new PEOptional("a");
		peRoot.setNextPE("b");
		parser = parserForTest(peRoot);
		assertTrue(parser.validates("a b"));
		assertTrue(parser.validates("b"));
		
		assertFalse(parser.validates(""));
		assertFalse(parser.validates("a"));
		
		parser = parserForTest(new PERepeat("a", 1));
		assertTrue(parser.validates("a"));
		assertTrue(parser.validates("a "));
		assertTrue(parser.validates("a a"));
		
		assertFalse(parser.validates("b"));
		assertFalse(parser.validates("a b"));
		assertFalse(parser.validates(""));
		
		peRoot = new PEWord("a");
		peRoot.setNextPE(new PEWord("b"));
		
		parser = parserForTest(new PERepeat(peRoot, 1));
		assertTrue(parser.validates("a b"));
		assertTrue(parser.validates("a b a b"));
		
		assertFalse(parser.validates("a"));
		assertFalse(parser.validates("ab"));
		assertFalse(parser.validates("a a"));
		assertFalse(parser.validates("a b a"));
		
		peRoot = new PERepeat("a", 0);
		peRoot.setNextPE("b");
		parser = parserForTest(peRoot);
		assertTrue(parser.validates("a b"));
		assertTrue(parser.validates("a b"));
		assertTrue(parser.validates("b"));
		
		parser = parserForTest(new PERepeat(peRoot, 0));
		assertTrue(parser.validates(""));
		assertTrue(parser.validates("a b"));
		assertTrue(parser.validates("a b a b"));
		
		assertFalse(parser.validates("a"));
		assertFalse(parser.validates("a b a"));
		
		parser = parserForTest(new PEOr().add("a").add("b"));
		assertTrue(parser.validates("a"));
		assertTrue(parser.validates("b"));
		
		assertFalse(parser.validates("c"));
		assertFalse(parser.validates(""));
		
		parser = parserForTest(new PEOr().add("ab").add("cd").add("ab cd"));
		assertTrue(parser.validates("ab"));
		assertTrue(parser.validates("cd"));
		assertTrue(parser.validates("ab cd"));
		
		assertFalse(parser.validates("db"));
		assertFalse(parser.validates("cd ab"));
		
		parser = parserForTest(new PEUntilNextString(":", true, true));
		assertTrue(parser.validates(":"));
		assertTrue(parser.validates("nom:"));
		
		parser = parserForTest(new PEUnordered().add("a").add("b").add("c"));
		assertTrue(parser.validates("a"));
		assertTrue(parser.validates("b"));
		assertTrue(parser.validates("c"));
		assertTrue(parser.validates("a b"));
		assertTrue(parser.validates("a c"));
		assertTrue(parser.validates("b a"));
		assertTrue(parser.validates("b a c"));
		
		assertFalse(parser.validates("b b"));
		assertFalse(parser.validates("b a b"));
		
    }
    
    public void testParsing1() throws ManagedException {
    	Parser<?> parser = parserForTest(
    		new ParsingRuleBuilder(
    					new PEUntilNextString(":", true, false)).
    			next(	new PEWord(":")).
    		parsingEntity()
    	);
    	assertTrue(parser.validates(":"));
		assertTrue(parser.validates("nom:"));
		
		assertFalse(parser.validates(""));
		assertFalse(parser.validates("nom"));
		assertFalse(parser.validates("valeur"));
		
		parser = parserForTest(
    		new ParsingRuleBuilder(
    					new PEUntilNextString(":", true, false)).
    			next(	new PEWord(":")).
    			next(	new PEWord("valeur")).
    		parsingEntity()
    	);
		assertTrue(parser.validates("nom:valeur"));
		assertTrue(parser.validates("nom : valeur"));
		
		parser = parserForTest(new PEUnordered().add("a b").add("c b").add("c a"));
		assertTrue(parser.validates("a b"));
		assertTrue(parser.validates("c a"));
		assertTrue(parser.validates("c a a b"));
		
		assertFalse(parser.validates("c a a a"));
		assertFalse(parser.validates("c a a"));
    }
    
    public void testFileParsing0() throws ManagedException {
    	RuleParser ebnfParser = new RuleParser(new PreParser().parseFile("C:/recherches/parsing5/parsing/src/test/java/com/exa/parsing/test/script.parser"), false);
    	CompiledRule cr = ebnfParser.parse(ebnfParser.getRuleConfig().getRule("root").src());
    	OutputParser1 p = new OutputParser1(cr);
    	
    	ParsedMap pm = p.parse("nom : Kouakou Koffi");
    	assertTrue("nom".equals(pm.get("rows").asParsedArray().get(0).get("champ").toString()));
    	assertTrue("Kouakou Koffi".equals(pm.get("rows").asParsedArray().get(0).get("valeur").toString()));
    	
    	pm = p.parse("nom : Koffi\nprénoms : Kouakou");
    	assertTrue("nom".equals(pm.get("rows").asParsedArray().get(0).get("champ").toString()));
    	System.out.println(pm.get("rows").asParsedArray().get(0).get("valeur").toString());
    	assertTrue("Koffi".equals(pm.get("rows").asParsedArray().get(0).get("valeur").toString()));
    	
    	assertTrue("prénoms".equals(pm.get("rows").asParsedArray().get(1).get("champ").toString()));
    	assertTrue("Kouakou".equals(pm.get("rows").asParsedArray().get(1).get("valeur").toString()));
    	
    	ebnfParser = new RuleParser(new PreParser().parseFile("C:/recherches/parsing5/parsing/src/test/java/com/exa/parsing/test/exat.parser"), false);
    	cr = ebnfParser.parse(ebnfParser.getRuleConfig().getRule("root").src());
    	p = new OutputParser1(cr);
    	
    	pm = p.parse("test");
    	assertTrue("test".equals(pm.get("tpart0").toString()));
    	
    	pm = p.parse("test1 %x% test2");
    	assertTrue("test1 ".equals(pm.get("tpart0").toString()));
    	assertTrue("x".equals(pm.get("parts").asParsedArray().get(0).get("epart").toString()));
    	assertTrue(" test2".equals(pm.get("parts").asParsedArray().get(0).get("tpart").toString()));
    	
    	pm = p.parse("test1 %x% test2 %y% test3");
    	assertTrue("test1 ".equals(pm.get("tpart0").toString()));
    	assertTrue("x".equals(pm.get("parts").asParsedArray().get(0).get("epart").toString()));
    	assertTrue(" test2 ".equals(pm.get("parts").asParsedArray().get(0).get("tpart").toString()));
    	assertTrue("y".equals(pm.get("parts").asParsedArray().get(1).get("epart").toString()));
    	assertTrue(" test3".equals(pm.get("parts").asParsedArray().get(1).get("tpart").toString()));
    	
    	pm = p.parse("test\\%");
    	assertTrue("test\\%".equals(pm.get("tpart0").toString()));
    }
    
    public void testFileParsing1() throws ManagedException {
    	RuleParser ebnfParser = new RuleParser(new PreParser().parseFile("C:/recherches/exat/exat/src/main/java/com/exa/exat/default.parser"), false);
    	CompiledRule cr = ebnfParser.parse(ebnfParser.getRuleConfig().getRule("test").src());
    	
    	OutputParser1 p = new OutputParser1(cr);
    	//assertTrue(p.validates("a"));
    	
    	assertTrue(p.validates("@{a}"));
    }
    
    /*public void testCharByCharParsing() throws ManagedException {
    	RuleParser ebnfParser = new RuleParser(new PreParser().parse("ignore ' \t'; row : nom = !':'+ ':' valeur = !('\n'|'\r')+ '\n'?; root : 'a';"), false);
    	CompiledRule cr = ebnfParser.parse("['a']");
    	OutputParser1 p = new OutputParser1(cr);
    	
    }*/
    
}
