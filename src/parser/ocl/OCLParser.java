// $ANTLR 3.1.3 Mar 18, 2009 10:09:25 OCL.g 2013-12-17 18:38:46
 
/*
 * NUIM - OCL Parser 
 * haowu@cs.nuim.ie
 */

package parser.ocl; 
import java.io.PrintWriter;
// ------------------------------------
//  OCL parser
// ------------------------------------


import org.antlr.runtime.*;
import java.util.Stack;
import java.util.List;
import java.util.ArrayList;

public class OCLParser extends Parser {
    public static final String[] tokenNames = new String[] {
        "<invalid>", "<EOR>", "<DOWN>", "<UP>", "IDENT", "COLON", "COLON_COLON", "EQUAL", "LPAREN", "COMMA", "RPAREN", "NOT_EQUAL", "LESS", "GREATER", "LESS_EQUAL", "GREATER_EQUAL", "PLUS", "MINUS", "STAR", "SLASH", "ARROW", "DOT", "AT", "BAR", "SEMI", "LBRACK", "RBRACK", "INT", "REAL", "STRING", "HASH", "LBRACE", "RBRACE", "DOTDOT", "NEWLINE", "WS", "SL_COMMENT", "ML_COMMENT", "COLON_EQUAL", "RANGE_OR_INT", "ESC", "HEX_DIGIT", "VOCAB", "'package'", "'endpackage'", "'context'", "'inv'", "'existential'", "'pre'", "'post'", "'let'", "'in'", "'implies'", "'or'", "'xor'", "'and'", "'div'", "'not'", "'allInstances'", "'iterate'", "'oclAsType'", "'oclIsKindOf'", "'oclIsTypeOf'", "'if'", "'then'", "'else'", "'endif'", "'true'", "'false'", "'Set'", "'Sequence'", "'Bag'", "'OrderedSet'", "'oclEmpty'", "'oclUndefined'", "'Undefined'", "'null'", "'Tuple'", "'Collection'"
    };
    public static final int T__68=68;
    public static final int T__69=69;
    public static final int T__66=66;
    public static final int T__67=67;
    public static final int LBRACK=25;
    public static final int STAR=18;
    public static final int T__64=64;
    public static final int T__65=65;
    public static final int T__62=62;
    public static final int T__63=63;
    public static final int ESC=40;
    public static final int LBRACE=31;
    public static final int DOTDOT=33;
    public static final int T__61=61;
    public static final int EOF=-1;
    public static final int T__60=60;
    public static final int LPAREN=8;
    public static final int AT=22;
    public static final int T__55=55;
    public static final int ML_COMMENT=37;
    public static final int T__56=56;
    public static final int RPAREN=10;
    public static final int T__57=57;
    public static final int T__58=58;
    public static final int COLON_EQUAL=38;
    public static final int SLASH=19;
    public static final int GREATER=13;
    public static final int T__51=51;
    public static final int T__52=52;
    public static final int T__53=53;
    public static final int T__54=54;
    public static final int NOT_EQUAL=11;
    public static final int COMMA=9;
    public static final int T__59=59;
    public static final int EQUAL=7;
    public static final int LESS=12;
    public static final int IDENT=4;
    public static final int PLUS=16;
    public static final int RANGE_OR_INT=39;
    public static final int DOT=21;
    public static final int T__50=50;
    public static final int RBRACK=26;
    public static final int T__43=43;
    public static final int T__46=46;
    public static final int T__47=47;
    public static final int RBRACE=32;
    public static final int T__44=44;
    public static final int T__45=45;
    public static final int T__48=48;
    public static final int T__49=49;
    public static final int HASH=30;
    public static final int HEX_DIGIT=41;
    public static final int INT=27;
    public static final int COLON_COLON=6;
    public static final int MINUS=17;
    public static final int SEMI=24;
    public static final int COLON=5;
    public static final int REAL=28;
    public static final int WS=35;
    public static final int T__71=71;
    public static final int T__72=72;
    public static final int NEWLINE=34;
    public static final int T__70=70;
    public static final int SL_COMMENT=36;
    public static final int VOCAB=42;
    public static final int ARROW=20;
    public static final int LESS_EQUAL=14;
    public static final int T__76=76;
    public static final int T__75=75;
    public static final int T__74=74;
    public static final int GREATER_EQUAL=15;
    public static final int T__73=73;
    public static final int BAR=23;
    public static final int STRING=29;
    public static final int T__78=78;
    public static final int T__77=77;

    // delegates
    // delegators


        public OCLParser(TokenStream input) {
            this(input, new RecognizerSharedState());
        }
        public OCLParser(TokenStream input, RecognizerSharedState state) {
            super(input, state);
             
        }
        

    public String[] getTokenNames() { return OCLParser.tokenNames; }
    public String getGrammarFileName() { return "OCL.g"; }



