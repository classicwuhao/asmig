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
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.io.File;
import org.eclipse.emf.common.*;
import org.eclipse.emf.common.util.*;
import org.eclipse.emf.ecore.*;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;

/* 
 * You have to be very careful to allocate a bound for each class.
 * 
 * TODO: We have to check bound for each class according to their
 * multiplicities defined over the metamodel.  This has already been done.
 * See BoundCalculator.java for details.
 * 
 */
public class Bound{
	private HashMap <EClass,bound> bounds = new HashMap<EClass,bound>();
	private HashMap <EAttribute,bound> str_bounds = new HashMap<EAttribute,bound>();
	private List<EClass> classes;
	private List<EReference> references;
	private List<EAttribute> attributes;
	private List<EEnum> enums;
	private String ecorefile;
	private ResourceSet resourceSet = new ResourceSetImpl();
	private File myEcoreFile;
	private URI myEcoreURI;
	private Resource myEcoreResource;
	private static bound d; // for default bound only;
	public static Bound DEFAULT = new Bound(1);
	private final int STRBOUND=5; // this is for string only.
	
	public Bound(String file){
		ecorefile=file;
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());
		myEcoreFile = new File(file);
		myEcoreURI = URI.createFileURI(myEcoreFile.getAbsolutePath());
		myEcoreResource = resourceSet.getResource(myEcoreURI, true);
		getObjects(); //load everything we need
		initDefault();
	}
	
	private Bound(int b){
		//default constructor
		d = new bound(b);
	}

	/* bound to a default number */
	public Bound(List<EClass> c){
		if (c==null)
			throw new BoundException("cannot set bound for an empty list");
		classes = c;
		initDefault();
	}

	private void initDefault(){
		for (int i=0;i<classes.size();i++)
			bounds.put(classes.get(i),d);

		for (int i=0;i<attributes.size();i++)
			str_bounds.put(attributes.get(i),new bound(STRBOUND));
	}

	public void boundEClass(EClass cls, int b){
		if (!bounds.containsKey(cls))
			//throw an exception: it is bounded already.
			throw new BoundException("object ["+cls.getName()+"] cannot be founded in the model.");
		
		bounds.put(cls,new bound(b));
	}

	public void boundEClass(String name, int b){
		for (EClass cls : bounds.keySet()){
			if (cls.getName().compareTo(name)==0){
				this.boundEClass(cls,b);
				return;
			}
		}
	}
	
	public void boundString(EAttribute attr, int b){
		if (attr.getEAttributeType()!=EcorePackage.Literals.ESTRING)
			throw new BoundException("Attribute ["+attr.getName()+"] is not type of EString.");

		str_bounds.put(attr, new bound(b));
	}

	public int getBound(EAttribute attr){
		if (attr==null) throw new BoundException("Attribute is a null object.");
		if (!str_bounds.containsKey(attr))
			throw new BoundException("Attribute ["+attr.getName()+"] cannot be founded for string bound.");

		return str_bounds.get(attr).getbound();
	}


	/* change a bound of a class in the metamodel */
	public void setbound(EClass cls, int b){
		if (cls==null)
			throw new BoundException("Cannot bound an empty object.");

		if (!bounds.containsKey(cls))
			throw new BoundException("failed to bound class["+cls.getName()+"] because it cannot be found.");
		
		bounds.put(cls,new bound(b));		
	}

	public void setbound(String name, int b){
		if (name==null)	
			throw new BoundException("Cannot bound an empty object.");
		
		for (EClass cls : bounds.keySet()){
			if (cls.getName().compareTo(name)==0){
				this.setbound(cls,b);
				return;
			}
		}
		
		/* we allow users to bound an unknown object, comment this line out. */
		//throw new BoundExpcetion("Cannot bound object["+ name +"] because it doesn't exist in the model.");		
	}

	public int getBound(EClass cls){
		if (!bounds.containsKey(cls))
			return DEFAULT.getDefault();
		else
			return bounds.get(cls).getbound();
	}
	
	public int getTotalBound(){
		int total=0;
		for (EClass cls : bounds.keySet())
			if (!cls.isAbstract())
				total+=bounds.get(cls).getbound();
		return total;
	}

	public int getDefault(){
		return d.getbound();		
	}

	public void changeDefault(int n){
		d.setbound(n);
	}

	/* this should be called after loading objects from an ecore file */
	public EEnum getEEnum (String name){
		for (EEnum e : enums)
			if (e.getName().compareTo(name)==0)
				return e;
		return null;
	}

	public List<EClass> getMetaclasses(){
		if (classes==null){
			throw new BoundException("error: cannot return empty classes.");
		}

		return classes;
	}

	public List<EReference> getMetareferences(){
		if (references==null){
			throw new BoundException("error: cannot return empty references.");
		}
		
		return references;
	}

	/* return the 1st reference that matches with RefName */
	public EReference getReferenceByName(String RefName){
		for (int i=0;i<references.size();i++)
			if (references.get(i).getName().compareTo(RefName)==0)
				return references.get(i);
		
		return null;
	}

	private void getObjects(){
		classes = new ArrayList<EClass>();
		references = new ArrayList<EReference>();
		enums = new ArrayList<EEnum>();
		attributes = new ArrayList<EAttribute>();
		for (Iterator<EObject> i = myEcoreResource.getAllContents(); i.hasNext();){
			EObject obj = i.next();
			if (obj instanceof EClass){
				EClass cls = (EClass) obj;
				classes.add (cls);
			}
			else if (obj instanceof EReference){
				EReference ref = (EReference) obj;
				references.add(ref);
			}
			else if (obj instanceof EEnum){
				EEnum eenum = (EEnum) obj;
				enums.add(eenum);
			}
			else if (obj instanceof EAttribute){
				EAttribute eattr= (EAttribute) obj;				
				if (eattr.getEAttributeType()==EcorePackage.Literals.ESTRING)
					attributes.add(eattr);
			}
			
		}// end of for loop
	}
	
	private class bound{
		private int b;
		public bound(int n){
			b=n;
		}	
		public int getbound(){
			return b;
		}
		public void setbound(int newbound){
			b = newbound;
		}
	}

	public String toString(){
		StringBuffer sb = new StringBuffer();	
		for (EClass cls : bounds.keySet()){
			bound b = bounds.get(cls);
			sb.append(cls.getName()+":"+b.getbound()+"["+b+"]"+"\n");
		}
		return sb.toString();
	}
		
}
