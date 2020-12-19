package lesson7;

import java.lang.reflect.*;
import java.util.*;

public class ObjectExplorer {

    private static Class<?> clazz;

    public static void explore(Object obj) {
        clazz = obj.getClass();
        exploreClass();
        exploreAncestors();
        exploreInterfaces();
        exploreConstructors();
        exploreMethods();
        exploreFields();
    }

    private static void exploreAncestors() {
        StringBuilder sb = new StringBuilder("parents:");
        Class<?> c = clazz;
        while ((c = c.getSuperclass()) != null) {
            sb.append('\n');
            sb.append("  ");
            sb.append(c.getName());
        }
        System.out.println(sb.toString());
    }

    private static void exploreClass() {
        System.out.println(getModifiers(clazz.getModifiers()) + clazz.getSimpleName());
    }

    private static void exploreInterfaces() {
        StringBuilder sb = new StringBuilder("interfaces:");
        Class<?> c = clazz;
        printInterfaces(Arrays.asList(c.getInterfaces()), sb);
        while ((c = c.getSuperclass()) != null)
            printInterfaces(Arrays.asList(c.getInterfaces()), sb);
        System.out.println(sb.toString());
    }

    private static void printInterfaces(List<Class<?>> interfaces, StringBuilder sb) {
        for (Class<?> i : interfaces) {
            sb.append('\n');
            sb.append("  ");
            sb.append(i.getName());
        }
    }

    public static void exploreInterfaces(Object obj) {
        clazz = obj.getClass();
        exploreInterfaces();
    }

    private static String getModifiers(int modifiers){
        StringBuilder sb = new StringBuilder();
        if (Modifier.isFinal(modifiers))
            sb.append("final ");
        if (Modifier.isAbstract(modifiers))
            sb.append("abstract ");
        if (Modifier.isStatic(modifiers))
            sb.append("static ");

        if (Modifier.isPrivate(modifiers))
            sb.append("private ");
        else if (Modifier.isProtected(modifiers))
            sb.append("protected ");
        else if (Modifier.isPublic(modifiers))
            sb.append("public ");

        return sb.toString();
    }

    private static void exploreParameter(Parameter p, StringBuilder sb){
        if (sb.length() != 0)
            sb.append(", ");
        sb.append(p.getType().getSimpleName());
        sb.append(" ");
        sb.append(p.getName());
    }

    private static String getParameters(Parameter[] parameters) {
        StringBuilder sb = new StringBuilder();
        for (Parameter p: parameters) {
            exploreParameter(p, sb);
        }
        return sb.toString();
    }

    public static void exploreConstructors() {
        List<Constructor<?>> constructors = Arrays.asList(clazz.getConstructors());
        if (constructors.size() > 0) {
            StringBuilder sb = new StringBuilder("constructors:");
            for (Constructor<?> constructor : constructors) {
                sb.append('\n');
                sb.append("  ");
                sb.append(getModifiers(constructor.getModifiers()));
                sb.append(constructor.getName());
                sb.append("(");
                sb.append(getParameters(constructor.getParameters()));
                sb.append(")");
            }
            System.out.println(sb.toString());
        }
    }

    public static void exploreMethods() {
        List<Method> methods = Arrays.asList(clazz.getDeclaredMethods());

        if (methods.size() > 0) {
            StringBuilder sb = new StringBuilder("methods: ");
            for (Method method : methods) {
                sb.append('\n');
                sb.append("  ");
                sb.append(getModifiers(method.getModifiers()));
                sb.append(method.getReturnType().getSimpleName());
                sb.append(" ");
                sb.append(method.getName());
                sb.append("(");
                sb.append(getParameters(method.getParameters()));
                sb.append(")");
            }
            System.out.println(sb.toString());
        }
    }

    public static void exploreFields() {
        Class<?> c = clazz;
        List<Field> fields = new ArrayList<>(Arrays.asList(c.getDeclaredFields()));
        while ((c = c.getSuperclass()) != null)
            fields.addAll(new ArrayList<>(Arrays.asList(c.getDeclaredFields())));
        if (fields.size() > 0) {
            StringBuilder sb = new StringBuilder("fields:");
            for (Field f : fields) {
                sb.append('\n');
                sb.append("  ");
                sb.append(getModifiers(f.getModifiers()));
                sb.append(f.getType().getSimpleName());
                sb.append(" ");
                sb.append(f.getName());
                sb.append(";");
            }
            System.out.println(sb.toString());
        }
    }
}
