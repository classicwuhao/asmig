/* +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++Written by: Hao Wu++++++++++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 *
 *	This is a part of my PhD work.
 *  haowu@cs.nuim.ie
 *  APR-2012 
 *  
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * ++++++++++++++++++++++++++++++Do or do not, there is no try.+++++++++++++++++++++++++
 * +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */
package atongmu.translator;
/*
 * e2a: ecore metamodel 2 attributed type graph
 */
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.io.File;
import java.util.Iterator;
import java.util.Collections;
import org.eclipse.emf.common.*;
import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;
import atongmu.atg.GraphNode;
import atongmu.atg.Edge;
import atongmu.atg.Factory;
import atongmu.atg.Type;
import atongmu.atg.PrimitiveType;


public final class e2a {
	//private List<EAttribute> attrs = new ArrayList<EAttribute>();
	private HashSet attrset = new HashSet();
	private Bound bound;	// give a bound to each class
	private Factory factory; // construct nodes for each class with respect to the bound.
	private HashMap<EClass,List<EClass>> itree = new HashMap<EClass,List<EClass>>();
	private String ecorefile;
	
	private e2a(){
		//default constructor;
	}

	public e2a(Factory f){
		factory = f;
	}

	/* deal with ecore directly 
     * setup bound and factory
     * ready to allocate
	 */
	public e2a(String file){
		ecorefile = file;
		bound = new Bound(file);
		factory = new Factory(bound);
		builditree();
		//calculateBound();
		//printitree();
		//test1();
		bound.boundEClass("Class",2);
		//bound.boundEClass("Method",5);
		//bound.boundEClass("Field",5);
		//bound.boundEClass("Field",4);
		//bound.boundEClass("Person",2);
	}

	private void TranslateEClass(){
		List<EClass> classes = bound.getMetaclasses();
		for (int i=0;i<classes.size();i++){
			EClass cls = classes.get(i);
			if (cls.isAbstract()) {factory.makeGraphNode(cls,true); continue;}
			factory.makeGraphNode(cls,false);
			attrset.clear();
			attrset = TranslateEAttributes(cls);
			for (Iterator<EAttribute> it = attrset.iterator();it.hasNext();){
				EAttribute attr = it.next();
				EDataType type = attr.getEAttributeType();
				
				if (type == EcorePackage.Literals.EBOOLEAN){
					//System.out.println(cls.getName()+"->"+attr.getName());
					factory.makeNodeAttribute(cls,attr.getName(),PrimitiveType.BOOL);
				}
				else if (type == EcorePackage.Literals.EINT){
					//System.out.println(cls.getName()+"->"+attr.getName());
					factory.makeNodeAttribute(cls,attr.getName(),PrimitiveType.INT);
				}
				else if (type == EcorePackage.Literals.ESTRING){
					//System.out.println(cls.getName()+"->"+attr.getName());
					factory.makeNodeAttribute(cls,attr,attr.getName(),PrimitiveType.STRING);
				}
				else if (type instanceof EEnum){
					//System.out.println("Enum found:"+attr.getName());
					EEnum t = (EEnum) type;
					factory.makeNodeAttribute(cls,t,attr.getName(),PrimitiveType.ENUM);
					
				}
			}
		}
		
	}
	
	private HashSet TranslateEAttributes(EClass cls){
		List<EClass> supers = cls.getESuperTypes();
		
		for (int i=0;i<supers.size();i++){
			TranslateEAttributes(supers.get(i));
		}

		for (int j=0;j<cls.getEAttributes().size();j++){
			EAttribute attr = cls.getEAttributes().get(j);
			attrset.add(attr);
		}
		
		return attrset;
	}


