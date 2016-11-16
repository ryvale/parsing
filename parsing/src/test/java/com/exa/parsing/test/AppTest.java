package com.exa.parsing.test;


import com.exa.parsing.IParser;
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
		
		assertTrue(parser.validates("nom0:x;"));
		
		assertFalse(parser.validates(":nom1"));
		assertFalse(parser.validates("nom2"));
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
    	
    	assertFalse(parser.validates("'a"));
    }
    
    public void testEBNF2() throws ManagedException {
    	RuleParser ebnfParser = new RuleParser(new PreParser(false).parse("ignore ' ';root : 'a';"), false);
    
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
    	
    	cr = ebnfParser.parse("valeur=!'\n'|!'\r'");
    	p = new OutputParser1(cr);
    }
    
    public void testEBNF3() throws ManagedException {
    	RuleParser ebnfParser = new RuleParser(new PreParser().parse("ignore ' \t'; row : nom = !':'+ ':' valeur=(!'\n'|!'\r')+ '\n'?; test : 'a';"), false);
    	
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
    	assertTrue(p.validates("a"));
    	assertTrue(p.validates("a b"));
    	
    	
    	cr = ebnfParser.parse("row");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("nom : Kouakou Koffi"));
    	
    	
    	cr = ebnfParser.parse("propriete = row");
    	p = new OutputParser1(cr);
    	assertTrue(p.validates("nom : Kouakou Koffi\n"));
    	assertTrue(p.validates("Prénoms : Joseph François"));
    }
    
    public void testEBNF4() throws ManagedException {
    	RuleParser ebnfParser = new RuleParser(new PreParser().parse("ignore ' \t'; row : nom $= !':'+ ':' valeur $=(!'\n'|!'\r')+ '\n'?; root : 'a';"), false);
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
    	
    	cr = ebnfParser.parse("propriete $= !':'+ ':' valeur $= (!'\n'|!'\r')+ ('\n'|'\r')?");
    	p = new OutputParser1(cr);
    	pm = p.parse("nom : Kouakou Koffi");
    	
    	assertTrue(pm.get("propriete").toString().equals("nom"));
    	assertTrue(pm.get("valeur").toString().equals("Kouakou Koffi"));
    	
    	cr = ebnfParser.parse("propriete = row");
    	p = new OutputParser1(cr);
    	
    	pm = p.parse("nom : Kouakou Koffi");
    	assertTrue(pm.get("propriete").asParsedMap().get("nom").toString().equals("nom"));
    	
    }
    
}
