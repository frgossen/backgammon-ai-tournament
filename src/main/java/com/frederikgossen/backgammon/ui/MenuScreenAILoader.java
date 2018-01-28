package com.frederikgossen.backgammon.ui;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.frederikgossen.backgammon.ai.AI;
import com.frederikgossen.backgammon.ai.GreedyAI;
import com.frederikgossen.backgammon.ai.OccupyingAI;
import com.frederikgossen.backgammon.ai.ProtectiveAI;
import com.frederikgossen.backgammon.ai.RandomAI;

public class MenuScreenAILoader {

	@SuppressWarnings("rawtypes")
	private static Class[] defaultAIs = new Class[] { RandomAI.class, GreedyAI.class, ProtectiveAI.class,
			OccupyingAI.class };

	@SuppressWarnings("rawtypes")
	private Map<String, Class> ais;

	@SuppressWarnings("rawtypes")
	public MenuScreenAILoader() {
		ais = new TreeMap<String, Class>();
		addAIs(Arrays.asList(defaultAIs));
	}

	@SuppressWarnings("rawtypes")
	private void addAIs(Iterable<Class> newAIs) {
		for (Class c : newAIs)
			addAI(c);
	}

	@SuppressWarnings("rawtypes")
	private void addAI(Class newAI) {
		ais.put(newAI.getSimpleName(), newAI);
	}

	public void loadFromJarFiles(File[] files) {
		for (File f : files)
			loadFromJarFile(f);
	}

	public void loadFromJarFile(File f) {
		JarFile jarFile = null;
		try {
			jarFile = new JarFile(f);
			Enumeration<JarEntry> jarEntries = jarFile.entries();
			URL[] urls = { new URL("jar:file:" + f.getAbsolutePath() + "!/") };
			URLClassLoader cl = URLClassLoader.newInstance(urls);
			while (jarEntries.hasMoreElements()) {
				JarEntry je = jarEntries.nextElement();
				if (isClassFile(je)) {
					@SuppressWarnings("rawtypes")
					Class c = cl.loadClass(getClassName(je));
					if (isInstantiableAIClass(c))
						addAI(c);
				}
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (jarFile != null)
					jarFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private boolean isClassFile(JarEntry je) {
		return !je.isDirectory() && je.getName().toLowerCase().endsWith(".class");
	}

	private String getClassName(JarEntry je) {
		String name = je.getName();
		int iEnd = name.lastIndexOf(".class");
		String nameWithoutExt = name.substring(0, iEnd);
		String className = nameWithoutExt.replace('/', '.');
		return className;
	}

	private boolean isInstantiableAIClass(@SuppressWarnings("rawtypes") Class c) {
		boolean isAI = AI.class.isAssignableFrom(c);
		boolean isInstantiable = !Modifier.isAbstract(c.getModifiers());
		return isAI && isInstantiable;
	}

	public String[] getNames() {
		String[] names = new String[ais.size()];
		int i = 0;
		for (String n : ais.keySet())
			names[i++] = n;
		return names;
	}

	public AI newInstance(String name) {
		try {
			return (AI) ais.get(name).newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		}
	}
}
