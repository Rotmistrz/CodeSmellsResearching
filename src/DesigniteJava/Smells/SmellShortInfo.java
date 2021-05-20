package DesigniteJava.Smells;

import java.util.ArrayList;
import java.util.Arrays;

public class SmellShortInfo {
    private String component;
    private ArrayList<Integer> smells;

    public SmellShortInfo(String component) {
        this.component = component;
        this.smells = new ArrayList<Integer>(2);
    }

    public String getComponent() {
        return this.component;
    }

    public ArrayList<Integer> getSmells() {
        return this.smells;
    }

    public String smellsIntoString() {
        StringBuilder builder = new StringBuilder();

        int i = 0;

        for (int smell : this.smells) {
            if (i > 0) {
                builder.append(" ");
            }

            builder.append(smell);

            i++;
        }

        return builder.toString();
    }

    public SmellShortInfo addSmell(int smell) {
        if (!hasSmell(smell)) {
            this.smells.add(smell);
        }

        return this;
    }

    public boolean hasSmell(int smell) {
        return this.smells.contains(smell);
    }
}