	private void TranslateEReference(){
		List<EReference> references = bound.getMetareferences();
		boolean bi;
		String fullname;
		for (int i=0;i<references.size();i++){
			/* un/bi-direction reference */
			EReference ref = references.get(i);
			EClass enda;
			EClass endb;
			if (ref.getEOpposite()!=null){
				enda = ref.getEReferenceType();
				endb = ref.getEOpposite().getEReferenceType();
				fullname=ref.getName();
				fullname+=":" + ref.getEOpposite().getName();
				references.remove(ref.getEOpposite());
				bi=true;
			}
			else{
				enda = ref.getEContainingClass();
				endb = ref.getEReferenceType();
				fullname = ref.getName();
				bi=false;
			}

			if (enda==null || endb==null )
				throw new TranslatorException
					("Error: Check your reference["+ref.getName()+"], because its type is not defined.");

			/* use hash set to get rid of duplicated children */
			HashSet childrena = new HashSet();
			HashSet childrenb = new HashSet();
			childrena.add(enda);childrenb.add(endb);
			getAllChildren(enda,childrena);
			getAllChildren(endb,childrenb);

			/* this is really bad, need to be done in a clever way */
			List<EClass> list1 = new ArrayList<EClass>();
			List<EClass> list2 = new ArrayList<EClass>();
			for (Iterator<EClass> it = childrena.iterator(); 
			it.hasNext();) list1.add(it.next());
			
			for (Iterator<EClass> it = childrenb.iterator();
			it.hasNext();) list2.add(it.next());
			factory.makeSrcDest(ref,list1,list2,fullname,bi);
			
		}
	}
	/* we do need an itree to track the inheritance relationships. */
	private void builditree(){
		List<EClass> classes = bound.getMetaclasses();

		for (int i=0;i<classes.size();i++){	
			EClass cls = classes.get(i);
			List<EClass> set;
			if (cls.getESuperTypes().size()>0){
				for (int j=0;j<cls.getESuperTypes().size();j++){
					EClass supercls = cls.getESuperTypes().get(j);				
					if (!itree.containsKey(supercls))
						set = new ArrayList<EClass>();
					else
						set = itree.get(supercls);
					
					set.add(cls);
					itree.put(supercls,set);
				}
			}
			/* @BUG this bug is fixed!!! */
			/*else{
				itree.put(cls,new ArrayList<EClass>());
			}*/
		}
		
		/* this bug is fixed!!! */
		/*List<EClass> keys = new ArrayList<EClass>();
		for(EClass cls : itree.keySet()) if (itree.get(cls).size()==0) keys.add(cls);		
		for (int i=0;i<keys.size();i++) itree.remove(keys.get(i));*/
	}

	private List<EClass> getChildren(EClass cls){
		if (itree.containsKey(cls))
			return itree.get(cls);
		else
			return null;
	}

	public void getAllChildren(EClass cls, HashSet children){
		if (children==null) return;
		if (itree.containsKey(cls)){
			List<EClass> classes = itree.get(cls);
			for (int i=0;i<classes.size();i++){
				getAllChildren(classes.get(i),children);
				children.add(classes.get(i));
			}
		}
	}

	/* testing method(s) for ECore-2-ATG translator */
	/*private void test1(){
		List<EClass> classes = bound.getMetaclasses();
		List<EClass> children = new ArrayList<EClass>();
		EClass cls = classes.get(0);
		
		for (int i=0;i<classes.size();i++)
			if (classes.get(i).getName().compareTo("Commentable")==0){
				cls = classes.get(i);
				break;
			}

		System.out.print(cls.getName()+" {");
		getAllChildren(cls,children);
		
		for (EClass k : children)
			System.out.print(k.getName()+",");
		System.out.println("}\n");
	}*/
	
	/* String representation of an itree */
	public String itree2String(){
		StringBuffer sb = new StringBuffer();
		for (EClass cls : itree.keySet()){
			sb.append(cls.getName()+"-> {");
			for (EClass child : itree.get(cls))
				sb.append(child.getName()+",");
			sb.append("}\n");
		}
		
		return sb.toString();
	}

	public String toString(){
		if (factory!=null)
			return factory.toString(false);
		else
			return new Factory(bound).toString(false);
	}

	public Factory getFactory(){
		return (factory==null) ?  new Factory(bound) : factory;
	}

	public Bound getBound(){
		return (bound==null) ? new Bound(ecorefile) : bound;
	}

	public EEnum getEnumbyName(String name){
		EEnum eenum = getBound().getEEnum(name);
		if (eenum==null) throw new TranslatorException("error: can not find enumeration type:<"+ name+ ">");
		return eenum;
	}

	public void translate(){
		TranslateEClass();
		TranslateEReference();
	}

	public void calculateBound(){
		BoundCalculator cal = new BoundCalculator(bound);
		HashMap<String, Integer> bounds = cal.calculate();
		
		if (bounds == null) {
			cal.reset(); // remove variables anyway.
			throw new BoundException("Error: automatic bound calculation is failed.");
		}

		for (String key : bounds.keySet())
			//System.err.println(key+"="+bounds.get(key));
			bound.boundEClass(key,bounds.get(key));
		
		cal.reset();
		System.err.println("Total bound:"+bound.getTotalBound());
	}

}
