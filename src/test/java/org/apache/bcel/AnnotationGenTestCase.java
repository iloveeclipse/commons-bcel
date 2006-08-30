package org.apache.bcel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apache.bcel.classfile.Annotations;
import org.apache.bcel.classfile.Attribute;
import org.apache.bcel.classfile.RuntimeInvisibleAnnotations;
import org.apache.bcel.classfile.RuntimeVisibleAnnotations;
import org.apache.bcel.classfile.Utility;
import org.apache.bcel.generic.AnnotationEntryGen;
import org.apache.bcel.generic.ClassGen;
import org.apache.bcel.generic.ConstantPoolGen;
import org.apache.bcel.generic.ElementValueGen;
import org.apache.bcel.generic.ElementValuePairGen;
import org.apache.bcel.generic.ObjectType;
import org.apache.bcel.generic.SimpleElementValueGen;

public class AnnotationGenTestCase extends AbstractTestCase
{
	private ClassGen createClassGen(String classname)
	{
		return new ClassGen(classname, "java.lang.Object", "<generated>",
				Constants.ACC_PUBLIC | Constants.ACC_SUPER, null);
	}

	/**
	 * Programmatically construct an mutable annotation (AnnotationGen) object.
	 */
	public void testConstructMutableAnnotation()
	{
		// Create the containing class
		ClassGen cg = createClassGen("HelloWorld");
		ConstantPoolGen cp = cg.getConstantPool();
		// Create the simple primitive value '4' of type 'int'
		SimpleElementValueGen evg = new SimpleElementValueGen(
				ElementValueGen.PRIMITIVE_INT, cp, 4);
		// Give it a name, call it 'id'
		ElementValuePairGen nvGen = new ElementValuePairGen("id", evg,
				cp);
		// Check it looks right
		assertTrue(
				"Should include string 'id=4' but says: " + nvGen.toString(),
				nvGen.toString().indexOf("id=4") != -1);
		ObjectType t = new ObjectType("SimpleAnnotation");
		List elements = new ArrayList();
		elements.add(nvGen);
		// Build an annotation of type 'SimpleAnnotation' with 'id=4' as the
		// only value :)
		AnnotationEntryGen a = new AnnotationEntryGen(t, elements, true, cp);
		// Check we can save and load it ok
		checkSerialize(a, cp);
	}

	public void testVisibleInvisibleAnnotationGen()
	{
		// Create the containing class
		ClassGen cg = createClassGen("HelloWorld");
		ConstantPoolGen cp = cg.getConstantPool();
		// Create the simple primitive value '4' of type 'int'
		SimpleElementValueGen evg = new SimpleElementValueGen(
				ElementValueGen.PRIMITIVE_INT, cp, 4);
		// Give it a name, call it 'id'
		ElementValuePairGen nvGen = new ElementValuePairGen("id", evg,
				cp);
		// Check it looks right
		assertTrue(
				"Should include string 'id=4' but says: " + nvGen.toString(),
				nvGen.toString().indexOf("id=4") != -1);
		ObjectType t = new ObjectType("SimpleAnnotation");
		List elements = new ArrayList();
		elements.add(nvGen);
		// Build a RV annotation of type 'SimpleAnnotation' with 'id=4' as the
		// only value :)
		AnnotationEntryGen a = new AnnotationEntryGen(t, elements, true, cp);
		Vector v = new Vector();
		v.add(a);
		Attribute[] attributes = Utility.getAnnotationAttributes(cp, v);
		boolean foundRV = false;
		for (int i = 0; i < attributes.length; i++)
		{
			Attribute attribute = attributes[i];
			if (attribute instanceof RuntimeVisibleAnnotations)
			{
				assertTrue(((Annotations) attribute).isRuntimeVisible());
				foundRV = true;
			}
		}
		assertTrue("Should have seen a RuntimeVisibleAnnotation", foundRV);
		// Build a RIV annotation of type 'SimpleAnnotation' with 'id=4' as the
		// only value :)
		AnnotationEntryGen a2 = new AnnotationEntryGen(t, elements, false, cp);
		Vector v2 = new Vector();
		v2.add(a2);
		Attribute[] attributes2 = Utility.getAnnotationAttributes(cp, v2);
		boolean foundRIV = false;
		for (int i = 0; i < attributes2.length; i++)
		{
			Attribute attribute = attributes2[i];
			if (attribute instanceof RuntimeInvisibleAnnotations)
			{
				assertFalse(((Annotations) attribute).isRuntimeVisible());
				foundRIV = true;
			}
		}
		assertTrue("Should have seen a RuntimeInvisibleAnnotation", foundRIV);
	}

	private void checkSerialize(AnnotationEntryGen a, ConstantPoolGen cpg)
	{
		try
		{
			String beforeName = a.getTypeName();
			List beforeValues = a.getValues();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataOutputStream dos = new DataOutputStream(baos);
			a.dump(dos);
			dos.flush();
			dos.close();
			byte[] bs = baos.toByteArray();
			ByteArrayInputStream bais = new ByteArrayInputStream(bs);
			DataInputStream dis = new DataInputStream(bais);
			AnnotationEntryGen annAfter = AnnotationEntryGen.read(dis, cpg, a
					.isRuntimeVisible());
			dis.close();
			String afterName = annAfter.getTypeName();
			List afterValues = annAfter.getValues();
			if (!beforeName.equals(afterName))
			{
				fail("Deserialization failed: before type='" + beforeName
						+ "' after type='" + afterName + "'");
			}
			if (a.getValues().size() != annAfter.getValues().size())
			{
				fail("Different numbers of element name value pairs?? "
						+ a.getValues().size() + "!="
						+ annAfter.getValues().size());
			}
			for (int i = 0; i < a.getValues().size(); i++)
			{
				ElementValuePairGen beforeElement = (ElementValuePairGen) a
						.getValues().get(i);
				ElementValuePairGen afterElement = (ElementValuePairGen) annAfter
						.getValues().get(i);
				if (!beforeElement.getNameString().equals(
						afterElement.getNameString()))
				{
					fail("Different names?? " + beforeElement.getNameString()
							+ "!=" + afterElement.getNameString());
				}
			}
		}
		catch (IOException ioe)
		{
			fail("Unexpected exception whilst checking serialization: " + ioe);
		}
	}
}