    // $ANTLR start "expressionOnly"
    // OCL.g:66:1: expressionOnly returns [ASTExpression n] : nExp= expression EOF ;
    public final ASTExpression expressionOnly() throws RecognitionException {
        ASTExpression n = null;

        ASTExpression nExp = null;


        try {
            // OCL.g:67:1: (nExp= expression EOF )
            // OCL.g:68:5: nExp= expression EOF
            {
            pushFollow(FOLLOW_expression_in_expressionOnly52);
            nExp=expression();

            state._fsp--;

            n = nExp;
            match(input,EOF,FOLLOW_EOF_in_expressionOnly56); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "expressionOnly"


    // $ANTLR start "oclexpression"
    // OCL.g:71:1: oclexpression returns [ASTOCLExpression n] : 'package' IDENT (consdef= invariant | p= prePost )* EOF 'endpackage' ;
    public final ASTOCLExpression oclexpression() throws RecognitionException {
        ASTOCLExpression n = null;

        ASTConstraintDefinition consdef = null;

        ASTPrePost p = null;


        try {
            // OCL.g:72:1: ( 'package' IDENT (consdef= invariant | p= prePost )* EOF 'endpackage' )
            // OCL.g:73:2: 'package' IDENT (consdef= invariant | p= prePost )* EOF 'endpackage'
            {
            n =new ASTOCLExpression();
            match(input,43,FOLLOW_43_in_oclexpression75); 
            match(input,IDENT,FOLLOW_IDENT_in_oclexpression77); 
            // OCL.g:74:18: (consdef= invariant | p= prePost )*
            loop1:
            do {
                int alt1=3;
                int LA1_0 = input.LA(1);

                if ( (LA1_0==45) ) {
                    int LA1_2 = input.LA(2);

                    if ( (LA1_2==IDENT) ) {
                        int LA1_3 = input.LA(3);

                        if ( (LA1_3==COLON_COLON) ) {
                            alt1=2;
                        }
                        else if ( (LA1_3==EOF||(LA1_3>=45 && LA1_3<=47)) ) {
                            alt1=1;
                        }


                    }


                }


                switch (alt1) {
            	case 1 :
            	    // OCL.g:74:19: consdef= invariant
            	    {
            	    pushFollow(FOLLOW_invariant_in_oclexpression82);
            	    consdef=invariant();

            	    state._fsp--;

            	    n.addConstraint(consdef);

            	    }
            	    break;
            	case 2 :
            	    // OCL.g:75:4: p= prePost
            	    {
            	    pushFollow(FOLLOW_prePost_in_oclexpression91);
            	    p=prePost();

            	    state._fsp--;

            	    n.addPrePost(p);

            	    }
            	    break;

            	default :
            	    break loop1;
                }
            } while (true);

            match(input,EOF,FOLLOW_EOF_in_oclexpression97); 
            match(input,44,FOLLOW_44_in_oclexpression100); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "oclexpression"


    // $ANTLR start "invariant"
    // OCL.g:87:1: invariant returns [ASTConstraintDefinition n] : 'context' v= IDENT (inv= invariantClause )* ;
    public final ASTConstraintDefinition invariant() throws RecognitionException {
        ASTConstraintDefinition n = null;

        Token v=null;
        ASTInvariantClause inv = null;


        try {
            // OCL.g:88:1: ( 'context' v= IDENT (inv= invariantClause )* )
            // OCL.g:89:2: 'context' v= IDENT (inv= invariantClause )*
            {
            n = new ASTConstraintDefinition();
            match(input,45,FOLLOW_45_in_invariant120); 
            v=(Token)match(input,IDENT,FOLLOW_IDENT_in_invariant129); 
            n.addVarName(v); 
            // OCL.g:94:5: (inv= invariantClause )*
            loop2:
            do {
                int alt2=2;
                int LA2_0 = input.LA(1);

                if ( ((LA2_0>=46 && LA2_0<=47)) ) {
                    alt2=1;
                }


                switch (alt2) {
            	case 1 :
            	    // OCL.g:94:7: inv= invariantClause
            	    {
            	    pushFollow(FOLLOW_invariantClause_in_invariant154);
            	    inv=invariantClause();

            	    state._fsp--;

            	     n.addInvariantClause(inv); 

            	    }
            	    break;

            	default :
            	    break loop2;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "invariant"


    // $ANTLR start "invariantClause"
    // OCL.g:101:1: invariantClause returns [ASTInvariantClause n] : ( 'inv' (name= IDENT )? COLON e= expression | 'existential' 'inv' (name= IDENT )? COLON e= expression );
    public final ASTInvariantClause invariantClause() throws RecognitionException {
        ASTInvariantClause n = null;

        Token name=null;
        ASTExpression e = null;


        try {
            // OCL.g:102:1: ( 'inv' (name= IDENT )? COLON e= expression | 'existential' 'inv' (name= IDENT )? COLON e= expression )
            int alt5=2;
            int LA5_0 = input.LA(1);

            if ( (LA5_0==46) ) {
                alt5=1;
            }
            else if ( (LA5_0==47) ) {
                alt5=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 5, 0, input);

                throw nvae;
            }
            switch (alt5) {
                case 1 :
                    // OCL.g:103:7: 'inv' (name= IDENT )? COLON e= expression
                    {
                    match(input,46,FOLLOW_46_in_invariantClause185); 
                    // OCL.g:103:13: (name= IDENT )?
                    int alt3=2;
                    int LA3_0 = input.LA(1);

                    if ( (LA3_0==IDENT) ) {
                        alt3=1;
                    }
                    switch (alt3) {
                        case 1 :
                            // OCL.g:103:15: name= IDENT
                            {
                            name=(Token)match(input,IDENT,FOLLOW_IDENT_in_invariantClause191); 

                            }
                            break;

                    }

                    match(input,COLON,FOLLOW_COLON_in_invariantClause196); 
                    pushFollow(FOLLOW_expression_in_invariantClause200);
                    e=expression();

                    state._fsp--;

                    n = new ASTInvariantClause(name, e);
                    			

                    }
                    break;
                case 2 :
                    // OCL.g:105:7: 'existential' 'inv' (name= IDENT )? COLON e= expression
                    {
                    match(input,47,FOLLOW_47_in_invariantClause210); 
                    match(input,46,FOLLOW_46_in_invariantClause212); 
                    // OCL.g:105:27: (name= IDENT )?
                    int alt4=2;
                    int LA4_0 = input.LA(1);

                    if ( (LA4_0==IDENT) ) {
                        alt4=1;
                    }
                    switch (alt4) {
                        case 1 :
                            // OCL.g:105:29: name= IDENT
                            {
                            name=(Token)match(input,IDENT,FOLLOW_IDENT_in_invariantClause218); 

                            }
                            break;

                    }

                    match(input,COLON,FOLLOW_COLON_in_invariantClause223); 
                    pushFollow(FOLLOW_expression_in_invariantClause227);
                    e=expression();

                    state._fsp--;

                     n = new ASTExistentialInvariantClause(name, e); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "invariantClause"


    // $ANTLR start "prePost"
    // OCL.g:115:1: prePost returns [ASTPrePost n] : 'context' classname= IDENT COLON_COLON opname= IDENT pl= paramList ( COLON rt= type )? (ppc= prePostClause )+ ;
    public final ASTPrePost prePost() throws RecognitionException {
        ASTPrePost n = null;

        Token classname=null;
        Token opname=null;
        List pl = null;

        ASTType rt = null;

        ASTPrePostClause ppc = null;


        try {
            // OCL.g:116:1: ( 'context' classname= IDENT COLON_COLON opname= IDENT pl= paramList ( COLON rt= type )? (ppc= prePostClause )+ )
            // OCL.g:117:5: 'context' classname= IDENT COLON_COLON opname= IDENT pl= paramList ( COLON rt= type )? (ppc= prePostClause )+
            {
            match(input,45,FOLLOW_45_in_prePost252); 
            classname=(Token)match(input,IDENT,FOLLOW_IDENT_in_prePost256); 
            match(input,COLON_COLON,FOLLOW_COLON_COLON_in_prePost258); 
            opname=(Token)match(input,IDENT,FOLLOW_IDENT_in_prePost262); 
            pushFollow(FOLLOW_paramList_in_prePost266);
            pl=paramList();

            state._fsp--;

            // OCL.g:117:69: ( COLON rt= type )?
            int alt6=2;
            int LA6_0 = input.LA(1);

            if ( (LA6_0==COLON) ) {
                alt6=1;
            }
            switch (alt6) {
                case 1 :
                    // OCL.g:117:71: COLON rt= type
                    {
                    match(input,COLON,FOLLOW_COLON_in_prePost270); 
                    pushFollow(FOLLOW_type_in_prePost274);
                    rt=type();

                    state._fsp--;


                    }
                    break;

            }

             n = new ASTPrePost(classname, opname, pl, rt); 
            // OCL.g:119:5: (ppc= prePostClause )+
            int cnt7=0;
            loop7:
            do {
                int alt7=2;
                int LA7_0 = input.LA(1);

                if ( ((LA7_0>=48 && LA7_0<=49)) ) {
                    alt7=1;
                }


                switch (alt7) {
            	case 1 :
            	    // OCL.g:119:7: ppc= prePostClause
            	    {
            	    pushFollow(FOLLOW_prePostClause_in_prePost293);
            	    ppc=prePostClause();

            	    state._fsp--;

            	     n.addPrePostClause(ppc); 

            	    }
            	    break;

            	default :
            	    if ( cnt7 >= 1 ) break loop7;
                        EarlyExitException eee =
                            new EarlyExitException(7, input);
                        throw eee;
                }
                cnt7++;
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "prePost"


    // $ANTLR start "prePostClause"
    // OCL.g:126:1: prePostClause returns [ASTPrePostClause n] : ( 'pre' | 'post' ) (name= IDENT )? COLON e= expression ;
    public final ASTPrePostClause prePostClause() throws RecognitionException {
        ASTPrePostClause n = null;

        Token name=null;
        ASTExpression e = null;


         Token t = null; 
        try {
            // OCL.g:128:1: ( ( 'pre' | 'post' ) (name= IDENT )? COLON e= expression )
            // OCL.g:129:5: ( 'pre' | 'post' ) (name= IDENT )? COLON e= expression
            {
             t = input.LT(1); 
            if ( (input.LA(1)>=48 && input.LA(1)<=49) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            // OCL.g:130:25: (name= IDENT )?
            int alt8=2;
            int LA8_0 = input.LA(1);

            if ( (LA8_0==IDENT) ) {
                alt8=1;
            }
            switch (alt8) {
                case 1 :
                    // OCL.g:130:27: name= IDENT
                    {
                    name=(Token)match(input,IDENT,FOLLOW_IDENT_in_prePostClause347); 

                    }
                    break;

            }

            match(input,COLON,FOLLOW_COLON_in_prePostClause352); 
            pushFollow(FOLLOW_expression_in_prePostClause356);
            e=expression();

            state._fsp--;

             n = new ASTPrePostClause(t, name, e); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "prePostClause"


    // $ANTLR start "expression"
    // OCL.g:138:1: expression returns [ASTExpression n] : ( 'let' name= IDENT ( COLON t= type )? EQUAL e1= expression 'in' )* nCndImplies= conditionalImpliesExpression ;
    public final ASTExpression expression() throws RecognitionException {
        ASTExpression n = null;

        Token name=null;
        ASTType t = null;

        ASTExpression e1 = null;

        ASTExpression nCndImplies = null;


         
          ASTLetExpression prevLet = null, firstLet = null;
          ASTExpression e2;
          Token tok = null;
          //System.out.println("...ASTExpression...");		

        try {
            // OCL.g:145:1: ( ( 'let' name= IDENT ( COLON t= type )? EQUAL e1= expression 'in' )* nCndImplies= conditionalImpliesExpression )
            // OCL.g:146:5: ( 'let' name= IDENT ( COLON t= type )? EQUAL e1= expression 'in' )* nCndImplies= conditionalImpliesExpression
            {
             tok = input.LT(1); /* remember start of expression */ 
            // OCL.g:147:5: ( 'let' name= IDENT ( COLON t= type )? EQUAL e1= expression 'in' )*
            loop10:
            do {
                int alt10=2;
                int LA10_0 = input.LA(1);

                if ( (LA10_0==50) ) {
                    alt10=1;
                }


                switch (alt10) {
            	case 1 :
            	    // OCL.g:148:7: 'let' name= IDENT ( COLON t= type )? EQUAL e1= expression 'in'
            	    {
            	    match(input,50,FOLLOW_50_in_expression404); 
            	    name=(Token)match(input,IDENT,FOLLOW_IDENT_in_expression408); 
            	    // OCL.g:148:24: ( COLON t= type )?
            	    int alt9=2;
            	    int LA9_0 = input.LA(1);

            	    if ( (LA9_0==COLON) ) {
            	        alt9=1;
            	    }
            	    switch (alt9) {
            	        case 1 :
            	            // OCL.g:148:26: COLON t= type
            	            {
            	            match(input,COLON,FOLLOW_COLON_in_expression412); 
            	            pushFollow(FOLLOW_type_in_expression416);
            	            t=type();

            	            state._fsp--;


            	            }
            	            break;

            	    }

            	    match(input,EQUAL,FOLLOW_EQUAL_in_expression421); 
            	    pushFollow(FOLLOW_expression_in_expression425);
            	    e1=expression();

            	    state._fsp--;

            	    match(input,51,FOLLOW_51_in_expression427); 
            	     ASTLetExpression nextLet = new ASTLetExpression(name, t, e1);
            	             if ( firstLet == null ) 
            	                 firstLet = nextLet;
            	             if ( prevLet != null ) 
            	                 prevLet.setInExpr(nextLet);
            	             prevLet = nextLet;
            	           

            	    }
            	    break;

            	default :
            	    break loop10;
                }
            } while (true);

            pushFollow(FOLLOW_conditionalImpliesExpression_in_expression452);
            nCndImplies=conditionalImpliesExpression();

            state._fsp--;

             
            	 if ( nCndImplies != null ) {
                	 n = nCndImplies;
                     n.setStartToken(tok);
                  }
                  
                  if ( prevLet != null ) { 
                     prevLet.setInExpr(n);
                     n = firstLet;
                     n.setStartToken(tok);
                  }
                

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "expression"


    // $ANTLR start "paramList"
    // OCL.g:177:1: paramList returns [List paramList] : LPAREN (v= variableDeclaration ( COMMA v= variableDeclaration )* )? RPAREN ;
    public final List paramList() throws RecognitionException {
        List paramList = null;

        ASTVariableDeclaration v = null;


         paramList = new ArrayList(); 
        try {
            // OCL.g:179:1: ( LPAREN (v= variableDeclaration ( COMMA v= variableDeclaration )* )? RPAREN )
            // OCL.g:180:5: LPAREN (v= variableDeclaration ( COMMA v= variableDeclaration )* )? RPAREN
            {
            match(input,LPAREN,FOLLOW_LPAREN_in_paramList485); 
            // OCL.g:181:5: (v= variableDeclaration ( COMMA v= variableDeclaration )* )?
            int alt12=2;
            int LA12_0 = input.LA(1);

            if ( (LA12_0==IDENT) ) {
                alt12=1;
            }
            switch (alt12) {
                case 1 :
                    // OCL.g:182:7: v= variableDeclaration ( COMMA v= variableDeclaration )*
                    {
                    pushFollow(FOLLOW_variableDeclaration_in_paramList502);
                    v=variableDeclaration();

                    state._fsp--;

                     paramList.add(v); 
                    // OCL.g:183:7: ( COMMA v= variableDeclaration )*
                    loop11:
                    do {
                        int alt11=2;
                        int LA11_0 = input.LA(1);

                        if ( (LA11_0==COMMA) ) {
                            alt11=1;
                        }


                        switch (alt11) {
                    	case 1 :
                    	    // OCL.g:183:9: COMMA v= variableDeclaration
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_paramList514); 
                    	    pushFollow(FOLLOW_variableDeclaration_in_paramList518);
                    	    v=variableDeclaration();

                    	    state._fsp--;

                    	     paramList.add(v); 

                    	    }
                    	    break;

                    	default :
                    	    break loop11;
                        }
                    } while (true);


                    }
                    break;

            }

            match(input,RPAREN,FOLLOW_RPAREN_in_paramList538); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return paramList;
    }
    // $ANTLR end "paramList"


    // $ANTLR start "idList"
    // OCL.g:191:1: idList returns [List idList] : id0= IDENT ( COMMA idn= IDENT )* ;
    public final List idList() throws RecognitionException {
        List idList = null;

        Token id0=null;
        Token idn=null;

         idList = new ArrayList(); 
        try {
            // OCL.g:193:1: (id0= IDENT ( COMMA idn= IDENT )* )
            // OCL.g:194:5: id0= IDENT ( COMMA idn= IDENT )*
            {
            id0=(Token)match(input,IDENT,FOLLOW_IDENT_in_idList567); 
             idList.add(id0); 
            // OCL.g:195:5: ( COMMA idn= IDENT )*
            loop13:
            do {
                int alt13=2;
                int LA13_0 = input.LA(1);

                if ( (LA13_0==COMMA) ) {
                    alt13=1;
                }


                switch (alt13) {
            	case 1 :
            	    // OCL.g:195:7: COMMA idn= IDENT
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_idList577); 
            	    idn=(Token)match(input,IDENT,FOLLOW_IDENT_in_idList581); 
            	     idList.add(idn); 

            	    }
            	    break;

            	default :
            	    break loop13;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return idList;
    }
    // $ANTLR end "idList"


    // $ANTLR start "variableDeclaration"
    // OCL.g:203:1: variableDeclaration returns [ASTVariableDeclaration n] : name= IDENT COLON t= type ;
    public final ASTVariableDeclaration variableDeclaration() throws RecognitionException {
        ASTVariableDeclaration n = null;

        Token name=null;
        ASTType t = null;


        try {
            // OCL.g:204:1: (name= IDENT COLON t= type )
            // OCL.g:205:5: name= IDENT COLON t= type
            {
            name=(Token)match(input,IDENT,FOLLOW_IDENT_in_variableDeclaration612); 
            match(input,COLON,FOLLOW_COLON_in_variableDeclaration614); 
            pushFollow(FOLLOW_type_in_variableDeclaration618);
            t=type();

            state._fsp--;

             n = new ASTVariableDeclaration(name, t); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "variableDeclaration"


    // $ANTLR start "conditionalImpliesExpression"
    // OCL.g:213:1: conditionalImpliesExpression returns [ASTExpression n] : nCndOrExp= conditionalOrExpression (op= 'implies' n1= conditionalOrExpression )* ;
    public final ASTExpression conditionalImpliesExpression() throws RecognitionException {
        ASTExpression n = null;

        Token op=null;
        ASTExpression nCndOrExp = null;

        ASTExpression n1 = null;


        try {
            // OCL.g:214:1: (nCndOrExp= conditionalOrExpression (op= 'implies' n1= conditionalOrExpression )* )
            // OCL.g:215:5: nCndOrExp= conditionalOrExpression (op= 'implies' n1= conditionalOrExpression )*
            {
            pushFollow(FOLLOW_conditionalOrExpression_in_conditionalImpliesExpression654);
            nCndOrExp=conditionalOrExpression();

            state._fsp--;

            n = nCndOrExp;
            // OCL.g:216:5: (op= 'implies' n1= conditionalOrExpression )*
            loop14:
            do {
                int alt14=2;
                int LA14_0 = input.LA(1);

                if ( (LA14_0==52) ) {
                    alt14=1;
                }


                switch (alt14) {
            	case 1 :
            	    // OCL.g:216:7: op= 'implies' n1= conditionalOrExpression
            	    {
            	    op=(Token)match(input,52,FOLLOW_52_in_conditionalImpliesExpression667); 
            	    pushFollow(FOLLOW_conditionalOrExpression_in_conditionalImpliesExpression671);
            	    n1=conditionalOrExpression();

            	    state._fsp--;

            	     n = new ASTBinaryExpression(op, n, n1); 

            	    }
            	    break;

            	default :
            	    break loop14;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "conditionalImpliesExpression"


    // $ANTLR start "conditionalOrExpression"
    // OCL.g:225:1: conditionalOrExpression returns [ASTExpression n] : nCndXorExp= conditionalXOrExpression (op= 'or' n1= conditionalXOrExpression )* ;
    public final ASTExpression conditionalOrExpression() throws RecognitionException {
        ASTExpression n = null;

        Token op=null;
        ASTExpression nCndXorExp = null;

        ASTExpression n1 = null;


        try {
            // OCL.g:226:1: (nCndXorExp= conditionalXOrExpression (op= 'or' n1= conditionalXOrExpression )* )
            // OCL.g:227:5: nCndXorExp= conditionalXOrExpression (op= 'or' n1= conditionalXOrExpression )*
            {
            pushFollow(FOLLOW_conditionalXOrExpression_in_conditionalOrExpression716);
            nCndXorExp=conditionalXOrExpression();

            state._fsp--;

            n = nCndXorExp;
            // OCL.g:228:5: (op= 'or' n1= conditionalXOrExpression )*
            loop15:
            do {
                int alt15=2;
                int LA15_0 = input.LA(1);

                if ( (LA15_0==53) ) {
                    alt15=1;
                }


                switch (alt15) {
            	case 1 :
            	    // OCL.g:228:7: op= 'or' n1= conditionalXOrExpression
            	    {
            	    op=(Token)match(input,53,FOLLOW_53_in_conditionalOrExpression729); 
            	    pushFollow(FOLLOW_conditionalXOrExpression_in_conditionalOrExpression733);
            	    n1=conditionalXOrExpression();

            	    state._fsp--;

            	     n = new ASTBinaryExpression(op, n, n1); 

            	    }
            	    break;

            	default :
            	    break loop15;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "conditionalOrExpression"


    // $ANTLR start "conditionalXOrExpression"
    // OCL.g:237:1: conditionalXOrExpression returns [ASTExpression n] : nCndAndExp= conditionalAndExpression (op= 'xor' n1= conditionalAndExpression )* ;
    public final ASTExpression conditionalXOrExpression() throws RecognitionException {
        ASTExpression n = null;

        Token op=null;
        ASTExpression nCndAndExp = null;

        ASTExpression n1 = null;


        try {
            // OCL.g:238:1: (nCndAndExp= conditionalAndExpression (op= 'xor' n1= conditionalAndExpression )* )
            // OCL.g:239:5: nCndAndExp= conditionalAndExpression (op= 'xor' n1= conditionalAndExpression )*
            {
            pushFollow(FOLLOW_conditionalAndExpression_in_conditionalXOrExpression777);
            nCndAndExp=conditionalAndExpression();

            state._fsp--;

            n = nCndAndExp;
            // OCL.g:240:5: (op= 'xor' n1= conditionalAndExpression )*
            loop16:
            do {
                int alt16=2;
                int LA16_0 = input.LA(1);

                if ( (LA16_0==54) ) {
                    alt16=1;
                }


                switch (alt16) {
            	case 1 :
            	    // OCL.g:240:7: op= 'xor' n1= conditionalAndExpression
            	    {
            	    op=(Token)match(input,54,FOLLOW_54_in_conditionalXOrExpression790); 
            	    pushFollow(FOLLOW_conditionalAndExpression_in_conditionalXOrExpression794);
            	    n1=conditionalAndExpression();

            	    state._fsp--;

            	     n = new ASTBinaryExpression(op, n, n1); 

            	    }
            	    break;

            	default :
            	    break loop16;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "conditionalXOrExpression"


    // $ANTLR start "conditionalAndExpression"
    // OCL.g:249:1: conditionalAndExpression returns [ASTExpression n] : nEqExp= equalityExpression (op= 'and' n1= equalityExpression )* ;
    public final ASTExpression conditionalAndExpression() throws RecognitionException {
        ASTExpression n = null;

        Token op=null;
        ASTExpression nEqExp = null;

        ASTExpression n1 = null;


        try {
            // OCL.g:250:1: (nEqExp= equalityExpression (op= 'and' n1= equalityExpression )* )
            // OCL.g:251:5: nEqExp= equalityExpression (op= 'and' n1= equalityExpression )*
            {
            pushFollow(FOLLOW_equalityExpression_in_conditionalAndExpression838);
            nEqExp=equalityExpression();

            state._fsp--;

            n = nEqExp;
            // OCL.g:252:5: (op= 'and' n1= equalityExpression )*
            loop17:
            do {
                int alt17=2;
                int LA17_0 = input.LA(1);

                if ( (LA17_0==55) ) {
                    alt17=1;
                }


                switch (alt17) {
            	case 1 :
            	    // OCL.g:252:7: op= 'and' n1= equalityExpression
            	    {
            	    op=(Token)match(input,55,FOLLOW_55_in_conditionalAndExpression851); 
            	    pushFollow(FOLLOW_equalityExpression_in_conditionalAndExpression855);
            	    n1=equalityExpression();

            	    state._fsp--;

            	     n = new ASTBinaryExpression(op, n, n1); 

            	    }
            	    break;

            	default :
            	    break loop17;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "conditionalAndExpression"


    // $ANTLR start "equalityExpression"
    // OCL.g:261:1: equalityExpression returns [ASTExpression n] : nRelExp= relationalExpression ( ( EQUAL | NOT_EQUAL ) n1= relationalExpression )* ;
    public final ASTExpression equalityExpression() throws RecognitionException {
        ASTExpression n = null;

        ASTExpression nRelExp = null;

        ASTExpression n1 = null;


         Token op = null; 
        try {
            // OCL.g:263:1: (nRelExp= relationalExpression ( ( EQUAL | NOT_EQUAL ) n1= relationalExpression )* )
            // OCL.g:264:5: nRelExp= relationalExpression ( ( EQUAL | NOT_EQUAL ) n1= relationalExpression )*
            {
            pushFollow(FOLLOW_relationalExpression_in_equalityExpression903);
            nRelExp=relationalExpression();

            state._fsp--;

            n = nRelExp;
            // OCL.g:265:5: ( ( EQUAL | NOT_EQUAL ) n1= relationalExpression )*
            loop18:
            do {
                int alt18=2;
                int LA18_0 = input.LA(1);

                if ( (LA18_0==EQUAL||LA18_0==NOT_EQUAL) ) {
                    alt18=1;
                }


                switch (alt18) {
            	case 1 :
            	    // OCL.g:265:7: ( EQUAL | NOT_EQUAL ) n1= relationalExpression
            	    {
            	     op = input.LT(1); 
            	    if ( input.LA(1)==EQUAL||input.LA(1)==NOT_EQUAL ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_relationalExpression_in_equalityExpression932);
            	    n1=relationalExpression();

            	    state._fsp--;

            	     n = new ASTBinaryExpression(op, n, n1); 

            	    }
            	    break;

            	default :
            	    break loop18;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "equalityExpression"


    // $ANTLR start "relationalExpression"
    // OCL.g:275:1: relationalExpression returns [ASTExpression n] : nAddiExp= additiveExpression ( ( LESS | GREATER | LESS_EQUAL | GREATER_EQUAL ) n1= additiveExpression )* ;
    public final ASTExpression relationalExpression() throws RecognitionException {
        ASTExpression n = null;

        ASTExpression nAddiExp = null;

        ASTExpression n1 = null;


         Token op = null; 
        try {
            // OCL.g:277:1: (nAddiExp= additiveExpression ( ( LESS | GREATER | LESS_EQUAL | GREATER_EQUAL ) n1= additiveExpression )* )
            // OCL.g:278:5: nAddiExp= additiveExpression ( ( LESS | GREATER | LESS_EQUAL | GREATER_EQUAL ) n1= additiveExpression )*
            {
            pushFollow(FOLLOW_additiveExpression_in_relationalExpression981);
            nAddiExp=additiveExpression();

            state._fsp--;

            n = nAddiExp;
            // OCL.g:279:5: ( ( LESS | GREATER | LESS_EQUAL | GREATER_EQUAL ) n1= additiveExpression )*
            loop19:
            do {
                int alt19=2;
                int LA19_0 = input.LA(1);

                if ( ((LA19_0>=LESS && LA19_0<=GREATER_EQUAL)) ) {
                    alt19=1;
                }


                switch (alt19) {
            	case 1 :
            	    // OCL.g:279:7: ( LESS | GREATER | LESS_EQUAL | GREATER_EQUAL ) n1= additiveExpression
            	    {
            	     op = input.LT(1); 
            	    if ( (input.LA(1)>=LESS && input.LA(1)<=GREATER_EQUAL) ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_additiveExpression_in_relationalExpression1017);
            	    n1=additiveExpression();

            	    state._fsp--;

            	     n = new ASTBinaryExpression(op, n, n1); 

            	    }
            	    break;

            	default :
            	    break loop19;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "relationalExpression"


    // $ANTLR start "additiveExpression"
    // OCL.g:289:1: additiveExpression returns [ASTExpression n] : nMulExp= multiplicativeExpression ( ( PLUS | MINUS ) n1= multiplicativeExpression )* ;
    public final ASTExpression additiveExpression() throws RecognitionException {
        ASTExpression n = null;

        ASTExpression nMulExp = null;

        ASTExpression n1 = null;


         Token op = null; 
        try {
            // OCL.g:291:1: (nMulExp= multiplicativeExpression ( ( PLUS | MINUS ) n1= multiplicativeExpression )* )
            // OCL.g:292:5: nMulExp= multiplicativeExpression ( ( PLUS | MINUS ) n1= multiplicativeExpression )*
            {
            pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression1067);
            nMulExp=multiplicativeExpression();

            state._fsp--;

            n = nMulExp;
            // OCL.g:293:5: ( ( PLUS | MINUS ) n1= multiplicativeExpression )*
            loop20:
            do {
                int alt20=2;
                int LA20_0 = input.LA(1);

                if ( ((LA20_0>=PLUS && LA20_0<=MINUS)) ) {
                    alt20=1;
                }


                switch (alt20) {
            	case 1 :
            	    // OCL.g:293:7: ( PLUS | MINUS ) n1= multiplicativeExpression
            	    {
            	     op = input.LT(1); 
            	    if ( (input.LA(1)>=PLUS && input.LA(1)<=MINUS) ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_multiplicativeExpression_in_additiveExpression1095);
            	    n1=multiplicativeExpression();

            	    state._fsp--;

            	     n = new ASTBinaryExpression(op, n, n1);

            	    }
            	    break;

            	default :
            	    break loop20;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "additiveExpression"


    // $ANTLR start "multiplicativeExpression"
    // OCL.g:304:1: multiplicativeExpression returns [ASTExpression n] : nUnExp= unaryExpression ( ( STAR | SLASH | 'div' ) n1= unaryExpression )* ;
    public final ASTExpression multiplicativeExpression() throws RecognitionException {
        ASTExpression n = null;

        ASTExpression nUnExp = null;

        ASTExpression n1 = null;


         Token op = null; 
        try {
            // OCL.g:306:1: (nUnExp= unaryExpression ( ( STAR | SLASH | 'div' ) n1= unaryExpression )* )
            // OCL.g:307:5: nUnExp= unaryExpression ( ( STAR | SLASH | 'div' ) n1= unaryExpression )*
            {
            pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression1145);
            nUnExp=unaryExpression();

            state._fsp--;

             n = nUnExp;
            // OCL.g:308:5: ( ( STAR | SLASH | 'div' ) n1= unaryExpression )*
            loop21:
            do {
                int alt21=2;
                int LA21_0 = input.LA(1);

                if ( ((LA21_0>=STAR && LA21_0<=SLASH)||LA21_0==56) ) {
                    alt21=1;
                }


                switch (alt21) {
            	case 1 :
            	    // OCL.g:308:7: ( STAR | SLASH | 'div' ) n1= unaryExpression
            	    {
            	     op = input.LT(1); 
            	    if ( (input.LA(1)>=STAR && input.LA(1)<=SLASH)||input.LA(1)==56 ) {
            	        input.consume();
            	        state.errorRecovery=false;
            	    }
            	    else {
            	        MismatchedSetException mse = new MismatchedSetException(null,input);
            	        throw mse;
            	    }

            	    pushFollow(FOLLOW_unaryExpression_in_multiplicativeExpression1177);
            	    n1=unaryExpression();

            	    state._fsp--;

            	     n = new ASTBinaryExpression(op, n, n1); 

            	    }
            	    break;

            	default :
            	    break loop21;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "multiplicativeExpression"


    // $ANTLR start "unaryExpression"
    // OCL.g:320:1: unaryExpression returns [ASTExpression n] : ( ( ( 'not' | MINUS | PLUS ) nUnExp= unaryExpression ) | nPosExp= postfixExpression );
    public final ASTExpression unaryExpression() throws RecognitionException {
        ASTExpression n = null;

        ASTExpression nUnExp = null;

        ASTExpression nPosExp = null;


         Token op = null; 
        try {
            // OCL.g:322:1: ( ( ( 'not' | MINUS | PLUS ) nUnExp= unaryExpression ) | nPosExp= postfixExpression )
            int alt22=2;
            int LA22_0 = input.LA(1);

            if ( ((LA22_0>=PLUS && LA22_0<=MINUS)||LA22_0==57) ) {
                alt22=1;
            }
            else if ( (LA22_0==IDENT||LA22_0==LPAREN||(LA22_0>=INT && LA22_0<=HASH)||(LA22_0>=59 && LA22_0<=63)||(LA22_0>=67 && LA22_0<=77)) ) {
                alt22=2;
            }
            else {
                NoViableAltException nvae =
                    new NoViableAltException("", 22, 0, input);

                throw nvae;
            }
            switch (alt22) {
                case 1 :
                    // OCL.g:323:7: ( ( 'not' | MINUS | PLUS ) nUnExp= unaryExpression )
                    {
                    // OCL.g:323:7: ( ( 'not' | MINUS | PLUS ) nUnExp= unaryExpression )
                    // OCL.g:323:9: ( 'not' | MINUS | PLUS ) nUnExp= unaryExpression
                    {
                     op = input.LT(1); 
                    if ( (input.LA(1)>=PLUS && input.LA(1)<=MINUS)||input.LA(1)==57 ) {
                        input.consume();
                        state.errorRecovery=false;
                    }
                    else {
                        MismatchedSetException mse = new MismatchedSetException(null,input);
                        throw mse;
                    }

                    pushFollow(FOLLOW_unaryExpression_in_unaryExpression1264);
                    nUnExp=unaryExpression();

                    state._fsp--;

                     n = new ASTUnaryExpression(op, nUnExp); 

                    }


                    }
                    break;
                case 2 :
                    // OCL.g:327:7: nPosExp= postfixExpression
                    {
                    pushFollow(FOLLOW_postfixExpression_in_unaryExpression1284);
                    nPosExp=postfixExpression();

                    state._fsp--;

                     n = nPosExp; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "unaryExpression"


    // $ANTLR start "postfixExpression"
    // OCL.g:335:1: postfixExpression returns [ASTExpression n] : nPrimExp= primaryExpression ( ( ARROW | DOT ) nPc= propertyCall[$n, arrow] )* ;
    public final ASTExpression postfixExpression() throws RecognitionException {
        ASTExpression n = null;

        ASTExpression nPrimExp = null;

        ASTExpression nPc = null;


         boolean arrow = false; 
        try {
            // OCL.g:337:1: (nPrimExp= primaryExpression ( ( ARROW | DOT ) nPc= propertyCall[$n, arrow] )* )
            // OCL.g:338:5: nPrimExp= primaryExpression ( ( ARROW | DOT ) nPc= propertyCall[$n, arrow] )*
            {
            pushFollow(FOLLOW_primaryExpression_in_postfixExpression1317);
            nPrimExp=primaryExpression();

            state._fsp--;

             n = nPrimExp; 
            // OCL.g:339:5: ( ( ARROW | DOT ) nPc= propertyCall[$n, arrow] )*
            loop24:
            do {
                int alt24=2;
                int LA24_0 = input.LA(1);

                if ( ((LA24_0>=ARROW && LA24_0<=DOT)) ) {
                    alt24=1;
                }


                switch (alt24) {
            	case 1 :
            	    // OCL.g:340:6: ( ARROW | DOT ) nPc= propertyCall[$n, arrow]
            	    {
            	    // OCL.g:340:6: ( ARROW | DOT )
            	    int alt23=2;
            	    int LA23_0 = input.LA(1);

            	    if ( (LA23_0==ARROW) ) {
            	        alt23=1;
            	    }
            	    else if ( (LA23_0==DOT) ) {
            	        alt23=2;
            	    }
            	    else {
            	        NoViableAltException nvae =
            	            new NoViableAltException("", 23, 0, input);

            	        throw nvae;
            	    }
            	    switch (alt23) {
            	        case 1 :
            	            // OCL.g:340:8: ARROW
            	            {
            	            match(input,ARROW,FOLLOW_ARROW_in_postfixExpression1335); 
            	             arrow = true; 

            	            }
            	            break;
            	        case 2 :
            	            // OCL.g:340:34: DOT
            	            {
            	            match(input,DOT,FOLLOW_DOT_in_postfixExpression1341); 
            	             arrow = false; 

            	            }
            	            break;

            	    }

            	    pushFollow(FOLLOW_propertyCall_in_postfixExpression1352);
            	    nPc=propertyCall(n, arrow);

            	    state._fsp--;

            	     n = nPc; 

            	    }
            	    break;

            	default :
            	    break loop24;
                }
            } while (true);


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "postfixExpression"


    // $ANTLR start "primaryExpression"
    // OCL.g:356:1: primaryExpression returns [ASTExpression n] : (nLit= literal | nPc= propertyCall[null, false] | LPAREN nExp= expression RPAREN | nIfExp= ifExpression | id1= IDENT DOT 'allInstances' ( LPAREN RPAREN )? ( AT 'pre' )? );
    public final ASTExpression primaryExpression() throws RecognitionException {
        ASTExpression n = null;

        Token id1=null;
        ASTExpression nLit = null;

        ASTExpression nPc = null;

        ASTExpression nExp = null;

        ASTExpression nIfExp = null;


        try {
            // OCL.g:357:1: (nLit= literal | nPc= propertyCall[null, false] | LPAREN nExp= expression RPAREN | nIfExp= ifExpression | id1= IDENT DOT 'allInstances' ( LPAREN RPAREN )? ( AT 'pre' )? )
            int alt27=5;
            switch ( input.LA(1) ) {
            case INT:
            case REAL:
            case STRING:
            case HASH:
            case 67:
            case 68:
            case 69:
            case 70:
            case 71:
            case 72:
            case 73:
            case 74:
            case 75:
            case 76:
            case 77:
                {
                alt27=1;
                }
                break;
            case IDENT:
                {
                int LA27_2 = input.LA(2);

                if ( (LA27_2==EOF||(LA27_2>=EQUAL && LA27_2<=ARROW)||(LA27_2>=AT && LA27_2<=BAR)||LA27_2==LBRACK||(LA27_2>=RBRACE && LA27_2<=DOTDOT)||(LA27_2>=45 && LA27_2<=49)||(LA27_2>=51 && LA27_2<=56)||(LA27_2>=64 && LA27_2<=66)) ) {
                    alt27=2;
                }
                else if ( (LA27_2==DOT) ) {
                    int LA27_6 = input.LA(3);

                    if ( (LA27_6==58) ) {
                        alt27=5;
                    }
                    else if ( (LA27_6==IDENT||(LA27_6>=59 && LA27_6<=62)) ) {
                        alt27=2;
                    }
                    else {
                        NoViableAltException nvae =
                            new NoViableAltException("", 27, 6, input);

                        throw nvae;
                    }
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 27, 2, input);

                    throw nvae;
                }
                }
                break;
            case 59:
            case 60:
            case 61:
            case 62:
                {
                alt27=2;
                }
                break;
            case LPAREN:
                {
                alt27=3;
                }
                break;
            case 63:
                {
                alt27=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 27, 0, input);

                throw nvae;
            }

            switch (alt27) {
                case 1 :
                    // OCL.g:358:7: nLit= literal
                    {
                    pushFollow(FOLLOW_literal_in_primaryExpression1392);
                    nLit=literal();

                    state._fsp--;

                     n = nLit; 

                    }
                    break;
                case 2 :
                    // OCL.g:359:7: nPc= propertyCall[null, false]
                    {
                    pushFollow(FOLLOW_propertyCall_in_primaryExpression1404);
                    nPc=propertyCall(null, false);

                    state._fsp--;

                     n = nPc; 

                    }
                    break;
                case 3 :
                    // OCL.g:360:7: LPAREN nExp= expression RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression1415); 
                    pushFollow(FOLLOW_expression_in_primaryExpression1419);
                    nExp=expression();

                    state._fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression1421); 
                     n = nExp; 

                    }
                    break;
                case 4 :
                    // OCL.g:361:7: nIfExp= ifExpression
                    {
                    pushFollow(FOLLOW_ifExpression_in_primaryExpression1433);
                    nIfExp=ifExpression();

                    state._fsp--;

                     n = nIfExp; 

                    }
                    break;
                case 5 :
                    // OCL.g:363:7: id1= IDENT DOT 'allInstances' ( LPAREN RPAREN )? ( AT 'pre' )?
                    {
                    id1=(Token)match(input,IDENT,FOLLOW_IDENT_in_primaryExpression1450); 
                    match(input,DOT,FOLLOW_DOT_in_primaryExpression1452); 
                    match(input,58,FOLLOW_58_in_primaryExpression1454); 
                    // OCL.g:363:36: ( LPAREN RPAREN )?
                    int alt25=2;
                    int LA25_0 = input.LA(1);

                    if ( (LA25_0==LPAREN) ) {
                        alt25=1;
                    }
                    switch (alt25) {
                        case 1 :
                            // OCL.g:363:38: LPAREN RPAREN
                            {
                            match(input,LPAREN,FOLLOW_LPAREN_in_primaryExpression1458); 
                            match(input,RPAREN,FOLLOW_RPAREN_in_primaryExpression1460); 

                            }
                            break;

                    }

                     n = new ASTAllInstancesExpression(id1); 
                    // OCL.g:365:7: ( AT 'pre' )?
                    int alt26=2;
                    int LA26_0 = input.LA(1);

                    if ( (LA26_0==AT) ) {
                        alt26=1;
                    }
                    switch (alt26) {
                        case 1 :
                            // OCL.g:365:9: AT 'pre'
                            {
                            match(input,AT,FOLLOW_AT_in_primaryExpression1481); 
                            match(input,48,FOLLOW_48_in_primaryExpression1483); 
                             n.setIsPre(); 

                            }
                            break;

                    }


                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "primaryExpression"


    // $ANTLR start "propertyCall"
    // OCL.g:379:1: propertyCall[ASTExpression source, boolean followsArrow] returns [ASTExpression n] : ({...}?nExpQuery= queryExpression[source] | nExpIterate= iterateExpression[source] | nExpOperation= operationExpression[source, followsArrow] | nExpType= typeExpression[source, followsArrow] );
    public final ASTExpression propertyCall(ASTExpression source, boolean followsArrow) throws RecognitionException {
        ASTExpression n = null;

        ASTExpression nExpQuery = null;

        ASTExpression nExpIterate = null;

        ASTOperationExpression nExpOperation = null;

        ASTTypeArgExpression nExpType = null;


        try {
            // OCL.g:380:1: ({...}?nExpQuery= queryExpression[source] | nExpIterate= iterateExpression[source] | nExpOperation= operationExpression[source, followsArrow] | nExpType= typeExpression[source, followsArrow] )
            int alt28=4;
            switch ( input.LA(1) ) {
            case IDENT:
                {
                int LA28_1 = input.LA(2);

                if ( (( ParserHelper.isQueryIdent(input.LT(1)) )) ) {
                    alt28=1;
                }
                else if ( (true) ) {
                    alt28=3;
                }
                else {
                    NoViableAltException nvae =
                        new NoViableAltException("", 28, 1, input);

                    throw nvae;
                }
                }
                break;
            case 59:
                {
                alt28=2;
                }
                break;
            case 60:
            case 61:
            case 62:
                {
                alt28=4;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 28, 0, input);

                throw nvae;
            }

            switch (alt28) {
                case 1 :
                    // OCL.g:385:7: {...}?nExpQuery= queryExpression[source]
                    {
                    if ( !(( ParserHelper.isQueryIdent(input.LT(1)) )) ) {
                        throw new FailedPredicateException(input, "propertyCall", " ParserHelper.isQueryIdent(input.LT(1)) ");
                    }
                    pushFollow(FOLLOW_queryExpression_in_propertyCall1555);
                    nExpQuery=queryExpression(source);

                    state._fsp--;

                     n = nExpQuery; 

                    }
                    break;
                case 2 :
                    // OCL.g:387:7: nExpIterate= iterateExpression[source]
                    {
                    pushFollow(FOLLOW_iterateExpression_in_propertyCall1568);
                    nExpIterate=iterateExpression(source);

                    state._fsp--;

                     n = nExpIterate; 

                    }
                    break;
                case 3 :
                    // OCL.g:388:7: nExpOperation= operationExpression[source, followsArrow]
                    {
                    pushFollow(FOLLOW_operationExpression_in_propertyCall1581);
                    nExpOperation=operationExpression(source, followsArrow);

                    state._fsp--;

                     n = nExpOperation;

                    }
                    break;
                case 4 :
                    // OCL.g:389:7: nExpType= typeExpression[source, followsArrow]
                    {
                    pushFollow(FOLLOW_typeExpression_in_propertyCall1594);
                    nExpType=typeExpression(source, followsArrow);

                    state._fsp--;

                     n = nExpType; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "propertyCall"


    // $ANTLR start "queryExpression"
    // OCL.g:399:1: queryExpression[ASTExpression range] returns [ASTExpression n] : op= IDENT LPAREN (decls= elemVarsDeclaration BAR )? nExp= expression RPAREN ;
    public final ASTExpression queryExpression(ASTExpression range) throws RecognitionException {
        ASTExpression n = null;

        Token op=null;
        ASTElemVarsDeclaration decls = null;

        ASTExpression nExp = null;


        ASTElemVarsDeclaration decl = new ASTElemVarsDeclaration(); 
        try {
            // OCL.g:400:69: (op= IDENT LPAREN (decls= elemVarsDeclaration BAR )? nExp= expression RPAREN )
            // OCL.g:401:5: op= IDENT LPAREN (decls= elemVarsDeclaration BAR )? nExp= expression RPAREN
            {
            op=(Token)match(input,IDENT,FOLLOW_IDENT_in_queryExpression1630); 
            match(input,LPAREN,FOLLOW_LPAREN_in_queryExpression1637); 
            // OCL.g:403:5: (decls= elemVarsDeclaration BAR )?
            int alt29=2;
            int LA29_0 = input.LA(1);

            if ( (LA29_0==IDENT) ) {
                int LA29_1 = input.LA(2);

                if ( (LA29_1==COLON||LA29_1==COMMA||LA29_1==BAR) ) {
                    alt29=1;
                }
            }
            switch (alt29) {
                case 1 :
                    // OCL.g:403:7: decls= elemVarsDeclaration BAR
                    {
                    pushFollow(FOLLOW_elemVarsDeclaration_in_queryExpression1648);
                    decls=elemVarsDeclaration();

                    state._fsp--;

                    decl = decls;
                    match(input,BAR,FOLLOW_BAR_in_queryExpression1652); 

                    }
                    break;

            }

            pushFollow(FOLLOW_expression_in_queryExpression1663);
            nExp=expression();

            state._fsp--;

            match(input,RPAREN,FOLLOW_RPAREN_in_queryExpression1669); 
             n = new ASTQueryExpression(op, range, decl, nExp); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "queryExpression"


    // $ANTLR start "iterateExpression"
    // OCL.g:417:1: iterateExpression[ASTExpression range] returns [ASTExpression n] : i= 'iterate' LPAREN decls= elemVarsDeclaration SEMI init= variableInitialization BAR nExp= expression RPAREN ;
    public final ASTExpression iterateExpression(ASTExpression range) throws RecognitionException {
        ASTExpression n = null;

        Token i=null;
        ASTElemVarsDeclaration decls = null;

        ASTVariableInitialization init = null;

        ASTExpression nExp = null;


        try {
            // OCL.g:417:65: (i= 'iterate' LPAREN decls= elemVarsDeclaration SEMI init= variableInitialization BAR nExp= expression RPAREN )
            // OCL.g:418:5: i= 'iterate' LPAREN decls= elemVarsDeclaration SEMI init= variableInitialization BAR nExp= expression RPAREN
            {
            i=(Token)match(input,59,FOLLOW_59_in_iterateExpression1701); 
            match(input,LPAREN,FOLLOW_LPAREN_in_iterateExpression1707); 
            pushFollow(FOLLOW_elemVarsDeclaration_in_iterateExpression1715);
            decls=elemVarsDeclaration();

            state._fsp--;

            match(input,SEMI,FOLLOW_SEMI_in_iterateExpression1717); 
            pushFollow(FOLLOW_variableInitialization_in_iterateExpression1725);
            init=variableInitialization();

            state._fsp--;

            match(input,BAR,FOLLOW_BAR_in_iterateExpression1727); 
            pushFollow(FOLLOW_expression_in_iterateExpression1735);
            nExp=expression();

            state._fsp--;

            match(input,RPAREN,FOLLOW_RPAREN_in_iterateExpression1741); 
             n = new ASTIterateExpression(i, range, decls, init, nExp); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "iterateExpression"


    // $ANTLR start "operationExpression"
    // OCL.g:439:1: operationExpression[ASTExpression source, boolean followsArrow] returns [ASTOperationExpression n] : name= IDENT ( LBRACK rolename= IDENT RBRACK )? ( AT 'pre' )? ( LPAREN (e= expression ( COMMA e= expression )* )? RPAREN )? ;
    public final ASTOperationExpression operationExpression(ASTExpression source, boolean followsArrow) throws RecognitionException {
        ASTOperationExpression n = null;

        Token name=null;
        Token rolename=null;
        ASTExpression e = null;


        try {
            // OCL.g:441:1: (name= IDENT ( LBRACK rolename= IDENT RBRACK )? ( AT 'pre' )? ( LPAREN (e= expression ( COMMA e= expression )* )? RPAREN )? )
            // OCL.g:442:5: name= IDENT ( LBRACK rolename= IDENT RBRACK )? ( AT 'pre' )? ( LPAREN (e= expression ( COMMA e= expression )* )? RPAREN )?
            {
            name=(Token)match(input,IDENT,FOLLOW_IDENT_in_operationExpression1785); 
             n = new ASTOperationExpression(name, source, followsArrow);
            // OCL.g:445:5: ( LBRACK rolename= IDENT RBRACK )?
            int alt30=2;
            int LA30_0 = input.LA(1);

            if ( (LA30_0==LBRACK) ) {
                alt30=1;
            }
            switch (alt30) {
                case 1 :
                    // OCL.g:445:7: LBRACK rolename= IDENT RBRACK
                    {
                    match(input,LBRACK,FOLLOW_LBRACK_in_operationExpression1801); 
                    rolename=(Token)match(input,IDENT,FOLLOW_IDENT_in_operationExpression1805); 
                    match(input,RBRACK,FOLLOW_RBRACK_in_operationExpression1807); 
                     n.setExplicitRolename(rolename); 

                    }
                    break;

            }

            // OCL.g:447:5: ( AT 'pre' )?
            int alt31=2;
            int LA31_0 = input.LA(1);

            if ( (LA31_0==AT) ) {
                alt31=1;
            }
            switch (alt31) {
                case 1 :
                    // OCL.g:447:7: AT 'pre'
                    {
                    match(input,AT,FOLLOW_AT_in_operationExpression1820); 
                    match(input,48,FOLLOW_48_in_operationExpression1822); 
                     n.setIsPre(); 

                    }
                    break;

            }

            // OCL.g:448:5: ( LPAREN (e= expression ( COMMA e= expression )* )? RPAREN )?
            int alt34=2;
            int LA34_0 = input.LA(1);

            if ( (LA34_0==LPAREN) ) {
                alt34=1;
            }
            switch (alt34) {
                case 1 :
                    // OCL.g:449:7: LPAREN (e= expression ( COMMA e= expression )* )? RPAREN
                    {
                    match(input,LPAREN,FOLLOW_LPAREN_in_operationExpression1843); 
                     n.hasParentheses(); 
                    // OCL.g:450:7: (e= expression ( COMMA e= expression )* )?
                    int alt33=2;
                    int LA33_0 = input.LA(1);

                    if ( (LA33_0==IDENT||LA33_0==LPAREN||(LA33_0>=PLUS && LA33_0<=MINUS)||(LA33_0>=INT && LA33_0<=HASH)||LA33_0==50||LA33_0==57||(LA33_0>=59 && LA33_0<=63)||(LA33_0>=67 && LA33_0<=77)) ) {
                        alt33=1;
                    }
                    switch (alt33) {
                        case 1 :
                            // OCL.g:451:7: e= expression ( COMMA e= expression )*
                            {
                            pushFollow(FOLLOW_expression_in_operationExpression1864);
                            e=expression();

                            state._fsp--;

                             n.addArg(e); 
                            // OCL.g:452:7: ( COMMA e= expression )*
                            loop32:
                            do {
                                int alt32=2;
                                int LA32_0 = input.LA(1);

                                if ( (LA32_0==COMMA) ) {
                                    alt32=1;
                                }


                                switch (alt32) {
                            	case 1 :
                            	    // OCL.g:452:9: COMMA e= expression
                            	    {
                            	    match(input,COMMA,FOLLOW_COMMA_in_operationExpression1876); 
                            	    pushFollow(FOLLOW_expression_in_operationExpression1880);
                            	    e=expression();

                            	    state._fsp--;

                            	     n.addArg(e); 

                            	    }
                            	    break;

                            	default :
                            	    break loop32;
                                }
                            } while (true);


                            }
                            break;

                    }

                    match(input,RPAREN,FOLLOW_RPAREN_in_operationExpression1900); 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "operationExpression"


    // $ANTLR start "typeExpression"
    // OCL.g:464:1: typeExpression[ASTExpression source, boolean followsArrow] returns [ASTTypeArgExpression n] : ( 'oclAsType' | 'oclIsKindOf' | 'oclIsTypeOf' ) LPAREN t= type RPAREN ;
    public final ASTTypeArgExpression typeExpression(ASTExpression source, boolean followsArrow) throws RecognitionException {
        ASTTypeArgExpression n = null;

        ASTType t = null;


         Token opToken = null; 
        try {
            // OCL.g:467:1: ( ( 'oclAsType' | 'oclIsKindOf' | 'oclIsTypeOf' ) LPAREN t= type RPAREN )
            // OCL.g:468:2: ( 'oclAsType' | 'oclIsKindOf' | 'oclIsTypeOf' ) LPAREN t= type RPAREN
            {
             opToken = input.LT(1); 
            if ( (input.LA(1)>=60 && input.LA(1)<=62) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            match(input,LPAREN,FOLLOW_LPAREN_in_typeExpression1959); 
            pushFollow(FOLLOW_type_in_typeExpression1963);
            t=type();

            state._fsp--;

            match(input,RPAREN,FOLLOW_RPAREN_in_typeExpression1965); 
             n = new ASTTypeArgExpression(opToken, source, t, followsArrow); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "typeExpression"


    // $ANTLR start "elemVarsDeclaration"
    // OCL.g:479:1: elemVarsDeclaration returns [ASTElemVarsDeclaration n] : idListRes= idList ( COLON t= type )? ;
    public final ASTElemVarsDeclaration elemVarsDeclaration() throws RecognitionException {
        ASTElemVarsDeclaration n = null;

        List idListRes = null;

        ASTType t = null;


         List idList; 
        try {
            // OCL.g:481:1: (idListRes= idList ( COLON t= type )? )
            // OCL.g:482:5: idListRes= idList ( COLON t= type )?
            {
            pushFollow(FOLLOW_idList_in_elemVarsDeclaration2004);
            idListRes=idList();

            state._fsp--;

            // OCL.g:483:5: ( COLON t= type )?
            int alt35=2;
            int LA35_0 = input.LA(1);

            if ( (LA35_0==COLON) ) {
                alt35=1;
            }
            switch (alt35) {
                case 1 :
                    // OCL.g:483:7: COLON t= type
                    {
                    match(input,COLON,FOLLOW_COLON_in_elemVarsDeclaration2012); 
                    pushFollow(FOLLOW_type_in_elemVarsDeclaration2016);
                    t=type();

                    state._fsp--;


                    }
                    break;

            }

             n = new ASTElemVarsDeclaration(idListRes, t); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "elemVarsDeclaration"


    // $ANTLR start "variableInitialization"
    // OCL.g:492:1: variableInitialization returns [ASTVariableInitialization n] : name= IDENT COLON t= type EQUAL e= expression ;
    public final ASTVariableInitialization variableInitialization() throws RecognitionException {
        ASTVariableInitialization n = null;

        Token name=null;
        ASTType t = null;

        ASTExpression e = null;


        try {
            // OCL.g:493:1: (name= IDENT COLON t= type EQUAL e= expression )
            // OCL.g:494:5: name= IDENT COLON t= type EQUAL e= expression
            {
            name=(Token)match(input,IDENT,FOLLOW_IDENT_in_variableInitialization2051); 
            match(input,COLON,FOLLOW_COLON_in_variableInitialization2053); 
            pushFollow(FOLLOW_type_in_variableInitialization2057);
            t=type();

            state._fsp--;

            match(input,EQUAL,FOLLOW_EQUAL_in_variableInitialization2059); 
            pushFollow(FOLLOW_expression_in_variableInitialization2063);
            e=expression();

            state._fsp--;

             n = new ASTVariableInitialization(name, t, e); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "variableInitialization"


    // $ANTLR start "ifExpression"
    // OCL.g:503:1: ifExpression returns [ASTExpression n] : i= 'if' cond= expression 'then' t= expression 'else' e= expression 'endif' ;
    public final ASTExpression ifExpression() throws RecognitionException {
        ASTExpression n = null;

        Token i=null;
        ASTExpression cond = null;

        ASTExpression t = null;

        ASTExpression e = null;


        try {
            // OCL.g:504:1: (i= 'if' cond= expression 'then' t= expression 'else' e= expression 'endif' )
            // OCL.g:505:5: i= 'if' cond= expression 'then' t= expression 'else' e= expression 'endif'
            {
            i=(Token)match(input,63,FOLLOW_63_in_ifExpression2095); 
            pushFollow(FOLLOW_expression_in_ifExpression2099);
            cond=expression();

            state._fsp--;

            match(input,64,FOLLOW_64_in_ifExpression2101); 
            pushFollow(FOLLOW_expression_in_ifExpression2105);
            t=expression();

            state._fsp--;

            match(input,65,FOLLOW_65_in_ifExpression2107); 
            pushFollow(FOLLOW_expression_in_ifExpression2111);
            e=expression();

            state._fsp--;

            match(input,66,FOLLOW_66_in_ifExpression2113); 
             n = new ASTIfExpression(i, cond, t, e); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "ifExpression"


    // $ANTLR start "literal"
    // OCL.g:523:1: literal returns [ASTExpression n] : (t= 'true' | f= 'false' | i= INT | r= REAL | s= STRING | HASH enumType= IDENT '::' enumLit= IDENT | nColIt= collectionLiteral | nEColIt= emptyCollectionLiteral | nUndLit= undefinedLiteral | nTupleLit= tupleLiteral );
    public final ASTExpression literal() throws RecognitionException {
        ASTExpression n = null;

        Token t=null;
        Token f=null;
        Token i=null;
        Token r=null;
        Token s=null;
        Token enumType=null;
        Token enumLit=null;
        ASTCollectionLiteral nColIt = null;

        ASTEmptyCollectionLiteral nEColIt = null;

        ASTUndefinedLiteral nUndLit = null;

        ASTTupleLiteral nTupleLit = null;


        try {
            // OCL.g:524:1: (t= 'true' | f= 'false' | i= INT | r= REAL | s= STRING | HASH enumType= IDENT '::' enumLit= IDENT | nColIt= collectionLiteral | nEColIt= emptyCollectionLiteral | nUndLit= undefinedLiteral | nTupleLit= tupleLiteral )
            int alt36=10;
            switch ( input.LA(1) ) {
            case 67:
                {
                alt36=1;
                }
                break;
            case 68:
                {
                alt36=2;
                }
                break;
            case INT:
                {
                alt36=3;
                }
                break;
            case REAL:
                {
                alt36=4;
                }
                break;
            case STRING:
                {
                alt36=5;
                }
                break;
            case HASH:
                {
                alt36=6;
                }
                break;
            case 69:
            case 70:
            case 71:
            case 72:
                {
                alt36=7;
                }
                break;
            case 73:
                {
                alt36=8;
                }
                break;
            case 74:
            case 75:
            case 76:
                {
                alt36=9;
                }
                break;
            case 77:
                {
                alt36=10;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 36, 0, input);

                throw nvae;
            }

            switch (alt36) {
                case 1 :
                    // OCL.g:525:7: t= 'true'
                    {
                    t=(Token)match(input,67,FOLLOW_67_in_literal2152); 
                     n = new ASTBooleanLiteral(true); 

                    }
                    break;
                case 2 :
                    // OCL.g:526:7: f= 'false'
                    {
                    f=(Token)match(input,68,FOLLOW_68_in_literal2166); 
                     n = new ASTBooleanLiteral(false); 

                    }
                    break;
                case 3 :
                    // OCL.g:527:7: i= INT
                    {
                    i=(Token)match(input,INT,FOLLOW_INT_in_literal2179); 
                     n = new ASTIntegerLiteral(i); 

                    }
                    break;
                case 4 :
                    // OCL.g:528:7: r= REAL
                    {
                    r=(Token)match(input,REAL,FOLLOW_REAL_in_literal2194); 
                     n = new ASTRealLiteral(r); 

                    }
                    break;
                case 5 :
                    // OCL.g:529:7: s= STRING
                    {
                    s=(Token)match(input,STRING,FOLLOW_STRING_in_literal2208); 
                     n = new ASTStringLiteral(s); 

                    }
                    break;
                case 6 :
                    // OCL.g:530:7: HASH enumType= IDENT '::' enumLit= IDENT
                    {
                    match(input,HASH,FOLLOW_HASH_in_literal2218); 
                    enumType=(Token)match(input,IDENT,FOLLOW_IDENT_in_literal2222); 
                    match(input,COLON_COLON,FOLLOW_COLON_COLON_in_literal2224); 
                    enumLit=(Token)match(input,IDENT,FOLLOW_IDENT_in_literal2228); 
                     System.out.println("enum literals...");n = new ASTEnumLiteral(enumType,enumLit);
                    		System.out.println("Type:"+enumType+"."+enumLit);

                    }
                    break;
                case 7 :
                    // OCL.g:532:7: nColIt= collectionLiteral
                    {
                    pushFollow(FOLLOW_collectionLiteral_in_literal2240);
                    nColIt=collectionLiteral();

                    state._fsp--;

                     n = nColIt; 

                    }
                    break;
                case 8 :
                    // OCL.g:533:7: nEColIt= emptyCollectionLiteral
                    {
                    pushFollow(FOLLOW_emptyCollectionLiteral_in_literal2252);
                    nEColIt=emptyCollectionLiteral();

                    state._fsp--;

                     n = nEColIt; 

                    }
                    break;
                case 9 :
                    // OCL.g:534:7: nUndLit= undefinedLiteral
                    {
                    pushFollow(FOLLOW_undefinedLiteral_in_literal2264);
                    nUndLit=undefinedLiteral();

                    state._fsp--;

                    n = nUndLit; 

                    }
                    break;
                case 10 :
                    // OCL.g:535:7: nTupleLit= tupleLiteral
                    {
                    pushFollow(FOLLOW_tupleLiteral_in_literal2276);
                    nTupleLit=tupleLiteral();

                    state._fsp--;

                    n = nTupleLit; 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "literal"


    // $ANTLR start "collectionLiteral"
    // OCL.g:543:1: collectionLiteral returns [ASTCollectionLiteral n] : ( 'Set' | 'Sequence' | 'Bag' | 'OrderedSet' ) LBRACE (ci= collectionItem ( COMMA ci= collectionItem )* )? RBRACE ;
    public final ASTCollectionLiteral collectionLiteral() throws RecognitionException {
        ASTCollectionLiteral n = null;

        ASTCollectionItem ci = null;


         Token op = null; 
        try {
            // OCL.g:545:1: ( ( 'Set' | 'Sequence' | 'Bag' | 'OrderedSet' ) LBRACE (ci= collectionItem ( COMMA ci= collectionItem )* )? RBRACE )
            // OCL.g:546:5: ( 'Set' | 'Sequence' | 'Bag' | 'OrderedSet' ) LBRACE (ci= collectionItem ( COMMA ci= collectionItem )* )? RBRACE
            {
             op = input.LT(1); 
            if ( (input.LA(1)>=69 && input.LA(1)<=72) ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

             n = new ASTCollectionLiteral(op); 
            match(input,LBRACE,FOLLOW_LBRACE_in_collectionLiteral2343); 
            // OCL.g:550:5: (ci= collectionItem ( COMMA ci= collectionItem )* )?
            int alt38=2;
            int LA38_0 = input.LA(1);

            if ( (LA38_0==IDENT||LA38_0==LPAREN||(LA38_0>=PLUS && LA38_0<=MINUS)||(LA38_0>=INT && LA38_0<=HASH)||LA38_0==50||LA38_0==57||(LA38_0>=59 && LA38_0<=63)||(LA38_0>=67 && LA38_0<=77)) ) {
                alt38=1;
            }
            switch (alt38) {
                case 1 :
                    // OCL.g:551:7: ci= collectionItem ( COMMA ci= collectionItem )*
                    {
                    pushFollow(FOLLOW_collectionItem_in_collectionLiteral2360);
                    ci=collectionItem();

                    state._fsp--;

                     n.addItem(ci); 
                    // OCL.g:552:7: ( COMMA ci= collectionItem )*
                    loop37:
                    do {
                        int alt37=2;
                        int LA37_0 = input.LA(1);

                        if ( (LA37_0==COMMA) ) {
                            alt37=1;
                        }


                        switch (alt37) {
                    	case 1 :
                    	    // OCL.g:552:9: COMMA ci= collectionItem
                    	    {
                    	    match(input,COMMA,FOLLOW_COMMA_in_collectionLiteral2373); 
                    	    pushFollow(FOLLOW_collectionItem_in_collectionLiteral2377);
                    	    ci=collectionItem();

                    	    state._fsp--;

                    	     n.addItem(ci); 

                    	    }
                    	    break;

                    	default :
                    	    break loop37;
                        }
                    } while (true);


                    }
                    break;

            }

            match(input,RBRACE,FOLLOW_RBRACE_in_collectionLiteral2396); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "collectionLiteral"


    // $ANTLR start "collectionItem"
    // OCL.g:561:1: collectionItem returns [ASTCollectionItem n] : e= expression ( DOTDOT e= expression )? ;
    public final ASTCollectionItem collectionItem() throws RecognitionException {
        ASTCollectionItem n = null;

        ASTExpression e = null;


         n = new ASTCollectionItem(); 
        try {
            // OCL.g:563:1: (e= expression ( DOTDOT e= expression )? )
            // OCL.g:564:5: e= expression ( DOTDOT e= expression )?
            {
            pushFollow(FOLLOW_expression_in_collectionItem2425);
            e=expression();

            state._fsp--;

             n.setFirst(e); 
            // OCL.g:565:5: ( DOTDOT e= expression )?
            int alt39=2;
            int LA39_0 = input.LA(1);

            if ( (LA39_0==DOTDOT) ) {
                alt39=1;
            }
            switch (alt39) {
                case 1 :
                    // OCL.g:565:7: DOTDOT e= expression
                    {
                    match(input,DOTDOT,FOLLOW_DOTDOT_in_collectionItem2436); 
                    pushFollow(FOLLOW_expression_in_collectionItem2440);
                    e=expression();

                    state._fsp--;

                     n.setSecond(e); 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "collectionItem"


    // $ANTLR start "emptyCollectionLiteral"
    // OCL.g:575:1: emptyCollectionLiteral returns [ASTEmptyCollectionLiteral n] : 'oclEmpty' LPAREN t= collectionType RPAREN ;
    public final ASTEmptyCollectionLiteral emptyCollectionLiteral() throws RecognitionException {
        ASTEmptyCollectionLiteral n = null;

        ASTCollectionType t = null;


        try {
            // OCL.g:576:1: ( 'oclEmpty' LPAREN t= collectionType RPAREN )
            // OCL.g:577:5: 'oclEmpty' LPAREN t= collectionType RPAREN
            {
            match(input,73,FOLLOW_73_in_emptyCollectionLiteral2469); 
            match(input,LPAREN,FOLLOW_LPAREN_in_emptyCollectionLiteral2471); 
            pushFollow(FOLLOW_collectionType_in_emptyCollectionLiteral2475);
            t=collectionType();

            state._fsp--;

            match(input,RPAREN,FOLLOW_RPAREN_in_emptyCollectionLiteral2477); 
             n = new ASTEmptyCollectionLiteral(t); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "emptyCollectionLiteral"


    // $ANTLR start "undefinedLiteral"
    // OCL.g:588:1: undefinedLiteral returns [ASTUndefinedLiteral n] : ( 'oclUndefined' LPAREN t= type RPAREN | 'Undefined' | 'null' );
    public final ASTUndefinedLiteral undefinedLiteral() throws RecognitionException {
        ASTUndefinedLiteral n = null;

        ASTType t = null;


        try {
            // OCL.g:589:1: ( 'oclUndefined' LPAREN t= type RPAREN | 'Undefined' | 'null' )
            int alt40=3;
            switch ( input.LA(1) ) {
            case 74:
                {
                alt40=1;
                }
                break;
            case 75:
                {
                alt40=2;
                }
                break;
            case 76:
                {
                alt40=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 40, 0, input);

                throw nvae;
            }

            switch (alt40) {
                case 1 :
                    // OCL.g:590:5: 'oclUndefined' LPAREN t= type RPAREN
                    {
                    match(input,74,FOLLOW_74_in_undefinedLiteral2507); 
                    match(input,LPAREN,FOLLOW_LPAREN_in_undefinedLiteral2509); 
                    pushFollow(FOLLOW_type_in_undefinedLiteral2513);
                    t=type();

                    state._fsp--;

                    match(input,RPAREN,FOLLOW_RPAREN_in_undefinedLiteral2515); 
                     n = new ASTUndefinedLiteral(t); 

                    }
                    break;
                case 2 :
                    // OCL.g:593:5: 'Undefined'
                    {
                    match(input,75,FOLLOW_75_in_undefinedLiteral2529); 
                     n = new ASTUndefinedLiteral(); 

                    }
                    break;
                case 3 :
                    // OCL.g:596:5: 'null'
                    {
                    match(input,76,FOLLOW_76_in_undefinedLiteral2543); 
                     n = new ASTUndefinedLiteral(); 

                    }
                    break;

            }
        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "undefinedLiteral"


    // $ANTLR start "tupleLiteral"
    // OCL.g:605:1: tupleLiteral returns [ASTTupleLiteral n] : 'Tuple' LBRACE ti= tupleItem ( COMMA ti= tupleItem )* RBRACE ;
    public final ASTTupleLiteral tupleLiteral() throws RecognitionException {
        ASTTupleLiteral n = null;

        ASTTupleItem ti = null;


         List tiList = new ArrayList(); 
        try {
            // OCL.g:607:1: ( 'Tuple' LBRACE ti= tupleItem ( COMMA ti= tupleItem )* RBRACE )
            // OCL.g:608:5: 'Tuple' LBRACE ti= tupleItem ( COMMA ti= tupleItem )* RBRACE
            {
            match(input,77,FOLLOW_77_in_tupleLiteral2577); 
            match(input,LBRACE,FOLLOW_LBRACE_in_tupleLiteral2583); 
            pushFollow(FOLLOW_tupleItem_in_tupleLiteral2591);
            ti=tupleItem();

            state._fsp--;

             tiList.add(ti); 
            // OCL.g:611:5: ( COMMA ti= tupleItem )*
            loop41:
            do {
                int alt41=2;
                int LA41_0 = input.LA(1);

                if ( (LA41_0==COMMA) ) {
                    alt41=1;
                }


                switch (alt41) {
            	case 1 :
            	    // OCL.g:611:7: COMMA ti= tupleItem
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_tupleLiteral2602); 
            	    pushFollow(FOLLOW_tupleItem_in_tupleLiteral2606);
            	    ti=tupleItem();

            	    state._fsp--;

            	     tiList.add(ti); 

            	    }
            	    break;

            	default :
            	    break loop41;
                }
            } while (true);

            match(input,RBRACE,FOLLOW_RBRACE_in_tupleLiteral2617); 
             n = new ASTTupleLiteral(tiList); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "tupleLiteral"


    // $ANTLR start "tupleItem"
    // OCL.g:619:1: tupleItem returns [ASTTupleItem n] : name= IDENT ( COLON | EQUAL ) e= expression ;
    public final ASTTupleItem tupleItem() throws RecognitionException {
        ASTTupleItem n = null;

        Token name=null;
        ASTExpression e = null;


        try {
            // OCL.g:620:1: (name= IDENT ( COLON | EQUAL ) e= expression )
            // OCL.g:621:5: name= IDENT ( COLON | EQUAL ) e= expression
            {
            name=(Token)match(input,IDENT,FOLLOW_IDENT_in_tupleItem2648); 
            if ( input.LA(1)==COLON||input.LA(1)==EQUAL ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            pushFollow(FOLLOW_expression_in_tupleItem2658);
            e=expression();

            state._fsp--;

             n = new ASTTupleItem(name, e); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "tupleItem"


    // $ANTLR start "type"
    // OCL.g:632:1: type returns [ASTType n] : (nTSimple= simpleType | nTCollection= collectionType | nTTuple= tupleType ) ;
    public final ASTType type() throws RecognitionException {
        ASTType n = null;

        ASTSimpleType nTSimple = null;

        ASTCollectionType nTCollection = null;

        ASTTupleType nTTuple = null;


         Token tok = null; 
        try {
            // OCL.g:634:1: ( (nTSimple= simpleType | nTCollection= collectionType | nTTuple= tupleType ) )
            // OCL.g:635:5: (nTSimple= simpleType | nTCollection= collectionType | nTTuple= tupleType )
            {
             tok = input.LT(1); /* remember start of type */ 
            // OCL.g:636:5: (nTSimple= simpleType | nTCollection= collectionType | nTTuple= tupleType )
            int alt42=3;
            switch ( input.LA(1) ) {
            case IDENT:
                {
                alt42=1;
                }
                break;
            case 69:
            case 70:
            case 71:
            case 72:
            case 78:
                {
                alt42=2;
                }
                break;
            case 77:
                {
                alt42=3;
                }
                break;
            default:
                NoViableAltException nvae =
                    new NoViableAltException("", 42, 0, input);

                throw nvae;
            }

            switch (alt42) {
                case 1 :
                    // OCL.g:637:7: nTSimple= simpleType
                    {
                    pushFollow(FOLLOW_simpleType_in_type2711);
                    nTSimple=simpleType();

                    state._fsp--;

                     n = nTSimple; n.setStartToken(tok); 

                    }
                    break;
                case 2 :
                    // OCL.g:638:7: nTCollection= collectionType
                    {
                    pushFollow(FOLLOW_collectionType_in_type2723);
                    nTCollection=collectionType();

                    state._fsp--;

                     n = nTCollection; n.setStartToken(tok); 

                    }
                    break;
                case 3 :
                    // OCL.g:639:7: nTTuple= tupleType
                    {
                    pushFollow(FOLLOW_tupleType_in_type2735);
                    nTTuple=tupleType();

                    state._fsp--;

                     n = nTTuple; n.setStartToken(tok); 

                    }
                    break;

            }


            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "type"


    // $ANTLR start "typeOnly"
    // OCL.g:644:1: typeOnly returns [ASTType n] : nT= type ;
    public final ASTType typeOnly() throws RecognitionException {
        ASTType n = null;

        ASTType nT = null;


        try {
            // OCL.g:645:1: (nT= type )
            // OCL.g:646:5: nT= type
            {
            pushFollow(FOLLOW_type_in_typeOnly2767);
            nT=type();

            state._fsp--;

             n = nT; 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "typeOnly"


    // $ANTLR start "simpleType"
    // OCL.g:656:1: simpleType returns [ASTSimpleType n] : name= IDENT ;
    public final ASTSimpleType simpleType() throws RecognitionException {
        ASTSimpleType n = null;

        Token name=null;

        try {
            // OCL.g:657:1: (name= IDENT )
            // OCL.g:658:5: name= IDENT
            {
            name=(Token)match(input,IDENT,FOLLOW_IDENT_in_simpleType2795); 
             n = new ASTSimpleType(name); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "simpleType"


    // $ANTLR start "collectionType"
    // OCL.g:666:1: collectionType returns [ASTCollectionType n] : ( 'Collection' | 'Set' | 'Sequence' | 'Bag' | 'OrderedSet' ) LPAREN elemType= type RPAREN ;
    public final ASTCollectionType collectionType() throws RecognitionException {
        ASTCollectionType n = null;

        ASTType elemType = null;


         Token op = null; 
        try {
            // OCL.g:668:1: ( ( 'Collection' | 'Set' | 'Sequence' | 'Bag' | 'OrderedSet' ) LPAREN elemType= type RPAREN )
            // OCL.g:669:5: ( 'Collection' | 'Set' | 'Sequence' | 'Bag' | 'OrderedSet' ) LPAREN elemType= type RPAREN
            {
             op = input.LT(1); 
            if ( (input.LA(1)>=69 && input.LA(1)<=72)||input.LA(1)==78 ) {
                input.consume();
                state.errorRecovery=false;
            }
            else {
                MismatchedSetException mse = new MismatchedSetException(null,input);
                throw mse;
            }

            match(input,LPAREN,FOLLOW_LPAREN_in_collectionType2860); 
            pushFollow(FOLLOW_type_in_collectionType2864);
            elemType=type();

            state._fsp--;

            match(input,RPAREN,FOLLOW_RPAREN_in_collectionType2866); 
             n = new ASTCollectionType(op, elemType); n.setStartToken(op);

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "collectionType"


    // $ANTLR start "tupleType"
    // OCL.g:679:1: tupleType returns [ASTTupleType n] : 'Tuple' LPAREN tp= tuplePart ( COMMA tp= tuplePart )* RPAREN ;
    public final ASTTupleType tupleType() throws RecognitionException {
        ASTTupleType n = null;

        ASTTuplePart tp = null;


         List tpList = new ArrayList(); 
        try {
            // OCL.g:681:1: ( 'Tuple' LPAREN tp= tuplePart ( COMMA tp= tuplePart )* RPAREN )
            // OCL.g:682:5: 'Tuple' LPAREN tp= tuplePart ( COMMA tp= tuplePart )* RPAREN
            {
            match(input,77,FOLLOW_77_in_tupleType2900); 
            match(input,LPAREN,FOLLOW_LPAREN_in_tupleType2902); 
            pushFollow(FOLLOW_tuplePart_in_tupleType2911);
            tp=tuplePart();

            state._fsp--;

             tpList.add(tp); 
            // OCL.g:684:5: ( COMMA tp= tuplePart )*
            loop43:
            do {
                int alt43=2;
                int LA43_0 = input.LA(1);

                if ( (LA43_0==COMMA) ) {
                    alt43=1;
                }


                switch (alt43) {
            	case 1 :
            	    // OCL.g:684:7: COMMA tp= tuplePart
            	    {
            	    match(input,COMMA,FOLLOW_COMMA_in_tupleType2922); 
            	    pushFollow(FOLLOW_tuplePart_in_tupleType2926);
            	    tp=tuplePart();

            	    state._fsp--;

            	     tpList.add(tp); 

            	    }
            	    break;

            	default :
            	    break loop43;
                }
            } while (true);

            match(input,RPAREN,FOLLOW_RPAREN_in_tupleType2938); 
             n = new ASTTupleType(tpList); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "tupleType"


    // $ANTLR start "tuplePart"
    // OCL.g:693:1: tuplePart returns [ASTTuplePart n] : name= IDENT COLON t= type ;
    public final ASTTuplePart tuplePart() throws RecognitionException {
        ASTTuplePart n = null;

        Token name=null;
        ASTType t = null;


        try {
            // OCL.g:694:1: (name= IDENT COLON t= type )
            // OCL.g:695:5: name= IDENT COLON t= type
            {
            name=(Token)match(input,IDENT,FOLLOW_IDENT_in_tuplePart2970); 
            match(input,COLON,FOLLOW_COLON_in_tuplePart2972); 
            pushFollow(FOLLOW_type_in_tuplePart2976);
            t=type();

            state._fsp--;

             n = new ASTTuplePart(name, t); 

            }

        }
        catch (RecognitionException re) {
            reportError(re);
            recover(input,re);
        }
        finally {
        }
        return n;
    }
    // $ANTLR end "tuplePart"

    // Delegated rules


 

    public static final BitSet FOLLOW_expression_in_expressionOnly52 = new BitSet(new long[]{0x0000000000000000L});
    public static final BitSet FOLLOW_EOF_in_expressionOnly56 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_43_in_oclexpression75 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENT_in_oclexpression77 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_invariant_in_oclexpression82 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_prePost_in_oclexpression91 = new BitSet(new long[]{0x0000200000000000L});
    public static final BitSet FOLLOW_EOF_in_oclexpression97 = new BitSet(new long[]{0x0000100000000000L});
    public static final BitSet FOLLOW_44_in_oclexpression100 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_45_in_invariant120 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENT_in_invariant129 = new BitSet(new long[]{0x0000C00000000002L});
    public static final BitSet FOLLOW_invariantClause_in_invariant154 = new BitSet(new long[]{0x0000C00000000002L});
    public static final BitSet FOLLOW_46_in_invariantClause185 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_IDENT_in_invariantClause191 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_COLON_in_invariantClause196 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_expression_in_invariantClause200 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_47_in_invariantClause210 = new BitSet(new long[]{0x0000400000000000L});
    public static final BitSet FOLLOW_46_in_invariantClause212 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_IDENT_in_invariantClause218 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_COLON_in_invariantClause223 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_expression_in_invariantClause227 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_45_in_prePost252 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENT_in_prePost256 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_COLON_COLON_in_prePost258 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENT_in_prePost262 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_paramList_in_prePost266 = new BitSet(new long[]{0x0003000000000020L});
    public static final BitSet FOLLOW_COLON_in_prePost270 = new BitSet(new long[]{0x0000000000000010L,0x00000000000061E0L});
    public static final BitSet FOLLOW_type_in_prePost274 = new BitSet(new long[]{0x0003000000000020L});
    public static final BitSet FOLLOW_prePostClause_in_prePost293 = new BitSet(new long[]{0x0003000000000022L});
    public static final BitSet FOLLOW_set_in_prePostClause332 = new BitSet(new long[]{0x0000000000000030L});
    public static final BitSet FOLLOW_IDENT_in_prePostClause347 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_COLON_in_prePostClause352 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_expression_in_prePostClause356 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_50_in_expression404 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENT_in_expression408 = new BitSet(new long[]{0x00000000000000A0L});
    public static final BitSet FOLLOW_COLON_in_expression412 = new BitSet(new long[]{0x0000000000000010L,0x00000000000061E0L});
    public static final BitSet FOLLOW_type_in_expression416 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_EQUAL_in_expression421 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_expression_in_expression425 = new BitSet(new long[]{0x0008000000000000L});
    public static final BitSet FOLLOW_51_in_expression427 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_conditionalImpliesExpression_in_expression452 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_paramList485 = new BitSet(new long[]{0x0000000000000410L});
    public static final BitSet FOLLOW_variableDeclaration_in_paramList502 = new BitSet(new long[]{0x0000000000000600L});
    public static final BitSet FOLLOW_COMMA_in_paramList514 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_variableDeclaration_in_paramList518 = new BitSet(new long[]{0x0000000000000600L});
    public static final BitSet FOLLOW_RPAREN_in_paramList538 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_idList567 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_COMMA_in_idList577 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENT_in_idList581 = new BitSet(new long[]{0x0000000000000202L});
    public static final BitSet FOLLOW_IDENT_in_variableDeclaration612 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_COLON_in_variableDeclaration614 = new BitSet(new long[]{0x0000000000000010L,0x00000000000061E0L});
    public static final BitSet FOLLOW_type_in_variableDeclaration618 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_conditionalOrExpression_in_conditionalImpliesExpression654 = new BitSet(new long[]{0x0010000000000002L});
    public static final BitSet FOLLOW_52_in_conditionalImpliesExpression667 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_conditionalOrExpression_in_conditionalImpliesExpression671 = new BitSet(new long[]{0x0010000000000002L});
    public static final BitSet FOLLOW_conditionalXOrExpression_in_conditionalOrExpression716 = new BitSet(new long[]{0x0020000000000002L});
    public static final BitSet FOLLOW_53_in_conditionalOrExpression729 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_conditionalXOrExpression_in_conditionalOrExpression733 = new BitSet(new long[]{0x0020000000000002L});
    public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalXOrExpression777 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_54_in_conditionalXOrExpression790 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_conditionalAndExpression_in_conditionalXOrExpression794 = new BitSet(new long[]{0x0040000000000002L});
    public static final BitSet FOLLOW_equalityExpression_in_conditionalAndExpression838 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_55_in_conditionalAndExpression851 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_equalityExpression_in_conditionalAndExpression855 = new BitSet(new long[]{0x0080000000000002L});
    public static final BitSet FOLLOW_relationalExpression_in_equalityExpression903 = new BitSet(new long[]{0x0000000000000882L});
    public static final BitSet FOLLOW_set_in_equalityExpression922 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_relationalExpression_in_equalityExpression932 = new BitSet(new long[]{0x0000000000000882L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression981 = new BitSet(new long[]{0x000000000000F002L});
    public static final BitSet FOLLOW_set_in_relationalExpression999 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_additiveExpression_in_relationalExpression1017 = new BitSet(new long[]{0x000000000000F002L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression1067 = new BitSet(new long[]{0x0000000000030002L});
    public static final BitSet FOLLOW_set_in_additiveExpression1085 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_multiplicativeExpression_in_additiveExpression1095 = new BitSet(new long[]{0x0000000000030002L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression1145 = new BitSet(new long[]{0x01000000000C0002L});
    public static final BitSet FOLLOW_set_in_multiplicativeExpression1163 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_unaryExpression_in_multiplicativeExpression1177 = new BitSet(new long[]{0x01000000000C0002L});
    public static final BitSet FOLLOW_set_in_unaryExpression1240 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_unaryExpression_in_unaryExpression1264 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_postfixExpression_in_unaryExpression1284 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_primaryExpression_in_postfixExpression1317 = new BitSet(new long[]{0x0000000000300002L});
    public static final BitSet FOLLOW_ARROW_in_postfixExpression1335 = new BitSet(new long[]{0x7800000000000010L});
    public static final BitSet FOLLOW_DOT_in_postfixExpression1341 = new BitSet(new long[]{0x7800000000000010L});
    public static final BitSet FOLLOW_propertyCall_in_postfixExpression1352 = new BitSet(new long[]{0x0000000000300002L});
    public static final BitSet FOLLOW_literal_in_primaryExpression1392 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_propertyCall_in_primaryExpression1404 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression1415 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_expression_in_primaryExpression1419 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression1421 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_ifExpression_in_primaryExpression1433 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_primaryExpression1450 = new BitSet(new long[]{0x0000000000200000L});
    public static final BitSet FOLLOW_DOT_in_primaryExpression1452 = new BitSet(new long[]{0x0400000000000000L});
    public static final BitSet FOLLOW_58_in_primaryExpression1454 = new BitSet(new long[]{0x0000000000400102L});
    public static final BitSet FOLLOW_LPAREN_in_primaryExpression1458 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_primaryExpression1460 = new BitSet(new long[]{0x0000000000400002L});
    public static final BitSet FOLLOW_AT_in_primaryExpression1481 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_primaryExpression1483 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_queryExpression_in_propertyCall1555 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_iterateExpression_in_propertyCall1568 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_operationExpression_in_propertyCall1581 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_typeExpression_in_propertyCall1594 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_queryExpression1630 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_LPAREN_in_queryExpression1637 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_elemVarsDeclaration_in_queryExpression1648 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_BAR_in_queryExpression1652 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_expression_in_queryExpression1663 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_queryExpression1669 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_59_in_iterateExpression1701 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_LPAREN_in_iterateExpression1707 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_elemVarsDeclaration_in_iterateExpression1715 = new BitSet(new long[]{0x0000000001000000L});
    public static final BitSet FOLLOW_SEMI_in_iterateExpression1717 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_variableInitialization_in_iterateExpression1725 = new BitSet(new long[]{0x0000000000800000L});
    public static final BitSet FOLLOW_BAR_in_iterateExpression1727 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_expression_in_iterateExpression1735 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_iterateExpression1741 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_operationExpression1785 = new BitSet(new long[]{0x0000000002400102L});
    public static final BitSet FOLLOW_LBRACK_in_operationExpression1801 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENT_in_operationExpression1805 = new BitSet(new long[]{0x0000000004000000L});
    public static final BitSet FOLLOW_RBRACK_in_operationExpression1807 = new BitSet(new long[]{0x0000000000400102L});
    public static final BitSet FOLLOW_AT_in_operationExpression1820 = new BitSet(new long[]{0x0001000000000000L});
    public static final BitSet FOLLOW_48_in_operationExpression1822 = new BitSet(new long[]{0x0000000000000102L});
    public static final BitSet FOLLOW_LPAREN_in_operationExpression1843 = new BitSet(new long[]{0xFA04000078030510L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_expression_in_operationExpression1864 = new BitSet(new long[]{0x0000000000000600L});
    public static final BitSet FOLLOW_COMMA_in_operationExpression1876 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_expression_in_operationExpression1880 = new BitSet(new long[]{0x0000000000000600L});
    public static final BitSet FOLLOW_RPAREN_in_operationExpression1900 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_typeExpression1943 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_LPAREN_in_typeExpression1959 = new BitSet(new long[]{0x0000000000000010L,0x00000000000061E0L});
    public static final BitSet FOLLOW_type_in_typeExpression1963 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_typeExpression1965 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_idList_in_elemVarsDeclaration2004 = new BitSet(new long[]{0x0000000000000022L});
    public static final BitSet FOLLOW_COLON_in_elemVarsDeclaration2012 = new BitSet(new long[]{0x0000000000000010L,0x00000000000061E0L});
    public static final BitSet FOLLOW_type_in_elemVarsDeclaration2016 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_variableInitialization2051 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_COLON_in_variableInitialization2053 = new BitSet(new long[]{0x0000000000000010L,0x00000000000061E0L});
    public static final BitSet FOLLOW_type_in_variableInitialization2057 = new BitSet(new long[]{0x0000000000000080L});
    public static final BitSet FOLLOW_EQUAL_in_variableInitialization2059 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_expression_in_variableInitialization2063 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_63_in_ifExpression2095 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_expression_in_ifExpression2099 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000001L});
    public static final BitSet FOLLOW_64_in_ifExpression2101 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_expression_in_ifExpression2105 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000002L});
    public static final BitSet FOLLOW_65_in_ifExpression2107 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_expression_in_ifExpression2111 = new BitSet(new long[]{0x0000000000000000L,0x0000000000000004L});
    public static final BitSet FOLLOW_66_in_ifExpression2113 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_67_in_literal2152 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_68_in_literal2166 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_INT_in_literal2179 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_REAL_in_literal2194 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_STRING_in_literal2208 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_HASH_in_literal2218 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENT_in_literal2222 = new BitSet(new long[]{0x0000000000000040L});
    public static final BitSet FOLLOW_COLON_COLON_in_literal2224 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_IDENT_in_literal2228 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collectionLiteral_in_literal2240 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_emptyCollectionLiteral_in_literal2252 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_undefinedLiteral_in_literal2264 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tupleLiteral_in_literal2276 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_collectionLiteral2314 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LBRACE_in_collectionLiteral2343 = new BitSet(new long[]{0xFA04000178030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_collectionItem_in_collectionLiteral2360 = new BitSet(new long[]{0x0000000100000200L});
    public static final BitSet FOLLOW_COMMA_in_collectionLiteral2373 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_collectionItem_in_collectionLiteral2377 = new BitSet(new long[]{0x0000000100000200L});
    public static final BitSet FOLLOW_RBRACE_in_collectionLiteral2396 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_expression_in_collectionItem2425 = new BitSet(new long[]{0x0000000200000002L});
    public static final BitSet FOLLOW_DOTDOT_in_collectionItem2436 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_expression_in_collectionItem2440 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_73_in_emptyCollectionLiteral2469 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_LPAREN_in_emptyCollectionLiteral2471 = new BitSet(new long[]{0x0000000000000000L,0x00000000000041E0L});
    public static final BitSet FOLLOW_collectionType_in_emptyCollectionLiteral2475 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_emptyCollectionLiteral2477 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_74_in_undefinedLiteral2507 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_LPAREN_in_undefinedLiteral2509 = new BitSet(new long[]{0x0000000000000010L,0x00000000000061E0L});
    public static final BitSet FOLLOW_type_in_undefinedLiteral2513 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_undefinedLiteral2515 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_75_in_undefinedLiteral2529 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_76_in_undefinedLiteral2543 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_77_in_tupleLiteral2577 = new BitSet(new long[]{0x0000000080000000L});
    public static final BitSet FOLLOW_LBRACE_in_tupleLiteral2583 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_tupleItem_in_tupleLiteral2591 = new BitSet(new long[]{0x0000000100000200L});
    public static final BitSet FOLLOW_COMMA_in_tupleLiteral2602 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_tupleItem_in_tupleLiteral2606 = new BitSet(new long[]{0x0000000100000200L});
    public static final BitSet FOLLOW_RBRACE_in_tupleLiteral2617 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_tupleItem2648 = new BitSet(new long[]{0x00000000000000A0L});
    public static final BitSet FOLLOW_set_in_tupleItem2650 = new BitSet(new long[]{0xFA04000078030110L,0x0000000000003FF8L});
    public static final BitSet FOLLOW_expression_in_tupleItem2658 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_simpleType_in_type2711 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_collectionType_in_type2723 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_tupleType_in_type2735 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_type_in_typeOnly2767 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_simpleType2795 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_set_in_collectionType2833 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_LPAREN_in_collectionType2860 = new BitSet(new long[]{0x0000000000000010L,0x00000000000061E0L});
    public static final BitSet FOLLOW_type_in_collectionType2864 = new BitSet(new long[]{0x0000000000000400L});
    public static final BitSet FOLLOW_RPAREN_in_collectionType2866 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_77_in_tupleType2900 = new BitSet(new long[]{0x0000000000000100L});
    public static final BitSet FOLLOW_LPAREN_in_tupleType2902 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_tuplePart_in_tupleType2911 = new BitSet(new long[]{0x0000000000000600L});
    public static final BitSet FOLLOW_COMMA_in_tupleType2922 = new BitSet(new long[]{0x0000000000000010L});
    public static final BitSet FOLLOW_tuplePart_in_tupleType2926 = new BitSet(new long[]{0x0000000000000600L});
    public static final BitSet FOLLOW_RPAREN_in_tupleType2938 = new BitSet(new long[]{0x0000000000000002L});
    public static final BitSet FOLLOW_IDENT_in_tuplePart2970 = new BitSet(new long[]{0x0000000000000020L});
    public static final BitSet FOLLOW_COLON_in_tuplePart2972 = new BitSet(new long[]{0x0000000000000010L,0x00000000000061E0L});
    public static final BitSet FOLLOW_type_in_tuplePart2976 = new BitSet(new long[]{0x0000000000000002L});

}